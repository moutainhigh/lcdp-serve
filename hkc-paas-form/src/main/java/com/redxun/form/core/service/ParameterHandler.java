package com.redxun.form.core.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.controller.IParameterHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParameterHandler {

    private volatile boolean isInit=false;

    private static Map<String,String> typeMap=new HashMap<>();

    private  Map<String,IParameterHandler> handlerMap=new HashMap<>();

    static {
        typeMap.put("string","S");
        typeMap.put("int","I");
        typeMap.put("date","D");
        typeMap.put("float","F");
    }

    private  void init() {
        if(isInit){
            return;
        }
        handlerMap.put("textbox",this::handCommon);
        handlerMap.put("datepicker",this::handCommon);
        handlerMap.put("month",this::handMonth);
        handlerMap.put("rangepicker",this::handRange);
        handlerMap.put("select",this::handDoubleValue);
        handlerMap.put("dialog",this::handDoubleValue);

        this.isInit=true;
    }

    /**
     * 在处理数据导出时，需要根据查询参数构造，查询条件的显示值。
     *  比如下拉框 1，男，2，女，我们在 输入1 是，构造的日志应该为 性别:男 而不是 性别:1
     * textbox,datepicker,month,select,dialog,rangepicker
     *
     * @param searchJson
     * @param request
     * @return
     */
    public String handParamters(String  searchJson, HttpServletRequest request){
        this.init();

        if(StringUtils.isEmpty(searchJson)){
            return  "";
        }

        StringBuilder sb=new StringBuilder();
        JSONArray jsonArray=JSONArray.parseArray(searchJson);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject json=jsonArray.getJSONObject(i);
            String label=json.getString("fieldLabel");
            String fc=json.getString("fc");

            IParameterHandler handler=handlerMap.get(fc);
            if(handler==null){
                continue;
            }
            String val=handler.handParameter(json,request);

            if(StringUtils.isNotEmpty(val)){
                sb.append(label +":" +val +",");
            }
        }
        String rtn=sb.toString();
        if(StringUtils.isNotEmpty(rtn)){
            rtn=rtn.substring(0,rtn.length()-1);
        }
        return rtn;
    }

    private String handDoubleValue(JSONObject json, HttpServletRequest request) {

        String param=getParamName(json,false);
        String val=request.getParameter(param);
        if(StringUtils.isEmpty(val)){
            return "";
        }
        String name=getParamName(json,true);
        String nameVal=request.getParameter(name);


        return   nameVal;
    }





    /**
     * Q_CREATE_TIME__D_GE: "2021-01-05 10:47:13", Q_CREATE_TIME__D_LE: "2021-02-10 10:47:13"}
     * @param json
     * @param request
     * @return
     */
    private String handRange(JSONObject json, HttpServletRequest request) {
        String fieldName=json.getString("fieldName");
        String tablePre=json.getString("tablePre");
        fieldName =StringUtils.isEmpty(tablePre)?fieldName :tablePre +"." + fieldName;

        String begin="Q_" +fieldName +"_D_GE";
        String end="Q_" +fieldName +"_D_LE";

        String beginVal=request.getParameter(begin);
        String endVal=request.getParameter(end);

        if(StringUtils.isEmpty(beginVal)){
            return "";
        }
        return  "从:" + beginVal +"到:" + endVal;
    }

    private String handMonth(JSONObject json, HttpServletRequest request) {
        String fieldName=json.getString("fieldName");
        String tablePre=json.getString("tablePre");

        fieldName =StringUtils.isEmpty(tablePre)?fieldName :tablePre +"." + fieldName;

        String begin="Q_" +fieldName +"_D_GE";



        String beginVal=request.getParameter(begin);
        if(StringUtils.isEmpty(beginVal)){
            return "";
        }
        return  beginVal.substring(0,7);
    }



    private String getParamName(JSONObject json,boolean appendName){

        String tablePre=json.getString("tablePre");
        String fieldName=json.getString("fieldName");
        String dataType=json.getString("dataType");
        String type="S";
        if(typeMap.containsKey(dataType)){
            type=typeMap.get(dataType);
        }
        fieldName =StringUtils.isEmpty(tablePre)?fieldName :tablePre +"." + fieldName;
        String fieldOp=json.getString("fieldOp");
        return "Q_" +fieldName + (appendName?"_display":"") +"_"+type+"_" + fieldOp;
    }

    private String handCommon(JSONObject json,HttpServletRequest request){
        String param=getParamName(json,false);

        String val=request.getParameter(param);
        if(StringUtils.isEmpty(val)){
            return "";
        }
        return val;
    }



    private String handParams(JSONObject json,HttpServletRequest request, IParameterHandler func){
        return func.handParameter(json,request);
    }

}
