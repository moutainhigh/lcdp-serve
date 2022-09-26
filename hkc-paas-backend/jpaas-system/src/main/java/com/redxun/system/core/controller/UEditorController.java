package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.ImageUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.operator.FileOperatorFactory;
import com.redxun.system.operator.IFileOperator;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.SysFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * UEditor的上传处理
 * @author mansan
 *
 */
@Slf4j
@RestController
@RequestMapping("/system/core/ueditor")
@ClassDefine(title = "UEDITOR附件",alias = "ueditorController",path = "/system/core/ueditor",packages = "core",packageName = "系统管理")
@Api(tags = "UEDITOR附件")
public class UEditorController  {
	@Autowired
	SysFileServiceImpl sysFileServive;
	@Autowired
	FileOperatorFactory fileOperatorFactory;

	@GetMapping()
	@ApiOperation("Editor首页地址")
	public String index() {
		return "/ueditor/index";
	}

	@MethodDefine(title = "上传文件", path = "/upload", method = HttpMethodConstants.GET,
			params = {@ParamDefine(title = "请求数据", varName = "request")})
	@ApiOperation("上传文件")
	@GetMapping("/upload")
	public void upload(HttpServletRequest request) throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response=attributes.getResponse();
		String action = request.getParameter("action");
		response.setCharacterEncoding("utf-8");
		if ("config".equals(action)) {
			OutputStream os = response.getOutputStream();
			InputStream is= this.getClass().getResourceAsStream("/ueditor-config.json");


			IOUtils.copy(is, os);
		}
	}

	@MethodDefine(title = "上传文件", path = "/upload", method = HttpMethodConstants.POST,
			params = {@ParamDefine(title = "请求数据", varName = "request")})
	@ApiOperation("上传文件")
	@PostMapping("/upload")
	public Map<String, String> upload(MultipartHttpServletRequest request) throws Exception {
		String action= RequestUtil.getString(request, "action");
		String genThumbnail= RequestUtil.getString(request, "genThumbnail");

		String fileSystem = SysFileUtil.getConfigKey("fileSystem");
		IFileOperator operator= fileOperatorFactory.getByType(fileSystem);
		Map<String, String> result = new HashMap<String, String>();

		SysFile sysFile = new SysFile();
		MultipartFile upfile=request.getFileMap().values().iterator().next();
		InputStream is = upfile.getInputStream();
		byte[] bytes= FileUtil.input2byte(is);

		String fileId= IdGenerator.getIdStr();
		String oriFileName = upfile.getOriginalFilename();
		String extName = FileUtil.getFileExt(oriFileName);
		// 新文件名
		String newFileName = fileId + "." + extName;
		FileModel fileModel=operator.createFile(newFileName, bytes);
		String fullPath=fileModel.getRelPath();
		sysFile.setFileId(fileId);
		sysFile.setPath(fullPath);
		sysFile.setFileSystem(fileSystem);
		sysFile.setFileName(newFileName);
		//设置新的文件名
		sysFile.setNewFname(newFileName);
		//设置其来源
		sysFile.setExt(extName);

		sysFile.setDelStatus("undeleted");
		int thumbnailsize=SysPropertiesUtil.getInt("thumbnailsize");
		// 如果为图片，则生成图片的缩略图
		if ("1".equals(genThumbnail)) {
			String imgName = fileId + "." + extName;
			byte[] imgBytes = ImageUtil.thumbnailImage(bytes, thumbnailsize, thumbnailsize, extName);
			if(imgBytes==null){
				sysFile.setThumbnail(fileModel.getRelPath());
			}else {
				FileModel imgModel=operator.createFile(imgName, imgBytes);
				sysFile.setThumbnail(imgModel.getRelPath());
			}
		}
		sysFileServive.save(sysFile);

		String host= SysPropertiesUtil.getString("serverAddress");
		result.put("url", host +  "/api/api-system/system/core/sysFile/public/download/" + sysFile.getFileId());
		result.put("size", String.valueOf(bytes.length));
		result.put("title", sysFile.getFileName());
		result.put("text", sysFile.getFileName() );
		result.put("alt", sysFile.getFileName());
		result.put("type",extName);
		result.put("state", "SUCCESS");
		return result;
	}



	public static String formatDate(Date date,String format){
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

}
