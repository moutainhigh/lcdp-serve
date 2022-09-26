package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gjh
 */
@Component
public class MiniGridColumnRenderAddress implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.ADDRESS.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		JSONObject renderConfObj = gridHeader.getRenderConfObj();
		String province = renderConfObj.getString("PROVINCE");
		String city = renderConfObj.getString("CITY");
		String county = renderConfObj.getString("COUNTY");
		String address = renderConfObj.getString("ADDRESS");
		String provinceStr="";
		String cityStr="";
		String countyStr="";
		String addressStr="";
		if(rowData.get(province)!=null){
			provinceStr =(String) rowData.get(province);
		}
		if(rowData.get(city)!=null){
			cityStr =(String) rowData.get(city);
		}
		if(rowData.get(county)!=null){
			countyStr =(String) rowData.get(county);
		}
		if(rowData.get(address)!=null){
			addressStr =(String) rowData.get(address);
		}
		return provinceStr+cityStr+countyStr+addressStr;
	}

}
