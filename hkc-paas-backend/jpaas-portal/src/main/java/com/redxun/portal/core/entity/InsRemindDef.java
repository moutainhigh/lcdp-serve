
/**
 * <pre>
 *
 * 描述：消息提醒实体类定义
 * 表:ins_remind_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-25 15:54:11
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 消息提醒
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "ins_remind_def")
public class InsRemindDef  extends BaseExtEntity<java.lang.String> {

    public static final String FUNCTION_TYPE="function";
    public static final String SQL_TYPE="sql";
    public static final String TYPE_ENABLED="YES";

    public static final String INS_REMIND_DEF="insFemindDef";

    @JsonCreator
    public InsRemindDef() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //主题
    @TableField(value = "SUBJECT_")
    private String subject;
    //连接地址
    @TableField(value = "URL_")
    private String url;
    //设置类型 function方法,SQL:sql
    @TableField(value = "TYPE_")
    private String type;
    //SQL语句或方法
    @TableField(value = "SETTING_")
    private String setting;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;
    //序号
    @TableField(value = "SN_")
    private Integer sn;
    //是否有效 YES.有效NO.无效
    @TableField(value = "ENABLED_")
    private String enabled;
    //图标
    @TableField(value = "ICON_")
    private String icon;
    //数据源名字
    @TableField(value = "DS_NAME_")
    private String dsName;
    //数据源别名
    @TableField(value = "DS_ALIAS_")
    private String dsAlias;

    @TableField(exist = false)
    private int count;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



