package com.redxun.bpm.activiti.config;

import lombok.Data;

/**
 * 用户与组或组合配置
 */
@Data
public class UserGroupComb extends UserGroupConfig{
    /**
     * 是否全部 当值=true 表示代表所有人都有权限访问
     */
    private String all;
}
