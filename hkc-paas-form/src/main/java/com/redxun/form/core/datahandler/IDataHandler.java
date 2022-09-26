package com.redxun.form.core.datahandler;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.form.DataResult;
import com.redxun.form.bo.entity.FormBoEntity;

import java.util.List;
import java.util.Map;

public interface IDataHandler {

    /**
     * 根据pkId读取数据
     * @param boDefId
     * @param pkId
     * @return
     */
    JSONObject getDataById(String boDefId,String pkId);
    /**
     * 读取一个表的数据。
     * @param formAlias
     * @param parentId
     * @return
     */
    List<JSONObject> getData(String formAlias,String parentId);


    JSONObject getInitData(String formAlias);

    JSONObject getInitDataByBoAlias(String boAlias);
    /**
     * 根据pk 获取业务数据。
     * @param pk
     * @param formAlias
     * @return
     */
    JSONObject getById(String formAlias,String pk);


    /**
     *
     * @param formAlias
     * @param params
     * @return
     */
    JSONObject getByParams(String formAlias, Map<String,Object> params);


    /**
     * {
     * 	name:"",
     * 	city:""
     * 	sub__user:{
     * 		name:"",
     * 		city:""
     *  },
     * 	sub__students:[
     *      {name:"",class:"",area:"",sub__sunzi:[
     *          {school:"",year:""}
     *       	]
     *       }
     * 	]
     * }
     * @param data          表单数据
     * @param formAlias     表单别名
     * @param isResume      是否是恢复操作
     * @return
     */
    DataResult save(JSONObject data, String formAlias, FormBoEntity boEntity,boolean isResume);

    /**
     * 根据主键，删除数据
     *
     * @param formAlias
     * @param pk
     * @return
     */
    JsonResult removeById(String formAlias, String pk);


    /**
     * 根据主键与实体 获取数据
     *
     * @param pk
     * @param boEntity
     * @return
     */
    JSONObject getByPk(String pk,FormBoEntity boEntity);

}
