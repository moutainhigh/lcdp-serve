package com.redxun.bpm.script.cls;

import lombok.Data;

import java.util.List;

/**
 * 对话框脚本节点定义
 *
 */
@Data
public class DialogScriptNode extends BaseScriptNode{
    /**
     * 字段
     */
    private String dialogField;
    /**
     * 标题
     */
    private String dialogTitle;
    /**
     * 对话框类型
     */
    private String dialogType;

    public DialogScriptNode(String dialogField, String dialogTitle, String dialogType) {
        this.dialogField = dialogField;
        this.dialogTitle = dialogTitle;
        this.dialogType = dialogType;
    }

    @Override
    public String getKey() {
        return "dialog_"+dialogField;
    }

    @Override
    public String getTitle() {
        return dialogTitle;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean getIsLeaf() {
        return true;
    }

    @Override
    public List<IScriptNode> getChildren() {
        return null;
    }

}
