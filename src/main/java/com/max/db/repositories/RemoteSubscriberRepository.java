package com.max.db.repositories;

import com.max.db.model.RemoteSubscriber;
import com.max.messaging.MaxTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;


/**
 * Access NotificationTemplates
 */
public interface RemoteSubscriberRepository extends JpaRepository<RemoteSubscriber, Integer>
{
    public RemoteSubscriber findByTopicAndName(MaxTopic topic, String name);

    public Collection<RemoteSubscriber> findByAutoRegister(boolean autoRegister);
}
