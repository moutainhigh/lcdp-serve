//package com.redxun.bpm.listener.taskcreate;
//
//import com.redxun.bpm.activiti.config.UserGroupConfig;
//import com.redxun.bpm.activiti.config.UserTaskConfig;
//import com.redxun.bpm.activiti.event.TaskCreateApplicationEvent;
//import com.redxun.bpm.core.entity.BpmInstCc;
//import com.redxun.bpm.core.entity.IExecutionCmd;
//import com.redxun.bpm.core.service.BpmInstCcServiceImpl;
//import com.redxun.bpm.core.service.TaskExecutorService;
//import com.redxun.bpm.listener.BaseCopyListener;
//import com.redxun.bpm.util.ProcessHandleUtil;
//import org.activiti.engine.impl.persistence.entity.TaskEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
///**
// * 在流程启动时处理抄送。
// * 1.处理配置的抄送。
// */
//@Component
//@Order(3)
//public class CopyListener extends BaseCopyListener implements ApplicationListener<TaskCreateApplicationEvent> {
//
//    @Autowired
//    TaskExecutorService taskExecutorService;
//    @Resource
//    BpmInstCcServiceImpl bpmInstCcService;
//
//    @Override
//    public void onApplicationEvent(TaskCreateApplicationEvent taskCreateApplicationEvent) {
//        UserTaskConfig userTaskConfig= taskCreateApplicationEvent.getConfig();
//
//        TaskEntity taskEntity= (TaskEntity) taskCreateApplicationEvent.getSource();
//        Map<String,Object> vars=taskEntity.getVariables();
//
//        BpmInstCc instCc=buildBpmInstCc(vars,taskEntity);
//
//        // 构建抄送实例
//        List<UserGroupConfig> copyConfig=getUserConfigs(userTaskConfig,taskCreateApplicationEvent.getProcessConfig(),true);
//
//        //处理配置的抄送。
//        boolean handConfigCopy=handConfigCopy(copyConfig,vars,instCc);
//
//        if(handConfigCopy){
//            bpmInstCcService.insert(instCc);
//        }
//    }
//
//
//
//
//}
