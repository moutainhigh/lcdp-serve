package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;

import java.util.Arrays;
import java.util.List;

public class FormHelper {

    //不做处理的控件类型
    public final static List<String> excludeCtlList = Arrays.asList(new String[]{"rx-ref", "rx-commonfield"});

    /**
     * 给BO的每一个属性赋值 formsetting和 datasetting
     * @param boEntity
     * @param formSetting
     * @param dataSetting
     */
    public static   void injectAttrJson(FormBoEntity boEntity, String formSetting, String dataSetting){
        if(StringUtils.isEmpty(formSetting) && StringUtils.isEmpty(dataSetting)) {
            return;
        }
        JSONObject dataSettingJson=new JSONObject();
        if (StringUtils.isNotEmpty(dataSetting)) {
            dataSettingJson=JSONObject.parseObject(dataSetting);
        }

        JSONObject formSettingJson=new JSONObject();
        if (StringUtils.isNotEmpty(formSetting)) {
            formSettingJson=JSONObject.parseObject(formSetting);
        }

        injectAttr(boEntity,true,dataSettingJson,formSettingJson);

        for(FormBoEntity subEnt:boEntity.getBoEntityList()){
            injectAttr(subEnt,false,dataSettingJson,formSettingJson);
        }
    }


    private static void injectAttr(FormBoEntity boEntity,boolean isMain,JSONObject dataSettingJson,JSONObject formSetting){
        if(dataSettingJson==null){
            dataSettingJson=new JSONObject();
        }
        if(formSetting==null){
            formSetting=new JSONObject();
        }
        String key=isMain? FormBoRelation.RELATION_MAIN:boEntity.getAlias();

        JSONObject dataJson=dataSettingJson.getJSONObject(key);
        JSONObject formJson=formSetting.getJSONObject(key);
        if(dataJson==null){
            dataJson=new JSONObject();
        }
        if(formJson==null){
            formJson=new JSONObject();
        }
        List<FormBoAttr> attrList=boEntity.getBoAttrList();

        for(FormBoAttr attr:attrList){
            if(excludeCtlList.contains(attr.getControl())){
                continue;
            }
            JSONObject attrDataJson= dataJson.getJSONObject(attr.getName());
            if(attrDataJson==null){
                attrDataJson=new JSONObject();
            }
            JSONObject attrFormSettingJson= formJson.getJSONObject(attr.getName());
            if(attrFormSettingJson==null){
                attrFormSettingJson=new JSONObject();
            }
            attr.setDataJson(attrDataJson);
            attr.setFormJson(attrFormSettingJson);
        }

    }
}
