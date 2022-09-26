package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 表格列表中数值百分比显示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderDisplayPercent implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.DISPLAY_PERCENT.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		if(!isExport){
			//是否为百分值
			Double numVal = new Double(0);
			//若为非数值
			if (!StringUtils.isNumeric(val.toString())) {
				return val.toString();
			}
			
			return  val.toString();
		}
		return val.toString();
	}
}
