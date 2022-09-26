package com.redxun.log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 审计日志
 *
 */
@Setter
@Getter
public class Audit extends BaseExtEntity<String> {

    public final static String ACTION="action";

    public final static String ACTION_ADD="ADD";

    public final static String ACTION_UPD="UPD";

    public final static String ACTION_DEL="DEL";

    public final static String CURRENT="CUR";

    public final static String ORIGIN="ORIGIN";

    public final static String OPERATION="operation";

    public final static String DETAIL="detail";

    public final static String PK="PK";

    public final static String BUS_TYPE="busType";

    public final static String BUS_TYPE_FORM="FORM";

    public final static String IS_LOG="isLog";

    public final static String STATUS="status";

    public final static String STATUS_SUCCESS="1";

    public final static String STATUS_FAIL="0";



    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;
    /**
     * 应用名
     */
    @TableId(value = "APP_NAME_")
    private String appName;

    /**
     * 子系统
     */
    @TableId(value = "MODULE_")
    private String module;

    /**
     * 模块
     */
    @TableId(value = "SUB_MODULE_")
    private String subModule;
    /**
     * 类名(使用全路径)
     */
    @TableId(value = "CLASS_NAME_")
    private String className;
    /**
     * 方法名
     */
    @TableId(value = "METHOD_NAME_")
    private String methodName;



    @TableId(value = "ACTION_")
    private String action;

    /**
     * 操作数据的主键。
     */
    @TableId(value = "PK_VALUE_")
    private String pkValue;

    /**
     * 客户操作ip.
     */
    @TableId(value = "IP_")
    private String ip;

    /**
     * 操作信息
     */
    @TableId(value = "OPERATION_")
    private String operation;

    @TableId(value = "DETAIL_")
    private String detail="";

    @TableId(value = "USER_NAME_")
    private String userName;

    @TableId(value = "DURATION_")
    private Long duration;

    @TableId(value = "BUS_TYPE_")
    private String busType;

    @TableId(value = "IS_RESUME_")
    private String isResume;


    @Override
    public String getPkId() {
        return this.id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    @Override
    public String toString() {
        return "{" +
                "appName='" + appName + '\'' +
                ", module='" + module + '\'' +
                ", subModule='" + subModule + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", action='" + action + '\'' +
                ", pkValue='" + pkValue + '\'' +
                ", ip='" + ip + '\'' +
                ", operation='" + operation + '\'' +
                ", detail='" + detail + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
