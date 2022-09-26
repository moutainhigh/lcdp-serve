package com.redxun.system.core.controller;

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
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.sys.SysTreeDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysTree;
import com.redxun.system.core.entity.SysTreeCat;
import com.redxun.system.core.service.SysAuthRightsServiceImpl;
import com.redxun.system.core.service.SysTreeCatServiceImpl;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/system/core/sysTree")
@ClassDefine(title = "系统分类树",alias = "sysTreeController",path = "/system/core/sysTree",packages = "core",packageName = "系统管理")
@Api(tags = "系统分类树")
public class SysTreeController extends BaseController<SysTree> {

    @Autowired
    SysTreeServiceImpl sysTreeServiceImpl;
    @Autowired
    SysTreeCatServiceImpl sysTreeCatServiceImpl;
    @Autowired
    SysAuthRightsServiceImpl sysAuthRightsService;
    @Autowired
    OrgClient orgClient;

    @Override
    public BaseService getBaseService() {
        return sysTreeServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统分类树";
    }




    @MethodDefine(title = "获取某一分类树的一级所有节点列表", path = "/getTopNodesByCatKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树KEY", varName = "catKey"),@ParamDefine(title = "阅读KEY", varName = "readKey"),@ParamDefine(title = "是否管理员", varName = "isAdmin"),@ParamDefine(title = "是否使用权限", varName = "isGrant")})
    @ApiOperation("获取某一分类树的一级所有节点列表")
    @GetMapping("/getTopNodesByCatKey")
    public List<SysTree> getTopNodesByCatKey(@ApiParam @RequestParam String catKey,@ApiParam @RequestParam(value = "readKey") String readKey,
                                             @ApiParam @RequestParam(value = "isAdmin") Boolean isAdmin,@ApiParam @RequestParam(value = "isGrant") Boolean isGrant
            ,@ApiParam @RequestParam (value = "appId") String appId){
        String tenantId= ContextUtil.getCurrentTenantId();
        String companyId=ContextUtil.getComanyId();
        //不启用组织分级管理  || 启用组织分级管理并且是超管的情况
        List<SysTree> list = sysTreeServiceImpl.getTopNodesByCatKey(catKey,tenantId,appId,companyId);
        if(isGrant) {
            Map<String,JSONObject> authTreeIdMap=sysAuthRightsService.getAllRightTreeId(catKey,readKey);
            list = sysAuthRightsService.parseAuthRight(authTreeIdMap,list, readKey, isAdmin,true);
        }

        return list;
    }

    @MethodDefine(title = "获取某一分类树的一级所有节点列表", path = "/getTopNodesByCatKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "父节点ID", varName = "parentId"),@ParamDefine(title = "分类树KEY", varName = "catKey"),@ParamDefine(title = "阅读KEY", varName = "readKey"),@ParamDefine(title = "是否管理员", varName = "isAdmin"),@ParamDefine(title = "是否使用权限", varName = "isGrant")})
    @ApiOperation("获取某个节点下的子节点列表，不含下下一级的子节点")
    @GetMapping("/getByParentId")
    public List<SysTree> getByParentId(@ApiParam @RequestParam String parentId,@ApiParam @RequestParam String catKey,@ApiParam @RequestParam(value = "readKey") String readKey,@ApiParam @RequestParam(value = "isAdmin") Boolean isAdmin,@ApiParam @RequestParam(value = "isGrant") Boolean isGrant){
        String companyId=ContextUtil.getComanyId();
        List<SysTree>    list = sysTreeServiceImpl.getByParentId(parentId,companyId);
        if(isGrant) {
            Map<String,JSONObject> authTreeIdMap=sysAuthRightsService.getAllRightTreeId(catKey,readKey);
            list = sysAuthRightsService.parseAuthRight(authTreeIdMap,list, readKey, isAdmin,false);
        }
        return list;
    }

    @MethodDefine(title = "获取某个节点下的子节点列表", path = "/getByCatKeyAndParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("获取某个节点下的子节点列表")
    @GetMapping("/getByCatKeyAndParentId")
    public List<SysTree> getByCatKeyAndParentId(HttpServletRequest request) {
        String catKey= RequestUtil.getString(request,"catKey");
        String parentId= RequestUtil.getString(request,"parentId");
        String appId= RequestUtil.getString(request,"appId");
        String getAll= RequestUtil.getString(request,"getAll");
        String tenantId=ContextUtil.getCurrentTenantId();
        String companyId=ContextUtil.getComanyId();
        List<SysTree> list=null;
        if(StringUtil.isNotEmpty(parentId)){
            list= sysTreeServiceImpl.getByParentId(parentId,companyId);
        }else if(StringUtils.isNotEmpty(getAll)){
            list= sysTreeServiceImpl.getNodesByCatKey(catKey,tenantId,appId,companyId);
        }else{
            list= sysTreeServiceImpl.getTopNodesByCatKey(catKey,tenantId,appId,companyId);
        }
        return list;
    }

    @MethodDefine(title = "获取某分类下的所有树节点，含下下一级的子节点", path = "/getByCatKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "分类树KEY", varName = "catKey"),@ParamDefine(title = "阅读KEY", varName = "readKey"),@ParamDefine(title = "是否管理员", varName = "isAdmin"),@ParamDefine(title = "是否使用权限", varName = "isGrant")})
    @ApiOperation("获取某分类下的所有树节点，含下下一级的子节点")
    @GetMapping("/getByCatKey")
    public List<SysTreeDto> getByCatKey(@ApiParam @RequestParam(value = "catKey") String catKey,
                                        @ApiParam @RequestParam(value = "readKey") String readKey,@ApiParam @RequestParam(value = "isAdmin") Boolean isAdmin,
                                        @ApiParam @RequestParam(value = "isGrant")Boolean isGrant, @ApiParam @RequestParam(value = "appId",required = false)String appId){
        String tenantId=ContextUtil.getCurrentTenantId();
        //不启用组织分级管理  || 启用组织分级管理并且是超管的情况
        List<SysTree> list =sysTreeServiceImpl.getByCatKey(catKey,tenantId,appId,false);
        if(isGrant) {
            Map<String,JSONObject> authTreeIdMap=sysAuthRightsService.getAllRightTreeId(catKey,readKey);
            list = sysAuthRightsService.parseAuthRight(authTreeIdMap,list, readKey, isAdmin,false);
        }

        List<SysTreeDto> sysTreeDtos = (List<SysTreeDto>)BeanUtil.copyList(list, SysTreeDto.class);
        return sysTreeDtos;
    }

    @ApiOperation(value="保存树详细信息", notes="根据提交的树JSON保存实体数据")
    @PostMapping("/save")
    @AuditLog(operation = "保存分类的信息")
    @Override
    public JsonResult save(@ApiParam @RequestBody SysTree entity, BindingResult validResult) throws Exception{
        String rootParentId="0";
        if(StringUtils.isEmpty(entity.getParentId())){
            entity.setParentId(rootParentId);
        }
        boolean isExist= sysTreeServiceImpl.isExist(entity);
        if(isExist){
            LogContext.addError("KEY:"+entity.getAlias()+"已存在,请修改KEY值再保存!");
            return  JsonResult.Fail("KEY已存在,请修改KEY值再保存!");
        }


        boolean isAdd=false;
        if(StringUtils.isEmpty(entity.getTreeId())){
            String pkId= IdGenerator.getIdStr();
            String path=rootParentId+"." + pkId + ".";
            entity.setPath(path);
            entity.setTreeId(pkId);
            isAdd=true;
        }

        //若存在父Id，则更新父Id
        if(!rootParentId.equals(entity.getParentId())){
            //更新父Id对应的树的子节点为非树节点
            SysTree parentTree= sysTreeServiceImpl.get(entity.getParentId());
            if(parentTree!=null){
                sysTreeServiceImpl.update(parentTree);
                entity.setPath(parentTree.getPath() + entity.getTreeId()+ ".");

            }
        }
        StringBuilder sb=new StringBuilder();
        String opMsg="";
        if(isAdd){
            sysTreeServiceImpl.insert(entity);
            sb.append("添加分类:" + entity.getName() +"("+entity.getTreeId()+")");
            opMsg="成功保存树节点信息！";
        }else{
            sysTreeServiceImpl.update(entity);
            sb.append("更新分类:" + entity.getName() +"("+entity.getTreeId()+")");
            opMsg="成功更新树节点信息";
        }
        LogContext.put(Audit.DETAIL,sb.toString());

        return new JsonResult(true,entity,opMsg);
    }

    @MethodDefine(title = "取得某节点下所有的节点数", path = "/getChildCounts", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "父节点ID", varName = "parentId")})
    @ApiOperation("取得某节点下所有的节点数")
    @GetMapping("/getChildCounts")
    public JsonResult getChildCounts(@ApiParam @RequestParam String parentId){
        Long counts= sysTreeServiceImpl.getChildCounts(parentId);
        JsonResult result= new JsonResult(true,"");
        result.setData(counts);
        return result;
    }

    @MethodDefine(title = "按路径取得某节点下所有的节点数", path = "/getByPath", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "路径", varName = "path")})
    @ApiOperation("按路径取得某节点下所有的节点数")
    @GetMapping("/getByPath")
    public JsonResult getByPath(@ApiParam @RequestParam String path){
        List list= sysTreeServiceImpl.getByLeftLikePath(path);
        JsonResult result=new JsonResult(true,"");
        result.setData(list);
        return result;
    }

    @MethodDefine(title = "删除某个节点下所有的子节点", path = "/delByTreeId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "树ID", varName = "treeId")})
    @ApiOperation("删除某个节点下所有的子节点")
    @AuditLog(operation = "删除某个节点")
    @PostMapping("delByTreeId")
    public JsonResult delByTreeId(@ApiParam @RequestParam String treeId){
        LogContext.put(Audit.DETAIL,"删除分类,分类ID:" + treeId);
        sysTreeServiceImpl.delCascade(treeId);
        return new JsonResult(true, "成功删除树及树下所有节点");
    }

    @MethodDefine(title = "根据树ID获取树信息", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId")})
    @ApiOperation("根据树ID获取树信息")
    @GetMapping("getById")
    public JsonResult getById(@ApiParam @RequestParam(value = "pkId") String treeId) {
        SysTree sysTree = sysTreeServiceImpl.getById(treeId);
        JsonResult result = JsonResult.Success();
        result.setData(sysTree);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "通过别名获取系统树", path = "/getByAliasCatKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias"),@ParamDefine(title = "分类树KEY", varName = "catKey")})
    @ApiOperation("通过别名获取系统树")
    @GetMapping("getByAliasCatKey")
    public JsonResult getByAlias(@ApiParam @RequestParam(value = "alias") String alias, @ApiParam @RequestParam(value = "catKey") String catKey) {
        SysTree sysTree = sysTreeServiceImpl.getByKey(alias, catKey);
        JsonResult result = JsonResult.Success();
        result.setData(sysTree);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "显示数据字典下的分类", path = "/listDicTree", method = HttpMethodConstants.GET)
    @ApiOperation("显示数据字典下的分类")
    @GetMapping("/listDicTree")
    public List<SysTree> listDicTree(@ApiParam @RequestParam(value = "appId",required = false)String appId) throws Exception {
        String tenantId=ContextUtil.getCurrentTenantId();
        List<SysTree> treeList= sysTreeServiceImpl.getByCatKey(SysTreeCat.CAT_DIC,tenantId,appId,true);
        return treeList;
    }


    @MethodDefine(title = "导出分类数据", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出分类数据")
    @AuditLog(operation = "导出分类数据")
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
            JSONObject json= sysTreeServiceImpl.doExportById(id);
            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-Tree-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入系统树", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @ApiOperation("导入系统树")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Sys-Tree";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        String appId=request.getParameter("appId");
        List<AlterSql> delaySqlList = sysTreeServiceImpl.importSysTreeZip(file,treeId,appId);
        return JsonResult.Success().setData(delaySqlList).setMessage("导入成功");
    }

    /**
     * 迁移菜单
     * @return
     */
    @MethodDefine(title = "迁移分类", path = "/toMoveTree", method = HttpMethodConstants.GET)
    @ApiOperation("迁移分类")
    @AuditLog(operation = "迁移分类")
    @PostMapping("/toMoveTree")
    public JsonResult toMoveTree(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String treeIds = RequestUtil.getString(request,"treeIds");
        String targetId = RequestUtil.getString(request,"targetId");



        String detail="将分类:"+ treeIds +"转移到目标ID下:" + targetId;

        LogContext.put(Audit.DETAIL,detail);

        String[] treeIdList = treeIds.split(",");
        //迁移到目标分类下
        SysTree parentTree =null;
        if(!"0".equals(targetId)){
            parentTree = sysTreeServiceImpl.get(targetId);
        }else {
            parentTree=new SysTree();
            parentTree.setTreeId("0");
            parentTree.setPath("0.");
        }
        for(int i=0;i<treeIdList.length;i++) {
            String treeId = treeIdList[i];
            //获得迁移的分类
            SysTree tree = sysTreeServiceImpl.get(treeId);

            //把当前分类下子分类一起迁移
            tree.setParentId(parentTree.getTreeId());
            tree.setPath(parentTree.getPath() + treeId + ".");
            sysTreeServiceImpl.update(tree);
            //递归其下所有子分类，并更新其路径
            cascadeUpdateSubTree(tree);
        }

        return new JsonResult(true,"修改成功");
    }

    private void cascadeUpdateSubTree(SysTree parentTree){
        List<SysTree> subTrees=sysTreeServiceImpl.getByParentId(parentTree.getTreeId(),parentTree.getCompanyId());
        for(SysTree tree:subTrees){
            tree.setParentId(parentTree.getTreeId());
            tree.setPath(parentTree.getPath()+tree.getTreeId()+".");
            tree.setAppId(parentTree.getAppId());
            sysTreeServiceImpl.update(tree);
            //递归其下所有子分类，并更新其路径
            cascadeUpdateSubTree(tree);
        }
    }
}
