package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysAuthRights;
import com.redxun.system.core.entity.SysAuthSetting;
import com.redxun.system.core.service.SysAuthRightsServiceImpl;
import com.redxun.system.core.service.SysAuthSettingServiceImpl;
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
@RequestMapping("/system/core/sysAuthSetting")
@ClassDefine(title = "分类权限配置管理",alias = "sysAuthSettingController",path = "/system/core/sysAuthSetting",packages = "core",packageName = "系统管理")
@Api(tags = "分类权限配置管理")
public class SysAuthSettingController extends BaseController<SysAuthSetting> {

    @Autowired
    SysAuthSettingServiceImpl sysAuthSettingService;
    @Autowired
    SysAuthRightsServiceImpl sysAuthRightsService;

    @Override
    public BaseService getBaseService() {
        return sysAuthSettingService;
    }

    @Override
    public String getComment() {
        return "权限配置表";
    }

    @MethodDefine(title = "查找权限配置", path = "/findAuthRight", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "类型", varName = "type"),@ParamDefine(title = "字段",varName = "readKey"),@ParamDefine(title = "treeId", varName = "树ID")})
    @ApiOperation("查找权限配置")
    @GetMapping("/findAuthRight")
    public boolean findAuthRight(@RequestParam(value="type")String type,@RequestParam(value="readKey")String readKey,@RequestParam(value="treeId")String treeId){
        return sysAuthRightsService.findAuthRight(type,readKey,treeId);
    }

    @MethodDefine(title = "根据ID集合获取权限配置", path = "/getAuthSetting", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId")})
    @ApiOperation("根据ID集合获取权限配置")
    @GetMapping("/getAuthSetting")
    public JsonResult getAuthSetting(@RequestParam (value="pkId") String pkId){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(ObjectUtils.isEmpty(pkId)){
            return result.setData(new Object());
        }
        SysAuthSetting sysAuthSetting = sysAuthSettingService.get(pkId);
        List<SysAuthRights> sysAuthRights = sysAuthRightsService.getBySettingId(pkId);
        sysAuthSetting.setSysAuthRights(sysAuthRights);
        return result.setData(sysAuthSetting);
    }

    @Override
    public JsonResult del(String ids) {
        sysAuthRightsService.delBySettingId(ids);
        return super.del(ids);
    }

    @MethodDefine(title = "保存权限配置", path = "/saveConfigJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "权限配置", varName = "json")})
    @ApiOperation("保存权限配置")
    @AuditLog(operation = "保存权限配置")
    @PostMapping("/saveConfigJson")
    public JsonResult saveConfigJson(@RequestBody JSONObject json) throws Exception {
        String id = json.getString("id");
        String type = json.getString("type");
        String name = json.getString("name");
        String enable = json.getString("enable");
        String treeId = json.getString("treeId");
        String treeName = json.getString("treeName");
        String rightJson = json.getString("rightJson");

        SysAuthSetting sysAuthSetting = new SysAuthSetting();
        sysAuthSetting.setId(id).setName(name).setType(type).setEnable(enable).setRightJson(rightJson);

        if(StringUtils.isEmpty(id)) {
            sysAuthSettingService.insert(sysAuthSetting);
        }else{
            sysAuthRightsService.delBySettingId(id);
            sysAuthSettingService.update(sysAuthSetting);
        }
        if(StringUtils.isNotEmpty(treeId)){
            String[] treeIds=treeId.split(",");
            String[] treeNames=treeName.split(",");
            for(int i=0;i<treeIds.length;i++) {
                sysAuthRightsService.insert(new SysAuthRights().setSettingId(sysAuthSetting.getId()).setTreeId(treeIds[i]).setTreeName(treeNames[i]));
            }
        }

        String detail="保存权限配置,权限名称:" + name +"("+id+")";
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.getSuccessResult("成功保存分类权限配置！");
    }

    @MethodDefine(title = "导出分类权限", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出分类权限")
    @AuditLog(operation = "导出分类权限")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "solutionIds") String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            LogContext.addError("导出失败，请选择要导出的记录。");
            return;
        }

        StringBuilder sb=new StringBuilder();
        sb.append("导出分类权限:");

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {
            JSONObject json = new JSONObject();
            SysAuthSetting sysAuthSetting = sysAuthSettingService.get(id);
            json.put("SysAuthSetting", sysAuthSetting);
            List<SysAuthRights> SysAuthRights = sysAuthRightsService.getBySettingId(id);
            json.put("SysAuthRights", SysAuthRights);

            sb.append(sysAuthSetting.getName() +"("+id+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }
        LogContext.put(Audit.DETAIL,sb.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-AuthSetting-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入分类权限", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @ApiOperation("导入分类权限")
    @AuditLog(operation = "导入分类权限")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Sys-AuthSetting";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        sysAuthSettingService.importSysAuth(file,treeId);
        return JsonResult.Success().setMessage("导入成功");
    }

}
