//package com.redxun.bpm.listener.processend;
//
//
//import com.redxun.bpm.activiti.config.ProcessConfig;
//import com.redxun.bpm.activiti.config.UserGroupConfig;
//import com.redxun.bpm.activiti.event.ProcessCompletedEvent;
//import com.redxun.bpm.core.entity.BpmDef;
//import com.redxun.bpm.core.entity.BpmInstCc;
//import com.redxun.bpm.core.entity.BpmInstVars;
//import com.redxun.bpm.core.entity.IExecutionCmd;
//import com.redxun.bpm.core.service.BpmDefService;
//import com.redxun.bpm.core.service.BpmInstCcServiceImpl;
//import com.redxun.bpm.listener.BaseCopyListener;
//import com.redxun.bpm.util.ProcessHandleUtil;
//import com.redxun.common.base.entity.IUser;
//import com.redxun.common.tool.IdGenerator;
//import com.redxun.common.utils.ContextUtil;
//import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.Map;
//
///**
// * 处理抄送。
// * 1. 处理配置的人员抄送。
// */
//@Component
//@Order(2)
//public class CopyProcessEndListener extends BaseCopyListener implements ApplicationListener<ProcessCompletedEvent> {
//
//    @Resource
//    BpmInstCcServiceImpl bpmInstCcService;
//    @Resource
//    BpmDefService bpmDefService;
//
//    @Override
//    public void onApplicationEvent(ProcessCompletedEvent processCompletedEvent) {
//
//        ProcessConfig processConfig=(ProcessConfig) processCompletedEvent.getSource();
//
//        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
//        List<UserGroupConfig> copyConfig = processConfig.getProcessEndCopyConfig();
//        ExecutionEntity entity = processCompletedEvent.getEntity();
//
//        Map<String,Object> vars=entity.getVariables();
//
//        BpmInstCc instCc = buildBpmCc(vars,entity);
//        //处理配置的抄送
//        boolean handConfigCopy=handConfigCopy(copyConfig,vars,instCc);
//        //处理上下文的抄送。
//        boolean handContextCopy=handContextCopy(cmd,instCc);
//
//        if(handConfigCopy || handContextCopy){
//            bpmInstCcService.insert(instCc);
//        }
//
//    }
//
//    /**
//     * 构建抄送。
//     * @param vars
//     * @param entity
//     * @return
//     */
//    private BpmInstCc buildBpmCc(Map<String,Object> vars,ExecutionEntity entity){
//        IUser currentUser= ContextUtil.getCurrentUser();
//        // 构建抄送实例
//        String defId=(String)vars.get(BpmInstVars.DEF_ID.getKey());
//        String instId=(String)vars.get(BpmInstVars.INST_ID.getKey());
//        String subject=(String)vars.get(BpmInstVars.PROCESS_SUBJECT.getKey());
//
//        BpmDef bpmDef=bpmDefService.get(defId);
//
//        BpmInstCc instCc=new BpmInstCc();
//        instCc.setId(IdGenerator.getIdStr());
//        instCc.setCcType(BpmInstCc.CC_TYPE_COPY);
//        instCc.setDefId(defId);
//        instCc.setInstId(instId);
//        instCc.setTreeId(bpmDef.getTreeId());
//        instCc.setFromUser(currentUser.getFullName());
//        instCc.setFromUserId(currentUser.getUserId());
//        instCc.setSubject(subject);
//        instCc.setNodeId(entity.getCurrentActivityId());
//        instCc.setNodeName(entity.getName());
//
//        return  instCc;
//    }
//}
