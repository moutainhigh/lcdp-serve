package com.redxun.web.log;

import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.SpringUtil;
import com.redxun.web.dto.SysErrorLogDto;
import com.redxun.web.mq.ErrLogOutput;
import org.slf4j.MDC;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class LogUtil {

    /**
     * 记录日志。
     * @param applicationName       应用名称
     * @param message               应用消息。
     */
    public static void handLog(String applicationName, String message){

        String url="";
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes!=null){
            HttpServletRequest request = servletRequestAttributes.getRequest();
            url=request.getRequestURI();
        }

        SysErrorLogDto sysErrorLogDto=new SysErrorLogDto();
        sysErrorLogDto.setAppName(applicationName);
        sysErrorLogDto.setContent(message);
        sysErrorLogDto.setUrl(url);
        sysErrorLogDto.setTraceId(MDC.get(CommonConstant.LOG_TRACE_ID));

        ErrLogOutput logOutput= SpringUtil.getBean(ErrLogOutput.class);

        logOutput.logOutput().send(MessageBuilder.withPayload(sysErrorLogDto).build());
    }
}
