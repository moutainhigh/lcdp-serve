
/**
 * <pre>
 *
 * 描述：业务实体实体类定义
 * 表:form_bo_relation
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.bo.entity;

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
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_relation")
public class FormBoRelation  extends BaseExtEntity<java.lang.String> {

    public final static String RELATION_MAIN="main";
    public final static String RELATION_ONETOONE="onetoone";
    public final static String RELATION_ONETOMANY="onetomany";
    public final static String CHILDREN="children";



    @JsonCreator
    public FormBoRelation() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //业务模型定义
    @TableField(value = "BODEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String bodefId;
    //实体ID
    @TableField(value = "ENT_ID_", jdbcType = JdbcType.VARCHAR)
    private String entId;
    //父实体ID
    @TableField(value = "PARENT_ENT_ID_", jdbcType = JdbcType.VARCHAR)
    private String parentEntId;
    //关系类型(onetoone,onetomany)
    @TableField(value = "TYPE_", jdbcType = JdbcType.VARCHAR)
    private String type;
    //是否引用
    @TableField(value = "IS_REF_", jdbcType = JdbcType.INTEGER)
    private Integer isRef=0;
    //关联字段
    @TableField(value = "FK_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String fkField;
    //关联主实体字段
    @TableField(value = "PK_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String pkField;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;


    @TableField(exist = false)
    private List<String> sunBoRelations;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



