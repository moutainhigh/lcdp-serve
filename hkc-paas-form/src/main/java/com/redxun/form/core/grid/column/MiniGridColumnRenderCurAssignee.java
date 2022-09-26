package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.BpmTaskClient;
import com.redxun.feign.OsUserClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 表格列表中流程实例的列的展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderCurAssignee implements MiniGridColumnRender{
	@Resource
	BpmTaskClient bpmTaskManager;
	@Resource
	OsUserClient userService;

	@Override
	public String getRenderType() {
		return MiniGridColumnType.CUR_ASSIGNEE.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		String instId = (String) rowData.get(FormBoEntity.FIELD_INST);
		if(StringUtils.isNotEmpty(instId)) {
			List<BpmTaskDto> list = bpmTaskManager.getByInstId(instId);
			if (BeanUtil.isNotEmpty(list)) {
				OsUserDto iUser = userService.getById(list.get(0).getAssignee());
				if (iUser != null) {
					return iUser.getFullName();
				}
			}
		}
		return "";
	}
}
