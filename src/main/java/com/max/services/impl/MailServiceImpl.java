package com.max.services.impl;

import com.max.services.MailService;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

public class MailServiceImpl implements MailService
{
    private Logger log = Logger.getLogger(MailServiceImpl.class);
/*
smtp.host=smtp.gmail.com
smtp.errors.recipient=maxinternationaldev@gmail.com
smtp.auth.password=F00b@r123
smtp.auth.username=dev@max.com
smtp.default.sender.email=dev@max.com
smtp.default.sender.name=Max IT

 */
    private String mailhost = "smtp.gmail.com";
    private String errorEmailAddress = "neastman@max.com";
    private String username = "dev@max.com";
    private String password = "F00b@r123";

    private String senderName = "Max Messaging - test";
    private String senderEmail = "test@max.com";

    @Override
    public void sendErrorEmail(String subject, String body)
    {
        try
        {
            this.sendMail(errorEmailAddress, subject, body, new File[0]);
        }
        catch (IOException e)
        {
            log.error("Can't send error email!! \n" + body, e);
        }
    }

    @Override
    public void sendErrorEmail(String subject, String body, Exception e)
    {
        try
        {
            this.sendMail(errorEmailAddress, subject, body + "\n\n" + Arrays.toString(e.getStackTrace()), new File[0]);
        }
        catch (IOException e1)
        {
            log.error("COULD NOT SEND ERROR MESSAGE ON EXCEPTION.  THIS IS NOT A TEST!", e);
        }
    }

    @Override
    public void sendErrorEmail(String subject, Exception e)
    {
        try
        {
            this.sendMail(errorEmailAddress, subject, Arrays.toString(e.getStackTrace()), new File[0]);
        }
        catch (IOException e1)
        {
            log.error("COULD NOT SEND ERROR MESSAGE ON EXCEPTION.  THIS IS NOT A TEST!", e);
        }
    }

    @Override
    public void sendMail(String to, String subject, String body) throws IOException
    {
        sendMail(to, subject, body, new File[0]);
    }

/*
    @Override
    public void sendMail(Set<String> to, String subject, String body) throws IOException
    {
        sendMail(to, subject, body, new File[0]);
    }

*/
    @Override
    public void sendMail(String to, String subject, String body, File... attachments) throws IOException
    {
        log.info("Emailing " + subject + " to " + to + " from " + senderEmail);
        try
        {
            Properties props = System.getProperties();
            // XXX - could use Session.getTransport() and Transport.connect()
            // XXX - assume we're using SMTP
            if (mailhost != null)
            {
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.port", "587");
            }

            // Get a Session object
            Session session = Session.getInstance(props, new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(username, password);
                }
            });

            // construct the message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail, senderName));

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            msg.setSubject(subject);
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(format(msg, body));
            bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(format(msg, body), "text/html")));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            if (attachments != null)
            {
                for (File attachment1 : attachments)
                {
                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.attachFile(attachment1);
                    multipart.addBodyPart(attachment);
                }
            }

            msg.setHeader("X-Mailer", "sendhtml");
            msg.setSentDate(new Date());
            msg.setContent(multipart);

            // send the thing off
            Transport.send(msg);
        }
        catch (IOException | MessagingException e)
        {
            log.error("Email could not be delivered!", e);
            log.debug(body);
            throw new IOException(e);
        }
    }

    public String format(Message msg, String body) throws MessagingException, IOException
    {
        String subject = msg.getSubject();

        return "<HTML>" + "<HEAD>" + "<TITLE>" + subject + "</TITLE>" + "</HEAD>" + "<BODY>" + "<H1>" + subject + "</H1>" + body + "</BODY>" + "</HTML>";
    }

    @SuppressWarnings("unused")
    public String getMailhost()
    {
        return mailhost;
    }

    public void setMailhost(String mailhost)
    {
        this.mailhost = mailhost;
    }

    @SuppressWarnings("unused")
    public String getErrorEmailAddress()
    {
        return errorEmailAddress;
    }

    public void setErrorEmailAddress(String errorEmailAddress)
    {
        this.errorEmailAddress = errorEmailAddress;
    }

    @SuppressWarnings("unused")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @SuppressWarnings("unused")
    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    @SuppressWarnings("unused")
    public String getSenderEmail()
    {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail)
    {
        this.senderEmail = senderEmail;
    }
}
