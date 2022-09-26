
/**
 * <pre>
 *
 * 描述：流转任务日志表实体类定义
 * 表:bpm_transfer_log
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-12-01 11:47:03
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
@TableName(value = "bpm_transfer_log")
public class BpmTransferLog  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmTransferLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //流转人
    @TableField(value = "ASSIGNEE_")
    private String assignee;
    //流转意见
    @TableField(value = "REMARK_")
    private String remark;
    //流转状态
    @TableField(value = "STATUS_")
    private String status;
    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //流转任务ID
    @TableField(value = "TRANS_TASK_ID_")
    private String transTaskId;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



