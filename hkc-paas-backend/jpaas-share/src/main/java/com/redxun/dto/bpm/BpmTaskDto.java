package com.redxun.dto.bpm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.redxun.common.dto.*;

import java.util.Date;

/**
 * 流程任务DTO
 */
@Data
public class BpmTaskDto extends BaseDto{
    //任务ID
    private String taskId;
    //Activiti任务ID
    private String actTaskId;
    private String treeId;
    //任务名称
    private String name;
    //任务Key
    private String key;
    //流程类型
    private String billType;
    //流程单号（冗余）
    private String billNo;
    //业务主键（冗余）
    private String busKey;
    //任务描述
    private String descp;
    //事项标题
    private String subject;
    //任务所属人
    private String owner;
    //任务执行人
    private String assignee;
    //流程实例ID
    private String actInstId;
    //ACT流程定义ID
    private String actDefId;
    //流程定义ID
    private String defId;
    //流程扩展实例ID
    private String instId;
    //任务状态
    private String status;
    //任务优先级
    private String priority;
    //任务过期时间
    private Date expiredTime;
    //转发次数
    private Integer forwardTimes;
    //任务类型
    private String taskType;
    private String parentId;
    //上一任务KEY
    private String fromTaskKey;
    //上一任务名称
    private String fromTaskName;

}
