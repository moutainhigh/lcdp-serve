
/**
 * <pre>
 *
 * 描述：催办实例表实体类定义
 * 表:bpm_remind_inst
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-30 17:11:53
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
@TableName(value = "bpm_remind_inst")
public class BpmRemindInst extends BaseExtEntity<String> {

    @JsonCreator
    public BpmRemindInst() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    /**
     * 任务ID
     */
    @TableField(value = "TASK_ID_")
    private String taskId;
    /**
     * 名称
     */
    @TableField(value = "NAME_")
    private String name;
    /**
     * 到期动作
     */
    @TableField(value = "ACTION_")
    private String action;
    /**
     * 期限
     */
    @TableField(value = "EXPIRE_DATE_")
    private java.util.Date expireDate;
    /**
     * 到期执行脚本
     */
    @TableField(value = "SCRIPT_")
    private String script;
    /**
     * 通知类型
     */
    @TableField(value = "NOTIFY_TYPE_")
    private String notifyType;
    /**
     * 开始发送消息时间点
     */
    @TableField(value = "TIME_TO_SEND_")
    private java.util.Date timeToSend;
    /**
     * 发送次数
     */
    @TableField(value = "SEND_TIMES_")
    private Integer sendTimes;
    /**
     * 发送时间间隔
     */
    @TableField(value = "SEND_INTERVAL_")
    private Integer sendInterval;
    /**
     * 状态(2,完成,0,创建,1,进行中)
     */
    @TableField(value = "STATUS_")
    private String status;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



