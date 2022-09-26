package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.core.entity.InsMsgboxBox;
import com.redxun.portal.core.service.InsMsgboxBoxServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/portal/core/insMsgboxBox")
@ClassDefine(title = "消息盒子定义",alias = "insMsgboxBoxController",path = "/portal/core/insMsgboxBox",packages = "core",packageName = "门户管理")
@Api(tags = "消息盒子定义")
public class InsMsgboxBoxController extends BaseController<InsMsgboxBox> {

    @Autowired
    InsMsgboxBoxServiceImpl insMsgboxBoxService;

    @Override
    public BaseService getBaseService() {
        return insMsgboxBoxService;
    }

    @Override
    public String getComment() {
        return "消息盒子定义";
    }



    /**
     * 根据消息盒子ID查询数据
     */
    @MethodDefine(title = "根据消息盒子ID查询数据", path = "/queryByBoxId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "消息盒子ID", varName = "boxId")})
    @ApiOperation(value = "根据消息盒子ID查询数据")
    @GetMapping("/queryByBoxId")
    public JsonResult<List<InsMsgboxBox>> queryByBoxId(@RequestParam(value = "boxId") String boxId) {
        if (StringUtils.isEmpty(boxId)) {
            return JsonResult.Success().setData(new ArrayList<>());
        }
        List<InsMsgboxBox> list = insMsgboxBoxService.queryByBoxId(boxId);
        return JsonResult.Success().setData(list);
    }
}
