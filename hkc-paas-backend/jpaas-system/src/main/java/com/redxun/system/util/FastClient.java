package com.redxun.system.util;

import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * FASTDFS 文件上传下载。
 * @author RAY
 *
 */
@Component
@Slf4j
public class FastClient {

	@Autowired
	ConfigService configService;

	public void init() throws IOException, MyException{
		Properties properties=new Properties();
		String config ="";
		try {

			config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);

		}catch (Exception e){
			log.error(ExceptionUtil.getExceptionMessage(e));
		}
		properties.put("fastdfs.connect_timeout_in_seconds", SysFileUtil.getConfigKey(config,"connect_timeout"));
		properties.put("fastdfs.network_timeout_in_seconds",SysFileUtil.getConfigKey(config,"network_timeout"));
		properties.put("fastdfs.charset",SysFileUtil.getConfigKey(config,"charset"));
		properties.put("fastdfs.http_tracker_http_port",SysFileUtil.getConfigKey(config,"http_tracker_http_port"));
		properties.put("fastdfs.http_anti_steal_token",SysFileUtil.getConfigKey(config,"http_anti_steal_token"));
		properties.put("fastdfs.http_secret_key",SysFileUtil.getConfigKey(config,"http_secret_key"));
		properties.put("fastdfs.tracker_servers",SysFileUtil.getConfigKey(config,"tracker_servers"));

		ClientGlobal.initByProperties(properties);
	}

	/**
	 * 上传文件
	 * @param bytes		文件内容
	 * @param extName	扩展名
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public String uploadFile(byte[] bytes,String extName) throws IOException, MyException{

        TrackerClient tracker = new TrackerClient(); 
        TrackerServer trackerServer = tracker.getConnection(); 
        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer); 
        
        String fileIds[] = storageClient.upload_file(bytes, extName, null);
        
        return fileIds[0] +"#" + fileIds[1];
	}
	
	/**
	 * 根据文件路径删除文件。
	 * path 组#文件路径
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public int delFile(String path) throws IOException, MyException{
		String[] ary=path.split("#");
		 TrackerClient tracker = new TrackerClient(); 
         TrackerServer trackerServer = tracker.getConnection(); 
         StorageServer storageServer = null;

         StorageClient storageClient = new StorageClient(trackerServer, 
                 storageServer); 
         int i = storageClient.delete_file(ary[0], ary[1]); 
         return i;
	}
	
	/**
	 * 根据文件的组和路径获取文件
	 * path 组#文件路径
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public byte[] getFile(String path) throws IOException, MyException{
		String[] ary=path.split("#");
		 TrackerClient tracker = new TrackerClient(); 
         TrackerServer trackerServer = tracker.getConnection(); 
         StorageServer storageServer = null;
         
         StorageClient storageClient = new StorageClient(trackerServer, storageServer); 
         byte[] bytes = storageClient.download_file(ary[0], ary[1]);

       
         return bytes;
	}

}
