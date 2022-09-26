package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.core.entity.InsMsgboxDef;
import com.redxun.portal.core.mapper.InsMsgboxDefMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [栏目消息盒子表]业务服务类
*/
@Service
public class InsMsgboxDefServiceImpl extends SuperServiceImpl<InsMsgboxDefMapper, InsMsgboxDef> implements BaseService<InsMsgboxDef> {

    @Resource
    private InsMsgboxDefMapper insMsgboxDefMapper;

    @Override
    public BaseDao<InsMsgboxDef> getRepository() {
        return insMsgboxDefMapper;
    }

    public boolean isExist(InsMsgboxDef ent){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty(ent.getBoxId())){
            wrapper.ne("BOX_ID_",ent.getBoxId());
        }
        Integer rtn= insMsgboxDefMapper.selectCount(wrapper);
        return  rtn>0;
    }
}
