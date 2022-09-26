package com.redxun.bpm.script.cls;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 脚本函数方法
 */
@Data
public class ScriptMethod extends BaseScriptNode {

    private Integer id;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法简述
     */
    private String title;

    /**
     * 方法描述
     */
    private String description;

    /**
     * 方法所属类
     */
    private Class cls;

    /**
     * 脚本方法参数
     */
    private List<ScriptMethodParam> params=new ArrayList<>();

    /**
     * 返回参数
     */
    private String returnType;


    @Override
    public String getKey() {
        return methodName;
    }

    @Override
    public String getTitle() {
        return "fn:"+ title;
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
