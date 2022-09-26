package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.tool.BeanUtil;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * @author yjy
 */
@Data
@TableName("sys_menu")
public class SysMenu extends BaseExtEntity<String> {
	private static final long serialVersionUID = 749360940290141180L;
    @JsonCreator
    public SysMenu() {
    }

    //主键
    @TableId(value = "MENU_ID_",type = IdType.INPUT)
    private String id;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;
    @FieldDef(comment = "菜单名称")
    @TableField(value = "NAME_")
    private String name;

    @TableField(value = "ICON_PC_")
    private String iconPc;
    @TableField(value = "ICON_APP_")
    private String iconApp;
    @TableField(value = "PARENT_ID_")
    private String parentId;
    @TableField(value = "PATH_")
    private String path;
    @TableField(value = "SN_")
    private String sn;

    /**
    * {label:'URL访问',value:'URL'},
    * {label:'功能面板集（标签）',value:'FUNS'},
    * {label:'功能面板集（单页）',value:'FUNS_BLOCK'},
    * {label:'功能面板',value:'FUN'},
    * {label:'弹窗',value:'POP_WIN'},
    * {label:'新窗口',value:'NEW_WIN'}
     */
    @TableField(value = "SHOW_TYPE_")
    private String showType;
    @FieldDef(comment = "菜单key")
    @TableField(value = "MENU_KEY_")
    private String menuKey;


    /**
     * C: 菜单
     * F: 按钮
     * I: 接口类型
     */
    @TableField(value = "MENU_TYPE_")
    private String menuType;
    /**
     * iframe:iframe 组件
     * custom: vue组件
     */
    @TableField(value = "SETTING_TYPE_")
    private String settingType="";
    @TableField(value = "COMPONENT_")
    private String component;
    @TableField(value = "PARAMS_")
    private String params;
    @TableField(value = "BO_LIST_KEY_")
    private String boListKey;
    @TableField(value = "ICON_PIC_")
    private String iconPic;
    @TableField(value = "URL_")
    private String url;
    @TableField(value = "METHOD_")
    private String method;


    /**
     * 机构个性化配置
     * */
    @TableField(value = "INST_CONFIG_")
    private String instConfig;


    @TableField(exist = false)
    private List<SysMenu> subMenus;
    @TableField(exist = false)
    private Long roleId;
    @TableField(exist = false)
    private Set<Long> menuIds;
    @TableField(exist = false)
    private String releaseId;
    @TableField(exist = false)
    private Boolean joinBtns;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SysMenu menu=new SysMenu();
        menu.setId("0001");
        menu.setName("系统管理");

        SysMenuDto dto=new SysMenuDto();

         BeanUtil.copyNotNullProperties(dto,menu);

        System.err.println("ok");
    }
}
