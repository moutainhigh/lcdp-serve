package com.redxun.bpm.core.service;


import com.redxun.api.bpm.ExtraTimeModel;
import com.redxun.api.bpm.ICalendarService;
import com.redxun.api.bpm.IExtraTimeService;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.core.entity.BpmCalCalendar;
import com.redxun.bpm.core.entity.BpmCalGrant;
import com.redxun.bpm.core.entity.BpmCalSetting;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class BpmCalendarServiceImpl implements ICalendarService {

    @Autowired
    BpmCalSettingServiceImpl bpmCalSettingService;

    @Autowired
    BpmCalCalendarServiceImpl bpmCalCalendarService;

    @Autowired
    BpmCalTimeBlockServiceImpl bpmCalTimeBlockService;

    @Autowired
    BpmCalGrantServiceImpl bpmCalGrantService;

    @Autowired(required = false)
    IExtraTimeService extraTimeService;


    @Autowired
    IOrgService orgService;

    @Override
    public Date getByUserId(String userId, Date startTime, int minute) {
        //结果日历区间
        BpmCalCalendar resultCalendar = null;
        //开始时间的时间戳
        Long startTimeMs = startTime.getTime();
        String settingId = getByUserId(userId);
        if (StringUtils.isEmpty(settingId)) {
            return new Timestamp(startTimeMs + (long) minute * BpmCalCalendar.TIME_NUM);
        }

        //经过上一轮洗牌,基本都能找到分配的日历,如果还是null,则按照24小时制度进行计算
        List<BpmCalCalendar> workCalendars = bpmCalCalendarService.getByStartDateAndSettingId(startTime, settingId);
        long lastMinutes = 0;
        if (workCalendars.size() > 0) {
            for (BpmCalCalendar workCalendar : workCalendars) {
                lastMinutes += (workCalendar.getEndTime().getTime() - workCalendar.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
            }
        } else {
            return new Timestamp(startTimeMs + (long) minute * BpmCalCalendar.TIME_NUM);
        }
        BpmCalCalendar workCalendarBefore = bpmCalCalendarService.getBlockBetweenStartAndEnd(startTime, settingId);
        if (workCalendarBefore != null) {
            lastMinutes += (workCalendarBefore.getEndTime().getTime() - startTime.getTime()) / BpmCalCalendar.TIME_NUM;
        }

        //获取加班  如果没实现加班算法这里可以忽略
        if (extraTimeService != null) {
            List<ExtraTimeModel> overTime = extraTimeService.getByUserAndStartTime(userId, startTime);
            for (ExtraTimeModel extraTimeModel : overTime) {
                if (extraTimeModel.getIsPositive()) {
                    lastMinutes += (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / (BpmCalCalendar.TIME_NUM * 10);
                }
            }

            ExtraTimeModel overBeforeTime = extraTimeService.getBlockBetweenEndAndStart(userId, startTime);
            if (overBeforeTime != null) {
                lastMinutes += (overBeforeTime.getEndTime().getTime() - startTime.getTime()) / BpmCalCalendar.TIME_NUM;
            }
        }

        if (lastMinutes < minute) {//如果日历剩余的时间总数不够需要计算的分钟数则抛出异常
            return null;
        }

        //////////////////////////////////////////////////////////////////////////////////////
        List<BpmCalCalendar> dayWorkCalendars = bpmCalCalendarService.getTimeBlock(startTime, settingId);
        if (dayWorkCalendars.size() > 0) {
            int difTimeMinutes = (int) ((dayWorkCalendars.get(0).getEndTime().getTime() - startTime.getTime()) / BpmCalCalendar.TIME_NUM);
            minute = minute - difTimeMinutes;//修正是否startTime在当天时间段安排内
            if (minute < 0) {
                return new Timestamp(dayWorkCalendars.get(0).getEndTime().getTime() + minute * BpmCalCalendar.TIME_NUM);
            }//如果没有超过一天则返回当个区间加上minute的时间戳
        }
        List<BpmCalCalendar> calendars = bpmCalCalendarService.getByStartDateAndSettingId(startTime, settingId);//此日期后的这个设定下的所有日历区间
        /*加班时间
         * 将某个开始点以后的加班时间以workCalendar的实体形式插入到calendars里再在最后进行排序
         * 排序后可以按照日历之前的按顺序用lastMinutes减法计算直到lastMinutes<0则是哪个日期
         * */
        if (extraTimeService != null) {

            List<ExtraTimeModel> extraTimeModels = extraTimeService.getByUserAndStartTime(userId, startTime);
            for (ExtraTimeModel extraTimeModel : extraTimeModels) {
                if (extraTimeModel.getIsPositive()) {
                    BpmCalCalendar workCalendar = new BpmCalCalendar();
                    workCalendar.setStartTime(extraTimeModel.getStartTime());
                    workCalendar.setEndTime(extraTimeModel.getEndTime());
                    calendars.add(workCalendar);
                }
            }
        }

        //取最小的日历
        BpmCalCalendar startBlock = getMin(calendars);
        List<BpmCalCalendar> resultBlock = bpmCalCalendarService.getByStartDateAndSettingId(startBlock.getStartTime(), settingId);
        /*计算加班
         * 将塞入了加班的日历碎片进行重新检索找出包括加班在内的最早时间,再按这个时间再次检索所有日历和加班时间碎片,方法同上
         * */
        if (extraTimeService != null) {
            List<ExtraTimeModel> extraTimeModels = extraTimeService.getByUserAndStartTime(userId, startBlock.getStartTime());
            for (ExtraTimeModel extraTimeModel : extraTimeModels) {
                if (extraTimeModel.getIsPositive()) {
                    BpmCalCalendar workCalendar = new BpmCalCalendar();
                    workCalendar.setStartTime(extraTimeModel.getStartTime());
                    workCalendar.setEndTime(extraTimeModel.getEndTime());
                    resultBlock.add(workCalendar);
                }
            }
            //将开始点夹在某个加班区间开始与结束的的加班区间初始化成传入的startTime和这个结束时间,以workCalendar的形式假如resultBlock参与计算
            ExtraTimeModel overBeforeTime = extraTimeService.getBlockBetweenEndAndStart(userId, startTime);
            if (overBeforeTime != null) {
                BpmCalCalendar beforeWorkCalendar = new BpmCalCalendar();
                beforeWorkCalendar.setStartTime(new Timestamp(startTime.getTime()));
                beforeWorkCalendar.setEndTime(overBeforeTime.getEndTime());
                resultBlock.add(beforeWorkCalendar);
            }

        }
        sortCalendar(resultBlock);


        for (int i = 0; i < resultBlock.size(); i++) {
            int sub = (int) ((resultBlock.get(i).getEndTime().getTime() - resultBlock.get(i).getStartTime().getTime()) / BpmCalCalendar.TIME_NUM);
            if (extraTimeService != null) {//减去请假的时长
                List<ExtraTimeModel> thisBlock = extraTimeService.getByUser(userId, resultBlock.get(i).getStartTime(), resultBlock.get(i).getEndTime());
                for (ExtraTimeModel extraTimeModel : thisBlock) {
                    sub = (int) (sub - (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM);
                }
            }
            if (minute > 0) {
                int tempMinute = minute;
                minute = minute - sub;
                if (minute <= 0) {
                    resultCalendar = resultBlock.get(i);
                    return new Timestamp(resultCalendar.getStartTime().getTime() + tempMinute * BpmCalCalendar.TIME_NUM);
                }
            } else {
                resultCalendar = resultBlock.get(i);
                break;
            }

        }
        if (resultCalendar != null) {
            return resultCalendar.getStartTime();
        }

        return null;
    }

    /**
     * 日历排序。
     *
     * @param calendarList
     */
    private void sortCalendar(List<BpmCalCalendar> calendarList) {
        Collections.sort(calendarList, new Comparator<Object>() {
            //排列出大小
            @Override
            public int compare(Object o1, Object o2) {
                BpmCalCalendar workCalendar1 = (BpmCalCalendar) o1;
                BpmCalCalendar workCalendar2 = (BpmCalCalendar) o2;
                if (workCalendar1.getStartTime().getTime() > workCalendar2.getStartTime().getTime()) {
                    return 1;
                }
                if (workCalendar1.getStartTime().getTime() < workCalendar2.getStartTime().getTime()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * 获取最小的起始时间段。
     *
     * @param calendarList
     * @return
     */
    private BpmCalCalendar getMin(List<BpmCalCalendar> calendarList) {
        BpmCalCalendar startBlock = Collections.min(calendarList, new Comparator<Object>() {
            //比较出集合开始时间最早的元素
            @Override
            public int compare(Object o1, Object o2) {
                BpmCalCalendar workCalendar1 = (BpmCalCalendar) o1;
                BpmCalCalendar workCalendar2 = (BpmCalCalendar) o2;
                if (workCalendar1.getStartTime().getTime() > workCalendar2.getStartTime().getTime()) {
                    return 1;
                }
                if (workCalendar1.getStartTime().getTime() < workCalendar2.getStartTime().getTime()) {
                    return -1;
                }
                return 0;
            }
        });
        return startBlock;
    }

    /**
     * 根据用户获取日历ID 。
     *
     * @param userId
     * @return
     */
    private String getByUserId(String userId) {
        BpmCalGrant calGrant = bpmCalGrantService.getByGroupIdOrUserId("USER", userId);
        if (BeanUtil.isNotEmpty(calGrant)) {
            return calGrant.getSettingId();
        }
        String settingId = "";

        //如果没有设定当前用户的日历则查找用户获取归属的组
        Map<String, Set<String>> map = orgService.getCurrentProfile();

        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            if ("user".equals(mapKey)) {
                continue;
            }
            Set<String> mapValue = entry.getValue();
            boolean isBreak = false;
            for (String groupId : mapValue) {
                BpmCalGrant thisCalGrant = bpmCalGrantService.getByGroupIdOrUserId("GROUP", groupId);
                if (BeanUtil.isNotEmpty(thisCalGrant)) {
                    calGrant = thisCalGrant;
                    settingId = calGrant.getSettingId();
                    isBreak = true;
                    break;
                }
            }
            if (isBreak) {
                break;
            }
        }
        if (calGrant == null) {//如果最后也没找到组织的日历,则查找系统公共日历
            BpmCalSetting calSetting = bpmCalSettingService.getIsCommon(BpmCalSetting.IS_COMMON_YES);
            if (BeanUtil.isNotEmpty(calSetting)) {
                settingId = calSetting.getSettingId();
            }
        }
        return settingId;
    }


    /**
     * 根据用户ID，开始时间，结束时间获取中间所花费的实际时间，时间单位为分钟。
     *
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public int getActualTimeUserId(String userId, Date beginDate, Date endDate) {
        String settingId = getByUserId(userId);
        if (StringUtils.isNotBlank(settingId)) {
            //经过上一轮洗牌,基本都能找到分配的日历,如果还是null,则按照24小时制度进行计算
            List<BpmCalCalendar> workCalendars = bpmCalCalendarService.getByStartAndEndAndSettingId(beginDate, endDate, settingId);
            int result = 0;
            boolean calculated = false;
            BpmCalCalendar biggerBlock = bpmCalCalendarService.getBiggerBlockThanStartAndEnd(beginDate, endDate, settingId);
            if (biggerBlock != null) {
                result += (endDate.getTime() - beginDate.getTime()) / BpmCalCalendar.TIME_NUM;
                calculated = true;
            }

            for (BpmCalCalendar workCalendar : workCalendars) {
                result += (workCalendar.getEndTime().getTime() - workCalendar.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;//计算中间
            }
            BpmCalCalendar workCalendar = bpmCalCalendarService.getBlockBetweenStartAndEnd(beginDate, settingId);
            if (workCalendar != null && !calculated) {
                //计算首部
                result += (workCalendar.getEndTime().getTime() - beginDate.getTime()) / BpmCalCalendar.TIME_NUM;
            }

            BpmCalCalendar workCalendar2 = bpmCalCalendarService.getBlockBetweenStartAndEnd(endDate, settingId);
            if (workCalendar2 != null && !calculated) {
                //计算尾部
                result += (endDate.getTime() - workCalendar2.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
            }

            if (extraTimeService != null) {
                //如果加班实现不为空
                List<ExtraTimeModel> computeBlocks = extraTimeService.getByUser(userId, beginDate, endDate);
                for (ExtraTimeModel extraTimeModel : computeBlocks) {
                    if (extraTimeModel.getIsPositive()) {
                        //如果是加班则加时间
                        result += (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
                    } else {
                        result -= (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
                    }
                }
            }
            return result;
        } else {
            //否则按照正常时间计算
            int subResultTime = (int) ((endDate.getTime() - beginDate.getTime()) / BpmCalCalendar.TIME_NUM);
            if (extraTimeService != null) {
                List<ExtraTimeModel> computeBlocks = extraTimeService.getByUser(userId, beginDate, endDate);
                for (ExtraTimeModel extraTimeModel : computeBlocks) {
                    if (extraTimeModel.getIsPositive()) {
                        //如果是加班则加时间
                        subResultTime += (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
                    } else {
                        subResultTime -= (extraTimeModel.getEndTime().getTime() - extraTimeModel.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
                    }
                }
            }
            return subResultTime;
        }
    }


    /**
     * 根据日历获取两个时间点的有效时间。
     *
     * @param settingId
     * @param beginDate
     * @param endDate
     * @return
     */
    @Override
    public int getMinuteBetweenTimeBlock(String settingId, Date beginDate, Date endDate) {
        BpmCalSetting calSetting = bpmCalSettingService.get(settingId);
        if (calSetting == null) {
            return -1;
        }
        List<BpmCalCalendar> workCalendars = bpmCalCalendarService.getByStartAndEndAndSettingId(beginDate, endDate, settingId);
        int result = 0;
        for (BpmCalCalendar workCalendar : workCalendars) {
            //计算中间
            result += (workCalendar.getEndTime().getTime() - workCalendar.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
        }
        BpmCalCalendar workCalendar = bpmCalCalendarService.getBlockBetweenStartAndEnd(beginDate, settingId);
        if (workCalendar != null) {
            //计算首部
            result += (workCalendar.getEndTime().getTime() - beginDate.getTime()) / BpmCalCalendar.TIME_NUM;
        }

        BpmCalCalendar workCalendar2 = bpmCalCalendarService.getBlockBetweenStartAndEnd(endDate, settingId);
        if (workCalendar2 != null) {
            //计算尾部
            result += (endDate.getTime() - workCalendar2.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
        }

        //如果只有一个内部的区间则重新计算result
        BpmCalCalendar littleCalendar = bpmCalCalendarService.getBiggerBlockThanStartAndEnd(beginDate, endDate, settingId);
        if (littleCalendar != null) {
            //如果有则代表这个小区间只在某个区间内
            result = (int) ((endDate.getTime() - beginDate.getTime()) / BpmCalCalendar.TIME_NUM);
        }
        return result;
    }

    /**
     * 根据日历和开始时间获取任务的结束时间。
     *
     * @param settingId
     * @param startTime
     * @param minute
     * @return
     */
    @Override
    public Date getEndTimeByCalendar(String settingId, Date startTime, int minute) {
        //结果日历区间
        BpmCalCalendar resultCalendar = null;
        List<BpmCalCalendar> workCalendars = bpmCalCalendarService.getByStartDateAndSettingId(startTime, settingId);
        int lastMinutes = 0;
        if (workCalendars.size() > 0) {
            for (BpmCalCalendar workCalendar : workCalendars) {
                lastMinutes += (workCalendar.getEndTime().getTime() - workCalendar.getStartTime().getTime()) / BpmCalCalendar.TIME_NUM;
            }
        }

        BpmCalCalendar workCalendar = bpmCalCalendarService.getBlockBetweenStartAndEnd(startTime, settingId);
        if (workCalendar != null) {
            lastMinutes += (workCalendar.getEndTime().getTime() - startTime.getTime()) / BpmCalCalendar.TIME_NUM;
        }

        if (lastMinutes >= minute) {
            //总区间大于可计算区间
            List<BpmCalCalendar> dayWorkCalendars = bpmCalCalendarService.getTimeBlock(startTime, settingId);
            if (dayWorkCalendars.size() > 0) {
                int difTimeMinutes = (int) ((dayWorkCalendars.get(0).getEndTime().getTime() - startTime.getTime()) / BpmCalCalendar.TIME_NUM);
                //修正是否startTime在当天时间段安排内
                minute = minute - difTimeMinutes;
                if (minute < 0) {
                    //如果没有超过一天则返回当个区间加上minute的时间戳
                    return new Timestamp(dayWorkCalendars.get(0).getEndTime().getTime() + minute * BpmCalCalendar.TIME_NUM);
                }
            }
            //此日期后的这个设定下的所有日历区间
            List<BpmCalCalendar> calendars = bpmCalCalendarService.getByStartDateAndSettingId(startTime, settingId);
            BpmCalCalendar startBlock = Collections.min(calendars, new Comparator<Object>() {
                //比较出集合开始时间最早的元素
                @Override
                public int compare(Object o1, Object o2) {
                    BpmCalCalendar workCalendar1 = (BpmCalCalendar) o1;
                    BpmCalCalendar workCalendar2 = (BpmCalCalendar) o2;
                    if (workCalendar1.getStartTime().getTime() > workCalendar2.getStartTime().getTime()) {
                        return 1;
                    }
                    if (workCalendar1.getStartTime().getTime() < workCalendar2.getStartTime().getTime()) {
                        return -1;
                    }
                    return 0;
                }
            });
            List<BpmCalCalendar> resultBlock = bpmCalCalendarService.getByStartDateAndSettingId(startBlock.getStartTime(), settingId);
            for (int i = 0; i < resultBlock.size(); i++) {
                int sub = (int) ((resultBlock.get(i).getEndTime().getTime() - resultBlock.get(i).getStartTime().getTime()) / BpmCalCalendar.TIME_NUM);
                if (minute > 0) {
                    int tempMinute = minute;
                    minute = minute - sub;
                    if (minute <= 0) {
                        resultCalendar = resultBlock.get(i);
                        return new Timestamp(resultCalendar.getStartTime().getTime() + tempMinute * BpmCalCalendar.TIME_NUM);
                    }
                } else {
                    resultCalendar = resultBlock.get(i);
                    break;
                }

            }
        }
        if (resultCalendar != null) {
            return resultCalendar.getStartTime();
        } else {
            return null;
        }
    }
}
