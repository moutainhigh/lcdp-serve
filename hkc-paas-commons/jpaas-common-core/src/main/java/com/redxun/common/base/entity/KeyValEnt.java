package com.redxun.common.base.entity;

import lombok.Data;

/**
 * 键值对对象
 * @param <T>
 */
@Data
public class KeyValEnt<T> {
    /**
     * 键
     */
    private String key="";
    /**
     * 值
     */
    private T val=null;

    public KeyValEnt(){}

    public KeyValEnt(String key,T val){
        this.key=key;
        this.val=val;
    }

}
