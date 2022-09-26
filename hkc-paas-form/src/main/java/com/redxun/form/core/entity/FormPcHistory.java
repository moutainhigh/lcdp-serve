
/**
 * <pre>
 *
 * 描述：表单设计历史实体类定义
 * 表:FORM_PC_HISTORY
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-01-25 09:53:12
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
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_PC_HISTORY")
public class FormPcHistory  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormPcHistory() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //表单设计主键
    @TableField(value = "FORM_PC_ID_",jdbcType=JdbcType.VARCHAR)
    private String formPcId;
    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //别名
    @TableField(value = "ALIAS_",jdbcType=JdbcType.VARCHAR)
    private String alias;
    //模版
    @TableField(value = "TEMPLATE_",jdbcType=JdbcType.VARCHAR)
    private String template;
    //表单脚本
    @TableField(value = "JAVASCRIPT_",jdbcType=JdbcType.VARCHAR)
    private String javascript;
    //表单脚本KEY
    @TableField(value = "JAVASCRIPT_KEY_",jdbcType=JdbcType.VARCHAR)
    private String javascriptKey;
    //表单数据
    @TableField(value = "METADATA_",jdbcType=JdbcType.VARCHAR)
    private String metadata;
    //意见定义
    @TableField(value = "OPINION_DEF_",jdbcType=JdbcType.VARCHAR)
    private String opinionDef;
    //按钮定义
    @TableField(value = "BUTTON_DEF_",jdbcType=JdbcType.VARCHAR)
    private String buttonDef;
    //表自定义按钮
    @TableField(value = "TABLE_BUTTON_DEF_",jdbcType=JdbcType.VARCHAR)
    private String tableButtonDef;
    //数据设定
    @TableField(value = "DATA_SETTING_",jdbcType=JdbcType.VARCHAR)
    private String dataSetting;
    //表单设定
    @TableField(value = "FORM_SETTING_",jdbcType=JdbcType.VARCHAR)
    private String formSetting;
    //表单组件
    @TableField(value = "COMPONENT_",jdbcType=JdbcType.VARCHAR)
    private String component;
    //支持向导
    @TableField(value = "WIZARD_",jdbcType=JdbcType.NUMERIC)
    private Integer wizard;
    //TAB定义
    @TableField(value = "TAB_DEF_",jdbcType=JdbcType.VARCHAR)
    private String tabDef;
    //备注
    @TableField(value = "REMARK_",jdbcType=JdbcType.VARCHAR)
    private String remark;

    //创建人名称
    @TableField(exist = false)
    private String createName;


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



