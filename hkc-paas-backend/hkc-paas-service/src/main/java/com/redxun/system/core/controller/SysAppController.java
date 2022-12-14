package com.redxun.system.core.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppActionLog;
import com.redxun.system.core.entity.SysAppFavorites;
import com.redxun.system.core.service.SysAppFavoritesServiceImpl;
import com.redxun.system.core.service.SysAppManagerServiceImpl;
import com.redxun.system.core.service.SysAppServiceImpl;
import com.redxun.system.core.service.SysInvokeScriptServiceImpl;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/system/core/sysApp")
@ClassDefine(title = "????????????",alias = "sysAppController",path = "/system/core/sysApp",packages = "core",packageName = "????????????")
@Api(tags = "????????????")
public class SysAppController extends BaseController<SysApp> {

    @Autowired
    SysAppServiceImpl sysAppServiceImpl;
    @Autowired
    SystemClient systemClient;
    @Autowired
    SysInvokeScriptServiceImpl sysInvokeScriptServiceImpl;
    @Resource
    SysAppFavoritesServiceImpl sysAppFavoritesService;
    @Resource
    private SysAppManagerServiceImpl sysAppManagerService;

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","APP","read");
        super.handleFilter(filter);
    }

    @Override
    public BaseService getBaseService() {
        return sysAppServiceImpl;
    }

    @Override
    public String getComment() {
        return "??????";
    }

    //??????????????????????????????????????????????????????
    @MethodDefine(title = "????????????????????????", path = "/save", method = HttpMethodConstants.POST, params = {@ParamDefine(title = "????????????JSON", varName = "entity")})
    @ApiOperation(value="????????????????????????", notes="?????????????????????JSON??????????????????????????????")
    @AuditLog(operation = "????????????????????????")
    @PostMapping("/save")
    @Override
    public JsonResult save(@Validated @ApiParam @RequestBody SysApp entity, BindingResult validResult) throws Exception{
        boolean needAddManager=false; //?????????????????????
        if(StringUtils.isEmpty(entity.getAppId()) && entity.getAppType()==1){
            needAddManager=true;
        }
        JsonResult result=super.save(entity,validResult);
        //?????????????????????????????????
        if(needAddManager && result.isSuccess()){
            sysAppManagerService.AddManager4CurUser(entity.getAppId());
        }
        return result;
    }
    @Override
    protected JsonResult beforeSave(SysApp ent) {
        boolean isExist= sysAppServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("?????????????????????!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "????????????????????????", path = "/getAllApps", method = HttpMethodConstants.GET)
    @ApiOperation("????????????????????????")
    @GetMapping("/getAllApps")
    public List<SysAppDto> getAllApps(){
        List<SysApp> list = sysAppServiceImpl.getAllApps();
        List<SysAppDto> sysAppDtos = BeanUtil.copyList(list,SysAppDto.class);
        decorateAppList(sysAppDtos);
        return sysAppDtos;
    }

    @MethodDefine(title = "????????????????????????", path = "/getAll", method = HttpMethodConstants.GET)
    @ApiOperation("????????????????????????")
    @GetMapping("/getAll")
    @Override
    public JsonPageResult getAll(){
        //????????????sn
        List data= sysAppServiceImpl.getAllApps();
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }


    @MethodDefine(title = "????????????ID???????????????????????????ID????????????????????????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title ="??????ID",varName = "appId")})
    @ApiOperation("????????????ID???????????????????????????ID????????????????????????????????????")
    @GetMapping("/getById")
    public JsonPageResult getById( @ApiParam @RequestParam (value = "appId") String appId) {
        String newAppId=appId;
        if(sysAppServiceImpl.isFixedApp(appId)){
            newAppId="";
        }
        List<SysApp> data = sysAppServiceImpl.getById(newAppId);
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getApps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title ="??????ID",varName = "appId")})
    @ApiOperation("??????????????????????????????")
    @GetMapping("/getApps")
    public List<SysAppDto> getApps( @ApiParam @RequestParam (value = "appId") String appId){
        String tenantId=ContextUtil.getCurrentTenantId();
        String newAppId=appId;
        if(sysAppServiceImpl.isFixedApp(appId)){
            newAppId="";
        }
        List<SysApp> list = sysAppServiceImpl.getApps(tenantId,newAppId);
        List<SysAppDto> sysAppDtos = BeanUtil.copyList(list,SysAppDto.class);
        decorateAppList(sysAppDtos);
        return sysAppDtos;
    }

    @MethodDefine(title = "??????ID????????????????????????", path = "/getAppsByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID??????", varName = "appIds")})
    @ApiOperation("??????ID????????????????????????")
    @PostMapping("/getAppsByIds")
    public List<SysAppDto> getAppsByIds(@ApiParam @RequestParam("appIds") String appIds){
        List<String> ids= new ArrayList<>();
        if (appIds.contains(",")){
            String[] split = appIds.split(",");
            for (String s : split) {
                ids.add("'"+s+"'");
            }
        }else {
            ids.add("'"+appIds+"'");
        }
        appIds=StringUtils.join(ids,",");
        List<SysApp> list = sysAppServiceImpl.getAppsByIds(appIds);
        List<SysAppDto> sysAppDtos = BeanUtil.copyList(list,SysAppDto.class);
        decorateAppList(sysAppDtos);
        return sysAppDtos;
    }
    /**
    *  ???????????????App????????????????????????????????????????????? ?????????
    * @param apps ????????????
    * @author  Elwin ZHANG
    * @date 2022/1/20 17:10
    **/
    private  void decorateAppList(List<SysAppDto> apps){
        //?????????????????????
        if(apps==null || apps.size()==0){
            return ;
        }
        //???????????????????????????ID??????
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return;
        }
        //???????????????????????????
        for (SysAppDto app :apps){
            SysAppFavorites fav=sysAppFavoritesService.getFavorite(userId,app.getAppId());
            if(fav==null){
                app.setIsFav(0);
            }else {
                app.setIsFav(fav.getIsFav());
                Date date=fav.getLastUseTime();
                if(date!=null){
                    String strDate=DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",date);
                    app.setLastUseTime(strDate);
                }
            }
        }
    }

    @MethodDefine(title = "????????????????????????", path = "/saveOrUpdate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "sysApp")})
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "????????????????????????")
    public JsonResult saveOrUpdate(@RequestBody SysApp sysApp) {
        return sysAppServiceImpl.saveOrUpdateSysApp(sysApp);
    }


    @MethodDefine(title = "????????????", path = "/delById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "id", varName = "id)")})
    @ApiOperation("????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("delById")
    public JsonResult delById(@RequestParam String id){

        if(StringUtils.isEmpty(id)){
            return new JsonResult(false,"???????????????");
        }
        sysAppServiceImpl.delete(id);
        sysAppServiceImpl.writeActionLog(id, SysAppActionLog.TYPE_DELETE,"");
        JsonResult result= JsonResult.getSuccessResult("????????????!");
        return result;
    }

    @MethodDefine(title = "????????????", path = "/doExport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "app")})
    @ApiOperation("????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("doExport")
    public void doExport(@RequestBody JSONObject app){
        try {
            String appId=app.getString("appId");
            String appName=app.getString("appName");
            boolean exportCustomTables=false;
            String  strExportCustomData=app.getString("exportCustomTables");
            if("true".equals(strExportCustomData) || "1".equals(strExportCustomData)){
                exportCustomTables=true;
            }
            if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appName)){
                log.error("?????????????????????????????????");
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            Map<String, String> map = sysAppServiceImpl.query(appId,exportCustomTables);
            LogContext.put(Audit.DETAIL, "??????" + map.size() +"??????");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String downFileName = appName + "_" + sdf.format(new Date());
            FileUtil.downloadZip(response, downFileName, map);
            sysAppServiceImpl.writeActionLog(appId, SysAppActionLog.TYPE_EXPORT,"");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @MethodDefine(title = "????????????", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "data") })
    @PostMapping("/doImport")
    @AuditLog(operation = "????????????")
    @ApiOperation("????????????")
    public JsonResult doImport(@ApiParam @RequestBody  HashMap<String,String> data  ){
        String fileName= data.get("fileName");
        String tenantId= data.get("tenantId");
        String treeId=data.get("treeId");
        String overrides= data.get("overrides");
        String key=fileName +"@" + ContextUtil.getCurrentUser().getUserId()+"@";
        return sysAppServiceImpl.install(key,tenantId,overrides.replace("\"","'"),treeId);
    }

    @MethodDefine(title = "??????????????????", path = "/importCheck", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????", varName = "request")})
    @PostMapping("/importCheck")
    @ApiOperation("??????????????????")
    public JsonResult importCheck(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence fileType = ".zip";
        if(!checkName.contains(fileType)){
            return JsonResult.Fail("???????????????????????????");
        }
        JSONArray array=sysInvokeScriptServiceImpl.readZipFile(file);
        String key=checkName +"@" + ContextUtil.getCurrentUser().getUserId()+"@";
        return sysAppServiceImpl.importCheck(array,key);
    }

    @MethodDefine(title = "???????????????????????????????????????", path = "/getByCategoryOrKey", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????ID", varName = "categoryId"),@ParamDefine(title = "???????????????", varName = "key")})
    @PostMapping("/getByCategoryOrKey")
    public  JsonResult getByCategoryOrKey(@ApiParam @RequestParam(value = "categoryId",required = false,defaultValue = "") String categoryId,
                                     @ApiParam @RequestParam(value = "key",required = false,defaultValue = "") String key){
        if(StringUtils.isEmpty(ContextUtil.getCurrentUserId())){
            return JsonResult.Fail("?????????????????????");
        }
        JsonResult result=JsonResult.Success("????????????");
        result.setShow(false);
        List<SysApp> lst=sysAppServiceImpl.getByCategory(categoryId,key);
        result.setData(lst);
        return  result;
    }

    @MethodDefine(title = "?????????????????????????????????", path = "/getCountByCategory", method = HttpMethodConstants.POST)
    @PostMapping("/getCountByCategory")
    public  JsonResult getCountByCategory(){
        if(StringUtils.isEmpty(ContextUtil.getCurrentUserId())){
            return JsonResult.Fail("?????????????????????");
        }
        JsonResult result=JsonResult.Success("????????????");
        result.setShow(false);
        Object data=sysAppServiceImpl.getCountByCategory();
        result.setData(data);
        return  result;
    }

    @MethodDefine(title = "??????appIds???????????????????????????????????????", path = "/getAppsByIdsAndType", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID??????", varName = "appIds"),@ParamDefine(title = "????????????", varName = "appType")})
    @ApiOperation("??????appIds???????????????????????????????????????")
    @GetMapping("/getAppsByIdsAndType")
    public List<SysAppDto> getAppsByIdsAndType(@ApiParam @RequestParam("appIds") String appIds,@ApiParam @RequestParam("appType") Integer appType){
        List<String> ids= new ArrayList<>();
        if (appIds.contains(",")){
            String[] split = appIds.split(",");
            for (String s : split) {
                ids.add("'"+s+"'");
            }
        }else {
            ids.add("'"+appIds+"'");
        }
        appIds=StringUtils.join(ids,",");
        List<SysApp> list=sysAppServiceImpl.getAppsByIdsAndType(appIds,appType);
        List<SysAppDto> sysAppDtos = BeanUtil.copyList(list,SysAppDto.class);
        decorateAppList(sysAppDtos);
        return sysAppDtos;
    }

    @MethodDefine(title = "???????????????????????????", path = "/downloadFront", method = HttpMethodConstants.GET)
    @ApiOperation("???????????????????????????")
    @PostMapping("downloadFront")
    public void downloadFront( ) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
           // File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static"+File.separator+"common-front-s.zip");
            ClassPathResource classPathResource = new ClassPathResource("static"+File.separator+"common-front.zip");
            response.addHeader("blob", "true");
            response.setContentType("application/zip");
            // ??????file??????
           /* String ext=new MimetypesFileTypeMap().getContentType(file);
            response.setContentType(ext);*/
            String  zipName = URLEncoder.encode("common-front","UTF-8");
            response.addHeader("Content-Disposition",  "attachment;filename=" +zipName+".zip");
            InputStream fis = classPathResource.getStream();
            FileUtil.writeInput(fis,response.getOutputStream());
        } catch (FileNotFoundException e) {
            log.error(ExceptionUtil.getExceptionMessage(e));
        }
    }

    @MethodDefine(title = "???????????????????????????", path = "/downloadBackend", method = HttpMethodConstants.POST)
    @ApiOperation("???????????????????????????")
    @PostMapping("downloadBackend")
    public void downloadBackend(@RequestBody JSONObject ent) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
            ClassPathResource classPathResource = new ClassPathResource("static"+File.separator+"jpaas-demo.zip");
            response.setContentType("application/zip");
            response.addHeader("blob", "true");
            // ??????file??????
           /* String ext=new MimetypesFileTypeMap().getContentType(file);
            response.setContentType(ext);*/
            String  zipName = URLEncoder.encode("jpaas-demo","UTF-8");
            response.addHeader("Content-Disposition",  "attachment;filename=" +zipName);
            InputStream fis = classPathResource.getStream();
            FileUtil.writeInput(fis,response.getOutputStream());
        } catch (FileNotFoundException e) {
            log.error(ExceptionUtil.getExceptionMessage(e));
        }

    }

}
