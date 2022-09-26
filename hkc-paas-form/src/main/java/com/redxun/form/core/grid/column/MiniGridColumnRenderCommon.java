package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.form.core.grid.enums.MiniGridColumnType;

import java.util.Map;

/**
 * @author hujun
 */
public class MiniGridColumnRenderCommon implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.COMMON.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(val==null) {
			return "";
		}
		return val.toString();
	}

}
