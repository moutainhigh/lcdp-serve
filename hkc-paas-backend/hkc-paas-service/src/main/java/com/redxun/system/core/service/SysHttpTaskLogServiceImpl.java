
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.entity.SysHttpTaskLog;
import com.redxun.system.core.mapper.SysHttpTaskLogMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * [接口调用日志表]业务服务类
 */
@Service
public class SysHttpTaskLogServiceImpl extends SuperServiceImpl<SysHttpTaskLogMapper, SysHttpTaskLog> implements BaseService<SysHttpTaskLog> {
    @Resource
    private SysHttpTaskLogMapper sysHttpTaskLogMapper;
    @Resource
    SysHttpTaskServiceImpl sysHttpTaskService;

    @Value("${retry.maxAttempts:3}")
    private int maxAttempts;
    @Value("${retry.delay:2000}")
    private int delay;
    @Value("${retry.multiplier:1.5}")
    private double multiplier;
    @Value("${retry.maxError:3}")
    private int maxError;

    @Override
    public BaseDao<SysHttpTaskLog> getRepository() {
        return sysHttpTaskLogMapper;
    }

    /**
     * 清理调数据库。
     */
    public void clearAll() {
        sysHttpTaskLogMapper.clearAll();
    }

    /**
     * 保存日志
     * @param batId
     * @param type
     * @param relId
     * @param relName
     * @param beanName
     * @param method
     * @param log
     * @param success
     * @param params
     */
    public void createLog(String batId,String type,String relId,String relName,
                          String beanName, String method, SysHttpTaskLog log, boolean success,Object... params) {
        if(StringUtils.isEmpty(batId)){
            return;
        }
        String tenantId=ContextUtil.getCurrentTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId="0";
            log.setTenantId(tenantId);
        }
        insert(log);
        SysHttpTask sysHttpTask=sysHttpTaskService.getById(batId);
        if(sysHttpTask==null){
            sysHttpTask=new SysHttpTask();
            sysHttpTask.setType(type).setRelId(relId).setRelName(relName)
                    .setBeanName(beanName).setMethod(method).setExecuteTimes(0)
                    .setMaxAttempts(maxAttempts).setDelay(delay).setMultiplier(multiplier).setTenantId(tenantId);
        }
        JSONArray array=new JSONArray();
        for(Object obj:params){
            JSONObject json=new JSONObject();
            Class clazz=obj.getClass();
            json.put("clazz",clazz.getName());
            if(!"String".equals(clazz.getSimpleName())){
                json.put("content",JSONObject.toJSONString(obj));
            }else{
                json.put("content",obj);
            }
            array.add(json);
        }
        JSONObject json=new JSONObject();
        json.put("clazz",String.class.getName());
        json.put("content",batId);
        array.add(json);
        sysHttpTask.setParams(array.toJSONString());
        sysHttpTask.setExecuteTimes(sysHttpTask.getExecuteTimes() + 1);
        if(success){
            //执行成功
            sysHttpTask.setStatus(SysHttpTask.STATUS_OK);
        }else {
            //执行失败
            if (sysHttpTask.getExecuteTimes() < maxError) {
                sysHttpTask.setStatus(SysHttpTask.STATUS_FAIL);
            } else {
                sysHttpTask.setStatus(SysHttpTask.STATUS_FAILEND);
            }
        }
        if(StringUtils.isEmpty(sysHttpTask.getId())){
            sysHttpTask.setPkId(batId);
            sysHttpTaskService.insert(sysHttpTask);
        }else{
            sysHttpTaskService.update(sysHttpTask);
        }
    }

    public void deleteByTaskId(String taskId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TASK_ID_",taskId);
        sysHttpTaskLogMapper.delete(queryWrapper);
    }

    public IPage getAllByTaskId(QueryFilter filter) {
        Map<String,Object> params= PageHelper.constructParams(filter);
        return sysHttpTaskLogMapper.getAllByTaskId(filter.getPage(),params);
    }
}
