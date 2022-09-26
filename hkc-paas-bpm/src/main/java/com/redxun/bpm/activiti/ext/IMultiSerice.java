package com.redxun.bpm.activiti.ext;

import com.redxun.dto.bpm.TaskExecutor;
import org.activiti.engine.delegate.DelegateExecution;

import java.util.List;

/**
 * 多实例任务。
 */
public interface IMultiSerice {


    List<TaskExecutor> getExcutors(DelegateExecution execution);

    boolean isComplete(DelegateExecution execution);

}
