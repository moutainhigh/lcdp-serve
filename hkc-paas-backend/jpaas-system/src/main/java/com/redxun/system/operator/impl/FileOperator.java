package com.redxun.system.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.utils.FileUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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

	@Autowired
	SysFileServiceImpl sysFileService;


	@Override
	public void downFile(HttpServletResponse response, SysFile sysFile, boolean transPdf, boolean isScale,boolean isDownload){
		try {
			// 创建file对象

			String fileName=sysFile.getFileName().toLowerCase();
			boolean isOffice= SysFileUtil.isOfficeDoc(sysFile.getExt());

			String	path= SysFileUtil.getFilePath(sysFile, isScale, transPdf);

			if(transPdf){
				fileName=SysFileUtil.replaceExt(sysFile.getFileName(),"pdf");
			}

			File file = new File(path);

            String ext=new MimetypesFileTypeMap().getContentType(file);
            response.setContentType(ext);

			addHeader(fileName,response,isDownload);

			if(transPdf){
				response.setContentType(CONTENT_TYPE_PDF);

				//如果没有转换则下载转换一次。
				boolean notConverted= MBoolean.NO.val.equals( sysFile.getCoverStatus());
				if(isOffice && notConverted){
					ConverResult result = convertPdf(sysFile);
					if(result.getSuccess()){
						String pdfPath = result.getPath();
						sysFile.setCoverStatus(MBoolean.YES.val);
						sysFile.setPdfPath(pdfPath);
						sysFileService.update(sysFile);
					}else if(result.getFileNotFind()){
						//源文件不存在将转换状态设置成FAIL
						sysFile.setCoverStatus(SysFile.FAIL);
						sysFileService.update(sysFile);
					}

				}
			}
			FileUtil.downLoad(file,response);

		}catch (Exception e){
			log.error("------FileOperator.downFile is error :"+e.getMessage());
		}
	}

	@Override
	public int delFile(String path) {
		String fullPath = SysFileUtil.getUploadPath()+ path;
		File file=new File(fullPath);
		file.delete();
		return 0;
	}





	@Override
	public FileModel createFile(String fileName, byte[] bytes) {
		String extName=FileUtil.getFileExt(fileName);

		String	relFilePath=getRealPath();

		
		String fullPath = SysFileUtil.getUploadPath()  + relFilePath;
		
		File dirFile = new File(fullPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		relFilePath+="/" +fileName;
		String filePath=fullPath + "/" +fileName;
		try {
			FileUtil.writeByte(filePath, bytes);
		} catch (Exception e) {
			log.error("------FileOperator.createFile is error :"+e.getMessage());
		}
		
		FileModel model=new FileModel();
		model.setFileName(fileName);
		model.setExtName(extName);
		model.setRelPath(relFilePath);
		return model;
	}



	@Override
	public ConverResult convertPdf(SysFile file) {

		String pdfPath=SysFileUtil.replaceExt(file.getPath() ,"pdf");

		String uploadPath=SysFileUtil.getUploadPath();

		String filePath= uploadPath + file.getPath();

		String fullPdfPath=uploadPath +pdfPath;

		JSONObject jsonObject = OpenOfficeUtil.coverFromOffice2Pdf(filePath, fullPdfPath );
		ConverResult result = new ConverResult();
		result.setSuccess(jsonObject.getBoolean("success"));
		result.setFileNotFind(jsonObject.getBoolean("fileNotFind"));
		result.setReason(jsonObject.getString("reason"));
		if(jsonObject.getBoolean("success")){
			result.setPath(pdfPath);
		}

		return result;
	}

}
