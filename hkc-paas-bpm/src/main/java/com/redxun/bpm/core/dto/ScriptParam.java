package com.redxun.bpm.core.dto;

import com.redxun.bpm.activiti.config.VariableConfig;
import lombok.Data;

import java.util.List;

/**
 * 请求脚本配置窗口构建参数
 */
@Data
public class ScriptParam {
    //boAlias
    private String boAlias;
    //var变量
    private List<VariableConfig> varConfigs=null;

}
