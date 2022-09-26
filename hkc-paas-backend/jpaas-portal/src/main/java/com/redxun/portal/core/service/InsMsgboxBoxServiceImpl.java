package com.redxun.portal.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.portal.core.entity.InsMsgboxBox;
import com.redxun.portal.core.mapper.InsMsgboxBoxMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * [INS_MSGBOX_BOX_DEF]业务服务类
 */
@Service
public class InsMsgboxBoxServiceImpl extends SuperServiceImpl<InsMsgboxBoxMapper, InsMsgboxBox> implements BaseService<InsMsgboxBox> {

    @Resource
    private InsMsgboxBoxMapper insMsgboxBoxMapper;

    @Override
    public BaseDao<InsMsgboxBox> getRepository() {
        return insMsgboxBoxMapper;
    }

    public List<InsMsgboxBox> queryByBoxId(String boxId) {
        return insMsgboxBoxMapper.queryByBoxId(boxId);
    }

    public void delByBoxId(String boxId) {
        insMsgboxBoxMapper.delByBoxId(boxId);
    }
}
