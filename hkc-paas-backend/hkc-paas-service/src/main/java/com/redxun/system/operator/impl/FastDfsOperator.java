package com.redxun.system.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.utils.FileUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.util.FastClient;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

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
	public void downFile(HttpServletResponse response, SysFile sysFile, boolean transPdf, boolean isScale,boolean isDownload){
		try {
			String fileName= SysFileUtil.getDownloadName(sysFile,transPdf);
			// 创建file对象
			String path=getFilePath(sysFile,isScale,transPdf);

			byte[]	bytes=getFile(path);

            String ext=new MimetypesFileTypeMap().getContentType(fileName);
            response.setContentType(ext);

            addHeader(fileName,response,isDownload);

			if(transPdf){
				response.setContentType(CONTENT_TYPE_PDF);
			}

			response.getOutputStream().write(bytes);
		}catch (Exception e){
			log.error("------FastDfsOperator.downFile is error :"+e.getMessage());
		}
	}

	private static String getFilePath(SysFile sysFile,boolean isScale,boolean isPdf) throws Exception {

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



	@Override
	public int delFile(String path) {
		try {
			fastClient.init();
			int rtn=fastClient.delFile(path);
			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
	public FileModel createFile(String fileName, byte[] bytes) {
		String extName = FileUtil.getFileExt(fileName);
		String path="";
		try {
			fastClient.init();
			path=fastClient.uploadFile(bytes, extName);
		} catch (IOException | MyException e) {
			log.error("------FastDfsOperator.createFile is error :"+e.getMessage());
		}

		FileModel model=new FileModel();
		model.setFileName(fileName);
		model.setExtName(extName);
		model.setRelPath(path);

		return model;
	}
	
	@Override
	public String getTitle() {
		return "fast分布式文件系统";
	}

	@Override
	public String getType() {
		return "fastdfs";
	}


	@Override
	public ConverResult convertPdf(SysFile sysFile) {
		ConverResult result = new ConverResult();
		try {
			String tmpId= UUID.randomUUID().toString();
			String pdfTmpPath=SysFileUtil.getConfigKey(PDF_TEMP_PATH);

			pdfTmpPath=pdfTmpPath.replaceAll("\\\\","/");

			if(!pdfTmpPath.endsWith("/")){
				pdfTmpPath=pdfTmpPath +"/";
			}

			String filePath=  pdfTmpPath + tmpId + "." + sysFile.getExt();
			String pdfPath=  pdfTmpPath + tmpId +".pdf";

			// 创建file对象
			String path=getFilePath(sysFile,false,false);
			byte[]	bytes=getFile(path);

			if(bytes == null){
				result.setSuccess(false);
				result.setFileNotFind(true);
				result.setReason("找不到源文件");

				return result;
			}

			FileUtil.writeByte(filePath, bytes);
			JSONObject jsonObject = OpenOfficeUtil.coverFromOffice2Pdf(filePath, pdfPath);

			FileModel model= createFile(tmpId +".pdf", FileUtil.readByte(pdfPath));

			FileUtil.deleteFile(filePath);
			FileUtil.deleteFile(pdfPath);

			result.setSuccess(jsonObject.getBoolean("success"));
			if(jsonObject.getBoolean("success")){
				result.setPath(model.getRelPath());
				return result;
			}else{
				return  result;
			}


		}catch (Exception e){
			log.error("------FastDfsOperator.convertPdf is error :"+e.getMessage());
			result.setSuccess(false);
			result.setFileNotFind(false);
			result.setReason("转换异常");

			return result;
		}


	}



}
