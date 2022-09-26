
/**
 * <pre>
 *
 * 描述：form_bo_list_history实体类定义
 * 表:form_bo_list_history
 * 作者：huangzihao
 * 邮箱: csx@redxun.cn
 * 日期:2020-09-03 17:01:21
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

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

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_list_history")
public class ListHistory  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public ListHistory() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //列表ID
    @TableField(value = "LIST_ID_", jdbcType = JdbcType.VARCHAR)
    private String listId;
    //模板内容
    @TableField(value = "CONTENT_", jdbcType = JdbcType.VARCHAR)
    private String content;
    //移动端模板内容
    @TableField(value = "MOBILE_CONTENT_", jdbcType = JdbcType.VARCHAR)
    private String mobileContent;
    //版本
    @TableField(value = "VERSION_", jdbcType = JdbcType.VARCHAR)
    private Integer version;
    //备注
    @TableField(value = "REMARK_",jdbcType=JdbcType.VARCHAR)
    private String remark;

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



