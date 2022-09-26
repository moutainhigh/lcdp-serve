package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderScript implements MiniGridColumnRender{
	@Resource
	GroovyEngine groovyEngine;
	@Override
	public String getRenderType() {
		return MiniGridColumnType.SCRIPT.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		Map<String,Object> contenxtData=new HashMap<>();
		IUser curUser= ContextUtil.getCurrentUser();
		contenxtData.put("curUserId", curUser.getUserId());
		contenxtData.put("curDepId",curUser.getDeptId());
		contenxtData.put("curTenantId", curUser.getTenantId());
		contenxtData.put("value", val);
		contenxtData.put("isExport", isExport);
		contenxtData.putAll(rowData);
		contenxtData.put("record",rowData);
		String script=gridHeader.getRenderConfObj().getString("script");

		if(StringUtils.isNotEmpty(script)){
			Object obj=groovyEngine.executeScripts(script, contenxtData);
			if(obj!=null){
				return obj.toString();
			}
		}

		return val.toString();
	}

}
