package com.redxun.bpm.core.entity;

/**
 * 流程实例全局变量
 */
public enum BpmInstVars {
    INST_ID("instId","流程实例ID"),
    ACT_INST_ID("actInstId","Act流程实例ID"),
    //ACT_INST_ID("actInstId","ACT流程实例ID"),
    DEF_ID("defId","流程定义ID"),
    START_USER_ID("startUserId","发起用户ID"),
    START_DEP_ID("startDepId","发起部门ID"),
    BUS_KEY("busKey","业务主键"),
    BILL_NO("billNo","流程单号"),
    BILL_TYPE("billType","单据类型"),
    NODE_USER_IDS("nodeUserIds","节点人员ID映射"),
    PROCESS_SUBJECT("processSubject","流程标题"),
    MAIN_INST_IDS("mainActInstId","主流程实例ID"),
    SUB_INST_IDS("subInstIds","子流程实例ID");


    String key;
    String name;
    BpmInstVars(String key,String name){
        this.key=key;
        this.name=name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
