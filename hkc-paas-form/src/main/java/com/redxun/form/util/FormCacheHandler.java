package com.redxun.form.util;

import com.redxun.cache.CacheUtil;

/**
 *
 */
public class FormCacheHandler {
    /**
     *
     */
    public static final String FORMALIAS_REGION="formAlias";


    /**
     * 根据表单别名获取BoAlias。
     * @param formAlias
     * @return
     */
    public static String getBoAliasKey(String formAlias){
        return "alias_" +formAlias;
    }



    /**
     * 根据表单别名清除对应bo别名缓存。
     * @param formAlias
     */
    public static void clearForm(String formAlias){

        String key=getBoAliasKey(formAlias);
        CacheUtil.remove(FORMALIAS_REGION,key);
    }



}
