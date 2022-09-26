package com.redxun.portal.context.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.core.entity.InsNews;
import com.redxun.portal.core.service.InsNewsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻公告消息类型获取服务类
 */
@Service
@Slf4j
public class NewsBelServiceImpl extends BaseColumnDataServiceImpl {

	@Resource
	private InsNewsServiceImpl insNewsService;

	@Override
	public String getType() {
		return "NewsBel";
	}

	@Override
	public String getName() {
		return "新闻公告";
	}

	/**
	 * 获取新闻公告数据列表
	 * @return
	 */
	@Override
	public Object getData(){
		JSONObject settingObj = JSONObject.parseObject(this.getSetting());
		String sysDic = settingObj.getString("newDIc");
		if(StringUtils.isEmpty(sysDic)){
			return new ArrayList<>();
		}

		QueryFilter filter=new QueryFilter();
		IPage page= filter.getPage();
		page.setSize(8);
		//返回新闻公告
		List<InsNews> list = insNewsService.getBySysDicNew(page,sysDic);
		return list;
	}


}
