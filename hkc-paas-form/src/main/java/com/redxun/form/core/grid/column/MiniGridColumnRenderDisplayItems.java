package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 表格列表中多项值枚举列的展示
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderDisplayItems implements MiniGridColumnRender{

	@Override
	public String getRenderType() {
		return MiniGridColumnType.DISPLAY_ITEMS.name();
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
	 *         }]
	 *	 spe
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
		JSONArray colorConfigs=gridHeader.getRenderConfObj().getJSONArray("colorConfigs");

		for(int i=0;i<colorConfigs.size();i++){
			JSONObject rowObj=colorConfigs.getJSONObject(i);
			String name=rowObj.getString("name");
			String code=rowObj.getString("code");
			if(StringUtils.isEmpty(name) || StringUtils.isEmpty(code)){
				continue;
			}
			//找到该值
			if(code.equals(val.toString())){
				//若为导出，直接显示其名称即可。
				if(isExport){
					return name;
				}
				String bgcolor=rowObj.getString("bgcolor");
				String fgcolor=rowObj.getString("fgcolor");
				if(StringUtils.isEmpty(bgcolor)){
					bgcolor="green";
				}
				if(StringUtils.isEmpty(fgcolor)){
					fgcolor="white";
				}

				return "<span class='multi-tag'  title='"+val.toString()+"' style='background-color:"+bgcolor
						+ ";color:" + fgcolor+ "'>"+name+"</span>";
			}

		}

		return val.toString();
	}
}
