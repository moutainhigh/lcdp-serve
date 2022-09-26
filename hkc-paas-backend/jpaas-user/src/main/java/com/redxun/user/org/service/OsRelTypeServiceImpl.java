package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsRelType;
import com.redxun.user.org.mapper.OsRelTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 关系类型定义Service业务层处理
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@Service
public class OsRelTypeServiceImpl extends SuperServiceImpl<OsRelTypeMapper, OsRelType>  implements BaseService<OsRelType> {

    @Resource
    private OsRelTypeMapper osRelTypeMapper;

    @Override
    public BaseDao<OsRelType> getRepository() {
        return osRelTypeMapper;
    }

    public List<OsRelType> getOsRelTypeOfRelType(String tenantId,String dimId, String relType){
        return osRelTypeMapper.getOsRelTypeOfRelType(tenantId,dimId,relType);
    }

    public List<OsRelType> getOsRelTypeOfRelType(String tenantId,String dimId, String relType,Integer level){
        //逻辑删除
        String deleted =null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        return osRelTypeMapper.getOsRelTypeOfRelTypeLevel(tenantId,dimId,relType,level,deleted);
    }

    /**
     * 根据租户和别名获取关系类型。
     * @param tenantId
     * @param relTypeKey
     * @return
     */
    public OsRelType getByKey(String tenantId,String relTypeKey) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(CommonConstant.TENANT_ID,new String[]{tenantId,"0"});
        queryWrapper.eq("KEY_",relTypeKey);
        return osRelTypeMapper.selectOne(queryWrapper);
    }

    public List<OsRelType> getUserRelType(String tenantId) {
        return getOsRelTypeOfRelType(tenantId,"",OsRelType.REL_TYPE_USER_USER);
    }

    public List<OsRelType> getGroupRelType(String tenantId, String dimId) {
        return getOsRelTypeOfRelType(tenantId,dimId,OsRelType.REL_TYPE_GROUP_GROUP);
    }

    /**
     * 判断关系类型是否存在。
     * @param relTypeKey
     * @param tenantId
     * @param typeId
     * @return
     */
    public boolean isRelTypeExist(String relTypeKey,String tenantId,String typeId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in(CommonConstant.TENANT_ID,new String[]{tenantId,"0"});
        queryWrapper.eq("KEY_",relTypeKey);

        if(StringUtils.isNotEmpty(typeId)){
            queryWrapper.ne("ID_",typeId);
        }

        int count= osRelTypeMapper.selectCount(queryWrapper);

        return count>0;
    }

    public List<OsRelType> getGroupRelsBydimIdAndRelType(String tenantId, String dimId,String relType) {
        return getOsRelTypeOfRelType(tenantId,dimId,relType,-1);
    }
}
