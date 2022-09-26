
/**
 * <pre>
 *
 * 描述：INS_MSGBOX_BOX_DEF实体类定义
 * 表:INS_MSGBOX_BOX_DEF
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-26 09:41:57
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

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "INS_MSGBOX_BOX_DEF")
public class InsMsgboxBox  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsMsgboxBox() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //SN_
    @TableField(value = "SN_")
    private int sn;
    //MSG_ID_
    @TableField(value = "MSG_ID_")
    private String msgId;
    //BOX_ID_
    @TableField(value = "BOX_ID_")
    private String boxId;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private String content;
    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



