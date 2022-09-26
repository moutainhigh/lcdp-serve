package com.redxun.config;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.context.ApplicationEvent;

public class PropertiesEvent extends ApplicationEvent {
    public PropertiesEvent(Object source) {
        super(source);
    }
}
