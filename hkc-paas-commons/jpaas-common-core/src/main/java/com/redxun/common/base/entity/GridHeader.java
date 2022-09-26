package com.redxun.common.base.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.tool.StringUtils;

import java.io.Serializable;
import java.sql.Types;

public class GridHeader implements Serializable {

    private String fieldLabel;

    private String fieldName;

    private String fieldType;

    private int dbFieldType;

    private String curDataType;

    private String format;

    private int length;

    private int precision;

    private String isNull;

    private String renderType;

    private String renderTypeName;

    private String renderConf;

    private String mobileShowMode="";

    private String renderMobileConf;

    private JSONObject renderConfObj=new JSONObject();

    public GridHeader(){

    }

    public GridHeader(String fieldName,String fieldType){
        this.fieldName=fieldName;
        this.fieldType=fieldType;
    }

    public String getFieldName(){
        return fieldName;
    }

    public void setFieldName(String fieldName){
        this.fieldName=fieldName;
    }

    public String getFieldType(){
        return fieldType;
    }

    public void setFieldType(String fieldType){
        this.fieldType=fieldType;
    }

    public String getFormat(){
        return format;
    }

    public void setFormat(String format){
        this.format=format;
    }

    public String getRenderMobileConf(){
        return renderMobileConf;
    }

    public void setRenderMobileConf(String renderMobileConf) {
        this.renderMobileConf = renderMobileConf;
    }

    public String getFieldLabel() {
        if(StringUtils.isEmpty(fieldLabel)){
            return fieldName;
        }
        return fieldLabel;
    }

    public int getDbFieldType() {
        return dbFieldType;
    }

    public void setDbFieldType(int dbFieldType) {
        this.dbFieldType = dbFieldType;
    }

    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public void setCurDataType(String curDataType){
        this.curDataType=curDataType;
    }

    public String getCurDataType(){
        return this.curDataType;
    }

    public String getDataType(){
        if(Types.VARCHAR==dbFieldType || Types.CLOB==dbFieldType){
            return "string";
        }else if(Types.BIGINT==dbFieldType || Types.INTEGER==dbFieldType || Types.DOUBLE==dbFieldType || Types.FLOAT==dbFieldType || Types.DECIMAL==dbFieldType){
            return "number";
        }else if(Types.DATE==dbFieldType || Types.TIMESTAMP==dbFieldType){
            return "date";
        }else{
            return "string";
        }
    }

    public String getQueryDataType(){
        if(Types.VARCHAR==dbFieldType || Types.CLOB==dbFieldType){
            return QueryParam.FIELD_TYPE_STRING;
        }else if(Types.BIGINT==dbFieldType){
            return QueryParam.FIELD_TYPE_LONG;
        }else if(Types.DATE==dbFieldType || Types.TIMESTAMP==dbFieldType){
            return QueryParam.FIELD_TYPE_DATE;
        }else if(Types.DECIMAL==dbFieldType){
            return QueryParam.FIELD_TYPE_BIGDECIMAL;
        }else if(Types.DOUBLE==dbFieldType){
            return QueryParam.FIELD_TYPE_DOUBLE;
        }else if(Types.FLOAT==dbFieldType){
            return QueryParam.FIELD_TYPE_FLOAT;
        }else{
            return QueryParam.FIELD_TYPE_STRING;
        }
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public String getRenderConf() {
        return renderConf;
    }

    public void setRenderConf(String renderConf) {
        this.renderConf = renderConf;
    }

    public String getRenderTypeName() {
        return renderTypeName;
    }

    public void setRenderTypeName(String renderTypeName) {
        this.renderTypeName = renderTypeName;
    }

    public JSONObject getRenderConfObj() {
        return renderConfObj;
    }

    public void setRenderConfObj(JSONObject renderConfObj) {
        this.renderConfObj = renderConfObj;
    }

    public String getMobileShowMode() {
        return mobileShowMode;
    }

    public void setMobileShowMode(String mobileShowMode) {
        this.mobileShowMode = mobileShowMode;
    }
}
