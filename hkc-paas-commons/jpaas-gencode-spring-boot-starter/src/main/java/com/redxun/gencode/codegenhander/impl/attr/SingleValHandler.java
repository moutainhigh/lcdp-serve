package com.redxun.gencode.codegenhander.impl.attr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.gencode.codegenhander.IFieldHndler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class SingleValHandler<E extends BaseEntity<? extends Serializable>> implements IFieldHndler {
        @Override
    public String getAttrName() {
        return "singleVal";
    }

    @Override
    public String getDescription() {
        return "单值控件";
    }

    @Override
    public void handJsonToEntity(BaseEntity entity,JSONObject tableJsonList,JSONObject entJson, JSONObject formData,String key,String createType) {
        Object obj=formData.get(key);
        if(BeanUtil.isEmpty(obj)){
            return;
        }
        BeanUtil.setFieldValue(entity, key,obj);
    }

    @Override
    public void handEntityToJsonData(BaseEntity entity, JSONObject tableJsonList, JSONObject fileds, JSONObject entJson, JSONObject formData, String key, String createType) {
        //单值，直接设置
        Object val = BeanUtil.getFieldValueFromObject(entity, key);
        formData.put(key,val);
    }
}
