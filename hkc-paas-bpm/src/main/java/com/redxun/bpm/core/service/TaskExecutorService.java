package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSON;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.config.UserGroupConfig;
import com.redxun.bpm.activiti.user.ExecutorCalcContext;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class TaskExecutorService {

    @Resource
    private ProcessScriptEngine processScriptEngine;

    @Resource
    IOrgService orgService;



    /**
     * 获得流程某个节点的人员配置
     * @param configs
     * @param vars

     * @return
     */
    public Set<TaskExecutor> getTaskExecutors(List<UserGroupConfig> configs, Map<String,Object> vars){
        Set<TaskExecutor> executors=new LinkedHashSet<>();
        for(UserGroupConfig config:configs){
            String condition=config.getCondition();
            //不满足条件则跳过。
            boolean rtn= handCondition(vars,condition);
            if(!rtn){
                continue;
            }
            //没有获取到人数据的时候也进行跳过
            Set<TaskExecutor> executorSet= getTaskExecutors( config, vars);
            if(BeanUtil.isEmpty(executorSet)){
                continue;
            }
            executors.addAll(executorSet);
        }
        return executors;
    }

    /**
     * 计算一个分组。
     * @param config
     * @param vars
     * @return
     */
    public Set<TaskExecutor> getTaskExecutors(UserGroupConfig config,Map<String,Object> vars){

        int i=0;
        Set<TaskExecutor> set=new LinkedHashSet<>();

        for(UserConfig userConfig :config.getConfigList()){
            //若变量中有，则优先从变量中获取

            i++;
            ITaskExecutorCalc cal = ExecutorCalcContext.getExecutorCalcByType(userConfig.getType());
            if(cal==null){
                continue;
            }
            Collection<TaskExecutor> taskExecutors=cal.getExecutors(userConfig,vars);
            if(BeanUtil.isEmpty(taskExecutors)){
                continue;
            }

            Collection<TaskExecutor> calcExecutors=calcExecutors(taskExecutors,userConfig);

            String logic=userConfig.getLogic();

            if(i==1){
                set.addAll(calcExecutors);
            }else if(UserConfig.LOGIC_NOT.equals(logic)){
                set.removeAll(calcExecutors);
            }else if(UserConfig.LOGIC_AND.equals(logic)){
                set.retainAll(calcExecutors);
            }else {
                set.addAll(calcExecutors);
            }
        }
        return set;
    }

    /**
     * 从流程变量中获取流程节点对应的执行人员
     * @param nodeId
     * @param vars
     * @return
     */
    public Set<TaskExecutor> getExecutorFromVars(String nodeId,Map<String,Object> vars){
        Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
        String nodeUserIds=(String)vars.get(BpmInstVars.NODE_USER_IDS.getKey());
        if(StringUtils.isEmpty(nodeUserIds)){
            return taskExecutors;
        }

        Map<String,Object> nodeIdUserMap= JSON.parseObject(nodeUserIds).getInnerMap();
        String userIds=(String)nodeIdUserMap.get(nodeId);
        if(StringUtils.isNotEmpty(userIds)){
            String[] userIdArr=userIds.split("[,]");
            for(String uId:userIdArr){
                OsUserDto osUserDto=orgService.getUserById(uId);
                if(osUserDto!=null) {
                    taskExecutors.add(new TaskExecutor(TaskExecutor.TYPE_USER, osUserDto.getUserId(),osUserDto.getFullName(),osUserDto.getAccount()));
                }
            }
        }

        return taskExecutors;
    }

    /**
     * 将人员进行计算。
     * @param taskExecutors
     * @param config
     * @return
     */
    private Set<TaskExecutor> calcExecutors(Collection<TaskExecutor> taskExecutors,UserConfig config){
        Set<TaskExecutor> executorSet=new LinkedHashSet<>();
        String calcType=config.getCalcType();
        for(TaskExecutor executor:taskExecutors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType() )){
                executorSet.add(executor);
            }
            else {
                if(UserConfig.CALC_YES.equals(calcType)){
                    if(StringUtils.isNotEmpty(executor.getId())) {
                        List<OsUserDto> users = orgService.getByGroupId(executor.getId());
                        for (OsUserDto userDto : users) {
                            executorSet.add(TaskExecutor.getUser(userDto.getUserId(), userDto.getFullName(),userDto.getAccount()));
                        }
                    }
                }
                else{
                    //组和延迟计算就不进行计算。
                    executor.setCalcType(calcType);
                    executorSet.add(executor);
                }
            }
        }
        return  executorSet;
    }

    /**
     * 处理组条件。
     * @param vars
     * @param condition
     * @return
     */
    private boolean handCondition(Map<String,Object> vars,String condition){
         if(StringUtils.isEmpty(condition)){
             return true;
         }
         Map<String,Object> model= ActivitiUtil.getConextData(vars);
         Object obj=processScriptEngine.exeScript(condition,model);
         if(obj instanceof  Boolean){
             return (Boolean)obj;
         }
         return false;
    }

}
