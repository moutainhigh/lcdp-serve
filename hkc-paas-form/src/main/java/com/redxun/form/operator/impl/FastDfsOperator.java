package com.redxun.form.operator.impl;

import com.redxun.dto.sys.SysFileDto;
import com.redxun.form.util.FastClient;
import com.redxun.form.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * fast dfs操作。
 * @author zhangyg
 *
 */
@Slf4j
@Component("fastdfs")
public class FastDfsOperator extends BaseFileOperator {
	@Resource
	private FastClient fastClient;

    @Override
    public InputStream getInputStream(SysFileDto sysFile, boolean transPdf, boolean isScale) {
        try {
            // 创建file对象
            String path=getFilePath(sysFile,isScale,transPdf);
            byte[]	bytes=getFile(path);
            return new ByteArrayInputStream(bytes);
        }catch (Exception e){
            log.error("------FastDfsOperator.getInputStream is error :"+e.getMessage());
        }
        return null;
    }

	public static String getFilePath(SysFileDto sysFile, boolean isScale, boolean isPdf) throws Exception {

		String fullPath;
		if(isPdf){
			if(SysFileUtil.PDF.equals(sysFile.getExt()) ){
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

		return fullPath;
	}




	private byte[] getFile(String path) {
		try {
			fastClient.init();
			byte[] bytes=fastClient.getFile(path);
			return bytes;
		} catch (Exception e) {
			log.error("------FastDfsOperator.getFile is error :"+e.getMessage());
		}
		return null;
	}


	@Override
	public String getTitle() {
		return "fast分布式文件系统";
	}

	@Override
	public String getType() {
		return "fastdfs";
	}



}
