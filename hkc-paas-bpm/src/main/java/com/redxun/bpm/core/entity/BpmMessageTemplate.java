
/**
 * <pre>
 *
 * 描述：bpm_message_template实体类定义
 * 表:bpm_message_template
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-05-14 22:37:24
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_message_template")
public class BpmMessageTemplate  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmMessageTemplate() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //BO_ALIAS_
    @TableField(value = "BO_ALIAS_",jdbcType=JdbcType.VARCHAR)
    private String boAlias;
    //BPM_DEF_ID_
    @TableField(value = "BPM_DEF_ID_",jdbcType=JdbcType.VARCHAR)
    private String bpmDefId;
    //MSG_TYPE_
    @TableField(value = "MSG_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String msgType;
    //NODE_ID_
    @TableField(value = "NODE_ID_",jdbcType=JdbcType.VARCHAR)
    private String nodeId;
    //TEMPLATE_
    @TableField(value = "TEMPLATE_",jdbcType=JdbcType.VARCHAR)
    private String template;
    //TEMPLATE_TYPE_
    @TableField(value = "TEMPLATE_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String templateType;

    /**
     * 是否默认模板。
     */
    @TableField(exist = false)
    private String isDefault= MBoolean.NO.val;







    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



