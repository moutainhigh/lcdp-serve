//package com.redxun.bpm.listener;
//
//import com.redxun.api.org.IOrgService;
//import com.redxun.bpm.activiti.config.ProcessConfig;
//import com.redxun.bpm.activiti.config.UserGroupConfig;
//import com.redxun.bpm.activiti.config.UserTaskConfig;
//import com.redxun.bpm.activiti.utils.ActivitiUtil;
//import com.redxun.bpm.core.entity.*;
//import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
//import com.redxun.bpm.core.service.BpmDefService;
//import com.redxun.bpm.core.service.BpmInstCpServiceImpl;
//import com.redxun.bpm.core.service.BpmInstServiceImpl;
//import com.redxun.bpm.core.service.TaskExecutorService;
//import com.redxun.bpm.util.ProcessHandleUtil;
//import com.redxun.common.base.entity.IUser;
//import com.redxun.common.constant.MBoolean;
//import com.redxun.common.script.GroovyEngine;
//import com.redxun.common.tool.BeanUtil;
//import com.redxun.common.tool.IdGenerator;
//import com.redxun.common.tool.StringUtils;
//import com.redxun.common.utils.ContextUtil;
//import com.redxun.dto.bpm.BpmConst;
//import com.redxun.dto.bpm.TaskExecutor;
//import com.redxun.dto.user.OsUserDto;
//import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
//import org.activiti.engine.impl.persistence.entity.TaskEntity;
//
//import javax.annotation.Resource;
//import java.util.*;
//
//
//public class BaseCopyListener  {
//
//    @Resource
//    TaskExecutorService taskExecutorService;
//    @Resource
//    IOrgService orgService;
//    @Resource
//    BpmInstCpServiceImpl bpmInstCpService;
//    @Resource
//    BpmInstServiceImpl bpmInstService;
//    @Resource
//    private GroovyEngine groovyEngine;
//    @Resource
//    BpmDefService bpmDefService;
//
//
//    protected void sendMessage(BpmInstCc bpmInstCc, String informTypes ,List<OsUserDto> users){
//        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
//        IUser user= ContextUtil.getCurrentUser();
//
//        OsUserDto userDto=orgService.getUserById(user.getUserId());
//
//        Map<String,Object> vars=new HashMap<>();
//
//        if(cmd.getBoDataMap()!=null) {
//            vars.putAll(cmd.getBoDataMap());
//        }
//        vars.put("instId",bpmInstCc.getInstId());
//        BpmInst bpmInst = bpmInstService.get(bpmInstCc.getInstId());
//        if(bpmInst==null){
//            bpmInst=(BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);
//        }
//        vars.put("actDefId",bpmInst.getActDefId());
//
//        MessageUtil.sendMessage(userDto,bpmInstCc.getSubject(),informTypes,"copy",users,vars);
//
//    }
//
//
//    /**
//     * 添加抄送人员。
//     * @param instCc
//     * @param users
//     */
//    protected void addBpmInstReceivers(BpmInstCc instCc,List<OsUserDto> users){
//        for(OsUserDto user:users){
//            if(BeanUtil.isNotEmpty(user)){
//                BpmInstCp bpmInstCp=new BpmInstCp();
//                bpmInstCp.setId(IdGenerator.getIdStr());
//                bpmInstCp.setCcId(instCc.getId());
//                bpmInstCp.setInstId(instCc.getInstId());
//                bpmInstCp.setUserId(user.getUserId());
//                bpmInstCp.setIsRead(MBoolean.NO.name());
//                bpmInstCpService.insert(bpmInstCp);
//            }
//        }
//    }
//
//    /**
//     * 获取抄送用户
//     * @param taskExecutors
//     * @return
//     */
//    protected List<OsUserDto> getCopyUsers(Set<TaskExecutor> taskExecutors){
//        List<TaskExecutor> list=new ArrayList<>();
//        list.addAll(taskExecutors);
//        List<OsUserDto> users= orgService.getUsersByTaskExecutor(list);
//        return users;
//    }
//
//    /**
//     * 获取节点配置的抄送配置。
//     * @param userTaskConfig
//     * @param processConfig
//     * @param start
//     * @return
//     */
//    protected List<UserGroupConfig> getUserConfigs(UserTaskConfig userTaskConfig, ProcessConfig processConfig,boolean start){
//        if(start){
//            List<UserGroupConfig> startCopyConfig = userTaskConfig.getStartCopyConfig();
//            if(startCopyConfig!=null && BeanUtil.isNotEmpty(startCopyConfig)){
//                return  startCopyConfig;
//            }
//            List<UserGroupConfig> copyConfig=processConfig.getTaskStartCopyConfig();
//            if(copyConfig!=null && BeanUtil.isNotEmpty(copyConfig)){
//                return  copyConfig;
//            }
//        }
//        else{
//            List<UserGroupConfig> completeCopyConfig = userTaskConfig.getCompleteCopyConfig();
//            if(completeCopyConfig!=null && BeanUtil.isNotEmpty(completeCopyConfig)){
//                return  completeCopyConfig;
//            }
//            List<UserGroupConfig> copyConfig=processConfig.getTaskCompleteCopyConfig();
//            if(copyConfig!=null && BeanUtil.isNotEmpty(copyConfig)){
//                return  copyConfig;
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * 计算抄送用户。
//     * @param userGroupConfig
//     * @param vars
//     * @return
//     */
//    protected List<OsUserDto> getCopyUsers(UserGroupConfig userGroupConfig, Map<String,Object> vars){
//        Set<TaskExecutor> userSets=taskExecutorService.getTaskExecutors(userGroupConfig,vars);
//        List<OsUserDto> users= getCopyUsers(userSets) ;
//        return users;
//    }
//
//
//    /**
//     * 构建抄送对象。
//     * @param vars
//     * @param taskEntity
//     * @return
//     */
//    protected BpmInstCc buildBpmInstCc(Map<String,Object> vars, TaskEntity taskEntity){
//        // 构建抄送实例
//        String defId=(String)vars.get(BpmInstVars.DEF_ID.getKey());
//        String instId=(String)vars.get(BpmInstVars.INST_ID.getKey());
//        String subject=(String)vars.get(BpmInstVars.PROCESS_SUBJECT.getKey());
//
//        IUser currentUser= ContextUtil.getCurrentUser();
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
//        instCc.setNodeId(taskEntity.getTaskDefinitionKey());
//        instCc.setNodeName(taskEntity.getName());
//
//        return  instCc;
//    }
//
//    /**
//     * 处理配置的抄送。
//     * @param copyConfig
//     * @param vars
//     * @param instCc
//     * @return
//     */
//    protected boolean handConfigCopy(List<UserGroupConfig> copyConfig,Map<String,Object> vars,BpmInstCc instCc){
//        if(BeanUtil.isEmpty(copyConfig)){
//            return false;
//        }
//        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
//        if(cmd!=null && cmd.getFormData()!=null) {
//            vars.putAll(cmd.getFormData());
//        }
//        boolean rtn=false;
//        for(UserGroupConfig userGroupConfig : copyConfig) {
//            String condition=userGroupConfig.getCondition();
//            //不满足条件则跳过。
//            boolean condiFlag= handCondition(vars,condition);
//            if(!condiFlag){
//                continue;
//            }
//            Set<TaskExecutor> userSets=taskExecutorService.getTaskExecutors(userGroupConfig,vars);
//            //获取接收人。
//            List<OsUserDto> users = getCopyUsers(userSets);
//            if(BeanUtil.isEmpty(users)){
//                continue;
//            }
//
//            rtn=true;
//
//            //添加抄送接收人
//            addBpmInstReceivers(instCc, users);
//            //发送消息。
//            sendMessage(instCc, userGroupConfig.getInfoTypes(), users);
//        }
//        return  rtn;
//
//    }
//
//    /**
//     * 处理组条件。
//     * @param vars
//     * @param condition
//     * @return
//     */
//    private boolean handCondition(Map<String,Object> vars,String condition){
//        if(StringUtils.isEmpty(condition)){
//            return true;
//        }
//        Map<String,Object> model= ActivitiUtil.getConextData(vars);
//        Object obj=groovyEngine.executeScripts(condition,model);
//        if(obj instanceof  Boolean){
//            return (Boolean)obj;
//        }
//        return false;
//    }
//
//    protected boolean handContextCopy(IExecutionCmd cmd,BpmInstCc instCc){
//        if(StringUtils.isEmpty(cmd.getCopyUserAccounts()) ) {
//            return false;
//        }
//        String[] userAccounts=cmd.getCopyUserAccounts().split("[,]");
//        List<OsUserDto> userList=new ArrayList<>();
//        for (String userAccount : userAccounts) {
//            OsUserDto user = orgService.getByAccount(userAccount);
//            userList.add(user);
//        }
//
//        addBpmInstReceivers(instCc,userList);
//        //发送抄送消息
//        sendMessage(instCc,cmd.getMsgTypes(),userList);
//        //清空抄送人
//        cmd.setCopyUserAccounts(null);
//
//        return  true;
//    }
//
//}
