package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.core.entity.InsColumnDef;
import com.redxun.portal.core.mapper.InsColumnDefMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * [栏目定义]业务服务类
 */
@Service
public class InsColumnDefServiceImpl extends SuperServiceImpl<InsColumnDefMapper, InsColumnDef> implements BaseService<InsColumnDef> {

    @Resource
    private InsColumnDefMapper insColumnDefMapper;

    @Override
    public BaseDao<InsColumnDef> getRepository() {
        return insColumnDefMapper;
    }

    public List<InsColumnDef> queryByIsNews(String isNews) {
        return insColumnDefMapper.queryByIsNews(isNews);
    }

    public boolean isExist(InsColumnDef ent){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty(ent.getColId())){
            wrapper.ne("COL_ID_",ent.getColId());
        }
        Integer rtn= insColumnDefMapper.selectCount(wrapper);
        return  rtn>0;
    }
}
