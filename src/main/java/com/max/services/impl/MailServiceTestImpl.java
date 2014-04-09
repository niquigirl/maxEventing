package com.max.services.impl;

import com.max.services.MailService;

import java.io.File;
import java.io.IOException;

/**
 * Test implementation
 */
public class MailServiceTestImpl implements MailService
{

    @Override
    public void sendErrorEmail(String subject, String body)
    {
        System.out.println("ERROR Email being delivered. Subject: " + subject + " : : : Body: " + body);
    }

    @Override
    public void sendErrorEmail(String subject, String body, Exception e)
    {
        System.out.println("ERROR Email being delivered. Subject: " + subject + " : : : Body: " + body + " : : : Exception: " + e);
    }

    @Override
    public void sendErrorEmail(String subject, Exception e)
    {
        System.out.println("ERROREmail being delivered. Subject: " + subject + " : : : Exception: " + e);
    }

    @Override
    public void sendMail(String to, String subject, String body) throws IOException
    {
        System.out.println("Email being delivered. Subject: " + subject + " : : : Body: " + body);
    }

    @Override
    public void sendMail(String to, String subject, String body, File... attachments) throws IOException
    {
        System.out.println("Email being delivered. Subject: " + subject + " : : : Body: " + body + " : : : With files: " + (attachments == null ? "null" : attachments.length));
    }
}
