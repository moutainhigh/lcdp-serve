package com.redxun.util.wechat;

import com.redxun.cache.CacheUtil;
import com.redxun.dto.bpm.BpmConst;

import java.util.Map;

/**
 * 微信配置缓存。
 */
public class WeChatConfigCacheUtil {

    public static final String DEFAULT_SYSTEM_KEY = "default_wechat_key_";

    private static String getKey(String systemId) {
        return DEFAULT_SYSTEM_KEY + systemId;
    }

    /**
     * 获取缓存。
     *
     * @param systemId
     * @return
     */
    public static Map<String, WeChatTokenModel> getCache(String systemId) {
        String key = getKey(systemId);
        return (Map<String, WeChatTokenModel>) CacheUtil.get(BpmConst.CACHE_REGION, key);
    }

    /**
     * 删除
     *
     * @param systemId
     */
    public static void remove(String systemId) {
        String key = getKey(systemId);
        CacheUtil.remove(BpmConst.CACHE_REGION, key);
    }

    /**
     * 添加缓存。
     *
     * @param systemId
     * @param configMap
     */
    public static void add(String systemId, Map<String, WeChatTokenModel> configMap) {
        String key = getKey(systemId);
        CacheUtil.set(BpmConst.CACHE_REGION, key, configMap);
    }
}
