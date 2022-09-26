package com.redxun.portal.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import com.redxun.portal.core.entity.InfInbox;
import com.redxun.portal.core.entity.InfInnerMsg;
import com.redxun.portal.core.mapper.InfInboxMapper;
import com.redxun.portal.core.mapper.InfInnerMsgMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* [内部短消息收件箱]业务服务类
*/
@Service
public class InfInboxServiceImpl extends SuperServiceImpl<InfInboxMapper, InfInbox> implements BaseService<InfInbox> {

    @Resource
    private InfInboxMapper infInboxMapper;

    @Override
    public BaseDao<InfInbox> getRepository() {
        return infInboxMapper;
    }

    /**
     * 使用QueryFilter 进行查询。
     * @param queryFilter
     * @return
     */
    @Override
    public IPage query(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return  infInboxMapper.query(queryFilter.getPage(),params, ContextUtil.getCurrentUserId());
    }

    public InfInbox getByMsgIdAndUserId(String msgId, String recUserId,String recType) {
        return infInboxMapper.getByMsgIdAndUserId(msgId,recUserId,recType);
    }
}
