package com.redxun.portal.context.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.portal.feign.FormCustomQueryClient;
import com.redxun.portal.feign.SysInterfaceApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 列表栏目服务类
 */
@Service
@Slf4j
public class ListServiceImpl extends BaseColumnDataServiceImpl {

	@Override
	public String getType() {
		return "List";
	}

	@Override
	public String getName() {
		return "列表";
	}

	private static final int CODE=200;

	@Resource
	private GroovyEngine groovyEngine;
	/**
	 * 注入表单自定义查询客户端
	 */
	@Resource
	private FormCustomQueryClient formCustomQueryClient;
	//注入第三方接口服务
	@Resource
    private SysInterfaceApiClient sysInterfaceApiClient;

	/**
	 * 获取返回的数据
	 * @return
	 */
	@Override
	public Object getData(){
		JSONObject settingObj = JSONObject.parseObject(this.getSetting());
		String funcType = settingObj.getString("funcType");
		String function = settingObj.getString("function");
		if(StringUtils.isEmpty(function)){
			return null;
		}
		Object object=null;
		try{
			if(ListServiceImpl.SQL_TYPE.equals(funcType)){
				JsonResult result =formCustomQueryClient.queryForJsonByAlias(function);
				if(ListServiceImpl.CODE==result.getCode()){
					object=result.getData();
				}
			}else if(ListServiceImpl.INTERFACE_TYPE.equals(funcType)){
                JsonResult result=sysInterfaceApiClient.executeApi(function,"{}");
                if(ListServiceImpl.CODE==result.getCode()){
                    object=result.getData();
                }
            }else {
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("colId", getColId());
				object =   groovyEngine.executeScripts(function,params);
			}

		}catch(Exception e){
			log.error("----ListService.getData() is error -----:"+e.getMessage());
		}
		return object;
	}
}
