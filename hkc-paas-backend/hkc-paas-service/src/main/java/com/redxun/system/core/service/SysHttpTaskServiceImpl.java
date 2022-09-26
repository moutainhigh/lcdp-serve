
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.SpringUtil;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.mapper.SysHttpTaskMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
* [接口调用表]业务服务类
*/
@Service
public class SysHttpTaskServiceImpl extends SuperServiceImpl<SysHttpTaskMapper, SysHttpTask> implements BaseService<SysHttpTask> {

    @Resource
    private SysHttpTaskMapper sysHttpTaskMapper;

    @Override
    public BaseDao<SysHttpTask> getRepository() {
        return sysHttpTaskMapper;
    }

    public List<SysHttpTask> getByStatus(String status) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("STATUS_",status);
        return sysHttpTaskMapper.selectList(queryWrapper);
    }

    /**
     * 清理调数据库。
     */
    public void clearAll() {
        sysHttpTaskMapper.clearAll();
    }

    /**
     * 执行任务。
     *
     * @param task
     */
    public void handJob(SysHttpTask task) throws Exception{
        JSONArray array=JSONArray.parseArray(task.getParams());
        Class[] args=new Class[array.size()];
        Object[] objs=new Object[array.size()];
        for(int i=0;i<array.size();i++){
            JSONObject json=array.getJSONObject(i);
            args[i]=Class.forName(json.getString("clazz"));
            if("HashMap".equals(args[i].getSimpleName())){
                objs[i] = json.getJSONObject("content").getInnerMap();
            }else {
                objs[i] = json.getObject("content", args[i]);
            }
        }
        Object sourceObj= SpringUtil.getBean(task.getBeanName());
        Method method=sourceObj.getClass().getDeclaredMethod(task.getMethod(),args);
        method.invoke(sourceObj,objs);
    }
}
