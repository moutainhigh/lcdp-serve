package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 业务实体配置
 */
@Data
public class EntSetting implements Serializable {
    /**
     * 实体别名
     */
    private  String alias="";
    /**
     * 实体名称
     */
    private String name="";

    /**
     * 实体属性配置
     */
    private List<AttrSetting> attrData;


}
