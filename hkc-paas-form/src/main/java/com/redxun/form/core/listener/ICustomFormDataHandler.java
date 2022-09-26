package com.redxun.form.core.listener;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.form.DataResult;

/**
 * 自定义表单数据处理器接口。
 *
 * @author ray
 */
public interface ICustomFormDataHandler {
    /**
     * 获取初始数据。
     *
     * @param boDefId
     * @return
     */
    JSONObject getInitData(String boDefId);

    /**
     * 根据pk返回数据。
     *
     * @param pk
     * @param boDefId
     * @return
     */
    JSONObject getByPk(String pk, String boDefId);

    /**
     * 保存表单数据。
     *
     * @param boDefId
     * @param jsonData
     * @param isResume
     * @return
     */
    DataResult save(String boDefId, JSONObject jsonData,boolean isResume);

    /**
     * 删除数据
     *
     * @param id
     */
    JsonResult delById(String id);

}
