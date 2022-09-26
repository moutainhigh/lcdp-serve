
/**
 * <pre>
 *
 * 描述：表单设计实体类定义
 * 表:form_pc
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
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.log.annotation.FieldDef;
import com.redxun.form.bo.entity.FormBoEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_pc")
public class FormPc  extends BaseExtEntity<java.lang.String> {

    /**
     * 表单类型-在线设计表单=ONLINE-DESIGN
     */
    public final static String FORM_TYPE_ONLINE_DESIGN="ONLINE-DESIGN";
    /**
     * 表单类型-拖动设计表单=EASY-DESIGN
     */
    public final static String FORM_TYPE_EASY_DESIGN="EASY-DESIGN";
    /**
     * 表单类型-自定义表单=SEL-DEV
     */
    public final static String FORM_TYPE_SEL_DEV="SEL-DEV";
    /**
     * 表单类型-自定义表单=SEL-DEV
     */
    public final static String CONTAI_TYPE_="CONTAI_TYPE";

    /**
     * 通过BO生成。
     */
    public final static String FORM_TYPE_GENBYBO="GENBYBO";


    public final static String PAGE_TAG="#page#";

    @JsonCreator
    public FormPc() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //分类ID
    @TableField(value = "CATEGORY_ID_", jdbcType = JdbcType.VARCHAR)
    private String categoryId;
    //别名
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //类型
    @TableField(value = "TYPE_", jdbcType = JdbcType.VARCHAR)
    private String type=FORM_TYPE_ONLINE_DESIGN;
    //模版
    @TableField(value = "TEMPLATE_", jdbcType = JdbcType.VARCHAR)
    private String template;
    //表单脚本
    @TableField(value = "JAVASCRIPT_", jdbcType = JdbcType.VARCHAR)
    private String javascript;

    //表单脚本
    @TableField(value = "JAVASCRIPT_KEY_", jdbcType = JdbcType.VARCHAR)
    private String javascriptKey;

    @TableField(value = "METADATA_", jdbcType = JdbcType.VARCHAR)
    private String metadata;

    //发布状态
    @TableField(value = "DEPLOYED_", jdbcType = JdbcType.INTEGER)
    private Integer deployed;
    //业务模型ID
    @TableField(value = "BODEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String bodefId;

    @TableField(value = "BODEF_ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String boDefAlias;



    //版本号
    @TableField(value = "VERSION_", jdbcType = JdbcType.INTEGER)
    private Integer version;
    //主版本
    @TableField(value = "MAIN_", jdbcType = JdbcType.INTEGER)
    private Integer main;
    @TableField(value = "BUTTON_DEF_", jdbcType = JdbcType.VARCHAR)
    private String buttonDef="";
    @TableField(value = "TAB_DEF_", jdbcType = JdbcType.VARCHAR)
    private String tabDef="";

    @TableField(value = "TABLE_BUTTON_DEF_", jdbcType = JdbcType.VARCHAR)
    private String tableButtonDef="";

    //意见定义
    @TableField(value = "OPINION_DEF_", jdbcType = JdbcType.VARCHAR)
    private String opinionDef="";

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;
    /**
     * 这个字段主要处理表单的初始数据和时需要处理的数据。
     * <pre>
     *  {
     *      main:{
     *          field1: {action:'init',from:'seqNo',val:""},
     *          field2:{init:true},
     *      },
     *      table1:{
     *              field1:{action:'save',from:'seqNo',val:""},
     *              field2:{initUser:true},
     *              field2:{defaultVal:"广州,武汉"},
     *          }
     *      },
     *      table2:{
     *                    field1:{action:'save',from:'seqNo',val:""},
     *                    field2:{initUser:true},
     *          }
     *      }
     *  }
     * </pre>
     */
    @TableField(value = "DATA_SETTING_", jdbcType = JdbcType.VARCHAR)
    private String dataSetting="";

    /**
     * 表单设定，这个字段存储表单的一些设定，比如高宽数据源，是否必填等。
     * {
     *     main:{
     *         feild1:{require:true,dataSource:{from:""}}
     *     },
     *     table1:{
     *         field1:{require:true,dataSource:{from:""}},
     *     },
     *     table2:{
     *             field1:{require:true,dataSource:{from:""}},
     *     }
     *
     * }
     */
    @TableField(value = "FORM_SETTING_", jdbcType = JdbcType.VARCHAR)
    private String formSettings="";


    @TableField(value = "COMPONENT_", jdbcType = JdbcType.VARCHAR)
    private String component="";

    @TableField(value = "WIZARD_", jdbcType = JdbcType.INTEGER)
    private int wizard=0;

    /**
     * 是否复制表单。
     */
    @TableField(value = "COPYED_")
    private int copyed=0;
    /**
     * 是否复制表单。
     */
    @TableField(value = "DATASOURCE_")
    private String datasource="";

    //模版(临时)
    @TableField(value = "TEMPLATE_TEMP_", jdbcType = JdbcType.VARCHAR)
    private String templateTemp;
    //表单脚本(临时)
    @TableField(value = "JAVASCRIPT_TEMP_", jdbcType = JdbcType.VARCHAR)
    private String javascriptTemp;
    //表单数据(临时)
    @TableField(value = "METADATA_TEMP_", jdbcType = JdbcType.VARCHAR)
    private String metadataTemp;


    /*
     *表单初始化权限_只读
     */
    @TableField(exist = false)
    private String permissionR;

    /*
     *表单初始化权限_编辑
     */
    @TableField(exist = false)
    private String permissionW;

    @TableField(exist = false)
    private FormBoDef formBoDef;

    /**
     * 表单主键。
     */
    @TableField(exist = false)
    private String pkField;

    @TableField(exist = false)
    private String mobileJavascript;

    @TableField(exist = false)
    private FormBoEntity formBoEntity;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



