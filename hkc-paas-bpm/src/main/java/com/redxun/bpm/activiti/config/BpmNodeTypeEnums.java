package com.redxun.bpm.activiti.config;

/**
 * 流程节点类型与流程节点的序列化类映射
 * @auth csx
 */
public enum BpmNodeTypeEnums {
    PROCESS("bpmn:Process", ProcessConfig.class),
    START_EVENT("bpmn:StartEvent", StartEventConfig.class),
    END_EVENT("bpmn:EndEvent", EndEventConfig.class),
    USER_TASK("bpmn:UserTask", UserTaskConfig.class),
    EXCLUSIVE_GATEWAY("bpmn:ExclusiveGateway", ExclusiveGatewayConfig.class),
    INCLUSIVE_GATEWAY("bpmn:InclusiveGateway", InclusiveGatewayConfig.class),
    SUB_PROCESS("bpmn:SubProcess", SubProcessConfig.class),
    SERVICE_TASK("bpmn:ServiceTask", ServiceTaskConfig.class),
    RECEIVE_TASK("bpmn:ReceiveTask",ReceiveTaskConfig.class);


    private String typeName;

    private Class<? extends NodeConfig> configClass;

    BpmNodeTypeEnums(String typeName, Class<? extends NodeConfig> configClass) {
        this.typeName = typeName;
        this.configClass = configClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Class<? extends NodeConfig> getConfigClass() {
        return configClass;
    }

    public void setConfigClass(Class<? extends NodeConfig> configClass) {
        this.configClass = configClass;
    }
}
