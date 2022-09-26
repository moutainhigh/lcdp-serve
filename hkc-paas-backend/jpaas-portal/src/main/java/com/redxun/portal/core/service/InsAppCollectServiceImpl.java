package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.FieldLogic;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ContextUtil;
import com.redxun.portal.core.entity.InsAppCollect;
import com.redxun.portal.core.mapper.InsAppCollectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* [ins_app_collect]业务服务类
*/
@Service
public class InsAppCollectServiceImpl extends SuperServiceImpl<InsAppCollectMapper, InsAppCollect> implements BaseService<InsAppCollect> {

    @Resource
    private InsAppCollectMapper insAppCollectMapper;

    @Override
    public BaseDao<InsAppCollect> getRepository() {
        return insAppCollectMapper;
    }

    /**
     * 获取当前人的常用应用
     * @return
     */
    public List<InsAppCollect> getCommonApp() {
        String curUserId = ContextUtil.getCurrentUserId();
        return insAppCollectMapper.getCommonApp(curUserId);
    }
}
