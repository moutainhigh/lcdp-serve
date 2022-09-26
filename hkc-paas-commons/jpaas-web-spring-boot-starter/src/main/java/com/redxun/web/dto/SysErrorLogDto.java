package com.redxun.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysErrorLogDto {


    public SysErrorLogDto() {
    }


    //跟踪ID
    private String traceId;
    //APP_NAME_
    private String appName;
    //访问地址
    private String url;
    //错误内容
    private String content;
}
