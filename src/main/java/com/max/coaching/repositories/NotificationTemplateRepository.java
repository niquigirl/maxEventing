package com.max.coaching.repositories;

import com.max.coaching.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Access NotificationTemplates
 */
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer>
{
}
