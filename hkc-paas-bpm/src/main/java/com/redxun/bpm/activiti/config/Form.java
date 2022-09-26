package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 表单的配置
 * @author  csx
 */
@Data
public class Form implements Serializable {
    /**
     * 表单ID
     */
    private String id;
    /**
     * 表单名称
     */
    private String name;
    /**
     * 表单别名
     */
    private String alias;
    /**
     * 表单权限
     */
    private String permission;

    /**
     * 返回BO定义名称。
     */
    private String boAlias="";

    /**
     * 全局表单权限是否默认只读。
     */
    private Boolean readOnly=false;

    /**
     * 表单类型 (online,custom)
     */
    private String type="online";

    /**
     * 表单组件路径。
     */
    private String component="";
}
