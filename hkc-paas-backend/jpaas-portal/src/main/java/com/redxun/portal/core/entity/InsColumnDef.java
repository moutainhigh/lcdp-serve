
/**
 * <pre>
 *
 * 描述：栏目定义实体类定义
 * 表:ins_column_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-26 22:44:03
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.portal.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 栏目定义
 */
@Data
@Accessors(chain = true)
@TableName(value = "ins_column_def")
public class InsColumnDef  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsColumnDef() {
    }

    //栏目ID
    @TableId(value = "COL_ID_",type = IdType.INPUT)
	private String colId;

    //栏目名
    @FieldDef(comment = "栏目定义")
    @TableField(value = "NAME_")
    private String name;
    //栏目别名
    @FieldDef(comment = "栏目别名")
    @TableField(value = "KEY_")
    private String key;
    //是否默认
    @TableField(value = "IS_DEFAULT_")
    private String isDefault;
    //模板
    @TableField(value = "TEMPLET_")
    private String templet;
    //是否公共栏目
    @TableField(value = "IS_PUBLIC_")
    private String isPublic;
    //类型
    @TableField(value = "TYPE_")
    private String type;
    //是否自定义移动栏目
    @TableField(value = "IS_MOBILE_")
    private String isMobile;

    //是否自定义移动栏目
    @TableField(value = "SET_TING_")
    private String setTing;

    //图标
    @TableField(value = "ICON_")
    private String icon;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private String typeName;


    /**
     * 消息盒子每行列数
     */
    @TableField(exist = false)
    private int listCount;

    @Override
    public String getPkId() {
        return colId;
    }

    @Override
    public void setPkId(String pkId) {
        this.colId=pkId;
    }
}



