package com.redxun.user.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.PageResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsRelInst;
import com.redxun.user.org.entity.OsRelType;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsRelInstServiceImpl;
import com.redxun.user.org.service.OsRelTypeServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 关系实例 提供者
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osRelInst")
@ClassDefine(title = "关系实例",alias = "osRelInstController",path = "/user/org/osRelInst",packages = "org",packageName = "组织架构")
@Api(tags = "关系实例")
public class OsRelInstController extends BaseController<OsRelInst> {

    @Autowired
    OsRelInstServiceImpl osRelInstServiceImpl;
    @Autowired
    OsRelTypeServiceImpl osRelTypeServiceImpl;
    @Autowired
    OsUserServiceImpl osUserServiceImpl;

    @Override
    public BaseService getBaseService() {
        return osRelInstServiceImpl;
    }

    @Override
    public String getComment() {
        return "关系实例";
    }

    @MethodDefine(title = "根据关系定义KEY和当前方ID查找关系数据", path = "/getByRelTypeKeyParty1", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义KEY",varName = "relTypeKey"),@ParamDefine(title = "当前方ID",varName = "party1")})
    @ApiOperation("根据关系定义KEY和当前方ID查找关系数据")
    @GetMapping("/getByRelTypeKeyParty1")
    public List<OsRelInst> getByRelTypeKeyParty1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1){
        String tenantId= ContextUtil.getCurrentTenantId();
        OsRelType osRelType=osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
        return osRelInstServiceImpl.getByRelTypeIdParty1(tenantId,osRelType.getId(),party1);
    }

    @MethodDefine(title = "根据关系定义KEY和当前方ID和维度查找关系数据", path = "/getByRelTypeKeyParty1AndDim1", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义KEY",varName = "relTypeKey"),@ParamDefine(title = "当前方ID",varName = "party1"),@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation("根据关系定义KEY和当前方ID和维度查找关系数据")
    @GetMapping("/getByRelTypeKeyParty1AndDim1")
    public List<OsRelInst> getByRelTypeKeyParty1AndDim1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1,@RequestParam("dimId")String dimId){
        String tenantId= ContextUtil.getCurrentTenantId();
        OsRelType osRelType=osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
        return osRelInstServiceImpl.getByRelTypeIdParty1AndDim1(osRelType.getId(),party1,dimId);
    }

