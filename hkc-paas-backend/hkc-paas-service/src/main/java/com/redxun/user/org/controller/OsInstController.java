package com.redxun.user.org.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsInstDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.user.org.entity.OsInst;
import com.redxun.user.org.entity.OsInstUsers;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsInstServiceImpl;
import com.redxun.user.org.service.OsInstUsersServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 注册机构 提供者
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osInst")
@ClassDefine(title = "注册机构",alias = "osInstController",path = "/user/org/osInst",packages = "org",packageName = "组织架构")
@Api(tags = "注册机构")
public class OsInstController extends BaseController<OsInst> {

    @Autowired
    OsInstServiceImpl osInstServiceImpl;
    @Autowired
    OsInstUsersServiceImpl osInstUsersService;
    @Autowired
    OsUserServiceImpl osUserService;
    @Autowired
    OsGroupServiceImpl osGroupService;

    @Override
    public BaseService getBaseService() {
        return osInstServiceImpl;
    }

    @Override
    public String getComment() {
        return "租户管理";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_a.DELETED__S_EQ","0");
        }
    }

    @MethodDefine(title = "通过机构ID查找机构信息", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "机构ID",varName = "instId")})
    @ApiOperation("通过机构ID查找机构信息")
    @GetMapping("/getById")
    public OsInstDto getById(@ApiParam @RequestParam(value = "instId") String instId) {
        OsInst osInst = osInstServiceImpl.getById(instId);
        OsInstDto osInstDto = new OsInstDto();
        if(BeanUtil.isNotEmpty(osInst)) {
            BeanUtil.copyProperties(osInstDto, osInst);
        }
        return osInstDto;
    }

    @MethodDefine(title = "获取机构直属下级机构ID集合", path = "/getDdownTenantIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "机构ID",varName = "instId")})
    @ApiOperation("获取机构直属下级机构ID集合")
    @GetMapping("/getDdownTenantIds")
    public List<String> getDdownTenantIds(@ApiParam @RequestParam("instId")String instId){
        List<String> tenantIds=new ArrayList<>();
        List<OsInst> list= osInstServiceImpl.getByParentId(instId);
        for(OsInst osInst:list){
            tenantIds.add(osInst.getInstId());
        }
        return tenantIds;
    }

    @MethodDefine(title = "获取机构所有下级机构ID集合", path = "/getDownTenantIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "机构ID",varName = "instId")})
    @ApiOperation("获取机构所有下级机构ID集合")
    @GetMapping("/getDownTenantIds")
    public List<String> getDownTenantIds(@ApiParam @RequestParam("instId")String instId){
        List<String> tenantIds=new ArrayList<>();
        OsInst inst= osInstServiceImpl.getById(instId);
        //若当前的目录路径为空,则让他找不到数据
        String pPath= StringUtils.isEmpty(inst.getPath())?"-1":inst.getPath();
        List<OsInst> list= osInstServiceImpl.getByPath(pPath);
        for(OsInst osInst:list){
            tenantIds.add(osInst.getInstId());
        }
        return tenantIds;
    }

    @PostMapping(value = "/applyInstData")
    public JsonPageResult applyInstData(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            IPage page= osInstUsersService.getByDomain(filter);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    /**
     * 查询申请机构列表
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询申请机构列表", path = "/applyListData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="查询申请机构列表", notes="查询申请机构列表")
    @PostMapping(value="/applyListData")
    public JsonPageResult applyListData(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            IUser user= ContextUtil.getCurrentUser();
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            IPage page= osInstServiceImpl.getByUserIdAndStatus(filter,null);

            for(int i=0;page.getRecords()!=null&&i<page.getRecords().size();i++){
                OsInst osInst = (OsInst) page.getRecords().get(i);
                if((osInst.getInstId().equals(user.getTenantId())) || ("CREATE".equals(osInst.getMoreInstCreateType()))){
                    osInst.setPresent(true);
                }else{
                    osInst.setPresent(false);
                }
            }
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @GetMapping("/getList")
    public JsonResult getList(){
        List<OsInst> list=osInstServiceImpl.getList();
        return JsonResult.getSuccessResult(list);
    }

    @GetMapping("/getMyApplyList")
    public JsonResult getMyApplyList(){
        List<OsInst> list=osInstServiceImpl.getByUserIdAndStatus(ContextUtil.getCurrentUserId(),OsUser.STATUS_IN_JOB);
        return JsonResult.getSuccessResult(list);
    }

    @PostMapping("/saveSelectInst")
    public JsonResult saveSelectInst(@RequestBody List<OsInst> sysInstList) {
        IUser user=ContextUtil.getCurrentUser();
        for (OsInst sysInst: sysInstList) {
            OsInstUsers oldUsers=osInstUsersService.getByUserTenant(user.getUserId(),sysInst.getInstId());
            if(oldUsers!=null){
                //已有申请则不重复添加
                return new JsonResult(false,"申请处理失败，用户已存在该机构！");
            }
            OsInstUsers osInstUsers = new OsInstUsers();
            osInstUsers.setUserId(user.getUserId());
            osInstUsers.setTenantId(sysInst.getInstId());
            osInstUsers.setStatus(OsUser.STATUS_OUT_JOB);
            osInstUsers.setIsAdmin(0);
            osInstUsers.setApplyStatus("APPLY");
            osInstUsers.setCreateType("APPLY");
            osInstUsers.setApplyNote(sysInst.getMoreInstNote());
            osInstUsersService.insert(osInstUsers);
        }
        return new JsonResult(true,"申请处理成功，请等待管理员审批！");
    }

    @GetMapping("/quitOrCancel")
    public JsonResult quitOrCancel(@RequestParam("instId")String instId,@RequestParam("moreInstStatus")String moreInstStatus){
        String name = "APPLY".equals(moreInstStatus)?"取消申请操作成功":"退出操作成功";
        IUser user=ContextUtil.getCurrentUser();

        osInstUsersService.removeByUserId(user.getUserId(),instId);
        if("ENABLED".equals(moreInstStatus)){
            osUserService.updateTenantIdFromDomain(user.getUserId(),null);
        }
        return new JsonResult(true,name);
    }

    @GetMapping("/updateCurUserTenantId")
    public JsonResult updateCurUserTenantId(@RequestParam("tenantId")String tenantId){
        osInstServiceImpl.updateCurUserTenantId(tenantId);
        return new JsonResult(true);
    }

    @PostMapping("/agreeOrRefuse")
    public JsonResult agreeOrRefuse(@RequestBody OsInstUsers osInstUsers) {
        IUser user=ContextUtil.getCurrentUser();
        String note = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",new Date());
        note = note+" 由"+user.getFullName()+"["+user.getAccount()+"]审批："+osInstUsers.getApplyNote();
        osInstUsers.setApplyNote(note);
        osInstUsers.setApproveUser(user.getUserId());
        if("1".equals(osInstUsers.getIsAgree())){
            osInstUsers.setStatus(OsUser.STATUS_IN_JOB);
            osInstUsers.setApplyStatus("ENABLED");
        }else{
            osInstUsers.setStatus(OsUser.STATUS_OUT_JOB);
            osInstUsers.setApplyStatus("ENABLED");
        }
        osInstUsersService.agreeOrRefuse(osInstUsers,user.getTenantId());
        return new JsonResult(true,"操作成功");
    }

    @Override
    protected JsonResult beforeSave(OsInst ent) {
        OsInst inst1=osInstServiceImpl.getByInstNo(ent.getInstNo());
        //机构存在，并且不是原机构记录
        if(inst1!=null && !inst1.getInstId().equals(ent.getInstId())){
            return JsonResult.Fail("机构编码("+ent.getInstNo()+")已存在!");
        }
        OsInst inst2=osInstServiceImpl.getByDomain(ent.getDomain());
        if(inst2!=null && !inst2.getInstId().equals(ent.getInstId())){
            return JsonResult.Fail("机构域名("+ent.getDomain()+")已经存在！");
        }
        return super.beforeSave(ent);
    }

    /**
     * 根据域名获取机构id
     */
    @GetMapping(value = "/findInstIdByDomain", params = "domain")
    @ApiOperation(value = "根据机构域名查找机构ID")
    public String findInstIdByDomain(@RequestParam(name = "domain") String domain) {
        return  osInstServiceImpl.findInstIdByDomain(domain);
    }

    /**
     * 根据域名获取机构id
     */
    @GetMapping(value = "/findInstIdByInstNo", params = "instNo")
    @ApiOperation(value = "根据机构名称查找机构ID")
    public String findInstIdByInstNo(@RequestParam(name = "instNo") String instNo) {
        return  osInstServiceImpl.findInstIdByInstNo(instNo);
    }

    @ApiOperation(value = "注册机构（租户），同时创建一个登录用户")
    @MethodDefine(title = "注册机构（租户）", path = "/register", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "注册机构（租户）", varName = "inst")})
    @AuditLog(operation = "注册机构（租户）")
    @PostMapping(value = "/register")
    public JsonResult register(@ApiParam @RequestBody OsInst inst){
        if(inst==null || StringUtils.isEmpty(inst.getInstNo()) || StringUtils.isEmpty(inst.getNameCn())
                || StringUtils.isEmpty(inst.getInstType())  || StringUtils.isEmpty(inst.getPhone())
                || StringUtils.isEmpty(inst.getEmail())  || StringUtils.isEmpty(inst.getAccount())  || StringUtils.isEmpty(inst.getPassword())
        ){
            String msg="机构编号、机构中文名、机构类型、手机号、Email、登录账号、密码";
            return  JsonResult.Fail(msg);
        }
        return  osInstServiceImpl.register(inst);
    }

    @ApiOperation(value = "查询机构（租户）数据")
    @MethodDefine(title = "查询机构（租户）数据", path = "/listInst", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询机构（租户）数据", varName = "instIds")})
    @AuditLog(operation = "查询机构（租户）数据")
    @GetMapping("/listInst")
    public List<OsInstDto> listInst(@ApiParam @RequestParam(value = "instIds") String instIds){
        List<OsInst> list;
        if (StringUtils.isNotEmpty(instIds)) {
            List<String> idList = Arrays.asList(instIds.split(","));
            list = osInstServiceImpl.getByIds(idList);
        } else {
            list = osInstServiceImpl.getList();
        }
        if(list != null && list.size() > 0) {
           return BeanUtil.copyList(list, OsInstDto.class);
        }
        return null;
    }


}
