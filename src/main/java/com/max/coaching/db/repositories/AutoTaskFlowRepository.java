package com.max.coaching.db.repositories;

import com.max.coaching.db.model.AutoTaskFlow;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Access/modify {@code AutoTaskFlow} data
 */
public interface AutoTaskFlowRepository extends JpaRepository<AutoTaskFlow, Integer>
{
    public List<AutoTaskFlow> findByTriggerTaskDescriptionKey(@NotNull String taskDescriptionKey);
}
