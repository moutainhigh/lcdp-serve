package com.redxun.common.base.search;

import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 字段查询参数
 *
 */
public class QueryParam  implements  WhereParam{

    protected static Logger logger=LoggerFactory.getLogger(QueryParam.class);
    //=========字段比较运算===============
    /**
     * 等于
     */
    public static final String OP_EQUAL="EQ";
    /**
     * 不等于
     */
    public static final String OP_NOT_EQUAL="NEQ";
    /**
     * 大于
     */
    public static final String OP_GREAT="GT";

    /**
     * 大于等于
     */
    public static final String OP_GREAT_EQUAL="GE";

    /**
     * 小于
     */
    public static final String OP_LESS="LT";
    /**
     * 小于等于
     */
    public static final String OP_LESS_EQUAL="LE";
    /**
     * 全模糊
     */
    public static final String OP_LIKE="LK";
    /**
     * 左模糊
     */
    public static final String OP_LEFT_LIKE="LEK";
    /**
     * 右模糊
     */
    public static final String OP_RIGHT_LIKE="RIK";



    //IS NULL
    public final static String OP_IS_NULL = "ISNULL";

    //IS NOT NULL
    public final static String OP_NOTNULL = "NOTNULL";
    //IN
    public final static String OP_IN="IN";


    //===========字段的类型=================
    /**
     * String Type = S
     */
    public static final String FIELD_TYPE_STRING="S";
    /**
     * Long Type=L
     */
    public static final String FIELD_TYPE_LONG="L";
    /**
     * Integer Type=I
     */
    public static final String FIELD_TYPE_INTEGER="I";

    /**
     * Short Type=I
     */
    public static final String FIELD_TYPE_SHORT="SN";

    /**
     * Float Type=F
     */
    public static final String FIELD_TYPE_FLOAT="F";
    /**
     * Double Type=DL
     */
    public static final String FIELD_TYPE_DOUBLE="DB";

    /**
     * Date Type = D
     */
    public static final String FIELD_TYPE_DATE="D";

    /**
     * BigDecimal=BD
     */
    public static final String FIELD_TYPE_BIGDECIMAL="BD";

    private static Map<String,String> operateMap=new HashMap<>();

    static {
        operateMap.put(OP_EQUAL," = ");
        operateMap.put(OP_NOT_EQUAL," != ");
        operateMap.put(OP_GREAT," > ");
        operateMap.put(OP_GREAT_EQUAL," >= ");
        operateMap.put(OP_LESS," < " );
        operateMap.put(OP_LESS_EQUAL," <= ");
        operateMap.put(OP_LIKE," like ");
        operateMap.put(OP_LEFT_LIKE," like ");
        operateMap.put(OP_RIGHT_LIKE," like ");
        operateMap.put(OP_IS_NULL,"  is null  ");
        operateMap.put(OP_NOTNULL,"  is not null  ");
        operateMap.put(OP_IN,"  in  ");
    }




    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 比较操作类型 如小于或大于或等于
     */
    private String opType=QueryParam.OP_EQUAL;
    /**
     * 值
     */
    private Object value;
    /**
     * 字段数据类型
     */
    private String fieldType=QueryParam.FIELD_TYPE_STRING;

    public QueryParam() {
    }

    public QueryParam(String fieldName, String opType, Object value) {
        this.fieldName = fieldName;
        this.opType = opType;
        this.value = value;
    }

    public QueryParam(String fieldName, String opType, Object value,String paramName) {
        this.fieldName = fieldName;
        this.opType = opType;
        this.value = value;
        this.paramName = paramName;
    }

    public String getFieldName() {
        String name=fieldName.replace(".", "");
        if (opType == null) {
            opType = OP_EQUAL;
        } else {
            opType = opType.toUpperCase();
        }

        if(OP_LESS_EQUAL.equals(opType) || OP_LESS.equals(opType)){
            return name +"_END";
        }
        else if(OP_GREAT.equals(opType) || OP_GREAT_EQUAL.equals(opType) ){
            return name +"_START";
        }
        return name;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        if(StringUtils.isEmpty(paramName)){
            return "";
        }
        String name=paramName.replace(".", "");
        if (opType == null) {
            opType = OP_EQUAL;
        } else {
            opType = opType.toUpperCase();
        }

        if(OP_LESS_EQUAL.equals(opType) || OP_LESS.equals(opType)){
            return name +"_END";
        }
        else if(OP_GREAT.equals(opType) || OP_GREAT_EQUAL.equals(opType) ){
            return name +"_START";
        }
        return name;
    }



    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public String getSql(){
        String type=operateMap.get(this.opType);
        if(QueryParam.OP_IN.equals(this.opType)){
            return   this.fieldName + type +"("+ this.value.toString()+")";
        }
        return   this.fieldName + type +this.getFieldParam();
    }



