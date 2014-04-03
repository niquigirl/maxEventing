package com.max.coaching.db.repositories;

import com.max.coaching.db.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Access NotificationTemplates
 */
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer>
{
}
