
package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.user.org.entity.OsUserPlatform;
import com.redxun.user.org.mapper.OsUserPlatformMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [第三方平台登陆绑定]业务服务类
*/
@Service
public class OsUserPlatformServiceImpl extends SuperServiceImpl<OsUserPlatformMapper, OsUserPlatform> implements BaseService<OsUserPlatform> {

    @Resource
    private OsUserPlatformMapper osUserPlatformMapper;

    @Override
    public BaseDao<OsUserPlatform> getRepository() {
        return osUserPlatformMapper;
    }

    /**
     * 根据tenantId,openId、platformType查询绑定关系
     * @param tenantId
     * @param openId
     * @param platformType
     * @return
     */
    public OsUserPlatform getOsUserPlatform(String tenantId, String openId, Integer platformType){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.eq("OPEN_ID_",openId);
        queryWrapper.eq("PLATFORM_TYPE_",platformType);
        List<OsUserPlatform> list= osUserPlatformMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据userId、platformType查询绑定关系
     * @param userId
     * @param platformType
     * @return
     */
    public OsUserPlatform getOsUserPlatformByUserId(String userId, Integer platformType){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("USER_ID_",userId);
        queryWrapper.eq("PLATFORM_TYPE_",platformType);
        List<OsUserPlatform> list= osUserPlatformMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据userId查询绑定关系
     * @param userId
     * @return
     */
    public List<OsUserPlatform> getOsUserPlatformByUserId(String userId,String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("USER_ID_",userId);
        queryWrapper.eq("TENANT_ID_",tenantId);
        List<OsUserPlatform> list= osUserPlatformMapper.selectList(queryWrapper);
        return list;
    }
}
