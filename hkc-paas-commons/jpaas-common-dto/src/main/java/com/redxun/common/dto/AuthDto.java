package com.redxun.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 认证实体DTO
 */
@Data
public class AuthDto  implements Serializable {
    private static final long serialVersionUID = 749360940290141180L;

    @JsonCreator
    public AuthDto() {
    }
    //主键
    private String id;
    private String name;
    private String parentId;
    private String appId;
    //接口id_
    private String apiId;
    //接口名称
    private String apiName;
    //接口路径
    private String apiPath;
    //接口使用方法
    private Integer apiMethod;


}