package com.redxun.api.bpm;

import java.util.Date;

/**
 * 日历时间计算接口。
 *
 */
public interface ICalendarService {



    /**
     * 根据用户，开始时间和有效的时长获取将来的时间点。
     * <pre>
     *  1.查找个人的日历。
     *  2.如果找不到，则查询组织的日历。
     *  3.如果找不到则获取系统配置的默认日历。
     *  4.如果没有日历则直接按照 直接进行计算。
     *  需要考虑加班和请假时间。
     * </pre>
     * @param userId
     * @param startTime
     * @param minute
     * @return
     * @throws Exception
     */
    Date getByUserId(String userId,Date startTime,int minute);

    /**
     * 根据用户ID，开始时间，结束时间获取中间所花费的实际时间，时间单位为分钟。
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    int getActualTimeUserId(String userId,Date beginDate,Date endDate);

    /**
     * 根据日历获取两个时间点的有效时间。
     * @param calendarId
     * @param startTime
     * @param endTime
     * @return
     */
    int getMinuteBetweenTimeBlock(String calendarId, Date startTime, Date endTime);

    /**
     * 根据日历和开始时间获取任务的结束时间。
     * @param calendarId
     * @param startTime
     * @param minute
     * @return
     */
    Date getEndTimeByCalendar(String calendarId, Date startTime,int minute);
}
