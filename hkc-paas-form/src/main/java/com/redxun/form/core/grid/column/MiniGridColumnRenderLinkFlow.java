package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.KeyValEnt;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmInstDto;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.BpmInstClient;
import com.redxun.feign.BpmTaskClient;
import com.redxun.feign.OsUserClient;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 表格列表中流程实例的列的展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderLinkFlow implements MiniGridColumnRender{
	@Resource
	BpmInstClient bpmInstManager;
	@Resource
	BpmTaskClient bpmTaskManager;
	@Resource
	OsUserClient userService;

	@Override
	public String getRenderType() {
		return MiniGridColumnType.LINK_FLOW.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(val==null || StringUtils.isEmpty(val.toString())){
			return "";
		}
		BpmInstDto bpmInst=bpmInstManager.getById(val.toString());
		if(bpmInst==null || StringUtils.isEmpty(bpmInst.getInstId())){
			return "";
		}

		JPaasUser user = (JPaasUser) ContextUtil.getCurrentUser();

		JSONObject jsonObject= gridHeader.getRenderConfObj();
		String showTitle=jsonObject.getString("showTitle");
		String showBpmInstLink=jsonObject.getString("showBpmInstLink");
		String showTask=jsonObject.getString("showTask");;
		String showTaskHandler=jsonObject.getString("showTaskHandler");

		if(StringUtils.isEmpty( bpmInst.getActInstId())){
			return "";
		}


		JSONObject sb=new JSONObject();
		if(!isExport && MBoolean.TRUE_LOWER.val.equals(showBpmInstLink)){
			sb.put("instId",bpmInst.getInstId());
		}
		if(MBoolean.TRUE_LOWER.val.equals(showTitle)){
			sb.put("subject",bpmInst.getSubject());
		}

		if(MBoolean.TRUE_LOWER.val.equals(showTask)){
			List<BpmTaskDto> bpmTasks=bpmTaskManager.getByActInstId(bpmInst.getActInstId());
			JSONArray taskAry=new JSONArray();
			for(BpmTaskDto task:bpmTasks){
				JSONObject taskObj=new JSONObject();
				String taskUsers=null;
				//当前用户是否具有该任务的处理权限
				boolean isCurTask=false;
				if(user!=null && user.isAdmin()){
				    isCurTask=true;
                }
				if(!isExport && MBoolean.TRUE_LOWER.val.equals(showTaskHandler)){
					if(StringUtils.isNotEmpty(task.getAssignee())){
						IUser iUser=userService.getById(task.getAssignee());
						if(user!=null && iUser!=null && user.getUserId().equals(iUser.getUserId())){
							isCurTask=true;
						}
						if(iUser!=null){
							taskUsers=iUser.getFullName();
						}
					}else{
						Collection<TaskExecutor> bpmIdenties= bpmTaskManager.getTaskExecutors(task.getTaskId());
						KeyValEnt ent= getUserInfoIdNames(bpmIdenties);
						if(user!=null && ent.getKey().indexOf(user.getUserId())!=-1){
							isCurTask=true;
						}
						taskUsers=ent.getVal().toString();
					}
				}

				if(!isExport && isCurTask){
					taskObj.put("taskId",task.getTaskId());
				}
				taskObj.put("taskName",task.getName());
				if(StringUtils.isNotEmpty(taskUsers)){
					taskObj.put("isUsers",true);
					taskObj.put("taskUsers",taskUsers);
				}else{
					taskObj.put("isUsers",false);
					taskObj.put("taskUsers","无审批人");
				}
				taskAry.add(taskObj);
			}
			sb.put("taskAry",taskAry);
		}
		return sb.toString();
	}

	private KeyValEnt getUserInfoIdNames(Collection<TaskExecutor> identityInfos){
		StringBuffer userNames=new StringBuffer();
		StringBuffer userIds=new StringBuffer();
		//显示用户
		for(TaskExecutor info:identityInfos){
			if(TaskExecutor.TYPE_USER.equals(info.getType())){
				userNames.append(info.getName()).append(",");
				userIds.append(info.getId()).append(",");
			}else {
				List<OsUserDto> osUsers=userService.getByGroupId(info.getId());
				for(IUser user:osUsers){
					userNames.append(user.getFullName()).append(",");
					userIds.append(user.getUserId()).append(",");
				}
			}
		}

		if(userNames.length()>0){
			userNames.deleteCharAt(userNames.length()-1);
			userIds.deleteCharAt(userIds.length()-1);
		}

		return new KeyValEnt(userIds.toString(), userNames.toString());

	}

}
