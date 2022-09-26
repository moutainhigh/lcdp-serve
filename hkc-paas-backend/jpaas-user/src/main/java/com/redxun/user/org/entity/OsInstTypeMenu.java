package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
/**
 * <pre>
 *
 * 描述：机构类型授权菜单实体类定义
 * 表:os_inst_type_menu
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-23 20:27:27
 * 版权：广州红迅软件
 * </pre>
 */

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_inst_type_menu")
public class OsInstTypeMenu  extends BaseExtEntity<String> {

    @JsonCreator
    public OsInstTypeMenu() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //机构类型ID
    @TableField(value = "INST_TYPE_ID_")
    private String instTypeId;
    //应用ID
    @TableField(value = "APP_ID_")
    private String appId;
    //单据列表KEY
    @TableField(value = "BO_LIST_KEY_",jdbcType=JdbcType.VARCHAR)
    private String boListKey;
    //展示组件
    @TableField(value = "COMPONENT_",jdbcType=JdbcType.VARCHAR)
    private String component;
    //APP图标样式
    @TableField(value = "ICON_APP_",jdbcType=JdbcType.VARCHAR)
    private String iconApp;
    //PC图标样式
    @TableField(value = "ICON_PC_",jdbcType=JdbcType.VARCHAR)
    private String iconPc;
    //图标
    @TableField(value = "ICON_PIC_",jdbcType=JdbcType.VARCHAR)
    private String iconPic;

    //菜单ID
    @TableField(value = "MENU_ID_",jdbcType=JdbcType.VARCHAR)
    private String menuId;
    //菜单唯一标识
    @TableField(value = "MENU_KEY_",jdbcType=JdbcType.VARCHAR)
    private String menuKey;
    //菜单类型
    @TableField(value = "MENU_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String menuType;
    //接口方法
    @TableField(value = "METHOD_",jdbcType=JdbcType.VARCHAR)
    private String method;
    //菜单名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //菜单参数
    @TableField(value = "PARAMS_",jdbcType=JdbcType.VARCHAR)
    private String params;
    //上级父ID
    @TableField(value = "PARENT_ID_",jdbcType=JdbcType.VARCHAR)
    private String parentId;
    //路径
    @TableField(value = "PATH_",jdbcType=JdbcType.VARCHAR)
    private String path;
    //配置类型(custom,iframe)
    @TableField(value = "SETTING_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String settingType;
    //访问方式
    @TableField(value = "SHOW_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String showType;
    //序号
    @TableField(value = "SN_",jdbcType=JdbcType.NUMERIC)
    private Integer sn;
    //接口地址
    @TableField(value = "URL_",jdbcType=JdbcType.VARCHAR)
    private String url;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


}



