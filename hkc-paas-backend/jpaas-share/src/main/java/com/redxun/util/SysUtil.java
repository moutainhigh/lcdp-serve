package com.redxun.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.controller.RestApiController;
import com.redxun.web.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统工具类，获取系统一些常量，扫描类注解获取系统的信息
 */
@Slf4j
public class SysUtil {

	/**
	 * 替换常量。
	 * @param sql
	 * @return
	 */
	public static String replaceConstant(String sql){
		ConstVarContext contextHandlerFactory=SpringUtil.getBean(ConstVarContext.class);
		String patten="\\[[a-z0-9A-Z-_\\.]*?\\]";
		Pattern p=Pattern.compile(patten, Pattern.CASE_INSENSITIVE);
		Matcher m= p.matcher(sql);
		while(m.find()){
			Object val=contextHandlerFactory.getValByKey(m.group(0),new HashMap<String, Object>());
			if(val==null) {
				continue;
			}
			sql=sql.replace(m.group(0), val.toString());
		}
		return sql;
	}
	
	/**
	 * 替换常量并且使用freemaker 进行替换。
	 * @param script
	 * @param vars
	 * @return
	 */
	public static String parseScript(String script, Map<String,Object> vars){
		FtlEngine freemarkEngine=SpringUtil.getBean(FtlEngine.class);
		script=replaceConstant(script);
		//使用freemark解析。
		try {
			script="<#setting number_format=\"#\">"+script;
			script = freemarkEngine.parseByStringTemplate(vars, script);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return script;
	}

	/**
	 * 获取微服务列表。
	 * @return
	 */
	public static List<String> getServices(){
		DiscoveryClient discoveryClient=SpringUtil.getBean(DiscoveryClient.class);
		return discoveryClient.getServices();
	}

	/**
	 * 获取除了 网关和 认证服务器的服务列表。
	 * @return
	 */
	public static List<String> getAppServices(){
		List<String> services = getServices();

		services.remove("jpaas-auth-server");
		services.remove("jpaas-gateway-server");
		services.remove("seata-server");
		services.remove("jpaas-job");
		services.remove("jpaas-screen");
		services.remove("jpaas-datart");
		services.remove("jpaas-ureport");

		return services;
	}

	/**
	 * 获取当前服务的外部API接口信息
	 * @return
	 * @throws Exception
	 */
	public static List<JSONObject> getAllClass() throws Exception{
		//获取所有的RestApiController类
		Collection<RestApiController> list= SpringUtil.getBeans(RestApiController.class);
		List<JSONObject> lists=new ArrayList<>();
		for (RestApiController restApiController : list) {
			JSONObject jsonObject=new JSONObject();
			//获取类定义注释
			ClassDefine classDefine=restApiController.getClass().getAnnotation(ClassDefine.class);
			if(classDefine==null){
				continue;
			}
			String className=restApiController.getClass().getName().split("\\$\\$")[0];
			JSONArray jsonArray=getAllMethodByClassName(className,classDefine,false);
			boolean hasChild=jsonArray.size()>0;
			jsonObject.put("title",classDefine.title());
			jsonObject.put("key",classDefine.alias());
			jsonObject.put("isLeaf",!hasChild);
			jsonObject.put("children",jsonArray);
			lists.add(jsonObject);
		}
		return lists;
	}

	/**
	 * 根据类名、ClassDefine、是否Parent获取所有方法名
	 * @param className
	 * @param classDefine
	 * @param isParent
	 * @return
	 * @throws Exception
	 */
	private static JSONArray getAllMethodByClassName(String className, ClassDefine classDefine,boolean isParent) throws Exception{
		JSONArray jarray = new JSONArray();
		Class clazz = Class.forName(className);
		Method[] methods = clazz.getDeclaredMethods();
		if(isParent){
			methods=ArrayUtils.addAll(methods,clazz.getSuperclass().getDeclaredMethods());
		}
		String classPath = classDefine.path();
		String service=SpringUtil.getApplicationContext().getId();
		service=service.substring(0,service.lastIndexOf("-"));
		for (Method method : methods) {
			String returnType = method.getReturnType().getCanonicalName();
			Integer modifirer = method.getModifiers();
			// 只要public方法
			if (modifirer != 1) {
				continue;
			}

			JSONObject jobMethod = new JSONObject();
			JSONArray jaryPara = new JSONArray();
			Class<?>[] paraArr = method.getParameterTypes();
			String desc = "";
			String methodType = "";
			String path = "";
			MethodDefine methodDefine = method.getAnnotation(MethodDefine.class);
			if(methodDefine==null){
				continue;
			}
			ParamDefine[] paramDefines = null;
			if (BeanUtil.isNotEmpty(methodDefine)) {
				desc = methodDefine.title();
				methodType = methodDefine.method().getValue();
				path = methodDefine.path();
				paramDefines = methodDefine.params();
			}
			if(paramDefines.length == paraArr.length) {
				for (int i = 0; i < paraArr.length; i++) {
					Class<?> para = paraArr[i];
					String paraName = "arg" + i;
					String paraDesc = "";
					if (BeanUtil.isNotEmpty(paramDefines)) {
						ParamDefine paramDefine = paramDefines[i];
						paraDesc = paramDefine.title();
						paraName = paramDefine.varName();
					}
					String paraType = para.getCanonicalName();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("paraName", paraName);
					jsonObject.put("paraType", paraType);
					jsonObject.put("paraDesc", paraDesc);
					jaryPara.add(jsonObject);
				}
			}
			String realPath = classPath + path;
			jobMethod.put("returnType", returnType);
			jobMethod.put("methodType", methodType);
			jobMethod.put("methodName", method.getName());
			jobMethod.put("service",service);
			jobMethod.put("para", jaryPara);
			jobMethod.put("title", "(" + methodType + ")" + desc);
			jobMethod.put("key", realPath);
			jarray.add(jobMethod);
		}
		return jarray;
	}

	/**
	 * 获取所有的API类
	 * @return
	 * @throws Exception
	 */
    public static List<JSONObject> getAllApiClass() throws Exception{
		Collection<BaseController> list= SpringUtil.getBeans(BaseController.class);
		List<JSONObject> lists=new ArrayList<>();
		for (BaseController baseController : list) {
			JSONObject jsonObject = new JSONObject();
			ClassDefine classDefine = baseController.getClass().getAnnotation(ClassDefine.class);
			if (classDefine == null) {
				continue;
			}
			String packages = classDefine.packages();
			String packageName = classDefine.packageName();
			String className = baseController.getClass().getName().split("\\$\\$")[0];
			JSONArray jsonArray = getAllMethodByClassName(className, classDefine, true);
			boolean hasChild = jsonArray.size() > 0;
			JSONObject json = new JSONObject();
			json.put("title", classDefine.title());
			json.put("key", classDefine.alias());
			json.put("isLeaf", !hasChild);
			json.put("children", jsonArray);
			boolean isExist = false;
			for (Object object : lists) {
				JSONObject obj = (JSONObject) object;
				if (packages.equals(obj.getString("key"))) {
					JSONArray array=obj.getJSONArray("children");
					array.add(json);
					hasChild = array.size() > 0;
					obj.put("isLeaf", !hasChild);
					obj.put("children", array);
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				JSONArray array=new JSONArray();
				array.add(json);
				hasChild = array.size() > 0;
				jsonObject.put("title", packageName);
				jsonObject.put("key", packages);
				jsonObject.put("isLeaf", !hasChild);
				jsonObject.put("children", array);
				lists.add(jsonObject);
			}
		}
		return lists;
    }

	/**
	 * 计算权限。
	 *
	 * @param rightAry
	 * @param profileMap
	 * @return
	 */
	public static boolean hasRights(String rightAry, Map<String, Set<String>> profileMap) {
		if (StringUtils.isEmpty(rightAry)) {
			return false;
		}
		//所有人
		if ("everyone".equals(rightAry)) {
			return true;
		}
		//无权限
		if ("none".equals(rightAry)) {
			return false;
		}
		if ("custom".equals(rightAry)) {
			return false;
		}
		JSONObject json = JSONObject.parseObject(rightAry);

		for (String key : profileMap.keySet()) {
			if (!json.containsKey(key)) {
				continue;
			}
			JSONObject setType = json.getJSONObject(key);
			String val = setType.getString("values");
			//配置为空跳过。
			if (StringUtils.isEmpty(val)) {
				continue;
			}
			Boolean include = setType.getBoolean("include");
			String[] aryVal = val.split(",");
			for (int j = 0; j < aryVal.length; j++) {
				String id = aryVal[j];
				Set<String> set = profileMap.get(key);
				if (BeanUtil.isNotEmpty(set)) {
					if (include) {
						if (set.contains(id)) {
							return true;
						}
					} else {
						if (!set.contains(id)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取返回租户的查询对象。
	 * @param baseExtEntity
	 * @return
	 */
	public static QueryWrapper getTenantWrapper(BaseExtEntity baseExtEntity){
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.eq("TENANT_ID_",baseExtEntity.getTenantId());
		return  wrapper;
	}



}
