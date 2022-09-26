
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysKettleDbdef;
import com.redxun.system.core.mapper.SysKettleDbdefMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [KETTLE资源库定义]业务服务类
*/
@Service
public class SysKettleDbdefServiceImpl extends SuperServiceImpl<SysKettleDbdefMapper, SysKettleDbdef> implements BaseService<SysKettleDbdef> {

    @Resource
    private SysKettleDbdefMapper sysKettleDbdefMapper;

    @Override
    public BaseDao<SysKettleDbdef> getRepository() {
        return sysKettleDbdefMapper;
    }

    public List getAllByStatus(String status) {
        return sysKettleDbdefMapper.getAllByStatus(status);
    }
}
