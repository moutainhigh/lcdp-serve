package com.redxun.form.bo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.base.search.WhereParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.dto.form.BoRelation;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.dto.sys.SysTreeDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.FormBoRelationServiceImpl;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author hujun
 */
@Slf4j
@RestController
@RequestMapping("/form/bo/formBoEntity")
@ClassDefine(title = "业务实体",alias = "formBoEntityController",path = "/form/bo/formBoEntity",packages = "bo",packageName = "业务模型管理")
@Api(tags = "业务实体")
public class FormBoEntityController extends BaseController<FormBoEntity> {

    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    FormBoDefServiceImpl formBoDefService;
    @Autowired
    FormBoRelationServiceImpl formBoRelationServiceImpl;
    @Autowired
    FormPcServiceImpl formPcServiceImpl;
    @Autowired
    FormBoAttrServiceImpl boAttrService;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return formBoEntityServiceImpl;
    }

    @Override
    public String getComment() {
        return "业务实体";
    }

    @Override
    protected JsonResult beforeSave(FormBoEntity ent) {
        boolean isExist= formBoEntityServiceImpl.isExist(ent);
        if(isExist){
            return new JsonResult(false,"【"+ ent.getAlias() +"】实体已存在!");
        }
        return JsonResult.Success();
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        filter.addQueryParam("Q_GEN_MODE__S_NEQ",FormBoEntity.GENMODE_EASYFORM);
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    /**
     * 通过单据定义别名获取业务实体别名与实体的映射结构
     * @param alias
     * @return
     */
    @MethodDefine(title = "通过单据定义别名获取业务实体别名与实体的映射结构", path = "/getAliasByFormAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据定义别名", varName = "alias")})
    @ApiOperation("通过单据定义别名获取业务实体别名与实体的映射结构")
    @PostMapping(value = "getAliasByFormAlias")
    public String getAliasByFormAlias(@RequestParam(value = "alias") String alias){
        String[] ary=alias.split(",");
        StringBuilder sb=new StringBuilder(40);
        JSONObject jsonObject=new JSONObject(ary.length);
        for(String formAlias:ary){
            String boAlias=formBoDefService.getAliasByFormAlias(formAlias);
            FormBoDef formBoDef=formBoDefService.getByAlias(boAlias);

            FormBoEntity ent= formBoEntityServiceImpl.getByDefId(formBoDef.getId(),false);

            //{alias:{boAlias:"",entMap:{aaa:"main"}}}
            JSONObject entJson=new JSONObject();
            entJson.put("boAlias",boAlias);
            JSONObject entMap=new JSONObject();
            entMap.put(ent.getAlias(), BoRelation.RELATION_MAIN);

            for(FormBoEntity subEnt:ent.getBoEntityList()){
                entMap.put(subEnt.getAlias(), subEnt.getBoRelation().getType());
            }
            entJson.put("entMap",entMap);
            jsonObject.put(formAlias,entJson);
        }
        return  jsonObject.toJSONString();
    }

    /**
     *
     * @param json  {
     *              aliasList:[]
     *              fieldSetting:[]
     * }
     * @return
     */
    @MethodDefine(title = "根据单据定义别名数组与Bo实体的字段属性查找单据的定义与属性定义", path = "/getFormBoEntityListByAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据定义别名数组与Bo实体的字段属性", varName = "json")})
    @ApiOperation("根据单据定义别名数组与Bo实体的字段属性查找单据的定义与属性定义")
    @PostMapping(value = "getFormBoEntityListByAlias")
    public JSONObject getFormBoEntityListByAlias(@RequestBody JSONObject json){
        JSONObject result = new JSONObject();

        String aliasList = json.getString("aliasList");
        JSONArray boEntityData = json.getJSONArray("fieldSetting");
        //审批意见配置
        JSONArray opinionSetting = json.getJSONArray("opinionSetting");
        if(StringUtils.isEmpty(aliasList)){
            return result;
        }

        String[] aliaList = aliasList.split("[,]");
        JSONArray fieldSettings=new JSONArray();
        JSONArray opinionSettings=new JSONArray();
        for (String alias:aliaList) {
            FormBoDef formBoDef= formBoDefService.getByAlias(alias);
            if(BeanUtil.isEmpty(formBoDef)){
                continue;
            }
            String boDefId=formBoDef.getId();
            FormBoEntity boEntity= formBoEntityServiceImpl.getWithAttrsByDefId(boDefId);
            JSONArray ary= formBoEntityServiceImpl.getAttrListByBoEntity(boEntityData,boEntity);
            FormPc formPc = formPcServiceImpl.getByAlias(alias);
            String opinionDef = formPc.getOpinionDef();
            JSONArray opinionArray = JSONArray.parseArray(opinionDef);
            JSONArray opinionAry =formBoEntityServiceImpl.getOpinionSettingByBoEntity(alias,opinionArray,opinionSetting,boEntity);
            for (int i = 0; i < ary.size(); i++) {
                fieldSettings.add(ary.get(i));
            }
            for (int i = 0; i < opinionAry.size(); i++) {
                opinionSettings.add(opinionAry.get(i));
            }
        }
        result.put("fieldSetting",fieldSettings);
        result.put("opinionSetting",opinionSettings);
        return result;
    }

    @MethodDefine(title = "根据单据定义别名获取字段属性", path = "/getFormFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据定义别名列表", varName = "aliasList")})
    @GetMapping(value = "getFormFields")
    public JSONArray getFormFields(@RequestParam(value = "aliasList")String aliasList){
        JSONArray resultData = new JSONArray();
        if(StringUtils.isEmpty(aliasList)){
            addSolutionObj(resultData);
            return resultData;
        }
        String[] aliaList = aliasList.split("[,]");
        for (String alias:aliaList) {
            FormPc formPc= formPcServiceImpl.getByAlias(alias);
            if(BeanUtil.isEmpty(formPc)){
                continue;
            }
            FormBoEntity boEntity= formBoEntityServiceImpl.getMainEntByDefId(formPc.getBodefId());
            if(BeanUtil.isEmpty(boEntity)){
                continue;
            }
            List<FormBoAttr> boAttrs=boAttrService.getByEntId(boEntity.getId());
            for (FormBoAttr attr:boAttrs) {
                if(attr.single()){
                    //单值
                    String key=alias+"."+attr.getName();
                    JSONObject attrObj= new JSONObject();
                    attrObj.put("key",key);
                    attrObj.put("name",attr.getComment());
                    resultData.add(attrObj);
                }else{
                    //双值
                    String key=alias+"."+attr.getName()+".value";
                    JSONObject attrObj= new JSONObject();
                    attrObj.put("key",key);
                    attrObj.put("name",attr.getComment()+"ID");
                    resultData.add(attrObj);
                    key=alias+"."+attr.getName()+".label";
                    attrObj= new JSONObject();
                    attrObj.put("key",key);
                    attrObj.put("name",attr.getComment());
                    resultData.add(attrObj);
                }


            }
        }
        addSolutionObj(resultData);
        return resultData;
    }

    private void addSolutionObj(JSONArray resultData){
        JSONObject processName= new JSONObject();
        processName.put("key","processName");
        processName.put("name","流程方案名称");
        resultData.add(processName);

        JSONObject createTime= new JSONObject();
        createTime.put("key","createTime");
        createTime.put("name","创建时间");
        resultData.add(createTime);

        JSONObject createUser= new JSONObject();
        createUser.put("key","createUser");
        createUser.put("name","创建人");
        resultData.add(createUser);

        JSONObject curDate= new JSONObject();
        curDate.put("key","curDate");
        curDate.put("name","创建日期");
        resultData.add(curDate);
    }

    @ApiOperation(value = "删除业务实体")
    @PostMapping(value = "delBoEntity")
    public JsonResult delBoEntity(@RequestParam("ids") String ids, @RequestParam("delTable") Boolean delTable) {
        if (StringUtils.isEmpty(ids)) {
            return new JsonResult(false, "删除失败!");
        }
        String[] aryId = ids.split(",");
        //删除失败的id
        List<String> failureIds=new ArrayList<>() ;
        for (int i = 0; i < aryId.length; i++) {
            List<FormBoRelation> boRelationList = formBoRelationServiceImpl.getByEntId(aryId[i]);
            if (boRelationList.size() == 0) {
                FormBoEntity formBoEntity = formBoEntityServiceImpl.get(aryId[i]);
                if(formBoEntity!=null && delTable){
                    //删除物理表
                    formBoEntityServiceImpl.deleteBoTable(aryId[i], formBoEntity.getTableName());
                }
                formBoEntityServiceImpl.delete(aryId[i]);
            }else {
                failureIds.add(aryId[i]);
            }
        }
        JsonResult result = new JsonResult().setShow(false);
        if(failureIds.size()>0){
            StringBuffer str=new StringBuffer();
            for(int i = 0; i < failureIds.size(); i++){
                if(i!=0){
                    str.append("、");
                }
                FormBoEntity formBoEntity = formBoEntityServiceImpl.get(failureIds.get(i));
                str.append(formBoEntity.getName());
            }
            result.setSuccess(false);
            result.setMessage(str.toString()+"已绑定业务模型无法删除!");
        }else {
            result.setSuccess(true);
            result.setMessage("删除" + getComment() + "成功!");
        }
        return result;

    }

    @MethodDefine(title = "生成物理表", path = "/creatBoTable", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="生成物理表")
    @GetMapping("/creatBoTable")
    public JsonResult creatBoTable(@RequestParam (value="pkId") String pkId){
        JsonResult jsonResult= formBoEntityServiceImpl.creatBoTable(pkId);
        return jsonResult;
    }

    @MethodDefine(title = "删除物理表", path = "/deleteBoTable", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId"),@ParamDefine(title = "表名", varName = "tableName")})
    @ApiOperation(value="删除物理表")
    @GetMapping("/deleteBoTable")
    public JsonResult deleteBoTable(@RequestParam(value="pkId")String pkId,@RequestParam(value="tableName")String tableName){
        JsonResult jsonResult= formBoEntityServiceImpl.deleteBoTable(pkId,tableName);
        return jsonResult;
    }

    @MethodDefine(title = "保存实体", path = "/saveBoEnt", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体数据", varName = "json"),@ParamDefine(title = "直接保存", varName = "directlySave")})
    @ApiOperation(value="保存实体")
    @PostMapping("/saveBoEnt")
    public JsonResult saveBoEnt(@RequestBody JSONObject json,@RequestParam(value = "directlySave",required = false,defaultValue = "false") Boolean directlySave){
        JsonResult jsonResult= formBoEntityServiceImpl.saveBoEnt(json,directlySave);
        return jsonResult;
    }

    @MethodDefine(title = "获取实体", path = "/getBoEnt", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId"),@ParamDefine(title = "暂存属性", varName = "boAttrTemp")})
    @ApiOperation(value="获取实体")
    @GetMapping("/getBoEnt")
    public JsonResult getBoEnt(@RequestParam(value = "pkId") String pkId,@RequestParam(value = "boAttrTemp",required = false,defaultValue = "false") boolean boAttrTemp) {
        JsonResult result = JsonResult.Success();
        result.setShow(false);
        if (BeanUtil.isEmpty(pkId)) {
            return result.setData(new Object());
        }
        FormBoEntity boEntity=formBoEntityServiceImpl.get(pkId);
        //取暂存的实体属性
        if(boAttrTemp && BeanUtil.isNotEmpty(boEntity.getBoAttrTemp())){
            List<FormBoAttr> attrs = JSONArray.parseArray(boEntity.getBoAttrTemp(), FormBoAttr.class);
            boEntity.setBoAttrList(attrs);
        }else {
            List<FormBoAttr> attrs= boAttrService.getByEntId(pkId);
            boEntity.setBoAttrList(attrs);
        }
        return result.setData(boEntity);
    }

    @GetMapping("/getBoEntById")
    public FormBoEntityDto getBoEntById(@RequestParam(value="entId")String entId){
        FormBoEntityDto dto=new FormBoEntityDto();
        FormBoEntity entity=formBoEntityServiceImpl.getByEntId(entId);
        BeanUtil.copyProperties(dto, entity);
        return dto;
    }

    @GetMapping("/getBoEntByTableName")
    public FormBoEntityDto getBoEntByTableName(@RequestParam(value="tableName")String tableName){
        FormBoEntityDto dto=new FormBoEntityDto();
        FormBoEntity entity=formBoEntityServiceImpl.getByTableName(tableName);
        BeanUtil.copyProperties(dto, entity);
        return dto;
    }

    @MethodDefine(title = "根据Bo定义ID和表名获取字段属性", path = "/getFieldByBoDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @GetMapping("/getFieldByBoDefId")
    public List<FormBoAttr> getFieldByBoDefId(HttpServletRequest request) {
        String boDefId = RequestUtil.getString(request, "boDefId");
        String entName = RequestUtil.getString(request, "tableName");
        List<FormBoAttr> sysBoAttrs = new ArrayList<>();
        String reg = ",";
        if (boDefId.indexOf(reg) != -1) {
            String[] defArray = boDefId.split(reg);
            for (int i = 0; i < defArray.length; i++) {
                FormBoEntity sysBoEnt = formBoEntityServiceImpl.getByDefId(defArray[i], true);
                entName = sysBoEnt.getAlias();
                FormBoRelation sysBoRelation = formBoRelationServiceImpl.getEntByEntNameAndDefId(defArray[i], entName);
                String entId = sysBoRelation.getEntId();
                sysBoAttrs.addAll(boAttrService.getByEntId(entId));
            }
        } else {
            if ("_main".equals(entName)) {
                FormBoEntity sysBoEnt = formBoEntityServiceImpl.getByDefId(boDefId, true);
                entName = sysBoEnt.getAlias();
            }
            FormBoRelation sysBoRelation = formBoRelationServiceImpl.getEntByEntNameAndDefId(boDefId, entName);
            String entId = sysBoRelation.getEntId();
            sysBoAttrs = boAttrService.getByEntId(entId);
        }

        return sysBoAttrs;
    }

    @MethodDefine(title = "根据Bo定义ID查询业务实体", path = "/queryToBoEntity", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义ID", varName = "boDefId")})
    @GetMapping("/queryToBoEntity")
    public List<FormBoEntity> queryToBoEntity(@RequestParam(value="boDefId")String boDefId){
        return formBoEntityServiceImpl.getByDefId(boDefId);
    }

    @MethodDefine(title = "根据Bo定义别名查询业务实体", path = "/queryToBoEntityByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义别名", varName = "boAlias")})
    @GetMapping("/queryToBoEntityByAlias")
    public JSONObject queryToBoEntityByAlias(@RequestParam(value="boAlias")String boAlias){
        JSONObject json=new JSONObject();
        List<FormBoEntity> list=new ArrayList<>();
        String[] alias = boAlias.split(",");
        for(int i=0;i<alias.length;i++){
            FormBoDef formBoDef=formBoDefService.getByAlias(alias[i]);
            if(formBoDef==null){
                continue;
            }
            list.addAll(formBoEntityServiceImpl.getByDefId(formBoDef.getId()));
        }

        json.put("data",list);
        return json;
    }

    /**
     * 返回bo，但不返回其属性定义
     * @param boDefId
     * @return
     */
    @MethodDefine(title = "根据Bo定义ID查询业务实体,但不返回其属性定义", path = "/getMainExcludeAttrsByBoDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义ID", varName = "boDefId")})
    @GetMapping("getMainExcludeAttrsByBoDefId")
    public JsonResult<FormBoEntity> getMainExcludeAttrsByBoDefId(@RequestParam(value="boDefId")String boDefId){
        if(StringUtils.isEmpty(boDefId)){
            return new JsonResult<>(false);
        }
        FormBoEntity sysBoEnt=formBoEntityServiceImpl.getMainEntByDefId(boDefId);
        return JsonResult.getSuccessResult(sysBoEnt,"success").setShow(false);
    }

    @MethodDefine(title = "保存实体", path = "/setFormInstStatus", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体数据", varName = "json")})
    @ApiOperation(value="保存实体")
    @PostMapping("/setFormInstStatus")
    public JsonResult setFormInstStatus(@RequestBody JSONArray jsonArray){
        JsonResult jsonResult= formBoEntityServiceImpl.setFormInstStatus(jsonArray);
        return jsonResult;
    }


    @MethodDefine(title = "实体暂存", path = "/temporarySave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体数据", varName = "json")})
    @ApiOperation(value="实体暂存")
    @PostMapping("/temporarySave")
    public JsonResult temporarySave(@RequestBody JSONObject json){
        JsonResult jsonResult= formBoEntityServiceImpl.temporarySave(json);
        return jsonResult;
    }
}
