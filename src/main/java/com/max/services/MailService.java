package com.max.services;

import javax.mail.Service;
import java.io.File;
import java.io.IOException;

/**
 * Mail service methods
 */
public interface MailService
{
    public void sendErrorEmail(String subject, String body);
    public void sendErrorEmail(String subject, String body, Exception e);
    public void sendErrorEmail(String subject, Exception e);

    public void sendMail(String to, String subject, String body) throws IOException;
//    public void sendMail(Set<String> to, String subject, String body) throws IOException;

    public void sendMail(String to, String subject, String body, File... attachments) throws IOException;
//    public void sendMail(Set<String> to, String subject, String body, File... attachments) throws IOException;
}
