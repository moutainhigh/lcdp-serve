
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.FormChangeLog;
import com.redxun.form.core.entity.FormDataSourceDef;
import com.redxun.form.core.entity.FormExecuteLog;
import com.redxun.form.core.mapper.FormChangeLogMapper;
import com.redxun.form.core.mapper.FormDataSourceDefMapper;
import com.redxun.form.core.mapper.FormExecuteLogMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

/**
* [表单变更记录]业务服务类
*/
@Service
public class FormChangeLogServiceImpl extends SuperServiceImpl<FormChangeLogMapper, FormChangeLog> implements BaseService<FormChangeLog> {

    @Resource
    private FormChangeLogMapper formChangeLogMapper;
    @Resource
    private FormExecuteLogServiceImpl formExecuteLogService;
    @Resource
    private OsInstClient osInstClient;
    @Resource
    CommonDao commonDao;
    @Resource
    FormDataSourceDefMapper formDataSourceDefMapper;
    @Resource
    private FormExecuteLogMapper formExecuteLogMapper;

    @Override
    public BaseDao<FormChangeLog> getRepository() {
        return formChangeLogMapper;
    }

    public Integer getMaxSn() {
        return formChangeLogMapper.getMaxSn();
    }

    //同步表单
    public JsonResult syncForm(JSONObject jsonObject) {
        JsonResult jsonResult=new JsonResult();
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            QueryWrapper queryWrapper= new QueryWrapper<FormChangeLog>();
            queryWrapper.eq("ALIAS_",key);
            FormDataSourceDef formDataSourceDef = formDataSourceDefMapper.selectOne(queryWrapper);
            //获取需要同步的记录
            List<String> ids = jsonObject.getObject(key,ArrayList.class);
            QueryWrapper wrapper= new QueryWrapper<FormChangeLog>();
            wrapper.in("ID_", ids);
            wrapper.orderByAsc("SN_");
            List<FormChangeLog> formChangeLogList = formChangeLogMapper.selectList(wrapper);
            if(BeanUtil.isNotEmpty(formChangeLogList) && formChangeLogList.size()>0){
                long time = new Date().getTime();
                for (FormChangeLog formChangeLog : formChangeLogList) {
                    String sql = formChangeLog.getSql();
                    String record="执行成功!";
                    String status="1";
                    try {
                        commonDao.execute(key, sql);
                    }catch (Exception exception){
                        status="0";
                        record=exception.getMessage();
                    }
                    createExecuteLog(formChangeLog,key,status,record,time+"");
                }
            }
            FormChangeLog formChangeLogEnd = formChangeLogList.get(formChangeLogList.size()-1);
            int sn = formChangeLogEnd.getSn();
            formDataSourceDef.setChangeSn(sn);
            formDataSourceDefMapper.updateById(formDataSourceDef);
        }
        return jsonResult.setMessage("同步表单完成").setShow(true).setSuccess(true);
    }

    /**
     * sql执行记录
     * @param formChangeLog 表单变更记录
     * @param status sql执行状态
     * @param record 执行记录
     * @param batch 批次 时间戳
     */
    public void createExecuteLog(FormChangeLog formChangeLog,String datasource, String status,String record,String batch){
        IUser user = ContextUtil.getCurrentUser();
        FormExecuteLog formExecuteLog=new FormExecuteLog();
        formExecuteLog.setDatasource(datasource);
        formExecuteLog.setChangeLogId(formChangeLog.getId());
        formExecuteLog.setSql(formChangeLog.getSql());
        formExecuteLog.setStatus(status);
        formExecuteLog.setRecord(record);
        formExecuteLog.setCreateByName(user.getFullName());
        formExecuteLog.setBatch(batch);
        formExecuteLogService.insert(formExecuteLog);
    }

    /**
     * 根据数据源别名获取表单变更记录
     * @param dsAlias
     * @return
     */
    public JsonResult getFormChangeLog(String dsAlias,String params) {
        JSONObject queryParams=new JSONObject();
        if(StringUtils.isNotEmpty(params)){
            queryParams = JSONObject.parseObject(params);
        }
        //根据租户ID获取表单变更记录
        List<FormChangeLog> formChangeLogList=formChangeLogMapper.queryChangeLog(dsAlias,queryParams);
        return new JsonResult(true, formChangeLogList,"获取成功!").setShow(false);
    }

    /**
     * 根据数据源别名获取已执行的记录
     * @param dsAlias 数据源别名
     * @return
     */
    public JsonResult getExecutedLog(String dsAlias) {
        List<FormExecuteLog> executedLog=formChangeLogMapper.getExecutedLog(dsAlias);
        return new JsonResult(true, executedLog,"获取成功!").setShow(false);
    }

    public JsonResult setIgnore(String dsAlias, String changeLogId, Boolean ignore) {
        FormChangeLog formChangeLog = formChangeLogMapper.selectById(changeLogId);
        //设置忽略
        if(ignore){
            long time = new Date().getTime();
            createExecuteLog(formChangeLog,dsAlias,"-1","忽略成功",time+"");
        }else { //取消忽略
            formExecuteLogMapper.delIgnoreFormChangeLog(formChangeLog.getId(),dsAlias);
        }
        return new JsonResult(true,"设置成功！");
    }

    /**
     * 根据数据源别名获取获取忽略的记录
     * @param dsAlias
     * @return
     */
    public JsonResult getIgnoreFormChangeLog(String dsAlias) {
        List<FormChangeLog> formChangeLogs=formChangeLogMapper.getIgnoreFormChangeLog(dsAlias);
        return new JsonResult(true, formChangeLogs,"获取成功!").setShow(false);
    }
}
