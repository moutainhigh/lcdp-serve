package com.redxun.form.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用于Miniui的树的生成配置
 * @author mansan
 *
 */
@Setter
@Getter
public class TreeConfig implements Serializable{
    //树控件Id
    private String treeId;
    //左导航tab中的标签名
    private String tabName="";
    //URL参数名
    private String paramName="";

    private String idField;

    private String textField;

    private String parentField="";
    //点击
    private String onnodeclick="";

}
