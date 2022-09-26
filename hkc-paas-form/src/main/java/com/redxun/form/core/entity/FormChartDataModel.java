
/**
 * <pre>
 *
 * 描述：图表数据模型实体类定义
 * 表:form_chart_data_model
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2021-04-30 11:13:36
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

import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_chart_data_model")
public class FormChartDataModel extends BaseExtEntity<String> {

    @JsonCreator
    public FormChartDataModel() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //分类ID
    @FieldDef(comment = "分类ID")
    @TableField(value = "CATEGORY_ID_", jdbcType = JdbcType.VARCHAR)
    private String categoryId;
    //数据源
    @TableField(value = "DATA_SOURCE_")
    private String dataSource;
    //模型配置
    @TableField(value = "MODEL_CONFIG_")
    private String modelConfig;
    //模型名称
    @TableField(value = "NAME_")
    private String name;
    //表配置
    @TableField(value = "TABLES_")
    private String tables;
    //模式
    @TableField(value = "TYPE_")
    private String type;
    //自定义SQL
    @TableField(value = "SQL_MODE_")
    private String sqlMode;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

    public static Map<String,String> operateMap=new HashMap<>();

    static {
        operateMap.put("EQ"," = ");
        operateMap.put("NEQ"," != ");
        operateMap.put("GREAT"," > ");
        operateMap.put("GREAT_EQUAL"," >= ");
        operateMap.put("LESS"," < " );
        operateMap.put("LESSEQUAL"," <= ");
        operateMap.put("LK"," like ");
        operateMap.put("LEK"," like ");
        operateMap.put("RIK"," like ");
        operateMap.put("ISNULL","  is null  ");
        operateMap.put("NOTNULL","  is not null  ");
        operateMap.put("IN","  in  ");
    }
}



