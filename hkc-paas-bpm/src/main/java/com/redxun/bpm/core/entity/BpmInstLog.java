
/**
 * <pre>
 *
 * 描述：流程活动日志实体类定义
 * 表:bpm_inst_log
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-01 10:38:33
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
@TableName(value = "bpm_inst_log")
public class BpmInstLog  extends BaseExtEntity<String> {

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //用户ID
    @TableField(value = "USER_ID_")
    private String userId;

    //用户NAME
    @TableField(value = "USER_NAME_")
    private String userName;

    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //任务名称
    @TableField(value = "TASK_NAME_")
    private String taskName;
    //任务Key
    @TableField(value = "TASK_KEY_")
    private String taskKey;
    //用户组ID
    @TableField(value = "OP_DESCP_")
    private String opDescp;
    @JsonCreator
    public BpmInstLog() {
    }

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



