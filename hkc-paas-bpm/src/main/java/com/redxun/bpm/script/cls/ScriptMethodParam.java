package com.redxun.bpm.script.cls;

import lombok.Data;

/**
 * 脚本函数方法
 */
@Data
public class ScriptMethodParam {
    /**
     * 名称
     */
    String varName="";
    /**
     * 类型
     */
    String type="";
    /**
     * 描述
     */
    String description="";

    public ScriptMethodParam(){

    }

    public ScriptMethodParam(String varName,String type,String description){
        this.varName=varName;
        this.type=type;
        this.description=description;
    }
}
