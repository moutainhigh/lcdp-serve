package com.redxun.cache.impl;

import com.redxun.cache.ICache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存缓存实现
 */
public class MemoryImpl implements ICache {

    private static Map<String,Object> map=new ConcurrentHashMap<>();

    /**
     * 写入缓存
     * @param region    缓存类型
     * @param key       缓存key
     * @param obj       缓存数据
     */
    @Override
    public  void set(String region, String key, Object obj) {
        String localKey=getKey(region , key);
        map.put(localKey,obj);
    }

    /**
     * 根据区域与缓存Key获取有效的缓存值
     * @param region
     * @param key
     * @return
     */
    private String getKey(String region,String key){
        String localKey= region +"_" +key;
        return  localKey;
    }

    /**
     * 设置缓存
     * @param region    缓存类型
     * @param key       缓存key
     * @param obj       缓存数据
     * @param second    缓存时间
     */
    @Override
    public void set(String region, String key, Object obj, long second) {
        String localKey=getKey(region , key);
        map.put(localKey,obj);
    }

    /**
     * 获取缓存
     * @param region    缓存类型
     * @param key       缓存key
     * @return
     */
    @Override
    public Object get(String region, String key) {
        String localKey=getKey(region , key);
        return map.get(localKey);
    }
    /**
     * 删除缓存
     * @param region
     * @param key
     */
    @Override
    public void remove(String region, String key) {
        String localKey=getKey(region , key);
        map.remove(localKey);
    }

    /**
     * 缓存是否存在
     * @param region
     * @param key
     * @return
     */
    @Override
    public boolean isExist(String region, String key) {
        String localKey=getKey(region , key);
        return map.containsKey(localKey);
    }

    /**
     * 清空缓存区
     * @param region
     */
    @Override
    public void clear(String region) {
        for(Iterator<String> it=map.keySet().iterator();it.hasNext();) {
            String key=it.next();
            if(key.startsWith(region)){
                map.remove(key);
            }
        }
    }
}
