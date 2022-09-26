package com.redxun.config;

import com.redxun.common.utils.CustomThreadPoolTaskExecutor;
import com.redxun.common.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务执行配置
 * @author yjy
 * @date 2018/12/13
 */
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties(FormAsyncTaskProperties.class)
@Configuration
public class FormAsyncTaskConfig implements ApplicationListener<PropertiesEvent> {
    @Autowired
    private FormAsyncTaskProperties formAsyncTaskProperties;

    private boolean isInit=false;

    private void resetExecutor(){
        ThreadPoolTaskExecutor executor= SpringUtil.getBean("formExportExecutor");
        String corePoolJson = formAsyncTaskProperties.getCorePoolJson();
        String [] corePoolJsons= corePoolJson.split("[|]");
        executor.setCorePoolSize(Integer.parseInt(corePoolJsons[0]));
        executor.setMaxPoolSize(Integer.parseInt(corePoolJsons[1]));
        executor.setQueueCapacity(Integer.parseInt(corePoolJsons[2]));
        executor.setThreadNamePrefix("formExcelDownExecutor_");
    }

    @Bean("formExportExecutor")
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        String corePoolJson = formAsyncTaskProperties.getCorePoolJson();
        String [] corePoolJsons= corePoolJson.split("[|]");
        executor.setCorePoolSize(Integer.parseInt(corePoolJsons[0]));
        executor.setMaxPoolSize(Integer.parseInt(corePoolJsons[1]));
        executor.setQueueCapacity(Integer.parseInt(corePoolJsons[2]));
        //线程池前缀
        executor.setThreadNamePrefix("formExcelDownExecutor_");
        /*
           rejection-policy：当pool已经达到max size的时候，如何处理新任务
           CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        this.isInit=true;
        return executor;
    }

    @Override
    public void onApplicationEvent(PropertiesEvent propertiesEvent) {
        if(this.isInit){
            this.resetExecutor();
        }
    }
}
