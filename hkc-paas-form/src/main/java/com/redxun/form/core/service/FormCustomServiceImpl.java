package com.redxun.form.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.FormCustom;
import com.redxun.form.core.mapper.FormCustomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [表单定制]业务服务类
*/
@Service
public class FormCustomServiceImpl extends SuperServiceImpl<FormCustomMapper, FormCustom> implements BaseService<FormCustom> {

    @Resource
    private FormCustomMapper formCustomMapper;

    @Override
    public BaseDao<FormCustom> getRepository() {
        return formCustomMapper;
    }

    public FormCustom getByAlias(String alias) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",alias);
        return formCustomMapper.selectOne(queryWrapper);
    }

    public boolean isExist(FormCustom ent) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",ent.getAlias());
        if(StringUtils.isNotEmpty( ent.getId())){
            queryWrapper.ne("ID_",ent.getId());
        }
        int count=formCustomMapper.selectCount(queryWrapper);

        return count>0;
    }
}
