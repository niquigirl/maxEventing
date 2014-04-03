package com.max.coaching.db.repositories;

import com.max.coaching.db.model.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Access NotificationTemplates
 */
public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, Integer>
{
    public TaskTemplate findByDescriptionKey(String descriptionKey);
}
