package com.redxun.portal.context.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.portal.context.calendar.CalendarDataService;
import com.redxun.portal.context.calendar.entity.CalendarData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 日程服务数据接口。
 */
@Service
@Slf4j
public class CalendarServiceImpl extends BaseColumnDataServiceImpl {


	@Autowired
	CalendarDataService calendarDataService;

	@Override
	public String getType() {
		return "Calendar";
	}

	@Override
	public String getName() {
		return "日历";
	}



	@Override
	public Object getData(){
		JSONObject settingObj = JSONObject.parseObject(getSetting());
		List<CalendarData> listData=calendarDataService.getMonthOrDayData(settingObj.getString("calendarMode"),"");
		return listData;
	}

}
