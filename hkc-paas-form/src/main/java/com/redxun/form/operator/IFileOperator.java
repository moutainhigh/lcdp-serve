package com.redxun.form.operator;


import com.redxun.dto.sys.SysFileDto;

import java.io.InputStream;

public interface IFileOperator {
	
	String getTitle();
	
	String getType();

	InputStream getInputStream(SysFileDto sysFile, boolean transPdf, boolean isScale);

}
