package com.redxun.bpm.core.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.entity.MobileTag;
import com.redxun.bpm.core.service.MobileTagServiceImpl;
import com.redxun.bpm.util.PushMobileMsgUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/core/mobiletag")
@ClassDefine(title = "手机绑定",alias = "mobileTagController",path = "/core/mobiletag",packages = "core",packageName = "流程管理")
@Api(tags = "手机绑定")
public class MobileTagController {

    @Resource
    MobileTagServiceImpl mobileTagService;

    /**
     * 保存clientId和账号的关系。
     * @throws IOException
     */
    @MethodDefine(title = "保存clientId和账号的关系", path = "/registCid", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @AuditLog(operation = "保存clientId和账号的关系")
    @GetMapping("registCid")
    public JsonResult registCid(HttpServletRequest request) {

        String cid = request.getParameter("cid");
        String mobileType = request.getParameter("mobileType");
        IUser user = ContextUtil.getCurrentUser();
        String userId = "";
        String userName="";
        if(user!=null){
            userId = user.getUserId();
            userName=user.getFullName();
        }
        if(StringUtils.isEmpty(cid))  {
            return new JsonResult(false, "请传入CID");
        }
        if(StringUtils.isEmpty(mobileType)) {
            return new JsonResult(false, "请传入MOBILETYPE!");
        }

        Integer totalCount = mobileTagService.getCount(cid,mobileType);
        if(totalCount==null||totalCount==0){
            MobileTag mobileTag = new MobileTag();
            mobileTag.setTagid( IdGenerator.getIdStr());
            mobileTag.setUserId(userId);
            mobileTag.setCid(cid);
            mobileTag.setMobileType(mobileType);
            mobileTagService.insert(mobileTag);
        }

        String detail="将["+cid+"]绑定到用户:" +userName +"("+userId+")";


        Boolean bindCid = PushMobileMsgUtil.userBindCid(userId,cid);
        if(!bindCid){
            LogContext.put(Audit.STATUS,Audit.STATUS_FAIL);
            detail+=",绑定失败!";
            LogContext.put(Audit.DETAIL,detail);
            return new JsonResult(false, "用户绑定CID失败");
        }
        LogContext.put(Audit.DETAIL,detail+",绑定成功!");
        LogContext.put(Audit.PK,cid);

        return new JsonResult(true,"用户成功绑定CID");
    }

    @MethodDefine(title = "获取单推数据", path = "/getBycidList", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单推ID列表", varName = "list")})
    @ApiOperation("获取单推数据")
    @AuditLog(operation = "获取单推数据")
    @PostMapping("getBycidList")
    public JSONObject getBycidList(@RequestBody JSONArray list){
        Map<String, Object> cidMobileTypeMap = new HashMap<String, Object>();
        for(int i=0;i<list.size();i++){
            String cid=list.getString(i);
            MobileTag mobileTag =  mobileTagService.getByCid(cid);
            if(mobileTag!=null){
                String mobileType = mobileTag.getMobileType();
                cidMobileTypeMap.put(cid, mobileType);
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("cidMobileTypeMap",JSONObject.toJSON(cidMobileTypeMap));
        return obj;
    }

}
