package com.redxun.bpm.script.cls;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProcessVarNode extends BaseScriptNode{

    private String varKey;

    private String varName;

    /**
     * 是否为树节点
     */
    private boolean isLeaf=false;


    private List<IScriptNode> children=new ArrayList<>();

    public ProcessVarNode(){

    }

    public ProcessVarNode(String varKey,String varName){
        this.varKey=varKey;
        this.varName=varName;
    }

    @Override
    public String getKey() {
        return varKey;
    }

    @Override
    public String getTitle() {
        return varName;
    }

    @Override
    public String getDescription() {
        return this.varName;
    }

    @Override
    public String getNodeType() {
        return NODE_TYPE_ATTR;
    }

    @Override
    public boolean getIsLeaf() {
        return isLeaf;
    }

    @Override
    public List<IScriptNode> getChildren() {
        return children;
    }

    public static final String VAR_PRE="vars";
}
