
/**
 * <pre>
 *
 * 描述：INS_MSG_DEF实体类定义
 * 表:ins_msg_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-25 17:49:56
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 消息定义
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "ins_msg_def")
public class InsMsgDef  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsMsgDef() {
    }

    //主键
    @TableId(value = "MSG_ID_",type = IdType.INPUT)
	private String msgId;

    //颜色
    @TableField(value = "COLOR_")
    private String color;
    //更多URL
    @TableField(value = "URL_")
    private String url;
    //图标
    @TableField(value = "ICON_")
    private String icon;
    //标题
    @FieldDef(comment = "标题")
    @TableField(value = "CONTENT_")
    private String content;
    //数据库名字
    @TableField(value = "DS_NAME_")
    private String dsName;
    //数据库别名
    @TableField(value = "DS_ALIAS_")
    private String dsAlias;
    //SQL语句
    @TableField(value = "SQL_FUNC_")
    private String sqlFunc;
    //类型
    @TableField(value = "TYPE_")
    private String type;
    //数量比较类型
    @TableField(value = "COUNT_TYPE_")
    private String countType;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private Object count;

    @Override
    public String getPkId() {
        return msgId;
    }

    @Override
    public void setPkId(String pkId) {
        this.msgId=pkId;
    }
}



