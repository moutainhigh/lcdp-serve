package com.redxun.bpm.script.cls;

import com.redxun.common.tool.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 表单字段类型
 */
@Data
public class FormItemNode extends BaseScriptNode {
    /**
     * 表单别名
     */
    private String formAlias;
    /**
     * 表单Key或字段Key
     */
    private String key;
    /**
     * 表单名或字段名
     */
    private String title;
    /**
     * 是否为树节点
     */
    private boolean isLeaf=false;
    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 是否Children
     */
    private List<IScriptNode> children=new ArrayList<>();

    public FormItemNode(){

    }

    public String getDataType(){
        if("varchar".equalsIgnoreCase(dataType)
        || "clob".equalsIgnoreCase(dataType) ){
            return "S";
        }
        if("date".equalsIgnoreCase(dataType)){
            return "D";
        }
        if("number".equalsIgnoreCase(dataType)){
            return "N";
        }
        return "S";
    }

    public FormItemNode(String formAlias,String key,String title){
        this.formAlias=formAlias;
        this.key=key;
        this.title=title;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getTitle(){
        if(StringUtils.isNotEmpty(dataType)){
            return this.title+ "("+ getDataType() + ")";
        }
        return this.title;
    }

    @Override
    public String getDescription() {
        return title;
    }

    @Override
    public boolean getIsLeaf() {
        return isLeaf;
    }


    @Override
    public String getPreFix() {
        return formAlias;
    }
}
