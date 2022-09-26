package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.feign.BpmTaskClient;
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
public class MiniGridColumnRenderOverTime implements MiniGridColumnRender{
	@Resource
	BpmTaskClient bpmTaskManager;
	
	@Override
	public String getRenderType() {
		return MiniGridColumnType.OVER_TIME.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		String instId = (String) rowData.get(FormBoEntity.FIELD_INST);
		if(StringUtils.isNotEmpty(instId)) {
			List<BpmTaskDto> list = bpmTaskManager.getByInstId(instId);
			if (BeanUtil.isNotEmpty(list)) {
				BpmTaskDto task = list.get(0);
				Integer overtime = 0;
				if (overtime > 0) {
					String tmp = DateUtils.getDisplayTime(overtime);
					return "<span style='color:red'>超时(" + tmp + ")</span>";
				}
			}
		}
		return "<span style='color:green'>正常</span>";
	}
}
