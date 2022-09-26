package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
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
public class MiniGridColumnRenderCurNode implements MiniGridColumnRender{
	@Resource
	BpmTaskClient bpmTaskManager;

	@Override
	public String getRenderType() {
		return MiniGridColumnType.CUR_NODE.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		String instId = (String) rowData.get(FormBoEntity.FIELD_INST);
		if(StringUtils.isNotEmpty(instId)) {
			List<BpmTaskDto> list = bpmTaskManager.getByInstId(instId);
			if (BeanUtil.isNotEmpty(list)) {
				BpmTaskDto bpmTask=list.get(0);
				if(StringUtils.isEmpty(bpmTask.getName())){
					return bpmTask.getKey();
				}
				return bpmTask.getName();
			}
		}
		return "";
	}
}
