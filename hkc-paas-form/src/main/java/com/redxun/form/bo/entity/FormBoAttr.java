
/**
 * <pre>
 *
 * 描述：业务实体属性实体类定义
 * 表:form_bo_attr
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.bo.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_attr")
public class FormBoAttr  extends BaseExtEntity<java.lang.String> {

    /**
     * 关联字段常量。
     */
    private final static String REF_FIELD="ref";

    @JsonCreator
    public FormBoAttr() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;

    //实体ID
    @TableField(value = "ENT_ID_", jdbcType = JdbcType.VARCHAR)
    private String entId;
    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //字段名
    @TableField(value = "FIELD_NAME_", jdbcType = JdbcType.VARCHAR)
    private String fieldName;
    //备注
    @TableField(value = "COMMENT_", jdbcType = JdbcType.VARCHAR)
    private String comment;
    //数据类型
    @TableField(value = "DATA_TYPE_", jdbcType = JdbcType.VARCHAR)
    private String dataType;
    //长度
    @TableField(value = "LENGTH_", jdbcType = JdbcType.INTEGER)
    private Integer length;
    //小数位
    @TableField(value = "DECIMAL_LENGTH_", jdbcType = JdbcType.INTEGER)
    private Integer decimalLength;
    //控件类型
    @TableField(value = "CONTROL_", jdbcType = JdbcType.VARCHAR)
    private String control;
    /**
     * 扩展JSON配置。
     */
    @TableField(value = "EXT_JSON_", jdbcType = JdbcType.VARCHAR)
    private String extJson="";

    //是否单字段
    @TableField(value = "IS_SINGLE_", jdbcType = JdbcType.INTEGER)
    private Integer isSingle=1;

    //序号
    @TableField(value = "SN_", jdbcType = JdbcType.INTEGER)
    private Integer sn;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    //是否主键
    @TableField(value = "IS_PK_", jdbcType = JdbcType.INTEGER)
    private Integer isPk;

    //是否非空
    @TableField(value = "IS_NOT_NULL_", jdbcType = JdbcType.INTEGER)
    private Integer isNotNull;

    //跨列数
    @TableField(value = "SPANS_", jdbcType = JdbcType.INTEGER)
    private Integer spans;

    //数据库字段类型
    @TableField(value = "DB_FIELD_TYPE_", jdbcType = JdbcType.VARCHAR)
    private String dbFieldType;


    //扩展JSON
    @TableField(exist = false)
    private JSONObject formJson;
    /**
     * 这个存放表单数据相关的属性。
     */
    @TableField(exist = false)
    private JSONObject dataJson;


    /**
     * 这个存放Excel表单数据导入时的格式化属性。
     */
    @TableField(exist = false)
    private String format;



    @TableField(exist = false)
    private FormBoAttr orignAttr;

    //选择显示列--代码生成配置
    @TableField(exist = false)
    private String headShow;

    //列表字段长度--代码生成配置
    @TableField(exist = false)
    private int fieldLength;



    /**
     * 字段类型。
     * 用于在bo保存时使用。
     */
    @TableField(exist = false)
    private String type= TableUtil.OP_UPD;

    @TableField(exist = false)
    private JSONObject extJsonObj= null;

    public void setExtJson(String extJson){
        if(StringUtils.isEmpty(extJson)) {
            return;
        }
        this.extJsonObj=JSONObject.parseObject(this.extJson);
        this.extJson=extJson;
    }

    public String getExtJson(){
        return this.extJson;
    }

    /**
     * 获取关联字段。
     * @return
     */
    public String getRelField(){
        extJsonObj=JSONObject.parseObject(extJson);
        if(extJsonObj==null){
            return  "";
        }
        if(extJsonObj.containsKey(REF_FIELD)){
            return extJsonObj.getString(REF_FIELD);
        }
        return "";
    }

    public String getByKey(String key){
        if(extJsonObj==null){
            return "";
        }
        if(extJsonObj.containsKey(key)){
            return extJsonObj.getString(key);
        }
        return  "";
    }

    public boolean single(){
        return this.isSingle==1;
    }

    public void setSpans(Integer spans){
        if(BeanUtil.isEmpty(spans)) {
            this.spans=1;
        }else{
            this.spans=spans;
        }
    }

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



