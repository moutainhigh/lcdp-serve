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
 * ??????????????? ?????????
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osGroup")
@ClassDefine(title = "???????????????",alias = "osGroupController",path = "/user/org/osGroup",packages = "org",packageName = "????????????")
@Api(tags = "???????????????")
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
        return "???????????????";
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

    @AuditLog(operation = "???????????????")
    @ApiOperation("???????????????")
    @Override
    public JsonResult save(@ApiParam @RequestBody OsGroup entity, BindingResult validResult) throws Exception{
        JsonResult jsonResult;
        if(StringUtils.isNotBlank(entity.getKey())){
            boolean isExist = osGroupServiceImpl.isExist(entity);
            if(isExist){
                return JsonResult.Fail("???KEY????????????!");
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
            // ????????????
            LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
            String detail= EntityUtil.getInfo(entity,true);
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,entity.getGroupId());

            str="??????" + getComment() + "??????";
        }else {
            OsGroup oldGroup= osGroupServiceImpl.get(entity.getGroupId());
            // ????????????
            LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
            String detail= EntityUtil.getUpdInfo(oldGroup,entity);
            LogContext.put(Audit.DETAIL,detail);
            LogContext.put(Audit.PK,entity.getGroupId());

            osGroupServiceImpl.update(entity);
            str="??????" + getComment() + "??????";
        }
        jsonResult=JsonResult.getSuccessResult(str);
        jsonResult.setData(entity);
        return jsonResult;
    }



    @MethodDefine(title = "???????????????????????????", path = "/queryByDimId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "dimId")})
    @ApiOperation(value="???????????????????????????", notes="??????????????????????????????")
    @GetMapping(value="/queryByDimId")
    public JsonResult queryByDimId(@RequestParam String dimId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("??????????????????!");
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

    @MethodDefine(title = "????????????Id???????????????", path = "/getMainDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "userId"),@ParamDefine(title = "??????ID", varName = "tenantId")})
    @ApiOperation("????????????Id???????????????")
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

    @MethodDefine(title = "???????????????????????????????????????", path = "/getBelongGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "userId")})
    @ApiOperation("???????????????????????????????????????")
    @GetMapping("/getBelongGroups")
    public List<OsGroupDto>  getBelongGroups(@ApiParam @RequestParam String userId) {
        List<OsGroup> belongGroups = osGroupServiceImpl.getBelongGroups(userId);
        List<OsGroupDto> groupDtos=getByOsGroups(belongGroups);
        return groupDtos;
    }

    @MethodDefine(title = "?????????????????????????????????", path = "/getUpGroup", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????ID", varName = "groupId")})
    @ApiOperation("?????????????????????????????????")
    @GetMapping("/getUpGroup")
    public OsGroupDto getUpGroup(@ApiParam @RequestParam("groupId") String groupId){
        OsGroup osGroup=osGroupServiceImpl.get(groupId);
        //?????????
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

    @MethodDefine(title = "????????????????????????????????????????????????????????????????????????", path = "/getUpRankGroup", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????ID", varName = "groupId"),@ParamDefine(title = "??????????????????", varName = "rankLevel")})
    @ApiOperation("????????????????????????????????????????????????????????????????????????")
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
     * ????????????????????????????????????
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

    @MethodDefine(title = "??????????????????????????????", path = "/getBelongDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "userId")})
    @ApiOperation("??????????????????????????????")
    @GetMapping("/getBelongDeps")
    public List<OsGroupDto>  getBelongDeps(@ApiParam @RequestParam String userId) {
        List<OsGroup> belongGroups = osGroupServiceImpl.getBelongDeps(userId);
        List<OsGroupDto> groupDtos=getByOsGroups(belongGroups);
        return groupDtos;
    }

    /**
     * ???????????????????????????????????????
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
     * ???????????????????????????????????????
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

    @MethodDefine(title = "?????????ID?????????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId")})
    @ApiOperation("?????????ID?????????????????????")
    @GetMapping("/getById")
    public OsGroupDto getById(@ApiParam @RequestParam String groupId) {
        OsGroup osGroup = osGroupServiceImpl.getById(groupId);
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isNotEmpty(osGroup)) {
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    @MethodDefine(title = "?????????ID?????????????????????", path = "/getGroupByIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "getGroupByIds")})
    @ApiOperation("?????????ID?????????????????????")
    @GetMapping("/getGroupByIds")
    public List< OsGroupDto> getGroupByIds(@ApiParam @RequestParam(value = "groupIds") String groupIds) {
        //????????????
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



    @MethodDefine(title = "?????????KEY?????????????????????", path = "/getByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???KEY", varName = "groupKey")})
    @ApiOperation("?????????KEY?????????????????????")
    @GetMapping("/getByKey")
    public OsGroupDto getByKey(@ApiParam @RequestParam String groupKey){
        OsGroup osGroup = osGroupServiceImpl.getByKey(groupKey);
        OsGroupDto osGroupDto = new OsGroupDto();
        if(BeanUtil.isNotEmpty(osGroup)) {
            BeanUtil.copyProperties(osGroupDto, osGroup);
        }
        return osGroupDto;
    }

    @MethodDefine(title = "?????????KEY?????????????????????", path = "/getByKeyTenantId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???KEY", varName = "groupKey"),@ParamDefine(title = "??????ID", varName = "tenantId")})
    @ApiOperation("?????????KEY?????????????????????")
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

    @MethodDefine(title = "????????????????????????????????????ID??????", path = "/getDdownDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId")})
    @ApiOperation("????????????????????????????????????ID??????")
    @GetMapping("/getDdownDeps")
    public List<String> getDdownDeps(@ApiParam @RequestParam("groupId")String groupId){
        List<String> groupIds=new ArrayList<>();
        List<OsGroup> list= osGroupServiceImpl.getByParentId(groupId,null);
        for(OsGroup osGroup:list){
            groupIds.add(osGroup.getGroupId());
        }
        return groupIds;
    }

    @MethodDefine(title = "????????????????????????????????????ID??????", path = "/getDownDeps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId")})
    @ApiOperation("????????????????????????????????????ID??????")
    @GetMapping("/getDownDeps")
    public List<String> getDownDeps(@ApiParam @RequestParam("groupId")String groupId){
        List<String> groupIds=new ArrayList<>();
        OsGroup group= osGroupServiceImpl.getById(groupId);
        //??????????????????????????????,????????????????????????
        String groupPath= StringUtils.isEmpty(group.getPath())?"-1":group.getPath();
        List<OsGroup> list= osGroupServiceImpl.getByPath(groupPath);
        for(OsGroup osGroup:list){
            groupIds.add(osGroup.getGroupId());
        }
        return groupIds;
    }

    @MethodDefine(title = "????????????????????????????????????????????????", path = "/getUpDepNames", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @ApiOperation("????????????????????????????????????????????????")
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
     * ??????????????????ID????????????
     */
    @MethodDefine(title = "??????????????????ID????????????", path = "/getGroupObj", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId")})
    @ApiOperation(value = "??????????????????ID????????????")
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
     * ??????????????????????????????
     * ????????????:
     * <pre>
     *     dimId:??????ID
     *     initRankLevel:????????????
     *     groupIds:??????????????????,????????????:["1","2"]
     * </pre>
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "??????????????????", path = "/getGroupListByDimId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "dimId"),@ParamDefine(title = "????????????", varName = "initRankLevel"),
                    @ParamDefine(title = "???????????????ID", varName = "groupIds")})
    @ApiOperation(value = "??????????????????")
    @PostMapping("/getGroupListByDimId")
    public Result getDimList(@RequestBody JSONObject jsonObject) {
        //????????????ID
        String dimId=jsonObject.getString("dimId");
        //?????????????????? ["1","2"]
        String groupIds=jsonObject.getString("groups");
        Integer initRankLevel=jsonObject.getInteger("initRankLevel");
        String tenantId=getCurrentTenantId();
        JPaasUser jPaasUser= (JPaasUser) ContextUtil.getCurrentUser();

        //1.??????????????????
        if(initRankLevel!=null) {
            return handRankLevel(initRankLevel);
        }
        //2.????????????????????????
        if(StringUtils.isNotEmpty(groupIds)){
            return handGroups(groupIds);
        }
        List<OsGroup> groups=osGroupServiceImpl.getByDimId(tenantId,dimId,"0",jPaasUser.getCompanyId());

        return Result.succeed(groups);
    }

    /**
     * ??????????????????????????????
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
     * ??????ID?????????
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
     * ????????????ID??????????????????
     */
    @MethodDefine(title = "??????????????????????????????", path = "/getAdminOrg", method = HttpMethodConstants.GET)
    @ApiOperation(value = "??????????????????????????????")
    @GetMapping("/getAdminOrg")
    public Result getAdminOrg() throws IOException {
        String tenantId=ContextUtil.getCurrentTenantId();

        String companyId=ContextUtil.getComanyId();

        List<OsGroup> groups = osGroupServiceImpl.getByDimId(tenantId, OsDimension.DIM_ADMIN_ID, "0", companyId);

        return Result.succeed(groups);

    }



    @MethodDefine(title = "???????????????", path = "/changeParent", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "???ID", varName = "parentId")})
    @ApiOperation(value = "???????????????")
    @GetMapping("changeParent")
    @AuditLog(operation = "???????????????")
    public JsonResult changeParent(@ApiParam @RequestParam String groupId,@ApiParam @RequestParam String parentId){
        osGroupServiceImpl.changeParent(groupId,parentId);
        return JsonResult.getSuccessResult("???????????????????????????") ;
    }

    @MethodDefine(title = "????????????????????????", path = "/getByParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "parentId"),@ParamDefine(title = "????????????", varName = "initRankLevel")})
    @ApiOperation(value = "????????????????????????")
    @GetMapping("getByParentId")
    public List<OsGroup> getByParentId(@ApiParam @RequestParam String parentId,@ApiParam @RequestParam(required = false) Integer initRankLevel){
        return osGroupServiceImpl.getByParentId(parentId,initRankLevel);
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getOsGroupByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "????????????", varName = "initRankLevel")})
    @ApiOperation(value = "??????????????????????????????")
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

    @MethodDefine(title = "???????????????????????????ID??????", path = "/getOsGroupByExcludeGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "??????ID", varName = "dimId"),@ParamDefine(title = "???ID", varName = "parentId")})
    @ApiOperation(value = "???????????????????????????ID??????")
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

    @MethodDefine(title = "????????????????????????????????????", path = "/joinGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "???????????????ID", varName = "groupIds"),@ParamDefine(title = "????????????ID", varName = "relTypeId")})
    @ApiOperation(value="????????????????????????????????????")
    @PostMapping("/joinGroups")
    @AuditLog(operation = "?????????????????????")
    public JsonResult joinGroups(@ApiParam @RequestParam(value ="groupId", required = false) String groupId,
                                 @ApiParam @RequestParam(value ="groupIds", required = false) String groupIds,
                                 @ApiParam @RequestParam(value = "relTypeId",required = false) String relTypeId){
        String[] gIds = groupIds.split("[,]");
        String tenantId=getCurrentTenantId();
        OsRelType osRelType= osRelTypeServiceImpl.get(relTypeId);
        OsGroup mainGroup = osGroupServiceImpl.get(groupId);
        StringBuilder detail=new StringBuilder( "????????????:" + mainGroup.getName() +"("+mainGroup.getGroupId()+")????????????:");

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
        detail.append("??????:[" + osRelType.getName() +"]??????");

        //??????
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.getSuccessResult("???????????????");
    }

    @MethodDefine(title = "?????????????????????", path = "/getCurrentProfile", method = HttpMethodConstants.GET)
    @ApiOperation(value = "?????????????????????")
    @GetMapping("getCurrentProfile")
    public Map<String,Set<String>> getCurrentProfile(){
        return ProfileContext.getCurrentProfile();
    }

    @MethodDefine(title = "?????????????????????", path = "/getOsUserGroupHanderList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "?????????????????????")
    @GetMapping("getOsUserGroupHanderList")
    public JSONArray getOsUserGroupHanderList(){
        return ProfileContext.getOsUserGroupHanderList();
    }

    @MethodDefine(title = "????????????id????????????id????????????", path = "/queryGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "dimId"),
                    @ParamDefine(title = "?????????ID", varName = "parentId")})
    @ApiOperation(value = "????????????id????????????id????????????")
    @GetMapping(value="/queryGroups")
    public JsonResult queryGroups(@RequestParam(value = "dimId") String dimId,
                                  @RequestParam(value = "parentId") String parentId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("??????????????????!");
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

    @MethodDefine(title = "???????????????", path = "/searchGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????", varName = "params")})
    @ApiOperation("???????????????")
    @PostMapping("/searchGroups")
    public List<OsGroup> searchGroups( @RequestBody QueryData queryData){
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        handleFilter(filter);
        return getBaseService().queryList(filter);
    }


    @MethodDefine(title = "????????????????????????????????????ID??????", path = "/getGroupsByUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "userId"),@ParamDefine(title = "??????ID", varName = "tenantId")})
    @ApiOperation("????????????????????????????????????ID??????")
    @GetMapping("/getGroupsByUser")
    public List<String> getGroupsByUser(@ApiParam @RequestParam("userId")String userId,
                                   @ApiParam @RequestParam("tenantId")String tenantId){
        List<String> groupIds = osGroupServiceImpl.getGroupsByUser(userId, tenantId);
        return groupIds;
    }

    @MethodDefine(title = "?????????????????????????????????", path = "/getGroupAndUserByParentId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID", varName = "parentId"),@ParamDefine(title = "??????ID", varName = "dimId")})
    @ApiOperation(value = "?????????????????????????????????")
    @GetMapping("getGroupAndUserByParentId")
    public JsonResult getGroupAndUserByParentId(@ApiParam @RequestParam String parentId,@ApiParam @RequestParam(required = false) String dimId){
        String tenantId=getCurrentTenantId();
        JsonResult jsonResult =osGroupServiceImpl.getGroupAndUserByParentId(parentId,dimId,tenantId);
        return jsonResult;
    }


    @MethodDefine(title = "???????????????????????????????????????", path = "/getByDimCodeGroupKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????Key", varName = "dimKey"),@ParamDefine(title = "?????????Key", varName = "groupKey")})
    @ApiOperation(value = "???????????????????????????????????????")
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


    @MethodDefine(title = "???????????????ids?????????????????????", path = "/getGoupNames", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????ids", varName = "groupIds")})
    @ApiOperation("???????????????ids?????????????????????")
    @GetMapping("/getGoupNames")
    public List<String> getGoupNames(@ApiParam @RequestParam("groupIds")List<String> groupIds){
        //????????????
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

    @MethodDefine(title = "??????????????????IDs???????????????????????????", path = "/selectByRecursion", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????id??????", varName = "groupIds")})
    @ApiOperation("??????????????????IDs???????????????????????????")
    @PostMapping("/selectByRecursion")
    public  JsonResult selectByRecursion(@ApiParam @RequestBody HashMap groupIds){
        JsonResult result=JsonResult.getFailResult("???????????????");
        if (groupIds==null || groupIds.size()==0) {
            return result;
        }
        result.setSuccess(true);
        result.setMessage("");
        result.setShow(false);
        result.setData(osGroupServiceImpl.getByRecursion(groupIds));
        return result;
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getSupportGradeConfig", method = HttpMethodConstants.GET)
    @ApiOperation(value = "????????????????????????????????????", notes = "????????????????????????????????????")
    @GetMapping(value = "/getSupportGradeConfig")
    public Boolean getSupportGradeConfig(){
        return SysPropertiesUtil.getSupportGradeConfig();
    }




    @MethodDefine(title = "????????????id?????????????????????", path = "/getCompanyIdByUserId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "tenantId"),@ParamDefine(title = "??????ID", varName = "tenantId")})
    @ApiOperation("????????????id?????????????????????")
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
     * ?????????????????????????????????
     * @return {
     *     companyId:"",
     *     originCompanyId:"",
     *     companys:[{groupId:"",name:""}]
     * }
     */
    @MethodDefine(title = "??????????????????????????????", path = "/getCompanys", method = HttpMethodConstants.GET)
    @ApiOperation("??????????????????????????????")
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



    @MethodDefine(title = "???????????????id???????????????", path = "/getGroupById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????id", varName = "groupId")})
    @ApiOperation("???????????????id???????????????")
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



    @MethodDefine(title = "????????????id????????????id???????????????????????????????????????", path = "/queryAllGroups", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "dimId"),
                    @ParamDefine(title = "?????????ID", varName = "parentId"),
                    @ParamDefine(title = "????????????", varName = "isGrade")})
    @ApiOperation(value = "????????????id????????????id???????????????????????????????????????")
    @GetMapping(value="/queryAllGroups")
    public JsonResult queryAllGroups(@RequestParam(value = "dimId") String dimId,
                                     @RequestParam(value = "parentId") String parentId,
                                     @RequestParam(value = "isGrade",required = false)String isGrade,
                                     @RequestParam(value = "initRankLevel",required = false)Integer initRankLevel,
                                     @RequestParam(value = "tenantId",required = false)String tenantId) throws Exception{
        JsonResult jsonResult=JsonResult.getSuccessResult("??????????????????!");
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

    @MethodDefine(title = "???????????????", path = "/importData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "???????????????", varName = "request")})
    @PostMapping("/importData")
    @ApiOperation("???????????????")
    @AuditLog(operation = "???????????????")
    public JsonResult importData(MultipartHttpServletRequest request) throws Exception {
        JsonResult result=JsonResult.Fail();
        result.setShow(false);
        MultipartFile file = request.getFile("file");
        String fileName = file.getOriginalFilename().toLowerCase();
        if(!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")){
            result.setMessage("????????????????????????");
            return  result;
        }
        String strOverride=request.getParameter("override");
        boolean override="true".equals(strOverride);
        List<Map<Integer, String>> maps = EasyExcelUtil.readExcel(file, "2", 0);
        if(maps==null || maps.size()<2){
            result.setMessage("???????????????????????????????????????????????????????????????????????????");
            return  result;
        }
        //??????????????????????????????
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

    @MethodDefine(title = "??????ID???????????????", path = "/removeById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "???????????????", varName = "request")})
    @PostMapping("/removeById")
    @ApiOperation("??????ID???????????????")
    @AuditLog(operation = "??????ID???????????????")
    public JsonResult removeById(@RequestParam(value = "groupId")String groupId) throws Exception {
        JsonResult result=JsonResult.Success();
        result.setShow(true);
        result.setMessage("?????????????????????!");
        OsGroup group=osGroupServiceImpl.get(groupId);
        String path=group.getPath();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.likeRight("PATH_",path);
        osGroupServiceImpl.remove(wrapper);
        LogContext.put(Audit.PK,groupId);
        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);
        LogContext.put(Audit.DETAIL,"???????????????:" + group.getName());

        return result;
    }



    @MethodDefine(title = "????????????????????????????????????", path = "/getOsGroupsByRelTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "relTypeId")})
    @ApiOperation(value = "????????????????????????????????????")
    @GetMapping("getOsGroupsByRelTypeId")
    public List<OsGroup> getOsGroupsByRelTypeId(@ApiParam @RequestParam("relTypeId") String relTypeId){
        return osGroupServiceImpl.getOsGroupsByRelTypeId(relTypeId);
    }

    @MethodDefine(title = "????????????Id????????????Key????????????????????????????????????Id", path = "/getByDimIdAndKeyOrName", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "groupJson")})
    @ApiOperation(value = "????????????Id????????????Key????????????????????????????????????Id")
    @PostMapping("getByDimIdAndKeyOrName")
    public List<OsGroupDto> getByDimIdAndKeyOrName(@RequestBody JSONObject groupJson){
        List<OsGroup> list= osGroupServiceImpl.getByDimIdAndKeyOrName(groupJson);
        List<OsGroupDto> groupDtos=getByOsGroups(list);
        return groupDtos;
    }
}
