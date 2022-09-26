package com.redxun.user.restapi;

import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.annotation.CurrentUser;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.SortParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.controller.RestApiController;
import com.redxun.idempotence.IdempotenceRequired;
import com.redxun.log.annotation.AuditLog;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.mapper.OsRelInstMapper;
import com.redxun.user.org.mapper.OsRelTypeMapper;
import com.redxun.user.org.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织用户相关外部API
 */
@Slf4j
@RestController
@RequestMapping("/restApi/user")
@ClassDefine(title = "组织用户外部API", alias = "orgApiController",path = "/restApi/user",packageName = "用户组织接口")
public class OrgApiController implements RestApiController {
    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;
    @Autowired
    OsRelTypeServiceImpl osRelTypeServiceImpl;
    @Autowired
    OsRelInstServiceImpl osRelInstServiceImpl;
    @Autowired
    OsUserServiceImpl osUserService;

    @Autowired
    OsInstServiceImpl osInstService;

    @Autowired
    OsInstUsersServiceImpl osInstUsersService;
    @Resource
    OsRelInstMapper osRelInstMapper;
    @Resource
    OsRelTypeMapper osRelTypeMapper;

    private JsonResult validJson(JSONObject json){
        if(BeanUtil.isEmpty(json)){
            return  JsonResult.Fail("上传数据不能为空!").setShow(false);
        }
        String account = json.getString("account");
        if(StringUtils.isEmpty(account)){
            return JsonResult.Fail("没有传入账号!").setShow(false);
        }
        String tenantId = json.getString("tenantId");
        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.Fail("没有传入租户ID!").setShow(false);
        }
        return JsonResult.Success();

    }

    /**
     * 格式：
     * {
     * tenantId:"租户编码",
     * account:"同步账号",
     * groups:[{
     *      id:"",---用户组id,唯一标识
     *      code:"",----用户组业务主键
     *      name:"",---用户组名称
     *      parentId:""--上级id,
     *      dimId:"",
     *      status:""
     * }]
     * }
     *
     * @param groupJson
     * @return
     */
    @MethodDefine(title = "同步用户组", path = "/addOrUpdGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户组账号", varName = "groupJson")})
    @AuditLog(operation = "同步用户组")
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/addOrUpdGroups")
    public JsonResult addOrUpateGroups(@RequestBody JSONObject groupJson) {

        try {
            JsonResult result= validJson(groupJson);
            if(!result.isSuccess()){
                return  result;
            }
            String tenantId = groupJson.getString("tenantId");
            OsInst osInst=osInstService.get(tenantId);
            if(osInst==null){
                return JsonResult.Fail("租户传入错误!").setShow(false);
            }

            JSONArray groupJsonList = groupJson.getJSONArray("groups");
            if (BeanUtil.isEmpty(groupJsonList)) {
                return JsonResult.Fail("用户组数据为空").setShow(false);
            }


            saveGroups(tenantId, groupJsonList);
        } catch (Exception e) {
            return JsonResult.Fail("同步用户组失败：" + e.getMessage()).setShow(false);
        }
        return JsonResult.Success("同步用户组数据成功");
    }

    private OsGroup constructGroup(JSONObject groupJson){
        String groupId = groupJson.getString("id");
        String groupKey = groupJson.getString("code");
        String name = groupJson.getString("name");
        String dimId = groupJson.getString("dimId");
        String parentId = groupJson.getString("parentId");
        String status = groupJson.getString("status");

        if(StringUtils.isEmpty(dimId)){
            dimId= OsDimension.DIM_ADMIN_ID;
        }

        if (StringUtils.isEmpty(parentId)) {
            parentId = "0";
        }

        OsGroup group = new OsGroup();
        group.setGroupId(groupId);
        group.setKey(groupKey);
        group.setDimId(dimId);
        group.setName(name);
        group.setParentId(parentId);
        group.setStatus(status);

        return group;

    }

    private void saveGroups(String tenantId, JSONArray groupJsonList) {
        for (int i = 0; i < groupJsonList.size(); i++) {
            JSONObject groupJson = groupJsonList.getJSONObject(i);
            OsGroup group=constructGroup(groupJson);
            group.setTenantId(tenantId);

            String groupId=group.getGroupId();
            String parentId=group.getParentId();

            if (StringUtils.isEmpty(group.getGroupId()) || StringUtils.isEmpty(group.getKey())) {
                continue;
            }
            String path = "0." + groupId + ".";
            if (!"0".equals(parentId)) {
                OsGroup parentGroup = osGroupServiceImpl.get(parentId);
                if (BeanUtil.isNotEmpty(parentGroup)) {
                    path=parentGroup.getPath() + groupId + ".";
                }
            }
            try {
                OsGroup oldGroup = osGroupServiceImpl.getById(groupId);
                if (oldGroup==null) {
                    group.setPath(path);
                    group.setParentId(parentId);
                    group.setSn(0);
                    group.setTenantId(tenantId);
                    group.setStatus("ENABLED");
                    osGroupServiceImpl.insert(group);
                }
                else{
                    String oldPath = oldGroup.getPath();
                    oldGroup.setKey(group.getKey());
                    oldGroup.setParentId(parentId);
                    oldGroup.setName(group.getName());
                    oldGroup.setPath(path);
                    oldGroup.setStatus(group.getStatus());
                    osGroupServiceImpl.update(oldGroup);
                    updateByPath(oldPath, path);
                }

            } catch (Exception e) {
                log.error("---OrgApiController.saveGroups is error---message=" + e.getMessage());
            }
        }
    }

    private void updateByPath(String oldPath, String newPath) {
        List<OsGroup> oldGroups = osGroupServiceImpl.getByLikePath(oldPath + "%");
        for (OsGroup group : oldGroups) {
            String[] paths = group.getPath().split(oldPath);
            if (BeanUtil.isNotEmpty(paths) && paths.length > 1) {
                group.setPath(newPath + paths[1]);
                osGroupServiceImpl.update(group);
            }
        }
    }


    /**
     * 格式：
     * {
     * tenantId:"1"---租户id,
     * account:"账号信息",
     * groupIds:"11,22,333"--删除的用户组ID列表
     * }
     *
     * @param groupJson
     * @return
     */
    @MethodDefine(title = "删除用户组", path = "/deleteGroups", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户组账号", varName = "userJson")})
    @AuditLog(operation = "删除用户组")
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/deleteGroups")
    public JsonResult deleteGroups(@RequestBody JSONObject groupJson) {
        JsonResult jsonResult= validJson(groupJson);
        if(!jsonResult.isSuccess()){
            return jsonResult;
        }

        String tenantId = groupJson.getString("tenantId");


        OsInst osInst=osInstService.get(tenantId);


        String groupIds = groupJson.getString("groupIds");
        if (StringUtils.isEmpty(groupIds)) {
            return JsonResult.Fail("需要删除的用户组数据为空").setShow(false);
        }
        try {
            String[] groupIdList = groupIds.split("[,]");
            for (String groupId : groupIdList) {
                osGroupServiceImpl.delete(groupId);
                deleteOsRelInst(groupId);
            }
        } catch (Exception ex) {
            return JsonResult.Fail("删除用户组成功失败!");
        }
        return JsonResult.Success("删除用户组成功");
    }


    private void deleteOsRelInst(String groupId){
        List<OsRelInst> osRelInsts =osRelInstServiceImpl.getByParty1(groupId,"NO");
        for (OsRelInst relInst:osRelInsts) {
            String instId=relInst.getInstId();
            osRelInstServiceImpl.delete(instId);
            if(OsRelType.REL_CAT_DEP_POS.equals(relInst.getRelTypeKey())){
                deleteOsRelInst(instId);
            }
        }
    }

    /**
     * 格式：
     * {
     * tenantId:"1"---租户id,
     * account:"操作人账号",
     * userAccounts:"zhangsan,lisi,wangwu"--删除的用户账号列表
     * }
     *
     * @param userJson
     * @return
     */
    @MethodDefine(title = "删除用户", path = "/deleteUsers", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户账号", varName = "userJson")})
    @AuditLog(operation = "删除用户")
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/deleteUsers")
    public JsonResult deleteUsers(@RequestBody JSONObject userJson) {

        JsonResult jsonResult= validJson(userJson);
        if(!jsonResult.isSuccess()){
            return jsonResult;
        }

        String tenantId = userJson.getString("tenantId");
        OsInst osInst=osInstService.get(tenantId);

        if(BeanUtil.isEmpty(osInst)){
            return JsonResult.Fail("没有传入租户信息");
        }

        String userAccounts = userJson.getString("userAccounts");
        if (StringUtils.isEmpty(userAccounts)) {
            return JsonResult.Fail("需要删除的用户数据为空").setShow(false);
        }
        try {
            String[] accountList = userAccounts.split("[,]");
            for (String userNo : accountList) {
                OsUser user = osUserService.getByUsername(userNo);
                if (BeanUtil.isEmpty(user)) {
                    continue;
                }

                String userId = user.getUserId();
                //删除旧的关系
                osRelInstServiceImpl.deleteByParty2AndTenantId(userId, tenantId);
                //删除用户和租户的关系
                QueryWrapper wrapper=new QueryWrapper();
                wrapper.eq("USER_ID_",userId);
                osInstUsersService.getRepository().delete(wrapper);
                //删除用户数据
                osUserService.delete(userId);

            }
        } catch (Exception ex) {
            return JsonResult.Fail("删除用户成功失败!");
        }
        return JsonResult.Success("删除用户成功");
    }


    /**
     * 格式：
     * {
     * tenantId:"",
     * account:"admin",
     * users:[{
     * userId:"",
     * account:"",---用户账号，唯一标识
     * fullname:"",----用户名称
     * sex:"",
     * email:"",
     * mobile:"",
     * qq:"",
     * address:"",
     * ddId:"",
     * wxOpenId:"",---用户所在企业微信账号
     * depId:"mainDepId",--主部门,
     * relations:[{relationType:"2",dimId:"", :"1"}]
     * status:"1" 1.正常,0.离职
     * }]
     * }
     *
     * @param userJson
     * @return
     */
    @MethodDefine(title = "同步用户", path = "/addOrUpdUsers", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户账号", varName = "userJson")})
    @AuditLog(operation = "同步用户")
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/addOrUpdUsers")
    public JsonResult addOrUpateUsers(@RequestBody JSONObject userJson) {
        JsonResult result=validJson(userJson);
        if(!result.isSuccess()){
            return result;
        }
        try {
            String tenantId = userJson.getString("tenantId");
            OsInst osInst=osInstService.get(tenantId);
            if(osInst==null){
                return  JsonResult.Fail("租户ID传入错误!");
            }


            JSONArray userJsonList = userJson.getJSONArray("users");
            if (BeanUtil.isEmpty(userJsonList)) {
                return JsonResult.Fail("用户数据为空").setShow(false);
            }


            saveUsers(tenantId, userJsonList);
        } catch (Exception e) {
            return JsonResult.Fail("同步用户失败：" + e.getMessage()).setShow(false);
        }
        return JsonResult.Success("同步用户数据成功");
    }

    private void saveUsers(String tenantId, JSONArray userJsonList) {
        try {
            for (int i = 0; i < userJsonList.size(); i++) {
                JSONObject userJson = userJsonList.getJSONObject(i);
                String account = userJson.getString("account");
                if (StringUtils.isEmpty(account)) {
                    continue;
                }
                String depId = userJson.getString("depId");
                JSONArray relations = userJson.getJSONArray("relations");

                OsUser user = JSONObject.toJavaObject(userJson, OsUser.class);
                user.setUserNo(account);
                if (StringUtils.isNotEmpty(depId)) {
                    user.setMainDepId(depId);
                }
                if (BeanUtil.isNotEmpty(relations)) {
                    user.setRelations(relations);
                }
                user.setTenantId(tenantId);
                boolean notExist = osUserService.isUserNotExist(user);

                if (!notExist) {
                    user.setPwd("1");
                    user.setFrom(OsUser.FORM_INTERFACE);
                    osUserService.addUserAndRelations(user);
                } else {
                    //更新用户
                    updateUser(user);

                    String relTypeId= OsRelType.REL_CAT_GROUP_USER_BELONG_ID;
                    /**
                     * 删除主关系。
                     */
                    QueryWrapper wrapper=new QueryWrapper();
                    wrapper.eq("PARTY2_",user.getUserId());
                    wrapper.eq("IS_MAIN_", MBoolean.YES.name());
                    wrapper.eq("REL_TYPE_ID_",relTypeId);
                    wrapper.eq("TENANT_ID_",user.getTenantId());
                    osRelInstMapper.delete(wrapper);

                    //所有关系类型
                    List<OsRelType> osRelTypes = osRelTypeMapper.selectList(new QueryWrapper<>());
                    Map<String, OsRelType> osRelTypeMap = osRelTypes.stream().collect(Collectors.toMap(OsRelType::getId, OsRelType -> OsRelType));

                    osUserService.createUserRelations(user,osRelTypeMap);
                }
            }
        } catch (Exception e) {
            log.error("---OrgApiController.saveUsers is error---message=" + e.getMessage());
        }
    }

    /**
     * 更新用户
     *
     * @param user
     */
    private void updateUser(OsUser user)   {
        OsUser oldUser = osUserService.getByUsername(user.getUserNo());
        if (BeanUtil.isNotEmpty(oldUser)) {
            cn.hutool.core.bean.BeanUtil.copyProperties(user,oldUser,CopyOptions.create().setIgnoreNullValue(true));

        }
        osUserService.updateUser(oldUser);
    }





    /* {
          "pageNo": 1,
          "pageSize": 10,
          "params": {},
         "sortField": "",
         "sortOrder": "asc"
     }*/
    @MethodDefine(title="根据租户id获取用户列表",path = "/getUsers",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据",varName = "queryData")})
    @IdempotenceRequired
    @PostMapping("/getUsers")
    public JsonPageResult getUsers(@RequestBody QueryData queryData){
        try {
            JsonPageResult jsonResult= JsonPageResult.getSuccess("返回数据成功!");
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addSortParam(new SortParam("create_time_","desc"));
            IPage page = osUserService.query(filter);
            jsonResult.setPageData(page);
            return  jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonPageResult.getFail("获取数据失败!");
        }
    }

    /* {
         "pageNo": 1,
         "pageSize": 10,
         "params": {},
        "sortField": "",TaskExecutor
        "sortOrder": "asc"
    }*/
    @MethodDefine(title="根据租户id获取用户组列表",path = "/getGroups",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据",varName = "queryData")})
    @IdempotenceRequired
    @PostMapping("/getGroups")
    public JsonPageResult getGroups(@RequestBody QueryData queryData){
        try {
            JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addSortParam(new SortParam("create_time_","desc"));
            IPage page = osGroupServiceImpl.query(filter);
            jsonResult.setPageData(page);
            return  jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonPageResult.getFail("获取数据失败!");
        }
    }


    /**
     * 同步租户。
     * [ {nameCn:"",instNo："",status:""} ]
     * @param json
     * @return
     */
    @MethodDefine(title = "同步租户", path = "/syncTenant", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "租户数组", varName = "jsonAry")})
    @AuditLog(operation = "同步租户")
    @IdempotenceRequired
    @PostMapping("/syncTenant")
    public JsonResult syncTenant(@RequestBody String json) {

        try {
            JSONArray jsonArray=JSONArray.parseArray(json);
            saveTenants(jsonArray);
        } catch (Exception e) {
            return JsonResult.Fail("同步租户失败：" + e.getMessage()).setShow(false);
        }
        return JsonResult.Success("同步租户数据成功");
    }


    /**
     * 保存租户
     * @param tenants
     */
    private void saveTenants(JSONArray tenants){
        for (int i = 0; i < tenants.size(); i++) {
            JSONObject tenantJson = tenants.getJSONObject(i);
            if(!tenantJson.containsKey("status")){
                tenantJson.put("status","ENABLED");
            }

            OsInst osInst=JSONObject.toJavaObject(tenantJson, OsInst.class);
            OsInst tenant= osInstService.get(osInst.getInstNo());
            if(tenant==null){
                osInstService.add(osInst);
            }
            else{
                osInstService.updInst(osInst);
            }
        }
    }


}
