package com.max.coaching.db.repositories;

import com.max.coaching.db.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Access NotificationTemplates
 */
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer>
{
    List<NotificationTemplate> findByEventName(String eventName);
}
