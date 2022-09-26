
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.SaveExport;
import com.redxun.form.core.mapper.SaveExportMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;

/**
* [form_save_export]业务服务类
*/
@Service
public class SaveExportServiceImpl extends SuperServiceImpl<SaveExportMapper, SaveExport> implements BaseService<SaveExport> {

    @Resource
    private SaveExportMapper saveExportMapper;

    @Override
    public BaseDao<SaveExport> getRepository() {
        return saveExportMapper;
    }


    @Override
    public int insert(SaveExport entity) {

        return 0;
    }

    public JsonResult addList(JSONObject json){
        String boAlias=json.getString("dataList");
        String name=json.getString("name");

        SaveExport saveExport = new SaveExport();

        saveExport.setDataList(boAlias);
        String key = json.getJSONArray("key").toString();
        saveExport.setSetting(key);
        saveExport.setAppId(json.getString("appId"));
        saveExport.setName(name);
        saveExport.setIsPublic(Integer.parseInt(json.getString("isPublic")));

        if(StringUtils.isNotEmpty(json.getString("id"))){
            saveExport.setId(json.getString("id"));
            saveExportMapper.updateById(saveExport);
        }else {
            saveExport.setId(IdGenerator.getIdStr());
            saveExportMapper.insert(saveExport);
        }


        return JsonResult.Success().setMessage("保存成功");
    }

    private boolean isExist(String boAlias,String name){
        String tenantId= ContextUtil.getCurrentTenantId();
        QueryWrapper wrapper =new QueryWrapper();
        wrapper.eq("DATA_LIST_",boAlias);
        wrapper.eq("NAME_",name);
        wrapper.eq("TENANT_ID_",tenantId);
        Integer rtn = saveExportMapper.selectCount(wrapper);
        return rtn>0;
    }

    public List<SaveExport> getByList(String dataList, String userId){
        return saveExportMapper.getByDataList(dataList,userId);
    }


    /**
     * 根据名称获取公开的配置。
     * @param dataList
     * @param appId
     * @return
     */
    public List<SaveExport> getByListPublic(String dataList){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DATA_LIST_",dataList);
        queryWrapper.eq("IS_PUBLIC_",1);
        return saveExportMapper.selectList(queryWrapper);
    }

    /**
     * 获取我创建的导出配置。
     * @param dataList
     * @param userId
     * @return
     */
    public List<SaveExport> getByListPrivate(String dataList,String userId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DATA_LIST_",dataList);
        queryWrapper.eq("IS_PUBLIC_",0);
        queryWrapper.eq("CREATE_BY_",userId);

        return saveExportMapper.selectList(queryWrapper);
    }

    public int removeConfig(String name, String dataList){
        return saveExportMapper.removeConfig(name, dataList);
    }

    public SaveExport getByName(String name, String dataList){
        return saveExportMapper.getByName(name, dataList);
    }

    public void deleteByDataList(String key,String appId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DATA_LIST_",key);
        queryWrapper.eq("IS_PUBLIC_",1);
        if(StringUtils.isNotEmpty(appId)) {
            queryWrapper.eq("APP_ID_", appId);
        }
        saveExportMapper.delete(queryWrapper);
    }

    public void saveConfig(String excelExport, String key, String appId) {
        deleteByDataList(key,appId);
        if(StringUtils.isEmpty(excelExport)){
            return;
        }
        JSONArray array=JSONArray.parseArray(excelExport);
        for(Object obj:array){
            JSONObject jsonObject=(JSONObject)obj;
            SaveExport saveExport=JSONObject.toJavaObject(jsonObject,SaveExport.class);
            if(jsonObject.containsKey("isNew") && jsonObject.getBoolean("isNew")){
                saveExport.setId(IdGenerator.getIdStr());
            }
            saveExport.setAppId(appId);
            saveExportMapper.insert(saveExport);
        }
    }
}
