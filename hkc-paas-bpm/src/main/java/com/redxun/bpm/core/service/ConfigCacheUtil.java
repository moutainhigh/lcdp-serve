package com.redxun.bpm.core.service;

import com.redxun.bpm.activiti.config.NodeConfig;
import com.redxun.cache.CacheUtil;
import com.redxun.dto.bpm.BpmConst;

import java.util.Map;

/**
 * 流程配置缓存。
 */
public class ConfigCacheUtil {

    private static String getKey(String actDefId){
        return  BpmConst.TYPE_CONFIG + actDefId;
    }

    /**
     * 获取缓存。
     * @param actDefId
     * @return
     */
    public static Map<String, NodeConfig> getCache(String actDefId){
        String key=getKey(actDefId);
        return (Map<String, NodeConfig>)CacheUtil.get(BpmConst.CACHE_REGION,key);
    }

    /**
     * 删除
     * @param actDefId
     */
    public static void remove(String actDefId){
        String key=getKey(actDefId);
        CacheUtil.remove(BpmConst.CACHE_REGION,key);
    }

    /**
     * 添加缓存。
     * @param actDefId
     * @param configMap
     */
    public static void add(String actDefId,Map<String, NodeConfig> configMap){
        String key=getKey(actDefId);
        CacheUtil.set(BpmConst.CACHE_REGION,key,configMap);
    }
}