    public static Object getObjValue(String opType,String fieldType, String paramValue) {
        Object value = null;
        try{
            // 字符串 精准匹配
            if (QueryParam.OP_IN .equals(opType)){
                value = getInVal(fieldType, paramValue);
            }
            else if (QueryParam.FIELD_TYPE_STRING.equals(fieldType)) {
                value = getStringVal(opType, paramValue);
            } else if (FIELD_TYPE_LONG.equals(fieldType)) {
                // 长整型
                value = new Long(paramValue);
            } else if (FIELD_TYPE_INTEGER.equals(fieldType)) {
                // 整型
                value = new Integer(paramValue);
            } else if (FIELD_TYPE_DOUBLE.equals(fieldType)) {
                //Double类型
                value = new Double(paramValue);
            } else if (FIELD_TYPE_BIGDECIMAL.equals(fieldType)) {
                // decimal
                value = new BigDecimal(paramValue);
            } else if (FIELD_TYPE_FLOAT.equals(fieldType)) {
                // FLOAT
                value = new Float(paramValue);
            } else if (FIELD_TYPE_SHORT.equals(fieldType)) {
                // short
                value = new Short(paramValue);
            } else if (FIELD_TYPE_DATE.equals(fieldType)) {
                value = getDateVal(opType, paramValue);
            }



        }catch(Exception ex){
            logger.error(ex.getMessage());
        }
        return value;
    }

    private static boolean hasTime(Date date){
        if(date.getHours()==0 && date.getMinutes()==0 && date.getSeconds()==0){
            return false;
        }
        return  true;
    }

    private static Object getDateVal(String opType, String paramValue) {
        Date value;
        if (paramValue.indexOf("T") == -1) {
            value = DateUtils.parseDate(paramValue, "");
        } else {
            paramValue = paramValue.replace("T", " ");
            value = DateUtils.parseDate(paramValue, DateUtils.DATE_FORMAT_FULL);
        }
        boolean hasTime=hasTime(value);

        if(QueryParam.OP_LESS_EQUAL.equals(opType)){
            if(!hasTime){
                value=DateUtils.setEndDay((Date)value);
            }
        }else if(QueryParam.OP_GREAT_EQUAL.equals(opType)){
            if(!hasTime){
                value = DateUtils.setStartDay((Date) value);
            }
        }
        return value;
    }

    private static Object getStringVal(String opType, String paramValue) {
        Object value=null;
        if(QueryParam.OP_LIKE.equals(opType)){
            value="%"+paramValue+"%";
        }else if(QueryParam.OP_LEFT_LIKE.equals(opType)){
            value="%" +paramValue;
        }else if(QueryParam.OP_RIGHT_LIKE.equals(opType)){
            value=paramValue+"%";
        }else{
            value = paramValue;
        }
        return value;
    }

    private static Object getInVal(String fieldType, String paramValue) {
        Object value=null;
        String[] tmpAry=paramValue.split(",");
        if("I".equals(fieldType) || "SN".equals(fieldType)){
            value=paramValue;
        }
        else{
            String tmp="";
            for(int i=0;i<tmpAry.length;i++){
                String str=tmpAry[i];
                if(i==0){
                    tmp+="'" + str +"'";
                }
                else{
                    tmp+=",'" + str +"'";
                }
            }
            value=tmp;
        }
        return value;
    }

    public String getFieldParam(){
        String name=fieldName.replace(".", "");
        if(StringUtils.isNotEmpty(paramName)){
            name=paramName.replace(".", "");
        }
        if (opType == null) {
            opType = OP_EQUAL;
        } else {
            opType = opType.toUpperCase();
        }
        if(OP_IS_NULL.equals(opType) || OP_NOTNULL.equals(opType)){
            return "";
        }

        if(OP_LESS_EQUAL.equals(opType) || OP_LESS.equals(opType)){
            name += "_END";
        }
        else if(OP_GREAT.equals(opType) || OP_GREAT_EQUAL.equals(opType) ){
            name += "_START";
        }

        return "#{w." + name +"}";
    }
}
