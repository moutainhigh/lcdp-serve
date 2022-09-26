package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * FomBo的头部按钮
 * @author mansan
 */
@Setter
@Getter
public class FormBoTopButton implements Serializable {
    /**
     * 按钮标签
     */
    private String btnLabel;
    /**
     * 按钮名称
     */
    private String btnName;
    /**
     * 按钮样式
     */
    private String btnIcon;
    /**
     * 按钮点击
     */
    private String btnClick;
    /**
     * 按钮配置 serverHandleScript:按钮执行脚本
     * {serverHandleScript:""}
     */
    private JSONObject config;

    public String getServerHandleScript(){
        if(BeanUtil.isEmpty(config)){
            return null;
        }
        return config.getString("serverHandleScript");
    }
}
