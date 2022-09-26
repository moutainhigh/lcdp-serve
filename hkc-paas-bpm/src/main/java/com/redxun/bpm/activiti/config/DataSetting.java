package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 流程数据设置
 *
 */
@Data
public class DataSetting  implements Serializable {
    //初始化脚本
    private String initScript="";
    //保存脚本
    private String saveScript="";
    //业务实体字段配置
    private List<EntSetting> fieldSetting;
    //审批意见配置
    private List<OpinionSetting> opinionSetting;
}
