package com.max.web.services;

import com.max.coaching.db.model.AssociateTask;
import com.max.coaching.db.repositories.AssociateTaskRepository;
import com.max.web.model.AssociateTaskBean;
import com.max.web.model.MaxDataResults;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Web Service for dealing with Tasks ({@code TaskTemplate}s, {@code AssociateTask}s
 */
@Controller
public class TaskService extends MaxWebService
{
    @Autowired
    AssociateTaskRepository repository;

    Logger log = Logger.getLogger(TaskService.class);

    @SuppressWarnings("unused")
    @RequestMapping(value = "{version}/{lang}/{country}/getAssociateTasks", method = RequestMethod.GET)
    public
    @ResponseBody
    MaxDataResults<AssociateTaskBean> getAssociateTasks(@PathVariable("version") String version, @PathVariable("lang") String lang, @PathVariable("country") String country,
                                                        @RequestParam String associateId,
                                                        @RequestParam(required = false) Boolean completed)
            throws ServletException, IOException
    {
        log.debug("Servicing request to get Associate Tasks for associate  :  " + associateId);
        MaxDataResults<AssociateTaskBean> results = new MaxDataResults<>("getAssociateTasks",
                new AbstractMap.SimpleEntry<>("associateId", associateId));
        results.addRequestParam("completed", completed);

        try
        {
            Integer customerIdVal = getInt("associateId", associateId);
            List<AssociateTask> associateTasks;

            if (completed == null)
            {
                associateTasks = repository.findByAssociateId(customerIdVal);
            }
            else if (completed)
                associateTasks = repository.findByAssociateIdAndCompletedDateIsNotNull(customerIdVal);
            else
                associateTasks = repository.findByAssociateIdAndCompletedDateIsNull(customerIdVal);

            results.setData(convertToAssociateTaskBean(associateTasks, lang));
        }
        catch (ParamsValidationException e)
        {
            e.printStackTrace();
            results.addError(e.getMessage());
        }

        return results;
    }

    /**
     * Create a List of UI/WS Beans from a list of DB objects
     *
     * @param associateTasks {@code List&lt;AssociateTask&gt;}
     * @param lang {@code String}
     * @return {@code List&lt;AssociateTaskBean&gt;}
     */
    private List<AssociateTaskBean> convertToAssociateTaskBean(List<AssociateTask> associateTasks, String lang)
    {
        if (associateTasks == null)
            return Collections.emptyList();

        List<AssociateTaskBean> beans = new LinkedList<>();
        for (AssociateTask data : associateTasks)
        {
            beans.add(AssociateTaskBean.getInstance(data, lang));
        }

        return beans;
    }

}
