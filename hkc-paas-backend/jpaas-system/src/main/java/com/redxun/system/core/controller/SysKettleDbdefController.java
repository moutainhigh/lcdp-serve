
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.system.core.entity.SysKettleDbdef;
import com.redxun.system.core.service.SysKettleDbdefServiceImpl;
import com.redxun.system.util.kettle.KRepository;
import com.redxun.system.util.kettle.KettleUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysKettleDbdef")
@Api(tags = "KETTLE资源库定义")
@ClassDefine(title = "KETTLE资源库定义",alias = "SysKettleDbdefController",path = "/system/core/sysKettleDbdef",packages = "core",packageName = "子系统名称")
public class SysKettleDbdefController extends BaseController<SysKettleDbdef> {

    @Autowired
    SysKettleDbdefServiceImpl sysKettleDbdefService;


    @Override
    public BaseService getBaseService() {
    return sysKettleDbdefService;
    }

    @Override
    public String getComment() {
    return "KETTLE资源库定义";
    }

    @Override
    protected JsonResult beforeSave(SysKettleDbdef ent) {

        String pluginPath= SysPropertiesUtil.getString("kettlePluginPath");

        if(ent.getStatus()==0){
            return JsonResult.Success();
        }

        KRepository repo=getResponsitory(ent);

        JsonResult result= KettleUtil.connectToRepository(pluginPath,repo);

        if(!result.isSuccess()){
            return  result;
        }

        return super.beforeSave(ent);
    }

    private KRepository getResponsitory(SysKettleDbdef dbdef){
        KRepository repo=new KRepository();
        repo.setResUser(dbdef.getResUser());
        repo.setResPwd(dbdef.getResPwd());
        repo.setHost(dbdef.getHost());
        repo.setPort(dbdef.getPort());
        repo.setDatabaseName(dbdef.getDatabase());
        repo.setDbType(dbdef.getDbType());
        repo.setUser(dbdef.getUser());
        repo.setPassword(dbdef.getPassword());

        return repo;
    }

    @RequestMapping("getPath")
    public void getPath(HttpServletResponse response) throws IOException {
        String path= this.getClass().getResource("/").getPath();
        response.getWriter().println(path);

    }


    @MethodDefine(title = "根据状态获取数据", path = "/getAllByStatus", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "状态", varName = "status")})
    @ApiOperation("根据状态获取数据")
    @GetMapping("/getAllByStatus")
    public JsonResult getAllByStatus(@RequestParam(name = "status") String status){
        JsonResult jsonResult=new JsonResult().setShow(false).setMessage("获取成功!");
        List list = sysKettleDbdefService.getAllByStatus(status);
        jsonResult.setData(list);
        return jsonResult;
    }
}

