package com.redxun.common.milvus.service;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 以图搜图服务接口
 * @author zfh
 */
public interface IImageService {


    /**
     * 将图片转成文件流，通过http发送到python服务器，然后利用VGG16提取特征，
     * 再将特征向量保存到milvus向量搜索引擎，最后，返回Milvus自动为该向量分配的ID
     * @param imagePath  图片路径
     * @return  Milvus自动为该向量分配的ID【1629439272605448000】
     */
    JsonResult saveImage(String imagePath) throws Exception;


    /**
     * 将图片转成文件流，通过http发送到python服务器，然后利用VGG16提取特征，
     * 再通过特征向量向milvus发起搜索，最后，将搜索出相关向量的IDS返回，
     * 在IDS中，相似度较高的ID排在前面
     * @param imagePath  图片路径
     * @return     索出相关向量的IDS【1629428842845673000,1629428530955267000,1629428743565005000,1629428876884364000,1629428526312984000,
     *              1629423404693810000,1629423281047056000,1629423257408373000,1629428849027596000,1629439272605448000】
     */
    JsonResult searchImage(String imagePath) throws Exception;

}