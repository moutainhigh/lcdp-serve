package com.redxun.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.util.SysUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/global")
@Api(tags = "全局API")
public class GlobalController {
    /**
     * 返回所有的Controller类
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllClass")
    public List<JSONObject> getAllClass() throws Exception{
        return SysUtil.getAllClass();
    }

    /**
     * 返回所有API类
     * @return
     * @throws Exception
     */
    @GetMapping("/getAllApiClass")
    public List<JSONObject> getAllApiClass() throws Exception{
        return SysUtil.getAllApiClass();
    }

    @GetMapping("/getDataSourcesByAlias")
    public DataSource getDataSourcesByAlias(@RequestParam(value = "alias")String alias){
        try {
            return DataSourceUtil.getDataSourcesByAlias(alias);
        }catch (Exception e){
        }
        return null;
    }
}
