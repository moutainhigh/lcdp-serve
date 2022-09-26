
/**
 * <pre>
 *
 * 描述：bpm_default_template实体类定义
 * 表:bpm_default_template
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-05-12 22:42:09
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_default_template")
public class BpmDefaultTemplate  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmDefaultTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //消息类型
    @TableField(value = "MESSAGE_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String messageType;
    //模板
    @TableField(value = "TEMPLATE_",jdbcType=JdbcType.VARCHAR)
    private String template;
    //模板类型
    @TableField(value = "TEMPLATE_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String templateType;



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



