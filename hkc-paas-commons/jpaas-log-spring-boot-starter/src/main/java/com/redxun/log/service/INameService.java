package com.redxun.log.service;

/**
 * 获取名字服务接口。
 */
public interface INameService {

    /**
     * 根据PK获取名称。
     * @param pk
     * @return
     */
    default String getName(String pk){
        return pk;
    }
}
