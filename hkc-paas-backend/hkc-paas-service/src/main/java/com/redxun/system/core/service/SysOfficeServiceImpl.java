package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysOffice;
import com.redxun.system.core.mapper.SysOfficeMapper;
import com.redxun.system.core.mapper.SysOfficeVerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [SYS_OFFICE]业务服务类
*/
@Service
public class SysOfficeServiceImpl extends SuperServiceImpl<SysOfficeMapper, SysOffice> implements BaseService<SysOffice> {

    @Resource
    private SysOfficeMapper sysOfficeMapper;
    @Resource
    private SysOfficeVerMapper sysOfficeVerMapper;

    @Override
    public BaseDao<SysOffice> getRepository() {
        return sysOfficeMapper;
    }

    /**
     * 根据office ID获取最大的版本。
     * @param officeId
     * @return
     */
    public Integer getVersionByOfficeId(String officeId){
        Integer rtn=sysOfficeVerMapper.getVersionByOfficeId(officeId);
        return rtn;

    }
}
