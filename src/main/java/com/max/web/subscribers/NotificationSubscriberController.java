package com.max.web.subscribers;

import com.max.coaching.db.model.NotificationTemplate;
import com.max.coaching.db.repositories.NotificationTemplateRepository;
import com.max.messaging.MessageUtils;
import com.max.messaging.publish.InvalidMessageException;
import com.max.messaging.publish.UserActivityTopicPublisher;
import com.max.messaging.subscribe.MaxMessageListener;
import com.max.services.MailService;
import com.max.web.model.HandlerResults;
import com.max.web.model.MaxMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * This subscriber deals with keeping Associates' tasks in sync with activity performed by the subscriber.
 * Basically this means that for any Task-related event, a pending Task is searched for which relates to the
 * event performed, or created if it does not already exist, and marked complete
 */
@Controller
public class NotificationSubscriberController extends MaxMessageListener
{
    private MailService mailService;

    @Autowired
    UserActivityTopicPublisher userActivityTopicPublisher;

    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;

    Logger log = Logger.getLogger(NotificationSubscriberController.class);

    /**
     * REST controller for testNotificationEngine
     *
     * @param version {@code String}
     * @param lang {@code String}
     * @param country {@code String}
     * @param message {@link com.max.web.model.MaxMessage}
     * @return {@link com.max.web.model.HandlerResults}
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/testNotificationEngine", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onTestMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                              @NotNull @RequestBody MaxMessage message)
    {
        return onTest(message);
    }

    /**
     * REST controller for runNotificationEngine
     *
     * @param version {@code String}
     * @param lang {@code String}
     * @param country {@code String}
     * @param message {@link com.max.web.model.MaxMessage}
     * @return {@link com.max.web.model.HandlerResults}
     */

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/runNotificationEngine", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public HandlerResults onMessage(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                          @NotNull @RequestBody MaxMessage message)
    {
        log.info("Running NotificationSubscriberController.onMessage : " + message);
        HandlerResults results = new HandlerResults();
        results.setMessage("Processing notifications");
        results.setSuccess(true);

        log.info("Running NotificationSubscriber");
        return onMessage(message);
    }

    /**
     * Perform the test stuff on message
     *
     * @param message {@link com.max.web.model.MaxMessage}
     * @return {@link com.max.web.model.HandlerResults}
     */
    @Override
    public HandlerResults onTest(MaxMessage message)
    {
        log.info("Running NotificationSubscriberController.onTestMessage : " + message);
        HandlerResults results = new HandlerResults();
        results.setMessage("NotificationSubscriberController test: " + message.toString());
        results.setSuccess(true);

        return results;
    }

    /**
     * Perform the production stuff on message
     *
     * @param message {@link com.max.web.model.MaxMessage}
     * @return {@link com.max.web.model.HandlerResults}
     */
    @Override
    public HandlerResults onMessage(MaxMessage message)
    {
        log.info("NotificationSubscriber subscribes to this message: " + message);

        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findByEventName(message.getVerb());

        StringBuilder errors = new StringBuilder("Errors on running NotificationSubscriber: ");
        boolean hasErrors = false;

        for (NotificationTemplate curTemplate : notificationTemplates)
        {
            log.info("Processing NotificationTemplate " + curTemplate.toString());

            try
            {
                // TODO: need to glean the recipient address by the context of the message, or something
                getMailService().sendMail("niqui@max.com", message.getVerb(),
                        (MessageUtils.getNotificationMessage(message.getVerb() + "_" + curTemplate.getRecipientType(), message.getLanguage())));

                if (!message.getVerb().equalsIgnoreCase("SendingEmail"))
                {
                    MaxMessage sendingEmailMessage = new MaxMessage();
                    sendingEmailMessage.setVerb("SendingEmail");
                    userActivityTopicPublisher.sendMessage(sendingEmailMessage);
                }
            }
            catch (IOException e)
            {
                hasErrors = true;
                errors.append("Could not send the notification email");
            }
            catch (NamingException | JMSException | InvalidMessageException e)
            {
                hasErrors = true;
                errors.append("Could not publish 'SendingEmail' message: ").append(e.getMessage());
            }
        }

        return new HandlerResults(hasErrors ? errors.toString() : "NotificationSubscriber complete", !hasErrors);
    }

    public MailService getMailService()
    {
        return mailService;
    }

    @SuppressWarnings("unused") // Injected
    public void setMailService(MailService mailService)
    {
        this.mailService = mailService;
    }
}