    @MethodDefine(title = "根据关系定义KEY和关联方ID查找关系数据", path = "/getByRelTypeKeyParty2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义KEY",varName = "relTypeKey"),@ParamDefine(title = "关联方ID",varName = "party2")})
    @ApiOperation("根据关系定义KEY和关联方ID查找关系数据")
    @GetMapping("/getByRelTypeKeyParty2")
    public List<OsRelInst> getByRelTypeKeyParty2(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party2")String party2){
        String tenantId= ContextUtil.getCurrentTenantId();
        OsRelType osRelType=osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
        return osRelInstServiceImpl.getByRelTypeIdParty2(osRelType.getId(),party2);
    }

    @MethodDefine(title = "根据关系定义KEY和关联方ID和维度ID查找关系数据", path = "/getByRelTypeKeyParty2AndDim1", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义KEY",varName = "relTypeKey"),@ParamDefine(title = "关联方ID",varName = "party2"),@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation("根据关系定义KEY和关联方ID和维度ID查找关系数据")
    @GetMapping("/getByRelTypeKeyParty2AndDim1")
    public List<OsRelInst> getByRelTypeKeyParty2AndDim1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party2")String party2,@RequestParam("dimId")String dimId){
        String relTypeId="";
        //关系定义KEY为空时 则查关联方该维度下的组织
        if(StringUtils.isNotEmpty(relTypeKey)){
            String tenantId= ContextUtil.getCurrentTenantId();
            OsRelType osRelType =osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
            relTypeId=osRelType.getId();
        }
        return osRelInstServiceImpl.getByRelTypeIdParty2AndDim1(relTypeId,party2,dimId);
    }

    @MethodDefine(title = "根据关系类型ID和组ID查找用户数据", path = "/getUserListByGroupIdAndRelTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "查询条件",varName = "params")})
    @ApiOperation("根据关系类型ID和组ID查找用户数据")
    @GetMapping("/getUserListByGroupIdAndRelTypeId")
    public PageResult getUserListByGroupIdAndRelTypeId(@RequestParam Map<String, Object> params) {
        if (("").equals(params.get("tenantId"))||params.get("tenantId")==null){
            String tenantId=getCurrentTenantId();
            params.put("tenantId",tenantId);
        }
        return osRelInstServiceImpl.getUserListByGroupIdAndRelTypeId(params);
    }

    @MethodDefine(title = "根据关系类型ID和组ID查找用户组数据", path = "/getGroupListByGroupIdAndRelTypeId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "查询条件",varName = "params")})
    @ApiOperation("根据关系类型ID和组ID查找用户组数据")
    @GetMapping("/getGroupListByGroupIdAndRelTypeId")
    public PageResult getGroupListByGroupIdAndRelTypeId(@RequestParam Map<String, Object> params) {
        String tenantId=getCurrentTenantId();
        params.put("tenantId",tenantId);
        return osRelInstServiceImpl.getGroupListByGroupIdAndRelTypeId(params);
    }

    @MethodDefine(title = "移除用户关系", path = "/unjoinRelInst", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体ID集合",varName = "ids"),@ParamDefine(title = "关系类型ID",varName = "relTypeId"),@ParamDefine(title = "组ID",varName = "groupId")})
    @ApiOperation(value="移除用户关系", notes="根据实体Id删除用户关系")
    @AuditLog(operation = "移除用户关系")
    @PostMapping("unjoinRelInst")
    public JsonResult unjoinRelInst(@RequestParam String ids, @RequestParam String relTypeId, @RequestParam String groupId){
        JsonResult result=JsonResult.getSuccessResult("移除成功！");
        //用户关系:
        osRelInstServiceImpl.unjoinRelInst(ids,relTypeId,groupId);
        return result;
    }

    @MethodDefine(title = "获取用户关系内的用户数据", path = "/groupRelUsersData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "当前方ID",varName = "p1"),@ParamDefine(title = "关联方ID",varName = "p2"),@ParamDefine(title = "关系类型ID",varName = "relTypeId")})
    @ApiOperation(value="获取用户关系内的用户数据")
    @PostMapping("groupRelUsersData")
    public JsonPageResult groupRelUsersData(@RequestParam(value = "p1") String p1,
                                            @RequestParam(value = "p2") String p2,
                                            @RequestParam(value = "relTypeId") String relTypeId){
        String tenantId= getCurrentTenantId();
        //主用户列表
        OsRelInst osRelInst= osRelInstServiceImpl.getByParty1Party2RelTypeId(p1,p2,relTypeId);
        List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdParty1(tenantId,OsRelType.REL_CAT_REL_USER_ID,osRelInst.getInstId());
        List<OsUser> users=new ArrayList<>();
        for(OsRelInst osRelInst1:list){
            users.add(osUserServiceImpl.get(osRelInst1.getParty2()));
        }
        JsonPageResult result=new JsonPageResult();
        result.setData(users);
        return result;
    }

    @MethodDefine(title = "将用户添加到用户组关系中", path = "/joinTwoGroupUsers", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "当前方ID",varName = "p1"),
                    @ParamDefine(title = "关联方ID",varName = "p2"),
                    @ParamDefine(title = "关系类型ID",varName = "relTypeId"),
                    @ParamDefine(title = "用户ID集合",varName = "userIds")})
    @ApiOperation(value="将用户添加到用户组关系中")
    @AuditLog(operation="将用户添加到用户组关系中")
    @PostMapping("joinTwoGroupUsers")
    public JsonResult joinTwoGroupUsers(@RequestParam(value = "p1") String p1,
                                        @RequestParam(value = "p2") String p2,
                                        @RequestParam(value = "relTypeId") String relTypeId,
                                        @RequestParam(value = "userIds") String userIds){
        String[] uIds=userIds.split("[,]");

        String tenantId=getCurrentTenantId();

        StringBuilder sb=new StringBuilder();

        //加入日志
        sb.append("将用户:");

        OsRelInst osRelInst= osRelInstServiceImpl.getByParty1Party2RelTypeId(p1,p2,relTypeId);
        String instId=osRelInst.getInstId();
        OsRelType osRelType= osRelTypeServiceImpl.get(OsRelType.REL_CAT_REL_USER_ID);
        for(String uId:uIds){
            OsRelInst osRelInst1= osRelInstServiceImpl.getByParty1Party2RelTypeId(instId,uId,OsRelType.REL_CAT_REL_USER_ID);
            if(osRelInst1!=null){
                continue;
            }
            //加入日志
            OsUser osUser=osUserServiceImpl.get(uId);
            sb.append(osUser.getFullName() +"("+osUser.getUserNo()+"),");

            OsRelInst inst = new OsRelInst();
            inst.setInstId(IdGenerator.getIdStr());
            inst.setParty1(instId);
            inst.setParty2(uId);
            inst.setPath("0." + inst.getParty1() + "." + inst.getParty2() + ".");
            inst.setIsMain(MBoolean.NO.name());
            inst.setStatus(MBoolean.ENABLED.name());
            inst.setRelTypeId(osRelType.getId());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeKey(osRelType.getKey());
            inst.setTenantId(tenantId);
            osRelInstServiceImpl.save(inst);

        }
        sb.append("加入到:" +osRelInst.getAlias() +"关系中!" );

        LogContext.put(Audit.DETAIL,sb.toString());

        return JsonResult.getSuccessResult("成功加入用户！");
    }

    @MethodDefine(title = "将用户从组关系类型中删除", path = "/delByTwoPartyUserId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "当前方ID",varName = "p1"),@ParamDefine(title = "关联方ID",varName = "p2"),@ParamDefine(title = "关系类型ID",varName = "relTypeId"),@ParamDefine(title = "用户ID",varName = "userId")})
    @ApiOperation(value="将用户从组关系类型中删除")
    @AuditLog(operation = "将用户从组关系类型中删除")
    @PostMapping("delByTwoPartyUserId")
    public JsonResult delByTwoPartyUserId(@RequestParam String p1, @RequestParam String p2,
                                        @RequestParam String relTypeId,@RequestParam String userId){

        OsRelInst osRelInst= osRelInstServiceImpl.getByParty1Party2RelTypeId(p1,p2,relTypeId);

        StringBuilder sb=new StringBuilder();
        OsUser osUser=osUserServiceImpl.get(userId);
        sb.append("将用户:"+ osUser.getFullName() +"("+osUser.getUserNo()+")从组关系类型："+osRelInst.getAlias()+"移除!");

        LogContext.put(Audit.DETAIL,sb.toString());

        String instId=osRelInst.getInstId();
        osRelInstServiceImpl.delByParty1Party2RelTypeId(instId,userId,OsRelType.REL_CAT_REL_USER_ID);

        return JsonResult.getSuccessResult("成功移除关系！");
    }

    @MethodDefine(title = "添加用户上下级关系", path = "/addRelInsts", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "关系数据",varName = "relation"),
                    @ParamDefine(title = "用户ID集合",varName = "userIds"),
                    @ParamDefine(title = "关系ID",varName = "instId")})
    @ApiOperation(value="添加用户上下级关系")
    @AuditLog(operation="添加用户上下级关系")
    @PostMapping("addRelInsts")
    public JsonResult addRelInsts(@RequestParam String relation,
                                  @RequestParam String userIds,
                                  @RequestParam String instId) throws Exception{
        JSONObject relationObj = JSONObject.parseObject(relation);
        String relTypeId = relationObj.getString("pkId");
        String relType = relationObj.getString("relType");
        String relTypeKey= relationObj.getString("key");
        String path="";
        String party1="";
        boolean flag=false;

        StringBuilder sb=new StringBuilder();

        if(!"".equals(instId)){
            //选中的关系定义
            OsRelInst selectRelInst = osRelInstServiceImpl.get(instId);
            party1=selectRelInst.getParty2();

            OsUser osUser=osUserServiceImpl.get(party1);
            sb.append("给:" + osUser.getFullName() );

            path=selectRelInst.getPath();
        }
        if("".equals(party1)){
            party1=relTypeId;
            flag=true;
            OsRelType osRelType= osRelTypeServiceImpl.get(relTypeId);
            sb.append("在:" + osRelType.getName());
        }

        List<String> userIdlist = JSONArray.parseArray(userIds, String.class);
        sb.append(",添加下级:"  );
        for (String userId : userIdlist) {
            String userPath=path;
            if(!flag) {
                OsUser osUser = osUserServiceImpl.get(party1);
                sb.append(osUser.getFullName() + "(" + osUser.getUserNo() + ")");
            }
            OsRelInst relInst=new OsRelInst();
            relInst.setPkId(IdGenerator.getIdStr());
            relInst.setParty2(userId);
            boolean isExist=osRelInstServiceImpl.isPartyExistInRelation(relTypeId, userId);
            if(isExist){
                return new JsonResult(false,"你选择的用户在该关系中已经存在，请先移除该用户再更改！");
            }

            //判断当前关系方是否已经出现在上级的里，以使关系不要形成闭环.自身不跟自身建立关系
            boolean inParent=osRelInstServiceImpl.isExistInParent(party1,userId,relTypeId);
            if(inParent){
                continue;
            }

            relInst.setParty1(party1);
            if("".equals(userPath)){
                userPath="0."+userId+".";
            }else {
                userPath=userPath+userId+".";
            }
            relInst.setPath(userPath);
            relInst.setRelTypeId(relTypeId);
            relInst.setRelTypeKey(relTypeKey);
            relInst.setRelType(relType);
            relInst.setStatus("ENABLED");
            relInst.setIsMain("NO");
            osRelInstServiceImpl.insert(relInst);
        }

        //添加日志
        LogContext.put(Audit.DETAIL,sb.toString());

        return new JsonResult(true,"成功创建了关系节点！");
    }

    @MethodDefine(title = "根据关系定义Id获取关系定义树状数据", path = "/getTreeData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "关系类型ID",varName = "relTypeId")})
    @ApiOperation(value="根据关系定义Id获取关系定义树状数据")
    @PostMapping("getTreeData")
    public List<OsRelInst> getTreeData(@RequestParam String relTypeId) throws Exception{
        String tenantId= ContextUtil.getCurrentTenantId();
        List<OsRelInst> osRelInstList = osRelInstServiceImpl.getByRelTypeIdParty1(tenantId,relTypeId, relTypeId);
        osRelInstServiceImpl.getRelInstChildren(tenantId,relTypeId,osRelInstList);
        return osRelInstList;
    }

    @MethodDefine(title = "根据关系定义Id级联删除关系定义", path = "/changeRelation", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "用户ID",varName = "userId"),@ParamDefine(title = "关系ID",varName = "instId")})
    @ApiOperation(value="根据关系定义Id级联删除关系定义")
    @AuditLog(operation = "更改用户的关系")
    @PostMapping("changeRelation")
    public JsonResult changeRelation(@RequestParam String userId,@RequestParam String instId) throws Exception{
        OsRelInst osRelInst=osRelInstServiceImpl.get(instId);
        if(StringUtils.isNotEmpty(userId)){
            boolean isExist=osRelInstServiceImpl.isPartyExistInRelation(osRelInst.getRelTypeId(), userId);
            if(isExist){
                return new JsonResult(false,"你选择的用户在该关系中已经存在，请先移除该用户再更改！");
            }
        }
        OsUser osUser=osUserServiceImpl.get(userId);

        LogContext.put(Audit.PK,userId);
        LogContext.put(Audit.DETAIL,"修改用户:"+ osUser.getFullName() +"的用户关系!");

        osRelInstServiceImpl.doChangeInst(instId,userId);
        return new JsonResult(true,"关系节点更换成功！");
    }

    @MethodDefine(title = "通过关系实例ID级联删除用户关系", path = "/delRelInsts", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "关系ID",varName = "instId")})
    @ApiOperation(value="通过关系实例ID级联删除用户关系")
    @AuditLog(operation = "通过关系实例ID级联删除用户关系")
    @PostMapping("delRelInsts")
    public JsonResult delRelInsts(@RequestParam String instId) throws Exception{
        OsRelInst osRelInst=osRelInstServiceImpl.get(instId);

        String userId=osRelInst.getParty2();
        OsUser user=osUserServiceImpl.get(userId);
        LogContext.put(Audit.DETAIL,"删除:" +user.getFullName() +"当前及下级用户关系实例!");

        osRelInstServiceImpl.delInstCascade(osRelInst);
        return new JsonResult(true,"成功删除了关系节点！");
    }

    @MethodDefine(title = "转移用户关系", path = "/transferRelation", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "转移关系ID",varName = "transferRelInstId"),@ParamDefine(title = "被转移关系ID",varName = "instId")})
    @ApiOperation(value="转移用户关系")
    @AuditLog(operation = "转移用户关系")
    @PostMapping("transferRelation")
    public JsonResult transferRelation(@RequestParam String transferRelInstId,@RequestParam String instId) throws Exception{
        //需要转移的关系
        OsRelInst osRelInst=osRelInstServiceImpl.get(instId);

        LogContext.put(Audit.DETAIL,"将关系ID:"+instId +"转移到:"+ transferRelInstId +"下");

        //目标为主节点的情况
        if("".equals(transferRelInstId)){
            return osRelInstServiceImpl.transferToMain(osRelInst);
        }else {
            //需要转移到此关系下
            OsRelInst transferRel=osRelInstServiceImpl.get(transferRelInstId);
            if(BeanUtil.isNotEmpty(transferRel)){
                boolean isExist=osRelInstServiceImpl.isPartyExistInRelation(osRelInst.getRelTypeId(), transferRel.getParty2());
                if(!isExist){
                    return new JsonResult(false,"你选择的用户在该关系中不存在，请先添加该用户关系再转移！");
                }
            }
            return osRelInstServiceImpl.transferRelation(osRelInst,transferRel);
        }

    }
    @MethodDefine(title = "新增或更新组关系", path = "/saveOrUpdate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体数据JSON", varName = "osRelInst")})
    @ApiOperation(value="新增或更新组关系")
    @AuditLog(operation = "新增或更新组关系")
    @PostMapping("saveOrUpdate")
    public JsonResult saveOrUpdate(@Validated @ApiParam @RequestBody OsRelInst osRelInst, BindingResult validResult) throws Exception{
        OsRelInst inst = osRelInstServiceImpl.getByParty1Party2RelTypeId(osRelInst.getParty1(),osRelInst.getParty2(),osRelInst.getRelTypeId());
        if(inst!=null){
            //1、新增时校验party1、party2和relTypeId是否已经存在关系
            //2、更新时校验party1、party2和relTypeId是否已经存在关系，并且不能是自己
            if(StringUtils.isEmpty(osRelInst.getInstId())
                    || (StringUtils.isNotEmpty(osRelInst.getInstId()) && !osRelInst.getInstId().equals(inst.getInstId())) ){
                return JsonResult.getFailResult("组关系已存在！");
            }
        }

        return this.save(osRelInst,validResult);
    }

    @MethodDefine(title = "根据关系定义KEY和当前方ID查找关系数据", path = "/getGroupRelExecutorCalc", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "关系定义KEY",varName = "relTypeKey"),@ParamDefine(title = "当前方ID",varName = "party1"),@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation("根据关系定义KEY和当前方ID查找关系数据")
    @GetMapping("/getGroupRelExecutorCalc")
    public List<OsUserDto> getGroupRelExecutorCalc(@RequestParam("relTypeKey")String relTypeKey, @RequestParam("party1")String party1, @RequestParam("dimId")String dimId){
        String tenantId= ContextUtil.getCurrentTenantId();
        List<OsUserDto> users=new ArrayList<>();
        OsRelType osRelType=osRelTypeServiceImpl.getByKey(tenantId,relTypeKey);
        List<OsRelInst> parentList = osRelInstServiceImpl.getByRelTypeIdParty1AndDim1(osRelType.getId(), party1, dimId);
        if(BeanUtil.isEmpty(parentList)){
            return users;
        }
        for (OsRelInst osRelInst:parentList) {
            List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdParty1(
                    tenantId,OsRelType.REL_CAT_REL_USER_ID,osRelInst.getInstId());
            for(OsRelInst osRelInst1:list){
                OsUser user = osUserServiceImpl.get(osRelInst1.getParty2());
                if(BeanUtil.isNotEmpty(user)){
                    users.add(osUserServiceImpl.convertOsUser(user));
                }
            }
        }
        return users;
    }

    @MethodDefine(title = "根据组织部门ID，关联维度ID，用户组关系类型ID获取用户", path = "/getUserByGroupIdAndRelTypeIdAndParty2", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "组织部门ID",varName = "groupId"),@ParamDefine(title = "关联维度ID",varName = "relTypeId"),@ParamDefine(title = "用户组关系类型ID",varName = "party2"),@ParamDefine(title = "维度ID",varName = "dimId")})
    @ApiOperation("根据组织部门ID，关联维度ID，用户组关系类型ID获取用户数据")
    @GetMapping("/getUserByGroupIdAndRelTypeIdAndParty2")
    public List<OsUserDto> getUserByGroupIdAndRelTypeIdAndParty2(@RequestParam("groupId")String groupId,@RequestParam("relTypeId")String relTypeId,@RequestParam("party2")String party2,@RequestParam("dimId")String dimId){
        String tenantId= ContextUtil.getCurrentTenantId();
        List<OsUserDto> users=new ArrayList<>();

        Map<String, Object> params=new HashMap<>();
        params.put("tenantId",tenantId);
        params.put("groupId",groupId);
        params.put("relTypeId",relTypeId);
        params.put("party2",party2);
        params.put("dimId",dimId);
        OsRelInst osRelInst =osRelInstServiceImpl.getGroupListByGroupIdAndRelTypeIdAndParty2(params);
        if(BeanUtil.isEmpty(osRelInst)){
            return users;
        }

        List<OsRelInst> list= osRelInstServiceImpl.getByRelTypeIdParty1(
                tenantId,OsRelType.REL_CAT_REL_USER_ID,osRelInst.getInstId());
        for(OsRelInst osRelInst1:list){
            OsUser user = osUserServiceImpl.get(osRelInst1.getParty2());
            if(BeanUtil.isNotEmpty(user)){
                users.add(osUserServiceImpl.convertOsUser(user));
            }
        }
        return users;
    }

    @MethodDefine(title = "根据关系类型ID和组ID查找用户数据", path = "/getUserListByGroupIdAndRelTypeIdAndDimId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "维度Id",varName = "dimId"),@ParamDefine(title = "组织部门ID",varName = "groupId"),@ParamDefine(title = "关联维度ID",varName = "relTypeId")})
    @ApiOperation("根据关系类型ID和组ID查找用户数据")
    @GetMapping("/getUserListByGroupIdAndRelTypeIdAndDimId")
    public List<OsUserDto> getUserListByGroupIdAndRelTypeIdAndDimId(@RequestParam("dimId")String dimId,@RequestParam("groupId")String groupId,@RequestParam("relTypeId")String relTypeId) {
        List<OsUserDto> users=new ArrayList<>();
        String tenantId=getCurrentTenantId();
        List<OsRelInst> list= osRelInstServiceImpl.getUserListByGroupIdAndRelTypeIdAndDimId(dimId,groupId,relTypeId,tenantId);
        for(OsRelInst osRelInst1:list){
            OsUser user = osUserServiceImpl.get(osRelInst1.getParty2());
            if(BeanUtil.isNotEmpty(user)){
                users.add(osUserServiceImpl.convertOsUser(user));
            }
        }
        return users;
    }
}
