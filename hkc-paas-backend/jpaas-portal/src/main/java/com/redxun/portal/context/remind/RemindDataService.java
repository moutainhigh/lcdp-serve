package com.redxun.portal.context.remind;

import com.redxun.portal.core.entity.InsRemindDef;

import java.util.List;

/**
 * 获取用户配置信息。
 * @author ray
 *
 */
public interface RemindDataService {

	/**
	 * 获取当前选择月分/日期的数据。
	 *  查询参数：{
	 * 	    calendarMode： 查询类型：月 year  天：month
	 * 	    calendarValue:查询的月份或者日期2020-05/2020-05-18
	 * 	 }
	 *
	 *  返回结果 ：[
	 * 	{
	 *    index:0,
	 *    day:日期 2020-05-19
	 *    startTime:开始时间 08:30
	 *    title:标题
	 * 	  describe:描述
	 * 	  ******自定义返回参数
	 * 	 }
	 * ]
	 *
	 * @return
	 */
	/**
	 * 获取策略配置数据。
	 * @return
	 */
	List<InsRemindDef> getData();
}
