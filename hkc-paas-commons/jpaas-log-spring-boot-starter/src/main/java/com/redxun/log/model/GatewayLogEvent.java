package com.redxun.log.model;

import org.springframework.context.ApplicationEvent;

public class GatewayLogEvent extends ApplicationEvent {

    private GatewayLog gatewayLog;
    public GatewayLogEvent(Object source,GatewayLog gatewayLog) {
        super(source);
        this.gatewayLog = gatewayLog;
    }

    public GatewayLog getGatewayLog(){
        return this.gatewayLog;
    }
}
