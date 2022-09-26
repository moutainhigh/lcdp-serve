
/**
 * <pre>
 *
 * 描述：流程流转表实体类定义
 * 表:bpm_transfer
 * 作者：hj
 * 邮箱: hj@redxun.cn
 * 日期:2020-10-30 09:46:30
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
@TableName(value = "bpm_transfer")
public class BpmTransfer  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmTransfer() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //签批类型
    @TableField(value = "APPROVE_TYPE_")
    private String approveType;
    //流转类型
    @TableField(value = "TRANSFER_TYPE_")
    private String transferType;
    //完成类型
    @TableField(value = "COMPLETE_TYPE_")
    private Integer completeType;
    //数量
    @TableField(value = "COUNT_")
    private Integer count;
    //完成次数
    @TableField(value = "COMPLETE_COUNT_")
    private Integer completeCount;
    //完成次数设定
    @TableField(value = "COMPLETE_SETTING_")
    private Integer completeSetting;
    //通知类型
    @TableField(value = "NOTICE_TYPE_")
    private String noticeType;
    //完成判断类型
    @TableField(value = "COMPLETE_JUDGE_TYPE_")
    private String completeJudgeType;
    //流转人员ID
    @TableField(value = "TASK_USER_ID_")
    private String taskUserId;
    //当前流转下标
    @TableField(value = "TASK_USER_IDX_")
    private Integer taskUserIdx;
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


    /**
    生成子表属性的Array List
    */

}



