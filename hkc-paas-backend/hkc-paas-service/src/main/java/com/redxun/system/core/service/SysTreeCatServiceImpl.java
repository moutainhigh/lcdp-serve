package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysTreeCat;
import com.redxun.system.core.mapper.SysTreeCatMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SysTreeCat 系统分类服务类
 */
@Service
public class SysTreeCatServiceImpl extends SuperServiceImpl<SysTreeCatMapper, SysTreeCat>  implements BaseService<SysTreeCat> {

    @Resource
    private SysTreeCatMapper sysTreeCatMapper;

    @Override
    public BaseDao<SysTreeCat> getRepository() {
        return sysTreeCatMapper;
    }

    public SysTreeCat getByKey(String key){
        return sysTreeCatMapper.getByKey(key);
    }


    public boolean isExist(SysTreeCat ent){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty(ent.getCatId())){
            wrapper.ne("cat_id_",ent.getCatId());
        }
        Integer rtn= sysTreeCatMapper.selectCount(wrapper);
        return  rtn>0;
    }
}
