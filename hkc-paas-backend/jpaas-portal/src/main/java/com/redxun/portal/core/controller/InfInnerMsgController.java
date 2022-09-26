package com.redxun.portal.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.portal.core.entity.InfInbox;
import com.redxun.portal.core.entity.InfInnerMsg;
import com.redxun.portal.core.service.InfInboxServiceImpl;
import com.redxun.portal.core.service.InfInnerMsgLogServiceImpl;
import com.redxun.portal.core.service.InfInnerMsgServiceImpl;
import com.redxun.portal.feign.OsUserClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/infInnerMsg")
@ClassDefine(title = "内部消息",alias = "infInnerMsgController",path = "/portal/core/infInnerMsg",packages = "core",packageName = "门户管理")
@Api(tags = "内部消息")
public class InfInnerMsgController extends BaseController<InfInnerMsg> {

    @Autowired
    InfInnerMsgServiceImpl infInnerMsgServiceImpl;

    @Autowired
    InfInboxServiceImpl infInboxServiceImpl;

    @Autowired
    InfInnerMsgLogServiceImpl infInnerMsgLogServiceImpl;
    @Resource
    OsUserClient userService;

    @Override
    public BaseService getBaseService() {
        return infInnerMsgServiceImpl;
    }

    @Override
    public String getComment() {
        return "内部短消息";
    }

    @MethodDefine(title = "发送个人内部消息", path = "/sendMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息内容", varName = "jsonObject")})
    @ApiOperation(value="发送个人内部消息")
    @AuditLog(operation = "发送个人内部消息")
    @PostMapping("/sendMsg")
    public JsonResult sendMsg(@RequestBody JSONObject jsonObject) throws Exception{
        return infInnerMsgServiceImpl.sendMsg(jsonObject);
    }

    @MethodDefine(title = "发送系统内部消息", path = "/sendSystemMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息内容", varName = "jsonObject")})
    @ApiOperation(value="发送系统内部消息")
    @AuditLog(operation = "发送系统内部消息")
    @PostMapping("/sendSystemMsg")
    public JsonResult sendSystemMsg(@RequestBody JSONObject jsonObject) throws Exception{
        return infInnerMsgServiceImpl.sendSystemMsg(jsonObject);
    }

    /**
     * 获得收到的消息，不传入条件时，则返回所有的收到的消息
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "根据条件查询所有消息", path = "/queryMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/queryMsg")
    public JsonPageResult queryMsg(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
            String tenantId= ContextUtil.getCurrentTenantId();
            if(StringUtils.isNotEmpty(tenantId)){
                filter.addQueryParam("Q_iim.TENANT_ID__S_EQ",tenantId);
            }
            //过滤未删除的记录
            filter.addQueryParam("Q_iim.DEL_FLAG__S_EQ","no");
            IPage page=infInnerMsgServiceImpl.queryMsg(filter,ContextUtil.getCurrentUserId(),"USER");
            handlePage(page);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    /**
     * 获得收到的组消息
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "获得收到的组消息", path = "/queryGroupMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="获得收到的组消息")
    @PostMapping(value="/queryGroupMsg")
    public JsonPageResult queryGroupMsg(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        QueryFilter queryFilter=QueryFilterBuilder.createQueryFilter(queryData);
        String tenantId= ContextUtil.getCurrentTenantId();
        if(StringUtils.isNotEmpty(tenantId)){
            queryFilter.addQueryParam("Q_iim.TENANT_ID__S_EQ",tenantId);
        }
        List<InfInnerMsg> innerMsgs = new ArrayList<InfInnerMsg>();
        String userId = ContextUtil.getCurrentUserId();// 获取用户Id
        OsUserDto osUserDto = userService.getById(userId);
        List<String> roles = osUserDto.getRoles();

        if (roles.size() > 0) {
            for (String role : roles){
                IPage<InfInnerMsg> innerMsgIPage = infInnerMsgServiceImpl.queryMsg(queryFilter,role,"GROUP");// 获得每个组收到的消息
                innerMsgs.addAll(innerMsgIPage.getRecords());
            }
        }
        IPage ipage=queryFilter.getPage();
        ipage.setRecords(innerMsgs);
        jsonResult.setPageData(ipage);
        return jsonResult;
    }


    /**
     * 获得发送的消息，不传入条件时，则返回所有的发送的消息
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "获得发送的消息", path = "/querySentMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="按条件查询发送的消息", notes="根据条件查询所有记录")
    @PostMapping(value="/querySentMsg")
    public JsonPageResult querySentMsg(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
            handleFilter(filter);
            IPage page=infInnerMsgServiceImpl.querySentMsg(filter,ContextUtil.getCurrentUserId());
            List<InfInnerMsg> infInnerMsgs = page.getRecords();
            //获取收信对象
            List<InfInnerMsg> newInfInnerMsgs=infInnerMsgServiceImpl.getSendObj(infInnerMsgs,filter);
            page.setRecords(newInfInnerMsgs);
            handlePage(page);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @MethodDefine(title = "查看消息", path = "/readMessage", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "jsonObject")})
    @ApiOperation(value="查看消息")
    @AuditLog(operation = "查看消息")
    @PostMapping("/readMessage")
    public void readMessage(@RequestBody JSONObject jsonObject) throws Exception{
        String msgId = jsonObject.getString("msgId");
        LogContext.put(Audit.DETAIL,"消息ID:" +msgId);
        if (StringUtils.isNotEmpty(msgId)) {
            infInnerMsgServiceImpl.readMessage(msgId);
        }
    }

    @MethodDefine(title = "根据recId删除消息", path = "/delByRecIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "jsonObject")})
    @ApiOperation(value="根据recId将接收消息标记为删除")
    @AuditLog(operation = "根据recId")
    @PostMapping("delByRecIds")
    public JsonResult delByRecIds(@RequestBody JSONObject jsonObject){
        String recIds = jsonObject.getString("recIds");
        if(StringUtils.isEmpty(recIds)){
            return new JsonResult(false,"删除失败!");
        }
        String[] aryId=recIds.split(",");
        List<InfInbox> list=new ArrayList<>();
        for (String redId : aryId) {
            InfInbox infInbox=infInboxServiceImpl.get(redId);
            if(BeanUtil.isNotEmpty(infInbox)){
                list.add(infInbox);
                infInnerMsgServiceImpl.updateDelFlag(infInbox.getMsgId());
            }
        }
        //更新log的是否删除
        infInnerMsgLogServiceImpl.updateIsDel(list);

        //记录删除标记。
        LogContext.put(Audit.DETAIL,"收件箱ID:" + recIds);

        JsonResult result=JsonResult.getSuccessResult("删除成功!");
        return result;
    }

    @MethodDefine(title = "删除已发消息", path = "/delSentMsg", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "jsonObject")})
    @ApiOperation(value="删除已发消息")
    @AuditLog(operation = "删除已发消息")
    @PostMapping("delSentMsg")
    public JsonResult delSentMsg(@RequestBody JSONObject jsonObject){
        String msgIds = jsonObject.getString("msgIds");
        if(StringUtils.isEmpty(msgIds)){
            return new JsonResult(false,"删除失败!");
        }


        String[] aryId=msgIds.split(",");
        for (String msgId : aryId) {
            infInnerMsgServiceImpl.updateDelFlag(msgId);
        }
        String detail="删除已发消息:" + msgIds;
        LogContext.put(Audit.DETAIL,detail);

        JsonResult result=JsonResult.getSuccessResult("删除成功!");
        return result;
    }
}
