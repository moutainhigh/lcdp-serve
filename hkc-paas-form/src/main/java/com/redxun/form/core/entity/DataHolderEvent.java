package com.redxun.form.core.entity;

import org.springframework.context.ApplicationEvent;

public class DataHolderEvent extends ApplicationEvent {

    public DataHolderEvent(DataHolder ent) {
        super(ent);
    }
}
