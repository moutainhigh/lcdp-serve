package com.redxun.portal.context.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * vue 栏目类型。
 */
@Service
@Slf4j
public class VueBelServiceImpl extends BaseColumnDataServiceImpl {

	@Override
	public String getType() {
		return "VueBel";
	}

	@Override
	public String getName() {
		return "VUE组件";
	}

	@Override
	public Object getData(){
		return null;
	}


}
