package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderNumber implements MiniGridColumnRender{

	protected static Logger logger=LogManager.getLogger(MiniGridColumnRenderNumber.class);
	
	
	@Override
	public String getRenderType() {
		return MiniGridColumnType.NUMBER.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		String format=gridHeader.getRenderConfObj().getString("format");
		if(StringUtils.isEmpty(format)) {
			return val.toString();
		}
		DecimalFormat df=new DecimalFormat(format);
		try{
			if(val instanceof Number) {
				String num = ((Number) val).doubleValue() == 0 ? "0" : "" + df.format(val);
				return num;
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return val.toString();
		}
		return val.toString();
	}

}
