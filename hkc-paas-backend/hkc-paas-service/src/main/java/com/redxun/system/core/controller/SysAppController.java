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
@ClassDefine(title = "系统应用",alias = "sysAppController",path = "/system/core/sysApp",packages = "core",packageName = "系统管理")
@Api(tags = "系统应用")
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
        return "应用";
    }

    //重写保存方法，为新的应用增加默认权限
    @MethodDefine(title = "保存业务数据记录", path = "/save", method = HttpMethodConstants.POST, params = {@ParamDefine(title = "实体数据JSON", varName = "entity")})
    @ApiOperation(value="保存业务数据记录", notes="根据提交的业务JSON数据保存业务数据记录")
    @AuditLog(operation = "保存业务数据记录")
    @PostMapping("/save")
    @Override
    public JsonResult save(@Validated @ApiParam @RequestBody SysApp entity, BindingResult validResult) throws Exception{
        boolean needAddManager=false; //需要增加管理员
        if(StringUtils.isEmpty(entity.getAppId()) && entity.getAppType()==1){
            needAddManager=true;
        }
        JsonResult result=super.save(entity,validResult);
        //分配当前用户管理员权限
        if(needAddManager && result.isSuccess()){
            sysAppManagerService.AddManager4CurUser(entity.getAppId());
        }
        return result;
    }
    @Override
    protected JsonResult beforeSave(SysApp ent) {
        boolean isExist= sysAppServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("应用标识已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "获取所有应用列表", path = "/getAllApps", method = HttpMethodConstants.GET)
    @ApiOperation("获取所有应用列表")
    @GetMapping("/getAllApps")
    public List<SysAppDto> getAllApps(){
        List<SysApp> list = sysAppServiceImpl.getAllApps();
        List<SysAppDto> sysAppDtos = BeanUtil.copyList(list,SysAppDto.class);
        decorateAppList(sysAppDtos);
        return sysAppDtos;
    }

    @MethodDefine(title = "获取所有应用列表", path = "/getAll", method = HttpMethodConstants.GET)
    @ApiOperation("获取所有应用列表")
    @GetMapping("/getAll")
    @Override
    public JsonPageResult getAll(){
        //进行排序sn
        List data= sysAppServiceImpl.getAllApps();
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }


    @MethodDefine(title = "通过应用ID查找应用，如果应用ID为空则显示系统内置的应用", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title ="应用ID",varName = "appId")})
    @ApiOperation("通过应用ID查找应用，如果应用ID为空则显示系统内置的应用")
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

    @MethodDefine(title = "获取非单页面应用列表", path = "/getApps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title ="应用ID",varName = "appId")})
    @ApiOperation("获取非单页面应用列表")
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

    @MethodDefine(title = "根据ID集合获取应用列表", path = "/getAppsByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用ID集合", varName = "appIds")})
    @ApiOperation("根据ID集合获取应用列表")
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
    *  功能：装饰App列表，把是否收藏、最近使用时间 加进来
    * @param apps 应用列表
    * @author  Elwin ZHANG
    * @date 2022/1/20 17:10
    **/
    private  void decorateAppList(List<SysAppDto> apps){
        //记录为空则退出
        if(apps==null || apps.size()==0){
            return ;
        }
        //未取得当前登录用户ID退出
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return;
        }
        //循环处理每一个应用
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

    @MethodDefine(title = "保存或者修改应用", path = "/saveOrUpdate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用数据", varName = "sysApp")})
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或者修改应用")
    public JsonResult saveOrUpdate(@RequestBody SysApp sysApp) {
        return sysAppServiceImpl.saveOrUpdateSysApp(sysApp);
    }


    @MethodDefine(title = "删除应用", path = "/delById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "id", varName = "id)")})
    @ApiOperation("删除应用")
    @AuditLog(operation = "删除应用")
    @PostMapping("delById")
    public JsonResult delById(@RequestParam String id){

        if(StringUtils.isEmpty(id)){
            return new JsonResult(false,"参数不正确");
        }
        sysAppServiceImpl.delete(id);
        sysAppServiceImpl.writeActionLog(id, SysAppActionLog.TYPE_DELETE,"");
        JsonResult result= JsonResult.getSuccessResult("删除成功!");
        return result;
    }

    @MethodDefine(title = "导出应用", path = "/doExport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用信息", varName = "app")})
    @ApiOperation("导出应用")
    @AuditLog(operation = "导出应用")
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
                log.error("导出应用，参数不正确！");
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            Map<String, String> map = sysAppServiceImpl.query(appId,exportCustomTables);
            LogContext.put(Audit.DETAIL, "导出" + map.size() +"张表");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String downFileName = appName + "_" + sdf.format(new Date());
            FileUtil.downloadZip(response, downFileName, map);
            sysAppServiceImpl.writeActionLog(appId, SysAppActionLog.TYPE_EXPORT,"");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @MethodDefine(title = "应用导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "相关数据", varName = "data") })
    @PostMapping("/doImport")
    @AuditLog(operation = "导入应用")
    @ApiOperation("应用导入")
    public JsonResult doImport(@ApiParam @RequestBody  HashMap<String,String> data  ){
        String fileName= data.get("fileName");
        String tenantId= data.get("tenantId");
        String treeId=data.get("treeId");
        String overrides= data.get("overrides");
        String key=fileName +"@" + ContextUtil.getCurrentUser().getUserId()+"@";
        return sysAppServiceImpl.install(key,tenantId,overrides.replace("\"","'"),treeId);
    }

    @MethodDefine(title = "应用导入校验", path = "/importCheck", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用导入校验", varName = "request")})
    @PostMapping("/importCheck")
    @ApiOperation("应用导入校验")
    public JsonResult importCheck(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence fileType = ".zip";
        if(!checkName.contains(fileType)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        JSONArray array=sysInvokeScriptServiceImpl.readZipFile(file);
        String key=checkName +"@" + ContextUtil.getCurrentUser().getUserId()+"@";
        return sysAppServiceImpl.importCheck(array,key);
    }

    @MethodDefine(title = "按分类或关键字检索开发应用", path = "/getByCategoryOrKey", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用分类ID", varName = "categoryId"),@ParamDefine(title = "搜索关键字", varName = "key")})
    @PostMapping("/getByCategoryOrKey")
    public  JsonResult getByCategoryOrKey(@ApiParam @RequestParam(value = "categoryId",required = false,defaultValue = "") String categoryId,
                                     @ApiParam @RequestParam(value = "key",required = false,defaultValue = "") String key){
        if(StringUtils.isEmpty(ContextUtil.getCurrentUserId())){
            return JsonResult.Fail("请先登录系统！");
        }
        JsonResult result=JsonResult.Success("获取成功");
        result.setShow(false);
        List<SysApp> lst=sysAppServiceImpl.getByCategory(categoryId,key);
        result.setData(lst);
        return  result;
    }

    @MethodDefine(title = "查询每个分类的应用数量", path = "/getCountByCategory", method = HttpMethodConstants.POST)
    @PostMapping("/getCountByCategory")
    public  JsonResult getCountByCategory(){
        if(StringUtils.isEmpty(ContextUtil.getCurrentUserId())){
            return JsonResult.Fail("请先登录系统！");
        }
        JsonResult result=JsonResult.Success("获取成功");
        result.setShow(false);
        Object data=sysAppServiceImpl.getCountByCategory();
        result.setData(data);
        return  result;
    }

    @MethodDefine(title = "根据appIds集合与应用类型获取应用列表", path = "/getAppsByIdsAndType", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID集合", varName = "appIds"),@ParamDefine(title = "应用类型", varName = "appType")})
    @ApiOperation("根据appIds集合与应用类型获取应用列表")
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

    @MethodDefine(title = "前端自定义页面开发", path = "/downloadFront", method = HttpMethodConstants.GET)
    @ApiOperation("前端自定义页面开发")
    @PostMapping("downloadFront")
    public void downloadFront( ) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
           // File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "static"+File.separator+"common-front-s.zip");
            ClassPathResource classPathResource = new ClassPathResource("static"+File.separator+"common-front.zip");
            response.addHeader("blob", "true");
            response.setContentType("application/zip");
            // 创建file对象
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

    @MethodDefine(title = "后端自定义接口开发", path = "/downloadBackend", method = HttpMethodConstants.POST)
    @ApiOperation("后端自定义接口开发")
    @PostMapping("downloadBackend")
    public void downloadBackend(@RequestBody JSONObject ent) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
            ClassPathResource classPathResource = new ClassPathResource("static"+File.separator+"jpaas-demo.zip");
            response.setContentType("application/zip");
            response.addHeader("blob", "true");
            // 创建file对象
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
