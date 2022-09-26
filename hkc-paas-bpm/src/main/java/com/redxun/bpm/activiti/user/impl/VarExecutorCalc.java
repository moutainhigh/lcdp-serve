package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流程任务节点人员执行策略
 * 通过流程变量查找用户或组（部门、角色、组）
 */
@Component
public class VarExecutorCalc implements ITaskExecutorCalc {

    @Resource
    IOrgService orgService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("var","通过流程变量查找用户或组（部门、角色、组）",4);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idList=new LinkedHashSet<>();
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idList;
        }
        JSONObject configObj=JSONObject.parseObject(userConfig.getConfig());

        if(configObj.get("varKey")==null || configObj.get("varType")==null) {
            return idList;
        }

        String varKey=configObj.getString("varKey");
        String userType=configObj.getString("varType");
        String valueType = configObj.getString("valueType");
        String idVals = (String)vars.get(varKey);
        if(StringUtils.isEmpty(idVals)){
            return idList;
        }

        //是否计算用户
        String[] uIds=idVals.split("[,]");
        if("org".equals(userType)){
            for(String value : uIds){
                OsGroupDto osGroup;
                if ("id".equals(valueType)) {
                    osGroup=orgService.getGroupById(value);
                }
                else {
                    osGroup=orgService.getGroupByKey(value);
                }
                if(osGroup==null) {
                    continue;
                }
                idList.add(TaskExecutor.getGroup(osGroup.getGroupId(),osGroup.getName()));
            }
        }else{
            for(String value:uIds){
                IUser osUser;
                if ("id".equals(valueType)) {
                    osUser=orgService.getUserById(value);
                }
                else {
                    osUser=orgService.getByUsername(value);
                }
                if(osUser==null){
                    continue;
                }
                idList.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
            }
        }
        return idList;
    }
}
