
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.dto.sys.SysInterfaceApiDto;
import com.redxun.system.core.entity.SysInterfaceApi;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/system/core/sysInterfaceApi")
@Api(tags = "接口API表")
@ClassDefine(title = "接口API表",alias = "SysInterfaceApiController",path = "/system/core/sysInterfaceApi",packages = "core",packageName = "子系统名称")
public class SysInterfaceApiController extends BaseController<SysInterfaceApi> {

    @Autowired
    SysInterfaceApiServiceImpl sysInterfaceApiService;


    @Override
    public BaseService getBaseService() {
        return sysInterfaceApiService;
    }

    @Override
    public String getComment() {
        return "接口API表";
    }

    @Override
    protected JsonResult beforeSave(SysInterfaceApi ent) {
        boolean result= sysInterfaceApiService.isExist( ent);
        if(result){
            return JsonResult.Fail("接口名称已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "根据接口ID获取接口信息", path = "getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "接口ID", varName = "apiId")})
    @ApiOperation("根据接口ID获取接口信息")
    @GetMapping("/getById")
    public SysInterfaceApiDto getById(@ApiParam @RequestParam(value = "apiId") String apiId){
        SysInterfaceApiDto sysInterfaceApiDto=new SysInterfaceApiDto();
        SysInterfaceApi sysInterfaceApi=sysInterfaceApiService.getById(apiId);
        if(BeanUtil.isNotEmpty(sysInterfaceApi)){
            BeanUtil.copyProperties(sysInterfaceApiDto,sysInterfaceApi);
        }
        return sysInterfaceApiDto;
    }

    @MethodDefine(title = "根据项目ID查询接口", path = "/queryByProjectId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "项目ID", varName = "projectId"),@ParamDefine(title = "状态", varName = "status")})
    @ApiOperation("根据项目ID查询接口")
    @GetMapping("/queryByProjectId")
    public List<SysInterfaceApi> queryByProjectId(@ApiParam @RequestParam(value = "projectId") String projectId,
                                                  @ApiParam @RequestParam(value = "status",required = false)String status){
        return sysInterfaceApiService.getByProjectId(projectId,status);
    }

    @MethodDefine(title = "根据分类ID查询接口", path = "/queryByClassificationId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类ID", varName = "classId"),@ParamDefine(title = "状态", varName = "status")})
    @ApiOperation("根据分类ID查询接口")
    @GetMapping("/queryByClassificationId")
    public List<SysInterfaceApi> queryByClassificationId(@ApiParam @RequestParam(value = "classId") String classId,
                                                         @ApiParam @RequestParam(value = "status",required = false)String status){
        return sysInterfaceApiService.getByClassificationId(classId,status);
    }

    @MethodDefine(title = "测试接口", path = "/executeTestApi", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "接口ID", varName = "apiId")})
    @ApiOperation("测试接口")
    @GetMapping("/executeTestApi")
    public JsonResult executeTestApi(@ApiParam @RequestParam(value = "apiId") String apiId) throws Exception{
        return sysInterfaceApiService.executeApiTest(apiId);
    }

    @MethodDefine(title = "执行接口", path = "/executeApi", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "接口ID", varName = "apiId"),@ParamDefine(title = "传递参数",varName = "params")})
    @ApiOperation("执行接口")
    @PostMapping("/executeApi")
    public JsonResult executeApi(@ApiParam @RequestParam(value = "apiId") String apiId,@ApiParam @RequestBody String params) throws Exception{
        return sysInterfaceApiService.executeApi(apiId,params, IdGenerator.getIdStr());
    }

    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("file");
        String checkName = file.getOriginalFilename();
        CharSequence sysInterface = "Sys-Interface";
        if(!checkName.contains(sysInterface)){
            return JsonResult.Fail().setData("请选择正确的压缩包").setMessage("导入失败");
        }
        String classId=request.getParameter("classId");
        String type=request.getParameter("type");
        String importMessage = sysInterfaceApiService.importSysInterfaceZip(file,classId,type);
        return JsonResult.Success().setData(importMessage).setMessage("导入成功");
    }

    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "classId") String classId,@ApiParam @RequestParam(value ="apiIds", required = false) String apiIds,@ApiParam @RequestParam String type)throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        JSONArray json=new JSONArray();
        if("SELECT".equals(type)){
            if(StringUtils.isEmpty(apiIds)){
                throw new Exception("导出失败，请选择要导出的接口。");
            }
            String[] apiIdAry=apiIds.split(",");
            for(String apiId:apiIdAry){
                json.add(sysInterfaceApiService.get(apiId));
            }
        }else {
            if (StringUtils.isEmpty(classId)) {
                throw new Exception("导出失败，请选择要导出的分类。");
            }
            json = sysInterfaceApiService.doExportById(classId, type);
        }
        Map<String, String> map = new HashMap<>();
        String fileName = classId + ".json";
        String defStr = JSONArray.toJSONString(json);
        map.put(fileName, defStr);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-Interface-" + sdf.format(new Date());
        FileUtil.downloadZip(response, downFileName, map);
    }

}

