package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.db.CommonDao;
import com.redxun.feign.common.SystemClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.mapper.FormBoEntityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class FormDataService {

    @Resource
    FormBoEntityMapper formBoEntityMapper;
    @Resource
    CommonDao commonDao;

    @Resource
    private SystemClient systemClient;

    /**
     * @Description:  查找系统树所属应用ID
     * @param treeId sys_tree表的主键值
     * @return java.lang.String  sys_tree表记录中APP_ID_字段的值
     * @Author: Elwin ZHANG  @Date: 2021/7/6 17:16
     **/
    public String getAppIdByTreeId(String treeId){
        try {
            String url = "/system/core/sysTree/get";
            JSONObject params = new JSONObject();
            params.put("pkId", treeId);
            Object o = systemClient.executeGetApi(url, params);
            HashMap<String,Object> result = (HashMap<String,Object>) o;
            HashMap<String,Object> data=(HashMap<String,Object>)result.get("data");
            String appId = data.get("appId").toString();
            return appId;
        }catch (Exception e ){
            return "";
        }
    }

    /**
     * 更新业务表数据状态。
     * @param boAlias
     * @param instId
     * @param status
     * @param pk
     */
    public void updDataByStatus(String boAlias,String instId,String status,String pk){
        FormBoEntity boEntity= formBoEntityMapper.getMainEntByBoAlias(boAlias);
        if(BeanUtil.isEmpty(boEntity)){
            return;
        }
        String sql="UPDATE  "+ boEntity.getTableName() +" SET INST_STATUS_=#{status} , INST_ID_=#{instId} where ID_=#{id}";
        SqlModel sqlModel=new SqlModel(sql);
        sqlModel.addParam("status",status);
        sqlModel.addParam("instId",instId);
        sqlModel.addParam("id",pk);
        commonDao.execute(boEntity.getDsAlias(), sqlModel);
    }
}
