
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormExcelGenTask;
import com.redxun.form.core.mapper.FormExcelGenTaskMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [Excel生成任务]业务服务类
*/
@Service
public class FormExcelGenTaskServiceImpl extends SuperServiceImpl<FormExcelGenTaskMapper, FormExcelGenTask> implements BaseService<FormExcelGenTask> {

    @Resource
    private FormExcelGenTaskMapper formExcelGenTaskMapper;

    @Override
    public BaseDao<FormExcelGenTask> getRepository() {
        return formExcelGenTaskMapper;
    }

}
