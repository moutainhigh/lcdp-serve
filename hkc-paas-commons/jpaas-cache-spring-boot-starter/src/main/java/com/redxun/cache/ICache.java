package com.redxun.cache;

/**
 * 缓存接口。
 */
public interface ICache {

    /**
     * 设置缓存
     * @param region    缓存类型
     * @param key       缓存key
     * @param obj       缓存数据
     */
    void set(String region, String key, Object obj);

    /**
     * 设置缓存 （这个方法一般不用)
     * @param region    缓存类型
     * @param key       缓存key
     * @param obj       缓存数据
     * @param second    缓存时间
     */
    void set(String region, String key, Object obj, long second);

    /**
     * 获取缓存数据。
     * @param region    缓存类型
     * @param key       缓存key
     * @return
     */
    Object get(String region, String key);

    /**
     * 删除缓存数据。
     * @param region
     * @param key
     */
    void remove(String region, String key);

    /**
     * 判断缓存是否存在。
     * @param region
     * @param key
     * @return
     */
    boolean isExist(String region, String key);

    /**
     * 根据缓存类型清除缓存。
     * @param region
     */
    void  clear(String region);
}
