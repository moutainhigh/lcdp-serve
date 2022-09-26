package com.redxun.dto.bpm;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.dto.BaseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审批历史DTO
 */
@Data
public class BpmCheckHistoryDto extends BaseDto {
    //HIS_ID_
    private String hisId;
    //ACT流程定义ID
    private String actDefId;
    //流程实例ID
    private String instId;

    /**
     * 树ID
     */
    private String treeId;

    /**
     * 标题
     */
    private String subject;

    //节点名称
    private String nodeName;
    //节点Key
    private String nodeId;
    //任务ID
    private String taskId;

    //完成时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date completeTime;

    //持续时长
    private Long duration;
    //有效审批时长
    private Long durationVal;
    //任务所属人ID
    private String ownerId;
    //处理人ID
    private String handlerId;
    //被代理人
    private String agentUserId;
    //审批状态
    private String checkStatus;
    //跳转类型
    private String jumpType;
    //处理类型
    private String jumpTypeName;
    //意见备注
    private String remark;
    //字段意见名称
    private String opinionName;
    //处理部门ID
    private String handleDepId;
    //处理部门全名
    private String handleDepFull;
    //是否支持手机
    private Short enableMobile;
    //沟通对象Id
    private String linkUpUserIds;
    //沟通对象
    private List<Map<String,String>> linkUpUsers;
    //处理人姓名
    private String handlerUserName;
    //附件集合
    private List<BpmCheckFileDto> opFiles;
}
