package com.xxl.job.core.biz.client;

import com.redxun.common.utils.SpringUtil;
import com.redxun.job.feign.JobClient;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.GsonTool;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * admin api test
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class AdminBizNacosClient implements AdminBiz {

    private static final Logger logger = LoggerFactory.getLogger(AdminBizNacosClient.class);


    public AdminBizNacosClient(String appName, String address, String accessToken) {
        this.address = address;
        this.appName = appName;
        this.accessToken = accessToken;
    }

    private String address ;
    private String appName ;
    private String accessToken;
    private int timeout = 3;


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        try {
            JobClient jobClient = SpringUtil.getBean("com.redxun.job.feign.JobClient");
            String requestBody = GsonTool.toJson(callbackParamList);
            return  jobClient.callback(requestBody);
        }catch (Exception e){
            logger.error(">>>>>>>>>>> xxl-job, AdminBizNacosClient  callback start  error, appName:{}, address:{}, ",appName, address, e);
        }
        return  ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        try {
            JobClient jobClient = SpringUtil.getBean("com.redxun.job.feign.JobClient");
            String requestBody = GsonTool.toJson(registryParam);
            return jobClient.registry(requestBody);
        }catch (Exception e){
            logger.error(">>>>>>>>>>> xxl-job, AdminBizNacosClient  callback start  error, appName:{}, address:{}, ",appName, address, e);
        }
        return  ReturnT.FAIL;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        try {
            JobClient jobClient = SpringUtil.getBean("com.redxun.job.feign.JobClient");
            String requestBody = GsonTool.toJson(registryParam);
            return jobClient.registryRemove(requestBody);
        }catch (Exception e){
            logger.error(">>>>>>>>>>> xxl-job, AdminBizNacosClient  callback start  error, appName:{}, address:{}, ",appName, address, e);
        }
        return  ReturnT.FAIL;
    }

}
