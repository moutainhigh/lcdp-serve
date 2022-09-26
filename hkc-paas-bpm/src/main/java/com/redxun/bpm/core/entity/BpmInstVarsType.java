package com.redxun.bpm.core.entity;

import lombok.Data;

@Data
public class BpmInstVarsType {
    public BpmInstVarsType() {

    }

    public BpmInstVarsType(BpmInstVars bpmInstVars) {
        this.label = bpmInstVars.getName();
        this.key = bpmInstVars.getKey();
    }


    String label;

    String key;
}
