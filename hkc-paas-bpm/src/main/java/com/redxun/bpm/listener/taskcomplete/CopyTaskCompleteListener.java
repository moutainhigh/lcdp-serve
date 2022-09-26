//package com.redxun.bpm.listener.taskcomplete;
//
//import com.redxun.api.org.IOrgService;
//import com.redxun.bpm.activiti.config.UserGroupConfig;
//import com.redxun.bpm.activiti.config.UserTaskConfig;
//import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
//import com.redxun.bpm.core.entity.BpmInstCc;
//import com.redxun.bpm.core.entity.IExecutionCmd;
//import com.redxun.bpm.core.service.BpmInstCcServiceImpl;
//import com.redxun.bpm.core.service.TaskExecutorService;
//import com.redxun.bpm.listener.BaseCopyListener;
//import com.redxun.bpm.util.ProcessHandleUtil;
//import org.activiti.engine.impl.persistence.entity.TaskEntity;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
///**
// *  功能说明：
// *   1.处理在上下文的抄送人。
// *   2.处理配置结束的抄送。
// */
//@Component
//@Order(4)
//public class CopyTaskCompleteListener extends BaseCopyListener implements ApplicationListener<TaskCompleteApplicationEvent> {
//
//    @Resource
//    BpmInstCcServiceImpl bpmInstCcService;
//    @Resource
//    TaskExecutorService taskExecutorService;
//    @Resource
//    IOrgService orgService;
//
//
//    @Override
//    public void onApplicationEvent(TaskCompleteApplicationEvent taskCompleteApplicationEvent) {
//
//        UserTaskConfig userTaskConfig= taskCompleteApplicationEvent.getConfig();
//        List<UserGroupConfig> copyConfig=getUserConfigs(userTaskConfig,taskCompleteApplicationEvent.getProcessConfig(),false);
//        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
//        TaskEntity taskEntity= (TaskEntity) taskCompleteApplicationEvent.getSource();
//
//        Map<String,Object> vars=taskEntity.getVariables();
//
//        BpmInstCc instCc=buildBpmInstCc(vars,taskEntity);
//        //处理配置的抄送
//        boolean handConfigCopy=handConfigCopy(copyConfig,vars,instCc);
//        //处理上下文的抄送。
//        boolean handContextCopy=handContextCopy(cmd,instCc);
//
//        if(handConfigCopy ||  handContextCopy){
//            bpmInstCcService.insert(instCc);
//        }
//
//    }
//}
