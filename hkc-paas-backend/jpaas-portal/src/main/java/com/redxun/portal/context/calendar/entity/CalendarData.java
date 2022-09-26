package com.redxun.portal.context.calendar.entity;

import lombok.Data;

/**
 * 日历数据
 */
@Data
public class CalendarData {
    public  static final String CALENDAR_SERVICE="service";
    public  static final String CALENDAR_MODE="calendarMode";
    public  static final String CALENDAR_VALUE="calendarValue";

    public  static final String CALENDAR_MODE_MONTH="year";
    public  static final String CALENDAR_MODE_DAY="month";

    public  static final String START_TIME=" 00:00:00";
    public  static final String END_TIME=" 23:59:59";

    /**
     * 下标，必填
     */
    private int index=0;
    /**
     * 日期，必填 2020-05-19
     */
    private String day="";
    /**
     * 开始时间，必填 08:30
     */
    private String startTime="";
    /**
     * 标题，必填
     */
    private String title="";
    /**
     * 描述，必填
     */
    private String describe="";
    /**
     * 跳转路径
     */
    private String url="";
}
