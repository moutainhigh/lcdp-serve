
/**
 * <pre>
 *
 * 描述：表单方案实体类定义
 * 表:form_solution
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_solution")
public class FormSolution  extends BaseExtEntity<java.lang.String> {

    public static String INST_STATUS_DRAFT="DRAFTED";
    public static String INST_STATUS_DELETE="DELETE";

    public static String INST_ID="INST_ID_";
    public static String INST_STATUS="INST_STATUS_";

    @JsonCreator
    public FormSolution() {
    }

    //方案ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //分类ID
    @TableField(value = "CATEGORY_ID_", jdbcType = JdbcType.VARCHAR)
    private String categoryId;
    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //别名
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //业务模型ID
    @TableField(value = "BODEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String bodefId;
    //表单
    @TableField(value = "FORM_ID_", jdbcType = JdbcType.VARCHAR)
    private String formId;

    @TableField(value = "FORM_NAME_", jdbcType = JdbcType.VARCHAR)
    private String formName;
    //手机表单ID
    @TableField(value = "MOBILE_FORM_ID_", jdbcType = JdbcType.VARCHAR)
    private String mobileFormId;

    @TableField(value = "MOBILE_FORM_NAME_", jdbcType = JdbcType.VARCHAR)
    private String mobileFormName;

    //表单数据处理器
    @TableField(value = "DATA_HANDLER_", jdbcType = JdbcType.VARCHAR)
    private String dataHandler;
    //TREE_
    @TableField(value = "TREE_", jdbcType = JdbcType.INTEGER)
    private Integer tree=0;
    //表间公式
    @TableField(value = "FORMULAS_", jdbcType = JdbcType.VARCHAR)
    private String formulas;
    //表间公式名称
    @TableField(value = "FORMULAS_NAME_", jdbcType = JdbcType.VARCHAR)
    private String formulasName;
    //BUTTONS_SETTING_
    @TableField(value = "BUTTONS_SETTING_", jdbcType = JdbcType.VARCHAR)
    private String buttonsSetting;
    //NO_PK_SETTING_
    @TableField(value = "NO_PK_SETTING_", jdbcType = JdbcType.VARCHAR)
    private String noPkSetting;
    //流程定义配置
    @TableField(value = "FLOW_DEF_MAPPING_", jdbcType = JdbcType.VARCHAR)
    private String flowDefMapping;
    //JAVA脚本
    @TableField(value = "JAVA_CODE_", jdbcType = JdbcType.VARCHAR)
    private String javaCode;
    //树形加载方式0,一次性加载,1,懒加载
    @TableField(value = "LOAD_MODE_", jdbcType = JdbcType.INTEGER)
    private Integer loadMode=0;
    //树显示字段
    @TableField(value = "DISPLAY_FIELDS_", jdbcType = JdbcType.VARCHAR)
    private String displayFields="";

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    /**
     * 是否生成物理表
     */
    @TableField(value = "IS_GENERATE_TABLE_")
    private int isGenerateTable;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



