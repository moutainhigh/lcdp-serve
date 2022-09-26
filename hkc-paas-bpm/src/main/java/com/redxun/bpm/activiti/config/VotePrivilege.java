package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 会签的特权投票
 */
@Data
public class VotePrivilege implements Serializable {
    /**
     * 行标识
     */
    private String uid;
    /**
     * 投票值
     */
    private Integer voteCount;
    /**
     * 用户与组配置
     */
    private UserGroupConfig configs;
}
