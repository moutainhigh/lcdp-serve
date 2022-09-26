package com.redxun.dto.bpm;


import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 节点执行人接口
 * @author csx
 */
@Setter
@Getter
public class TaskExecutor implements Serializable {
    /**
     * 不进行任何计算
     */
    public static final String CALC_NONE="none";
    /**
     * 进行计算
     */
    public static final String CALC_YES="calc";
    /**
     * 延迟计算
     */
    public static final String CALC_DELAY="delay";

    /**
     * 用户类型 = user
     */
    public static final String TYPE_USER="user";
    /**
     * 用户组类型 = group
     */
    public static final String TYPE_GROUP="group";

    /**
     * 执行人类型
     */
    private String type="";

    /**
     * 执行人ID
     * 可以是用户ID
     * 用户组ID
     */
    private String id="";

    /**
     * 执行人名称
     */
    private String name="";

    /**
     * 执行人账号
     * 用户组无账号
     */
    private String account="";

    /**
     * 是否进行计算。
     * 只有用户组的时候需要设置计算。
     */
    private String calcType=CALC_NONE;

    public TaskExecutor() {
    }

    public TaskExecutor(String type, String id, String name,String account) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public TaskExecutor(String type,String id){
        this.type=type;
        this.id=id;
    }

    public TaskExecutor(Map map){
        this.type=(String)map.get("type");
        this.id=(String)map.get("id");
        this.name=(String)map.get("name");
        this.calcType=(String)map.get("calcType");
    }

    public static TaskExecutor getUser(String userId, String name,String account){
        return new TaskExecutor(TYPE_USER, userId,name,account);
    }

    public static TaskExecutor getUser(OsUserDto osUserDto){
        return new TaskExecutor(TYPE_USER,osUserDto.getUserId(),osUserDto.getFullName(),osUserDto.getAccount());
    }

    public static TaskExecutor getGroup(String groupId, String name){
        return new TaskExecutor(TYPE_GROUP, groupId,name,"");
    }

    public static TaskExecutor getGroup(OsGroupDto osGroupDto){
        return new TaskExecutor(TYPE_GROUP,osGroupDto.getGroupId(),osGroupDto.getName(),"");
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskExecutor) {
            TaskExecutor executor = (TaskExecutor) obj;
            return this.id.equals( executor.id)  && this.type.equals(executor.getType());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

}
