package com.redxun.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.SysPropertiesUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: yjy
 * @Date: 2019/12/18 11:40
 */

@Setter
@Getter
public class JPaasUser implements SocialUserDetails, IUser {

    private static final long serialVersionUID = 1L;

    public JPaasUser(){

    }

    /**
     * 用户id
     */
    private String userId;
    /**
     * 账号
     */
    private String account;

    private String fullName;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户标签
     */
    private String tenantLabel;
    /**
     * 组织部门id
     */
    private String deptId;
    /**
     * 组织部门name
     */
    private String deptName;

    private String password;

    private String email;

    private String mobile;

    private List<String> roles;

    private String rootPath;

    private String status;

    private String photo;



    /**
     * 是否超管。
     */
    private boolean admin=false;


    private String userType="";

    /**
     * 当前公司ID，当用户属于多个分公司的时候，可以切换分公司。
     */
    private String companyId;

    /**
     * 初始公司ID，查找用户主部门所属的公司ID.
     */
    public String originCompanyId;



    /**
     * 账号是否锁定
     */
    private String isLock;

    /**
     * 密码修改时间
     */
    private Date pwdUpdateTime;

    /**
     * 密码修改时间
     */
    private Date createTime;

    /**
     * 用户是否首次登录
     */
    private String isFirstLogin;

    /***
     * 权限重写
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        return collection;
    }

    @Override
    public String getUsername() {
        return this.account;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(IUser.STATUS_ENABLED.equals(getStatus())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status=status;
    }



    /**
     * 是否根租户管理员
     * @return
     */
    public boolean isRootAdmin(){
        if(IUser.ROOT_TENANT_ID.equals(this.tenantId) && this.admin){
            return  true;
        }
        return false;
    }

    /**
     * 是否根租户管理员
     * @return
     */
    public boolean isTenantAdmin(){
        if(!IUser.ROOT_TENANT_ID.equals(this.tenantId) && this.admin){
            return  true;
        }
        return false;
    }

    @Override
    public String getCompanyId(){
        //是否支持分公司
        boolean support= SysPropertiesUtil.getSupportGradeConfig();
        //不支持直接返回0
        if(!support){
            return CommonConstant.COMPANY_ZERO;
        }
        return this.companyId;
    }

    /**
     * 是否切换了分公司
     * @return
     */
    public boolean isSwitchedCompany(){
        boolean support= SysPropertiesUtil.getSupportGradeConfig();
        if(!support){
            return false;
        }
        return !this.companyId.equals(this.originCompanyId);
    }



    private List<OsUserPlatformDto> platforms;

    /**
     * 根据平台类型获取公众号。
     * @param platform
     * @return
     */
    public String getOpenId(Integer platform){
        if(BeanUtil.isEmpty(platforms)){
            return "";
        }
        for(OsUserPlatformDto dto:platforms){
            if(platform.equals( dto.getPlatformType())){
                return  dto.getOpenId();
            }
        }
        return "";
    }

    /**
     * 设置openId。
     * @param platform  平台
     * @param openId    openId
     */
    public void setOpenId(Integer platform,String openId){
        if(BeanUtil.isEmpty(platforms)){
            return ;
        }
        for(OsUserPlatformDto dto:platforms){
            if(platform.equals( dto.getPlatformType())){
                dto.setOpenId(openId);
            }
        }
    }




}
