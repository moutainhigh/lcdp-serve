
package com.redxun.user.org.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.user.org.entity.OsDdCorp;
import com.redxun.user.org.mapper.OsDdCorpMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [os_dd_corp]业务服务类
*/
@Service
public class OsDdCorpServiceImpl extends SuperServiceImpl<OsDdCorpMapper, OsDdCorp> implements BaseService<OsDdCorp> {

    @Resource
    private OsDdCorpMapper osDdCorpMapper;

    @Override
    public BaseDao<OsDdCorp> getRepository() {
        return osDdCorpMapper;
    }

}
