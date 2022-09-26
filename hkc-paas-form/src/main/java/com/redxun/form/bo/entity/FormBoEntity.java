
/**
 * <pre>
 *
 * 描述：业务实体实体类定义
 * 表:form_bo_entity
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.bo.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.tool.Tree;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_entity")
public class FormBoEntity  extends BaseExtEntity<java.lang.String>  implements Tree {

    public static final  String FIELD_PK="ID_";
    public static final  String FIELD_FK="REF_ID_";
    public static final  String SQL_FK_STATEMENT="#{fk}";
    public static final  String COMPLEX_NAME="_NAME";

    public static  final String FIELD_TENANT="TENANT_ID_";
    public static  final String FIELD_INST="INST_ID_";
    public static  final String FIELD_INST_STATUS_="INST_STATUS_";
    public static  final String FIELD_CREATE_BY="CREATE_BY_";
    public static  final String FIELD_CREATE_TIME="CREATE_TIME_";
    public static  final String FIELD_UPDATE_BY="UPDATE_BY_";
    public static  final String FIELD_UPDATE_TIME="UPDATE_TIME_";
    public static  final String FIELD_CREATE_DEP="CREATE_DEP_ID_";
    public static  final String FIELD_PARENTID="PARENT_ID_";
    public static  final String FIELD_UPDATE_VERSION="UPDATE_VERSION_";
    public static  final String FIELD_COMPANY="COMPANY_ID_";
    public static  final String FIELD_CREATE_BY_NAME="CREATE_BY_NAME";
    public static  final String FIELD_UPDATE_BY_NAME="UPDATE_BY_NAME";
    public static  final String FIELD_LOGIC_DEL="DELETED_";

    /**
     * 从数据库创建。
     */
    public static  final String GENMODE_DB="db";

    /**
     * 直接创建业务模型。
     */
    public static  final String GENMODE_CREATE="create";

    /**
     * 从表单设计。
     */
    public static  final String GENMODE_FORM="form";

    /**
     * 从拖动表单设计
     */
    public static final String GENMODE_EASYFORM="easyform";

    @JsonCreator
    public FormBoEntity() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //ALIAS_
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    @TableField(value = "GEN_MODE_", jdbcType = JdbcType.VARCHAR)
    private String genMode;
    //外部
    @TableField(value = "IS_MAIN_", jdbcType = JdbcType.INTEGER)
    private Integer isMain;

    @TableField(value = "TREE_ID_", jdbcType = JdbcType.VARCHAR)
    private String treeId;

    //主键字段
    @TableField(value = "ID_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String idField;
    //父ID
    @TableField(value = "PARENT_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String parentField;
    //版本字段
    @TableField(value = "VERSION_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String versionField;

    //生成数据库
    @TableField(value = "GENDB_", jdbcType = JdbcType.INTEGER)
    private Integer gendb;
    //数据源别名
    @TableField(value = "DS_ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String dsAlias;
    //数据源名称
    @TableField(value = "DS_NAME_", jdbcType = JdbcType.VARCHAR)
    private String dsName;
    //TABLE_NAME_
    @TableField(value = "TABLE_NAME_", jdbcType = JdbcType.VARCHAR)
    private String tableName;
    //暂存的实体属性
    @TableField(value = "BO_ATTR_TEMP_", jdbcType = JdbcType.VARCHAR)
    private String boAttrTemp;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @FieldDef(comment = "是否为租户使用")
    @TableField(value = "IS_TENANT_", jdbcType = JdbcType.VARCHAR)
    private String isTenant;
    /**
     * 子表是否为树形。
     */
    @TableField(exist=false)
    private int tree=0;


    /**
     * 关联关系。
     */
    @TableField(exist=false,jdbcType = JdbcType.VARCHAR)
    private String relationType="";

    /**
     * 实体操作类型。
     */
    @TableField(exist=false)
    private String type="";

    @TableField(exist=false)
    private  FormBoRelation  boRelation;

    @TableField(exist=false)
    private  List<Tree>  childrens;



    @TableField(exist=false)
    private Integer gendef;

    @TableField(exist=false)
    private Map<String,String> vars;

    //主键字段名
    @TableField(exist=false)
    private String idFieldName;
    //主键字段注解
    @TableField(exist=false)
    private String idFieldComment;
    //主键字段ID
    @TableField(exist=false)
    private String idFieldId;
    /**
     * 子表按钮定义只是临时存储。
     */
    @TableField(exist=false)
    private String buttons="";

    public String getTableName(){
        if(GENMODE_FORM.equals(genMode) || GENMODE_CREATE.equals(genMode) || GENMODE_EASYFORM.equals(genMode)){
            return TableUtil.getTablePre() + this.alias;
        }
        return  tableName;
    }

    /**
     * 实体属性
     */
    @TableField(exist=false)
    private List<FormBoAttr> boAttrList=new ArrayList<>();

    @TableField(exist=false)
    private List<FormBoEntity> boEntityList=new ArrayList<>();

    public void  addAttr(FormBoAttr attr){
        this.boAttrList.add(attr);
    }

    public void addBoEnt(FormBoEntity boEntity){
        this.boEntityList.add(boEntity);
    }

    /**
     * 原实体。
     */
    @TableField(exist=false)
    private FormBoEntity originEnt=null;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    @Override
    public String getParentId() {
        if(boRelation==null){
            return "0";
        }
        return boRelation.getParentEntId();
    }

    @Override
    public String getText() {
        return this.name;
    }

    @Override
    public List<Tree> getChildren() {
        return this.childrens;
    }

    @Override
    public boolean hasChildren() {
        return this.childrens.size()>0;
    }

    @Override
    public void setChildren(List<Tree> list) {
        this.childrens=list;
    }

    public boolean external(){
        return GENMODE_DB.equals(genMode);
    }

    public boolean tree(){
        return this.tree==1;
    }

    /**
     * 原实体。
     */
    @TableField(exist=false)
    private JSONArray sunBoRelations;

    /**
     * 实体引用属性
     */
    @TableField(exist=false)
    private List<FormBoAttr> boAttrRefList=new ArrayList<>();
}



