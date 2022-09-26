package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.FormQueryStrategy;
import com.redxun.form.core.mapper.FormQueryStrategyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * [查询策略表]业务服务类
 */
@Service
public class FormQueryStrategyServiceImpl extends SuperServiceImpl<FormQueryStrategyMapper, FormQueryStrategy> implements BaseService<FormQueryStrategy> {

    @Resource
    private FormQueryStrategyMapper formQueryStrategyMapper;

    @Override
    public BaseDao<FormQueryStrategy> getRepository() {
        return formQueryStrategyMapper;
    }

    public List<FormQueryStrategy> getByListId(String listId,String viewType){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("LIST_ID_", listId);
        queryWrapper.eq("VIEW_TYPE_", "SYSTEM");
        List<FormQueryStrategy> systemList = formQueryStrategyMapper.selectList(queryWrapper);
        return systemList;
    }

    public List<FormQueryStrategy> getByListId(String listId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("LIST_ID_", listId);
        queryWrapper.eq("VIEW_TYPE_", "SYSTEM");
        List<FormQueryStrategy> systemList = formQueryStrategyMapper.selectList(queryWrapper);
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("LIST_ID_", listId);
        queryWrapper.eq("VIEW_TYPE_", "USER");
        queryWrapper.eq("CREATE_BY_", ContextUtil.getCurrentUserId());
        List<FormQueryStrategy> privateList = formQueryStrategyMapper.selectList(queryWrapper);
        systemList.addAll(privateList);
        return systemList;
    }

    public void deleteByBoListId(String boListId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("LIST_ID_",boListId);
        queryWrapper.eq("VIEW_TYPE_", "SYSTEM");
        formQueryStrategyMapper.delete(queryWrapper);
    }

    public void saveConfig(String queryStrategyViewJson, String boListId,String appId) {
        deleteByBoListId(boListId);
        JSONArray array=JSONArray.parseArray(queryStrategyViewJson);
        for(Object obj:array){
            JSONObject jsonObject=(JSONObject)obj;
            FormQueryStrategy formQueryStrategy=JSONObject.toJavaObject(jsonObject,FormQueryStrategy.class);
            if(jsonObject.containsKey("isNew") && jsonObject.getBoolean("isNew")){
                formQueryStrategy.setPkId(IdGenerator.getIdStr());
            }
            formQueryStrategy.setListId(boListId);
            formQueryStrategy.setAppId(appId);
            insert(formQueryStrategy);
        }
    }
}
