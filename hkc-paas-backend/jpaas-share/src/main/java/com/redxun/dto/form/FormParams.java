package com.redxun.dto.form;

import lombok.Getter;
import lombok.Setter;

/**
 * Form数据
 */
@Getter
@Setter
public class FormParams {

    /**
     * 表单主键
     */
    private String pk="";

    /**
     * 表单别名
     */
    private String alias="";

    /**
     * 权限配置
     */
    private String permission="";

    /**
     * bo定义别名。
     */
    private String boAlias="";

    /**
     * 全局表单权限是否默认只读。
     */
    private Boolean readOnly=false;

    /**
     * 是否默认可写。
     */
    private Boolean defaultWrite=false;

    /**
     *  pc端表单ID
     */
    private String pcFormId="";

    /**
     * 发起人ID
     */
    private String startUserId;

    /**
     * 当前节点ID
     */
    private String nodeId;

    /**
     * 已审批节点
     */
    private String approveNodes;

    /**
     * 抄送人ID
     */
    private String ccUserIds;
}
