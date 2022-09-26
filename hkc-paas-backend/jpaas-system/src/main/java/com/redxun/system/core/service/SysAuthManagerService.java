package com.redxun.system.core.service;

import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysAuthManager;
import com.redxun.system.core.mapper.SysAuthManagerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [应用接口授权管理表]业务服务类
*/
@Service
public class SysAuthManagerService extends SuperServiceImpl<SysAuthManagerMapper, SysAuthManager> implements BaseService<SysAuthManager> {

    private final static String CLIENT_PRE="client_";

    public final static String REGIN_API="api";

    @Resource
    private SysAuthManagerMapper sysAuthManagerMapper;

    @Override
    public BaseDao<SysAuthManager> getRepository() {
        return sysAuthManagerMapper;
    }

    /**
     * 获取缓存KEY
     * @param clientId
     * @return
     */
    public  String getCacheKey(String clientId){
        return CLIENT_PRE +clientId;
    }

    /**
     * 清除客户端缓存。
     * @param clientId
     */
    public void cleanCache(String clientId){
        String key=getCacheKey(clientId);
        CacheUtil.remove(REGIN_API,key);
    }


}
