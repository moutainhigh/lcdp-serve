package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redxun.common.model.SuperEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  注册机构对象 os_inst
 *
 * @author yjy
 * @date 2019-11-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_inst")
public class OsInst extends SuperEntity<String> {

    private static final long serialVersionUID=1L;

    @TableId(value = "INST_ID_",type = IdType.INPUT)
    private String instId;

    @TableField(value = "NAME_CN_")
    private String nameCn;

    @FieldDef(comment = "租户名")
    @TableField(value = "NAME_EN_")
    private String nameEn;
    @FieldDef(comment = "租户编码")
    @TableField(value = "INST_NO_")
    private String instNo;

    @TableField(value = "BUS_LICE_NO_")
    private String busLiceNo;
    @TableField(value = "DOMAIN_")
    private String domain;
    @TableField(value = "NAME_CN_S_")
    private String nameCnS;
    @TableField(value = "NAME_EN_S_")
    private String nameEnS;
    @TableField(value = "LEGAL_MAN_")
    private String legalMan;
    @TableField(value = "DESCP_")
    private String descp;
    @TableField(value = "ADDRESS_")
    private String address;
    @TableField(value = "PHONE_")
    private String phone;
    @TableField(value = "EMAIL_")
    private String email;
    @TableField(value = "FAX_")
    private String fax;
    @TableField(value = "CONTRACTOR_")
    private String contractor;
    @TableField(value = "HOME_URL_")
    private String homeUrl;
    @TableField(value = "INST_TYPE_")
    private String instType;
    @TableField(value = "STATUS_")
    private String status;
    @TableField(value = "PARENT_ID_")
    private String parentId;
    @TableField(value = "PATH_")
    private String path;
    //标签
    @TableField(value = "LABEL_")
    private String label;
    //数据源
    @TableField(value = "DATASOURCE_")
    private String datasource;

    @TableField(exist = false)
    private String typeName;
    @TableField(exist = false)
    private String moreInstStatus;
    @TableField(exist = false)
    private String moreInstNote;
    @TableField(exist = false)
    private String moreInstCreateType;
    @TableField(exist = false)
    private boolean isPresent;
    //登录账号、用于机构注册
    @TableField(exist = false)
    private  String account;
    //登录密码、用于机构注册
    @TableField(exist = false)
    private  String password;

    public String getRootAdmin(){
        return this.instNo +"_admin";
    }

    @Override
    public String getPkId() {

        return instId;
    }


    @Override
    public void setPkId(String pkId) {
        this.instId=pkId;
    }

}
