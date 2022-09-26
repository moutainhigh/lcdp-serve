package com.redxun.form.core.controller;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.RequestUtil;
import com.redxun.db.CommonDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/form/share/formAddress")
@Api(tags = "区域地址")
public class FormAddressController {

    @Resource
    private CommonDao commonDao;

    @ApiOperation("区域地址")
    @RequestMapping(value = "/queryAddress",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResult queryAddress(HttpServletRequest request) {

        String addressKey = RequestUtil.getString(request, "addressKey");
        String config = RequestUtil.getString(request, "config");
        JsonResult jsonResult = new JsonResult().setShow(false).setSuccess(true);
        if(StringUtils.isNotEmpty(addressKey)){
            String sql = "SELECT AREA_CODE_,AREA_NAME_,PARENT_CODE_ FROM sys_nation_area ";
            String parentCode= JSONObject.parseObject(config).getString("PARENT_CODE_");
            Map<String,Object> params = new HashMap<>();
            params.put("parentCode",parentCode);
            //省
            if ("provinces".equals(addressKey)) {
                sql+="WHERE AREA_LEVEL_='1' AND PARENT_CODE_='0'";
            }
            //市
            else if ("cities".equals(addressKey)) {
                sql+="WHERE AREA_LEVEL_='2' AND PARENT_CODE_= #{w.parentCode}";
            }
            //县(区)
            else  {
                sql+="WHERE AREA_LEVEL_='3' AND PARENT_CODE_= #{w.parentCode}";
            }
            List list = commonDao.query(sql,params);
            return jsonResult.setData(list);
        }else {
            return new JsonResult().setSuccess(false).setShow(false).setMessage("查询失败");
        }

    }
}
