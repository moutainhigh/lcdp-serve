package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.KeyValEnt;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.constvar.ConstVarType;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.core.entity.FormCustomQuery;
import com.redxun.form.core.service.FormCustomQueryServiceImpl;
import com.redxun.form.core.service.FormCustomQueryUtil;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @author hujun
 */
@Slf4j
@RestController
@RequestMapping("/form/core/formCustomQuery")
@ClassDefine(title = "自定查询",alias = "formCustomQueryController",path = "/form/core/formCustomQuery",packages = "core",packageName = "表单管理")
@Api(tags = "自定查询")
@ContextQuerySupport(company = ContextQuerySupport.NONE)
public class FormCustomQueryController extends BaseController<FormCustomQuery> {

    @Autowired
    FormCustomQueryServiceImpl formCustomQueryServiceImpl;
    @Resource
    private FormExOrImportHandler formExOrImportHandler;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return formCustomQueryServiceImpl;
    }

    @Override
    public String getComment() {
        return "自定查询";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    @MethodDefine(title = "获取常量数据", path = "/getConstantItem", method = HttpMethodConstants.GET)
    @ApiOperation(value="获取常量", notes="获取常量数据")
    @GetMapping("/getConstantItem")
    public List<KeyValEnt> getConstantItem(){
        List<KeyValEnt> ents=new ArrayList<>();
        List<ConstVarType> list = ConstVarContext.getTypes();
        for(ConstVarType constVarType:list){
            ents.add(new KeyValEnt(constVarType.getKey(),constVarType.getName()));
        }
        return ents;
    }

    @MethodDefine(title = "根据别名查询自定义SQL数据", path = "/queryForJson_*", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request"),@ParamDefine(title = "别名", varName = "alias")})
    @ApiOperation(value="查询JSON", notes="根据别名查询自定义SQL数据")
    @PostMapping("/queryForJson_{alias}")
    public JsonResult queryForJson( @PathVariable(value = "alias")String alias) throws Exception{
        if(StringUtils.isEmpty(alias)){
            return JsonResult.getFailResult("请输入别名");
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String jsonStr = RequestUtil.getString(request,"params");
        String deploy = RequestUtil.getString(request,"deploy");

        JsonResult result = FormCustomQueryUtil.queryForJson(alias,jsonStr,deploy);
        return result;
    }


    @MethodDefine(title = "根据别名查询自定义SQL数据", path = "/queryForJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "别名", varName = "alias"),
                    @ParamDefine(title = "参数", varName = "params"),
                    @ParamDefine(title = "树形参数", varName = "deploy")})
    @ApiOperation(value="根据别名查询自定义SQL数据", notes="根据别名查询自定义SQL数据")
    @PostMapping("/queryForJson")
    public JsonResult queryForJson( @RequestParam(value = "alias")String alias,
                                    @RequestParam(value = "params" ,required = false)String params,
                                    @RequestParam(value = "deploy",required = false)String deploy) throws Exception{
        if(StringUtils.isEmpty(alias)){
            return JsonResult.getFailResult("请输入别名");
        }

        JsonResult result = FormCustomQueryUtil.queryForJson(alias,params,deploy);
        return result;
    }

    /**
     * 预览
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据主键预览自定义SQL", path = "/preview", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="自定义SQL预览", notes="根据主键预览自定义SQL")
    @GetMapping("/preview")
    public JsonResult preview(@RequestParam (value="pkId") String pkId) throws Exception{
        if(BeanUtil.isEmpty(pkId)){
            return JsonResult.Success().setData((Serializable) new Object()).setShow(false);
        }
        FormCustomQuery formCustomQuery= formCustomQueryServiceImpl.get(pkId);
        JSONArray whereColumns=new JSONArray();
        if(StringUtils.isNotEmpty(formCustomQuery.getWhereField())){
            JSONArray jsonArray=JSONArray.parseArray(formCustomQuery.getWhereField());
            for(int i=0;i<jsonArray.size();i++){
                JSONObject obj=jsonArray.getJSONObject(i);
                String valueSource=obj.getString("valueSource");
                if("param".equals(valueSource)){
                    whereColumns.add(obj);
                }
            }
        }
        JSONObject ent = (JSONObject) JSONObject.toJSON(formCustomQuery);
        ent.put("whereList",whereColumns);
        return JsonResult.Success("get data success").setData(ent).setShow(false);
    }


    /**
     * 预览
     * @param key
     * @return
     */
    @MethodDefine(title = "获取自定义SQL对象", path = "/getByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "key")})
    @ApiOperation(value="获取自定义SQL对象", notes="获取自定义SQL对象")
    @GetMapping("/getByKey")
    public JsonResult getByKey(@RequestParam (value="key") String key) throws Exception{
        FormCustomQuery formCustomQuery= formCustomQueryServiceImpl.getByKey(key);
        if(BeanUtil.isEmpty(formCustomQuery)){
            return JsonResult.Success().setData((Serializable) new Object());
        }
        return JsonResult.Success().setData(formCustomQuery);
    }

    @Override
    protected JsonResult beforeSave(FormCustomQuery ent) {
        boolean isExist= formCustomQueryServiceImpl.isExist(ent);
        if(isExist){
            return  JsonResult.Fail("自定义查询别名已存在!");
        }
        return  JsonResult.Success();
    }


    @MethodDefine(title = "自定义SQL导出", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "自定义SQL列表", varName = "solutionIds")})
    @ApiOperation("自定义SQL导出")
    @AuditLog(operation = "自定义SQL导出")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "solutionIds") String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            throw new Exception("导出失败，请选择要导出的记录。");
        }

        StringBuilder sb=new StringBuilder();

        sb.append("自定义SQL导出,导出列表如下:");

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {

            JSONObject json = new JSONObject();
            FormCustomQuery formCustomQuery = formCustomQueryServiceImpl.get(id);
            json.put("formCustomQuery", formCustomQuery);

            sb.append(formCustomQuery.getName() +"("+formCustomQuery.getId()+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }

        LogContext.put(Audit.DETAIL,sb.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Form-CustomQuery-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "自定义SQL导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "自定义SQL导入")
    @PostMapping("/doImport")
    @ApiOperation("批量导入")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Form-CustomQuery";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        formCustomQueryServiceImpl.importCustomQuery(file,treeId);
        return JsonResult.Success().setMessage("导入成功");
    }

}
