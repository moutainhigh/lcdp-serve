package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.api.sys.ISystemService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.excel.EasyExcelUtil;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.model.PageResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.Result;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.service.*;
import com.redxun.web.controller.BaseController;
import com.taobao.api.ApiException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * ??????Controller
 * @author ?????? yjy
 * ??????
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osUser")
@ClassDefine(title = "????????????",alias = "osUserController",path = "/user/org/osUser",packages = "org",packageName = "????????????")
@Api(tags = "????????????api")
public class OsUserController  extends BaseController<OsUser> {

    @Autowired
    OsUserServiceImpl osUserServiceImpl;
    @Autowired
    OsRelInstServiceImpl osRelInstServiceImpl;
    @Autowired
    OsRelTypeServiceImpl osRelTypeServiceImpl;
    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;
    @Autowired
    OsDimensionServiceImpl osDimensionServiceImpl;
    @Autowired
    OsPropertiesValServiceImpl osPropertiesValService;
    @Autowired
    OsPropertiesDefServiceImpl osPropertiesDefService;
    @Autowired
    ISystemService systemService;
    @Autowired
    OsInstUsersServiceImpl osInstUsersService;
    @Autowired
    OsInstServiceImpl osInstService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    final String dd_ok_code="0";


    @Override
    public BaseService getBaseService() {
        return osUserServiceImpl;
    }

    @Override
    public String getComment() {
        return "??????";
    }


    @Override
    protected void handleData(OsUser ent) {
        OsInstUsers osInstUsers=osInstUsersService.getByUserTenant(ent.getUserId(),ContextUtil.getCurrentTenantId());
        if(osInstUsers!=null){
            ent.setUserType(osInstUsers.getUserType());
        }
        super.handleData(ent);
    }

