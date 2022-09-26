package com.redxun.ureport.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.dto.form.AlterSql;
import com.redxun.feign.sys.SystemClient;
import com.redxun.ureport.core.entity.UreportFile;
import com.redxun.ureport.core.export.UreportExport;
import com.redxun.ureport.core.service.UreportFileServiceImpl;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.controller.IExport;
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
@RequestMapping("/ureport2/core/ureportFile")
@ClassDefine(title = "ureport_file",alias = "ureportFileController",path = "/ureport2/core/ureportFile",packages = "core",packageName = "报表管理")
@Api(tags = "ureport_file")
public class UreportFileController extends BaseController<UreportFile> {

    @Autowired
    UreportFileServiceImpl ureportFileService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    UreportExport ureportExport;

    @Override
    protected IExport getExport() {
        return ureportExport;
    }
    @Override
    public BaseService getBaseService() {
        return ureportFileService;
    }

    @Override
    public String getComment() {
        return "ureport_file";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        //增加分类过滤
        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","UREPORT2","read");

        super.handleFilter(filter);
    }



    @MethodDefine(title = "导出", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            throw new Exception("导出失败，请选择要导出的记录。");
        }

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {
            JSONObject json= ureportFileService.doExportById(id);
            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Ureport-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "报表导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @ApiOperation("导入")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        List<AlterSql> delaySqlList = ureportFileService.importUreportZip(file,treeId);
        return JsonResult.Success().setData(delaySqlList).setMessage("导入成功");
    }

}
