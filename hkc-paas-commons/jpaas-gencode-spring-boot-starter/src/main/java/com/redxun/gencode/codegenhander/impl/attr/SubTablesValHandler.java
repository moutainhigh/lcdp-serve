package com.redxun.gencode.codegenhander.impl.attr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.gencode.codegenhander.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class SubTablesValHandler<E extends BaseEntity<? extends Serializable>> implements IFieldHndler {
        @Override
    public String getAttrName() {
        return "subTablesVal";
    }

    @Override
    public String getDescription() {
        return "子表控件";
    }

    @Override
    public void handJsonToEntity(BaseEntity mainEntity,JSONObject tableJsonList,JSONObject entJson, JSONObject formData,String key,String createType) {
        String subTableAlias = entJson.getString("fieldName");
        JSONArray subJsonList=formData.getJSONArray("sub__"+subTableAlias);
        if(BeanUtil.isEmpty(subJsonList)){
            return;
        }
        List list = new ArrayList<>();
        String genMode = entJson.getString("genMode");
        for(int i=0;i<subJsonList.size();i++){
            BaseEntity subEntity=getSubEntityByMain(mainEntity,key);
            JSONObject subJson = subJsonList.getJSONObject(i);
            //根据表单KEY获取表单配置信息
            JSONObject subAliaFormJson = tableJsonList.getJSONObject(subTableAlias);
            //获取表单属性列配置信息
            JSONObject subEntityJson = subAliaFormJson.getJSONObject("fileds");

            Iterator<String> iterator = subEntityJson.keySet().iterator();
            while(iterator.hasNext()){
                String subKey = iterator.next();
                JSONObject subEnt = subEntityJson.getJSONObject(subKey);
                IFieldHndler control = IFieldContext.getByType(subEnt.getString("ctr"));
                control.handJsonToEntity(subEntity,tableJsonList,subEnt,subJson,subKey,genMode);
            }

            list.add(subEntity);
        }
        BeanUtil.setFieldValue(mainEntity, key,list);
    }

    /**
     * 通过主表获取子表实体
     */
    private BaseEntity getSubEntityByMain(BaseEntity entity,String subKey){
        BaseEntity subEntity=null;
        try {
            Class cls = entity.getClass();
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if(!field.getName().equals(subKey)){
                    continue;
                }
                if(!field.isAccessible()){
                    field.setAccessible(true);
                }
                SubTableDef subTableDef =field.getAnnotation(SubTableDef.class);
                Class aClass = subTableDef.className();
                subEntity = (BaseEntity)aClass.newInstance();
                break;
            }
        }catch (Exception e){
            log.error("**** getSubEntityBuMain is error: message={}", ExceptionUtil.getExceptionMessage(e));
        }
        return subEntity;
    }

    @Override
    public void handEntityToJsonData(BaseEntity entity,JSONObject tableJsonList, JSONObject fileds, JSONObject entJson,JSONObject formData,String key,String createType) {
        //子表数据
        JSONArray subJsonList = new JSONArray();
        Object subLis = BeanUtil.getFieldValueFromObject(entity, key);
        if(BeanUtil.isEmpty(subLis)){
            return;
        }
        String subTableAlias = entJson.getString("fieldName");
        String genMode = entJson.getString("genMode");
        List<E> subList = (List)subLis;
        for (E subEnt:subList) {
            JSONObject subFormData =new JSONObject();
            JSONObject resultJson=null;
            //根据子表表单KEY获取表单配置信息
            JSONObject aliaFormJson = tableJsonList.getJSONObject(subTableAlias);
            //获取子表表单属性列配置信息
            JSONObject subEntityJson = aliaFormJson.getJSONObject("fileds");

            Iterator<String> iterator = subEntityJson.keySet().iterator();
            while(iterator.hasNext()){
                String subKey = iterator.next();
                JSONObject subEntJson = subEntityJson.getJSONObject(subKey);
                IFieldHndler control = IFieldContext.getByType(subEntJson.getString("ctr"));
                control.handEntityToJsonData(subEnt,tableJsonList,subEntityJson, subEntJson,subFormData,subKey,genMode);
            }
            subJsonList.add(subFormData);
        }
        formData.put("sub__"+subTableAlias,subJsonList);
    }
}
