
/**
 * <pre>
 *
 * 描述：业务模型实体类定义
 * 表:form_bo_def
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
@TableName(value = "form_bo_def")
public class    FormBoDef  extends BaseExtEntity<java.lang.String> {

    public final static String GEN_TYPE_FORM="form";
    public final static String GEN_TYPE_EASY_FORM="easyform";
    public final static String GEN_TYPE_DIRECT="direct";


    @JsonCreator
    public FormBoDef() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //别名
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //分类ID
    @TableField(value = "TREE_ID_", jdbcType = JdbcType.VARCHAR)
    private String treeId;
    //支持数据库
    @TableField(value = "SUPPORT_DB_", jdbcType = JdbcType.INTEGER)
    private Integer supportDb;
    //DESCRIPTION_
    @TableField(value = "DESCRIPTION_", jdbcType = JdbcType.VARCHAR)
    private String description;

    @TableField(value = "GEN_TYPE_", jdbcType = JdbcType.VARCHAR)
    private String genType=GEN_TYPE_FORM;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private FormBoEntity formBoEntity;
    @TableField(exist = false)
    private List<FormBoRelation> relations;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



