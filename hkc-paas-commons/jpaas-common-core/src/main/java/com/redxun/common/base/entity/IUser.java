package com.redxun.common.base.entity;

import java.util.List;

public interface IUser {

    String ROOT_TENANT_ID="1";

    String STATUS_ENABLED="1";

    String STATUS_DISABLED="0";

     String SEPARATOR="##";

    /**
     * 用户id
     */
    String getUserId();

    /**
     * 用户id
     */
    void setUserId(String userId);
    /**
     * 账号
     */
    String getAccount();
    /**
     * 账号
     */
    void setAccount(String account);


    String getFullName();

    void setFullName(String fullName);

    String getEmail();

    void setEmail(String email);

    String getMobile();

    void setMobile(String mobile);

    /**
     * 租户ID
     */
    String getTenantId();

    /**
     * 租户ID
     */
    void setTenantId(String tenantId);
    /**
     * 租户ID
     */
    String getTenantLabel();

    /**
     * 租户ID
     */
    void setTenantLabel(String tenantLabel);
    /**
     * 组织部门id
     */
    String getDeptId();

    /**
     * 组织部门id
     */
    void setDeptId(String deptId);

    /**
     * 组织部门name
     */
    String getDeptName();

    void setDeptName(String deptName);

    String getPassword();

    void setPassword(String password);

    String getStatus();

    void setStatus(String status);


    List<String> getRoles();


    void setRoles(List<String> roles);

    String getRootPath();

    void setRootPath(String rootPath);

    String getPhoto();

    void setPhoto(String photo);

//    void setWxOpenId(String wxOpenId);
//
//    String getWxOpenId();
//
//    void setDdId(String ddId);
//
//    String getDdId();

    boolean isAdmin();

    String getCompanyId();



}
