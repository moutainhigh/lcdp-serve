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
 * 用户Controller
 * @author 作者 yjy
 * 用户
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osUser")
@ClassDefine(title = "用户模块",alias = "osUserController",path = "/user/org/osUser",packages = "org",packageName = "组织架构")
@Api(tags = "用户模块api")
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
        return "用户";
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
     * 重写父类的查询方法，实现按用户组Id过滤用户
     * @return
     * @throws Exception
     */
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult= JsonPageResult.getSuccess("返回数据成功!");
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
            //逻辑删除
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
     * 当前登录用户 LoginAppUser
     *
     * @return
     */
    @MethodDefine(title = "根据token获取当前用户", path = "/current", method = HttpMethodConstants.GET)
    @ApiOperation(value = "根据token获取当前用户")
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
     * 获取当前用户信息与菜单
     *
     * @return
     */
    @MethodDefine(title = "获取当前用户信息与菜单", path = "/getLoginUserAndMenu", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取当前用户信息与菜单")
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
     * 查询用户登录对象LoginAppUser
     */
    @GetMapping(value = "/findByUsername", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public JPaasUser findByUsername(@RequestParam(name = "username") String username) {
        return  osUserServiceImpl.findByUsername(username);
    }



    /**
     * 查询用户登录对象LoginAppUser
     */
    @GetMapping(value = "/findByUsernameAndTenantId",  params={"username","tenantId"})
    @ApiOperation(value = "根据用户名和租户id查询用户")
    public JPaasUser findByUsernameAndTenantId(@RequestParam(name = "username") String username, @RequestParam(name = "tenantId") String tenantId) {
        return  osUserServiceImpl.findByUsernameAndTenantId(username, tenantId);
    }



    @MethodDefine(title = "根据ID查询用户实体", path = "/getUserById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID",varName = "userId")})
    @ApiOperation("根据ID查询用户实体")
    @GetMapping("/getUserById")
    public JPaasUser getUserById(@ApiParam @RequestParam(value = "userId") String userId){
        return osUserServiceImpl.getUserById(userId);
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据手机号查询用户")
    public JPaasUser findByMobile(String mobile) {
        return  osUserServiceImpl.findByMobile(mobile);
    }



    @MethodDefine(title = "根据用户名查询用户实体", path = "/getByUsername", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户名",varName = "username")})
    @ApiOperation("根据用户名查询用户实体")
    @GetMapping("/getByUsername")
    public JPaasUser getByUsername(@ApiParam @RequestParam String username){
        return osUserServiceImpl.findByUsername(username);
    }

    @MethodDefine(title = "根据用户账号查询用户实体", path = "/getByAccount", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户账号",varName = "account")})
    @ApiOperation("根据用户账号查询用户实体")
    @GetMapping("/getByAccount")
    public OsUserDto getByAccount(@ApiParam @RequestParam String account){
        return osUserServiceImpl.getByAccount(account);
    }

    @MethodDefine(title = "根据企业微信账号获取用户", path = "/getByWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "微信定义ID",varName = "wxOpenId")})
    @ApiOperation(value = " 根据企业微信账号获取用户")
    @GetMapping("/getByWxOpenId")
    public JPaasUser getByWxOpenId(@ApiParam @RequestParam String wxOpenId){
        return osUserServiceImpl.getByWxOpenId(wxOpenId);
    }

    @MethodDefine(title = "检验其他用户是否已经存在当前企业微信账号", path = "/isOtherUserContainWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户名",varName = "username"),@ParamDefine(title = "微信定义ID",varName = "wxOpenId")})
    @ApiOperation(value = "检验其他用户是否已经存在当前企业微信账号")
    @GetMapping("/isOtherUserContainWxOpenId")
    public String isOtherUserContainWxOpenId(@ApiParam @RequestParam String username,@ApiParam @RequestParam String wxOpenId){
        return osUserServiceImpl.isOtherUserContainWxOpenId(username,wxOpenId);
    }

    @MethodDefine(title = "绑定企业微信账号", path = "/updateByWxOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户名",varName = "username"),@ParamDefine(title = "微信定义ID",varName = "wxOpenId")})
    @ApiOperation(value = "绑定企业微信账号")
    @GetMapping("/updateByWxOpenId")
    @AuditLog(operation = "绑定企业微信账号")
    public void updateByWxOpenId(@ApiParam @RequestParam String username,@ApiParam @RequestParam String wxOpenId){
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        if(BeanUtil.isNotEmpty(osUser)){
            osUser.setWxOpenId(wxOpenId);
            osUserServiceImpl.updateUser(osUser);
        }
    }



    @MethodDefine(title = "根据用户ID获取用户数据", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID",varName = "userId")})
    @ApiOperation(value = "根据用户ID获取用户数据")
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
     * 将用户对象转成DTO对象。
     * @param groupId
     * @return
     */
    @MethodDefine(title = "根据组ID获取用户集合", path = "/getByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组ID",varName = "groupId")})
    @ApiOperation(value = "根据组ID获取用户集合")
    @GetMapping("/getByGroupId")
    public List<OsUserDto> getByGroupId(@ApiParam @RequestParam(value = "groupId") String groupId) {
        List<OsUser> osUsers = osUserServiceImpl.getBelongUsers(groupId);
        List<OsUserDto> osUserDtos = osUserServiceImpl.convertOsUsers(osUsers);
        return osUserDtos;
    }

    @MethodDefine(title = "获得当前用户所在上下级的人员路径", path = "/getUserUpLowPath", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID",varName = "userId")})
    @ApiOperation(value = "获得当前用户所在上下级的人员路径")
    @GetMapping("/getUserUpLowPath")
    public String getUserUpLowPath(@ApiParam @RequestParam String userId){
        List<OsRelInst> upLowRelInsts = osRelInstServiceImpl.getByRelTypeIdParty2(OsRelType.REL_CAT_USER_UP_LOWER_ID,userId);
        if(upLowRelInsts.size()==1){
            return upLowRelInsts.get(0).getPath();
        }
        return "";
    }

    @MethodDefine(title = "获取用户直属上级用户ID集合", path = "/getDupUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "人员路径",varName = "upLowPath")})
    @ApiOperation(value = "获取用户直属上级用户ID集合")
    @GetMapping("/getDupUserIds")
    public List<String> getDupUserIds(@ApiParam @RequestParam("upLowPath")String upLowPath){
        List<String> userIds=new ArrayList<>();
        List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdPath(OsRelType.REL_CAT_USER_UP_LOWER_ID,upLowPath);
        for(OsRelInst osRelInst:list){
            userIds.add(osRelInst.getParty1());
        }
        return userIds;
    }

    @MethodDefine(title = "获取用户直属下级用户ID集合", path = "/getDdownUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID",varName = "userId")})
    @ApiOperation(value = "获取用户直属下级用户ID集合")
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

    @MethodDefine(title = "获取用户所有下级用户ID集合", path = "/getDownUserIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "人员路径",varName = "upLowPath")})
    @ApiOperation(value = "获取用户所有下级用户ID集合")
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
    @ApiOperation(value = "保存用户信息")
    @AuditLog(operation = "保存用户信息")
    public JsonResult save(@ApiParam @RequestBody OsUser entity, BindingResult validResult) throws Exception{
        JsonResult jsonResult;
        //新增时，状态默认为在职
        if(StringUtils.isEmpty(entity.getUserId())){
            entity.setStatus(OsUser.STATUS_IN_JOB);
            if(StringUtils.isEmpty(entity.getTenantId())) {
                entity.setTenantId(ContextUtil.getCurrentTenantId());
            }
        }
        //如果租户ID为空，那么就设置为当前登录用户的租户ID
        if(StringUtils.isNotEmpty(entity.getUserId())){
            OsUser originUser=osUserServiceImpl.getById(entity.getUserId());
            entity.setCurTenantId(entity.getTenantId());
            entity.setTenantId(originUser.getTenantId());
        }


        boolean isExist = osUserServiceImpl.isUserNotExist(entity);
        if(isExist){
            jsonResult=new JsonResult().setMessage("账号重复!").setSuccess(false);
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

    @MethodDefine(title = "加入用户", path = "/joinUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "关系类型ID", varName = "relTypeId"),@ParamDefine(title = "用户ID", varName = "userIds")})
    @ApiOperation(value="加入用户")
    @AuditLog(operation = "加入用户")
    @PostMapping("/joinUser")
    public JsonResult joinUser(@RequestParam(required = false)String groupId,@RequestParam(required = false)String relTypeId,@RequestParam(required = false)String userIds){

        StringBuilder sb=new StringBuilder();
        sb.append("将用户:");

        OsGroup group=osGroupServiceImpl.get(groupId);
        String dimId= group.getDimId();
        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(relTypeId)){
            return JsonResult.getFailResult("请选择用户组和关系类型！");
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
            sb.append("加入到组:" +group.getName() +"的" +osRelType.getName() +"关系类型中已存在");
            LogContext.put(Audit.DETAIL,sb.toString());
            return JsonResult.getSuccessResult("用户已存在！");
        }
        sb.append("加入到组:" +group.getName() +"的" +osRelType.getName() +"关系类型中成功");
        LogContext.put(Audit.DETAIL,sb.toString());
        return JsonResult.getSuccessResult("成功加入！");
    }

    @MethodDefine(title = "获取用户编辑页面所需参数", path = "/edit", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "pkId"),@ParamDefine(title = "组ID", varName = "groupId"),@ParamDefine(title = "机构ID",varName = "tenantId")})
    @ApiOperation(value="用户编辑页面", notes="获取用户编辑页面所需参数")
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
        //其他部门
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
            jsonObject.put("dimName", "其他组");
            canGroupIds.add(jsonObject);
        }
        if(StringUtils.isNotEmpty(pkId)) {
            //主部门
            mainDep = osGroupServiceImpl.getMainDeps(pkId, tenantId);
            if (mainDep != null) {
                mv.put("mainDepId", mainDep.getGroupId());
                mv.put("mainDepName", mainDep.getName());
            }
            canGroups= osGroupServiceImpl.getCanGroups(pkId,tenantId);
        }

        //传入了用户组
        if(StringUtils.isNotEmpty(groupId)){
            mainDep= osGroupServiceImpl.get(groupId);
            //行政维度
            if(mainDep!=null && mainDep.getDimId()!=null){
                OsDimension dim = osDimensionServiceImpl.get(mainDep.getDimId());
                if(OsDimension.DIM_ADMIN.equals(dim.getCode())){
                    //主部门
                    mv.put("mainDepId", mainDep.getGroupId());
                    mv.put("mainDepName", mainDep.getName());
                }else{//为其他
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
     * 设置用户组
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
     *  获取某个部门（多个）下某个角色（多个）下的用户Id列表
     * @return
     */
    @MethodDefine(title = "获取某个部门（多个）下某个角色（多个）下的用户Id列表", path = "/getUserIdsByGroupIdsRoleIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组Ids", varName = "groupIds"),@ParamDefine(title = "角色Ids", varName = "roleIds")})
    @GetMapping("/getUserIdsByGroupIdsRoleIds")
    public List<String> getUserIdsByGroupIdsRoleIds(@RequestParam("groupIds") String groupIds,
                                                    @RequestParam("roleIds") String roleIds){
//        if(StringUtils.isEmpty(groupIds) || StringUtils.isEmpty(roleIds)){
//            return Result.failed("groupIds或roleIds参数值不能为空！");
//        }
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByGroupIdsRoleIds(groupIds,roleIds);
        return userIdList;
    }

    /**
     * 查找某用户属于用户组1，用户组2下的所有用户Ids
     * @return
     */
    @MethodDefine(title = "查找某用户属于用户组1，用户组2下的所有用户Ids", path = "/getByUserIdsByGroupId1GroupId2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组Id1", varName = "groupId1"),@ParamDefine(title = "组Id2", varName = "groupId2")})
    @GetMapping("/getUserIdsByGroupId1GroupId2")
    public List<String> getUserIdsByGroupId1GroupId2(@RequestParam("groupId1") String groupId1,
                                              @RequestParam("groupId2") String groupId2){
//        if(StringUtils.isEmpty(groupId1) || StringUtils.isEmpty(groupId2)){
//            return Result.failed("groupId1 或 groupId2 参数值不能为空！");
//        }
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByGroupId1GroupId2(groupId1,groupId2);
        return userIdList;
    }

    /**
     * 查找与用户组有某种关系的用户Id清单
     * @param groupId 用户组ID
     * @param relTypeKey 用户组类型
     * @return
     */
    @MethodDefine(title = "查找与用户组有某种关系的用户Id清单", path = "/getUserIdsByGroupIdRelTypeKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组Id", varName = "groupId"),@ParamDefine(title = "关系标识Key", varName = "relTypeKey")})
    @GetMapping("/getUserIdsByGroupIdRelTypeKey")
    public List<String> getUserIdsByGroupIdRelTypeKey(@RequestParam("groupId") String groupId,
                                                @RequestParam("relTypeKey") String relTypeKey){
//        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(relTypeKey)){
//            return Result.failed("groupId 或 relTypeKey 参数值不能为空！");
//        }
        // 查找用户
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByParty1RelTypeKey(groupId,relTypeKey);
        return userIdList;
        //return Result.succeed(userIdList);
    }

    /**
     * 查找与某用户有某种关系的用户Id清单
     * @param userId 用户ID
     * @param relTypeKey 用户组类型
     * @return
     */
    @MethodDefine(title = "查找与某用户有某种关系的用户Id清单", path = "/getUserIdsByUserIdRelTypeKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId"),@ParamDefine(title = "关系标识Key", varName = "relTypeKey")})
    @GetMapping("/getUserIdsByUserIdRelTypeKey")
    public List<String> getUserIdsByUserIdRelTypeKey(@RequestParam("userId") String userId,
                                                     @RequestParam("relTypeKey") String relTypeKey){
//        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(relTypeKey)){
//            return Result.failed("userId 或 relTypeKey 参数值不能为空！");
//        }
        // 查找用户
        List<String> userIdList = osRelInstServiceImpl.getUserIdsByParty1RelTypeKey(userId,relTypeKey);
        return userIdList;
    }

    /**
     * 列表
     */
    @MethodDefine(title = "查询列表", path = "/list", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "查询数据", varName = "params")})
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping("/list")
    public PageResult list(@RequestParam Map<String, Object> params) {
        return osUserServiceImpl.findUsers(params);
    }

    /**
     * 根据维分组ID查询用户列表
     */
    @MethodDefine(title = "查询维度列表", path = "/getUserListByGroupId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度ID", varName = "dimId")})
    @ApiOperation(value = "查询维度列表")
    @GetMapping("/getUserListByGroupId")
    public Result getDimList(@RequestParam String dimId) {

        List<OsUser> model = osUserServiceImpl.list(new QueryWrapper<OsUser>()
                .eq("DIM_ID_", dimId)
                .orderByDesc("UPDATE_TIME_"));
        return Result.succeed(model);
    }

    /**
     * 更新用户状态为可用状态
     *
     * @param params
     * @return
     */
    @MethodDefine(title = "修改用户状态", path = "/updateEnabled", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "修改数据", varName = "params")})
    @ApiOperation(value = "修改用户状态")
    @AuditLog(operation = "修改用户状态")
    @GetMapping("/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "用户参数", required = true, dataType = "Map<String, Object>")
    })
    public Result updateEnabled(@RequestParam Map<String, Object> params) {
        String id = MapUtils.getString(params, "id");
        JPaasUser user= (JPaasUser) ContextUtil.getCurrentUser();
        if(user.getUserId().equals(id) ){
            return Result.failed("自己不能修改自己的状态!");
        }
        if(user.isAdmin()){
            return Result.failed("不能修改超管的状态!");
        }

        return osUserServiceImpl.updateEnabled(params);
    }
    /**
     * 导出excel
     *
     * @return
     */
    @MethodDefine(title = "导出EXCEL", path = "/export", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "导出EXCEL", varName = "params")})
    @ApiOperation(value = "导出EXCEL")
    @PostMapping("/export")
    public void exportUser(@RequestParam Map<String, Object> params) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        List<SysUserExcel> result = osUserServiceImpl.findAllUsers(params);
        //导出操作
        ExcelUtil.exportExcel(result, null, "用户", SysUserExcel.class, "user", response);
    }


    @MethodDefine(title = "计算流程人员", path = "/getUsersByTaskExecutor", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程人员", varName = "executors")})
    @ApiOperation(value = "计算流程人员")
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

    @MethodDefine(title = "根据userId获取用户", path = "/getUsersByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户ID", varName = "userIds")})
    @ApiOperation(value = "根据userId获取用户")
    @PostMapping ("/getUsersByIds")
    public List<OsUserDto> getUsersByIds( @RequestParam("userIds") String userIds){
        return osUserServiceImpl.getByUsers(userIds);
    }

    @MethodDefine(title = "离职用户", path = "/dimissionByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户ID", varName = "ids"),@ParamDefine(title = "离职时间", varName = "quitTime")})
    @ApiOperation(value = "离职用户")
    @AuditLog(operation = "离职用户")
    @PostMapping ("/dimissionByIds")
    public JsonResult dimissionByIds( @RequestParam("ids") String ids,@RequestParam("quitTime") String quitTime){
        try {
            String[] userIds=ids.split(",");
            String detail="用户:";
            for(String userId:userIds){
                OsUser osUser=osUserServiceImpl.getById(userId);
                detail+= osUser.getFullName() +"("+osUser.getUserNo()+"),";
            }
            detail+="离职";
            LogContext.put(Audit.DETAIL,detail);

            JsonResult jsonResult=osUserServiceImpl.setStatusAndQuitTime(userIds,quitTime);
            return jsonResult;
        } catch (Exception e) {
            //失败
            LogContext.put(Audit.DETAIL,ExceptionUtil.getExceptionMessage(e));
            return new JsonResult().setMessage("操作失败!").setSuccess(false);
        }
    }

    @MethodDefine(title = "用户修改个人密码", path = "/updatePassword", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "旧密码", varName = "oldPwd"),@ParamDefine(title = "新密码", varName = "newPwd")})
    @ApiOperation(value = "用户修改个人密码")
    @AuditLog(operation = "用户修改个人密码")
    @PostMapping ("/updatePassword")
    public JsonResult updatePassword( @RequestParam("oldPwd") String oldPwd,@RequestParam("newPwd") String newPwd){
        IUser user=ContextUtil.getCurrentUser();
        LogContext.put(Audit.PK,user.getUserId());
        String detail=user.getFullName() +"("+user.getAccount()+")修改个人密码";
        LogContext.put(Audit.DETAIL,detail);

        JsonResult jsonResult = osUserServiceImpl.updatePassword(user.getUserId(), oldPwd, newPwd);
        return jsonResult;
    }

    @MethodDefine(title = "设置用户密码", path = "/setPassword", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户ID", varName = "userId"),@ParamDefine(title = "新密码", varName = "newPwd")})
    @ApiOperation(value = "设置用户密码")
    @AuditLog(operation = "设置用户密码")
    @PostMapping ("/setPassword")
    public JsonResult setPassword( @RequestParam("userId") String userId,@RequestParam("newPwd") String newPwd){
        OsUser osUser=osUserServiceImpl.getById(userId);
        LogContext.put(Audit.PK,osUser.getUserId());
        String detail= "修改"+osUser.getFullName()+"("+osUser.getUserNo()+")密码";
        LogContext.put(Audit.DETAIL,detail);

        JsonResult jsonResult = osUserServiceImpl.setPassword(userId, newPwd);
        return jsonResult;
    }

    @ApiOperation(value="删除用户")
    @AuditLog(operation = "删除用户")
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

        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");
        return result;
    }







    @ApiOperation(value = "钉钉id绑定用户")
    @AuditLog(operation = "钉钉id绑定用户")
    @PostMapping("/users-anon/updateByDdId")
    public void updateByDdId(@ApiParam @RequestParam(value = "username") String username,
                             @ApiParam @RequestParam(value = "ddId") String ddId) throws ApiException {
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        String detail="绑定用户:"+ osUser.getFullName() +"("+username+")钉钉信息";
        LogContext.put(Audit.DETAIL, detail);

        if(BeanUtil.isNotEmpty(osUser)){
            osUser.setDdId(ddId);
            osUserServiceImpl.updateUser(osUser);
        }
    }


    @ApiOperation(value = "获取超管用户")
    @GetMapping("/getAdmin")
    public List<OsUser> getAdmin() {
        String tenantId=getCurrentTenantId();
        List<OsUser> users= osUserServiceImpl.getAdmin(tenantId);
        return users;
    }


    @ApiOperation(value = "设置超管用户")
    @AuditLog(operation = "设置超管用户")
    @PostMapping("/setAdmin")
    public JsonResult setAdmin(@RequestParam(value = "userId") String userId,
                               @RequestParam(value = "admin") Integer admin) {
        StringBuilder detail=new StringBuilder( "设置超管用户:");
        String tenantId=getCurrentTenantId();
        detail.append(( admin.equals(2)?"设置超管:":"删除超管:") +"租户ID:" + tenantId +",用户ID为:"+ userId +"动作:" );
        osInstUsersService.setAdmin(userId,tenantId,admin);
        LogContext.put(Audit.DETAIL,detail.toString());
        LogContext.put(Audit.ACTION,"setAdmin");
        String message=admin.equals(2)?"添加超管用户成功!":"删除超管用户成功!";
        return JsonResult.Success(message);
    }


    @MethodDefine(title = "重置用户密码", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户名",varName = "username"),
                    @ParamDefine(title = "密码",varName = "password")})
    @ApiOperation(value = "重置用户密码")
    @GetMapping("/resetPassword")
    public JsonResult resetPassword(@ApiParam @RequestParam String username, @ApiParam @RequestParam String password) {
        OsUser osUser = osUserServiceImpl.getByUsername(username);
        if(osUser==null){
            return JsonResult.getFailResult("用户名不存在");
        }

        return osUserServiceImpl.setPassword(osUser.getUserId(), password);

    }

    /**
     * 根据用户组关系类型获取用户列表。
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
     * 根据用户组关系类型获取用户列表。
     * <pre>
     *     1.根据part1 ,part2 获取实例ID
     *     2. 将实例ID作为PART1 进行查询用户列表。
     *     3. 返回用户数据。
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

    @MethodDefine(title = "查看该用户组及子组的用户", path = "/getAllUserByGroupId", method = HttpMethodConstants.POST)
    @ApiOperation(value = "查看该用户组及子组的用户")
    @PostMapping("getAllUserByGroupId")
    public JsonPageResult getAllUserByGroupId(@ApiParam @RequestBody QueryData queryData) {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
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
     * 根据应用Id 查询当前用户的菜单
     * @param appId 应用Id
     * @return
     */
    @MethodDefine(title = "根据应用Id 查询当前用户的菜单", path = "/getMenusByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用Id", varName = "appId")})
    @ApiOperation(value = "根据应用Id 查询当前用户的菜单")
    @GetMapping("/getMenusByAppId")
    public List<SysMenuDto> getMenusByAppId(String appId) {
        //根据应用Id 查询当前用户的菜单
        List<SysMenuDto> menuDtos = osUserServiceImpl.getMenusByAppId(appId);
        return menuDtos;
    }

    @MethodDefine(title = "获取当前人应用", path = "/getAppByCurUser", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用类型", varName = "appType")})
    @ApiOperation("获取当前人应用")
    @GetMapping("/getAppByCurUser")
    public List<SysAppDto> getAppByCurUser(int appType){
        return osUserServiceImpl.getAppByCurUser(appType);
    }

    /**
     * 根据token获取登录用户信息
     *
     * @param token
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     * @throws
     * @author xtk
     * @date 2021/9/7 17:19
     */
    @GetMapping("/getLoginUserInfoByToken")
    @MethodDefine(title = "根据token获取登录用户信息", path = "/getLoginUserInfoByToken", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "token", varName = "token")})
    @ApiOperation(value="根据token获取登录用户信息")
    public JsonResult<JPaasUser> getLoginUserInfo(@RequestParam("token") String token) {
        JsonResult<JPaasUser> jsonResult = new JsonResult(false);
        try {
            log.info("获取登录用户信息，参数token：{}" + token);
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
            jsonResult.setMessage("查询成功！");
        } catch (Exception e) {
            log.error("获取登录用户信息出错：{}", e);
            jsonResult.setMessage("获取登录用户信息出错！");
            jsonResult.setSuccess(false);
            jsonResult.setCode(JsonResult.FAIL_CODE);
        }
        return jsonResult;
    }

    @MethodDefine(title = "用户导入", path = "/importData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户导入", varName = "request")})
    @PostMapping("/importData")
    @ApiOperation("用户导入")
    @AuditLog(operation = "用户导入")
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

    @MethodDefine(title = "解锁用户账号", path = "/unlockAccount", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation(value = "解锁用户账号")
    @AuditLog(operation = "解锁用户账号")
    @PostMapping ("/unlockAccount")
    public JsonResult unlockAccount( @RequestParam("userId") String userId){
        return  osUserServiceImpl.unlockAccount(userId);
    }


    @MethodDefine(title = "用户首次登录修改密码", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户名",varName = "username"),
                    @ParamDefine(title = "密码",varName = "password")})
    @ApiOperation(value = "用户首次登录修改密码")
    @GetMapping("/changePassword")
    public JsonResult changePassword(@RequestParam("userId") String userId, @RequestParam("password") String password) {

        return osUserServiceImpl.changePassword(userId, password);

    }

    /**
     * 根据openId、platformType、tenantId查询用户
     */
    @GetMapping(value = "/findByOpenId")
    @MethodDefine(title = "根据token获取登录用户信息",path = "/findByOpenId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "openId", varName = "openId"),
                    @ParamDefine(title = "platformType", varName = "第三方平台类型：1微信公众号，2企业微信，3钉钉，4飞书"),
                    @ParamDefine(title = "tenantId", varName = "租户id")})
    @ApiOperation(value = "根据用户名查询用户")
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

    @MethodDefine(title = "根据条件查询流程管理员记录", path = "/queryUserAccess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value = "根据条件查询流程管理员记录")
    @PostMapping("queryUserAccess")
    public JsonPageResult queryUserAccess(@RequestBody QueryData queryData) {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
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
