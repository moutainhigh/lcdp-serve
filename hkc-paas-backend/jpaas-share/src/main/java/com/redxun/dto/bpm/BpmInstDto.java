package com.redxun.dto.bpm;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 流程实例DTO
 */
@Data
public class BpmInstDto extends BaseDto {
    //INST_ID_
    private String instId;
    //流程定义ID
    private String defId;
    //ACT实例ID
    private String actInstId;
    //ACT定义ID
    private String actDefId;
    //解决方案ID_
    private String defCode;
    //流程实例工单号
    private String instNo;
    //业务单号
    private String billNo;
    //标题
    private String subject;
    //运行状态
    private String status;
    //版本
    private Integer version;
    //业务键ID
    private String busKey;
    //审批正文依据ID
    private String checkFileId;
    //是否为测试
    private String isTest;
    //出错信息
    private String errors;
    //结束时间
    private java.util.Date endTime;
    //数据保存模式(all,json,db)
    private String dataSaveMode;
    //支持手机端
    private Integer supportMobile;
    //发起部门ID
    private String startDepId;
    //发起部门全名
    private String startDepFull;
    //是否复活
    private String isLive;

}
