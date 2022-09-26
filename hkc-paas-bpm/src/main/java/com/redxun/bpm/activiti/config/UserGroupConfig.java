package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户与组的配置
 * @author csx
 */
@Data
public class UserGroupConfig implements Serializable {

    /**
     * 组名称
     */
    private String name="";

    /**
     * 组条件
     */
    private String condition="";

    /**
     * 通知类型
     */
    private String infoTypes="";


    private List<UserConfig> configList=new ArrayList<>();


}
