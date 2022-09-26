
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.system.core.entity.*;
import com.redxun.system.core.mapper.SysInterfaceApiMapper;
import com.redxun.system.util.SysInterfaceApiUtil;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [接口API表]业务服务类
 */
@Service
public class SysInterfaceApiServiceImpl extends SuperServiceImpl<SysInterfaceApiMapper, SysInterfaceApi> implements BaseService<SysInterfaceApi> {

    @Resource
    private SysInterfaceApiMapper sysInterfaceApiMapper;
    @Resource
    SysHttpTaskLogServiceImpl sysHttpTaskLogService;
    @Resource
    private SysInterfaceClassificationServiceImpl sysInterfaceClassificationService;
    @Resource
    private SysInterfaceProjectServiceImpl sysInterfaceProjectService;

    @Override
    public BaseDao<SysInterfaceApi> getRepository() {
        return sysInterfaceApiMapper;
    }

    /**
     * 根据项目ID查询接口集合
     * @param projectId 项目ID
     * @return
     */
    public List<SysInterfaceApi> getByProjectId(String projectId,String status) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PROJECT_ID_",projectId);
        if(StringUtils.isNotEmpty(status)){
            queryWrapper.eq("STATUS_",status);
        }
        return sysInterfaceApiMapper.selectList(queryWrapper);
    }

    /**
     * 根据分类ID删除接口
     * @param classificationId
     */
    public void deleteByClassificationId(String classificationId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("CLASSIFICATION_ID_",classificationId);
        sysInterfaceApiMapper.delete(queryWrapper);
    }

    /**
     * 根据分类ID查询接口集合
     * @param classificationId 分类ID
     * @param status
     * @return
     */
    public List<SysInterfaceApi> getByClassificationId(String classificationId,String status){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("CLASSIFICATION_ID_",classificationId);
        if(StringUtils.isNotEmpty(status)){
            queryWrapper.eq("STATUS_",status);
        }
        return sysInterfaceApiMapper.selectList(queryWrapper);
    }

    /**
     * 测试接口
     * @param apiId
     * @return
     */
    public JsonResult executeApiTest(String apiId) throws Exception{
        return SysInterfaceApiUtil.executeApi(apiId,new HashMap<>(),new HashMap<>(),new HashMap<>(),new JSONObject(),true,null,null);
    }

    @Recover
    public JsonResult recover(Exception e,String apiId, String params,String batId) {
        String message=e.getMessage();
        SysHttpTaskLog log=new SysHttpTaskLog();
        log.setResponseState("500");
        log.setResult("0");
        log.setTaskId(batId);
        if(StringUtils.isNotEmpty(message)){
            JSONObject json=JSONObject.parseObject(message);
            log=JSONObject.toJavaObject(json,SysHttpTaskLog.class);
        }
        JsonResult result=new JsonResult();
        result.setSuccess(false);
        result.setCode(Integer.parseInt(log.getResponseState()));
        result.setMessage(log.getErrorMessage());

        SysInterfaceApi sysInterfaceApi=get(apiId);
        if(MBoolean.YES.name().equals(sysInterfaceApi.getIsLog())) {
            SysInterfaceProject sysInterfaceProject = sysInterfaceProjectService.get(sysInterfaceApi.getProjectId());
            sysHttpTaskLogService.createLog(batId, SysHttpTask.TYPE_INTERFACE, apiId,
                    sysInterfaceProject.getProjectName() + "/" + sysInterfaceApi.getApiName(),
                    "sysInterfaceApiServiceImpl", "executeApi", log, false, apiId, params);
        }
        return result;
    }

    /**
     * 执行接口
     * @param apiId
     * @param params
     * @param batId
     * @return
     */
    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts:3}",
            backoff = @Backoff(delayExpression = "${retry.delay:2000}",
                    multiplierExpression = "${retry.multiplier:1.5}"))
    public JsonResult executeApi(String apiId, String params,String batId) throws Exception {
        Map<String,Object> pathParams=new HashMap<>();
        Map<String,Object> headers=new HashMap<>();
        Map<String,Object> querys=new HashMap<>();
        Object bodys=new JSONObject();
        try {
            if (StringUtils.isNotEmpty(params)) {
                JSONObject paramObj=JSONObject.parseObject(params);
                if(paramObj.containsKey("pathParams")){
                    pathParams=paramObj.getJSONObject("pathParams").getInnerMap();
                }
                if(paramObj.containsKey("headers")){
                    headers=paramObj.getJSONObject("headers").getInnerMap();
                }
                if(paramObj.containsKey("querys")){
                    querys=paramObj.getJSONObject("querys").getInnerMap();
                }
                if(paramObj.containsKey("bodys") && paramObj.get("bodys")!=null){
                    bodys=paramObj.get("bodys");
                }
            }
        }catch (Exception e){
            log.error(ExceptionUtil.getExceptionMessage(e));
        }
        return SysInterfaceApiUtil.executeApi(apiId,pathParams,headers,querys,bodys,false,batId,params);
    }

    public boolean isExist(SysInterfaceApi sysInterfaceApi) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("API_NAME_",sysInterfaceApi.getApiName());
        if(StringUtils.isNotEmpty(sysInterfaceApi.getApiId()) ){
            wrapper.ne("API_ID_",sysInterfaceApi.getApiId());
        }
        int count=this.sysInterfaceApiMapper.selectCount(wrapper);
        return  count>0;
    }

    public JSONArray doExportById(String id,String type) {
        //查询所有
        if("ALL".equals(type)){
            type=null;
        }
        List<SysInterfaceApi> list=getByClassificationId(id,type);
        return JSONArray.parseArray(JSONArray.toJSONString(list));
    }

    public String importSysInterfaceZip(MultipartFile file, String classId,String type) {
        //成功导入接口数
        int successNum=0;
        //已存在接口数
        int existNum=0;
        JSONArray sysInterfaceArray  = readZipFile(file);
        for (Object obj:sysInterfaceArray) {
            JSONArray interfaceAry = (JSONArray)obj;
            for(Object interfaceObj:interfaceAry){
                JSONObject interfaceJson=(JSONObject) interfaceObj;
                String sysInterfaceStr = interfaceJson.toJSONString();
                SysInterfaceApi sysNewInterface = JSONObject.parseObject(sysInterfaceStr,SysInterfaceApi.class);
                //设置新分类
                SysInterfaceClassification sysInterfaceClassification=sysInterfaceClassificationService.get(classId);
                sysNewInterface.setClassificationId(classId);
                sysNewInterface.setProjectId(sysInterfaceClassification.getProjectId());
                String id = sysNewInterface.getApiId();
                //获取已存在的接口
                SysInterfaceApi oldInterface = get(id);
                if("COMMON".equals(type)){
                    //普通模式:不导入已存在的接口
                    if(oldInterface==null){
                        insert(sysNewInterface);
                        successNum+=1;
                    }
                }else if("MERGE".equals(type)){
                    //完全覆盖:完全使用新的接口配置
                    if(oldInterface!=null){
                        update(sysNewInterface);
                    }else{
                        insert(sysNewInterface);
                    }
                    successNum+=1;
                }
                if(oldInterface!=null){
                    existNum+=1;
                }
            }
        }
        return "成功导入接口"+successNum+"个，已存在接口"+existNum+"个";
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
                JSONArray sulotionObj = JSON.parseArray(sulotionStr);
                formSultionArry.add(sulotionObj);
            }
            zipIs.close();
        }catch (Exception e){
            log.error("---SysInterfaceApiServiceImpl.readZipFile is error =="+e.getMessage());
        }
        return formSultionArry;
    }

    public void deleteByProjectId(String projectId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PROJECT_ID_",projectId);
        sysInterfaceApiMapper.delete(queryWrapper);
    }
}
