package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsInstType;
import com.redxun.user.org.mapper.OsInstTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 机构类型
 *
 * @author yjy
 * @date 2019-10-29 17:31:21
 */
@Slf4j
@Service
public class OsInstTypeServiceImpl extends SuperServiceImpl<OsInstTypeMapper, OsInstType>  implements BaseService<OsInstType> {

    @Resource
    private OsInstTypeMapper osInstTypeMapper;

    @Override
    public BaseDao<OsInstType> getRepository() {
        return osInstTypeMapper;
    }

    public boolean isExist(OsInstType ent) {
        QueryWrapper<OsInstType> queryWrapper = new QueryWrapper<OsInstType>();
        queryWrapper.and(w -> w.eq("TYPE_CODE_",ent.getTypeCode()).or().eq("TYPE_NAME_",ent.getTypeName()));
        if (StringUtils.isNotEmpty(ent.getTypeId())) {
            queryWrapper.ne("TYPE_ID_", ent.getTypeId());
        }
        int count = osInstTypeMapper.selectCount(queryWrapper);


        return count > 0;
    }


}
