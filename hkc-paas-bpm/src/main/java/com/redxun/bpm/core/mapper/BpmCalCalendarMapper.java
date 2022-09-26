package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmCalCalendar;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 工作日历安排数据库访问层
 */
@Mapper
public interface BpmCalCalendarMapper extends BaseDao<BpmCalCalendar> {

    List<BpmCalCalendar> getTimeBlock(Map<String, Object> params);

    List<BpmCalCalendar> getByStartDateAndSettingId(Map<String, Object> params);

    BpmCalCalendar getBiggerBlockThanStartAndEnd(Map<String, Object> params);

    BpmCalCalendar getBlockBetweenStartAndEnd(Map<String, Object> params);

    /**
     * 通过开始时间结束时间以及所属于的日历设定获取workCalendar
     *
     * @return
     */
    List<BpmCalCalendar> getByStartAndEndAndSettingId(Map<String, Object> params);

    void delByConnectId(String connectId);

    List<BpmCalCalendar> getWorkCalendarBySettingId(String settingId);

    List<BpmCalCalendar> getCalendarInDayBySettingIdConnectId(Map<String, Object> params);
}
