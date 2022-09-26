package com.redxun.dto.sys;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统应用日志DTO
 */
@Data
public class SysAppLogDto extends BaseDto {
    //主键
    private String id;
    //应用名称。
    private String appName="";

    //应用ID
    private String appId;
    //方法类型
    private String method;
    //接口路径
    private String url;
    //持续时间
    private Integer duration;
}
