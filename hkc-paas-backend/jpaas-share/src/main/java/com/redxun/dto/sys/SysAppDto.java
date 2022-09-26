package com.redxun.dto.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.dto.BaseDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 系统认证DTO
 */
@Data
public class SysAppDto extends BaseDto {

    //主键
    private String appId;

    //应用编码
    private String clientCode;
    //应用名称
    private String clientName;
    //图标
    private String icon;
    //状态
    private String status;
    //描述
    private String descp;
    //序号
    private Integer sn;
    //首页地址
    private String homeUrl;
    //首页类型
    private String homeType;
    //弹出方式
    private String urlType;
    //布局
    private String layout;
    //父组件
    private String parentModule;

    private String params;
    private String isAuth;
    private String authSetting;
    private String appType;
    private String path;
    //菜单导航方式
    private String menuNavType;
    // 应用图标背景色
    private  String backColor;
    //是否收藏（0未收藏，1已收藏）
    private int isFav;
    //最近使用时间
    private String lastUseTime;
    //分类ID
    private String categoryId;
}
