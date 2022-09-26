package com.redxun.bpm.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmAgent;
import com.redxun.bpm.core.service.BpmAgentServiceImpl;
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
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmAgent")
@ClassDefine(title = "流程代理配置",alias = "bpmAgentController",path = "/bpm/core/bpmAgent",packages = "core",packageName = "流程管理")
@Api(tags = "流程代理配置")
public class BpmAgentController extends BaseController<BpmAgent> {

    @Autowired
    BpmAgentServiceImpl bpmAgentService;

    @Override
    public BaseService getBaseService() {
        return bpmAgentService;
    }

    @Override
    public String getComment() {
        return "流程代理配置";
    }

    @Override
    protected JsonResult beforeSave(BpmAgent ent) {
        if(StringUtils.isEmpty(ent.getId())){
            IUser user= ContextUtil.getCurrentUser();
            ent.setOwnerId(user.getUserId());
        }

        ent.setEndTime(DateUtils.setEndDay( ent.getEndTime()));
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "根据条件查询所有记录", path = "/getMyAgent", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/getMyAgent")
    public JsonPageResult getMyAgent(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        IUser user= ContextUtil.getCurrentUser();
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);

        filter.addQueryParam("Q_OWNER_ID__S_EQ",user.getUserId());

        IPage page= getBaseService().query(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }


}
