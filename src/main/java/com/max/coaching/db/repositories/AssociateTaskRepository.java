package com.max.coaching.db.repositories;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Access NotificationTemplates
 */
public interface AssociateTaskRepository extends JpaRepository<AssociateTask, Integer>
{
    List<AssociateTask> findByAssociateIdAndSubjectIdAndTaskInOrderByCreatedDateDesc(Integer associateId, Integer subjectId, List<TaskTemplate> tasks);

    List<AssociateTask> findByAssociateIdAndTaskDescriptionKey(Integer assigneeId, String descriptionKey);
    List<AssociateTask> findByAssociateIdAndTaskDescriptionKeyAndSubjectId(Integer assigneeId, String descriptionKey, Integer subjectId);
    List<AssociateTask> findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNull(Integer assigneeId, String descriptionKey, Integer subjectId);
    List<AssociateTask> findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNull(Integer assigneeId, String descriptionKey);
    List<AssociateTask> findByAssociateIdAndTaskDescriptionKeyAndCompletedDateIsNotNull(Integer assigneeId, String descriptionKey);
    List<AssociateTask> findByAssociateIdAndTaskDescriptionKeyAndSubjectIdAndCompletedDateIsNotNull(Integer assigneeId, String descriptionKey, Integer subjectId);

    List<AssociateTask> findByAssociateIdAndCompletedDateIsNotNull(Integer customerId);

    List<AssociateTask> findByAssociateId(Integer customerIdVal);

    List<AssociateTask> findByAssociateIdAndCompletedDateIsNull(Integer customerIdVal);
}
