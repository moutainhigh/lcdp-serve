package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsRelTypeDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 通过用户组（部门、角色、组等）关系查找用户组下用户
 */
@Component
public class GroupRelExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("groupRel","通过用户组（部门、角色、组等）关系查找用户组下用户",9);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();

        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idInfos;
        }

        JSONObject jsonObj=JSONObject.parseObject(userConfig.getConfig());
        /**
         * 用户组
         * {value:"startDepId",label:"发起部门ID"},
         * {value:"currentUserId",label:"当前人部门ID"},
         * {value:"formDepId",label:"来自表单部门ID"},
         */
        String groupIdVar=jsonObj.getString("groupId");



        //选择维度，当用户组类型为发起部门ID/当前人部门ID，选择组织维度固定行政组织
        String dimIdVar=jsonObj.getString("dimId");
        if("startDepId".equals(groupIdVar) || "currentUserId".equals(groupIdVar)){
            dimIdVar="1";
        }
        //选择关系类型
        String relType= jsonObj.getString("relType");
        //关联维度--获得关系Key及需要查找的关系方
        String relTypeId= jsonObj.getString("relTypeKey");
        //用户组关系类型
        String relTypeGroup= jsonObj.getString("relTypeGroup");



        String groupId="";
        if("startDepId".equals(groupIdVar)){
            groupId=(String)vars.get(groupIdVar);
        }else if("currentUserId".equals(groupIdVar)){
            groupId= ContextUtil.getCurrentUser().getDeptId();
        }else if("formDepId".equals(groupIdVar)){
            //表单名
            String boAlias= jsonObj.getString("boAlias");
            if(StringUtils.isEmpty(boAlias)){
                return idInfos;
            }
            //表字段
            String varKey= jsonObj.getString("varKey");
            if(StringUtils.isEmpty(varKey)){
                return idInfos;
            }


            IExecutionCmd processCmd =ProcessHandleUtil.getProcessCmd();
            Map<String,Object> boMapData=processCmd.getBoDataMap();
            if(BeanUtil.isEmpty(boMapData)){
                return idInfos;
            }
            //表单数据
            if(!boMapData.containsKey(boAlias)){
                return idInfos;
            }

            Object formData=boMapData.get(boAlias);
            String formDataStr =JSONObject.toJSONString(formData);
            JSONObject formDataJson = JSONObject.parseObject(formDataStr);
            String groupStr=formDataJson.getString(varKey);
            if(StringUtils.isEmpty(groupStr)){
                return idInfos;
            }
            //值类型
            String controlValType = jsonObj.getString("controlValType");
            if("osGroup".equals(controlValType)){
                //部门控件
                JSONObject groupJson = JSONObject.parseObject(groupStr);
                groupId=groupJson.getString("value");
            }else if("groupId".equals(controlValType)){
                //部门ID_
                groupId=groupStr;
            }else if("groupKey".equals(controlValType)|| "groupName".equals(controlValType)){
                //部门Key/部门名称
                JSONObject groupJson = new JSONObject();
                groupJson.put("dimId",dimIdVar);
                groupJson.put("tenantId",ContextUtil.getCurrentTenantId());
                groupJson.put("controlValType",controlValType);
                groupJson.put("groupVal",groupStr);
                List<OsGroupDto> groupDtos =orgClient.getByDimIdAndKeyOrName(groupJson);
                if(BeanUtil.isEmpty(groupDtos)){
                    return idInfos;
                }
                Set<String> groupIds = new HashSet<>();
                for (OsGroupDto group:groupDtos) {
                    groupIds.add(group.getGroupId());
                }
                groupId = StringUtils.join(groupIds,",");
            }


            if(StringUtils.isEmpty(groupId)){
                return idInfos;
            }
        }

        List<OsUserDto> ustList =new ArrayList<>();

        //选择关系类型：用户关系
        if("GROUP-USER".equals(relType)){
            if(groupId.indexOf(",")>-1){
                String[] groupIds=groupId.split("[,]");
                for (String newGroupId:groupIds) {
                    //部门ID，关联维度ID
                    List<OsUserDto> users =orgClient.getUserListByGroupIdAndRelTypeIdAndDimId(dimIdVar,newGroupId,relTypeId);
                    ustList.addAll(users);
                }
            }else {
                //部门ID，关联维度ID
                List<OsUserDto> users =orgClient.getUserListByGroupIdAndRelTypeIdAndDimId(dimIdVar,groupId,relTypeId);
                ustList.addAll(users);
            }
        }else if("GROUP-GROUP".equals(relType)){
            if(groupId.indexOf(",")>-1){
                String[] groupIds=groupId.split("[,]");
                for (String newGroupId:groupIds) {
                    //部门ID，关联维度ID，用户组关系类型ID获取用户，维度ID
                    List<OsUserDto> users =orgClient.
                            getUserByGroupIdAndRelTypeIdAndParty2(newGroupId,relTypeId,relTypeGroup,dimIdVar);
                    ustList.addAll(users);
                }
            }else {
                //部门ID，关联维度ID，用户组关系类型ID获取用户，维度ID
                List<OsUserDto> users =orgClient.
                        getUserByGroupIdAndRelTypeIdAndParty2(groupId,relTypeId,relTypeGroup,dimIdVar);
                ustList.addAll(users);
            }
        }else {
            return idInfos;
        }


        for(OsUserDto osUser:ustList){
            idInfos.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
        }
        return idInfos;
    }
}
