package com.redxun.ureport.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.dto.form.AlterSql;
import com.redxun.feign.common.SystemClient;
import com.redxun.ureport.core.entity.UreportFile;
import com.redxun.ureport.core.mapper.UreportFileMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* [ureport_file]业务服务类
*/
@Service
public class UreportFileServiceImpl extends SuperServiceImpl<UreportFileMapper, UreportFile> implements BaseService<UreportFile> {

    @Resource
    private UreportFileMapper ureportFileMapper;

    @Resource
    private SystemClient systemClient;


    @Override
    public BaseDao<UreportFile> getRepository() {
        return ureportFileMapper;
    }

    public UreportFile getByFileName(String name){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("NAME_",name);
        return ureportFileMapper.selectOne(wrapper);
    }

    /**
    * @Description:  查找系统树所属应用ID
    * @param treeId sys_tree表的主键值
    * @return java.lang.String  sys_tree表记录中APP_ID_字段的值
    * @Author: Elwin ZHANG  @Date: 2021/7/6 17:16
    **/
    public String getAppIdByTreeId(String treeId){
        try {
            String url = "/system/core/sysTree/get";
            JSONObject params = new JSONObject();
            params.put("pkId", treeId);
            Object o = systemClient.executeGetApi(url, params);
            HashMap<String,Object> result = (HashMap<String,Object>) o;
            HashMap<String,Object> data=(HashMap<String,Object>)result.get("data");
            String appId = data.get("appId").toString();
            return appId;
        }catch (Exception e ){
            return "";
        }
    }

    public List<AlterSql> importUreportZip(MultipartFile file, String treeId) {
        List<AlterSql> delaySqlList = new ArrayList<>();
        JSONArray uReportArray  = readZipFile(file);
        String appId=getAppIdByTreeId(treeId);
        for (Object obj:uReportArray) {
            JSONObject uReObj = (JSONObject)obj;
            JSONObject uReport = uReObj.getJSONObject("uReport");
            if(BeanUtil.isNotEmpty(uReport)){
                String uReportStr = uReport.toJSONString();
                UreportFile uReportFile = JSONObject.parseObject(uReportStr,UreportFile.class);
                uReportFile.setCategoryId(treeId);
                uReportFile.setAppId(appId);
                String id = uReportFile.getId();
                UreportFile oldUre = get(id);
                if(BeanUtil.isNotEmpty(oldUre)) {
                    update(uReportFile);
                }
                else{
                    insert(uReportFile);
                }
            }
        }
        return delaySqlList;
    }

    public JSONObject doExportById(String id) {
        JSONObject json = new JSONObject();
        UreportFile uReportFile = get(id);
        json.put("uReport", uReportFile);
        return json;
    }

    public JSONArray readZipFile(MultipartFile file){
        JSONArray formSultionArry = new JSONArray();
        try{
            InputStream is = file.getInputStream();
            // 转化为Zip的输入流
            ZipArchiveInputStream zipIs = new ZipArchiveInputStream(is, "UTF-8");
            while ((zipIs.getNextZipEntry()) != null) {// 读取Zip中的每个文件
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(zipIs, baos);
                String sulotionStr = baos.toString("UTF-8");
                JSONObject sulotionObj = JSON.parseObject(sulotionStr);
                formSultionArry.add(sulotionObj);
            }
            zipIs.close();
        }catch (Exception e){
            log.error("---FormExOrImportHandler.readZipFile is error =="+e.getMessage());
        }
        return formSultionArry;
    }

}
