package com.redxun.gencode.util;

import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * json文件加载。
 * @author RAY
 *
 */
@Slf4j
@Component
public class ReaderFileUtil {
	public static String CODE="_code";
	public static String ADDRESS_PROVINCE="province";

	public static JSONObject getEntityJson(String formAlias,String pack){
		try {
			ClassPathResource classPathResource = new ClassPathResource("entityJson/"+pack+"/"+formAlias+".json");
			InputStream resourceAsStream = classPathResource.getStream();
			String fileStr= toString(resourceAsStream, StandardCharsets.UTF_8);
			JSONObject obj =JSONObject.parseObject(fileStr);
			return obj;
		}catch (Exception e){
			log.error("ReaderFileUtil.getEntityJson is error : message ={}", ExceptionUtil.getExceptionMessage(e));
		}
		return new JSONObject();
	}

	public static JSONObject getDefaultCtlConfigJson(){
		try {
			ClassPathResource classPathResource = new ClassPathResource("entityJson/core/defaultCtlConfig.json");
			InputStream resourceAsStream = classPathResource.getStream();
			String fileStr= toString(resourceAsStream, StandardCharsets.UTF_8);
			JSONObject defaultCtlConfigJson =JSONObject.parseObject(fileStr);
			return defaultCtlConfigJson;
		}catch (Exception e){
			log.error("ReaderFileUtil.getDefaultCtlConfigJson is error : message ={}", ExceptionUtil.getExceptionMessage(e));
		}
		return new JSONObject();
	}

	public static String toString(InputStream inputStream, Charset charset) throws IOException {
		final int bufferSize = 1024;
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		Reader in = new InputStreamReader(inputStream, charset);

		int charsRead;
		while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
			out.append(buffer, 0, charsRead);
		}

		return out.toString();
	}
	public static JSONObject parseAddressFromExtJson(String extJsonStr){
		JSONObject extJson = new JSONObject();
		if(StringUtils.isEmpty(extJsonStr)){
			return extJson;
		}
		extJson = JSONObject.parseObject(extJsonStr);
		JSONObject setting = extJson.getJSONObject("setting");
		return setting;
	}

	public static JSONObject parseStrToJson(String str){
		JSONObject strJson = new JSONObject();
		if(StringUtils.isEmpty(str)){
			return strJson;
		}
		return JSONObject.parseObject(str);
	}


	public static String getAddressFromExtJson(String extJsonStr,String key){
		if(StringUtils.isEmpty(extJsonStr)){
			return "";
		}
		JSONObject extJson = JSONObject.parseObject(extJsonStr);
		JSONObject setting = extJson.getJSONObject("setting");
		return setting.getString(key);
	}


	public static int countAddressFromExtJson(String extJsonStr){
		int extNum=0;
		if(StringUtils.isEmpty(extJsonStr)){
			return extNum;
		}
		JSONObject extJson = JSONObject.parseObject(extJsonStr);
		JSONObject setting = extJson.getJSONObject("setting");
		if(setting.getBoolean("isCity")){
			extNum++;
		}
		if(setting.getBoolean("isCounty")){
			extNum++;
		}
		if(setting.getBoolean("isAddress")){
			extNum++;
		}
		return extNum;
	}

	public static String jsonToStr(Object strJson){
		String jsonStr="";
		if(BeanUtil.isEmpty(strJson)){
			return jsonStr;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			jsonStr = objectMapper.writeValueAsString(strJson);
		}catch (Exception e){
			log.error("*************ReaderFileUtil.jsonToStr is error :message={}",ExceptionUtil.getExceptionMessage(e));
		}
		return jsonStr;
	}

	public static JSONObject getColByColList(Object model,String colName,String keyType){
		JSONObject col= new JSONObject();
		if(BeanUtil.isEmpty(model) || StringUtils.isEmpty(colName)|| StringUtils.isEmpty(keyType)){
			return col;
		}
		try {
			String modelStr = JSONObject.toJSONString(model);
			JSONObject modelJson = JSONObject.parseObject(modelStr);
			JSONArray  boAttrList = modelJson.getJSONArray("boAttrRefList");
			if(BeanUtil.isEmpty(boAttrList)){
				return col;
			}
			for (Object colObj:boAttrList) {
				JSONObject attr = (JSONObject)colObj;
				if(colName.equals(attr.getString(keyType))){
					return  attr;
				}
			}
		}catch (Exception e){
			log.error("*************ReaderFileUtil.getColByColList is error :message={}",ExceptionUtil.getExceptionMessage(e));
		}
		return col;
	}

	public static String getColNameByFielName(JSONObject fiels,String fieldName){
		if(BeanUtil.isEmpty(fiels) || StringUtils.isEmpty(fieldName)){
			return "";
		}

		for (String key:fiels.keySet()) {
			JSONObject fiel =fiels.getJSONObject(key);
			if(fieldName.equals(fiel.getString("fieldName"))){
				return fiel.getString("name");
			}
		}
		return "";
	}

	public static JSONObject getRelation(Object relations,String key){
		if(BeanUtil.isEmpty(relations) || StringUtils.isEmpty(key)){
			return new JSONObject();
		}
		String relationsStr = JSONObject.toJSONString(relations);
		JSONObject relationJsons= JSONObject.parseObject(relationsStr);
		JSONObject relation = relationJsons.getJSONObject(key);
		if(BeanUtil.isNotEmpty(relation)){
			return relation;
		}
		return new JSONObject();
	}
}
