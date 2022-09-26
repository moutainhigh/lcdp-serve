package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.excel.EasyExcelUtil;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.Result;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.profile.ProfileContext;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.service.*;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统用户组 提供者
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osGroup")
@ClassDefine(title = "系统用户组",alias = "osGroupController",path = "/user/org/osGroup",packages = "org",packageName = "组织架构")
@Api(tags = "系统用户组")
@ContextQuerySupport
public class OsGroupController extends BaseController<OsGroup> {

    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;
    @Autowired
    OsRelTypeServiceImpl osRelTypeServiceImpl;
    @Autowired
    OsRelInstServiceImpl osRelInstServiceImpl;
    @Autowired
    OsPropertiesValServiceImpl osPropertiesValService;
    @Autowired
    OsPropertiesDefServiceImpl osPropertiesDefService;
    @Resource
    private OsGradeAdminServiceImpl osGradeAdminService;
    @Resource
    private OsGradeRoleServiceImpl osGradeRoleService;

    @Resource
    private OsDimensionServiceImpl osDimensionService;
    @Resource
    private OsCompanyAuthServiceImpl osCompanyAuthService;

    @Override
    public BaseService getBaseService() {
        return osGroupServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统用户组";
    }

    @PostMapping("/getGroupProperty")
    public JsonResult getGroupProperty(@RequestBody JSONObject json){
        String groupId=json.getString("groupId");
        String attrName=json.getString("attrName");
        OsGroup osGroup=osGroupServiceImpl.getById(groupId);
        Object value=BeanUtil.getFieldValueFromObject(osGroup,attrName);
        if(value==null){
            List<OsPropertiesVal> list=osPropertiesValService.getByOwnerId(groupId);
            for(OsPropertiesVal osPropertiesVal:list){
                OsPropertiesDef osPropertiesDef=osPropertiesDefService.getById(osPropertiesVal.getProperyId());
                if(osPropertiesDef!=null && osPropertiesDef.getName().equals(attrName)){
                    String dataType=osPropertiesDef.getDataType();
                    if("varchar".equals(dataType)){
                        value = osPropertiesVal.getTxtVal();
                    }else if("number".equals(dataType)){
                        value = osPropertiesVal.getNumVal();
                    }else if("date".equals(dataType)){
                        value = osPropertiesVal.getDateVal();
                    }
                }
            }
        }
        return JsonResult.getSuccessResult(value);
    }

    @AuditLog(operation = "保存用户组")
    @ApiOperation("保存用户组")
    @Override
    public JsonResult save(@ApiParam @RequestBody OsGroup entity, BindingResult validResult) throws Exception{
        JsonResult jsonResult;
        if(StringUtils.isNotBlank(entity.getKey())){
            boolean isExist = osGroupServiceImpl.isExist(entity);
            if(isExist){
                return JsonResult.Fail("组KEY不能重复!");
            }
        }



        boolean isAddEntity=false;
        String groupId =entity.getGroupId();
        if(StringUtils.isEmpty(groupId)){
            groupId= IdGenerator.getIdStr();
            entity.setGroupId(groupId);
            isAddEntity=true;


        }
        if (StringUtils.isBlank(entity.getKey())){
            entity.setKey(groupId);
        }
        if("0".equals(entity.getParentId())){
            entity.setPath("0."+groupId+".");
        }else {
            OsGroup parentGroup = osGroupServiceImpl.get(entity.getParentId());
            if (BeanUtil.isNotEmpty(parentGroup)) {
                entity.setPath(parentGroup.getPath() + groupId + ".");
            }
        }

        String str="";
        if(isAddEntity){
            osGroupServiceImpl.insert(entity);
            // 记录日志
            LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
            String detail= EntityUtil.getInfo(entity,true);
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,entity.getGroupId());

            str="添加" + getComment() + "成功";
        }else {
            OsGroup oldGroup= osGroupServiceImpl.get(entity.getGroupId());
            // 记录日志
            LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
            String detail= EntityUtil.getUpdInfo(oldGroup,entity);
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,entity.getGroupId());

