package com.redxun.form.operator.impl;

import com.redxun.dto.sys.SysFileDto;
import com.redxun.form.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 文件操作。
 * @author ray
 *
 */
@Slf4j
@Component("file")
public class FileOperator extends BaseFileOperator {

	@Override
	public String getTitle() {
		return "文件系统";
	}

	@Override
	public String getType() {
		return "file";
	}

    @Override
    public InputStream getInputStream(SysFileDto sysFile, boolean transPdf, boolean isScale) {
        try {
            // 创建file对象
            String path= SysFileUtil.getFilePath(sysFile,isScale,transPdf);
            File file = new File(path);
            return new FileInputStream(file);
        }catch (Exception e){
            log.error("------FileOperator.getInputStream is error :"+e.getMessage());
        }
        return null;
    }


}
