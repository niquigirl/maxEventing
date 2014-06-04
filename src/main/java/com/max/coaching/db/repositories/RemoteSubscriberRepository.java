package com.max.coaching.db.repositories;

import com.max.coaching.db.model.RemoteSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Access NotificationTemplates
 */
public interface RemoteSubscriberRepository extends JpaRepository<RemoteSubscriber, Integer>
{
    public RemoteSubscriber findByName(String name);

}
