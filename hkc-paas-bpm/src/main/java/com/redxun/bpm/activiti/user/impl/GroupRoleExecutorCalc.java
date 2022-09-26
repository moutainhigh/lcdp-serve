package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.activiti.user.enums.GroupValueType;
import com.redxun.bpm.activiti.user.enums.UserVarType;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 查找某用户组下的某角色用户
 */
@Component
public class GroupRoleExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;
    /**
     * 表单JSON中主表Key
     */
    private static final String MAIN="main";
    /**
     * 表单JSON子表的前缀Key
     */
    private static final String SUBTABLE="sub__";

    @Override
    public ExecutorType getType() {
        return new ExecutorType("groupRole","查找某用户组下的某角色用户",12);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        String tenantId= ContextUtil.getCurrentTenantId();
        Set<TaskExecutor> idList=new LinkedHashSet<TaskExecutor>();
        //获取不到配置
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idList;
        }
        JSONObject configObj=JSONObject.parseObject(userConfig.getConfig());
        //获取不到配置
        if(configObj==null){
            return idList;
        }
        //用户组的变量类型
        String varType=configObj.getString("varType");
        // 值类型
        String valueType=configObj.getString("valueType");

        //计算的用户组ID
        List<String> groupIds=null;
        String boAlias=configObj.getString("boAlias");

        String instId = (String)vars.get(BpmInstVars.INST_ID.getKey());
        if(StringUtils.isEmpty(instId)){
            return idList;
        }

        //角色Key变量
        String roleValues=configObj.getString("roleValues");
        if(StringUtils.isEmpty(roleValues)){
            return idList;
        }

        JSONObject roleJsonObj=JSONObject.parseObject(roleValues);
        if(roleJsonObj==null || roleJsonObj.getString("value") == null){
            return idList;
        }
        //获取角色列表
        String roleIds=roleJsonObj.getString("value");

        //存放计算返回的用户Ids
        List<String> userIds=null;
        if(UserVarType.FORM.getVarType().equals(varType)){ // 值来自表单变量
            /**
             * 当值来自表单字段时，其配置的参数格式如下所示：
             * {
             * 	"boAlias":"ExpReq",
             * 	"varType":"form",
             * 	"valueType":"id",
             * 	"table":"main",
             * 	"tableField":"qkr",
             * 	"isSingle":0,
             * 	"roleValues":"{\"value\":\"1446126550750662657,1446126484543574017\",\"text\":\"总经理,主管经理\"}",
             * }
             */
            IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
            JSONObject formData = cmd.getBoDataMap();
            if(!formData.containsKey(boAlias)){
                return idList;
            }
            formData = formData.getJSONObject(boAlias);
            // 计算出表单变量下的用户组Id或Key
            groupIds=getUserOrgIds(configObj,formData);
            // 用户组为Key值，需要转化为ID值
            if(GroupValueType.KEY.getKeyVal().equals(valueType)){
                List<String> groupIdList = new ArrayList<>();
                for(String groupKey:groupIds){
                    OsGroupDto groupDto=orgClient.getGroupByKeyTenantId(groupKey,tenantId);
                    if(groupDto!=null){
                        groupIdList.add(groupDto.getGroupId());
                    }
                }
                groupIds = groupIdList;
            }
            if(groupIds!=null && groupIds.size() > 0) {
                userIds = orgClient.getUserIdsByGroupIdsRoleIds(StringUtils.join(groupIds, "[,]"), roleIds);
            }
        }else if(UserVarType.FLOW_VAR.getVarType().equals(varType)){ //来自流程变量
            /**
             *  当来自流程变量时，其值获取的值如下所示：
             * {
             * 	"varType":"flowVar",
             * 	"valueType:"id",
             * 	"roleValues":"{\"value\":\"1446126550750662657,1446126484543574017\",\"text\":\"总经理,主管经理\"}",
             * 	"varKey":"startDepId"
             * }
             */
            //获取用户组ID变量名
            String groupIdVar=configObj.getString("varKey");
            String groupId=(String)vars.get(groupIdVar);
            String [] groupIdArr= groupId.split("[,]");
            // 找不到用户组
            if(StringUtils.isEmpty(groupId)){
                return idList;
            }
            // 用户组为Key值，需要转化为ID值
            if(GroupValueType.KEY.getKeyVal().equals(valueType)){
                List<String> groupIdList = new ArrayList<>();
                for(String groupKey:groupIdArr){
                    OsGroupDto groupDto=orgClient.getGroupByKeyTenantId(groupKey,tenantId);
                    if(groupDto!=null){
                        groupIdList.add(groupDto.getGroupId());
                    }
                }
                groupId = StringUtils.join(groupIdList,",");
            }
            if(StringUtils.isNotEmpty(groupId)) {
                //计算返回的用户
                userIds = orgClient.getUserIdsByGroupIdsRoleIds(groupId, roleIds);
            }
        }
        //转化用户为执行者
        if(userIds != null){
            List<OsUserDto> osUserDtos=orgClient.getUsersByIds(StringUtils.join(userIds,","));
            for(OsUserDto osUser:osUserDtos){
                idList.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
            }
        }

        return idList;
    }

    /**
     * 通过表单变量获取用户或组织的ID
     * @param configObj
     * @param formData
     * @return
     */
    private List<String> getUserOrgIds(JSONObject configObj,JSONObject formData){
        String varKey=configObj.getString("tableField");
        Integer isSingle=configObj.getInteger("isSingle");
        String table=configObj.getString("table");
        List<String> idList=new ArrayList<>();
        //主表
        if(table.equals(MAIN)){
            handRow(formData,varKey,isSingle,idList);
        } else {//来自子表
            Object obj=formData.get(SUBTABLE +table);
            if(obj instanceof JSONArray){
                JSONArray array=formData.getJSONArray(SUBTABLE +table);
                for(int i=0;i<array.size();i++){
                    JSONObject row=array.getJSONObject(i);
                    handRow(row,varKey,isSingle,idList);
                }
            }
            else{
                JSONObject jsonObj=formData.getJSONObject(SUBTABLE +table);
                handRow(jsonObj,varKey,isSingle,idList);
            }
        }
        return idList;
    }

    /**
     * 处理一行数据。
     * @param row
     * @param key
     * @param isSingle
     * @param idList
     */
    private void handRow(JSONObject row,String key,Integer isSingle,List<String> idList){
        String idVals=row.getString(key);
        if(isSingle!=null && isSingle!=1){
            JSONObject jsonObject = JSONObject.parseObject(idVals);
            idVals=jsonObject.getString("value");
        }
        handList(idList,idVals);
    }

    /**
     * 获取用户组列表
     * @param idVals
     * @param configObj
     * @return
     */
    private Set<TaskExecutor> getGroupIdList(String idVals,JSONObject configObj){
        Set<TaskExecutor> idList=new LinkedHashSet<TaskExecutor>();
        List<OsGroupDto> groupDtos = orgClient.getGroupByIds(idVals);
        if(BeanUtil.isNotEmpty(groupDtos)){
            for(OsGroupDto dto:groupDtos){
                idList.add(TaskExecutor.getGroup(dto.getGroupId(), dto.getName()));
            }
        }
        return idList;
    }

    /**
     * 添加到列表。
     * @param idList
     * @param idVals
     */
    private  void handList(List<java.lang.String> idList,String idVals){
        if(StringUtils.isEmpty(idVals)){
            return;
        }
        String[] aryId=idVals.split(",");
        for(String id:aryId){
            if(idList.contains(id)){
                continue;
            }
            idList.add(id);
        }
    }

}
