package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zfh
 */
@Component
public class MiniGridColumnRenderMap implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.MAP.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		String result = "";
		if(BeanUtil.isNotEmpty(val)){
			JSONObject renderConfObj = gridHeader.getRenderConfObj();
			String map = renderConfObj.getString("MAP");
			JSONObject jo = JSONObject.parseObject((String)val);
			String location = "坐标：" + jo.getString("longitude") + "," + jo.getString("latitude");
			String address = "地址：" + jo.getString("address");
			if(StringUtils.isBlank(map)){
				result = location;
			}else{
				if(map.equals("location")){
					result = location;
				}else if(map.equals("address")){
					result = address;
				}else if(map.equals("locAdd")){
					result = location+ "   " + address;
				}
			}
		}
		return result;
	}

}
