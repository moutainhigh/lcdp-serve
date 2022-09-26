package com.redxun.form.core.controller;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.db.CommonDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/form")
@Api(tags = "加密URL")
public class EncryptUrlController {

    @Autowired
    private CommonDao commonDao;

    /**
     * 解密加密的数据，返回真实路径
     * @return
     */
    @ApiOperation("解密加密数据")
    @GetMapping(value = "/encryptUrl/{code}")
    public void encryptUrlByCode(@PathVariable("code") String code, HttpServletResponse response) throws Exception{
        String dataSource= SysPropertiesUtil.getString("encrypt_datasource");
        String sql = SysPropertiesUtil.getString("encrypt_sql");
        if(StringUtils.isEmpty(sql)){
            response.setStatus(404);
            return;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",code);
        sql=StringUtils.replaceVariableMap(sql,map);
        String relPath = (String)commonDao.queryOne(dataSource,sql);
        if(StringUtils.isEmpty(relPath)){
            response.setStatus(404);
            return;
        }
        response.sendRedirect(relPath);
    }

}