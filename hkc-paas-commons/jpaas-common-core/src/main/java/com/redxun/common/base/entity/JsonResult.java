package com.redxun.common.base.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.utils.ExceptionUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 返回前台响应的数据对象。
 *
 */
@Accessors(chain = true)
@Data
@Slf4j
public class JsonResult<T extends Object> implements  Serializable {
    /**
     * 返回的成功状态=200
     */
    public final static int SUCESS_CODE=200;
    /**
     * 返回的失败状态=500
     */
    public final static int FAIL_CODE=500;

    /**
     * 是否成功
     */
    private Boolean success=true;

    /**
     * 返回的消息。
     */
    private String message="";
    /**
     * 详细的消息内容，一般用于显示错误的原因
     */
    private String detailMsg="";

    /**
     * 在浏览器端的默认信息中是否显示。
     */
    private boolean show=true;

  /**
     * 成功:200
     * 失败:
   *   503: 服务不正常
     * 500 : 异常
     * 403 : 没有权限
     * 401 : 未登录
     */
    private  Integer code=SUCESS_CODE;

    /**
     * 需要返回的数据。
     */
    private T data;

    public JsonResult() {

    }

    public JsonResult(int code){
        this.code=code;
    }

    public JsonResult(boolean success){
        this.success=success;
        if(success){
            this.code=SUCESS_CODE;
        }else{
            this.code=FAIL_CODE;
        }
    }

    /**
     * 返回的成功的响应实例
     * @param message
     * @return
     */
    public static JsonResult Success(String message){
        JsonResult result=new JsonResult(true,message);
        return result;
    }

    /**
     * 返回的成功的响应
     * @return
     */
    public static JsonResult Success(){
        JsonResult result=new JsonResult(true);
        return result;
    }

    /**
     * 根据错误消息返回响应值
     * @param message
     * @return
     */
    public static JsonResult Fail(String message){
        JsonResult result=new JsonResult(false,message);
        return result;
    }

    /**
     * 返回错误的响应
     * @return
     */
    public static JsonResult Fail(){
        JsonResult result=new JsonResult(false);
        return result;
    }

    /**
     *
     * @return
     */
    public  boolean isSuccess(){
        return success;
    }

    /**
     * 根据错误码返回实例
     * @param code
     * @return
     */
    public static JsonResult Fail(Integer code){
        JsonResult result=new JsonResult(code);
        return result;
    }

    /**
     * 根据错误码跟消息内容返回错误提示
     * @param code
     * @param message
     * @return
     */
    public static JsonResult Fail(Integer code,String message){
       return error(code,message);
    }

    /**
     * 根据错误硫跟消息内容返回错误提示
     * @param code
     * @param message
     * @param detailMsg
     * @return
     */
    public static JsonResult Fail(Integer code,String message,String detailMsg){
        return error(code,message,detailMsg);
    }

    public static <T> JsonResult<T> error(JsonResult<?> result) {
        return error(result.getCode(), result.getMessage());
    }

    /**
     *  返回错误的响应
     * @param code 响应Code
     * @param message 错误的信息
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> error(Integer code, String message) {
        JsonResult<T>  jsonResult = new JsonResult<>();
        jsonResult.success = false;
        jsonResult.code = code;
        jsonResult.message = message;
        return jsonResult;
    }

    /**
     *  返回错误的响应
     * @param code 响应Code
     * @param message 错误的信息
     * @param detailMsg 错误的详细信息
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> error(Integer code, String message,String detailMsg) {
        JsonResult<T>  jsonResult = new JsonResult<>();
        jsonResult.success = false;
        jsonResult.code = code;
        jsonResult.message = message;
        jsonResult.detailMsg=detailMsg;
        return jsonResult;
    }

    /**
     * 构建函数
     * @param success 是否成功
     * @param message 消息内容
     */
    public JsonResult(boolean success, String message ){
        this.success=success;
        this.message=message;
        if(success){
            this.code=SUCESS_CODE;
        }else{
            this.code=FAIL_CODE;
        }
    }

    /**
     * 构造函数
     * @param success 是否成功
     * @param data 响应数据
     * @param message 消息提示内容
     */
    public JsonResult(boolean success, T data , String message){
        this.success=success;
        if(success){
            this.code=SUCESS_CODE;
        }else{
            this.code=FAIL_CODE;
        }
        this.data=data;
        this.message=message;
    }

    /**
     * 构造函数
     * @param code 响应Code
     * @param data 响应数据
     * @param message 消息提示内容
     */
    public JsonResult(int code, T data , String message){
        if(code == SUCESS_CODE){
            this.success=Boolean.TRUE;
        }else{
            this.success=Boolean.FALSE;
        }
        this.data=data;
        this.message=message;
    }

    public void setException(Throwable exception){
        String msg= ExceptionUtil.getExceptionMessage(exception);
        this.data= (T) msg;
        log.error(msg);
    }

    /**
     * 返回成功的响应值
     * @param msg
     * @return
     */
    public static JsonResult getSuccessResult(String msg) {
        return getSuccessResult(null, SUCESS_CODE, msg);
    }

    /**
     * 返回成功的消息值
     * @param data 响应数据
     * @param msg 返回的消息内容
     * @param <T>
     * @return
     */
    public static <T> JsonResult getSuccessResult(T data, String msg) {
        return getSuccessResult(data, SUCESS_CODE, msg);
    }

    public static <T> JsonResult getSuccessResult(T data) {
        return getSuccessResult(data, SUCESS_CODE, "");
    }

    public static <T> JsonResult getSuccessResult(T data ,Integer code, String msg){
        JsonResult<T> result=new JsonResult<T>();
        result.setSuccess(true);
        result.setData(data);
        result.setCode(SUCESS_CODE);
        result.setMessage(msg);
        return result;
    }

    /**
     * 获取错误结果对象。
     * @param msg
     * @return
     */
    public static JsonResult getFailResult(String msg){
        return getFailResult(null, FAIL_CODE, msg);
    }

    public static JsonResult getFailResult(String msg,int code){
        return getFailResult(null, code, msg);
    }

    public static <T> JsonResult getFailResult(T data, String msg) {
        return getFailResult(data, FAIL_CODE, msg);
    }

    public static  <T> JsonResult getFailResult(T data ,Integer code, String msg){
        JsonResult<T> result=new JsonResult<T>();
        result.setSuccess(false);
        result.setData(data);
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
