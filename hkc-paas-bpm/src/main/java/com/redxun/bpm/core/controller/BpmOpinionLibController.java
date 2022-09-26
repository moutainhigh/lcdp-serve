package com.redxun.bpm.core.controller;

import com.redxun.bpm.core.service.BpmOpinionLibServiceImpl;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmOpinionLib;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmOpinionLib")
@ClassDefine(title = "意见收藏表",alias = "bpmOpinionLibController",path = "/bpm/core/bpmOpinionLib",packages = "core",packageName = "流程管理")
@Api(tags = "意见收藏表")
public class BpmOpinionLibController extends BaseController<BpmOpinionLib> {

    @Autowired
    BpmOpinionLibServiceImpl bpmOpinionLibService;

    @Override
    public BaseService getBaseService() {
        return bpmOpinionLibService;
    }

    @Override
    public String getComment() {
        return "意见收藏表";
    }

    /**
     * 获取当前用户常用审批意见
     * @return
     */
    @MethodDefine(title = "获取当前用户常用审批意见", path = "/getUserText", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @GetMapping("getUserText")
    public List<BpmOpinionLib> getUserText(){
        String userId = ContextUtil.getCurrentUserId();
        List<BpmOpinionLib> list = bpmOpinionLibService.getByUserId(userId);
        return list;
    }

    @Override
    protected JsonResult beforeSave(BpmOpinionLib ent) {
        String userId = ContextUtil.getCurrentUserId();
        String opText=ent.getOpText();
        if(bpmOpinionLibService.isOpinionSaved(userId, opText)){
            return JsonResult.Fail("已经收藏过了！");
        }
        ent.setUserId(userId);
        return super.beforeSave(ent);
    }
}
