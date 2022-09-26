package com.redxun.system.operator;

import com.redxun.system.core.entity.SysFile;
import com.redxun.system.util.FileModel;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作接口。
 */
public interface IFileOperator {
	
	String getTitle();
	
	String getType();

	/**
	 * 下载文件
	 * @param response
	 * @param sysFile
	 * @param transPdf
	 * @param isScale
     * @param isDownload
	 * @return
	 */
	void downFile(HttpServletResponse response, SysFile sysFile, boolean transPdf, boolean isScale,boolean isDownload);

	/**
	 * 创建文件，传入扩展名和文件内容写入文件。
	 * @param bytes
	 * @return
	 */
	FileModel createFile(String fileName, byte[] bytes);
	/**
	 * 删除文件。
	 * @param path
	 * @return
	 */
	int delFile(String path);


	/**
	 * 将文件转换成PDF。
	 * 返回ConverResult。
	 * @param file
	 */
	ConverResult convertPdf(SysFile file);

}
