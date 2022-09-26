package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmCalCalendar;
import com.redxun.bpm.core.mapper.BpmCalCalendarMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* [工作日历安排]业务服务类
*/
@Service
public class BpmCalCalendarServiceImpl extends SuperServiceImpl<BpmCalCalendarMapper, BpmCalCalendar> implements BaseService<BpmCalCalendar> {

    @Resource
    private BpmCalCalendarMapper bpmCalCalendarMapper;

    @Override
    public BaseDao<BpmCalCalendar> getRepository() {
        return bpmCalCalendarMapper;
    }

    /**
     * 寻找date是否在某个WorkCalendar区间内,在的话获得workCalendar
     * @param date
     * @return
     */
    public List<BpmCalCalendar> getTimeBlock(Date date,String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("startTime",date);
        params.put("endTime",date);
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getTimeBlock(params);
    }

    /**
     * 寻找从某个Date开始的某个settingId下的工作日历列表
     * @param date
     * @param settingId
     * @return
     */
    public List<BpmCalCalendar> getByStartDateAndSettingId(Date date,String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("startTime",date);
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getByStartDateAndSettingId(params);
    }

    /**
     * 获取能包裹这个startTime和endtTime的稍微大一点的时间碎片
     * @param startDate
     * @param endDate
     * @param settingId
     * @return
     */
    public BpmCalCalendar getBiggerBlockThanStartAndEnd(Date startDate,Date endDate,String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getBiggerBlockThanStartAndEnd(params);
    }

    /**
     *  获取|--- I*****|----------------------I [*****]区间,也就是交集
     * @param date
     * @param settingId
     * @return
     */
    public BpmCalCalendar getBlockBetweenStartAndEnd(Date date,String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("startDate",date);
        params.put("endDate",date);
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getBlockBetweenStartAndEnd(params);
    }

    /**
     * 通过开始时间结束时间以及所属于的日历设定获取workCalendar
     * @param startDate
     * @param endDate
     * @param settingId
     * @return
     */
    public List<BpmCalCalendar> getByStartAndEndAndSettingId(Date startDate,Date endDate,String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("startDate",startDate);
        params.put("endDate",endDate);
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getByStartAndEndAndSettingId(params);
    }

    public void delByConnectId(String connectId){
        bpmCalCalendarMapper.delByConnectId(connectId);
    }

    public List<BpmCalCalendar> getWorkCalendarBySettingId(String settingId){
        Map<String,Object> params=new HashMap<>();
        params.put("settingId",settingId);
        return bpmCalCalendarMapper.getWorkCalendarBySettingId(settingId);
    }

    public List<BpmCalCalendar> getCalendarInDayBySettingIdConnectId(Date date, String settingId, String connectId){
        Calendar paramCalendar =new GregorianCalendar();
        paramCalendar.setTime(date);
        paramCalendar.set(Calendar.HOUR_OF_DAY, 0);
        paramCalendar.set(Calendar.MINUTE, 0);
        paramCalendar.set(Calendar.SECOND, 0);
        paramCalendar.set(Calendar.MILLISECOND, 0);
        Date thisDay=paramCalendar.getTime();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(thisDay);
        calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        Date nextDay=calendar.getTime();

        Map<String,Object> params=new HashMap<>();
        params.put("startTime",thisDay);
        params.put("endTime",nextDay);
        params.put("settingId",settingId);
        params.put("connectId",connectId);
        return bpmCalCalendarMapper.getCalendarInDayBySettingIdConnectId(params);
    }
}
