package com.redxun.dto.user;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 租户机构DTO
 * @author hujun
 */
@Data
public class OsInstDto extends BaseDto {

    public final static String ROOT_INST="1";
    /**
     * 机构ID
     */
    private String instId;
    /**
     * 中文全名
     */
    private String nameCn;
    /**
     * 英文全名
     */
    private String nameEn;
    /**
     * 机构编码
     */
    private String instNo;
    /**
     * 营业执照编码
     */
    private String busLiceNo;
    /**
     * 域名
     */
    private String domain;
    /**
     * 中文简称
     */
    private String nameCnS;
    /**
     * 英文简称
     */
    private String nameEnS;
    /**
     * 法人
     */
    private String legalMan;
    /**
     * 公司描述
     */
    private String descp;
    /**
     * 公司地址
     */
    private String address;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮件
     */
    private String email;
    /**
     * 传真
     */
    private String fax;
    /**
     * 联系人
     */
    private String contractor;
    /**
     * 网地
     */
    private String homeUrl;
    /**
     * 机构类型
     */
    private String instType;
    /**
     * 地址
     */
    private String status;
    /**
     * 父ID
     */
    private String parentId;
    /**
     * 路径
     */
    private String path;
    /**
     * 数据源
     */
    private String datasource;
    /**
     * 标签
     */
    private String label;

}
