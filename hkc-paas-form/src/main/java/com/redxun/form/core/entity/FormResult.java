package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FormResult {

    private FormSolution formSolution;

    private JSONObject data;

    private String template="";

    private JSONObject permission;

    private String script="";

    /**
     * 按钮配置。
     */
    private String buttons="";

    /**
     * 字段属性。
     */
    private String metadata="";

    /**
     * 方案别名。
     */
    private String alias="";

    /**
     * 方案名称。
     */
    private String name="";

    /**
     * 主键字段
     */
    private String idField;

    /**
     * 是否能启动流程。
     */
    private boolean canStartFlow=false;

    private IUser curUser;
    /**
     * 向导表单。
     */
    private int wizard=0;
    /**
     * 表单类型
     */
    private String type;

    private JSONObject instDetail=new JSONObject();

}
