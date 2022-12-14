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
@ClassDefine(title = "????????????",alias = "formCustomQueryController",path = "/form/core/formCustomQuery",packages = "core",packageName = "????????????")
@Api(tags = "????????????")
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
        return "????????????";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    @MethodDefine(title = "??????????????????", path = "/getConstantItem", method = HttpMethodConstants.GET)
    @ApiOperation(value="????????????", notes="??????????????????")
    @GetMapping("/getConstantItem")
    public List<KeyValEnt> getConstantItem(){
        List<KeyValEnt> ents=new ArrayList<>();
        List<ConstVarType> list = ConstVarContext.getTypes();
        for(ConstVarType constVarType:list){
            ents.add(new KeyValEnt(constVarType.getKey(),constVarType.getName()));
        }
        return ents;
    }

    @MethodDefine(title = "???????????????????????????SQL??????", path = "/queryForJson_*", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request"),@ParamDefine(title = "??????", varName = "alias")})
    @ApiOperation(value="??????JSON", notes="???????????????????????????SQL??????")
    @PostMapping("/queryForJson_{alias}")
    public JsonResult queryForJson( @PathVariable(value = "alias")String alias) throws Exception{
        if(StringUtils.isEmpty(alias)){
            return JsonResult.getFailResult("???????????????");
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String jsonStr = RequestUtil.getString(request,"params");
        String deploy = RequestUtil.getString(request,"deploy");

        JsonResult result = FormCustomQueryUtil.queryForJson(alias,jsonStr,deploy);
        return result;
    }


    @MethodDefine(title = "???????????????????????????SQL??????", path = "/queryForJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????", varName = "alias"),
                    @ParamDefine(title = "??????", varName = "params"),
                    @ParamDefine(title = "????????????", varName = "deploy")})
    @ApiOperation(value="???????????????????????????SQL??????", notes="???????????????????????????SQL??????")
    @PostMapping("/queryForJson")
    public JsonResult queryForJson( @RequestParam(value = "alias")String alias,
                                    @RequestParam(value = "params" ,required = false)String params,
                                    @RequestParam(value = "deploy",required = false)String deploy) throws Exception{
        if(StringUtils.isEmpty(alias)){
            return JsonResult.getFailResult("???????????????");
        }

        JsonResult result = FormCustomQueryUtil.queryForJson(alias,params,deploy);
        return result;
    }

    /**
     * ??????
     * @param pkId
     * @return
     */
    @MethodDefine(title = "???????????????????????????SQL", path = "/preview", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "pkId")})
    @ApiOperation(value="?????????SQL??????", notes="???????????????????????????SQL")
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
     * ??????
     * @param key
     * @return
     */
    @MethodDefine(title = "???????????????SQL??????", path = "/getByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "key")})
    @ApiOperation(value="???????????????SQL??????", notes="???????????????SQL??????")
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
            return  JsonResult.Fail("??????????????????????????????!");
        }
        return  JsonResult.Success();
    }


    @MethodDefine(title = "?????????SQL??????", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????SQL??????", varName = "solutionIds")})
    @ApiOperation("?????????SQL??????")
    @AuditLog(operation = "?????????SQL??????")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "solutionIds") String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            throw new Exception("?????????????????????????????????????????????");
        }

        StringBuilder sb=new StringBuilder();

        sb.append("?????????SQL??????,??????????????????:");

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


    @MethodDefine(title = "?????????SQL??????", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "request")})
    @AuditLog(operation = "?????????SQL??????")
    @PostMapping("/doImport")
    @ApiOperation("????????????")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Form-CustomQuery";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("???????????????????????????");
        }
        String treeId=request.getParameter("treeId");
        formCustomQueryServiceImpl.importCustomQuery(file,treeId);
        return JsonResult.Success().setMessage("????????????");
    }

}
