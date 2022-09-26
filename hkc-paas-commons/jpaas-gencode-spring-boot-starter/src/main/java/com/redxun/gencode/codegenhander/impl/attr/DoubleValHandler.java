package com.redxun.gencode.codegenhander.impl.attr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.gencode.codegenhander.IFieldHndler;
import com.redxun.gencode.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class DoubleValHandler<E extends BaseEntity<? extends Serializable>> implements IFieldHndler {


    @Override
    public String getAttrName() {
        return "doubleVal";
    }


    @Override
    public String getDescription() {
        return "双值控件";
    }

    @Override
    public void handJsonToEntity(BaseEntity entity,JSONObject tableJsonList,JSONObject entJson, JSONObject formData,String key,String createType) {
        String keyVal = formData.getString(key);
        if(StringUtils.isEmpty(keyVal)){
            return;
        }
        JSONObject keyJson = JSONObject.parseObject(keyVal);
        if(BeanUtil.isEmpty(keyJson)){
            return;
        }
        if("db".equals(createType)){
            JSONObject extJson = entJson.getJSONObject("extJson");
            if(BeanUtil.isEmpty(extJson)){
                return;
            }
            String refName = extJson.getString("refName");
            setFieldValue(entity, refName,keyJson.getString("label"));
            setFieldValue(entity, key,keyJson.getString("value"));
        }else {
            setFieldValue(entity, key+"Name",keyJson.getString("label"));
            setFieldValue(entity, key,keyJson.getString("value"));
        }
    }

    @Override
    public void handEntityToJsonData(BaseEntity entity, JSONObject tableJsonList, JSONObject fileds, JSONObject entJson, JSONObject formData, String key, String createType) {
        JSONObject json = new JSONObject();
        if("db".equals(createType)){
            JSONObject extJson = entJson.getJSONObject("extJson");
            String refName = extJson.getString("refName");
            json.put("label",BeanUtil.getFieldValueFromObject(entity, refName));
            json.put("value",BeanUtil.getFieldValueFromObject(entity, key));
        }else {
            json.put("label",BeanUtil.getFieldValueFromObject(entity, key+"Name"));
            json.put("value",BeanUtil.getFieldValueFromObject(entity, key));
        }
        formData.put(key,json.toJSONString());
    }

    private void setFieldValue(BaseEntity entity, String key,String val){
        if(StringUtils.isEmpty(val)){
            return;
        }
        try {
            BeanUtil.setFieldValue(entity, key,val);
        }catch (Exception e){
            log.error("setFieldValue is error : message ={}", ExceptionUtil.getExceptionMessage(e));
        }

    }

}
