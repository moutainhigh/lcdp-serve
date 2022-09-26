package com.redxun.common.milvus.service.impl;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.http.HttpFactory;
import com.redxun.common.http.HttpRtnModel;
import com.redxun.common.http.IHttp;
import com.redxun.common.milvus.service.IImageService;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 以图搜图服务类接口实现类
 *
 * @author zfh
 */
@Service
public class ImageServiceImpl implements IImageService {

    /**
     * 保存图片
     * @param imagePath  图片路径
     * @return
     * @throws Exception
     */
    @Override
    public JsonResult saveImage(String imagePath) throws Exception {
        HttpFactory httpFactory= SpringUtil.getBean(HttpFactory.class);
        //使用HttpClient方式
        IHttp httpClient = httpFactory.getHttpByResfulTemplate();
        String url = SysPropertiesUtil.getString("saveImageUrl");
        Map<String,String> headerMap= new HashMap<>();
        Map<String,String> queryMap= new HashMap<>();
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        }catch (IOException e){
            throw e;
        }finally {
            inputStream.close();
        }

        // 加密

        String s = Base64.encode(data);;

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("file", s);
        HttpRtnModel htm = httpClient.postFromUrl(url, headerMap, queryMap, bodyMap);
        JSONObject jsonObject = JSONObject.parseObject(htm.getContent());
        if(jsonObject.getBoolean("success").booleanValue()){
            return JsonResult.Success().setData(jsonObject.getLong("id"));
        }else {
            return JsonResult.Fail();
        }

    }

    /**
     * 搜索图片
     * @param imagePath  图片路径
     * @return
     * @throws Exception
     */
    @Override
    public JsonResult searchImage(String imagePath) throws Exception {
        HttpFactory httpFactory= SpringUtil.getBean(HttpFactory.class);
        //使用HttpClient方式
        IHttp httpClient = httpFactory.getHttpByResfulTemplate();
        String url = SysPropertiesUtil.getString("searchImageUrl");
        Map<String,String> headerMap= new HashMap<>();
        Map<String,String> queryMap= new HashMap<>();
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        }catch (IOException e){
            throw e;
        }finally {
            inputStream.close();
        }

        // 加密
        String s = Base64.encode(data);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("file", s);
        HttpRtnModel htm = httpClient.postFromUrl(url, headerMap, queryMap, bodyMap);
        JSONObject jsonObject = JSONObject.parseObject(htm.getContent());

        if(jsonObject.getBoolean("success").booleanValue()){
            return JsonResult.Success().setData(jsonObject.getString("ids"));
        }else {
            return JsonResult.Fail();
        }

    }
}
