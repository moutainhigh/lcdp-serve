package com.redxun.bpm.script.cls;

import lombok.Data;

/**
 * 基础脚本节点类型
 */
public abstract class BaseScriptNode implements IScriptNode{

    /**
     * 节点类型
     */
    protected String nodeType;

    /**
     * 前缀
     */
    protected String preFix;

    @Override
    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String getPreFix() {
        return preFix;
    }

    public void setPreFix(String preFix) {
        this.preFix = preFix;
    }
}
