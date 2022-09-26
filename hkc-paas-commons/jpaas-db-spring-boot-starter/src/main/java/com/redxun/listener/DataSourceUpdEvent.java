package com.redxun.listener;

import org.springframework.context.ApplicationEvent;

/**
 * 数据源发生变化事件。
 * 当数据源发生变化时，发布监听事件。
 */
public class DataSourceUpdEvent extends ApplicationEvent {

    public DataSourceUpdEvent(Object source) {
        super(source);
    }
}
