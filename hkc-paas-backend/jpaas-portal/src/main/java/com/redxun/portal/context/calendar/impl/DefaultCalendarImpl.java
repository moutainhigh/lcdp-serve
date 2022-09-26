package com.redxun.portal.context.calendar.impl;


import com.redxun.api.bpm.IBpmTaskService;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.portal.context.calendar.CalendarDataService;
import com.redxun.portal.context.calendar.entity.CalendarData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 缺省的日历实现
 */
@Service
@Slf4j
public class DefaultCalendarImpl implements CalendarDataService {


    @Autowired
    IBpmTaskService bpmTaskService;

    @Override
    public List<String> getCalendarMonthData(String calendarMonthVal) {
        List<String> returnList = new ArrayList<>();
        List<CalendarData> listData = getMonthList(calendarMonthVal);
        for (CalendarData calendar : listData) {
            String day = calendar.getDay();
            if (returnList.size() == 0) {
                returnList.add(day);
            } else {
                if (returnList.indexOf(day) == -1) {
                    returnList.add(day);
                }
            }
        }
        return returnList;
    }


    /**
     * 查询参数：{
     * *    calendarMode： 查询类型：月 year  天：month
     * *    calendarValue:查询的月份或者日期2020-05/2020-05-18
     * * }
     * <p>
     * 返回结果 ：[
     * {
     * index:0,
     * day:日期 2020-05-19
     * startTime:开始时间 08:30
     * title:标题
     * describe:描述
     * ******自定义返回参数
     * }
     * ]
     */
    @Override
    public List<CalendarData> getMonthOrDayData(String calendarMode, String calendarValue) {
        List<CalendarData> listData = null;
        if (StringUtils.isEmpty(calendarValue)) {
            int curYear = DateUtils.getCurYear();
            int newCurMonth = DateUtils.getCurMonth() + 1;
            calendarValue = curYear + "-" + newCurMonth;
            if (CalendarData.CALENDAR_MODE_DAY.equals(calendarMode)) {
                int curDay = DateUtils.getCurDay();
                calendarValue = curYear + "-" + newCurMonth + "-" + curDay;
            }
        }
        if (CalendarData.CALENDAR_MODE_MONTH.equals(calendarMode)) {
            listData = getMonthList(calendarValue);
        } else {
            listData = getDayList(calendarValue);
        }
        return listData;
    }


    /**
     * 获取一天内的数据
     *
     * @param calendarValue
     * @return
     */
    private List<CalendarData> getDayList(String calendarValue) {
        String startTime = calendarValue + CalendarData.START_TIME;
        String endTime = calendarValue + CalendarData.END_TIME;
        return getDataList(calendarValue, startTime, endTime);
    }

    /**
     * 获取一个月内的数据
     *
     * @param calendarValue
     * @return
     */
    private List<CalendarData> getMonthList(String calendarValue) {
        String newCalendarValue = calendarValue + "-01";
        String startTime = newCalendarValue + CalendarData.START_TIME;
        String[] datas = calendarValue.split("[-]");
        int curYear = Integer.parseInt(datas[0]);
        int newCurMonth = Integer.parseInt(datas[1]);
        String lastDayOfMonth = DateUtils.getLastDayOfMonth(curYear, newCurMonth, DateUtils.YYYY_MM_DD) + CalendarData.END_TIME;
        return getDataList(newCalendarValue, startTime, lastDayOfMonth);
    }

    private List<CalendarData> getDataList(String calendarValue, String startTime, String endTime) {
        List<CalendarData> list = new ArrayList<>();
        List<BpmTaskDto> listData = bpmTaskService.myTasksToStartBetweenEnd(startTime, endTime);

        int indexNum = 0;
        for (BpmTaskDto bpmTask : listData) {
            String creatTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,bpmTask.getCreateTime());
            CalendarData calendar = new CalendarData();
            calendar.setIndex(indexNum);
            calendar.setDay(getsplitTimeTobeginDay(creatTime));
            calendar.setStartTime(getsplitTimeToEnd(creatTime));
            calendar.setTitle(bpmTask.getName());
            calendar.setDescribe(bpmTask.getSubject());
            indexNum++;
            list.add(calendar);
        }
        return list;
    }

    private String getsplitTimeTobeginDay(String creatTime) {
        String[] times = creatTime.split(" ");
        return times[0];
    }

    private String getsplitTimeToEnd(String creatTime) {
        String[] times = creatTime.split(" ");
        String[] entTimes = times[1].split("[:]");
        return entTimes[0] + ":" + entTimes[1];
    }

}
