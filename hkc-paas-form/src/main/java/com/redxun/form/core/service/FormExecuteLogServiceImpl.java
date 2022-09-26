
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormExecuteLog;
import com.redxun.form.core.mapper.FormExecuteLogMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
* [租户数据源执行记录]业务服务类
*/
@Service
public class FormExecuteLogServiceImpl extends SuperServiceImpl<FormExecuteLogMapper, FormExecuteLog> implements BaseService<FormExecuteLog> {

    @Resource
    private FormExecuteLogMapper formExecuteLogMapper;

    @Override
    public BaseDao<FormExecuteLog> getRepository() {
        return formExecuteLogMapper;
    }

    public IPage getExecuteLog(QueryFilter queryFilter) {
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return formExecuteLogMapper.getExecuteLog(queryFilter.getPage(),params);
    }

    /**
     * 根据批次获取数据记录
     * @param batch
     * @return
     */
    public List<FormExecuteLog> getByBatch(String batch) {
        return formExecuteLogMapper.getByBatch(batch);
    }
}
