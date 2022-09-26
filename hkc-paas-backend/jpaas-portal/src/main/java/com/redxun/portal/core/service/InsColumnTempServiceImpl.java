package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.portal.core.entity.InsColumnTemp;
import com.redxun.portal.core.mapper.InsColumnTempMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [栏目模板]业务服务类
*/
@Service
public class InsColumnTempServiceImpl extends SuperServiceImpl<InsColumnTempMapper, InsColumnTemp> implements BaseService<InsColumnTemp> {

    @Resource
    private InsColumnTempMapper insColumnTempMapper;

    @Override
    public BaseDao<InsColumnTemp> getRepository() {
        return insColumnTempMapper;
    }

    public boolean isExist(InsColumnTemp ent){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty(ent.getId())){
            wrapper.ne("ID_",ent.getId());
        }
        Integer rtn= insColumnTempMapper.selectCount(wrapper);
        return  rtn>0;
    }

    /**
     * 获取栏目模板
     * @param tenantId
     * @return
     */
    public List<InsColumnTemp> getColumnTempList(String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in ("TENANT_ID_" ,new String[]{"0",tenantId});
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        List list = insColumnTempMapper.selectList(queryWrapper);
        return list;
    }
}
