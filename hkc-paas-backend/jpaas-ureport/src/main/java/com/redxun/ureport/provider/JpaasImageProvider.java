package com.redxun.ureport.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bstek.ureport.provider.image.ImageProvider;
import com.redxun.common.tool.StringUtils;
import com.redxun.feign.common.SystemClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.bstek.ureport.exception.ReportComputeException;

/**
 * @Description: 报表图片适配类
 * @Author: Elwin ZHANG
 * @Date: 2021/11/2 9:36
 **/
@Component
public class JpaasImageProvider implements ImageProvider, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private String baseWebPath;
    private static final   String FILE_ID_KEY= "fileId"  ;
    private static final   String FILE_ID_KEY2= "\"" +"fileId" + "\"" ;

    @Autowired
    SystemClient systemClient;

    @Override
    public InputStream getImage(String path) {
        if(StringUtils.isEmpty(path)){
            return  null;
        }
        try {
            //db:fieldId
            //来自表单字段, 示例：
            // [{"fileId":"1455364633169862657","fileName":"aaa.jpg","size":28900},{
            // "fileId":"1455364634042277889","fileName":"gree.jpg","size":168180}]
            if(path.indexOf(FILE_ID_KEY2)>0){
                String fileId=getFileIdId(path);
                String filePath=getPicPath(fileId);
                return new FileInputStream(filePath);
            }
        } catch (IOException e) {
            throw new ReportComputeException(e);
        }
        return  null;
    }
    /**
    * @Description:  从表单字段中取出第一个fileId的值
    * @param  json 字符串
    * @Author: Elwin ZHANG  @Date: 2021/11/2 16:37
    **/
    private String getFileIdId(String  json){
        if(StringUtils.isEmpty(json)){
            return  "";
        }
        //多个文件
        if(json.trim().startsWith("[")){
            try {
                JSONArray array = JSONObject.parseArray(json);
                JSONObject obj=(JSONObject)array.get(0);
                return obj.getString(FILE_ID_KEY);
            }catch (Exception e){
            }
        }
        if(json.trim().startsWith("{")){
            try {
                JSONObject obj =JSONObject.parseObject(json);
                return obj.getString(FILE_ID_KEY);
            }catch (Exception e){
            }
        }
        return "";
    }

    /**
    * @Description: 通过系统文件Id获取文件的路径，在文件系统存储的情况下才能获取到
    * @param fileId   文件Id  （Sysfile表的主键）
    * @return java.lang.String 文件存储路径
    * @Author: Elwin ZHANG  @Date: 2021/11/2 15:02
    **/
    private  String getPicPath(String fileId){
        String url="/system/core/sysFile/getFilePath";
        JSONObject params = new JSONObject();
        params.put("fileId", fileId);
        params.put("isScale",false);
        Object obj = systemClient.executeGetApi(url, params);
        if(obj==null){
            return "";
        }
        HashMap<String,Object> result = (HashMap<String,Object>) obj;
        String path=result.get("data").toString();
        return path;
    }

    @Override
    public boolean support(String path) {
        if(path.indexOf(FILE_ID_KEY2)>0){
            return  true;
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(applicationContext instanceof WebApplicationContext){
            WebApplicationContext context=(WebApplicationContext)applicationContext;
            baseWebPath=context.getServletContext().getRealPath("/");
        }
        this.applicationContext=applicationContext;
    }
}
