package com.redxun.bpm.activiti.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 外部子流程定义配置
 */
@Data
public class SubProcessDefConfig implements Serializable {
    /**
     * 名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 配置
     */
    private JSONObject config;
}
