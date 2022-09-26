package com.redxun.gencode.codegenhander.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.gencode.codegenhander.IConvertHandler;
import com.redxun.gencode.codegenhander.IFieldContext;
import com.redxun.gencode.codegenhander.IFieldHndler;
import com.redxun.gencode.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 表单创建类型解析执行策略
 */
@Slf4j
@Component
public class FormConvertHandler<E extends BaseEntity<? extends Serializable>> implements IConvertHandler {
    @Override
    public String getCreateType() {
        return "form";
    }

    @Override
    public JSONObject handEntityToJsonData(BaseEntity result, String formAlias, String pack,String genMode) {
        JSONObject formData =new JSONObject();
        if(BeanUtil.isEmpty(result)) {
            return formData;
        }

        //获取主子表配置信息
        JSONObject typeFileJson = ReaderFileUtil.getEntityJson(formAlias,pack);
        JSONObject tableJsonList = typeFileJson.getJSONObject("tables");
        //根据表单KEY获取表单配置信息
        JSONObject aliaFormJson = tableJsonList.getJSONObject(formAlias);
        //获取表单属性列配置信息
        JSONObject entityJson = aliaFormJson.getJSONObject("fileds");

        Iterator<String> iterator = entityJson.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            JSONObject ent = entityJson.getJSONObject(key);
            IFieldHndler control = IFieldContext.getByType(ent.getString("ctr"));
            control.handEntityToJsonData(result,tableJsonList,entityJson, ent,formData,key,genMode);
        }

        formData.put("initData",typeFileJson.getJSONObject("initData"));
        return  formData;
    }

    @Override
    public BaseEntity<? extends Serializable> handJsonToEntity(JSONObject formData, BaseEntity entity,
                                                               String formAlias, String pack,String genMode) {
        try {
            //获取主子表配置信息
            JSONObject typeFileJson = ReaderFileUtil.getEntityJson(formAlias,pack);
            JSONObject tableJsonList = typeFileJson.getJSONObject("tables");
            //根据表单KEY获取表单配置信息
            JSONObject aliaFormJson = tableJsonList.getJSONObject(formAlias);
            //获取表单属性列配置信息
            JSONObject entityJson = aliaFormJson.getJSONObject("fileds");

            Iterator<String> iterator = entityJson.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                JSONObject ent = entityJson.getJSONObject(key);
                IFieldHndler control = IFieldContext.getByType(ent.getString("ctr"));
                control.handJsonToEntity(entity,tableJsonList,ent,formData,key,genMode);
            }
        }catch (Exception e){
            log.error("handJsonToEntyForm is error : message ={}", ExceptionUtil.getExceptionMessage(e));
        }
        return entity;
    }

    private void setFieldValue(BaseEntity entity, String key,String val){
        if(StringUtils.isEmpty(val)){
            return;
        }
        try {
            BeanUtil.setFieldValue(entity, key,val);
        }catch (Exception e){
            log.error("setFieldValue is error : message ={}",ExceptionUtil.getExceptionMessage(e));
        }

    }
}
