package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.dto.bpm.TaskExecutor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * <pre>
 *
 * 描述：流程任务实体类定义
 * 表:BPM_TASK
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-18 17:09:46
 * 版权：广州红迅软件
 * </pre>
 */
@Getter
@Setter
@TableName(value = "BPM_TASK")
public class BpmTask  extends BaseExtEntity<String> {
    /**
     * 办理中=HANDLE
     */
    public final static String STATUS_HANDLE="HANDLE";
    /**
     * 未办理=UNHANDLE
     */
    public final static String STATUS_UNHANDLE="UNHANDLE";
    /**
     * 任务已经完成，不能在处理。
     */
    public final static String STATUS_COMPLETED="COMPLETED";

    /**
     * 任务已经完成，不能在处理。
     */
    public final static String STATUS_LOCKED="LOCKED";

    /**
     * 沟通=LINKUP
     */
    public final static String STATUS_LINKUP="LINKUP";

    /**
     * 流程任务=FLOW_TASK
     */
    public final static String TYPE_FLOW_TASK="FLOW_TASK";
    /**
     * 流转任务=TRANSFER_TASK
     */
    public final static String TYPE_TRANSFER_TASK="TRANSFER_TASK";

    /**
     * 沟通任务=LINKUP_TASK
     */
    public final static String TYPE_LINKUP_TASK="LINKUP_TASK";
    /**
     * 顺序的多实例任务=SEQUENTIAL_TASK
     */
    public final static String TYPE_SEQUENTIAL_TASK="SEQUENTIAL_TASK";
    /**
     * 并行的多实例任务=PARALLEL_TASK
     */
    public final static String TYPE_PARALLEL_TASK="PARALLEL_TASK";

    /**
     * 人工任务=MAN_TASK
     */
    public final static String TYPE_MAN_TASK="MAN_TASK";

    /**
     * 驳回产生的任务。
     */
    public final static String TYPE_REJECT_TASK="REJECT_TASK";

    /**
     * 表单类型-自定义表单=SEL-DEV
     */
    public final static String FORM_TYPE_SEL_DEV="custom";

    @JsonCreator
    public BpmTask() {
    }

    //任务ID
    @TableId(value = "TASK_ID_",type = IdType.INPUT)
	private String taskId;

    //Activiti任务ID
    @TableField(value = "ACT_TASK_ID_")
    private String actTaskId;


    @TableField(value = "TREE_ID_")
    private String treeId;

    //任务名称
    @TableField(value = "NAME_")
    private String name;

    //任务Key
    @TableField(value = "KEY_")
    private String key;

    //流程类型
    @TableField(value = "BILL_TYPE_")
    private String billType;

    //流程单号（冗余）
    @TableField(value = "BILL_NO_")
    private String billNo;

    //业务主键（冗余）
    @TableField(value = "BUS_KEY_")
    private String busKey;

    //任务描述
    @TableField(value = "DESCP_")
    private String descp;

    //事项标题
    @TableField(value = "SUBJECT_")
    private String subject;

    //任务所属人
    @TableField(value = "OWNER_")
    private String owner;

    //任务执行人
    @TableField(value = "ASSIGNEE_",updateStrategy = FieldStrategy.IGNORED)
    private String assignee;

    //流程实例ID
    @TableField(value = "ACT_INST_ID_")
    private String actInstId;

    //ACT流程定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;

    //流程定义ID
    @TableField(value = "DEF_ID_")
    private String defId;

    //流程扩展实例ID
    @TableField(value = "INST_ID_")
    private String instId;

    //任务状态
    @TableField(value = "STATUS_")
    private String status;

    //任务优先级
    @TableField(value = "PRIORITY_")
    private String priority;

    //任务过期时间
    @TableField(value = "EXPIRED_TIME_")
    private java.util.Date expiredTime;



    //任务类型
    @TableField(value = "TASK_TYPE_")
    private String taskType;

    @TableField(value = "PARENT_ID_")
    private String parentId;

    /**
     * 前一个任务，如果存在的情况则时原路返回。
     */
    @TableField(value = "PRE_TASK_ID_")
    private String preTaskId;

    @TableField(value = "EXECUTION_ID_")
    private String executionId;

    //是否有执行人
    @TableField(value = "EXECUTOR_")
    private String executor;



    @TableField(exist = false)
    private Set<TaskExecutor> taskExecutors;

    @TableField(exist = false)
    private BpmCheckHistory bpmCheckHistory;

    /**
     * 是否有候选任务。
     */
    @TableField(exist = false)
    private Boolean hasCandicate=false;

    /**
     * 申请人姓名
     */
    @TableField(exist = false)
    private String applicantName;
    /**
     * 申请人账号
     */
    @TableField(exist = false)
    private String applicantNo;

    /**
     * 表单JSON数据。
     */
    @TableField(exist = false)
    private String formJson;

    @Override
    public String getPkId() {
        return taskId;
    }

    @Override
    public void setPkId(String pkId) {
        this.taskId=pkId;
    }
}



