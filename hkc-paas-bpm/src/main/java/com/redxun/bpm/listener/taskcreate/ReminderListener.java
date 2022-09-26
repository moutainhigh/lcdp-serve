package com.redxun.bpm.listener.taskcreate;

import com.redxun.api.bpm.ICalendarService;
import com.redxun.bpm.activiti.config.DateConfig;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.RemindDefConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.DateType;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCreateApplicationEvent;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmRuPathMapper;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 催办实例产生监听器
 *
 * @author redxun
 */
@Service
@Order(1)
public class ReminderListener implements ApplicationListener<TaskCreateApplicationEvent> {

	@Resource
	BpmRemindInstServiceImpl bpmRemindInstService;
	@Resource
	BpmRuPathServiceImpl bpmRuPathService;

	@Resource
	ProcessScriptEngine processScriptEngine;
	@Resource
	BpmDefService bpmDefService;
	@Autowired
	BpmCalSettingServiceImpl bpmCalSettingService;

	@Autowired
	ICalendarService iCalendarService;


	@Override
	public void onApplicationEvent(TaskCreateApplicationEvent event) {
		TaskEntity taskEnt = (TaskEntity) event.getSource();
		//获取催办定义。
		List<RemindDefConfig> remindList = event.getConfig().getRemindDefs();

		if (BeanUtil.isEmpty(remindList)) {
			//全局催办配置
			ProcessConfig processConfig = event.getProcessConfig();
			if (BeanUtil.isNotEmpty(processConfig) && BeanUtil.isNotEmpty(processConfig.getRemindDefs())) {
				remindList = processConfig.getRemindDefs();
			} else {
				return;
			}
		}
		//插入催办实例
		for (RemindDefConfig remindDef : remindList) {
			String condition = remindDef.getCondition();
			//首先判断条件是否满足。
			if (StringUtils.isNotEmpty(condition)) {
				//改为从流程定义的上下文脚本条件中获取内容
				Boolean rtn = (Boolean) processScriptEngine.exeScript(condition, taskEnt.getVariables());
				if (!rtn) {
					continue;
				}
			}
			addReminderInst(remindDef, taskEnt, event.getConfig());
		}
	}

	private void addReminderInst(RemindDefConfig remindDef, TaskEntity taskEnt, UserTaskConfig config) {

		//获取催办处理的基础时间，到期时间需要根据这个进行计算。
		Date baseTime = getStartTime(remindDef, taskEnt, config);
		if (baseTime == null) {
			return;
		}
		String nodeId = taskEnt.getTaskDefinitionKey();
		String instId = (String) taskEnt.getVariable(BpmInstVars.INST_ID.getKey());
		String userId = ContextUtil.getCurrentUserId();
		String depId = null;
		if (ContextUtil.getCurrentUser() != null) {
			depId = ContextUtil.getCurrentUser().getDeptId();
		}

		Integer endTime;
		String timeLimitHander = remindDef.getTimeLimitHandler();
		if (StringUtils.isNotEmpty(timeLimitHander)) {
			ITimeLimitHandler handler = (ITimeLimitHandler) SpringUtil.getBean(timeLimitHander);
			endTime = handler.getExpireTimeLimit(userId, depId, taskEnt.getProcessDefinitionId(), nodeId, instId, taskEnt.getId());
		} else {
			//过期时间。
			endTime = getMinite(remindDef.getExpireDate());
		}


		//计算出未来的时间。
		Date endDate = getFutureTime(taskEnt.getAssignee(), baseTime, remindDef, endTime);

		BpmRemindInst remindInst = new BpmRemindInst();

		remindInst.setTaskId(taskEnt.getId());
		remindInst.setScript(remindInst.getScript());
		//通知类型
		if (StringUtils.isNotEmpty(remindDef.getNotifyType())) {
			Integer timeToSend;
			if (StringUtils.isNotEmpty(timeLimitHander)) {
				ITimeLimitHandler handler = (ITimeLimitHandler) SpringUtil.getBean(timeLimitHander);
				timeToSend = handler.getSendTimeLimit(userId, depId, taskEnt.getProcessDefinitionId(), nodeId, instId, taskEnt.getId());
			} else {
				timeToSend = getMinite(remindDef.getTimeToSend());
			}
			Date startTime = getFutureTime(taskEnt.getAssignee(), baseTime, remindDef, timeToSend);
			if(BeanUtil.isEmpty(startTime)){
				throw new RuntimeException("催办发送时间计算错误!");
			}
			remindInst.setTimeToSend(startTime);
			remindInst.setNotifyType(remindDef.getNotifyType());


			remindInst.setSendTimes(remindDef.getSendTimes());
			//发送间隔时间
			int sendInterval = getMinite(remindDef.getSendInterval());
			remindInst.setSendInterval(sendInterval);

			//获取间隔
			int tmp = sendInterval * (remindDef.getSendTimes() - 1);
			Date endRemindDate = DateUtils.addMinutes(startTime, tmp);
			if (BeanUtil.isEmpty(endDate) || endRemindDate.after(endDate)) {
				throw new RuntimeException("催办时间必须小于到期时间!");
			}
		}
		remindInst.setStatus("create");
		remindInst.setExpireDate(endDate);
		remindInst.setAction(remindDef.getAction());
		remindInst.setName(remindDef.getName());
		bpmRemindInstService.insert(remindInst);
	}

