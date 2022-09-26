package com.redxun.user.org.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * 用户实体类
 * @author yjy
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_user")
public class OsUser extends BaseExtEntity<String> {
    /**
     *  接口同步=INTERFACE
     */
    public final static String FORM_INTERFACE="INTERFACE";

    public final static String ADMIN_ACCOUNT="admin";

    public final static String FROM_SYS="system";


    public final static String STATUS_IN_JOB="1";

    public final static String STATUS_OUT_JOB="0";


    public final static String MALE="male";

    public final static String FMALE="fmale";

    public final static String WEIXIN="weixin";
    public final static String DD="dd";

    public  final  static String ADMIN="1";

	private static final long serialVersionUID = -5886012896705137070L;


    @TableId(value = "USER_ID_",type = IdType.INPUT)
    private String userId;

    @FieldDef(comment = "用户名")
    @TableField(value = "FULLNAME_")
    private String fullName;

    @FieldDef(comment = "账号")
    @TableField(value = "USER_NO_")
    private String userNo;
    /**
     * 密码
     */
    @TableField(value = "PWD_")
    private String pwd;

    /**
     * 入职时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "ENTRY_TIME_")
    private Date entryTime;
    /**
     * 离职时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "QUIT_TIME_")
    private Date quitTime;
    /**
     * 用户类型
     */
    @TableField(exist = false)
    private String userType;
    @TableField(value = "FROM_")
    private String from;
    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "BIRTHDAY_")
    private Date birthday;

    /**
     * 性别
     */
    @TableField(value = "SEX_")
    private String sex;
    /**
     * 手机号
     */
    @TableField(value = "MOBILE_")
    private String mobile;
    /**
     * 邮件
     */
    @TableField(value = "EMAIL_")
    private String email;
    /**
     * 地址
     */
    @TableField(value = "ADDRESS_")
    private String address;
    /**
     * 紧急联系人
     */
    @TableField(value = "URGENT_")
    private String urgent;
    /**
     * 同步微信
     */
    @TableField(value = "SYNC_WX_")
    private Integer syncWx;
    /**
     * 紧急人手机号
     */
    @TableField(value = "URGENT_MOBILE_")
    private String urgentMobile;
    /**
     * QQ
     */
    @TableField(value = "QQ_")
    private String qq;
    /**
     * 相片
     */
    @TableField(value = "PHOTO_")
    private String photo;
    /**
     * 状态
     */
    @TableField(value = "STATUS_")
    private String status;
    /**
     * 是否启用
     */
    @TableField(value = "ENABLED_")
    private String enable;
    /**
     * 微信OpenID
     */
    @TableField(value = "WX_OPEN_ID_",updateStrategy = FieldStrategy.IGNORED)
    private String wxOpenId;
    /**
     * 钉钉ID
     */
    @TableField(value = "DD_ID_",updateStrategy = FieldStrategy.IGNORED)
    private String ddId;


    @TableField(exist = false)
    private String curTenantId;

    @TableField(exist = false)
    private String userGroupName;
    @TableField(exist = false)
    private String userGroupId;
	@TableField(exist = false)
	private List<OsUserType> roles;
    /**
     * 角色ID
     */
	@TableField(exist = false)
	private String roleId;
    /**
     * 旧密码
     */
	@TableField(exist = false)
	private String oldPassword;
    /**
     * 新密码
     */
	@TableField(exist = false)
	private String newPassword;
    /**
     * 关系类型ID
     */
    @TableField(exist = false)
    private String relTypeId;
    /**
     * 关系实例ID
     */
    @TableField(exist = false)
    private String relInstId;
    /**
     * 主部门ID
     */
    @TableField(exist = false)
    private String mainDepId;
    /**
     * 主部门名称
     */
    @TableField(exist = false)
    private String deptName;
    /**
     * 候选部门ID
     */
    @TableField(exist = false)
    private String canGroupIds;
    /**
     * 用户类型名称
     */
    @TableField(exist = false)
    private String userTypeName;
    /**
     * 是否管理员
     */
    @TableField(exist = false)
    private int admin=0;
    //关系
    @TableField(exist = false)
    private JSONArray relations;
    //扩展属性
    @TableField(exist = false)
    private List<OsPropertiesGroup> propertiesGroups;

    @TableField(exist = false)
    private Boolean isAddAdmin = false;

    /**
     * 账号是否锁定
     */
    @TableField(value = "IS_LOCK_")
    private String isLock;

    /**
     * 密码修改时间
     */
    @TableField(value = "PWD_UPDATE_TIME_")
    private Date pwdUpdateTime;

    /**
     * 用户是否首次登录
     */
    @TableField(value = "IS_FIRST_LOGIN_")
    private String isFirstLogin;

    /**
     * 序号
     */
    @TableField(value = "SN_")
    private int sn;


    @Override
    public String getPkId() {
        return userId;
    }

    @Override
    public void setPkId(String pkId) {
        this.userId=pkId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        if (!super.equals(o)) {return false;}
        OsUser osUser = (OsUser) o;
        return admin == osUser.admin &&
                Objects.equals(userId, osUser.userId) &&
                Objects.equals(fullName, osUser.fullName) &&
                Objects.equals(userNo, osUser.userNo) &&
                Objects.equals(pwd, osUser.pwd) &&
                Objects.equals(entryTime, osUser.entryTime) &&
                Objects.equals(quitTime, osUser.quitTime) &&
                Objects.equals(userType, osUser.userType) &&
                Objects.equals(from, osUser.from) &&
                Objects.equals(birthday, osUser.birthday) &&
                Objects.equals(sex, osUser.sex) &&
                Objects.equals(mobile, osUser.mobile) &&
                Objects.equals(email, osUser.email) &&
                Objects.equals(address, osUser.address) &&
                Objects.equals(urgent, osUser.urgent) &&
                Objects.equals(syncWx, osUser.syncWx) &&
                Objects.equals(urgentMobile, osUser.urgentMobile) &&
                Objects.equals(qq, osUser.qq) &&
                Objects.equals(photo, osUser.photo) &&
                Objects.equals(status, osUser.status) &&
                Objects.equals(enable, osUser.enable) &&
                Objects.equals(wxOpenId, osUser.wxOpenId) &&
                Objects.equals(ddId, osUser.ddId) &&
                Objects.equals(roles, osUser.roles) &&
                Objects.equals(roleId, osUser.roleId) &&
                Objects.equals(oldPassword, osUser.oldPassword) &&
                Objects.equals(newPassword, osUser.newPassword) &&
                Objects.equals(relTypeId, osUser.relTypeId) &&
                Objects.equals(relInstId, osUser.relInstId) &&
                Objects.equals(mainDepId, osUser.mainDepId) &&
                Objects.equals(canGroupIds, osUser.canGroupIds) &&
                Objects.equals(userTypeName, osUser.userTypeName) &&
                Objects.equals(relations, osUser.relations) &&
                Objects.equals(propertiesGroups, osUser.propertiesGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, fullName, userNo, pwd, entryTime, quitTime, userType, from, birthday, sex, mobile, email, address, urgent, syncWx, urgentMobile, qq, photo,  status, enable, wxOpenId, ddId, roles, roleId, oldPassword, newPassword, relTypeId, relInstId, mainDepId, canGroupIds, userTypeName, admin, relations, propertiesGroups);
    }
}
