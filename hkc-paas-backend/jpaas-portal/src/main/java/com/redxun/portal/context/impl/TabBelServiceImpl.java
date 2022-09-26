package com.redxun.portal.context.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.core.entity.InsColumnDef;
import com.redxun.portal.core.entity.InsColumnTemp;
import com.redxun.portal.core.service.InsColumnDefServiceImpl;
import com.redxun.portal.core.service.InsColumnTempServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 门户tab栏目实现
 */
@Service
@Slf4j
public class TabBelServiceImpl extends BaseColumnDataServiceImpl {

	@Autowired
    InsColumnDefServiceImpl insColumnDefService;
	@Autowired
	InsColumnTempServiceImpl insColumnTempService;

	@Override
	public String getType() {
		return "TabBel";
	}

	@Override
	public String getName() {
		return "Tab标签页";
	}

	@Override
	public Object getData(){
		JSONArray newJsonArray = new JSONArray();
		JSONObject settingObj = JSONObject.parseObject(this.getSetting());
		JSONObject tabgroups = settingObj.getJSONObject("tabgroups");
		if(tabgroups==null){
			return newJsonArray;
		}
		String values = tabgroups.getString("value");
		if(StringUtils.isEmpty(values)){
			return newJsonArray;
		}

		String [] colIds = values.split("[,]");
		for (String newColId:colIds) {
			InsColumnDef insColumnDef = insColumnDefService.get(newColId);
			if(BeanUtil.isEmpty(insColumnDef)){
				continue;
			}
			InsColumnTemp insColumnTemp = insColumnTempService.get(insColumnDef.getType());
			insColumnDef.setTypeName(insColumnTemp.getTempType());
			JSONObject layout = new JSONObject();
			layout.put("insColumnDef", insColumnDef);
			layout.put("name", insColumnDef.getName());
			newJsonArray.add(layout);
		}
		return newJsonArray;
	}


}