            osGroupServiceImpl.update(entity);
            str="更新" + getComment() + "成功";
        }
        jsonResult=JsonResult.getSuccessResult(str);
        jsonResult.setData(entity);
        return jsonResult;
    }



    @MethodDefine(title = "按条件查询所有记录", path = "/queryByDimId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID", varName = "dimId")})
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @GetMapping(value="/queryByDimId")
    public JsonResult queryByDimId(@RequestParam String dimId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("返回数据成功!");
        try{
            String tenantId=ContextUtil.getCurrentTenantId();
            List<OsGroup> list = osGroupServiceImpl.getByDimId(tenantId,dimId);
            jsonResult.setData(list).setShow(false);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "根据用户Id获取主部门", path = "/getMainDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId"),@ParamDefine(title = "机构ID", varName = "tenantId")})
    @ApiOperation("根据用户Id获取主部门")
    @GetMapping("/getMainDeps")
    public OsGroupDto getMainDeps(@ApiParam @RequestParam String userId,@ApiParam @RequestParam String tenantId) {
        OsGroup osGroup= osGroupServiceImpl.getMainDeps(userId,tenantId);
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isEmpty(osGroup)) {
            return null;
        }
        BeanUtil.copyProperties(osGroupDto, osGroup);
        return osGroupDto;
    }

    @MethodDefine(title = "取得用户从属于的用户组列表", path = "/getBelongGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation("取得用户从属于的用户组列表")
    @GetMapping("/getBelongGroups")
    public List<OsGroupDto>  getBelongGroups(@ApiParam @RequestParam String userId) {
        List<OsGroup> belongGroups = osGroupServiceImpl.getBelongGroups(userId);
        List<OsGroupDto> groupDtos=getByOsGroups(belongGroups);
        return groupDtos;
    }

    @MethodDefine(title = "获取用户组的上级用户组", path = "/getUpGroup", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组ID", varName = "groupId")})
    @ApiOperation("获取用户组的上级用户组")
    @GetMapping("/getUpGroup")
    public OsGroupDto getUpGroup(@ApiParam @RequestParam("groupId") String groupId){
        OsGroup osGroup=osGroupServiceImpl.get(groupId);
        //无上级
        if(StringUtils.isEmpty(osGroup.getParentId()) || "0".equals(osGroup.getParentId())) {
            return null;
        }
        OsGroup parentGroup=osGroupServiceImpl.get(osGroup.getParentId());
        if(parentGroup==null){
            return null;
        }
        OsGroupDto dto = new OsGroupDto();
        BeanUtil.copyProperties(dto, parentGroup);
        return dto;
    }

    @MethodDefine(title = "根据当前用户组（往上查找符合等级的）的上级用户组", path = "/getUpRankGroup", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组ID", varName = "groupId"),@ParamDefine(title = "用户组等级值", varName = "rankLevel")})
    @ApiOperation("根据当前用户组（往上查找符合等级的）的上级用户组")
    @GetMapping("/getUpRankLevelGroup")
    public OsGroupDto getUpRankLevelGroup(@ApiParam @RequestParam("groupId") String groupId,@ApiParam @RequestParam("rankLevel") Integer rankLevel){
        OsGroup osGroup=osGroupServiceImpl.get(groupId);
        OsGroup parentGroup=getUpupGroups(osGroup,rankLevel);
        if(parentGroup!=null) {
            OsGroupDto dto = new OsGroupDto();
            BeanUtil.copyProperties(dto, parentGroup);
            return dto;
        }
        return  null;
    }

    /**
     * 获取符合等级的上级用户组
     * @param osGroup
     * @param rankLevel
     * @return
     */
    private OsGroup getUpupGroups(OsGroup osGroup,Integer rankLevel){
        if(StringUtils.isEmpty(osGroup.getParentId()) ||  "0".equals(osGroup.getParentId())) {
            return null;
        }
        OsGroup parentGroup=osGroupServiceImpl.get(osGroup.getParentId());
        if(parentGroup==null){
            return null;
        }
        if(parentGroup.getRankLevel().equals(rankLevel) ){
            return parentGroup;
        }else{
            return getUpupGroups(parentGroup,rankLevel);
        }
    }

    @MethodDefine(title = "取得用户从属的部门组", path = "/getBelongDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation("取得用户从属的部门组")
    @GetMapping("/getBelongDeps")
    public List<OsGroupDto>  getBelongDeps(@ApiParam @RequestParam String userId) {
        List<OsGroup> belongGroups = osGroupServiceImpl.getBelongDeps(userId);
        List<OsGroupDto> groupDtos=getByOsGroups(belongGroups);
        return groupDtos;
    }

    /**
     * 根据用户组转标准对外用户组
     * @param osGroup
     * @return
     */
    private OsGroupDto getByOsGroup(OsGroup osGroup){
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isNotEmpty(osGroup)) {
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    /**
     * 用户组数据转对外标准用户组
     * @param osGroups
     * @return
     */
    private List<OsGroupDto> getByOsGroups(List<OsGroup> osGroups){
        List<OsGroupDto> dtoList=new ArrayList<>();
        for(OsGroup osGroup:osGroups){
            OsGroupDto dto= getByOsGroup(osGroup);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @MethodDefine(title = "通过组ID获取用户组信息", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId")})
    @ApiOperation("通过组ID获取用户组信息")
    @GetMapping("/getById")
    public OsGroupDto getById(@ApiParam @RequestParam String groupId) {
        OsGroup osGroup = osGroupServiceImpl.getById(groupId);
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isNotEmpty(osGroup)) {
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    @MethodDefine(title = "通过组ID获取用户组信息", path = "/getGroupByIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "getGroupByIds")})
    @ApiOperation("通过组ID获取用户组信息")
    @GetMapping("/getGroupByIds")
    public List< OsGroupDto> getGroupByIds(@ApiParam @RequestParam(value = "groupIds") String groupIds) {
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        String[] groups=groupIds.split(",");
        List<String> groupList = Arrays.asList(groups);
        List<OsGroup> osGroupList = osGroupServiceImpl.getByGroupIds(groupList,deleted);

        List<OsGroupDto> groupDtos=new ArrayList<>();

        for(OsGroup group:osGroupList){
            OsGroupDto osGroupDto = new OsGroupDto();
            if(BeanUtil.isNotEmpty(osGroupDto)) {
                BeanUtil.copyProperties(osGroupDto, group);
            }
            groupDtos.add(osGroupDto);
        }

        return groupDtos;
    }



    @MethodDefine(title = "通过组KEY获取用户组信息", path = "/getByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组KEY", varName = "groupKey")})
    @ApiOperation("通过组KEY获取用户组信息")
    @GetMapping("/getByKey")
    public OsGroupDto getByKey(@ApiParam @RequestParam String groupKey){
        OsGroup osGroup = osGroupServiceImpl.getByKey(groupKey);
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isNotEmpty(osGroup)) {
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    @MethodDefine(title = "通过组KEY获取用户组信息", path = "/getByKeyTenantId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组KEY", varName = "groupKey"),@ParamDefine(title = "机构ID", varName = "tenantId")})
    @ApiOperation("通过组KEY获取用户组信息")
    @GetMapping("/getByKeyTenantId")
    public OsGroupDto getByKeyTenantId(@ApiParam @RequestParam("groupKey") String groupKey,
                                       @ApiParam @RequestParam("tenantId") String tenantId){
        OsGroup osGroup = osGroupServiceImpl.getByKey(groupKey,tenantId);
        OsGroupDto osGroupDto = null;
        if(BeanUtil.isNotEmpty(osGroup)) {
            osGroupDto = new OsGroupDto();
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    @MethodDefine(title = "获取用户组直属下级用户组ID集合", path = "/getDdownDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId")})
    @ApiOperation("获取用户组直属下级用户组ID集合")
    @GetMapping("/getDdownDeps")
    public List<String> getDdownDeps(@ApiParam @RequestParam("groupId")String groupId){
        List<String> groupIds=new ArrayList<>();
        List<OsGroup> list= osGroupServiceImpl.getByParentId(groupId,null);
        for(OsGroup osGroup:list){
            groupIds.add(osGroup.getGroupId());
        }
        return groupIds;
    }

    @MethodDefine(title = "获取用户组所有下级用户组ID集合", path = "/getDownDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId")})
    @ApiOperation("获取用户组所有下级用户组ID集合")
    @GetMapping("/getDownDeps")
    public List<String> getDownDeps(@ApiParam @RequestParam("groupId")String groupId){
        List<String> groupIds=new ArrayList<>();
        OsGroup group= osGroupServiceImpl.getById(groupId);
        //若当前的目录路径为空,则让他找不到数据
        String groupPath= StringUtils.isEmpty(group.getPath())?"-1":group.getPath();
        List<OsGroup> list= osGroupServiceImpl.getByPath(groupPath);
        for(OsGroup osGroup:list){
            groupIds.add(osGroup.getGroupId());
        }
        return groupIds;
    }

    @MethodDefine(title = "获取用户组所有上级用户组名称集合", path = "/getUpDepNames", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "接口数据", varName = "json")})
    @ApiOperation("获取用户组所有上级用户组名称集合")
    @PostMapping("/getUpDepNames")
    public List<String> getUpDepNames(@ApiParam @RequestBody JSONObject json){
        String groupIds=json.getString("groupIds");
        Integer level=json.getInteger("level");
        List<String> groupList=new ArrayList<>();
        String[] groupArr=groupIds.split(",");
        for(String groupId:groupArr) {
            OsGroup group = osGroupServiceImpl.getById(groupId);
            if(group==null){
                continue;
            }

            List<String> groups=new ArrayList<>();
            if (level == null || level == 0) {
                if (StringUtils.isNotEmpty(group.getPath())) {
                    String path = group.getPath();
                    String[] arr = path.split("[.]");
                    for (String id : arr) {
                        if("0".equals(id)){
                            continue;
                        }
                        OsGroup parentGroup = osGroupServiceImpl.getById(id);
                        groups.add(parentGroup != null ? parentGroup.getName() : null);
                    }
                }
            } else {
                OsGroup parentGroup;
                for (int i = 0; i < level; i++) {
                    if("0".equals(group.getParentId())){
                        continue;
                    }
                    parentGroup = osGroupServiceImpl.getById(group.getParentId());
                    groups.add(parentGroup.getName());
                }
                groups.add(group.getName());
            }
            groupList.add(StringUtils.join(groups,"/"));

        }
        return groupList;
    }

    /**
     * 门户根据组织ID获取组织
     */
    @MethodDefine(title = "门户根据组织ID获取组织", path = "/getGroupObj", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId")})
    @ApiOperation(value = "门户根据组织ID获取组织")
    @GetMapping("/getGroupObj")
    public JSONObject getGroupObj(@RequestParam String groupId) {
        JSONObject result = new JSONObject();
        OsGroup group  = osGroupServiceImpl.get(groupId);
        if(group!=null && StringUtils.isNotEmpty(group.getName())){
            result.put("groupId",groupId);
            result.put("name",group.getName());
            result.put("key",group.getKey());
        }
        return result;
    }


    /**
     * 根据条件获取用户组。
     * 参数说明:
     * <pre>
     *     dimId:维度ID
     *     initRankLevel:用户等级
     *     groupIds:指定的用户组,参数格式:["1","2"]
     * </pre>
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "查询维度列表", path = "/getGroupListByDimId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "维度ID", varName = "dimId"),@ParamDefine(title = "维度等级", varName = "initRankLevel"),
                    @ParamDefine(title = "查询的组织ID", varName = "groupIds")})
    @ApiOperation(value = "查询维度列表")
    @PostMapping("/getGroupListByDimId")
    public Result getDimList(@RequestBody JSONObject jsonObject) {
        //传入维度ID
        String dimId=jsonObject.getString("dimId");
        //传入指定的组 ["1","2"]
        String groupIds=jsonObject.getString("groups");
        Integer initRankLevel=jsonObject.getInteger("initRankLevel");
        String tenantId=getCurrentTenantId();
        JPaasUser jPaasUser= (JPaasUser) ContextUtil.getCurrentUser();

        //1.处理用户等级
        if(initRankLevel!=null) {
            return handRankLevel(initRankLevel);
        }
        //2.处理指定的用户组
        if(StringUtils.isNotEmpty(groupIds)){
            return handGroups(groupIds);
        }
        List<OsGroup> groups=osGroupServiceImpl.getByDimId(tenantId,dimId,"0",jPaasUser.getCompanyId());

        return Result.succeed(groups);
    }

    /**
     * 处理组织等级的情况。
     * @param initRankLevel
     * @return
     */
    private Result handRankLevel(Integer initRankLevel){
        JPaasUser jPaasUser= (JPaasUser) ContextUtil.getCurrentUser();
        String depId=jPaasUser.getDeptId();
        OsGroup group= osGroupServiceImpl.getByRankLevel(depId,initRankLevel);
        List<OsGroup> groups=new ArrayList<>();
        if(group!=null){
            groups.add(group);
        }
        return Result.succeed(groups);
    }

    /**
     * 根据ID查询。
     * @param groupIds ["1","2"]
     * @return
     */
    private Result handGroups(String groupIds){
        QueryWrapper queryWrapper=new QueryWrapper<OsGroup>()
                .orderByDesc("SN_");
        List<String> groupIdList = JSON.parseArray(groupIds, String.class);
        queryWrapper.in("GROUP_ID_", groupIdList);
        List groups = osGroupServiceImpl.list(queryWrapper);
        return Result.succeed(groups);
    }



    /**
     * 根据维度ID查询分组列表
     */
    @MethodDefine(title = "查询行政维度下的部门", path = "/getAdminOrg", method = HttpMethodConstants.GET)
    @ApiOperation(value = "查询行政维度下的部门")
    @GetMapping("/getAdminOrg")
    public Result getAdminOrg() throws IOException {
        String tenantId=ContextUtil.getCurrentTenantId();

        String companyId=ContextUtil.getComanyId();

        List<OsGroup> groups = osGroupServiceImpl.getByDimId(tenantId, OsDimension.DIM_ADMIN_ID, "0", companyId);

        return Result.succeed(groups);

    }



    @MethodDefine(title = "父节点切换", path = "/changeParent", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "父ID", varName = "parentId")})
    @ApiOperation(value = "父节点切换")
    @GetMapping("changeParent")
    @AuditLog(operation = "用户组转移")
    public JsonResult changeParent(@ApiParam @RequestParam String groupId,@ApiParam @RequestParam String parentId){
        osGroupServiceImpl.changeParent(groupId,parentId);
        return JsonResult.getSuccessResult("成功进行父节点切换") ;
    }

    @MethodDefine(title = "查看下级的用户组", path = "/getByParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "父ID", varName = "parentId"),@ParamDefine(title = "维度等级", varName = "initRankLevel")})
    @ApiOperation(value = "查看下级的用户组")
    @GetMapping("getByParentId")
    public List<OsGroup> getByParentId(@ApiParam @RequestParam String parentId,@ApiParam @RequestParam(required = false) Integer initRankLevel){
        return osGroupServiceImpl.getByParentId(parentId,initRankLevel);
    }

    @MethodDefine(title = "查看指定用户组的列表", path = "/getOsGroupByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "维度等级", varName = "initRankLevel")})
    @ApiOperation(value = "查看指定用户组的列表")
    @GetMapping("getOsGroupByGroupId")
    public Set<OsGroup> getOsGroupByGroupId(@ApiParam @RequestParam String groupId,@ApiParam @RequestParam(required = false) Integer initRankLevel){
        if("curOrg".equals(groupId)){
            IUser user= ContextUtil.getCurrentUser();
            if(user!=null){
                groupId=user.getDeptId();
            }
        }
        String[] groupIds=groupId.split(",");
        Set<OsGroup> groupSet=new HashSet<>();
        for(String gId:groupIds){
            groupSet.addAll(osGroupServiceImpl.getOsGroupByGroupId(gId,initRankLevel));
        }
        return groupSet;
    }

    @MethodDefine(title = "获取排除指定用户组ID列表", path = "/getOsGroupByExcludeGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "维度ID", varName = "dimId"),@ParamDefine(title = "父ID", varName = "parentId")})
    @ApiOperation(value = "获取排除指定用户组ID列表")
    @GetMapping("getOsGroupByExcludeGroupId")
    public List<OsGroup> getOsGroupByExcludeGroupId(@ApiParam @RequestParam(value = "groupId") String groupId,
                                                    @ApiParam @RequestParam(value = "dimId") String dimId,
                                                    @ApiParam @RequestParam(value = "parentId") String parentId,
                                                    @ApiParam @RequestParam(value = "initRankLevel",required = false) Integer initRankLevel,
                                                    @ApiParam @RequestParam(value = "tenantId",required = false) String tenantId){
        if(StringUtils.isEmpty(tenantId)){
            tenantId=getCurrentTenantId();
        }
        if(initRankLevel!=null){
            List<OsGroup> list=osGroupServiceImpl.getByDimId(tenantId,dimId);
            return list.stream().filter(item -> initRankLevel.equals(item.getRankLevel()) &&!groupId.equals(item.getGroupId())).collect(Collectors.toList());
        }
        return osGroupServiceImpl.getOsGroupByExcludeGroupId(groupId,dimId,parentId,tenantId);
    }

    @MethodDefine(title = "为用户组按关系关联用户组", path = "/joinGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "关联用户组ID", varName = "groupIds"),@ParamDefine(title = "关系定义ID", varName = "relTypeId")})
    @ApiOperation(value="为用户组按关系关联用户组")
    @PostMapping("/joinGroups")
    @AuditLog(operation = "创建用户组关系")
    public JsonResult joinGroups(@ApiParam @RequestParam(value ="groupId", required = false) String groupId,
                                 @ApiParam @RequestParam(value ="groupIds", required = false) String groupIds,
                                 @ApiParam @RequestParam(value = "relTypeId",required = false) String relTypeId){
        String[] gIds = groupIds.split("[,]");
        String tenantId=getCurrentTenantId();
        OsRelType osRelType= osRelTypeServiceImpl.get(relTypeId);
        OsGroup mainGroup = osGroupServiceImpl.get(groupId);
        StringBuilder detail=new StringBuilder( "给用户组:" + mainGroup.getName() +"("+mainGroup.getGroupId()+")和用户组:");

        for(String gId : gIds){
            OsRelInst inst1 = osRelInstServiceImpl.getByParty1Party2RelTypeId(groupId,gId,relTypeId);
            if(inst1!=null){
                continue;
            }
            OsRelInst inst = new OsRelInst();
            inst.setParty1(groupId);
            inst.setParty2(gId);
            inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelTypeId(relTypeId);
            inst.setRelType(osRelType.getRelType());
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setIsMain(MBoolean.NO.name());
            inst.setTenantId(tenantId);

            OsGroup subGroup = osGroupServiceImpl.get(gId);

            detail.append(subGroup.getName() +"("+subGroup.getGroupId()+"),");

            if(mainGroup!=null && subGroup!=null){
                inst.setAlias(mainGroup.getName()+"-"+subGroup.getName());
                inst.setDim1(mainGroup.getDimId());
                inst.setDim2(subGroup.getDimId());
            }
            osRelInstServiceImpl.insert(inst);
        }
        detail.append("添加:[" + osRelType.getName() +"]关系");

        //日志
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.getSuccessResult("成功加入！");
    }

    @MethodDefine(title = "查看用户组策略", path = "/getCurrentProfile", method = HttpMethodConstants.GET)
    @ApiOperation(value = "查看用户组策略")
    @GetMapping("getCurrentProfile")
    public Map<String,Set<String>> getCurrentProfile(){
        return ProfileContext.getCurrentProfile();
    }

    @MethodDefine(title = "获取处理器列表", path = "/getOsUserGroupHanderList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取处理器列表")
    @GetMapping("getOsUserGroupHanderList")
    public JSONArray getOsUserGroupHanderList(){
        return ProfileContext.getOsUserGroupHanderList();
    }

    @MethodDefine(title = "根据维度id与父节点id查询组织", path = "/queryGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID", varName = "dimId"),
                    @ParamDefine(title = "父节点ID", varName = "parentId")})
    @ApiOperation(value = "根据维度id与父节点id查询组织")
    @GetMapping(value="/queryGroups")
    public JsonResult queryGroups(@RequestParam(value = "dimId") String dimId,
                                  @RequestParam(value = "parentId") String parentId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("返回数据成功!");
        String  tenantId=getCurrentTenantId();
        try{
            String companyId=ContextUtil.getComanyId();
            List<OsGroup> list=osGroupServiceImpl.getByDimId(tenantId,dimId,parentId,companyId);
            jsonResult.setData(list).setShow(false);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "查询用户组", path = "/searchGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "参数", varName = "params")})
    @ApiOperation("查询用户组")
    @PostMapping("/searchGroups")
    public List<OsGroup> searchGroups( @RequestBody QueryData queryData){
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        handleFilter(filter);
        return getBaseService().queryList(filter);
    }


    @MethodDefine(title = "获取用户组所有下级用户组ID集合", path = "/getGroupsByUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId"),@ParamDefine(title = "机构ID", varName = "tenantId")})
    @ApiOperation("获取用户组所有下级用户组ID集合")
    @GetMapping("/getGroupsByUser")
    public List<String> getGroupsByUser(@ApiParam @RequestParam("userId")String userId,
                                   @ApiParam @RequestParam("tenantId")String tenantId){
        List<String> groupIds = osGroupServiceImpl.getGroupsByUser(userId, tenantId);
        return groupIds;
    }

    @MethodDefine(title = "查看下级的用户组与用户", path = "/getGroupAndUserByParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "父ID", varName = "parentId"),@ParamDefine(title = "维度ID", varName = "dimId")})
    @ApiOperation(value = "查看下级的用户组与用户")
    @GetMapping("getGroupAndUserByParentId")
    public JsonResult getGroupAndUserByParentId(@ApiParam @RequestParam String parentId,@ApiParam @RequestParam(required = false) String dimId){
        String tenantId=getCurrentTenantId();
        JsonResult jsonResult =osGroupServiceImpl.getGroupAndUserByParentId(parentId,dimId,tenantId);
        return jsonResult;
    }


    @MethodDefine(title = "查找某个维度下的某个用户组", path = "/getByDimCodeGroupKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度Key", varName = "dimKey"),@ParamDefine(title = "用户组Key", varName = "groupKey")})
    @ApiOperation(value = "查找某个维度下的某个用户组")
    @GetMapping("getByDimCodeGroupKey")
    public OsGroupDto getByDimKeyGroupKey(@ApiParam @RequestParam("dimKey") String dimKey,@ApiParam @RequestParam("groupKey") String groupKey){
        OsDimension osDimension=osDimensionService.getByCodeTenantId(dimKey,ContextUtil.getCurrentTenantId());
        if(osDimension==null){
            return null;
        }
        OsGroup osGroup=osGroupServiceImpl.getByDimIdGroupKey(osDimension.getDimId(),groupKey,ContextUtil.getCurrentTenantId());
        OsGroupDto dto=new OsGroupDto();
        BeanUtil.copyProperties(dto, osGroup);
        return dto;
    }


    @MethodDefine(title = "根据用户组ids获取用户组名称", path = "/getGoupNames", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组ids", varName = "groupIds")})
    @ApiOperation("根据用户组ids获取用户组名称")
    @GetMapping("/getGoupNames")
    public List<String> getGoupNames(@ApiParam @RequestParam("groupIds")List<String> groupIds){
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        List<OsGroup> list =  osGroupServiceImpl.getByGroupIds(groupIds,deleted);
        List<String> result = new ArrayList<>();
        if(null !=  list && list.size() > 0){
            for(OsGroup group : list){
                result.add(group.getName());
            }
        }

        return result;
    }

    @MethodDefine(title = "根据传入的组IDs，获取其全部子孙组", path = "/selectByRecursion", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组id列表", varName = "groupIds")})
    @ApiOperation("根据传入的组IDs，获取其全部子孙组")
    @PostMapping("/selectByRecursion")
    public  JsonResult selectByRecursion(@ApiParam @RequestBody HashMap groupIds){
        JsonResult result=JsonResult.getFailResult("参数不正确");
        if (groupIds==null || groupIds.size()==0) {
            return result;
        }
        result.setSuccess(true);
        result.setMessage("");
        result.setShow(false);
        result.setData(osGroupServiceImpl.getByRecursion(groupIds));
        return result;
    }

    @MethodDefine(title = "获取组织分级管理开关配置", path = "/getSupportGradeConfig", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取组织分级管理开关配置", notes = "获取组织分级管理开关配置")
    @GetMapping(value = "/getSupportGradeConfig")
    public Boolean getSupportGradeConfig(){
        return SysPropertiesUtil.getSupportGradeConfig();
    }




    @MethodDefine(title = "根据用户id获取其所在公司", path = "/getCompanyIdByUserId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "租户ID", varName = "tenantId"),@ParamDefine(title = "用户ID", varName = "tenantId")})
    @ApiOperation("根据用户id获取其所在公司")
    @GetMapping("/getCompanyByUserId")
    public OsGroupDto getCompanyByUserId(@ApiParam @RequestParam String tenantId,@ApiParam @RequestParam String userId) {
        OsGroup osGroup= osGroupServiceImpl.getMainDeps(userId,tenantId);
        OsGroupDto dto = new OsGroupDto();
        if(BeanUtil.isEmpty(osGroup)){
            return dto;
        }
        OsGroup company = osGroupServiceImpl.getCompany(osGroup);
        if(BeanUtil.isNotEmpty(osGroup)){
            dto.setGroupId(company.getGroupId());
            dto.setName(company.getName());
            return dto;
        }
        return dto;
    }


    /**
     * 获取当前人的公司数据。
     * @return {
     *     companyId:"",
     *     originCompanyId:"",
     *     companys:[{groupId:"",name:""}]
     * }
     */
    @MethodDefine(title = "获取当前人的公司数据", path = "/getCompanys", method = HttpMethodConstants.GET)
    @ApiOperation("获取当前人的公司数据")
    @GetMapping("/getCompanys")
    public JSONObject getCompanys(){

        boolean supportGrade= SysPropertiesUtil.getSupportGradeConfig();
        JSONObject json=new JSONObject();
        json.put("supportGrade",supportGrade);
        if(!supportGrade){
            return json;
        }

        String  companyId=ContextUtil.getComanyId();

        JPaasUser user=(JPaasUser)ContextUtil.getCurrentUser();
        json.put("companyId",companyId);
        json.put("originCompanyId",user.getOriginCompanyId());
        JSONArray array=new JSONArray();
        List<OsGroup> companys = osGroupServiceImpl.getCompanysById(companyId, true);
        for(OsGroup group:companys){
            JSONObject groupJson=new JSONObject();
            groupJson.put("groupId",group.getGroupId());
            groupJson.put("name",group.getName());
            array.add(groupJson);
        }
        json.put("companys",array);
        return json;
    }



    @MethodDefine(title = "根据用户组id获取用户组", path = "/getGroupById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户组id", varName = "groupId")})
    @ApiOperation("根据用户组id获取用户组")
    @GetMapping("/getGroupById")
    public OsGroupDto getGroupById(@ApiParam @RequestParam String groupId) {
        OsGroup osGroup = this.osGroupServiceImpl.get(groupId);
        if(BeanUtil.isEmpty(osGroup)){
            return null;
        }

        OsGroupDto dto = new OsGroupDto();
        dto.setGroupId(osGroup.getGroupId());
        dto.setName(osGroup.getName());
        return dto;

    }



    @MethodDefine(title = "根据维度id与父节点id查询所有组织，不做分级授权", path = "/queryAllGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID", varName = "dimId"),
                    @ParamDefine(title = "父节点ID", varName = "parentId"),
                    @ParamDefine(title = "是否授权", varName = "isGrade")})
    @ApiOperation(value = "根据维度id与父节点id查询所有组织，不做分级授权")
    @GetMapping(value="/queryAllGroups")
    public JsonResult queryAllGroups(@RequestParam(value = "dimId") String dimId,
                                     @RequestParam(value = "parentId") String parentId,
                                     @RequestParam(value = "isGrade",required = false)String isGrade,
                                     @RequestParam(value = "initRankLevel",required = false)Integer initRankLevel,
                                     @RequestParam(value = "tenantId",required = false)String tenantId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("返回数据成功!");
        if(StringUtils.isEmpty(tenantId)){
            tenantId=getCurrentTenantId();
        }
        try{
            List<OsGroup> list = osGroupServiceImpl.queryAllGroups(tenantId,dimId,parentId);
            if(initRankLevel!=null) {
                list=osGroupServiceImpl.getByDimId(tenantId,dimId);
                list=list.stream().filter(item->initRankLevel.equals(item.getRankLevel())).collect(Collectors.toList());
            }

            jsonResult.setData(list).setShow(false);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "用户组导入", path = "/importData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户组导入", varName = "request")})
    @PostMapping("/importData")
    @ApiOperation("用户组导入")
    @AuditLog(operation = "用户组导入")
    public JsonResult importData(MultipartHttpServletRequest request) throws Exception {
        JsonResult result=JsonResult.Fail();
        result.setShow(false);
        MultipartFile file = request.getFile("file");
        String fileName = file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")){
            result.setMessage("文件格式不正确！");
            return  result;
        }
        String strOverride=request.getParameter("override");
        boolean override="true".equals(strOverride);
        List<Map<Integer, String>> maps = EasyExcelUtil.readExcel(file, "2", 0);
        if(maps==null || maps.size()<2){
            result.setMessage("未读取到记录或内容不正确，请按照模板格式整理记录！");
            return  result;
        }
        //导入数据，有异常捕捉
        try {
            String message = osGroupServiceImpl.importData(maps, override);
            result=JsonResult.Success(message);
            LogContext.put(Audit.DETAIL,message);
            result.setShow(false);
            return result;
        }catch (RuntimeException e){
            JsonResult failResult=JsonResult.Fail(e.getMessage());
            failResult.setShow(false);
            return  failResult;
        }
    }

    @MethodDefine(title = "根据ID删除用户组", path = "/removeById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户组导入", varName = "request")})
    @PostMapping("/removeById")
    @ApiOperation("根据ID删除用户组")
    @AuditLog(operation = "根据ID删除用户组")
    public JsonResult removeById(@RequestParam(value = "groupId")String groupId) throws Exception {
        JsonResult result=JsonResult.Success();
        result.setShow(true);
        result.setMessage("删除用户组成功!");
        OsGroup group=osGroupServiceImpl.get(groupId);
        String path=group.getPath();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.likeRight("PATH_",path);
        osGroupServiceImpl.remove(wrapper);
        LogContext.put(Audit.PK,groupId);
        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);
        LogContext.put(Audit.DETAIL,"删除用户组:" + group.getName());

        return result;
    }



    @MethodDefine(title = "通过关系定义主键获取关联", path = "/getOsGroupsByRelTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义主键", varName = "relTypeId")})
    @ApiOperation(value = "通过关系定义主键获取关联")
    @GetMapping("getOsGroupsByRelTypeId")
    public List<OsGroup> getOsGroupsByRelTypeId(@ApiParam @RequestParam("relTypeId") String relTypeId){
        return osGroupServiceImpl.getOsGroupsByRelTypeId(relTypeId);
    }

    @MethodDefine(title = "根据维度Id与用户组Key或者用户组名称返回用户组Id", path = "/getByDimIdAndKeyOrName", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询查询", varName = "groupJson")})
    @ApiOperation(value = "根据维度Id与用户组Key或者用户组名称返回用户组Id")
    @PostMapping("getByDimIdAndKeyOrName")
    public List<OsGroupDto> getByDimIdAndKeyOrName(@RequestBody JSONObject groupJson){
        List<OsGroup> list= osGroupServiceImpl.getByDimIdAndKeyOrName(groupJson);
        List<OsGroupDto> groupDtos=getByOsGroups(list);
        return groupDtos;
    }
}
