package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞书用户信息
 * @author ycs
 * @version V1.0
 * @since 2022-06-14 16:20
 */
@Data
public class FeiShuUserEditReq {

    //--------------以下必填字段-----------
    /**
     * 申请了"获取用户 user_id"权限的应用返回该字段
     */
    @JSONField(name = "user_id")
    private String userId;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 部门id
     */
    @JSONField(name = "department_ids")
    private List<String> departmentIds = new ArrayList<String>();
    /**
     * 手机号，在本企业内不可重复；未认证企业仅支持添加中国大陆手机号，通过飞书认证的企业允许添加海外手机号，注意国际电话区号前缀中必须包含加号 +
     */
    private String mobile;
    /**
     * 员工类型，可选值有1正式员工，2实习生，3外包，4劳务，5顾问
     */
    @JSONField(name = "employee_type")
    private String employeeType;

    //--------------以下选填字段-----------
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
     * 邮箱，注意非 +86 手机号成员必须同时添加邮箱
     */
    private String email;

    /**
     * 性别 0:保密 1:男 2:女
     */
    private Integer gender;

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


}
