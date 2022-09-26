package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsRankType;
import com.redxun.user.org.mapper.OsRankTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户组等级分类
 *
 * @author yjy
 * @date 2019-10-29 17:31:18
 */
@Slf4j
@Service
public class OsRankTypeServiceImpl extends SuperServiceImpl<OsRankTypeMapper, OsRankType>  implements BaseService<OsRankType> {

    @Resource
    private OsRankTypeMapper osRankTypeMapper;

    @Override
    public BaseDao<OsRankType> getRepository() {
        return osRankTypeMapper;
    }

    public List<OsRankType> getByDimId(String dimId,String tenantId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DIM_ID_",dimId);
        queryWrapper.eq(CommonConstant.TENANT_ID,tenantId);
        queryWrapper.orderByAsc("LEVEL_");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        return osRankTypeMapper.selectList(queryWrapper);
    }

    public boolean isExist(OsRankType ent) {
        Map<String,Object> params=new HashMap<>();
        params.put("KEY_",ent.getKey());
        params.put(CommonConstant.TENANT_ID,ent.getTenantId());
        params.put("DIM_ID_",ent.getDimId());
        if(StringUtils.isNotEmpty(ent.getRkId())){
            params.put("RK_ID_",ent.getRkId());
        }
        int count=osRankTypeMapper.isExist(params);

        return count>0;
    }
}
