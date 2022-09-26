package com.redxun.system.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.mapper.SysInvokeScriptMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
* [脚本调用]业务服务类
*/
@Service
public class SysInvokeScriptServiceImpl extends SuperServiceImpl<SysInvokeScriptMapper, SysInvokeScript> implements BaseService<SysInvokeScript> {

    @Resource
    private SysInvokeScriptMapper sysInvokeScriptMapper;

    @Override
    public BaseDao<SysInvokeScript> getRepository() {
        return sysInvokeScriptMapper;
    }

    public boolean isExist(SysInvokeScript sysInvokeScript){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",sysInvokeScript.getAlias());
        if(StringUtils.isNotEmpty( sysInvokeScript.getId())){
            queryWrapper.ne("ID_",sysInvokeScript.getId());
        }
        int count=sysInvokeScriptMapper.selectCount(queryWrapper);
        return count>0;
    }

    public SysInvokeScript getByAlias(String alias) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",alias);
        return sysInvokeScriptMapper.selectOne(queryWrapper);
    }

    public void importInvokeScript(MultipartFile file, String treeId,String appId) {

        StringBuilder sb=new StringBuilder();

        sb.append("导入调用脚本:");

        JSONArray sysScriptArray  = readZipFile(file);
        for (Object obj:sysScriptArray) {
            JSONObject scriptObj = (JSONObject)obj;
            JSONObject sysScript = scriptObj.getJSONObject("sysInvokeScript");
            if(BeanUtil.isEmpty(sysScript)){
                continue;
            }

            String sysScriptStr = sysScript.toJSONString();
            SysInvokeScript sysNewInvokeScript = JSONObject.parseObject(sysScriptStr,SysInvokeScript.class);
            sysNewInvokeScript.setTreeId(treeId);
            sysNewInvokeScript.setAppId(appId);
            String id = sysNewInvokeScript.getId();
            SysInvokeScript oldScript = get(id);

            sb.append("("+id+"),");

            if(BeanUtil.isNotEmpty(oldScript)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldScript.getAppId())){
                    update(sysNewInvokeScript);
                }else{
                    sysNewInvokeScript.setId(IdGenerator.getIdStr());
                    insert(sysNewInvokeScript);
                }
            }
            else{
                insert(sysNewInvokeScript);
            }
            LogContext.put(Audit.DETAIL,sb);
        }

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
