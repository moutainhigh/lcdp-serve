package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.constant.ProcessStatus;
import com.redxun.common.tool.BeanUtil;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 表格列表中流程实例的列的展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderFlowStatus implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.FLOW_STATUS.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		String label=val.toString();
		//若找到该状态
        try {
            ProcessStatus status= ProcessStatus.valueOf(label);
            if(status!=null){
                label= status.getStatusLabel();
            }
        }catch (Exception e){
            label = "运行中";
        }


		if(!isExport){
			return "<span class='display-label- flow_status_"+val.toString().toLowerCase()+"'>"+label+"</span>";
		}
		return label;
	}
}
