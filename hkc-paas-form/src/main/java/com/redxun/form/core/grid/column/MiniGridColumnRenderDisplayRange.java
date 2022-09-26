package com.redxun.form.core.grid.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 表格列表中数值范围
 * @author mansan
 *
 */
@Component
public class MiniGridColumnRenderDisplayRange implements MiniGridColumnRender{
	@Resource
	GroovyEngine groovyEngine;
	@Override
	public String getRenderType() {
		return MiniGridColumnType.DISPLAY_RANGE.name();
	}

	@Override
	public String render(GridHeader gridHeader, Map<String,Object> rowData, Object val, boolean isExport) {
		if(BeanUtil.isEmpty(val)) {
			return "";
		}
		JSONArray colorConfigs=gridHeader.getRenderConfObj().getJSONArray("colorConfigs");
		String isfull=gridHeader.getRenderConfObj().getString("isfull");
		if(StringUtils.isEmpty(isfull)){
			isfull="";
		}
		for(int i=0;i<colorConfigs.size();i++){
			JSONObject rowObj=colorConfigs.getJSONObject(i);
			String express=rowObj.getString("express");
			String name=rowObj.getString("name");
			if(StringUtils.isEmpty(express)){
				continue;
			}
			try{
				Map<String,Object> params=new HashMap<>(16);
                String dataType=gridHeader.getCurDataType();
                if("int".equals(dataType) || "number".equals(dataType)){
                    val=Double.valueOf(val.toString());
                }
                params.put("value", val);
				Object result=groovyEngine.executeScripts(express, params);
				//检查该条件是否满足
				if(result instanceof Boolean){
					Boolean rs=(Boolean)result;
					if(rs==true){
						
						if(isExport){
							return StringUtils.isEmpty(name)?val.toString():name;
						}
						String bgcolor=rowObj.getString("bgcolor");
						String fgcolor=rowObj.getString("fgcolor");
						String frame=rowObj.getString("frame");
						if(StringUtils.isEmpty(bgcolor)){
							bgcolor="green";
						}
						if(StringUtils.isEmpty(fgcolor)){
							fgcolor="white";
						}
						if(StringUtils.isEmpty(frame)){
							frame="white";
						}
						return "<span class='display-label-"+isfull+"' style='background-color:"+bgcolor
								+ ";color:" + fgcolor+ "; border: 1px solid "+ frame +";border-radius: 5px;padding: 3px 10px 3px 10px;'>"+(StringUtils.isEmpty(name)?val:name)+"</span>";
						
					}
				}
			}catch(Exception e){
			}
			
		}
		
		return val.toString();
	}
}
