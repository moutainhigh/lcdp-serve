package com.redxun.bpm.core.enums;

/**
 * 流程ProcessNextCmd属性枚举属性变量
 */
public enum ProcessCmdAttrEnum {
    DEF_ID("defId","流程定义ID","流程定义ID"),
    TASK_ID("taskId","任务ID","任务ID"),
    INST_ID("instId","流程实例Id","流程实例Id"),
    CHECK_ACTION("checkType","审批动作","AGREE(\"通过\"),OVERTIME_AUTO_AGREE(\"超时审批\"),SKIP(\"跳过\"),RECOVER(\"撤回\"),TIMEOUT_SKIP(\"超时跳过\"),REFUSE(\"不同意\"),COMMUNICATE(\"沟通\"),REPLY_COMMUNICATE(\"回复沟通\"),CANCEL_COMMUNICATE(\"取消沟通\"),BACK(\"驳回\"),BACK_TO_STARTOR(\"驳回发起人\"),BACK_SPEC(\"驳回节点\"),BACK_GATEWAY(\"驳回网关\"),ABSTAIN(\"弃权\"),BACK_CANCEL(\"驳回撤销\"),\tRECOVER_CANCEL(\"撤回撤销\"),TRANSFER(\"转办\"),INTERPOSE(\"干预\");"),
    CHECK_OPINION("opinion","审批意见","审批意见"),
    FORM_DATA("formData","单据数据","单据数据(JSONObject)");

    String key;
    String title;
    String description;

    ProcessCmdAttrEnum(String key,String title,String description){
        this.key=key;
        this.title=title;
        this.description=description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
