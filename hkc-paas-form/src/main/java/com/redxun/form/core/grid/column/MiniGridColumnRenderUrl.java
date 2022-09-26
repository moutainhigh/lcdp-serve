package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class MiniGridColumnRenderUrl implements MiniGridColumnRender{
	@Override
	public String getRenderType() {
		return MiniGridColumnType.URL.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if (BeanUtil.isEmpty(val)) {
			return "";
		}
		String url = gridHeader.getRenderConfObj().getString("url");

		Map<String, Object> params = new HashMap<>();
		params.put("ctxPath", SysPropertiesUtil.getString("ctxPath"));
		Iterator<String> keyIt = rowData.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			params.put("record." + key, rowData.get(key));
		}
		try {
			url = StringUtils.replaceVariableMap(url, params);
		}catch (Exception e){
		}
		return url;
	}

}
