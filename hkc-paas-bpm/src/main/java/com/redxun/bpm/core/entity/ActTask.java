
/**
 * <pre>
 *
 * 描述：act_ru_task实体类定义
 * 表:act_ru_task
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-25 23:56:57
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "act_ru_task")
public class ActTask  implements BaseEntity<String> {

    @JsonCreator
    public ActTask() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //REV_
    @TableField(value = "REV_")
    private Integer rev;
    //EXECUTION_ID_
    @TableField(value = "EXECUTION_ID_")
    private String executionId;
    //PROC_INST_ID_
    @TableField(value = "PROC_INST_ID_")
    private String procInstId;
    //PROC_DEF_ID_
    @TableField(value = "PROC_DEF_ID_")
    private String procDefId;
    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //PARENT_TASK_ID_
    @TableField(value = "PARENT_TASK_ID_")
    private String parentTaskId;
    //DESCRIPTION_
    @TableField(value = "DESCRIPTION_")
    private String description;
    //TASK_DEF_KEY_
    @TableField(value = "TASK_DEF_KEY_")
    private String taskDefKey;
    //OWNER_
    @TableField(value = "OWNER_")
    private String owner;
    //ASSIGNEE_
    @TableField(value = "ASSIGNEE_")
    private String assignee;
    //DELEGATION_
    @TableField(value = "DELEGATION_")
    private String delegation;
    //PRIORITY_
    @TableField(value = "PRIORITY_")
    private Integer priority;
    //DUE_DATE_
    @TableField(value = "DUE_DATE_")
    private java.util.Date dueDate;
    //CATEGORY_
    @TableField(value = "CATEGORY_")
    private String category;
    //SUSPENSION_STATE_
    @TableField(value = "SUSPENSION_STATE_")
    private Integer suspensionState;
    //FORM_KEY_
    @TableField(value = "FORM_KEY_")
    private String formKey;
    //CLAIM_TIME_
    @TableField(value = "CLAIM_TIME_")
    private java.util.Date claimTime;

    @TableField(value = "CREATE_TIME_")
    private java.util.Date createTime;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



