package com.redxun.bpm.core.entity;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.dto.NodeUsersDto;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务明细信息
 * @author csx
 */
@Data
public class BpmTaskDetail {
    private BpmTask bpmTask;
    private BpmInst bpmInst;
    private ProcessConfig processConfig;
    private UserTaskConfig taskConfig;
    //获取回退的节点
    private PathResult backPathResult=null;
    //是否允许驳回
    private Boolean isCanBack=false;
    //允许进行审批处理
    private Boolean canCheck=true;
    /**
     * 是否可以回复。
     */
    private Boolean canReply=true;
    /**
     * 是否能够转办。
     */
    private Boolean canTransfer=false;
    /**
     * 是否为流转任务
     */
    private Boolean isTransferRoam=false;
    /**
     * 是否可取消流转任务
     */
    private Boolean isCanRoamTransfer=false;

    //是否允许加签
    private Boolean canAddSign=false;
    //是否允许作废
    private Boolean isShowDiscardBtn=false;
    //允许审批
    private JsonResult allowApprove=JsonResult.Success();

    /**
     * 是否沟通过。
     */
    private boolean communicated=false;

    private JsonResult formData;

    private IUser curUser;

    /**
     * 审批意见历史
     */
    private List<BpmCheckHistory> bpmCheckHistories=new ArrayList<>();

    /**
     * 当前节点的后续执行人员及列表
     */
    private List<NodeUsersDto> nodeExecutors=new ArrayList<>();

    //是否自动跟踪
    private String tracked="0";

    public BpmTaskDetail(){

    }

    public BpmTaskDetail(BpmTask bpmTask,BpmInst bpmInst,ProcessConfig processConfig,UserTaskConfig taskConfig){
        this.bpmTask=bpmTask;
        this.bpmInst=bpmInst;
        this.processConfig=processConfig;
        this.taskConfig=taskConfig;
    }
}
