package com.max.web.model;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.model.TaskTemplate;
import com.max.messaging.MessageUtils;

import java.util.Date;

/**
 * Web Service Data for {code AssociateTasks}
 */
public class AssociateTaskBean extends DataModelBean<AssociateTask>
{
    public TaskBean task;
    public Integer associateId;
    public Integer subjectId;
    public String subjectObjectType;
    public Date completedDate;
    public Date createdDate;
    public Date dueDate;
    public boolean ignored;

    public AssociateTaskBean()
    {
        super();
    }

    public static AssociateTaskBean getInstance(AssociateTask data, String lang)
    {
        AssociateTaskBean bean = new AssociateTaskBean();
        bean.loadFrom(data, lang);

        return bean;
    }

    @Override
    public boolean loadFrom(AssociateTask data, String lang)
    {
        task = new TaskBean();
        task.loadFrom(data.getTask(), lang);
        
        associateId = data.getAssociateId();
        subjectId = data.getSubjectId();
        subjectObjectType = data.getSubjectObjectType();
        completedDate = data.getCompletedDate();
        createdDate = data.getCreatedDate();
        ignored = data.isIgnored() == null ? false : data.isIgnored();
        dueDate = data.getDueDate();
        
        return true;
    }

    private class TaskBean extends DataModelBean<TaskTemplate>
    {
        public String description;
        public String url;
        public String detailUrl;
        public String formUrl;

        @Override
        public boolean loadFrom(TaskTemplate coachingMaxElement, String lang)
        {
            description = MessageUtils.getTaskDescription(coachingMaxElement.getDescriptionKey(), lang);
            url = coachingMaxElement.getUrl();
            detailUrl = coachingMaxElement.getDetailUrl();
            formUrl = coachingMaxElement.getFormUrl();

            return true;
        }
    }
}
