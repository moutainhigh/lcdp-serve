
/**
 * <pre>
 *
 * 描述：栏目消息盒子表实体类定义
 * 表:INS_MSGBOX_DEF
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-25 16:28:24
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
 * 消息盒子定义
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "INS_MSGBOX_DEF")
public class InsMsgboxDef  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsMsgboxDef() {
    }

    //盒子ID
    @TableId(value = "BOX_ID_",type = IdType.INPUT)
	private String boxId;


    //标识键
    @TableField(value = "KEY_")
    @FieldDef(comment = "标识键")
    private String key;
    //名字
    @FieldDef(comment = "名字")
    @TableField(value = "NAME_")
    private String name;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    //栏目ID
    @TableField(exist = false)
    private String colId;

    @Override
    public String getPkId() {
        return boxId;
    }

    @Override
    public void setPkId(String pkId) {
        this.boxId=pkId;
    }
}



