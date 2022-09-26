
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormBusInstData;
import com.redxun.form.core.mapper.FormBusInstDataMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [表单业务数据]业务服务类
*/
@Service
public class FormBusInstDataServiceImpl extends SuperServiceImpl<FormBusInstDataMapper, FormBusInstData> implements BaseService<FormBusInstData> {

    @Resource
    private FormBusInstDataMapper formBusInstDataMapper;

    @Override
    public BaseDao<FormBusInstData> getRepository() {
        return formBusInstDataMapper;
    }

    public List<FormBusInstData> getDataByMainPk(String busSolId, String mainPk) {
        List<FormBusInstData> list = formBusInstDataMapper.getDataByMainPk(busSolId,mainPk);
        return list;
    }
}
