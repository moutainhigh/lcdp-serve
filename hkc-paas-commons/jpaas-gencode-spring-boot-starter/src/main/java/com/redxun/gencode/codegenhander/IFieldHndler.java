package com.redxun.gencode.codegenhander;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;

import java.io.Serializable;


public interface IFieldHndler<E extends BaseEntity<? extends Serializable>> {
    String getAttrName();

    String getDescription();

    /*
     * 获取json对象的属性值到业务实体
     * @param entity 业务实体
     * @param entJson 控件类型配置信息
     * @param formData 表单数据
     * @param key 业务实体属性名
     * @param createType 业务实体创建类型
     */
    void handJsonToEntity(E entity,JSONObject tableJsonList,JSONObject entJson,JSONObject formData,String key,String createType);

    /*
     * 获取业务实体的属性值
     *@param entity 业务实体
     * @param tableJsonList 主子表配置信息
     * @param entJson 控件类型配置信息（实体属性）
     * @param formData 业务实体转json对象
     * @param key 业务实体属性名
     * @param createType 业务实体创建类型
     */
    void handEntityToJsonData(E entity,JSONObject tableJsonList, JSONObject fileds, JSONObject entJson,JSONObject formData,String key,String createType);


}
