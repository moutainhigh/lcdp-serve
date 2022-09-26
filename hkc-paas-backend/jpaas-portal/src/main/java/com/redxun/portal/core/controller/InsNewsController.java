package com.redxun.portal.core.controller;

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
import com.redxun.dto.sys.SysDicDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.portal.core.entity.InsNews;
import com.redxun.portal.core.service.InsNewsServiceImpl;
import com.redxun.portal.feign.OsUserClient;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/portal/core/insNews")
@ClassDefine(title = "信息公告",alias = "insNewsController",path = "/portal/core/insNews",packages = "core",packageName = "门户管理")
@Api(tags = "信息公告")
public class InsNewsController extends BaseController<InsNews> {

    @Autowired
    InsNewsServiceImpl insNewsService;
    @Autowired
    OsUserClient osUserClient;
    @Autowired
    SystemClient systemClient;

    @Override
    public BaseService getBaseService() {
        return insNewsService;
    }

    @Override
    public String getComment() {
        return "信息公告";
    }

    /**
     * 根据主键查询记录详细信息
     *
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据主键查询记录详细信息", path = "/readInsNews", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value = "查看单条记录信息", notes = "根据主键查询记录详细信息")
    @GetMapping("/readInsNews")
    public JsonResult<InsNews> readInsNews(@RequestParam(value = "pkId") String pkId) {
        JsonResult result = JsonResult.Success();
        result.setShow(false);
        if (BeanUtil.isEmpty(pkId)) {
            return result.setData(new Object());
        }
        InsNews ent = insNewsService.get(pkId);
        ent.setReadTimes(ent.getReadTimes() + 1);
        insNewsService.update(ent);

        OsUserDto user = osUserClient.getById(ent.getAuthor());
        if (user != null) {
            ent.setAuthor(user.getFullName());
        }
        return result.setData(ent);
    }


    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "根据条件查询业务数据记录", path = "/query", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="根据条件查询业务数据记录", notes="根据条件查询业务数据记录")
    @PostMapping(value="/getAllIssuedNews")
    public JsonPageResult getAllIssuedNews(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_STATUS__S_EQ",InsNews.STATUS_ISSUED);
            handleFilter(filter);
            IPage page= getBaseService().query(filter);
            handlePage(page);
            jsonResult.setPageData(page);
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @Override
    protected JsonResult beforeSave(InsNews ent) {
        if (StringUtils.isEmpty(ent.getPkId())) {
            ent.setAuthor(ContextUtil.getCurrentUserId());
        }
        return super.beforeSave(ent);
    }

    @Override
    protected void handlePage(IPage page) {
        List<SysDicDto> list=systemClient.getTopDicByKey("NEWS");
        Map<String, String> dicMap = list.stream().collect(Collectors.toMap(p->p.getValue(), p -> p.getName()));
        List<InsNews> newsList= page.getRecords();
        for(InsNews news:newsList){
            news.setSysDicName(dicMap.get(news.getSysDicNew()));
        }
    }
}
