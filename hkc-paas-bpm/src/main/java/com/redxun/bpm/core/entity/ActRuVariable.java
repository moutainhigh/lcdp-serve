
/**
 * <pre>
 *
 * 描述：act_ru_variable实体类定义
 * 表:act_ru_variable
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-08-23 12:46:42
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "act_ru_variable")
public class ActRuVariable  extends BaseExtEntity<String> {

    @JsonCreator
    public ActRuVariable() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //BYTEARRAY_ID_
    @TableField(value = "BYTEARRAY_ID_")
    private String bytearrayId;
    //DOUBLE_
    @TableField(value = "DOUBLE_")
    private Double doubleVal;
    //EXECUTION_ID_
    @TableField(value = "EXECUTION_ID_")
    private String executionId;
    //LONG_
    @TableField(value = "LONG_")
    private Long longVal;
    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //PROC_INST_ID_
    @TableField(value = "PROC_INST_ID_")
    private String procInstId;
    //REV_
    @TableField(value = "REV_")
    private Integer rev;
    //TASK_ID_
    @TableField(value = "TASK_ID_")
    private String taskId;
    //TEXT2_
    @TableField(value = "TEXT2_")
    private String text2;
    //TEXT_
    @TableField(value = "TEXT_")
    private String text;
    //TYPE_
    @TableField(value = "TYPE_")
    private String type;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