	/**
	 * 获取发起的时间。
	 *
	 * @param remindDef
	 * @param taskEnt
	 * @return
	 */
	private Date getStartTime(RemindDefConfig remindDef, TaskEntity taskEnt, UserTaskConfig config) {
		//当前节点Id
		String curNodeId = config.getKey();
		//相对节点
		String relNodeId = remindDef.getNodeId();

		String instId = taskEnt.getVariableInstanceEntities().get("instId").getTextValue();
		//未配置了相对节点 或者相对节点为当前节点
		if (StringUtils.isEmpty(relNodeId) || relNodeId.equals(curNodeId)) {
			return new Date();
		} else {
			//获取最近的处理时间。
			BpmRuPath bpmRuPath = bpmRuPathService.getLatestByInstIdNodeId(instId, relNodeId);
			if (bpmRuPath == null) {
				return new Date();
			}
			if (EventType.TASK_COMPLETED.name().equals(remindDef.getEvent())) {
				Date date = bpmRuPath.getEndTime();
				if (date == null) {
					return new Date();
				}
				return bpmRuPath.getEndTime();
			} else {
				return bpmRuPath.getCreateTime();
			}
		}
	}

	/**
	 * {"day":0,"hour":"4","minute":"45"}
	 *
	 * @param config
	 * @return
	 */
	private int getMinite(DateConfig config) {
		int day = 0;
		try {
			day = Integer.parseInt(config.getDay());
		} catch (Exception e) {
		}
		int hour = 0;
		if(BeanUtil.isNotEmpty(config.getHour())){
			hour=config.getHour();
		}
		int minute = 0;
		if(BeanUtil.isNotEmpty(config.getMinute())){
			minute=config.getMinute();
		}
		int total = (24 * 60) * day + hour * 60 + minute;
		return total;
	}

	/**
	 * 计算将来的时间。
	 *
	 * @param userId
	 * @param startTime
	 * @param remindDef
	 * @param miniute
	 * @return
	 */
	private Date getFutureTime(String userId, Date startTime, RemindDefConfig remindDef, Integer miniute) {
		//没有配置日历的情况。
		if (DateType.COMMON.name().equals(remindDef.getDateType())) {
			return DateUtils.addMinutes(startTime, miniute);
		}
		if(iCalendarService==null){
			throw new RuntimeException("系统没有实现日历接口!");
		}
		// 用户不为空的情况。
		if(StringUtils.isNotEmpty(userId)){
			try{
				return iCalendarService.getByUserId(userId, startTime, miniute);
			}
			catch(Exception ex){
				throw new RuntimeException("获取日历时间出错!");
			}
		}
		else{
			try {
				//设置了日历
				if(StringUtils.isNotEmpty(remindDef.getCalendar())&&!"default".equals(remindDef.getCalendar())){
					BpmCalSetting calSetting= bpmCalSettingService.get(remindDef.getCalendar());
					if(BeanUtil.isNotEmpty(calSetting)) {
						return  iCalendarService.getEndTimeByCalendar(calSetting.getSettingId(), startTime, miniute);
					}
				}

				//使用默认日历
				BpmCalSetting calSetting= bpmCalSettingService.getIsCommon(BpmCalSetting.IS_COMMON_YES);
				if(calSetting==null) {
					throw new RuntimeException("没有设置默认日历!");
				}
				return  iCalendarService.getEndTimeByCalendar(calSetting.getSettingId(), startTime, miniute);

			} catch (Exception e) {
				throw new RuntimeException("获取日历时间出错!");
			}
		}
	}
}
