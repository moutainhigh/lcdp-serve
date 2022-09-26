package com.redxun.user.org.controller;


import com.redxun.constvar.ConstVarContext;
import com.redxun.constvar.ConstVarType;
import com.redxun.profile.ProfileContext;
import com.redxun.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 公共API
 */
@Slf4j
@RestController
@RequestMapping("/user/org/public")
@Api(tags = "公共API")
public class PublicController {

    @Resource
    private ConstVarContext constVarContext;

    @ApiOperation("获取常量类型")
    @GetMapping("/getConstVars")
    public List<ConstVarType> getConstVars() {
        return  ConstVarContext.getTypes();
    }

    /**
     * 根据键获取常量的值。
     * @param key
     * @param vars
     * @return
     */
    @ApiOperation("根据常量Key获取常量值")
    @GetMapping("/getValByKey")
    public Object getValByKey(@ApiParam @RequestParam(value = "key") String key,
                              @ApiParam @RequestBody(required = false) Map<String,Object> vars) {
        return  constVarContext .getValByKey("["+key+"]",vars);
    }


    /**
     * 获取当前人的身份信息。
     * @return
     */
    @ApiOperation("获取当前人的身份信息")
    @GetMapping("/getProfiles")
    public Object getProfiles() {
        Map<String, Set<String>>  profiles= ProfileContext.getCurrentProfile();
        return  profiles;
    }

    /**
     * 获取APP的所有服务
     * @return
     */
    @ApiOperation("获取APP的所有服务")
    @GetMapping("/getAppServices")
    public List<String> getAppServices(){
        return SysUtil.getAppServices();
    }

}
