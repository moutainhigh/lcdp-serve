package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;

/**
 * 飞书用户信息
 * @author ycs
 * @version V1.0
 * @since 2022-06-14 16:20
 */
@Data
public class FeiShuUser {
    /**
     * 用户统一ID
     */
    @JSONField(name = "union_id")
    private String unionId;
    /**
     * 申请了"获取用户 user_id"权限的应用返回该字段
     */
    @JSONField(name = "user_id")
    private String userId;
    /**
     * 用户在应用内的唯一标识
     */
    @JSONField(name = "open_id")
    private String openId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户英文名称
     */
    @JSONField(name = "en_name")
    private String enName;
    /**
     * 别名
     */
    @JSONField(name = "nickname")
    private String nickname;
    /**
     * 申请了"获取用户邮箱"权限的应用返回该字段
     */
    private String email;
    /**
     * 申请了"获取用户手机号"权限的应用返回该字段
     */
    private String mobile;
    /**
     * 性别 0:保密 1:男 2:女
     */
    private Integer gender;
    /**
     * 用户头像信息
     */
    private AvatarInfo  avatar;
    /**
     * 用户状态
     */
    private UserStatus  status;

    /**
     * 部门id
     */
    @JSONField(name = "department_ids")
    private ArrayList<String> departmentIds;
    /**
     * 用户的直接主管的用户ID，ID值与查询参数中的user_id_type 对应。
     */
    @JSONField(name = "leader_user_id")
    private String leaderUserId;
    /**
     * 城市
     */
    @JSONField(name = "city")
    private String city;
    /**
     * 国家或地区Code缩写，具体写入格式请参考
     */
    @JSONField(name = "country")
    private String country;
    /**
     * 工位
     */
    @JSONField(name = "work_station")
    private String workStation;
    /**
     *
     * 入职时间
     */
    @JSONField(name = "join_time")
    private Integer joinTime;
    /**
     * 是否是租户超级管理员
     */
    @JSONField(name = "is_tenant_manager")
    private Boolean isTenantManager;
    /**
     * 工号
     */
    @JSONField(name = "employee_no")
    private String employeeNo;
    /**
     * 员工类型，可选值有1正式员工，2实习生，3外包，4劳务，5顾问
     */
    @JSONField(name = "employee_type")
    private String employeeType;
    /**
     * 企业邮箱，请先确保已在管理后台启用飞书邮箱服务
     */
    @JSONField(name = "enterprise_email")
    private String enterpriseEmail;
    /**
     * 职务
     */
    @JSONField(name = "job_title")
    private String jobTitle;

    //orders 用户排序
    //custom_attrs 自定义字段

    @Data
    public class AvatarInfo {
        /**
         * 72*72像素头像链接
         */
        @JSONField(name = "avatar_72")
        private String avatar72;
        /**
         * 240*240像素头像链接
         */
        @JSONField(name = "avatar_240")
        private String avatar240;
        /**
         * 640*640像素头像链接
         */
        @JSONField(name = "avatar_640")
        private String avatar640;
        /**
         * 原始头像链接
         */
        @JSONField(name = "avatar_origin")
        private String avatarOrigin;
    }

    @Data
    public class UserStatus {
        /**
         * 是否暂停
         */
        @JSONField(name = "is_frozen")
        private Boolean isFrozen;
        /**
         * 是否离职
         */
        @JSONField(name = "is_resigned")
        private Boolean isResigned;
        /**
         * 是否激活
         */
        @JSONField(name = "is_activated")
        private Boolean isActivated;
        /**
         * 是否主动退出，主动退出一段时间后用户会自动转为已离职
         */
        @JSONField(name = "is_exited")
        private Boolean isExited;
        /**
         * 是否未加入，需要用户自主确认才能加入团队
         */
        @JSONField(name = "is_unjoin")
        private Boolean isUnjoin;

    }
}
