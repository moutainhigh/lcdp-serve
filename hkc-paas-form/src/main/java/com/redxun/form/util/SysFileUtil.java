package com.redxun.form.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.sys.SysFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 文件上传下载。
 * @author RAY
 *
 */
@Slf4j
@Component
public class SysFileUtil {
	public static final String FILE_CONFIG = "sysFileSource.json";
	public static final String DEFAULT_GROUP = "DEFAULT_GROUP";
	public static final String UPLOAD_PATH = "uploadPath";
	public static final String PDF = "pdf";


	/**
	 * 替换扩展名
	 * @param fileName
	 * @param ext
	 * @return
	 */
	public static String replaceExt(String fileName,String ext){
		fileName =fileName.substring(0,fileName.lastIndexOf(".")) + "."+ ext;
		return  fileName;
	}


	public static String getUploadPath(){
		String uploadPath=getConfigKey(UPLOAD_PATH);
		uploadPath=uploadPath.replaceAll("\\\\","/");
		if(!uploadPath.endsWith("/")){
			uploadPath=uploadPath+"/";
		}
		return uploadPath;
	}

	/**
	 * 获取文件路径。
	 * @param sysFile
	 * @param isScale
	 * @param isPdf
	 * @return
	 * @throws Exception
	 */
	public static String getFilePath(SysFileDto sysFile, boolean isScale, boolean isPdf) throws Exception {
		String uploadPath=getUploadPath();
		String fullPath;
		if(isPdf){
			if(PDF.equals(sysFile.getExt()) ){
				fullPath =  sysFile.getPath();
			}
			else {
				fullPath =  sysFile.getPdfPath();
			}
		}
		else{
			if (isScale){
				fullPath =  sysFile.getThumbnail();
			}else {
				fullPath = sysFile.getPath();
			}
		}

		fullPath=uploadPath + fullPath;

		return fullPath;
	}

	/**
	 * 根据属性在NACOS配置中获取值。
	 * <pre>
	 *     1. 配置先从sysFileSource.json中获取。
	 *     2. 获取不到则从 nacos-config.properties 配置中获取。
	 * </pre>
	 * @param key
	 * @return
	 */
	public static String getConfigKey(String key){
		String keyValue ="";
		try {
			ConfigService configService=SpringUtil.getBean(ConfigService.class);
			String config = configService.getConfig(FILE_CONFIG, DEFAULT_GROUP, 0L);
			if(StringUtils.isNotEmpty(config)){
				JSONObject sysFileStore = JSONObject.parseObject(config);
				keyValue = sysFileStore.getString(key);
			}
		}catch (Exception e){
			log.error("----获取config失败!");
		}

		return keyValue;
	}


	/**
	 * 配置先从JSON中获取，获取不到则到 nacos-config.properties 中获取。
	 * @param config
	 * @param key
	 * @return
	 */
	public static String getConfigKey(String config,String key){
		String keyValue ="";
		try {
			if(StringUtils.isNotEmpty(config)){
				JSONObject sysFileStore = JSONObject.parseObject(config);
				keyValue = sysFileStore.getString(key);
			}
		}catch (Exception e){

		}

		return keyValue;
	}
}
