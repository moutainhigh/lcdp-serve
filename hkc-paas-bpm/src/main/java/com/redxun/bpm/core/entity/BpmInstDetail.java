package com.redxun.bpm.core.entity;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.form.BpmView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程实例明细
 */
@Data
public class BpmInstDetail {

    private BpmInst bpmInst;

    private ProcessConfig processConfig;

    private JsonResult formData;

    //是否自动跟踪
    private String tracked="0";

    private List<BpmView> bpmViews=new ArrayList<>();

    /**
     * 可以撤销。
     */
    private boolean canRevoke=false;

    public BpmInstDetail(){

    }

    /**
     * 审批意见历史
     */
    private List<BpmCheckHistory> bpmCheckHistories=new ArrayList<>();

    /**
     * 流程实例明细
     * @param bpmInst
     * @param processConfig
     * @param formData
     */
    public BpmInstDetail(BpmInst bpmInst,ProcessConfig processConfig,JsonResult formData){
        this.bpmInst=bpmInst;
        this.processConfig=processConfig;
        this.formData=formData;
    }
}
