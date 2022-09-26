package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
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
import com.redxun.dto.sys.SysDicDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysDic;
import com.redxun.system.core.entity.SysTree;
import com.redxun.system.core.entity.SysTreeCat;
import com.redxun.system.core.service.SysDicServiceImpl;
import com.redxun.system.core.service.SysTreeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/system/core/sysDic")
@ClassDefine(title = "系统数字字典分类",alias = "sysDicController",path = "/system/core/sysDic",packages = "core",packageName = "系统管理")
@Api(tags = "系统数字字典分类")
public class SysDicController extends BaseController<SysDic> {

    @Autowired
    SysDicServiceImpl sysDicServiceImpl;
    @Autowired
    SysTreeServiceImpl sysTreeServiceImpl;

    @Override
    public BaseService getBaseService() {
        return sysDicServiceImpl;
    }

    @Override
    public String getComment() {
        return "数字字典";
    }

    @MethodDefine(title = "获取某一分类树的一级所有字典列表", path = "/getTopDicByTreeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树ID", varName = "treeId")})
    @ApiOperation("获取某一分类树的一级所有字典列表")
    @GetMapping("/getTopDicByTreeId")
    public List<SysDicDto> getTopDicByTreeId(@ApiParam @RequestParam String treeId){
        List<SysDicDto> list= sysDicServiceImpl.getTopDicByTreeId(treeId);
        return list;
    }

    @MethodDefine(title = "获取某一分类树的所有字典列表", path = "/getTopDicByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树KEY", varName = "key")})
    @ApiOperation("获取某一分类树的所有字典列表")
    @GetMapping("/getTopDicByKey")
    public List<SysDicDto> getTopDicByKey(@ApiParam @RequestParam(value = "key") String key){
        SysTree sysTree = sysTreeServiceImpl.getByKey(key, SysTreeCat.CAT_DIC);
        if(BeanUtil.isEmpty(sysTree)){
            return new ArrayList<>();
        }
        List<SysDicDto> list= sysDicServiceImpl.getTopDicByTreeId(sysTree.getTreeId());
        return list;
    }

    @MethodDefine(title = "根据树Id来获取所有的数字字典", path = "/getByTreeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树ID", varName = "treeId")})
    @ApiOperation("根据树Id来获取所有的数字字典")
    @GetMapping("getByTreeId")
    public List<SysDic> getByTreeId(@ApiParam @RequestParam String treeId){
        List list= sysDicServiceImpl.getByTreeId(treeId);
        return list;
    }

    @MethodDefine(title = "根据树别名来获取所有的数字字典", path = "/getByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树别名", varName = "alias")})
    @ApiOperation("根据树别名来获取所有的数字字典")
    @GetMapping("getByAlias")
    public List<SysDic> getByAlias(@ApiParam @RequestParam String alias){
        SysTree sysTree=sysTreeServiceImpl.getByKey(alias,SysTreeCat.CAT_DIC);
        List list =null;
        if(sysTree!=null) {
           list= sysDicServiceImpl.getByTreeId(sysTree.getTreeId());
        }
        return list;
    }

    @MethodDefine(title = "根据树Id来获取所有的数字字典", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树ID", varName = "dicId")})
    @ApiOperation("根据树Id来获取所有的数字字典")
    @GetMapping("getById")
    public  JsonResult getById(@ApiParam @RequestParam(value = "dicId") String dicId){
        SysDic sysDic= sysDicServiceImpl.getByDicId(dicId);
        JsonResult result= JsonResult.Success();
        result.setData(sysDic);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "获取某个节点下的子节点列表，不含下下一级的子节点", path = "/getByParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "父节点ID", varName = "parentId")})
    @ApiOperation("获取某个节点下的子节点列表，不含下下一级的子节点")
    @GetMapping("/getByParentId")
    public List<SysDic> getByParentId(@ApiParam @RequestParam String parentId){
        return sysDicServiceImpl.getByParentId(parentId);
    }

    @MethodDefine(title = "获取某个节点下的子节点列表", path = "/getByTreeIdAndParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树ID", varName = "treeId"),@ParamDefine(title = "父节点ID", varName = "parentId")})
    @ApiOperation("获取某个节点下的子节点列表")
    @GetMapping("/getByTreeIdAndParentId")
    public List<SysDic> getByTreeIdAndParentId(@ApiParam @RequestParam(required = false) String treeId,@ApiParam @RequestParam(required = false) String parentId) {
        List list= sysDicServiceImpl.getTopDicByTreeId(treeId);
        if(StringUtil.isNotEmpty(parentId)){
            list= sysDicServiceImpl.getByParentId(parentId);
        }
        return list;
    }

    @ApiOperation(value="保存数字字典详细信息", notes="根据提交的树JSON保存实体数据")
    @PostMapping("/save")
    @AuditLog(operation = "保存数字字典详细信息")
    @Override
    public JsonResult save(@ApiParam @RequestBody SysDic entity, BindingResult validResult) throws Exception{
        String rootParentId="0";
        if(StringUtils.isEmpty(entity.getParentId())){
            entity.setParentId(rootParentId);
        }

        boolean isNameExist= sysDicServiceImpl.isNameExist(entity);
        if(isNameExist){
            LogContext.addError("字典名系统中已存在!");
            return JsonResult.Fail("字典名系统中已存在!");
        }

        boolean isValueExist= sysDicServiceImpl.isValueExist(entity);
        if(isValueExist){
            LogContext.addError("字典值系统中已存在!");
            return JsonResult.Fail("字典值系统中已存在!");
        }

        boolean isAdd=false;
        if(StringUtils.isEmpty(entity.getDicId())){
            String pkId= IdGenerator.getIdStr();
            String path=rootParentId+"." + pkId + ".";
            entity.setPath(path);
            entity.setDicId(pkId);
            isAdd=true;
        }

        //若存在父Id，则更新父Id
        if(!rootParentId.equals(entity.getParentId())){
            //更新父Id对应的树的子节点为非树节点
            SysDic parentTree= sysDicServiceImpl.get(entity.getParentId());
            if(parentTree!=null){
                sysDicServiceImpl.update(parentTree);
                entity.setPath(parentTree.getPath() + entity.getDicId()+ ".");

            }
        }
        String opMsg="";
        if(isAdd){
            sysDicServiceImpl.insert(entity);
            LogContext.put(Audit.PK,entity.getDicId() );
            LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
            LogContext.put(Audit.DETAIL,"添加数据字典:" + entity.getName() +"("+entity.getDicId()+")");
            opMsg="成功保存树节点信息！";
        }else{

            sysDicServiceImpl.update(entity);

            LogContext.put(Audit.PK,entity.getDicId() );
            LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
            LogContext.put(Audit.DETAIL,"更新数据字典:" + entity.getName() +"("+entity.getDicId()+")");


            opMsg="成功更新树节点信息";
        }
        sysDicServiceImpl.removeCache(entity.getTreeId());
        return new JsonResult(true,entity,opMsg);
    }

    @MethodDefine(title = "按路径取得某节点下所有的节点数", path = "/getByPath", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "路径", varName = "path")})
    @ApiOperation("按路径取得某节点下所有的节点数")
    @GetMapping("/getByPath")
    public JsonResult getByPath(@ApiParam @RequestParam String path){
        List list= sysDicServiceImpl.getByLeftLikePath(path);
        JsonResult result=new JsonResult(true,"");
        result.setData(list);
        return result;
    }

    @MethodDefine(title = "删除某个节点下所有的子节点", path = "/delByDicId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "节点ID", varName = "dicId"),@ParamDefine(title = "分类ID", varName = "treeId")})
    @ApiOperation("删除某个节点下所有的子节点")
    @AuditLog(operation = "删除某个节点下所有的子节点")
    @PostMapping("delByDicId")
    public JsonResult delByDicId(@ApiParam @RequestParam(value = "dicId") String dicId,
                                 @ApiParam @RequestParam(value = "treeId") String treeId) {
        LogContext.put(Audit.DETAIL,"删除字典,ID为"+dicId  );
        SysDic sysDic=sysDicServiceImpl.getByDicId(dicId);
        sysDicServiceImpl.removeCache(sysDic.getTreeId());
        sysDicServiceImpl.delCascade(dicId);
        return new JsonResult(true, "成功删除树及树下所有节点");
    }

    @MethodDefine(title = "删除多个节点下所有的子节点", path = "/delByDicIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "节点ID集合", varName = "dicIds"),@ParamDefine(title = "分类ID", varName = "treeId")})
    @ApiOperation("删除多个节点下所有的子节点")
    @AuditLog(operation = "删除多个节点下所有的子节点")
    @PostMapping("delByDicIds")
    public JsonResult delByDicIds(@ApiParam @RequestParam(value = "dicIds") String dicIds,
                                  @ApiParam @RequestParam(value = "treeId") String treeId) {

        LogContext.put(Audit.DETAIL,"删除多个字典,ID为"+dicIds  );

        if (StringUtils.isNotEmpty(dicIds)) {
            String[] ids = dicIds.split(",");
            for (String id : ids) {
                sysDicServiceImpl.delCascade(id);
            }
        }
        sysDicServiceImpl.removeCache(treeId);
        return new JsonResult(true, "成功删除树及树下所有节点");
    }

    @MethodDefine(title = "保存数据字典", path = "/rowSave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据字典集合", varName = "jsonObject")})
    @ApiOperation("保存数据字典")
    @AuditLog(operation = "保存数据字典")
    @PostMapping("rowSave")
    public JsonResult rowSave(@RequestBody JSONObject jsonObject) {
        String treeId = jsonObject.getString("treeId");
        SysDic sysDic = JSONObject.toJavaObject(jsonObject.getJSONObject("data"), SysDic.class);
        sysDic.setTreeId(treeId);
        boolean isNameExist= sysDicServiceImpl.isNameExist(sysDic);
        if(isNameExist){
            return JsonResult.Fail("字典名系统中已存在!");
        }

        boolean isValueExist= sysDicServiceImpl.isValueExist(sysDic);
        if(isValueExist){
            return JsonResult.Fail("字典值系统中已存在!");
        }
        LogContext.put(Audit.DETAIL,"保存数据字典:" +sysDic.getName()+",保存到分类:" +treeId);
        if (StringUtils.isEmpty(sysDic.getDicId())) {
            sysDic.setDicId(IdGenerator.getIdStr());
            if (StringUtils.isNotEmpty(sysDic.getParentId())) {
                SysDic parentDic = sysDicServiceImpl.get(sysDic.getParentId());
                sysDic.setPath(parentDic.getPath() + sysDic.getDicId() + ".");
                sysDicServiceImpl.update(parentDic);
            } else {
                sysDic.setParentId("0");
                sysDic.setPath("0." + sysDic.getDicId() + ".");
            }
            sysDic.setTreeId(treeId);
            sysDicServiceImpl.insert(sysDic);
        } else {
            sysDicServiceImpl.update(sysDic);
        }
        sysDicServiceImpl.removeCache(treeId);
        return new JsonResult(true, sysDic, "成功保存数组字典-" + sysDic.getName());
    }

    @MethodDefine(title = "批量保存数据字典", path = "/batchSave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据字典集合", varName = "jsonObject")})
    @ApiOperation("批量保存数据字典")
    @AuditLog(operation = "批量保存数据字典")
    @PostMapping("batchSave")
    public JsonResult batchSave(@RequestBody JSONObject jsonObject) {
        String treeId = jsonObject.getString("treeId");
        SysTree sysTree = sysTreeServiceImpl.get(treeId);
        String gridData = jsonObject.getString("gridData");

        String detail="批量保存数据字典:" + gridData +",保存到分类:" + treeId;
        LogContext.put(Audit.DETAIL,detail);

        genDics(gridData, null, sysTree);
        sysDicServiceImpl.removeCache(treeId);
        return new JsonResult(true, "成功保存数据字典！");
    }

    /**
     * 产生数据项及上下级关系
     *
     * @param groupJson
     * @param parentDic
     * @param sysTree
     */
    private void genDics(String groupJson, SysDic parentDic, SysTree sysTree) {
        JSONArray jsonArray = JSONArray.parseArray(groupJson);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            String dicId = jsonObj.getString("dicId");
            SysDic sysDic = null;
            //是否为创建
            boolean isCreated = false;
            if (StringUtils.isEmpty(dicId)) {
                sysDic = new SysDic();
                sysDic.setDicId(IdGenerator.getIdStr());
                sysDic.setTreeId(sysTree.getTreeId());
                isCreated = true;
            } else {
                sysDic = sysDicServiceImpl.get(dicId);
            }

            if (sysDic == null) {
                continue;
            }

            String name = jsonObj.getString("name");
            String value = jsonObj.getString("value");
            String descp = jsonObj.getString("descp");
            String sn = jsonObj.getString("sn");
            String appId=jsonObj.getString("appId");
            if("".equals(sn)){
                sn="1";
            }
            sysDic.setName(name);
            sysDic.setValue(value);
            sysDic.setDescp(descp);
            sysDic.setSn(Integer.parseInt(sn));
            sysDic.setAppId(appId);

            if (parentDic == null) {
                sysDic.setParentId("0");
                sysDic.setPath("0." + sysDic.getDicId() + ".");
            } else {
                sysDic.setParentId(parentDic.getDicId());
                sysDic.setPath(parentDic.getPath() + sysDic.getDicId() + ".");
            }

            String children = jsonObj.getString("children");
            if (StringUtils.isNotEmpty(children)) {
                genDics(children, sysDic, sysTree);
            }
            boolean isNameExist= sysDicServiceImpl.isNameExist(sysDic);
            if(isNameExist){
                continue;
            }
            boolean isValueExist= sysDicServiceImpl.isValueExist(sysDic);
            if(isValueExist){
                continue;
            }
            if (isCreated) {
                sysDicServiceImpl.insert(sysDic);
            } else {
                sysDicServiceImpl.update(sysDic);
            }
        }
    }

    @MethodDefine(title = "获取字典", path = "/listByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "字典KEY", varName = "key")})
    @ApiOperation("获取字典")
    @GetMapping("/listByKey")
    public List<SysDic> listByKey(@ApiParam @RequestParam String key) {
        SysTree sysTree = sysTreeServiceImpl.getByKey(key, SysTreeCat.CAT_DIC);
        if (sysTree == null) {
            logger.error("not found the alias " + key + " as dic list");
            return new ArrayList<SysDic>();
        }
        List<SysDic> dicList = sysDicServiceImpl.getByTreeId(sysTree.getTreeId());
        return dicList;
    }

    @MethodDefine(title = "获取字典(树形)", path = "/treeByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "字典KEY", varName = "key")})
    @ApiOperation("获取字典(树形)")
    @GetMapping("/treeByKey")
    public JSONArray treeByKey(@ApiParam @RequestParam String key) {
        SysTree sysTree = sysTreeServiceImpl.getByKey(key, SysTreeCat.CAT_DIC);
        if (sysTree == null) {
            logger.error("not found the alias " + key + " as dic list");
            return new JSONArray();
        }
        List<SysDic> dicList = sysDicServiceImpl.getByTreeId(sysTree.getTreeId());
        JSONArray array= JSONArray.parseArray(JSON.toJSONString(dicList));
        JSONArray tree = BeanUtil.arrayToTree(array,"dicId","parentId");
        return tree;
    }

    @MethodDefine(title = "根据id与父id查询数据字典", path = "/getByPidAndDicId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "字典KEY", varName = "dicKey"),@ParamDefine(title = "父ID", varName = "parentId"),@ParamDefine(title = "项名", varName = "name")})
    @ApiOperation("根据id与父id查询数据字典")
    @GetMapping("/getByPidAndDicId")
    public List<SysDic> getByPidAndDicId(@ApiParam @RequestParam String dicKey,@ApiParam @RequestParam String parentId,@ApiParam @RequestParam(required = false) String name){
        SysTree sysTree= sysTreeServiceImpl.getByKey(dicKey, SysTreeCat.CAT_DIC);
        if(sysTree!=null){
            List<SysDic> dicList=new ArrayList<>();
            //过滤项名
            if(StringUtils.isNotEmpty(name)){
                dicList= sysDicServiceImpl.getByPidAndName(sysTree.getTreeId(),parentId,name);
            }else {
                dicList= sysDicServiceImpl.getByPidAndDicId(sysTree.getTreeId(),parentId);
            }
            return dicList;
        }else {
            return new ArrayList<SysDic>();
        }
    }


    @MethodDefine(title = "导出数据字典", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出数据字典")
    @AuditLog(operation = "导出数据字典")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            throw new Exception("导出失败，请选择要导出的记录。");
        }

        StringBuilder sb=new StringBuilder();
        sb.append("导出字典数据:");

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {

            JSONObject json = new JSONObject();
            SysDic sysDic =sysDicServiceImpl.get(id);
            json.put("sysDic", sysDic);
            sb.append(sysDic.getName() +"("+id+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }

        LogContext.put(Audit.DETAIL,sb.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-Dic-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入数据字典", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入数据字典")
    @ApiOperation("导入数据字典")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Sys-Dic";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        if(StringUtils.isEmpty(treeId)){
            return JsonResult.Fail("请选择一个分类！");
        }
        String appId=request.getParameter("appId");
        sysDicServiceImpl.importDic(file,treeId,appId);
        sysDicServiceImpl.removeCache(treeId);
        return JsonResult.Success().setMessage("导入成功");
    }

    @MethodDefine(title = "根据字典KEY与项值查询数据字典", path = "/getDicByDicValue", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "字典KEY", varName = "dicKey"),
                    @ParamDefine(title = "项值", varName = "dicValue")})
    @ApiOperation("根据字典KEY与项值查询数据字典")
    @GetMapping("/getDicByDicValue")
    public List<SysDic> getDicByDicValue(@ApiParam @RequestParam(value = "dicKey") String dicKey,
                                         @ApiParam @RequestParam(value = "dicValue") String dicValue){
        SysTree sysTree= sysTreeServiceImpl.getByKey(dicKey, SysTreeCat.CAT_DIC);
        if(sysTree!=null){
            return sysDicServiceImpl.getDicByDicValue(sysTree.getTreeId(),dicValue);
        }else {
            return new ArrayList<>();
        }
    }

    @MethodDefine(title = "根据分类ID与项值查询数据字典", path = "/getDicListByTreeIdDicValues", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类ID", varName = "treeId"),
                    @ParamDefine(title = "项值", varName = "dicValue")})
    @ApiOperation("根据分类ID与项值查询数据字典")
    @GetMapping("/getDicListByTreeIdDicValues")
    public List<SysDic> getDicListByTreeIdDicValues(@ApiParam @RequestParam(value = "treeId") String treeId,
                                                    @ApiParam @RequestParam(value = "dicValue") String dicValue) {
        return sysDicServiceImpl.getDicByDicValue(treeId, dicValue);
    }

    @MethodDefine(title = "获取某个节点下的子节点列表", path = "/getByTreeIdParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树ID", varName = "treeId"),@ParamDefine(title = "父节点ID", varName = "parentId")})
    @ApiOperation("获取某个节点下的子节点列表")
    @GetMapping("/getByTreeIdParentId")
    public List<SysDic> getByTreeIdParentId(@ApiParam @RequestParam(value = "treeId") String treeId,
                                                @ApiParam @RequestParam(value = "parentId",required = false) String parentId) {
        List list= sysDicServiceImpl.getByTreeIdParentId(treeId,parentId);
        return list;
    }
}