    @PostMapping("/getUserProperty")
    public JsonResult getUserProperty(@RequestBody JSONObject json){
        String userId=json.getString("userId");
        String attrName=json.getString("attrName");
        OsUser osUser=osUserServiceImpl.getById(userId);
        Object value=BeanUtil.getFieldValueFromObject(osUser,attrName);
        if(value==null){
            List<OsPropertiesVal> list=osPropertiesValService.getByOwnerId(userId);
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

    /**
     * ????????????????????????????????????????????????Id????????????
     * @return
     * @throws Exception
     */
    @ApiOperation(value="???????????????????????????", notes="??????????????????????????????")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult= JsonPageResult.getSuccess("??????????????????!");
        try{
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            String dimId="dimId";
            String groupId="groupId";
            Map<String, String> params = queryData.getParams();
            String tenantId=getCurrentTenantId();
            filter.addParam("tenantId",tenantId);
            if(StringUtils.isEmpty(params.get(dimId))){
                filter.getParams().put(dimId,"");
            }
            if(StringUtils.isEmpty(params.get(groupId))){
                filter.getParams().put(groupId,"");
            }
            //????????????
            if (DbLogicDelete.getLogicDelete()) {
                filter.addQueryParam("Q_u.DELETED__S_EQ","0");
            }
            IPage page= osUserServiceImpl.searchByBelongGroupId(filter);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }






    /**
     * ?????????????????? LoginAppUser
     *
     * @return
     */
    @MethodDefine(title = "??????token??????????????????", path = "/current", method = HttpMethodConstants.GET)
    @ApiOperation(value = "??????token??????????????????")
    @GetMapping("/current")
    public JsonResult<IUser> getLoginAppUser() {
         IUser user=ContextUtil.getCurrentUser();
         JsonResult json=JsonResult.Success();
         user.setRootPath(SysPropertiesUtil.getString("serverAddress"));
        String photo = osUserServiceImpl.get(user.getUserId()).getPhoto();
        user.setPhoto(photo);
         json.setData(user);
         return json;
     }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @MethodDefine(title = "?????????????????????????????????", path = "/getLoginUserAndMenu", method = HttpMethodConstants.GET)
    @ApiOperation(value = "?????????????????????????????????")
    @GetMapping("/getLoginUserAndMenu")
    public JsonResult getLoginUserAndMenu() {
        JPaasUser user = null;
        try {
            user = SysUserUtil.getLoginUser();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String photo = osUserServiceImpl.get(user.getUserId()).getPhoto();
        user.setPhoto(photo);
        JsonResult jsonResult=JsonResult.Success();
        List<SysMenuDto> menuDtos = osUserServiceImpl.selectMenusByUser(user);
        JSONObject allMenuButtons = systemService.getAllButtonsByMenuType("F");
        user.setRootPath(SysPropertiesUtil.getString("serverAddress"));
        if(StringUtils.isNotEmpty(user.getTenantId())){
            OsInst osInst = osInstService.get(user.getTenantId());
            String label = osInst.getLabel();
            user.setTenantLabel(label);
        }
        Map<String,Object> map=new HashMap();
        map.put("user",user);
        map.put("menus",menuDtos);
        map.put("allMenuButtons",allMenuButtons);
        jsonResult.setData(map);
        return jsonResult;
    }


    /**
     * ????????????????????????LoginAppUser
     */
    @GetMapping(value = "/findByUsername", params = "username")
    @ApiOperation(value = "???????????????????????????")
    public JPaasUser findByUsername(@RequestParam(name = "username") String username) {
        return  osUserServiceImpl.findByUsername(username);
    }



    /**
     * ????????????????????????LoginAppUser
     */
    @GetMapping(value = "/findByUsernameAndTenantId",  params={"username","tenantId"})
    @ApiOperation(value = "????????????????????????id????????????")
    public JPaasUser findByUsernameAndTenantId(@RequestParam(name = "username") String username, @RequestParam(name = "tenantId") String tenantId) {
        return  osUserServiceImpl.findByUsernameAndTenantId(username, tenantId);
    }



    @MethodDefine(title = "??????ID??????????????????", path = "/getUserById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID",varName = "userId")})
    @ApiOperation("??????ID??????????????????")
    @GetMapping("/getUserById")
    public JPaasUser getUserById(@ApiParam @RequestParam(value = "userId") String userId){
        return osUserServiceImpl.getUserById(userId);
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param mobile ?????????
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "???????????????????????????")
    public JPaasUser findByMobile(String mobile) {
        return  osUserServiceImpl.findByMobile(mobile);
    }



    @MethodDefine(title = "?????????????????????????????????", path = "/getByUsername", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????",varName = "username")})
    @ApiOperation("?????????????????????????????????")
    @GetMapping("/getByUsername")
    public JPaasUser getByUsername(@ApiParam @RequestParam String username){
        return osUserServiceImpl.findByUsername(username);
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getByAccount", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????",varName = "account")})
    @ApiOperation("????????????????????????????????????")
    @GetMapping("/getByAccount")
    public OsUserDto getByAccount(@ApiParam @RequestParam String account){
        return osUserServiceImpl.getByAccount(account);
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getByWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????ID",varName = "wxOpenId")})
    @ApiOperation(value = " ????????????????????????????????????")
    @GetMapping("/getByWxOpenId")
    public JPaasUser getByWxOpenId(@ApiParam @RequestParam String wxOpenId){
        return osUserServiceImpl.getByWxOpenId(wxOpenId);
    }

    @MethodDefine(title = "????????????????????????????????????????????????????????????", path = "/isOtherUserContainWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????",varName = "username"),@ParamDefine(title = "????????????ID",varName = "wxOpenId")})
    @ApiOperation(value = "????????????????????????????????????????????????????????????")
    @GetMapping("/isOtherUserContainWxOpenId")
    public String isOtherUserContainWxOpenId(@ApiParam @RequestParam String username,@ApiParam @RequestParam String wxOpenId){
        return osUserServiceImpl.isOtherUserContainWxOpenId(username,wxOpenId);
    }

    @MethodDefine(title = "????????????????????????", path = "/updateByWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????",varName = "username"),@ParamDefine(title = "????????????ID",varName = "wxOpenId")})
    @ApiOperation(value = "????????????????????????")
    @GetMapping("/updateByWxOpenId")
    @AuditLog(operation = "????????????????????????")
    public void updateByWxOpenId(@ApiParam @RequestParam String username,@ApiParam @RequestParam String wxOpenId){
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        if(BeanUtil.isNotEmpty(osUser)){
            osUser.setWxOpenId(wxOpenId);
            osUserServiceImpl.updateUser(osUser);
        }
    }



    @MethodDefine(title = "????????????ID??????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID",varName = "userId")})
    @ApiOperation(value = "????????????ID??????????????????")
    @GetMapping("/getById")
    public OsUserDto getById(@ApiParam @RequestParam String userId) {
        OsUser osUser = osUserServiceImpl.getById(userId);
        if(osUser==null){
            return null;
        }
        OsUserDto osUserDto = osUserServiceImpl.convertOsUser(osUser);
        return osUserDto;
    }

    /**
     * ?????????????????????DTO?????????
     * @param groupId
     * @return
     */
    @MethodDefine(title = "?????????ID??????????????????", path = "/getByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???ID",varName = "groupId")})
    @ApiOperation(value = "?????????ID??????????????????")
    @GetMapping("/getByGroupId")
    public List<OsUserDto> getByGroupId(@ApiParam @RequestParam(value = "groupId") String groupId) {
        List<OsUser> osUsers = osUserServiceImpl.getBelongUsers(groupId);
        List<OsUserDto> osUserDtos = osUserServiceImpl.convertOsUsers(osUsers);
        return osUserDtos;
    }

    @MethodDefine(title = "????????????????????????????????????????????????", path = "/getUserUpLowPath", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID",varName = "userId")})
    @ApiOperation(value = "????????????????????????????????????????????????")
    @GetMapping("/getUserUpLowPath")
    public String getUserUpLowPath(@ApiParam @RequestParam String userId){
        List<OsRelInst> upLowRelInsts = osRelInstServiceImpl.getByRelTypeIdParty2(OsRelType.REL_CAT_USER_UP_LOWER_ID,userId);
        if(upLowRelInsts.size()==1){
            return upLowRelInsts.get(0).getPath();
        }
        return "";
    }

    @MethodDefine(title = "??????????????????????????????ID??????", path = "/getDupUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????",varName = "upLowPath")})
    @ApiOperation(value = "??????????????????????????????ID??????")
    @GetMapping("/getDupUserIds")
    public List<String> getDupUserIds(@ApiParam @RequestParam("upLowPath")String upLowPath){
        List<String> userIds=new ArrayList<>();
        List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdPath(OsRelType.REL_CAT_USER_UP_LOWER_ID,upLowPath);
        for(OsRelInst osRelInst:list){
            userIds.add(osRelInst.getParty1());
        }
        return userIds;
    }

    @MethodDefine(title = "??????????????????????????????ID??????", path = "/getDdownUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID",varName = "userId")})
    @ApiOperation(value = "??????????????????????????????ID??????")
    @GetMapping("/getDdownUserIds")
    public List<String> getDdownUserIds(@ApiParam @RequestParam("userId")String userId){
        String tenantId=ContextUtil.getCurrentTenantId();
        List<String> userIds=new ArrayList<>();
        List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdParty1(tenantId,  OsRelType.REL_CAT_USER_UP_LOWER_ID,userId);
        for(OsRelInst osRelInst:list){
            userIds.add(osRelInst.getParty2());
        }
        return userIds;
    }

    @MethodDefine(title = "??????????????????????????????ID??????", path = "/getDownUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????",varName = "upLowPath")})
    @ApiOperation(value = "??????????????????????????????ID??????")
    @GetMapping("/getDownUserIds")
    public List<String> getDownUserIds(@ApiParam @RequestParam("upLowPath")String upLowPath){
        List<String> userIds=new ArrayList<>();
        List<OsRelInst> list= osRelInstServiceImpl.getByPath(upLowPath);
        for(OsRelInst osRelInst:list){
            userIds.add(osRelInst.getParty2());
        }
        return userIds;
    }



    @Override
    @PostMapping("/save")
    @ApiOperation(value = "??????????????????")
    @AuditLog(operation = "??????????????????")
    public JsonResult save(@ApiParam @RequestBody OsUser entity, BindingResult validResult) throws Exception{
        JsonResult jsonResult;
        //?????????????????????????????????
        if(StringUtils.isEmpty(entity.getUserId())){
            entity.setStatus(OsUser.STATUS_IN_JOB);
            if(StringUtils.isEmpty(entity.getTenantId())) {
                entity.setTenantId(ContextUtil.getCurrentTenantId());
            }
        }
        //????????????ID??????????????????????????????????????????????????????ID
        if(StringUtils.isNotEmpty(entity.getUserId())){
            OsUser originUser=osUserServiceImpl.getById(entity.getUserId());
            entity.setCurTenantId(entity.getTenantId());
            entity.setTenantId(originUser.getTenantId());
        }


        boolean isExist = osUserServiceImpl.isUserNotExist(entity);
        if(isExist){
            jsonResult=new JsonResult().setMessage("????????????!").setSuccess(false);
            return jsonResult;
        }
        jsonResult = super.save(entity,validResult);

        List<OsPropertiesGroup> propertiesGroups = entity.getPropertiesGroups();
        if(BeanUtil.isNotEmpty(propertiesGroups)){
            OsUser user = (OsUser) jsonResult.getData();
            String userId = user.getUserId();
            osPropertiesValService.saveProPerty(propertiesGroups,userId);
        }

        return jsonResult;
    }

    @MethodDefine(title = "????????????", path = "/joinUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "????????????ID", varName = "relTypeId"),@ParamDefine(title = "??????ID", varName = "userIds")})
    @ApiOperation(value="????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("/joinUser")
    public JsonResult joinUser(@RequestParam(required = false)String groupId,@RequestParam(required = false)String relTypeId,@RequestParam(required = false)String userIds){

        StringBuilder sb=new StringBuilder();
        sb.append("?????????:");

        OsGroup group=osGroupServiceImpl.get(groupId);
        String dimId= group.getDimId();
        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(relTypeId)){
            return JsonResult.getFailResult("????????????????????????????????????");
        }
        boolean flag=true;
        OsRelType osRelType= osRelTypeServiceImpl.get(relTypeId);
        String[] uIds=userIds.split("[,]");
        for(String userId:uIds){
            OsUser user=osUserServiceImpl.get(userId);
            sb.append(user.getFullName() +"(" +user.getUserNo() +")");

            OsRelInst inst1= osRelInstServiceImpl.getByParty1Party2RelTypeId(groupId,userId,relTypeId);
            if(inst1!=null){
                continue;
            }
            OsRelInst inst =new OsRelInst();
            inst.setDim1(dimId);
            inst.setParty1(groupId);
            inst.setParty2(userId);
            inst.setPath("0."+groupId+"."+userId+".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeId(relTypeId);
            inst.setIsMain(MBoolean.NO.name());
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setTenantId(group.getTenantId());
            osRelInstServiceImpl.insert(inst);
            flag=false;
        }
        if(flag){
            sb.append("????????????:" +group.getName() +"???" +osRelType.getName() +"????????????????????????");
            LogContext.put(Audit.DETAIL,sb.toString());
            return JsonResult.getSuccessResult("??????????????????");
        }
        sb.append("????????????:" +group.getName() +"???" +osRelType.getName() +"?????????????????????");
        LogContext.put(Audit.DETAIL,sb.toString());
        return JsonResult.getSuccessResult("???????????????");
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/edit", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "pkId"),@ParamDefine(title = "???ID", varName = "groupId"),@ParamDefine(title = "??????ID",varName = "tenantId")})
    @ApiOperation(value="??????????????????", notes="????????????????????????????????????")
    @GetMapping("/edit")
    public JSONObject edit(@RequestParam(required = false)String pkId,@RequestParam(required = false)String groupId,@RequestParam(required = false)String tenantId){
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        OsUser user= osUserServiceImpl.get(pkId);
        JSONObject mv=new JSONObject();
        if(BeanUtil.isNotEmpty(user)) {
            handleData(user);
            mv.put("sn", user.getSn());
        }else {
            int maxSn = osUserServiceImpl.getMaxSn();
            mv.put("sn", maxSn+1);
        }
        OsGroup mainDep=null;
        List<OsGroup> canDeps=null;
        List<OsGroup> canGroups=null;
        //????????????
        List<OsDimension> dimList = osDimensionServiceImpl.getTenantDims(tenantId);
        JSONArray canGroupIds = new JSONArray();
        for (OsDimension dim : dimList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dimId", dim.getDimId());
            jsonObject.put("dimName", dim.getName());
            canGroupIds.add(jsonObject);
        }
        if (canGroupIds.size() == 0) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dimId", "other");
            jsonObject.put("dimName", "?????????");
            canGroupIds.add(jsonObject);
        }
        if(StringUtils.isNotEmpty(pkId)) {
            //?????????
            mainDep = osGroupServiceImpl.getMainDeps(pkId, tenantId);
            if (mainDep != null) {
                mv.put("mainDepId", mainDep.getGroupId());
                mv.put("mainDepName", mainDep.getName());
            }
            canGroups= osGroupServiceImpl.getCanGroups(pkId,tenantId);
        }

        //??????????????????
        if(StringUtils.isNotEmpty(groupId)){
            mainDep= osGroupServiceImpl.get(groupId);
            //????????????
            if(mainDep!=null && mainDep.getDimId()!=null){
                OsDimension dim = osDimensionServiceImpl.get(mainDep.getDimId());
                if(OsDimension.DIM_ADMIN.equals(dim.getCode())){
                    //?????????
                    mv.put("mainDepId", mainDep.getGroupId());
                    mv.put("mainDepName", mainDep.getName());
                }else{//?????????
                    canGroups=new ArrayList<>();
                    canGroups.add(mainDep);
                }
            }
        }
        if(canGroups!=null){
            for(OsGroup g:canGroups){
                setOsGroups(g,canGroupIds);
            }
        }

        mv.put("dimArray",canGroupIds);
        mv.put("user",user);
        return mv;
    }

    /**
     * ???????????????
     * @param g
     * @param canGroupIds
     */
    private void setOsGroups(OsGroup g,JSONArray canGroupIds){
        for(int i=0;i<canGroupIds.size();i++){
            JSONObject jsonObject=canGroupIds.getJSONObject(i);
            if(g.getDimId().equals(jsonObject.getString("dimId")) || i==canGroupIds.size()-1){
                String groupIds=jsonObject.getString("groupIds");
                String groupNames=jsonObject.getString("groupNames");
                if(StringUtils.isEmpty(groupIds)){
                    jsonObject.put("groupIds",g.getGroupId());
                    jsonObject.put("groupNames",g.getName());
                }else{
                    groupIds +=","+g.getGroupId();
                    groupNames +=","+g.getName();
                    jsonObject.put("groupIds",groupIds);
                    jsonObject.put("groupNames",groupNames);
                }
                break;
            }
        }
    }

    /**
     *  ?????????????????????????????????????????????????????????????????????Id??????
     * @return
     */
    @MethodDefine(title = "?????????????????????????????????????????????????????????????????????Id??????", path = "/getUserIdsByGroupIdsRoleIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???Ids", varName = "groupIds"),@ParamDefine(title = "??????Ids", varName = "roleIds")})
    @GetMapping("/getUserIdsByGroupIdsRoleIds")
    public List<String> getUserIdsByGroupIdsRoleIds(@RequestParam("groupIds") String groupIds,
                                                    @RequestParam("roleIds") String roleIds){
//        if(StringUtils.isEmpty(groupIds) || StringUtils.isEmpty(roleIds)){
//            return Result.failed("groupIds???roleIds????????????????????????");
//        }
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByGroupIdsRoleIds(groupIds,roleIds);
        return userIdList;
    }

    /**
     * ??????????????????????????????1????????????2??????????????????Ids
     * @return
     */
    @MethodDefine(title = "??????????????????????????????1????????????2??????????????????Ids", path = "/getByUserIdsByGroupId1GroupId2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???Id1", varName = "groupId1"),@ParamDefine(title = "???Id2", varName = "groupId2")})
    @GetMapping("/getUserIdsByGroupId1GroupId2")
    public List<String> getUserIdsByGroupId1GroupId2(@RequestParam("groupId1") String groupId1,
                                              @RequestParam("groupId2") String groupId2){
//        if(StringUtils.isEmpty(groupId1) || StringUtils.isEmpty(groupId2)){
//            return Result.failed("groupId1 ??? groupId2 ????????????????????????");
//        }
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByGroupId1GroupId2(groupId1,groupId2);
        return userIdList;
    }

    /**
     * ??????????????????????????????????????????Id??????
     * @param groupId ?????????ID
     * @param relTypeKey ???????????????
     * @return
     */
    @MethodDefine(title = "??????????????????????????????????????????Id??????", path = "/getUserIdsByGroupIdRelTypeKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "???Id", varName = "groupId"),@ParamDefine(title = "????????????Key", varName = "relTypeKey")})
    @GetMapping("/getUserIdsByGroupIdRelTypeKey")
    public List<String> getUserIdsByGroupIdRelTypeKey(@RequestParam("groupId") String groupId,
                                                @RequestParam("relTypeKey") String relTypeKey){
//        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(relTypeKey)){
//            return Result.failed("groupId ??? relTypeKey ????????????????????????");
//        }
        // ????????????
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByParty1RelTypeKey(groupId,relTypeKey);
        return userIdList;
        //return Result.succeed(userIdList);
    }

    /**
     * ??????????????????????????????????????????Id??????
     * @param userId ??????ID
     * @param relTypeKey ???????????????
     * @return
     */
    @MethodDefine(title = "??????????????????????????????????????????Id??????", path = "/getUserIdsByUserIdRelTypeKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "userId"),@ParamDefine(title = "????????????Key", varName = "relTypeKey")})
    @GetMapping("/getUserIdsByUserIdRelTypeKey")
    public List<String> getUserIdsByUserIdRelTypeKey(@RequestParam("userId") String userId,
                                                     @RequestParam("relTypeKey") String relTypeKey){
//        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(relTypeKey)){
//            return Result.failed("userId ??? relTypeKey ????????????????????????");
//        }
        // ????????????
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByParty1RelTypeKey(userId,relTypeKey);
        return userIdList;
    }

    /**
     * ??????
     */
    @MethodDefine(title = "????????????", path = "/list", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "params")})
    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "??????????????????", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "??????????????????", required = true, dataType = "Integer")
    })
    @GetMapping("/list")
    public PageResult list(@RequestParam Map<String, Object> params) {
        return osUserServiceImpl.findUsers(params);
    }

    /**
     * ???????????????ID??????????????????
     */
    @MethodDefine(title = "??????????????????", path = "/getUserListByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "dimId")})
    @ApiOperation(value = "??????????????????")
    @GetMapping("/getUserListByGroupId")
    public Result getDimList(@RequestParam String dimId) {

        List<OsUser> model = osUserServiceImpl.list(new QueryWrapper<OsUser>()
                .eq("DIM_ID_", dimId)
                .orderByDesc("UPDATE_TIME_"));
        return Result.succeed(model);
    }

    /**
     * ?????????????????????????????????
     *
     * @param params
     * @return
     */
    @MethodDefine(title = "??????????????????", path = "/updateEnabled", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "params")})
    @ApiOperation(value = "??????????????????")
    @AuditLog(operation = "??????????????????")
    @GetMapping("/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "????????????", required = true, dataType = "Map<String, Object>")
    })
    public Result updateEnabled(@RequestParam Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        JPaasUser user= (JPaasUser) ContextUtil.getCurrentUser();
        if(user.getUserId().equals(id) ){
            return Result.failed("?????????????????????????????????!");
        }
        if(user.isAdmin()){
            return Result.failed("???????????????????????????!");
        }

        return osUserServiceImpl.updateEnabled(params);
    }
    /**
     * ??????excel
     *
     * @return
     */
    @MethodDefine(title = "??????EXCEL", path = "/export", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????EXCEL", varName = "params")})
    @ApiOperation(value = "??????EXCEL")
    @PostMapping("/export")
    public void exportUser(@RequestParam Map<String, Object> params) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        List<SysUserExcel> result = osUserServiceImpl.findAllUsers(params);
        //????????????
        ExcelUtil.exportExcel(result, null, "??????", SysUserExcel.class, "user", response);
    }


    @MethodDefine(title = "??????????????????", path = "/getUsersByTaskExecutor", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "executors")})
    @ApiOperation(value = "??????????????????")
    @PostMapping ("/getUsersByTaskExecutor")
    public List<OsUserDto> getUsersByTaskExecutor(@RequestBody List<TaskExecutor> executors){
        List<OsUserDto> list=new ArrayList<>();
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                OsUserDto user= osUserServiceImpl.convertOsUser( osUserServiceImpl.get(executor.getId()));
                list.add(user);
            }
            else{
                List<OsUser> users = osUserServiceImpl.getBelongUsers(executor.getId());
                List<OsUserDto> osUserDtos=osUserServiceImpl.convertOsUsers(users);
                list.addAll(osUserDtos);
            }
        }
        return list;
    }

    @MethodDefine(title = "??????userId????????????", path = "/getUsersByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "userIds")})
    @ApiOperation(value = "??????userId????????????")
    @PostMapping ("/getUsersByIds")
    public List<OsUserDto> getUsersByIds( @RequestParam("userIds") String userIds){
        return osUserServiceImpl.getByUsers(userIds);
    }

    @MethodDefine(title = "????????????", path = "/dimissionByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "ids"),@ParamDefine(title = "????????????", varName = "quitTime")})
    @ApiOperation(value = "????????????")
    @AuditLog(operation = "????????????")
    @PostMapping ("/dimissionByIds")
    public JsonResult dimissionByIds( @RequestParam("ids") String ids,@RequestParam("quitTime") String quitTime){
        try {
            String[] userIds=ids.split(",");
            String detail="??????:";
            for(String userId:userIds){
                OsUser osUser=osUserServiceImpl.getById(userId);
                detail+= osUser.getFullName() +"("+osUser.getUserNo()+"),";
            }
            detail+="??????";
            LogContext.put(Audit.DETAIL,detail);

            JsonResult jsonResult=osUserServiceImpl.setStatusAndQuitTime(userIds,quitTime);
            return jsonResult;
        } catch (Exception e) {
            //??????
            LogContext.put(Audit.DETAIL,ExceptionUtil.getExceptionMessage(e));
            return new JsonResult().setMessage("????????????!").setSuccess(false);
        }
    }

    @MethodDefine(title = "????????????????????????", path = "/updatePassword", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "?????????", varName = "oldPwd"),@ParamDefine(title = "?????????", varName = "newPwd")})
    @ApiOperation(value = "????????????????????????")
    @AuditLog(operation = "????????????????????????")
    @PostMapping ("/updatePassword")
    public JsonResult updatePassword( @RequestParam("oldPwd") String oldPwd,@RequestParam("newPwd") String newPwd){
        IUser user=ContextUtil.getCurrentUser();
        LogContext.put(Audit.PK,user.getUserId());
        String detail=user.getFullName() +"("+user.getAccount()+")??????????????????";
        LogContext.put(Audit.DETAIL,detail);

        JsonResult jsonResult = osUserServiceImpl.updatePassword(user.getUserId(), oldPwd, newPwd);
        return jsonResult;
    }

    @MethodDefine(title = "??????????????????", path = "/setPassword", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "userId"),@ParamDefine(title = "?????????", varName = "newPwd")})
    @ApiOperation(value = "??????????????????")
    @AuditLog(operation = "??????????????????")
    @PostMapping ("/setPassword")
    public JsonResult setPassword( @RequestParam("userId") String userId,@RequestParam("newPwd") String newPwd){
        OsUser osUser=osUserServiceImpl.getById(userId);
        LogContext.put(Audit.PK,osUser.getUserId());
        String detail= "??????"+osUser.getFullName()+"("+osUser.getUserNo()+")??????";
        LogContext.put(Audit.DETAIL,detail);

        JsonResult jsonResult = osUserServiceImpl.setPassword(userId, newPwd);
        return jsonResult;
    }

    @ApiOperation(value="????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("del")
    @Override
    public JsonResult del(@RequestParam String ids){

        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String tenantId=ContextUtil.getCurrentTenantId();
        String[] aryId=ids.split(",");
        List<String> list= Arrays.asList(aryId);
        osUserServiceImpl.delByIds(list,tenantId);

        JsonResult result=JsonResult.getSuccessResult("??????"+getComment()+"??????!");
        return result;
    }







    @ApiOperation(value = "??????id????????????")
    @AuditLog(operation = "??????id????????????")
    @PostMapping("/users-anon/updateByDdId")
    public void updateByDdId(@ApiParam @RequestParam(value = "username") String username,
                             @ApiParam @RequestParam(value = "ddId") String ddId) throws ApiException {
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        String detail="????????????:"+ osUser.getFullName() +"("+username+")????????????";
        LogContext.put(Audit.DETAIL, detail);

        if(BeanUtil.isNotEmpty(osUser)){
            osUser.setDdId(ddId);
            osUserServiceImpl.updateUser(osUser);
        }
    }


    @ApiOperation(value = "??????????????????")
    @GetMapping("/getAdmin")
    public List<OsUser> getAdmin() {
        String tenantId=getCurrentTenantId();
        List<OsUser> users= osUserServiceImpl.getAdmin(tenantId);
        return users;
    }


    @ApiOperation(value = "??????????????????")
    @AuditLog(operation = "??????????????????")
    @PostMapping("/setAdmin")
    public JsonResult setAdmin(@RequestParam(value = "userId") String userId,
                               @RequestParam(value = "admin") Integer admin) {
        StringBuilder detail=new StringBuilder( "??????????????????:");
        String tenantId=getCurrentTenantId();
        detail.append(( admin.equals(2)?"????????????:":"????????????:") +"??????ID:" + tenantId +",??????ID???:"+ userId +"??????:" );
        osInstUsersService.setAdmin(userId,tenantId,admin);
        LogContext.put(Audit.DETAIL,detail.toString());
        LogContext.put(Audit.ACTION,"setAdmin");
        String message=admin.equals(2)?"????????????????????????!":"????????????????????????!";
        return JsonResult.Success(message);
    }


    @MethodDefine(title = "??????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????",varName = "username"),
                    @ParamDefine(title = "??????",varName = "password")})
    @ApiOperation(value = "??????????????????")
    @GetMapping("/resetPassword")
    public JsonResult resetPassword(@ApiParam @RequestParam String username, @ApiParam @RequestParam String password) {
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        if(osUser==null){
            return JsonResult.getFailResult("??????????????????");
        }

        return osUserServiceImpl.setPassword(osUser.getUserId(), password);

    }

    /**
     * ????????????????????????????????????????????????
     * @param relTypeKey
     * @param party1
     * @return
     */
    @GetMapping({"getUserByRelTypeKeyParty1"})
    List<OsUserDto> getUserByRelTypeKeyParty1(@RequestParam("relTypeKey") String relTypeKey,
                                    @RequestParam("party1") String party1){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("REL_TYPE_KEY_",relTypeKey);
        wrapper.eq("PARTY1_",party1);

        List<OsRelInst> osRelInsts=osRelInstServiceImpl.getRepository().selectList(wrapper);
        List<String> userIds=new ArrayList<>();
        for(OsRelInst inst:osRelInsts){
            userIds.add(inst.getParty2());
        }
        List<OsUserDto> userDtos=getUserDto(userIds);
        return userDtos;
    }

    /**
     * ????????????????????????????????????????????????
     * <pre>
     *     1.??????part1 ,part2 ????????????ID
     *     2. ?????????ID??????PART1 ???????????????????????????
     *     3. ?????????????????????
     * </pre>
     * @param relTypeKey
     * @param party1
     * @return
     */
    @GetMapping({"getUserByRelTypePart12"})
    List<OsUserDto> getUserByRelTypePart12(@RequestParam("relTypeKey") String relTypeKey,
                                    @RequestParam("party1") String party1,
                                    @RequestParam("party2")String party2){
        String[] aryPart2=party2.split(",");
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("REL_TYPE_KEY_",relTypeKey);
        wrapper.eq("PARTY1_",party1);
        wrapper.in("PARTY2_",aryPart2);

        List<OsRelInst> relInsts=osRelInstServiceImpl.getRepository().selectList(wrapper);

        List<String> instList=new ArrayList<>();
        for(OsRelInst inst:relInsts){
            instList.add(inst.getInstId());
        }


        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("PARTY1_",instList);

        List<OsRelInst> osRelInsts=osRelInstServiceImpl.getRepository().selectList(queryWrapper);
        List<String> userIds=new ArrayList<>();
        for(OsRelInst inst:osRelInsts){
            userIds.add(inst.getParty2());
        }
        List<OsUserDto> userDtos=getUserDto(userIds);

        return userDtos;
    }

    private List<OsUserDto> getUserDto(List<String> userIds){
        List<OsUser> users = getBaseService().getByIds(userIds);
        List<OsUserDto> userDtos=new ArrayList<>();
        for(OsUser osUser:users){
            OsUserDto osUserDto = new OsUserDto();
            if(BeanUtil.isNotEmpty(osUser)) {
                BeanUtil.copyProperties(osUserDto, osUser);
            }
            userDtos.add(osUserDto);
        }
        return userDtos;

    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getAllUserByGroupId", method = HttpMethodConstants.POST)
    @ApiOperation(value = "????????????????????????????????????")
    @PostMapping("getAllUserByGroupId")
    public JsonPageResult getAllUserByGroupId(@ApiParam @RequestBody QueryData queryData) {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("??????????????????!");
        try {
            Map<String, String> params = queryData.getParams();
            QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
            params.put("tenantId",ContextUtil.getCurrentTenantId());
            IPage<OsUser> osUserIPage = osUserServiceImpl.getAllUserByGroupId(filter, params);
            jsonResult.setPageData(osUserIPage);

        } catch (Exception ex) {
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }

        return jsonResult;
    }

    /**
     * ????????????Id ???????????????????????????
     * @param appId ??????Id
     * @return
     */
    @MethodDefine(title = "????????????Id ???????????????????????????", path = "/getMenusByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????Id", varName = "appId")})
    @ApiOperation(value = "????????????Id ???????????????????????????")
    @GetMapping("/getMenusByAppId")
    public List<SysMenuDto> getMenusByAppId(String appId) {
        //????????????Id ???????????????????????????
        List<SysMenuDto> menuDtos = osUserServiceImpl.getMenusByAppId(appId);
        return menuDtos;
    }

    @MethodDefine(title = "?????????????????????", path = "/getAppByCurUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "appType")})
    @ApiOperation("?????????????????????")
    @GetMapping("/getAppByCurUser")
    public List<SysAppDto> getAppByCurUser(int appType){
        return osUserServiceImpl.getAppByCurUser(appType);
    }

    /**
     * ??????token????????????????????????
     *
     * @param token
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @throws
     * @author xtk
     * @date 2021/9/7 17:19
     */
    @GetMapping("/getLoginUserInfoByToken")
    @MethodDefine(title = "??????token????????????????????????", path = "/getLoginUserInfoByToken", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "token", varName = "token")})
    @ApiOperation(value="??????token????????????????????????")
    public JsonResult<JPaasUser> getLoginUserInfo(@RequestParam("token") String token) {
        JsonResult<JPaasUser> jsonResult = new JsonResult(false);
        try {
            log.info("?????????????????????????????????token???{}" + token);
            token = "auth:" + token;
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) redisTemplate.opsForValue().get(token);
            if (oAuth2Authentication != null) {
                Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
                if (userAuthentication != null) {
                    JPaasUser jPaasUser = (JPaasUser) userAuthentication.getPrincipal();
                    jPaasUser = this.getByUsername(jPaasUser.getUsername());
                    jsonResult.setData(jPaasUser);
                }
            }
            jsonResult.setSuccess(Boolean.TRUE);
            jsonResult.setCode(JsonResult.SUCESS_CODE);
            jsonResult.setMessage("???????????????");
        } catch (Exception e) {
            log.error("?????????????????????????????????{}", e);
            jsonResult.setMessage("?????????????????????????????????");
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.FAIL_CODE);
        }
        return jsonResult;
    }

    @MethodDefine(title = "????????????", path = "/importData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @PostMapping("/importData")
    @ApiOperation("????????????")
    @AuditLog(operation = "????????????")
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
            String message = osUserServiceImpl.importData(maps, override);
            result=JsonResult.Success(message);
            LogContext.put(Audit.DETAIL,message);
            result.setShow(false);
            return result;
        }catch (RuntimeException e){
            JsonResult failResult=JsonResult.Fail(e.getMessage());
            failResult.setShow(false);
            failResult.setData(e.getStackTrace());
            return  failResult;
        }
    }

    @MethodDefine(title = "??????????????????", path = "/unlockAccount", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "userId")})
    @ApiOperation(value = "??????????????????")
    @AuditLog(operation = "??????????????????")
    @PostMapping ("/unlockAccount")
    public JsonResult unlockAccount( @RequestParam("userId") String userId){
        return  osUserServiceImpl.unlockAccount(userId);
    }


    @MethodDefine(title = "??????????????????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "?????????",varName = "username"),
                    @ParamDefine(title = "??????",varName = "password")})
    @ApiOperation(value = "??????????????????????????????")
    @GetMapping("/changePassword")
    public JsonResult changePassword(@RequestParam("userId") String userId, @RequestParam("password") String password) {

        return osUserServiceImpl.changePassword(userId, password);

    }

    /**
     * ??????openId???platformType???tenantId????????????
     */
    @GetMapping(value = "/findByOpenId")
    @MethodDefine(title = "??????token????????????????????????",path = "/findByOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "openId", varName = "openId"),
                    @ParamDefine(title = "platformType", varName = "????????????????????????1??????????????????2???????????????3?????????4??????"),
                    @ParamDefine(title = "tenantId", varName = "??????id")})
    @ApiOperation(value = "???????????????????????????")
    public JPaasUser findByOpenId(@RequestParam(name = "openId") String openId,
                                  @RequestParam(name = "platformType") Integer platformType,
                                  @RequestParam(name = "tenantId") String tenantId) {
        return  osUserServiceImpl.findByOpenId(openId, platformType, tenantId);
    }

    @GetMapping("getBpmAdminGroup")
    public JsonResult getBpmAdminGroup(){
        JsonResult result=JsonResult.Success();
        String bpmAdminGroup=SysPropertiesUtil.getString("bpmAdminGroup");
        if(StringUtils.isEmpty(bpmAdminGroup)){
            return result;
        }
        OsGroup osGroup=osGroupServiceImpl.getByKey(bpmAdminGroup);
        if(BeanUtil.isEmpty(osGroup)){
            return result;
        }
        result.setData(osGroup);
        return result;
    }

    @MethodDefine(title = "???????????????????????????????????????", path = "/queryUserAccess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "queryData")})
    @ApiOperation(value = "???????????????????????????????????????")
    @PostMapping("queryUserAccess")
    public JsonPageResult queryUserAccess(@RequestBody QueryData queryData) {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("??????????????????!");
        jsonResult.setPageData(new Page());
        QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
        Map<String, Object> params = filter.getParams();
        params.put("relTypeId","1");
        params.put("tenantId",ContextUtil.getCurrentTenantId());
        String bpmAdminGroup=SysPropertiesUtil.getString("bpmAdminGroup");
        if(StringUtils.isEmpty(bpmAdminGroup)){
            return jsonResult;
        }
        OsGroup osGroup=osGroupServiceImpl.getByKey(bpmAdminGroup);
        if(BeanUtil.isEmpty(osGroup)){
            return jsonResult;
        }
        params.put("groupId",osGroup.getGroupId());
        IPage page = osRelInstServiceImpl.getUserAccessList(filter,params);
        List<OsRelInst> relInstList=page.getRecords();
        for(OsRelInst relInst:relInstList){
            OsGroup mainGroup=osGroupServiceImpl.getMainDeps(relInst.getParty2(),ContextUtil.getCurrentTenantId());
            if(mainGroup!=null) {
                relInst.setDeptName(mainGroup.getName());
            }
        }
        jsonResult.setPageData(page);
        return jsonResult;
    }
}
