package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderDate implements MiniGridColumnRender{
	
	protected  Logger logger=LogManager.getLogger(MiniGridColumnRenderDate.class);
	
	@Override
	public String getRenderType() {
		return MiniGridColumnType.DATE.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		String format=gridHeader.getRenderConfObj().getString("format");
		if(StringUtils.isEmpty(format)) {
			format="yyyy-MM-dd";
		}
		DateFormat df=new SimpleDateFormat(format);
		try{
			if(val instanceof Date){
				return df.format((Date)val);
			}else{
				Date ndate= DateUtils.parseDate(val.toString());
				return df.format(ndate);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
			return val.toString();
		}
	}

}
