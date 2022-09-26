package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.form.core.entity.FormRegLib;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import com.redxun.form.core.service.FormRegLibServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderRegLib implements MiniGridColumnRender{

	protected static Logger logger=LogManager.getLogger(MiniGridColumnRenderRegLib.class);
	@Resource
	FormRegLibServiceImpl formRegLibService;
	
	@Override
	public String getRenderType() {
		return MiniGridColumnType.REG_LIB.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		JSONObject reg=gridHeader.getRenderConfObj().getJSONObject("reg");
		if(reg==null) {
			return val.toString();
		}
		FormRegLib formRegLib = formRegLibService.getByKey(reg.getString("value"),FormRegLib.MASKING);
		if (BeanUtil.isNotEmpty(formRegLib) && val instanceof String) {
			String regText = formRegLib.getRegText();
			String mentText = formRegLib.getMentText();
			String obj = val.toString();
			return obj.replaceAll(regText, mentText);
		}
		return val.toString();
	}

}
