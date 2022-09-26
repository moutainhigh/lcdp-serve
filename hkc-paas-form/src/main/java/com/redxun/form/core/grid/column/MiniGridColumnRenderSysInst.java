package com.redxun.form.core.grid.column;


import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderSysInst implements MiniGridColumnRender{
	@Resource
	OsInstClient sysInstManager;
	
	@Override
	public String getRenderType() {
		return MiniGridColumnType.SYSINST.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		OsInstDto sysInst=sysInstManager.getById(val.toString());
		if(sysInst==null) {
			return "";
		}
        String showField=gridHeader.getRenderConfObj().getString("showField");
        if(StringUtils.isEmpty(showField)){
            showField="nameCn";
        }
        String userLabel=(String) BeanUtil.getFieldValueFromObject(sysInst, showField);
		return userLabel;
	}
}
