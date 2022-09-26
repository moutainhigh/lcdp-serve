package com.redxun.dto.sys;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统Web请求DTO
 * @author lenovo
 */
@Data
public class SysWebReqDefDto extends BaseDto {
    //主键
    private String id;

    //名称
    private String name;
    //别名
    private String alias;
    //请求地址
    private String url;
    //请求方式
    private String mode;
    //请求类型
    private String type;
    //数据类型
    private String dataType;
    //参数配置
    private String paramsSet;
    //传递数据
    private String data;
    //请求报文模板
    private String temp;
    //状态
    private String status;
}
