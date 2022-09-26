package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.constvar.ConstVarContext;
import com.redxun.dboperator.model.Column;
import com.redxun.feign.PublicClient;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class TextboxAttrHandler extends BaseAttrHandler {

    @Resource
    GroovyEngine groovyEngine;

    @Override
    public String getPluginName() {
        return "rx-textbox";
    }

    @Override
    public String getDescription() {
        return "文本控件";
    }

    @Resource
    PublicClient publicClient;

    @Resource
    private ConstVarContext constVarContext;

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        //dataJson
        String from=setting.getString("from");
        String settings=setting.getString("setting");

        String val="";
        if("seqno".equals(from)){
            if(StringUtils.isNotEmpty(settings)){
                val= publicClient.genSeqNo(settings);
            }
        }

        if("script".equals(from)){
            Map<String,Object> params=new HashMap<>();
            val= (String) groovyEngine.executeScripts(settings,params);
        }

        if("constant".equals(from)){
            if (!"".equals(settings)){
                val= constVarContext.getValByKey(settings,new HashMap<>()).toString();
            }
        }

        if(StringUtils.isEmpty(val)){
           String  defaultval= setting.getString("defaultval");
           if(StringUtils.isNotEmpty(defaultval)){
               val=defaultval;
           }
        }
        if(Column.COLUMN_TYPE_NUMBER.equals(attr.getDataType())){
            if(StringUtils.isEmpty(val)){
                jsonObject.put(attr.getName(),0);
            }
            else {
                if(val.indexOf(".")==-1) {
                    jsonObject.put(attr.getName(), Integer.parseInt(val));
                }
                else{
                    jsonObject.put(attr.getName(), Double.parseDouble(val));
                }
            }
        }
        else{
            jsonObject.put(attr.getName(),val);
        }



    }


    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String from=jsonObject.getString("from");
        JSONObject json=new JSONObject();
        json.put("from",from);
        if("seqno".equals(from)){
            json.put("setting",jsonObject.getString("seqNo"));
        }
        if("script".equals( from)){
            json.put("setting",jsonObject.getString("script"));
        }
        if("constant".equals( from)){
            json.put("setting",jsonObject.getString("constant"));
        }

        String defaultVal=jsonObject.getString("defaultval");
        if(StringUtils.isNotEmpty(defaultVal)){
            json.put("defaultval",defaultVal);
        }

        field.setDataJson(json);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable){
        JSONObject fieldJson=super.parseMetadata(boAttr,isTable);
        fieldJson.put("from","input");
        return fieldJson;
    }
}
