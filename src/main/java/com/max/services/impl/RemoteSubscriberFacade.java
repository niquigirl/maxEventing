package com.max.services.impl;

import com.max.messaging.subscribe.MaxMessageListener;
import com.max.web.model.HandlerResults;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.wso2.andes.client.message.JMSTextMessage;

import javax.jms.JMSException;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Local subscriber serving as a facade for each registered remote subscriber
 */
@Service
@Scope("prototype")
public class RemoteSubscriberFacade extends MaxMessageListener
{
    private Logger log = Logger.getLogger(RemoteSubscriberFacade.class);

    private String serviceUrl;
    private Integer timeoutMS;

    /**
     * <p>
     * Call remote service on Message received
     * </p>
     *
     * @param activityMessage {@link com.max.web.model.DefaultActivityMessage}
     */
    @Override
    public HandlerResults onMessage(JMSTextMessage activityMessage)
    {
        getLogger().debug(this + " _ " + getName() + ": Handling message for remote subscriber");
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30000 : getTimeoutMS());

        try
        {
            getLogger().debug("RemoteSubscriberFacade sending message: " + activityMessage.getText());
/*
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final JSONObject request = new JSONObject(activityMessage.getText());
            getLogger().debug("JSONObject: " + request);
            final HandlerResults handlerResults = restTemplate.postForObject(getServiceUrl(), new JSONObject(activityMessage.getText()), HandlerResults.class);
*/
            final HandlerResults handlerResults = forwardMessage(activityMessage.getText(), getServiceUrl());
            final String message = getName() + ": Received the following results from " + getServiceUrl() + " : " + handlerResults;
            getLogger().debug(message);
            return new HandlerResults(message, true);
        }
        catch (RestClientException e)
        {
            final String message = "Could not send message to remote subscriber URL " + getServiceUrl() + " : " + e.getMessage();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
        catch (JMSException e)
        {
            final String message = "Could not get message text from message " + activityMessage + " : " + e.getMessage();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
    }

    /**
     *
     * @return
     */
    private HandlerResults forwardMessage(String message, String serviceUrl)
    {
        try
        {
            final SSLSocketFactory sslSocketFactory = disableCertificateValidation();
            URL url = new URL(serviceUrl);
            HttpsURLConnection.setFollowRedirects(true);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            conn.setSSLSocketFactory(sslSocketFactory);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
            conn.setRequestMethod("POST");
            conn.setAllowUserInteraction(false);

            // POST the data
            //
            OutputStreamWriter out = new java.io.OutputStreamWriter(conn.getOutputStream());
            out.write(message);
            out.flush();
            out.close();

            // read the response
            //
            String response;

            if ((response = readResponse(conn)) != null)
            {
                ObjectMapper mapper = new ObjectMapper();
                final ObjectReader reader = mapper.reader(HandlerResults.class);

                return reader.readValue(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new HandlerResults("Could not place call to publish message: " + e.getMessage(), false);
        }

        return new HandlerResults("Not sure what the problem was, but it didn't succeed and it didn't fail in any predictable ways to publish a message", false);
    }

    /**
     *
     * @param con
     * @return
     */
    private String readResponse(HttpURLConnection con)
    {
        if (con != null)
        {
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder response = new StringBuilder();
                String input;

                while ((input = br.readLine()) != null)
                {
                    response.append(input);
                }

                br.close();

                if (response.length() <= 0)
                {
                    System.err.println("Response is empty.");
                    return null;
                }

                return response.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    private SSLSocketFactory disableCertificateValidation() throws Exception
    {
        // Create a trust manager that does not validate certificate chains
        //
        TrustManager[] myTrustMgr = new TrustManager[]{new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType)
            {
                // ignore
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType)
            {
                // ignore all certs
            }
        }};

        // Install the all-trusting trust manager
        //
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, myTrustMgr, new SecureRandom());
            final SSLSocketFactory socketFactory = sc.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);

            return socketFactory;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }


    public String getServiceUrl()
    {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl)
    {
        this.serviceUrl = serviceUrl;
    }

    public Integer getTimeoutMS()
    {
        return timeoutMS;
    }

    public void setTimeoutMS(Integer timeoutMS)
    {
        this.timeoutMS = timeoutMS;
    }

    public Logger getLogger()
    {
        return log;
    }

}

