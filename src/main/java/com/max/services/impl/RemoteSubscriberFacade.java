package com.max.services.impl;

import com.max.services.MaxMessageListener;
import com.max.web.model.HandlerResults;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
    public HandlerResults onMessage(String activityMessage)
    {
        getLogger().debug(this + " _ " + getName() + ": Handling message for remote subscriber");
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(getTimeoutMS() == null ? 30000 : getTimeoutMS());

        try
        {
            getLogger().debug("RemoteSubscriberFacade sending message: " + activityMessage + " to " + getServiceUrl());
/*
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            final JSONObject request = new JSONObject(activityMessage.getText());
            getLogger().debug("JSONObject: " + request);
            final HandlerResults handlerResults = restTemplate.postForObject(getServiceUrl(), new JSONObject(activityMessage.getText()), HandlerResults.class);
*/
            final HandlerResults handlerResults = forwardMessage(activityMessage, getServiceUrl());
            final String message = getName() + ": Received the following results from " + getServiceUrl() + " : " + handlerResults;
            getLogger().debug(message);
            return new HandlerResults(message, handlerResults.isSuccess());
        }
        catch (RestClientException e)
        {
            final String message = "Could not send message to remote subscriber URL " + getServiceUrl() + " : " + e.getMessage();
            getLogger().error(message, e);
            return new HandlerResults(message, false);
        }
    }

    /**
     * Front method to determine how to call the registrar's service Url and call the appropriate method
     * depending on the URL protocol
     *
     * @return {@link com.max.web.model.HandlerResults}
     */
    private HandlerResults forwardMessage(String message, String serviceUrl)
    {
        try
        {
            URL url = new URL(serviceUrl);

            HttpURLConnection conn;

            if (url.getProtocol().equalsIgnoreCase("https"))
            {
                disableCertificateValidation();
                conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
            }
            else
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
            }

            String response = forwardMessage(conn, message);
            conn.disconnect();

            return getHandlerResults(url, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new HandlerResults("Could not place call to publish message: " + e.getMessage(), false);
        }
    }

    /**
     * Push the message through to the opened connection
     *
     * @param conn {@code URLConnection}
     * @param message {@code String}
     * @return {@code String} which is assumed to be a stringified JSON object
     * @throws Exception
     */
    private String forwardMessage(URLConnection conn, String message) throws Exception
    {
        HttpURLConnection.setFollowRedirects(true);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf8");

        try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream()))
        {
            out.write(message);
            out.flush();
        }

        return readResponse(conn);
    }

    /**
     * Parse a {@link com.max.web.model.HandlerResults} from the String provided
     *
     * @param url {@code URL}
     * @param result {@code STring}
     * @return {@link com.max.web.model.HandlerResults}
     */
    private HandlerResults getHandlerResults(URL url, String result)
    {
        if (!StringUtils.isBlank(result))
        {
            try
            {
                ObjectMapper mapper = new ObjectMapper();
                final ObjectReader reader = mapper.reader(HandlerResults.class);

                return reader.readValue(result);
            }
            catch (IOException e)
            {
                log.warn("IOException attempting to deserialize results from " + url.toString() + ".  <result>" + result + "</result>");
            }
        }

        return new HandlerResults("Could not deserialize results from " + url.toString() + ".  <result>" + result + "</result>", false);
    }

    /**
     * Read the response from a call to forward a message to the registrar's service Url
     *
     * @param con {@code HttpURLConnection}
     * @return String which may be {@code null} representing an empty or no response
     */
    private String readResponse(URLConnection con)
    {
        if (con != null)
        {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream())))
            {
                StringBuilder response = new StringBuilder();
                String input;

                while ((input = br.readLine()) != null)
                {
                    response.append(input);
                }

                return response.length() <= 0 ? null : response.toString();
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

