package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.mapper.OsDimensionMapper;
import com.redxun.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户组维度Service业务层处理
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@Service
public class OsDimensionServiceImpl extends SuperServiceImpl<OsDimensionMapper, OsDimension>  implements BaseService<OsDimension> {

    @Resource
    private OsDimensionMapper osDimensionMapper;

    @Resource
    private OsGroupServiceImpl osGroupServiceImpl;

    @Override
    public BaseDao<OsDimension> getRepository() {
        return osDimensionMapper;
    }

    /**
     * 判断用户组维度是否存在
     * @param ent
     * @return
     */
    public boolean isExist(OsDimension ent) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("CODE_",ent.getCode());
        wrapper.eq(CommonConstant.TENANT_ID,ent.getTenantId());
        if(StringUtils.isNotEmpty(ent.getDimId())){
            wrapper.ne("DIM_ID_",ent.getDimId());
        }
        int count=osDimensionMapper.selectCount(wrapper);

        return count>0;
    }

    public List<OsDimension> findList(ContextQuerySupport support){

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("STATUS_","ENABLED");

        WebUtil.handContextWrapper(support,queryWrapper);

        queryWrapper.orderByAsc("SN_");
        return osDimensionMapper.selectList(queryWrapper);
    }

    /**
     * 根据维度Id数组获取所有的维度列表
     * @param dimIds
     * @return
     */
    public List<OsDimension> getByDimIds(List<String> dimIds) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("STATUS_","ENABLED");
        queryWrapper.in("DIM_ID_",dimIds);
        queryWrapper.orderByAsc("SN_");
        return osDimensionMapper.selectList(queryWrapper);
    }

    /**
     * 根据编码与租户ID获取维度
     * @param code
     * @param tenantId
     * @return
     */
    public OsDimension getByCodeTenantId(String code,String tenantId){
        return osDimensionMapper.getByCodeTenantId(code,tenantId);
    }

    /**
     * 获取某个租户下的所有用户组维度
     * @param tenantId
     * @return
     */
    public List<OsDimension> getTenantDims(@Param("tenantId") String tenantId){
        return osDimensionMapper.getTenantDims(tenantId);
    }

}
