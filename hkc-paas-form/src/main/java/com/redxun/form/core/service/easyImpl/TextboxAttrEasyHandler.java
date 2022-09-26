package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.constvar.ConstVarContext;
import com.redxun.feign.PublicClient;
import com.redxun.form.bo.entity.FormBoAttr;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Component("easyTextboxAttrHandler")
public class TextboxAttrEasyHandler extends BaseAttrEasyHandler {
    @Resource
    GroovyEngine groovyEngine;

    @Resource
    PublicClient publicClient;

    @Resource
    private ConstVarContext constVarContext;

    @Override
    public String getPluginName() {
        return "input";
    }

    @Override
    public String getDescription() {
        return "输入框";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setIsSingle(1);
        return attr;
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        //dataJson
        JSONObject options=setting.getJSONObject("options");
        String from = options.getString("valueSource");

        String val="";
        if("seqno".equals(from)){
            String settings=options.getString("seqNo");
            if(StringUtils.isNotEmpty(settings)){
                val= publicClient.genSeqNo(settings);
            }
        }

        if("script".equals(from)){
            String settings=options.getString("script");
            Map<String,Object> params=new HashMap<>();
            val= (String) groovyEngine.executeScripts(settings,params);
        }

        if("constant".equals(from)){
            String settings = options.getString("constant");
            if (!"".equals(settings)){
                val= constVarContext.getValByKey(settings,new HashMap<>()).toString();
            }
        }

        if(StringUtils.isEmpty(val)){
            String  defaultval= options.getString("defaultValue");
            if(StringUtils.isNotEmpty(defaultval)){
                val=defaultval;
            }
        }

        jsonObject.put(attr.getName(),val);
    }
}
