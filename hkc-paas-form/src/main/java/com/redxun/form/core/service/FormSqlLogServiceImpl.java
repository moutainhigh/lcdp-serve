
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormSqlLog;
import com.redxun.form.core.mapper.FormSqlLogMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [SQL执行日志]业务服务类
*/
@Service
public class FormSqlLogServiceImpl extends SuperServiceImpl<FormSqlLogMapper, FormSqlLog> implements BaseService<FormSqlLog> {

    @Resource
    private FormSqlLogMapper formSqlLogMapper;

    @Override
    public BaseDao<FormSqlLog> getRepository() {
        return formSqlLogMapper;
    }

    public void delByType(String type){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TYPE_",type);
        formSqlLogMapper.delete(queryWrapper);
    }
}
