package com.redxun.system.core.controller;


import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.dto.sys.SysFileDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.feign.FormBoListClient;
import com.redxun.system.feign.FormClient;
import com.redxun.system.mq.SysInputOutput;
import com.redxun.system.operator.FileOperatorFactory;
import com.redxun.system.operator.IFileOperator;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@RestController
@RequestMapping("/system/core/sysFile/")
@ClassDefine(title = "系统附件", alias = "sysFileController", path = "/system/core/sysFile", packages = "core", packageName = "系统管理")
@Api(tags = "系统附件")
public class SysFileController extends BaseController<SysFile> {

    private static Pattern iconRegex = Pattern.compile("\\.(icon-[\\w-]*?):\\s*before\\s*\\{.*?\\}", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL);

    @Autowired
    SysFileServiceImpl sysFileServiceImpl;
    @Autowired
    FileOperatorFactory fileOperatorFactory;

    @Autowired
    SysInputOutput sysInputOutput;

    @Autowired
    private ConfigService configService;

    @Autowired
    FormClient formClient;

    @Override
    public BaseService getBaseService() {
        return sysFileServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统附件";
    }

    @MethodDefine(title = "根据文件id查询文件", path = "/getByFileId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "文件ID", varName = "fileId")})
    @ApiOperation("根据文件id查询文件")
    @GetMapping("/getByFileId")
    public SysFileDto getByFileId(@ApiParam @RequestParam String fileId) {
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        if (sysFile == null) {
            return null;
        }
        SysFileDto sysFileDto = new SysFileDto();
        BeanUtil.copyProperties(sysFileDto, sysFile);
        return sysFileDto;
    }


    /**
     * feign公共上传文件接口
     *  file:"文件",
     *  uploadPath:"文件上传路，非必填",
     *  fileId:"文件主键，非必填",
     *  isConvertOffice:"是否转换pdf，非必填",
     * @return
     */
    @ApiOperation("文件上传")
    @MethodDefine(title = "文件上传", path = "/uploadFile", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "上传文件", varName = "request")})
    @AuditLog(operation = "上传文件")
    @PostMapping(value = "uploadFile" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonResult uploadFile(@RequestPart(value = "file") MultipartFile file,
                                 @RequestParam(required = false) String uploadPath,
                                 @RequestParam(required = false) String fileId,
                                 @RequestParam(required = false) boolean isConvertOffice) throws Exception {
        JsonResult jsonResult = new JsonResult();

        JPaasUser curUser = (JPaasUser) ContextUtil.getCurrentUser();

        if(StringUtils.isEmpty(fileId)){
            fileId=IdGenerator.getIdStr();
        }
        String fileSystem = SysFileUtil.getConfigKey("fileSystem");
        IFileOperator operator = fileOperatorFactory.getByType(fileSystem);

        SysFile multipartFile = new SysFile();
        multipartFile.setFileId(fileId);
        String oriFileName = file.getOriginalFilename();
        if(StringUtils.isEmpty(oriFileName) && StringUtils.isNotEmpty(uploadPath)){
            oriFileName=uploadPath;
        }
        String contentType = file.getContentType();
        String[] split = contentType.split("/");
        String type = split[0];
        String extName = FileUtil.getFileExt(oriFileName);
        // 新文件名
        String newFileName = fileId + "." + extName;

        StringBuilder sb = new StringBuilder();
        sb.append("上传文件:");

        sb.append(fileId + ",");

        InputStream is = file.getInputStream();
        byte[] bytes = FileUtil.input2byte(is);
        FileModel fileModel = operator.createFile(newFileName, bytes);
        multipartFile.setPath(fileModel.getRelPath());
        multipartFile.setFileSystem(fileSystem);
        multipartFile.setFileName(oriFileName);
        multipartFile.setTotalBytes(bytes.length);
        multipartFile.setExt(extName);
        multipartFile.setDelStatus("undeleted");
        if(curUser==null){
            multipartFile.setCreateUser("system");
        }
        else{
            multipartFile.setCreateUser(curUser.getFullName());
        }

        // 如果为图片，则生成图片的缩略图
        if ("image".equals(type) || isImg(extName)) {
            handImage(extName, bytes, operator, multipartFile, fileModel.getRelPath());
        }

        sysFileServiceImpl.save(multipartFile);

        //发送文件转pdf消息中间件处理
        if(isConvertOffice){
            SysFile temp = new SysFile();
            temp.setFileId(multipartFile.getFileId());
            temp.setPath(multipartFile.getPath());
            temp.setFileSystem(multipartFile.getFileSystem());
            temp.setExt(multipartFile.getExt());
            convertOffice(temp);
        }
        jsonResult.setData(multipartFile.getFileId());
        jsonResult.setSuccess(true);
        jsonResult.setMessage("成功上传!");
        jsonResult.setShow(false);
        return jsonResult;
    }

    @ApiOperation("文件上传")
    @MethodDefine(title = "文件上传", path = "/upload", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "上传文件", varName = "request")})
    @AuditLog(operation = "上传文件")
    @RequestMapping("upload")
    public JsonResult upload(MultipartHttpServletRequest request) throws Exception {
        JsonResult jsonResult = new JsonResult();

        JPaasUser curUser = (JPaasUser) ContextUtil.getCurrentUser();

        String timestamp = request.getParameter("time");
        Map<String, MultipartFile> multiFileMap = request.getFileMap();
        Collection<MultipartFile> files= multiFileMap.values();

        Iterator<MultipartFile> it = files.iterator();
        List<SysFile> fileList = new ArrayList<SysFile>();

        String fileSystem = SysFileUtil.getConfigKey("fileSystem");
        IFileOperator operator = fileOperatorFactory.getByType(fileSystem);

        StringBuilder sb = new StringBuilder();
        sb.append("上传文件:");

        while (it.hasNext()) {
            String fileId = IdGenerator.getIdStr();
            SysFile file = new SysFile();
            file.setFileId(fileId);
            MultipartFile multipartFile = it.next();
            String oriFileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            String[] split = contentType.split("/");
            String type = split[0];
            String extName = FileUtil.getFileExt(oriFileName);
            // 新文件名
            String newFileName = fileId + "." + extName;

            sb.append(fileId + ",");

            InputStream is = multipartFile.getInputStream();
            byte[] bytes = FileUtil.input2byte(is);
            FileModel fileModel = operator.createFile(newFileName, bytes);
            file.setPath(fileModel.getRelPath());
            file.setFileSystem(fileSystem);
            file.setFileName(oriFileName);
            file.setTotalBytes(bytes.length);
            file.setExt(extName);
            file.setDelStatus("undeleted");
            if(curUser==null){
                file.setCreateUser("system");
            }
            else{
                file.setCreateUser(curUser.getFullName());
            }



            // 如果为图片，则生成图片的缩略图
            if ("image".equals(type) || isImg(extName)) {
                handImage(extName, bytes, operator, file, fileModel.getRelPath());
            }
            sysFileServiceImpl.save(file);

            file.setTimestamp(timestamp);
            fileList.add(file);
            file.setFileContent(bytes);

            //发送文件转pdf消息中间件处理
            SysFile temp = new SysFile();
            temp.setFileId(file.getFileId());
            temp.setPath(file.getPath());
            temp.setFileSystem(file.getFileSystem());
            temp.setExt(file.getExt());
            convertOffice(temp);
        }
        jsonResult.setSuccess(true);
        for (SysFile file : fileList) {
            file.setFileContent(null);
        }
        jsonResult.setData(fileList);
        jsonResult.setMessage("成功上传!");
        jsonResult.setShow(false);

        return jsonResult;
    }

    private void handImage(String extName, byte[] bytes, IFileOperator operator, SysFile file, String relPath) {
        String fileId = IdGenerator.getIdStr();
        String imgName = fileId + "." + extName;
        int thumbnailsize = SysPropertiesUtil.getInt("thumbnailsize");
        byte[] imgBytes = ImageUtil.thumbnailImage(bytes, thumbnailsize, thumbnailsize, extName);
        if (imgBytes == null) {
            file.setThumbnail(relPath);
        } else {
            FileModel imgModel = operator.createFile(imgName, imgBytes);
            String thumbnailPath = imgModel.getRelPath();
            file.setThumbnail(thumbnailPath);
        }
    }

    /**
     * 将office文件置入转换队列
     *
     * @param file
     * @throws Exception
     */
    private void convertOffice(SysFile file) throws Exception {
        String ext = file.getExt();
        if (!SysFileUtil.isOfficeDoc(ext)) {
            return;
        }
        boolean enableOpenOffice = OpenOfficeUtil.isOpenOfficeEnabled();
        if (!enableOpenOffice) {
            return;
        }
        //用消息队列处理doc转换成pdf
        sysInputOutput.filedOutput().send(MessageBuilder.withPayload(file).build());
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    @ApiOperation("文件下载或打开")
    @GetMapping("download/{fileId}")
    public void downloadOne(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, false);
    }

    @ApiOperation("文件下载或打开")
    @GetMapping("/public/download/{fileId}")
    public void publicDownloadOne(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, false);
    }

    //打开缩放图
    @ApiOperation("打开缩放图")
    @GetMapping("downloadScale/{fileId}")
    public void downloadScale(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, true);
    }

    /**
     * 下载文件
     *
     * @param response
     * @param fileId
     * @param transPdf
     * @param isScale
     * @throws Exception
     */
    private void download(HttpServletResponse response, String fileId, boolean transPdf, boolean isScale) throws Exception {
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        String fileSystem = sysFile.getFileSystem();
        IFileOperator operator = fileOperatorFactory.getByType(fileSystem);
        operator.downFile(response, sysFile, transPdf, isScale, true);
    }

    @ApiOperation("获取附件或图片路径")
    @GetMapping("getFilePath")
    public JsonResult  getFilePath(@ApiParam(name = "系统文件ID") @RequestParam(value = "fileId") String fileId,
                               @ApiParam(name = "是否取缩略图") @RequestParam(value = "isScale") boolean isScale) throws  Exception{
        JsonResult result=JsonResult.Fail("获取出错");
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        String fileSystem = sysFile.getFileSystem();
        IFileOperator operator = fileOperatorFactory.getByType(fileSystem);
        if(!"file".equals(operator.getType())){
            return result;
        }
        String	path= SysFileUtil.getFilePath(sysFile, isScale, false);
        result.setSuccess(true);
        result.setMessage(path);
        result.setData(path);
        return  result;
    }

    /**
     * 文件预览
     *
     * @param fileId
     * @throws Exception
     */
    @ApiOperation("文件预览")
    @GetMapping("previewFile")
    public void previewFile(@RequestParam String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        // 创建file对象
        IFileOperator operator = fileOperatorFactory.getByType(sysFile.getFileSystem());

        operator.downFile(response, sysFile, false, false, false);
    }

    /**
     * 图片预览
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation("图片预览")
    @GetMapping("previewImg")
    public void previewImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileId");
        boolean isScale = RequestUtil.getBoolean(request, "isScale", false);
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        IFileOperator operator = fileOperatorFactory.getByType(sysFile.getFileSystem());
        operator.downFile(response, sysFile, false, isScale, false);
    }

    /**
     * 根据文件的后缀名判断是否为图片
     *
     * @param ext
     * @return
     */
    private boolean isImg(String ext) {
        if ("jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext)
                || "gif".equals(ext) || "bmp".equals(ext)) {
            return true;
        }
        return false;
    }


    @MethodDefine(title = "移动端图标选择对话框", path = "/mobileIconSelectDialog", method = HttpMethodConstants.GET)
    @ApiOperation("移动端图标选择对话框")
    @GetMapping("/mobileIconSelectDialog")
    public Object mobileIconSelectDialog() throws IOException {
        List<String> iconList = new ArrayList<String>();
        ClassPathResource classPathResource = new ClassPathResource("mobileIcon/iconfont.css");
        InputStream is = classPathResource.getInputStream();
        String icon = FileUtil.readFile(is);
        Matcher regexMatcher = iconRegex.matcher(icon);
        while (regexMatcher.find()) {
            iconList.add(regexMatcher.group(1));
        }
        return iconList;
    }

    @ApiOperation("图片上传")
    @MethodDefine(title = "图片上传", path = "/uploadPicture", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "图片链接", varName = "urlStr")})
    @PostMapping("uploadPicture")
    public String uploadPicture(@RequestParam(value = "urlStr") String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);

        InputStream inputStream = conn.getInputStream();
        String contentType = conn.getContentType();

        String fileSystem = SysFileUtil.getConfigKey("fileSystem");
        IFileOperator operator = fileOperatorFactory.getByType(fileSystem);
        String fileId = IdGenerator.getIdStr();
        SysFile file = new SysFile();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inputStream.close();
        byte[] bytes = outStream.toByteArray();

        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType jpeg = allTypes.forName(contentType);
        String extName = jpeg.getExtension();
        if (StringUtils.isNotEmpty(extName)) {
            extName = extName.substring(1);
        }
        //生成缩略图
        String fileName = fileId + "." + extName;
        FileModel fileModel = operator.createFile(fileName, bytes);
        if (isImg(extName)) {
            int thumbNailSize = SysPropertiesUtil.getInt("thumbnailsize");
            byte[] imgBytes = ImageUtil.thumbnailImage(bytes, thumbNailSize, thumbNailSize, extName);
            fileName = IdGenerator.getIdStr() + "." + extName;
            FileModel imgModel = operator.createFile(fileName, imgBytes);
            String thumbnailPath = imgModel.getRelPath();
            file.setThumbnail(thumbnailPath);
        }

        file.setFileId(fileId);
        file.setPath(fileModel.getRelPath());
        file.setFileSystem(fileSystem);
        file.setFileName(fileName);

        file.setExt(extName);
        file.setDelStatus("undeleted");

        sysFileServiceImpl.save(file);

        return fileId;
    }

    /**
     * 预览PDF文件
     *
     * @param fileId
     * @throws Exception
     */
    @MethodDefine(title = "预览PDF文件", path = "/previewPdf", method = HttpMethodConstants.GET)
    @ApiOperation("预览PDF文件")
    @GetMapping("previewPdf")
    public void previewPdf(@RequestParam String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setCharacterEncoding("utf-8");
        SysFile sysFile = sysFileServiceImpl.get(fileId);

        if (SysFileUtil.isOfficeDoc(sysFile.getExt())) {
            if (!sysFile.getCoverStatus().equals(MBoolean.YES.val)) {
                //文件不存在，无法转换成PDF文件
                if (sysFile.getCoverStatus().equals(SysFile.FAIL)) {
                    response.getWriter().println("文件不存在，无法转换成PDF文件!");
                    return;
                }

                //发送文件转pdf消息中间件处理
                SysFile temp = new SysFile();
                temp.setFileId(sysFile.getFileId());
                temp.setPath(sysFile.getPath());
                temp.setFileSystem(sysFile.getFileSystem());
                temp.setExt(sysFile.getExt());
                convertOffice(temp);
//
//                response.getWriter().println("PDF文件还没有转换完成，请稍后再试!");
//                return;

//                String pdfPath = sysFile.getPdfPath();
                //office转换没有启用
                boolean officeEnabled = OpenOfficeUtil.isOpenOfficeEnabled();
                if (!officeEnabled) {
                    response.getWriter().println("系统还没有启用OPENOFFICE转换,请先在系统中配置!");
                    return;
                }
            }
        }
        IFileOperator fileOperator = fileOperatorFactory.getByType(sysFile.getFileSystem());
        fileOperator.downFile(response, sysFile, true, false, false);
    }

    /**
     * 下载文件（将文件打成压缩包）
     * @param fileIdStr 逗号隔开
     * @throws Exception
     */
    @MethodDefine(title = "下载文件（将文件打成压缩包）", path = "/downloadZip", method = HttpMethodConstants.GET)
    @ApiOperation("下载文件（将文件打成压缩包）")
    @GetMapping("downloadZip/{fileIdStr}")
    public void downloadZip( @PathVariable("fileIdStr") String fileIdStr) throws Exception {
        if (StringUtils.isNotEmpty(fileIdStr)){
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            String[] fileIds = fileIdStr.split(",");
            List<File> fileList = new ArrayList<>();
            String path = SysFileUtil.getConfigKey("uploadPath");
            for (int i = 0; i < fileIds.length; i++) {
                SysFile sysFile = sysFileServiceImpl.get(fileIds[i]);
                if(StringUtils.isNotEmpty(sysFile.getPath())){
                    fileList.add(new File(path+sysFile.getPath()));
                }
            }
            String fileName= IdGenerator.getIdStr()+".zip";
            FileUtil.compressedDownload(response,fileList,path,fileName);
        }
    }

    @MethodDefine(title = "根据文件id删除文件", path = "/delByFileIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "文件ID", varName = "fileIds")})
    @ApiOperation("根据文件id删除文件")
    @GetMapping("/delByFileIds")
    public void delByFileIds(@ApiParam @RequestParam List<String> fileIds) {
        for (int i = 0; i < fileIds.size(); i++) {
            SysFile sysFile = sysFileServiceImpl.get(fileIds.get(i));
            sysFileServiceImpl.delete(sysFile.getFileId());
            String fileSystem = SysFileUtil.getConfigKey("fileSystem");
            IFileOperator operator = fileOperatorFactory.getByType(fileSystem);
            operator.delFile(sysFile.getPath());
        }
    }
}
