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
@ClassDefine(title = "????????????", alias = "sysFileController", path = "/system/core/sysFile", packages = "core", packageName = "????????????")
@Api(tags = "????????????")
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
        return "????????????";
    }

    @MethodDefine(title = "????????????id????????????", path = "/getByFileId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "fileId")})
    @ApiOperation("????????????id????????????")
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
     * feign????????????????????????
     *  file:"??????",
     *  uploadPath:"???????????????????????????",
     *  fileId:"????????????????????????",
     *  isConvertOffice:"????????????pdf????????????",
     * @return
     */
    @ApiOperation("????????????")
    @MethodDefine(title = "????????????", path = "/uploadFile", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @AuditLog(operation = "????????????")
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
        // ????????????
        String newFileName = fileId + "." + extName;

        StringBuilder sb = new StringBuilder();
        sb.append("????????????:");

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

        // ?????????????????????????????????????????????
        if ("image".equals(type) || isImg(extName)) {
            handImage(extName, bytes, operator, multipartFile, fileModel.getRelPath());
        }

        sysFileServiceImpl.save(multipartFile);

        //???????????????pdf?????????????????????
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
        jsonResult.setMessage("????????????!");
        jsonResult.setShow(false);
        return jsonResult;
    }

    @ApiOperation("????????????")
    @MethodDefine(title = "????????????", path = "/upload", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @AuditLog(operation = "????????????")
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
        sb.append("????????????:");

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
            // ????????????
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



            // ?????????????????????????????????????????????
            if ("image".equals(type) || isImg(extName)) {
                handImage(extName, bytes, operator, file, fileModel.getRelPath());
            }
            sysFileServiceImpl.save(file);

            file.setTimestamp(timestamp);
            fileList.add(file);
            file.setFileContent(bytes);

            //???????????????pdf?????????????????????
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
        jsonResult.setMessage("????????????!");
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
     * ???office????????????????????????
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
        //?????????????????????doc?????????pdf
        sysInputOutput.filedOutput().send(MessageBuilder.withPayload(file).build());
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    @ApiOperation("?????????????????????")
    @GetMapping("download/{fileId}")
    public void downloadOne(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, false);
    }

    @ApiOperation("?????????????????????")
    @GetMapping("/public/download/{fileId}")
    public void publicDownloadOne(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, false);
    }

    //???????????????
    @ApiOperation("???????????????")
    @GetMapping("downloadScale/{fileId}")
    public void downloadScale(@PathVariable("fileId") String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        download(response, fileId, false, true);
    }

    /**
     * ????????????
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

    @ApiOperation("???????????????????????????")
    @GetMapping("getFilePath")
    public JsonResult  getFilePath(@ApiParam(name = "????????????ID") @RequestParam(value = "fileId") String fileId,
                               @ApiParam(name = "??????????????????") @RequestParam(value = "isScale") boolean isScale) throws  Exception{
        JsonResult result=JsonResult.Fail("????????????");
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
     * ????????????
     *
     * @param fileId
     * @throws Exception
     */
    @ApiOperation("????????????")
    @GetMapping("previewFile")
    public void previewFile(@RequestParam String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        // ??????file??????
        IFileOperator operator = fileOperatorFactory.getByType(sysFile.getFileSystem());

        operator.downFile(response, sysFile, false, false, false);
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation("????????????")
    @GetMapping("previewImg")
    public void previewImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileId = request.getParameter("fileId");
        boolean isScale = RequestUtil.getBoolean(request, "isScale", false);
        SysFile sysFile = sysFileServiceImpl.get(fileId);
        IFileOperator operator = fileOperatorFactory.getByType(sysFile.getFileSystem());
        operator.downFile(response, sysFile, false, isScale, false);
    }

    /**
     * ?????????????????????????????????????????????
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


    @MethodDefine(title = "??????????????????????????????", path = "/mobileIconSelectDialog", method = HttpMethodConstants.GET)
    @ApiOperation("??????????????????????????????")
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

    @ApiOperation("????????????")
    @MethodDefine(title = "????????????", path = "/uploadPicture", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "urlStr")})
    @PostMapping("uploadPicture")
    public String uploadPicture(@RequestParam(value = "urlStr") String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //??????????????????3???
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
        //???????????????
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
     * ??????PDF??????
     *
     * @param fileId
     * @throws Exception
     */
    @MethodDefine(title = "??????PDF??????", path = "/previewPdf", method = HttpMethodConstants.GET)
    @ApiOperation("??????PDF??????")
    @GetMapping("previewPdf")
    public void previewPdf(@RequestParam String fileId) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setCharacterEncoding("utf-8");
        SysFile sysFile = sysFileServiceImpl.get(fileId);

        if (SysFileUtil.isOfficeDoc(sysFile.getExt())) {
            if (!sysFile.getCoverStatus().equals(MBoolean.YES.val)) {
                //?????????????????????????????????PDF??????
                if (sysFile.getCoverStatus().equals(SysFile.FAIL)) {
                    response.getWriter().println("?????????????????????????????????PDF??????!");
                    return;
                }

                //???????????????pdf?????????????????????
                SysFile temp = new SysFile();
                temp.setFileId(sysFile.getFileId());
                temp.setPath(sysFile.getPath());
                temp.setFileSystem(sysFile.getFileSystem());
                temp.setExt(sysFile.getExt());
                convertOffice(temp);
//
//                response.getWriter().println("PDF?????????????????????????????????????????????!");
//                return;

//                String pdfPath = sysFile.getPdfPath();
                //office??????????????????
                boolean officeEnabled = OpenOfficeUtil.isOpenOfficeEnabled();
                if (!officeEnabled) {
                    response.getWriter().println("?????????????????????OPENOFFICE??????,????????????????????????!");
                    return;
                }
            }
        }
        IFileOperator fileOperator = fileOperatorFactory.getByType(sysFile.getFileSystem());
        fileOperator.downFile(response, sysFile, true, false, false);
    }

    /**
     * ??????????????????????????????????????????
     * @param fileIdStr ????????????
     * @throws Exception
     */
    @MethodDefine(title = "??????????????????????????????????????????", path = "/downloadZip", method = HttpMethodConstants.GET)
    @ApiOperation("??????????????????????????????????????????")
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

    @MethodDefine(title = "????????????id????????????", path = "/delByFileIds", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "fileIds")})
    @ApiOperation("????????????id????????????")
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
