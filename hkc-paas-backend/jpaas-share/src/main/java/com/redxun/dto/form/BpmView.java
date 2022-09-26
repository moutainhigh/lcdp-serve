package com.redxun.dto.form;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 启动或审批流程页面的数据对象包装。
 */
@Getter
@Setter
public class BpmView {
    /**
     * 表单ID
     */
    private String formId="";
    /**
     * 表单别名
     */
    private String alias="";
    /**
     * 主键字段
     */
    private String idField="";

    /**
     * BO别名。
     */
    private String boAlias="";
    /**
     * 名称
     */
    private String title="";

    /**
     * 表单数据
     */
    private JSONObject data;

    /**
     * 表单模版
     */
    private String template="";

    /**
     * 权限数据
     */
    private JSONObject permission;

    /**
     * 脚本数据
     */
    private String script="";

    /**
     * 按钮配置。
     */
    private String buttons="";

    /**
     * 按钮配置。
     */
    private String metadata="";

    /**
     *审批意见配置
     */
    private List opinionSetting;
    /**
     * 实体名称列表。
     */
    private Map<String,String> entMap;

    /**
     * 使用向导。
     */
    private int wizard=0;

    /**
     * 类型
     */
    private String type;
}
