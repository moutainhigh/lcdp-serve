package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 表格列表中显示标签列的值展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderDisplayLabel implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.DISPLAY_LABEL.name();
	}


	/**
	 * {
	 *     "colorConfigs": [
	 *         {
	 *             "index": "",
	 *             "code": "1",
	 *             "name": "踢球",
	 *             "bgcolor": "#d11515",
	 *             "fgcolor": "",
	 *             "idx_": "xfndres914443",
	 *             "serial": 1
	 *         }],
	 *	 separator:""
	 *}
	 *
	 * @param gridHeader
	 * @param rowData
	 * @param val
	 * @param isExport
	 * @return
	 */
	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		if(isExport){
			return (String)val;
		}
		JSONArray colorConfigs=gridHeader.getRenderConfObj().getJSONArray("colorConfigs");
		String separator=gridHeader.getRenderConfObj().getString("separator");


		String[] aryVal=val.toString().split("["+ separator+"]");

		Map<String, JSONObject> map=new HashMap<>();
		for(int i=0;i<colorConfigs.size();i++){
			JSONObject json=colorConfigs.getJSONObject(i);
			map.put(json.getString("code"),json);
		}
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<aryVal.length;i++){
			String tempVal=aryVal[i];
			JSONObject item=map.get(tempVal);
			String name=item.getString("name");
			if(StringUtils.isEmpty(name)){
				name=tempVal;
			}
			String bgcolor=item.getString("bgcolor");
			String fgcolor=item.getString("fgcolor");
			sb.append("<span class='multi-tag'   style='background-color:"+bgcolor+ ";color:" + fgcolor+ "'>"+name+"</span>");
		}

		return sb.toString();


	}
}
