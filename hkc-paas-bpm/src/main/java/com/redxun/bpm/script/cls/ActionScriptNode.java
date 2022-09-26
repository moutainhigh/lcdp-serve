package com.redxun.bpm.script.cls;

import lombok.Data;

import java.util.List;

@Data
public class ActionScriptNode extends BaseScriptNode{

    private String fieldName;
    private String fieldTitle;
    private String fieldDescp;

    public ActionScriptNode(String fieldName, String fieldTitle, String fieldDescp) {
        this.fieldName = fieldName;
        this.fieldTitle = fieldTitle;
        this.fieldDescp = fieldDescp;
    }

    public ActionScriptNode(String fieldName, String fieldTitle) {
        this.fieldName = fieldName;
        this.fieldTitle = fieldTitle;

    }

    @Override
    public String getKey() {
        return fieldName;
    }

    @Override
    public String getTitle() {
        return fieldTitle;
    }

    @Override
    public String getDescription() {
        return fieldDescp;
    }

    @Override
    public boolean getIsLeaf() {
        return true;
    }

    @Override
    public List<IScriptNode> getChildren() {
        return null;
    }

    public static final String VAR_PRE="cmd";
}
