package com.redxun.user.job;

import com.redxun.common.tool.BeanUtil;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 更新离职状态的用户任务调度处理器
 */
@Slf4j
@Component
public class QuitTimeJob extends IJobHandler {
    @Resource
    OsUserServiceImpl osUserService;

    @Override
    @XxlJob("quitTimeJobHandler")
    public void execute() throws Exception {
        //获取参数
        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数....[{}]", param);

        List<OsUser> list = osUserService.getQuitTimeUsers(new Date());
        if(list!=null){
            for(OsUser osUser:list){
                //修改为：离职状态
                osUser.setStatus("0");
                osUserService.updateUser(osUser);
            }
        }

        //日志返回
        XxlJobHelper.handleSuccess("用户离职任务执行成功!");

    }
}
