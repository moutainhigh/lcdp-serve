package com.redxun.gencode.codegenhander;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.BaseEntity;

import java.io.Serializable;

/**
 * json与实体互相解析接口。
 * <pre>
 *     接口功能:
 *     1.负责将客户端的数据，转成类的属性
 *     2.负责将类转成，客户端可以使用的数据。
 * </pre>
 * @author csx
 */
public interface IConvertHandler<E extends BaseEntity<? extends Serializable>> {
    /**
     * 获取创建类型：form:表单创建、db:物理表创建、create:手工创建、
     * @return
     */
    String getCreateType();

    /**
     * 实体转json接口
     * @return
     */
    JSONObject handEntityToJsonData(E result,String formAlias,String pack,String genMode);

    /**
     * json转实体接口
     * @return
     */
    BaseEntity<? extends Serializable> handJsonToEntity(JSONObject formData,E entity,String formAlias,String pack,String genMode);

}
