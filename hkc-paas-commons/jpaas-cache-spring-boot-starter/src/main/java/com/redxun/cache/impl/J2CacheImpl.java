package com.redxun.cache.impl;


import com.redxun.cache.ICache;
import lombok.Setter;
import net.oschina.j2cache.CacheChannel;

/**
 * J2Cache的缓存实现
 */
@Setter
public class J2CacheImpl implements ICache {

    private CacheChannel cacheChannel;

    @Override
    public void set(String region, String key, Object obj) {
        cacheChannel.set(region,key,obj);
    }

    @Override
    public void set(String region, String key, Object obj, long second) {
        cacheChannel.set(region,key,obj,second);
    }

    @Override
    public Object get(String region, String key) {
        return cacheChannel.get(region,key).getValue();
    }

    @Override
    public void remove(String region, String key) {
        cacheChannel.evict(region,key);
    }

    @Override
    public boolean isExist(String region, String key) {
        return cacheChannel.exists(region,key);
    }

    @Override
    public void clear(String region) {
        cacheChannel.clear(region);
    }

}
