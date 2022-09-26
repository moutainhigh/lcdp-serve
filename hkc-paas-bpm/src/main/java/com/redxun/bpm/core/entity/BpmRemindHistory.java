
/**
 * <pre>
 *
 * 描述：催办历史表实体类定义
 * 表:bpm_remind_history
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-06 11:15:26
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
@TableName(value = "bpm_remind_history")
public class BpmRemindHistory  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmRemindHistory() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //催办实例ID
    @TableField(value = "REMINDER_INST_ID_")
    private String reminderInstId;
    //催办类型
    @TableField(value = "REMIND_TYPE_")
    private String remindType;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



