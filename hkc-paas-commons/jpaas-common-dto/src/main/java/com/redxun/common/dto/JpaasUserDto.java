package com.redxun.common.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * JPAAS用户DTO
 */
@Data
public class JpaasUserDto {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 账号
     */
    private String account;
    /**
     * 用户名
     */
    private String username;

    private String fullName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 组织部门id
     */
    private String deptId;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * OpenID
     */
    private String openId;
    /**
     * 权限
     */
    private Set<String> permissions;
    /**
     * 角色
     */
    private List<String> roles;
    /**
     * 允许访问的Token
     */
    private String accessToken="";

}
