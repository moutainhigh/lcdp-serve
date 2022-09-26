package com.redxun.bpm.activiti.user.impl;

import com.gexin.fastjson.JSON;
import com.gexin.fastjson.JSONArray;
import com.gexin.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
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
 * 通过用户组扩展属性查找
 */
@Component
public class GroupPropertiesExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("GroupProperties","通过用户组扩展属性查找",15);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return taskExecutors;
        }
        JSONObject jsonObject = (JSONObject) JSON.parse(userConfig.getConfig());
        String mainType = jsonObject.getString("type");
        JSONArray conditionAry = jsonObject.getJSONArray("condition");
        String dimId = jsonObject.getString("dimId");
        //单个条件查询的用户组id
        List<List<String>> groupIds=new ArrayList<>();
        //条件组查询的用户id
        List<List<String>> groupGroupIds=new ArrayList<>();

        for (int i = 0; i < conditionAry.size(); i++) {
            Object type = conditionAry.getJSONObject(i).get("type");
            //条件组情况
            if(BeanUtil.isNotEmpty(type)){
                JSONArray groupConditions = conditionAry.getJSONObject(i).getJSONArray("condition");
                for (int j = 0; j < groupConditions.size(); j++) {
                    groupGroupIds.add(getGroup(groupConditions.getJSONObject(j),dimId));
                }
                groupIds.add(getGroupIdsByType((String) type, groupGroupIds));

            }else {
                groupIds.add(getGroup(conditionAry.getJSONObject(i),dimId));
            }
        }
        List<String> gIds = getGroupIdsByType(mainType, groupIds);

        //获取执行用户组
        for(String gId:gIds){
            OsGroupDto osGroup=orgClient.getGroupById(gId);
            TaskExecutor groupExecutor= TaskExecutor.getGroup(osGroup.getGroupId(),osGroup.getName());
            taskExecutors.add(groupExecutor);
        }
        return taskExecutors;
    }

    //获取符合条件的用户组id
    private  List<String> getGroup(JSONObject jsonObject,String dimId){
        String condition="";
        //数据类型
        String dataType= jsonObject.getString("dataType");
        String op = jsonObject.getString("op");
        String value = jsonObject.getString("value");

        //根据数据类型构造条件
        if("number".equals(dataType)){
            condition="NUM_VAL_ " + op +value;
        } else if("date".equals(dataType)){
            condition="DATE_VAL " + op +value;
        } else {
            if("like".equals(op)){
                condition="TXT_VAL " + op +" '%"+value+"%'";
            }
            condition="TXT_VAL " + op +"'"+value+"'";
        }
        return orgClient.getOwnerIdsByCondition(dimId, condition);
    }

    //根据类型筛选用户组
    private List<String> getGroupIdsByType(String type, List<List<String>> groupIds){
        //存放符合条件的用户组Id
        List<String> groupIdList=new ArrayList<>();
        if(groupIds.size()>0){
            if("and".equals(type)){
                //只有一个条件情况
                if(groupIds.size()==1){
                    groupIdList.addAll(groupIds.get(0));
                }else {
                    boolean flag=true;
                    //组条件有一个条件查询为空，即此条件组无符合条件数据
                    for (int i = 0; i < groupIds.size(); i++) {
                        if(groupIds.get(i).size()==0){
                            flag=false;
                            break;
                        }
                    }
                    if(flag){
                        List<String> list=new ArrayList();
                        //先获取前两个条件符合的数据
                        for (String s1 : groupIds.get(0)) {
                            for (String s2 : groupIds.get(1)) {
                                if(s1.equals(s2)){
                                    list.add(s1);
                                }
                            }
                        }
                        //获取剩下的条件的数据
                        if(list.size()>0&&groupIds.size()>=3){
                            List<String> list2 = new ArrayList();
                            for (int i = 2; i < groupIds.size(); i++) {
                                for (String str1 : groupIds.get(i)) {
                                    for (String str2 : list) {
                                        if(str1.equals(str2)){
                                            list2.add(str1);
                                        }
                                    }
                                }

                            }
                            groupIdList.addAll(list2);
                        }else {
                            groupIdList.addAll(list);
                        }
                    }
                }
            }else {
                for (List<String> ids : groupIds) {
                    groupIdList.addAll(ids);
                }
            }
            //去重
            HashSet set = new HashSet(groupIdList);
            groupIdList.clear();
            groupIdList.addAll(set);
        }
        return  groupIdList;
    }
}
