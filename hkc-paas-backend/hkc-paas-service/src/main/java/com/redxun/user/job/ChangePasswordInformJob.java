package com.redxun.user.job;

import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsPasswordPolicyServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 通知用户修改密码任务调度处理器
 */
@Slf4j
@Component
public class ChangePasswordInformJob extends IJobHandler {
    @Resource
    OsUserServiceImpl osUserService;

    @Resource
    OsPasswordPolicyServiceImpl osPasswordPolicyService;

    @Override
    @XxlJob("changePasswordInformJobHandler")
    public void execute() throws Exception {

        Date now = new Date();

        //获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数....[{}]", param);

        List<OsUser> list = osUserService.list();
        if(list!=null){
            for(OsUser osUser:list){
                osPasswordPolicyService.informUserChangePassword(osUser, now);
            }
        }

        //日志返回
        XxlJobHelper.handleSuccess("通知用户修改密码任务执行成功!");

    }
}
