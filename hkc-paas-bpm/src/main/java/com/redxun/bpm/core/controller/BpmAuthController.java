
package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmAuth;
import com.redxun.bpm.core.service.BpmAuthServiceImpl;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.SortParam;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmAuth")
@Api(tags = "流程授权表")
@ClassDefine(title = "流程授权表",alias = "BpmAuthController",path = "/bpm/core/bpmAuth",packages = "core",packageName = "子系统名称")

public class BpmAuthController extends BaseController<BpmAuth> {

    @Autowired
    BpmAuthServiceImpl bpmAuthService;
    @Autowired
    IOrgService orgService;

    @Override
    public BaseService getBaseService() {
        return bpmAuthService;
    }

    @Override
    public String getComment() {
        return "流程授权表";
    }

    @PostMapping("/delAuth")
    public JsonResult delAuth(@RequestParam("ids") String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new JsonResult(false, "");
        }
        bpmAuthService.delAuth(ids);
        return new JsonResult(true, "删除成功");
    }
    @PostMapping("/queryByUser")
    public JsonPageResult queryByUser(@RequestParam(value = "userId")String userId, @RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");

        try {
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_TO_AUTH_USER_ID__S_EQ",userId);
            filter.addSortParam(new SortParam("STATUS_","DESC"));
            filter.addSortParam(new SortParam("DEL_TIME_","DESC"));
            filter.addSortParam(new SortParam("CREATE_TIME_","DESC"));
            this.handleFilter(filter);
            IPage page = this.getBaseService().query(filter);
            this.handlePage(page);
            jsonResult.setPageData(page);
        } catch (Exception var10) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(var10));
        }

        return jsonResult;
    }

    @PostMapping("/queryByProcess")
    public JsonPageResult queryByProcess(@RequestParam(value = "defKey")String defKey,@RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");

        try {
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_PROCESS_KEY__S_EQ",defKey);
            filter.addSortParam(new SortParam("STATUS_","DESC"));
            filter.addSortParam(new SortParam("DEL_TIME_","DESC"));
            filter.addSortParam(new SortParam("CREATE_TIME_","DESC"));
            this.handleFilter(filter);
            IPage page = this.getBaseService().query(filter);
            this.handlePage(page);
            List<BpmAuth> list=page.getRecords();
            for(BpmAuth bpmAuth:list){
                OsUserDto osUserDto=orgService.getUserById(bpmAuth.getToAuthUserId());
                bpmAuth.setEmail(osUserDto.getEmail());
                bpmAuth.setMobile(osUserDto.getMobile());
                bpmAuth.setMainDeptName(osUserDto.getDeptName());
            }
            jsonResult.setPageData(page);
        } catch (Exception var10) {
            jsonResult.setSuccess(false);
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(var10));
        }

        return jsonResult;
    }

    @PostMapping("/saveAddProcessArray")
    public JsonResult saveAddProcessArray(@RequestBody JSONObject json){
        String userId=json.getString("userId");
        String defIds=json.getString("defIds");
        bpmAuthService.saveAddProcessArray(userId,defIds);
        return JsonResult.Success("新增流程成功");
    }

    @PostMapping("/saveAddUserArray")
    public JsonResult saveAddUserArray(@RequestBody JSONObject json){
        String defKey=json.getString("defKey");
        String userIds=json.getString("userIds");
        bpmAuthService.saveAddUserArray(defKey,userIds);
        return JsonResult.Success("新增流程成功");
    }
}

