package com.redxun.system.core.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysSeqId;
import com.redxun.system.core.service.SysSeqIdServiceImpl;
import com.redxun.util.QueryFilterUtil;
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
@RequestMapping("/system/core/sysSeqId")
@ClassDefine(title = "系统流水号",alias = "sysSeqIdController",path = "/system/core/sysSeqId",packages = "core",packageName = "系统管理")
@Api(tags = "系统流水号")
public class SysSeqIdController extends BaseController<SysSeqId> {

    @Autowired
    SysSeqIdServiceImpl sysSeqIdServiceImpl;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return sysSeqIdServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统流水号";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {

        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    @Override
    protected JsonResult beforeSave(SysSeqId ent) {
        if(BeanUtil.isNotEmpty(ent.getConfAttr())){
            ent.setRuleConf(JSONUtil.toJsonStr(ent.getConfAttr()));
        }
        boolean result= sysSeqIdServiceImpl.isExist( ent);
        if(result){
            return JsonResult.Fail("数据已经存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "生成流水号", path = "/genSeqNo", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias")})
    @ApiOperation("生成流水号")
    @GetMapping(value = "genSeqNo")
    public String genSeqNo(@RequestParam(value = "alias") String alias){
        String seqNo= sysSeqIdServiceImpl.genSeqNo(alias);
        return  seqNo;
    }

    @MethodDefine(title = "获取所有流水号", path = "/getAllSeqno", method = HttpMethodConstants.GET)
    @ApiOperation("获取所有流水号")
    @GetMapping(value = "getAllSeqno")
    public List getAllSeqno(){
        List list= sysSeqIdServiceImpl.getAll();
        return  list;
    }

    @MethodDefine(title = "根据别名和名称获取流水号", path = "/getByAliasAndName", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "别名", varName = "seqAlias"),@ParamDefine(title = "名称", varName = "seqName")})
    @ApiOperation("根据别名和名称获取流水号")
    @PostMapping(value = "getByAliasAndName")
    public List getByAliasAndName(@RequestParam(value = "seqAlias") String seqAlias,@RequestParam(value = "seqName") String seqName,
                                  @RequestParam(value = "appId",required = false) String appId){
        List list= sysSeqIdServiceImpl.getByAliasAndName(seqAlias,seqName,appId);
        return  list;
    }

    @MethodDefine(title = "导出流水号", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出流水号")
    @AuditLog(operation = "导出流水号")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            LogContext.addError("导出失败，请选择要导出的记录。");
            return;
        }

        StringBuilder sb=new StringBuilder();
        sb.append("导出流水号:");


        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {
            JSONObject json = new JSONObject();
            SysSeqId sysSeqId = sysSeqIdServiceImpl.get(id);
            json.put("sysSeqId", sysSeqId);

            sb.append(sysSeqId.getName() +"("+id+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }

        LogContext.put(Audit.DETAIL,sb.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-SeqId-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入流水号", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入流水号")
    @ApiOperation("导入流水号")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Sys-SeqId";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        String appId=request.getParameter("appId");
        sysSeqIdServiceImpl.importSysSeq(file,treeId,appId);
        return JsonResult.Success().setMessage("导入成功");
    }

}
