package com.redxun.bpm.script;


import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.feign.UserClient;
import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.MethodDefine;
import com.redxun.bpm.script.cls.ParamDefine;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义脚本函数
 */
@ClassScriptType(type= "CustomApi",description = "自定义函数")
@Component("CustomApi")
public class CustomScript implements IScript {

    @Resource
    OrgClient orgClient;
    @Resource
    UserClient userClient;

    @MethodDefine(title = "获取某个部门下的关系用户",description = "获取某个部门下的关系用户作为任务节点执行人")
    public List<TaskExecutor> getGroupIdRelKeyByUser(@ParamDefine(varName = "groupId",description = "组ID") String groupId,
                                                     @ParamDefine(varName = "relKey",description = "组关系") String relKey){
        List<OsRelInstDto> list=orgClient.getByRelTypeKeyParty1(relKey,groupId);
        Set<String> userIds=new HashSet();
        for(int i=0;i<list.size();i++){
            userIds.add(list.get(i).getParty2());
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }

    public  List<TaskExecutor> changeUsers2Executors(@ParamDefine(varName = "users",description = "用户列表")List<OsUserDto>  users){
        List<TaskExecutor> tasks=new ArrayList<>();
        for(int i=0;i<users.size();i++) {
            tasks.add(TaskExecutor.getUser(users.get(i)));
        }
        return tasks;
    }

    @MethodDefine(title = "获取用户属性",description = "根据用户id和属性名获取属性值")
    public Object getUserProperty(@ParamDefine(varName = "userId",description = "用户ID")String userId,
                                  @ParamDefine(varName = "attrName",description = "属性名")String attrName){
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(attrName)){
            return null;
        }
        JSONObject json=new JSONObject();
        json.put("userId",userId);
        json.put("attrName",attrName);
        return userClient.getUserProperty(json).getData();
    }

    @MethodDefine(title = "获取组织属性",description = "根据组织id和属性名获取属性值")
    public Object getGroupProperty(@ParamDefine(varName = "groupId",description = "组织ID")String groupId,
                                   @ParamDefine(varName = "attrName",description = "属性名")String attrName){
        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(attrName)){
            return null;
        }
        JSONObject json=new JSONObject();
        json.put("groupId",groupId);
        json.put("attrName",attrName);
        return userClient.getGroupProperty(json).getData();
    }
}
