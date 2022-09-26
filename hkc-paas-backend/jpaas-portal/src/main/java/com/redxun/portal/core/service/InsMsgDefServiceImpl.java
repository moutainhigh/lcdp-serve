package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.portal.core.entity.InsMsgDef;
import com.redxun.portal.core.mapper.InsMsgDefMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [INS_MSG_DEF]业务服务类
*/
@Service
public class InsMsgDefServiceImpl extends SuperServiceImpl<InsMsgDefMapper, InsMsgDef> implements BaseService<InsMsgDef> {

    @Resource
    private InsMsgDefMapper insMsgDefMapper;

    @Override
    public BaseDao<InsMsgDef> getRepository() {
        return insMsgDefMapper;
    }

    public List<InsMsgDef> getDataByBoxId(String boxId){
        return insMsgDefMapper.getDataByBoxId(boxId);
    }
}
