package com.redxun.dto.user;

import com.redxun.common.base.entity.IUser;
import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 系统用户DTO
 * 参考jpaas-user中的OsUser类
 */
@Getter
@Setter
public class OsUserDto extends BaseDto implements IUser {

    public OsUserDto(){}

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 姓名
     */
    private String fullName;
    /**
     * 编码或员工号或账号
     */
    private String userNo;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 入职时间
     */
    private Date entryTime;
    /**
     * 离职时间
     */
    private Date quitTime;
    /**
     * 用户类型Code
     */
    private String userType;
    /**
     * 数据来源
     */
    private String from;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 性别 male/female
     */
    private String sex;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮件
     */
    private String email;
    /**
     * 地址
     */
    private String address;
    /**
     * 紧急联系人
     */
    private String urgent;
    /**
     * 紧急联系人电话
     */
    private String urgentMobile;
    /**
     * 同步微信
     */
    private Integer syncWx;
    /**
     * QQ
     */
    private String qq;
    /**
     * 照片
     */
    private String photo;
    /**
     * OPEN ID
     */
    private String openId;
    /**
     * 是否启用
     */
    private String enabled;
    /**
     * 状态
     */
    private String status;
    /**
     * 父目录
     */
    private String rootPath;
    /**
     * 部门名
     */
    private String deptName;
    /**
     * Weixin Open ID
     */
    private String wxOpenId;
    /**
     * 钉钉 ID
     */
    private String ddId;
    private String userNumber;
    private String workPhone;
    private String credNumber;
    private String credType;
    private String userGroupName;
    private boolean admin=false;
    private String companyId;
    private String subCompanyId;

    /**
     * 飞书 ID
     */
    private String fsOpenId;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户标签
     */
    private String tenantLabel;
    /**
     * 主部门ID
     */
    private String deptId;
    /**
     * 所属角色
     */
    private List<String> roles;


    @Override
    public String getAccount() {
        return this.userNo;
    }

    @Override
    public void setAccount(String account) {
        this.userNo=account;
    }


    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public void setPassword(String password) {
        this.pwd=password;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public String getCompanyId() {
        return null;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OsUserDto userDto = (OsUserDto) o;
        return Objects.equals(userId, userDto.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
