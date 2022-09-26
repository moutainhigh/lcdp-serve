package com.redxun.gateway;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统应用认证实体对象
 */
@Data
public class SysAppAuthDto extends BaseDto {
    //主键
    private String id;
    //应用ID
    private String appId;
    //方法类型
    private String method;
    //接口路径
    private String url;
}
