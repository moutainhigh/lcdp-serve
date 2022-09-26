package com.redxun.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 系统菜单DTO
 * @author yjy
 */
@Data
public class SysMenuDto extends BaseDto {
    private static final long serialVersionUID = 749360940290141180L;
    /**
     * 菜单
     */
    public static final String TYPE_MENU="C";
    /**
     * 按钮
     */
    public static final String TYPE_BUTTON="F";

    @JsonCreator
    public SysMenuDto() {
    }
    //主键
    private String id;
    //APPID
    private String appId;
    //名称
    private String name;
    //PC Icon
    private String iconPc;
    // APP ICon
    private String iconApp;
    // 父类
    private String parentId;
    // SN
    private String sn;
    // 菜单Key
    private String menuKey;
    // 显示风格
    private String showType;
    // 菜单风格
    private String menuType;
    // 设置类型
    private String settingType="";
    // 组件
    private String component;
    // 参数
    private String params;
    // 绑定BO列表Key
    private String boListKey;
    // ICON 图片
    private String iconPic;
    // Layout
    private String layout;
    // 父模块
    private String parentModule;
    // 子菜单
    private List<SysMenuDto> children;
    // APP类型
    private String appType;
    // APP路径
    private String appPath;
    // 菜单导航类型
    private String menuNavType;
    // 应用图标背景色
    private  String backColor;
    //是否收藏（0未收藏，1已收藏）
    private int isFav;
    //最近使用时间
    private String lastUseTime;
    private String url;
    private String method;
    //机构个性化配置
    private String instConfig;
    //分类ID
    private String categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysMenuDto dto = (SysMenuDto) o;
        return Objects.equals(id, dto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
