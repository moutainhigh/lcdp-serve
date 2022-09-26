
/**
 * <pre>
 *
 * 描述：sys_app实体类定义
 * 表:sys_app
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-22 20:41:51
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Data
@TableName(value = "sys_app")
public class SysApp  extends BaseExtEntity<String> {

    @JsonCreator
    public SysApp() {
    }

    /**
    * 平台内置应用
    **/
    public  static final int  TYPE_FIXED = 0;
    /**
     * 平台二次开发应用
     **/
    public  static final int  TYPE_USER = 1;
    /**
     *  其他系统应用
     **/
    public  static final int  TYPE_OUT = 2;
    //主键
    @TableId(value = "app_id_",type = IdType.INPUT)
	private String appId;

    //应用编码
    @TableField(value = "client_code_")
    private String clientCode;
    //应用名称
    @FieldDef(comment = "应用名称")
    @TableField(value = "client_name_")
    private String clientName;

    //分类ID
    @TableField(value = "CATEGORY_ID_")
    private String categoryId;

    //图标
    @TableField(value = "icon_")
    private String icon;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //描述
    @TableField(value = "descp_")
    private String descp;
    //序号
    @TableField(value = "sn_")
    private Integer sn;
    //首页地址
    @TableField(value = "HOME_URL_")
    private String homeUrl;
    //首页类型
    @TableField(value = "HOME_TYPE_")
    private String homeType;
    //弹出方式
    @TableField(value = "URL_TYPE_")
    private String urlType;
    //图标显示名
    @TableField(value = "ICON_PIC_")
    private String iconPic;
    //页面
    @TableField(value = "LAYOUT_")
    private String layout;
    //父节点
    @TableField(value = "PARENT_MODULE_")
    private String parentModule;
    @TableField(value = "PARAMS_")
    private String params;
    @TableField(value = "IS_AUTH_")
    private String isAuth;
    @TableField(value = "AUTH_SETTING_")
    private String authSetting;
    @TableField(value = "PATH_")
    private String path;

    /**
     * 所属公司ID
     * */
    @TableField(value = "COMPANY_ID_")
    private String companyId;


    /**
     * 是否共享 。
     * 这个共享 是租户管理员在授权时可见。
     */
    @TableField(value = "SHARE_",jdbcType = JdbcType.VARCHAR)
    private String share= MBoolean.N.val;

    /**
     * 是否付费
     */
    @FieldDef(comment = "是否付费：Y: 是, N: 否")
    @TableField(value = "FREE_",jdbcType = JdbcType.VARCHAR)
    private String free = MBoolean.N.val;


    @FieldDef(comment = "应用类型=>0:系统内置，1:用户二次开发，2:外部")
    @TableField(value = "APP_TYPE_")
    private int appType;

    @FieldDef(comment = "菜单导航方式：0:内置,1:微前端")
    @TableField(value = "MENU_NAV_TYPE_")
    private int menuNavType;

    @FieldDef(comment = "版本号")
    @TableField(value = "VERSION_")
    private String version;

    @FieldDef(comment = "版权所有")
    @TableField(value = "COPYRIGHT_")
    private String copyright;

    @FieldDef(comment = "PC端可用")
    @TableField(value = "PC_USE_")
    private int pcUse;

    @FieldDef(comment = "手机端可用")
    @TableField(value = "MOBILE_USE_")
    private int mobileUse;

    @FieldDef(comment = "手机端首页地址")
    @TableField(value = "MOBILE_HOME_")
    private String mobileHome;

    @FieldDef(comment = "图标背景颜色")
    @TableField(value = "BACK_COLOR_")
    private String backColor;

    //是否收藏（0未收藏，1已收藏）
    @TableField(exist = false)
    private int isFav;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(exist = false)
    private Date lastUseTime;

    @Override
    public String getPkId() {
        return appId;
    }

    @Override
    public void setPkId(String pkId) {
        this.appId=pkId;
    }
}



