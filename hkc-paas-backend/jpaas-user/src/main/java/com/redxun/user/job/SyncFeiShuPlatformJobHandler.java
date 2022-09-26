package com.redxun.user.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.user.handler.platform.IPlatformHandler;
import com.redxun.user.handler.platform.PlatformHandlerContext;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.entity.OsFsAgent;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsFsAgentServiceImpl;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 更新离职状态的用户任务调度处理器
 */
@Slf4j
@Component
public class SyncFeiShuPlatformJobHandler extends IJobHandler {
    @Resource
    OsUserServiceImpl osUserService;
    @Autowired
    OsGroupServiceImpl osGroupService;
    @Autowired
    OsFsAgentServiceImpl osFsAgentService;

    @Override
    @XxlJob("syncFeiShuPlatformJobHandler")
    public void execute() throws Exception {

        //获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数....[{}]", param);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("IS_PUSH_", 1);//查询推送状态为“是”
        queryWrapper.eq("IS_DEFAULT_", 1);//查询默认应用状态为“是”
        List<OsFsAgent> list = osFsAgentService.findAll(queryWrapper);
        if(list==null){
            XxlJobHelper.handleFail("未找到需要推送配置信息!");
            return ;
        }
        list.stream().forEach(osFsAgent -> {
            String tenantId = osFsAgent.getTenantId();
            IPlatformHandler platformHandler = PlatformHandlerContext.getPlatformHandler("4");
            if(platformHandler==null){
                XxlJobHelper.handleFail("未找到第三方平台实现类!");
                return ;
            }
            pushGroup(tenantId,platformHandler);
            pushUser(tenantId,platformHandler);
        });
        //日志返回
        XxlJobHelper.handleSuccess("推送用户至飞书执行成功!");
    }

    private void pushGroup(String tenantId,IPlatformHandler platformHandler){

        Calendar instance = Calendar.getInstance();
        Date endDate = instance.getTime();
        instance.set(Calendar.HOUR_OF_DAY, -1);//推送最近一小时修改过的数据
        Date beginDate = instance.getTime();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.eq("DIM_ID_", OsDimension.DIM_ADMIN_ID);
        queryWrapper.between("UPDATE_TIME_",beginDate,endDate);
        List<OsGroup> list = osGroupService.findAll(queryWrapper);
        if(list !=null && list.size()>0){
            list.stream().forEach(osGroup -> {
                platformHandler.pushAddDepartment(osGroup);
            });
        }

    }
    private void pushUser(String tenantId,IPlatformHandler platformHandler ){
        Calendar instance = Calendar.getInstance();
        Date endDate = instance.getTime();
        instance.set(Calendar.HOUR_OF_DAY, -1);//推送最近一小时修改过的数据
        Date beginDate = instance.getTime();
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.between("UPDATE_TIME_",beginDate,endDate);
        List<OsUser> list = osUserService.findAll(queryWrapper);
        if(list!=null){
            list.stream().forEach(osUser -> {
                //设置主部门
                OsGroup mainGroup = osGroupService.getMainGroup(osUser.getUserId(),osUser.getTenantId());
                osUser.setMainDepId(mainGroup!=null?mainGroup.getGroupId():"");
                platformHandler.pushAddUser(osUser);
            });
        }
    }
}
