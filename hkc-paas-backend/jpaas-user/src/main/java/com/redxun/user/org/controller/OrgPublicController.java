package com.redxun.user.org.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.user.org.service.OsInstServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @auth chenshangxuan
 * @description: 组织架构对外的公共接口
 */
@Api(tags = "组织架构对外的公共接口")
@ClassDefine(title = "组织架构对外的公共接口",packageName = "组织架构",alias = "OrgPublicController",path = "")
@Slf4j
@Controller
@RequestMapping("/user/org/public")
public class OrgPublicController {
    @Resource
    OsInstServiceImpl osInstService;

    /**
     * 获取有效的组织机构
     * @return
     */
    @PostMapping("/getValidInsts")
    @ResponseBody
    public JsonPageResult getValidInsts(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        IPage page= osInstService.query(filter);
        jsonResult.setPageData(page);
        return jsonResult;
    }
}
