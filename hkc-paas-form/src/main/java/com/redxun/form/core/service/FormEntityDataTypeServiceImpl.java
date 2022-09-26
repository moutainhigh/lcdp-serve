
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.FormEntityDataType;
import com.redxun.form.core.mapper.FormEntityDataTypeMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [业务实体数据类型]业务服务类
*/
@Service
public class FormEntityDataTypeServiceImpl extends SuperServiceImpl<FormEntityDataTypeMapper, FormEntityDataType> implements BaseService<FormEntityDataType> {

    @Resource
    private FormEntityDataTypeMapper formEntityDataTypeMapper;

    @Override
    public BaseDao<FormEntityDataType> getRepository() {
        return formEntityDataTypeMapper;
    }

    /**
     * 判断是否存在类型名称
     * @param ent
     * @return
     */
    public boolean isExist(FormEntityDataType ent) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("NAME_",ent.getName());
        if(StringUtils.isNotEmpty(ent.getId())){
            wrapper.ne("ID_",ent.getId());
        }
        Integer rtn= formEntityDataTypeMapper.selectCount(wrapper);
        return  rtn>0;
    }
}
