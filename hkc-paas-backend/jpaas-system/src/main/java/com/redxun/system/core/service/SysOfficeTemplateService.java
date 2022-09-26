package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysOfficeTemplate;
import com.redxun.system.core.mapper.SysOfficeTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [SYS_OFFICE_TEMPLATE]业务服务类
*/
@Service
public class SysOfficeTemplateService extends SuperServiceImpl<SysOfficeTemplateMapper, SysOfficeTemplate> implements BaseService<SysOfficeTemplate> {

    @Resource
    private SysOfficeTemplateMapper sysOfficeTemplateMapper;

    @Override
    public BaseDao<SysOfficeTemplate> getRepository() {
        return sysOfficeTemplateMapper;
    }
}
