package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hujun
 */
@Component
public class MiniGridColumnRenderText implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.TEXT.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(val==null) {
			return "";
		}
        Integer maxNote=gridHeader.getRenderConfObj().getInteger("maxNote");
		if(maxNote==null){
		    maxNote=100;
        }
		String str=val.toString();
		if(str.length()>maxNote) {
            return str.substring(0, maxNote) + "...";
        }
		return str;
	}

}
