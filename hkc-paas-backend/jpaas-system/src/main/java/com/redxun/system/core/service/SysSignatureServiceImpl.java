package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysSignature;
import com.redxun.system.core.mapper.SysSignatureMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [签名实体]业务服务类
*/
@Service
public class SysSignatureServiceImpl extends SuperServiceImpl<SysSignatureMapper, SysSignature> implements BaseService<SysSignature> {

    @Resource
    private SysSignatureMapper sysSignatureMapper;

    @Override
    public BaseDao<SysSignature> getRepository() {
        return sysSignatureMapper;
    }

    public List<SysSignature> getSignatureList(String userId) {
        return sysSignatureMapper.getSignatureList(userId);
    }

}
