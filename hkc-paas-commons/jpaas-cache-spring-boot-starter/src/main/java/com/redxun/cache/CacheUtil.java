package com.redxun.cache;


import com.redxun.common.utils.SpringUtil;

/**
 * 缓存工具类。
 */
public class CacheUtil {

    public static ICache getCache(){
        return SpringUtil.getBean(ICache.class);
    }

    /**
     * 设置缓存
     * @param region
     * @param key
     * @param obj
     */
    public static void set(String region,String key,Object obj){
        ICache cache=getCache();
        cache.set(region,key,obj);
    }

    /**
     * 根据键获取数据。
     * @param region
     * @param key
     * @return
     */
    public static Object get(String region,String key){
        ICache cache=getCache();
        return cache.get(region,key);
    }

    /**
     * 根据键删除。
     * @param region
     * @param key
     */
    public static void remove(String region,String key){
        ICache cache=getCache();
        cache.remove(region,key);
    }

    /**
     * 删除区域
     * @param region
     */
    public static void clear(String region){
        ICache cache=getCache();
        cache.clear(region);
    }

    /**
     * 判断键是否存在。
     * @param region
     * @param key
     * @return
     */
    public static boolean isExist(String region,String key){
        ICache cache=getCache();
        return  cache.isExist(region,key);
    }
}
