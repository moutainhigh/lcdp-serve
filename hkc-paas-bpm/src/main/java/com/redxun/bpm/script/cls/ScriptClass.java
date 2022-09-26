package com.redxun.bpm.script.cls;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 脚本分类
 */
@Data
public class ScriptClass extends BaseScriptNode {

    /**
     * 脚本描述
     */
    private String description;

//    /**
//     * 脚本实现类型
//     */
//    private Class<IScript> scriptCls;


    /**
     * 脚本方法
     */
    private List<IScriptNode> childNodes = new ArrayList<>();

    public ScriptClass(){

    }

    public ScriptClass(String preFix,String nodeType,String description){
        this.preFix=preFix;
        this.nodeType=nodeType;
        this.description=description;
    }

    @Override
    public String getKey() {
        return this.preFix;
    }

    @Override
    public String getTitle() {
        return description;
    }

    @Override
    public String getNodeType() {
        return nodeType;
    }

    @Override
    public boolean getIsLeaf() {
        return false;
    }

    @Override
    public List<IScriptNode> getChildren() {
        return childNodes;
    }
}
