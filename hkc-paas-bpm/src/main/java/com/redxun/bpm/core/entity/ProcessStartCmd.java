package com.redxun.bpm.core.entity;

import lombok.Data;

@Data
public class ProcessStartCmd extends AbstractExecutionCmd{



    /**
     * 流程定义Id
     */
    private String defId;

    /**
     * 流程定义Key
     */
    private String defKey;
    /**
     * Act流程定义Id
     */
    private String actDefId;
    /**
     * 表单方案别名
     */
    private String formSolutionAlias;
    /**
     * 事项标题
     */
    private String subject;
    /**
     * 业务主键
     */
    private String busKey;
    /**
     * 单据类型
     */
    private String billType;

    /**
     * 流程实例ID
     */
    private String instId="";

    /**
     * 父流程实例ID
     */
    private String parentActInstId="";

    /**
     * BO业务模型。
     */
    private String boAlias="";


    /**
     * 这个主要是处理当业务数据已存在的情况来启动流程。
     * 默认是false，如果设置成true ，那么也会增加BPM_INST_DATA的数据关联。
     */
    private boolean hasPk=false;


}
