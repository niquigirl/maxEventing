package com.max.messaging;


import java.util.Locale;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *
 * Gather appropriate messages per division and survey usage (display template or instance)
 */
public class MessageUtils
{

    public static String getNotificationMessage(String eventName, String lang)
    {
        ResourceBundleMessageSource msgBundle = new ResourceBundleMessageSource();
        msgBundle.setBasename("notificationMessages");

        return msgBundle.getMessage(eventName, null, Locale.forLanguageTag(lang));
    }

    public static String getWebFeedMessage(String eventName, String lang)
    {
        ResourceBundleMessageSource msgBundle = new ResourceBundleMessageSource();
        msgBundle.setBasename("webFeedMessages");

        return msgBundle.getMessage(eventName, null, Locale.forLanguageTag(lang));
    }

    public static String getTaskDescription(String messageKey, String lang)
    {
        ResourceBundleMessageSource msgBundle = new ResourceBundleMessageSource();
        msgBundle.setBasename("taskDescriptions");

        return msgBundle.getMessage(messageKey, null, Locale.forLanguageTag(lang));
    }

}
