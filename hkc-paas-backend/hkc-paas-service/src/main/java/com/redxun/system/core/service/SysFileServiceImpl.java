package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.mapper.SysFileMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [系统附件]业务服务类
*/
@Service
public class SysFileServiceImpl extends SuperServiceImpl<SysFileMapper, SysFile> implements BaseService<SysFile> {

    @Resource
    private SysFileMapper sysFileMapper;

    @Override
    public BaseDao<SysFile> getRepository() {
        return sysFileMapper;
    }
}
