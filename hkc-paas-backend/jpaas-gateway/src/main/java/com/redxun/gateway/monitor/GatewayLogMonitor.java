package com.redxun.gateway.monitor;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.IdGenerator;
import com.redxun.log.model.GatewayLog;
import com.redxun.log.model.GatewayLogEvent;
import com.redxun.log.monitor.PointUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GatewayLogMonitor {

    @Async("lazyTraceExecutor")
    @EventListener(value = GatewayLogEvent.class)
    public GatewayLogEvent addGatewayLogEvent(GatewayLogEvent event){
        GatewayLog gatewayLog = event.getGatewayLog();
        //应该把数据格式化的存储到第三方系统
        PointUtil.debug(IdGenerator.getIdStr(),"info", JSONObject.toJSONString(gatewayLog));
        return event;
    }
}
