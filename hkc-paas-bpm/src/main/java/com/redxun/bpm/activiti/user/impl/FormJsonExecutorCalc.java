package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 用户或组来自表单字段
 */
@Component
public class FormJsonExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    private static final String MAIN="main";
    private static final String SUBTABLE="sub__";

    /**
     * 关系类型，用户组关系=GROUP-GROUP；
     */
    private final static String REL_TYPE_GROUP_GROUP="GROUP-GROUP";
    /**
     * 关系类型，组与用户关系=GROUP-USER
     */
    private final static String REL_TYPE_GROUP_USER="GROUP-USER";

    @Override
    public ExecutorType getType() {
        return new ExecutorType("formJson","用户或组来自表单字段",5);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idList=new LinkedHashSet<TaskExecutor>();
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idList;
        }
        /**
         * {
        *   varType:"变量类型",
        *   boAlias:"业务模型别名",
        *   table:"对应的表",
        *   varKey:"字段数据",
        *   isSingle:"是否单值模式",
        *   useRelation:"使用关系",
        *   relTypeKey:"关系key",
        *   relType:"关系类型",
         *  relPart:[
         *     {
         *         "name": "老板",
         *         "id": "1403280614014525441"
         *     }
         * ]
         *  }
         */
        JSONObject configObj=JSONObject.parseObject(userConfig.getConfig());
        String userType=configObj.getString("varType");
        String boAlias=configObj.getString("boAlias");

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        JSONObject formData = cmd.getBoDataMap();
        if(!formData.containsKey(boAlias)){
            return idList;
        }

        formData = formData.getJSONObject(boAlias);

        /**
         * 获取用户或组织的ID
         */
        String idVals=getIds(configObj,formData);

        String[] ids=idVals.split(",");
        for(String id:ids) {
            idList.addAll(getIdList(id, userType, configObj));
        }

        return idList;
    }


    /**
     * 获取用户或组织的ID
     * @param configObj
     * @param formData
     * @return
     */
    private String getIds(JSONObject configObj,JSONObject formData){
        String varKey=configObj.getString("varKey");
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
        if(idList.size()==0){
            return "";
        }
        return StringUtils.join(idList,",");
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
            if(jsonObject!=null) {
                idVals = jsonObject.getString("value");
            }
        }
        handList(idList,idVals);
    }

    /**
     * 添加到列表。
     * @param idList
     * @param idVals
     */
    private   void handList(List<java.lang.String> idList,String idVals){
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

    private Set<TaskExecutor> getIdList(String idVals,String type,JSONObject configObj){
        Set<TaskExecutor> idList=new LinkedHashSet<TaskExecutor>();
        if("org".equals(type)){
            if(configObj.containsKey("useRelation") && configObj.getBoolean("useRelation")){
               getByRelType(configObj, idList, idVals);
            }
            else {
                List<OsGroupDto> groupDtos = orgClient.getGroupByIds(idVals);
                if(BeanUtil.isNotEmpty(groupDtos)){
                    for(OsGroupDto dto:groupDtos){
                        idList.add(TaskExecutor.getGroup(dto.getGroupId(), dto.getName()));
                    }
                }
            }
        }else{//user
            String userIds=idVals;
            List<OsUserDto> users = orgClient.getUsersByIds(userIds);
            for(OsUserDto osUser:users){
                if(osUser!=null){
                    idList.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
                }
            }
        }

        return idList;
    }

    private void getByRelType(JSONObject configObj,Set<TaskExecutor> idList,String orgId){

        String relTypeKey=configObj.getString("relTypeKey");

        String relType=configObj.getString("relType");

        List<OsUserDto> userDtos;

        if(REL_TYPE_GROUP_USER.equals(relType)){
            userDtos = orgClient.getUserByRelTypeKeyParty1(relTypeKey, orgId);
        }
        else {
            JSONArray parts= configObj.getJSONArray("relPart");
            List<String> part2List=new ArrayList<>();
            for(int i=0;i<parts.size();i++){
                JSONObject json= parts.getJSONObject(i);
                String id=json.getString("id");
                if(!part2List.contains(id)){
                    part2List.add(id);
                }
            }
            String part2=StringUtils.join(part2List,",");

            userDtos = orgClient.getUserByRelTypePart12(relTypeKey, orgId,part2);
        }
        for(OsUserDto userDto:userDtos){
            idList.add(TaskExecutor.getUser(userDto));
        }
    }
}
