package com.redxun.system.core.controller;

import com.github.pagehelper.util.StringUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.entity.SysOffice;
import com.redxun.system.core.entity.SysOfficeVer;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.core.service.SysOfficeServiceImpl;
import com.redxun.system.core.service.SysOfficeVerServiceImpl;
import com.redxun.system.util.SysFileUtil;
import com.redxun.system.util.SysOfficeUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/system/core/sysOffice")
@ClassDefine(title = "OFFICE文件管理",alias = "sysOfficeController",path = "/system/core/sysOffice",packages = "core",packageName = "系统管理")
@Api(tags = "OFFICE文件管理")
public class SysOfficeController extends BaseController<SysOffice> {

    @Autowired
    SysOfficeServiceImpl sysOfficeService;
    @Autowired
    SysFileServiceImpl sysFileService;
    @Autowired
    SysOfficeVerServiceImpl sysOfficeVerService;

    @Override
    public BaseService getBaseService() {
        return sysOfficeService;
    }

    @Override
    public String getComment() {
        return "SYS_OFFICE";
    }

    @MethodDefine(title = "保存OFFICE文件", path = "/saveVerFile", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @AuditLog(operation = "保存OFFICE文件")
    @RequestMapping("/saveVerFile")
    public void saveVerFile(MultipartHttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JsonResult<String> jsonResult = new JsonResult<String>();
        StringBuilder sb=new StringBuilder();

        String accessToken = RequestUtil.getString(request, "accessToken");
        if (StringUtils.isEmpty(accessToken)) {

            LogContext.addError("---SysOfficeController.saveFile is error : accessToken is null");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
        if (BeanUtil.isEmpty(loginUser)) {
            LogContext.addError("---SysOfficeController.saveFile is error : accessToken 失效");

            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String account = loginUser.getAccount();

        String officeId = RequestUtil.getString(request, "officeId", "");
        // 上传的文件类型
        String type = RequestUtil.getString(request, "type", "docx");
        String name = RequestUtil.getString(request, "name", "");
        boolean ver = RequestUtil.getBoolean(request, "ver", true);

        //新增情况
        // 1.支持版本
        // 添加 SYS_OFFICE, 插入SYS_FILE, 插入SYS_OFFICE_VAR
        // 2.不支持 一致。

        // 保存的情况
        // 1.支持版本
        // 	1.更新SYS_OFFICE , 2插入 SYS_FILE 3. 插入SYS_OFFICE_VAR
        // 2.不支持
        //  1.更新SYS_OFFICE， 更新 SYS_FILE
        boolean isAdd = StringUtil.isEmpty(officeId);

        SysFile file = saveFile(account, officeId, ver, request);

        if (isAdd) {
            sb.append("新增OFFICE文档:");
            //sysoffice
            SysOffice sysOffice = new SysOffice();
            officeId = IdGenerator.getIdStr();
            sysOffice.setId(officeId);
            sysOffice.setName(name);

            sb.append(name +"("+officeId+")");

            sysOffice.setSupportVersion(ver ? SysOffice.SUPPORT_VER : SysOffice.NOT_SUPPORT_VER);
            sysOffice.setVersion(1);
            sysOfficeService.insert(sysOffice);
            //sysofficever
            SysOfficeVer officeVer = new SysOfficeVer();
            officeVer.setId(IdGenerator.getIdStr());
            officeVer.setFileId(file.getFileId());
            officeVer.setOfficeId(officeId);
            officeVer.setVersion(1);
            officeVer.setFileName(file.getFileName());
            sysOfficeVerService.insert(officeVer);
        } else {
            sb.append("更新OFFICE文档:");
            SysOffice sysOffice = sysOfficeService.get(officeId);
            if (SysOffice.SUPPORT_VER.equals(sysOffice.getSupportVersion())) {
                //sys_office
                Integer maxVersion = sysOfficeService.getVersionByOfficeId(officeId) + 1;
                sysOffice.setVersion(maxVersion);
                sysOffice.setName(name);

                sb.append(name +"("+officeId+")");

                sysOfficeService.update(sysOffice);
                //版本
                SysOfficeVer officeVer = new SysOfficeVer();
                officeVer.setId(IdGenerator.getIdStr());
                officeVer.setFileId(file.getFileId());
                officeVer.setOfficeId(officeId);
                officeVer.setVersion(maxVersion);
                officeVer.setFileName(file.getFileName());
                sysOfficeVerService.insert(officeVer);
            } else {
                sysOfficeService.update(sysOffice);
            }
        }

        LogContext.put(Audit.DETAIL,sb.toString());

        jsonResult.setSuccess(true);
        jsonResult.setMessage("成功上传!");
        jsonResult.setData(officeId + "," + type);
        response.getWriter().print(jsonResult.toString());
    }

    /**
     * 写入文件。
     *
     * @param officeId
     * @param version
     * @param request
     * @return
     * @throws IOException
     */
    private SysFile saveFile(String account, String officeId, boolean version, MultipartHttpServletRequest request) throws IOException {
        String type = RequestUtil.getString(request, "type", "docx");
        Map<String, MultipartFile> files = request.getFileMap();
        Iterator<MultipartFile> it = files.values().iterator();

        boolean isAdd = false;
        if (version || StringUtil.isEmpty(officeId)) {
            isAdd = true;
        }
        SysFile file = null;
        String relFilePath = "";
        MultipartFile multipartFile = null;
        if (it.hasNext()) {
            multipartFile = it.next();
        }
        String fileId = "";
        if (!isAdd) {
            fileId = sysOfficeVerService.getFileIdByOfficeId(officeId);
            file = sysFileService.get(fileId);
            relFilePath = file.getPath();
        } else {
            fileId = IdGenerator.getIdStr();
            relFilePath = getRelFilePath(account, fileId, type);
        }
        // 全路径
        String fullPath = SysFileUtil.getUploadPath() + relFilePath;
        String path = fullPath.substring(0, fullPath.lastIndexOf("/"));
        FileUtil.createFolder(path, false);

        Long fileSize = multipartFile.getSize();

        InputStream is = multipartFile.getInputStream();
        //写入文件
        FileUtil.writeFile(fullPath, is);
        if (isAdd) {
            file = new SysFile();
            file.setFileId(fileId);
            String newFileName = fileId + "." + type;
            file.setFileName(newFileName);
            file.setNewFname(newFileName);
            file.setPath(relFilePath);
            file.setExt(type);
            file.setDesc("");
            file.setDelStatus("undeleted");
            file.setFileSystem(SysPropertiesUtil.getString("fileSystem"));
        }
        file.setTotalBytes(fileSize.intValue());
        if (isAdd) {
            sysFileService.insert(file);
        } else {
            sysFileService.update(file);
        }
        return file;
    }

    @MethodDefine(title = "根据文件ID打开Office文件", path = "/openOfficeById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("根据文件ID打开Office文件")
    @GetMapping("/openOfficeById")
    public void openOfficeById(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JsonResult<String> jsonResult = new JsonResult<String>();
        String accessToken = RequestUtil.getString(request, "accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            log.error("---SysOfficeController.saveFile is error : accessToken is null");
            jsonResult.setSuccess(false);
            jsonResult.setMessage("没有传入token");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
        if (BeanUtil.isEmpty(loginUser)) {
            log.error("---SysOfficeController.saveFile is error : accessToken 失效");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        if(StringUtils.isEmpty(officeId)){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("没有传入文档ID");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String fileId=sysOfficeVerService.getFileIdByOfficeId(officeId);
        if(StringUtils.isEmpty(fileId)){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("没有找到文档!");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        download(response, fileId);
    }

    @MethodDefine(title = "根据文件ID下载Office文件", path = "/downloadById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("根据文件ID下载Office文件")
    @GetMapping("/downloadById")
    public void downloadById(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JsonResult<String> jsonResult = new JsonResult<String>();
        String accessToken = RequestUtil.getString(request, "accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            log.error("---SysOfficeController.saveFile is error : accessToken is null");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
        if (BeanUtil.isEmpty(loginUser)) {
            log.error("---SysOfficeController.saveFile is error : accessToken 失效");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        String fileId=sysOfficeVerService.getFileIdByOfficeId(officeId);
        download(response,fileId);
    }

    @MethodDefine(title = "根据文件ID下载Office文件", path = "/downloadOfficeById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("根据文件ID下载Office文件")
    @GetMapping("/downloadOfficeById")
    public void downloadOfficeById(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JsonResult<String> jsonResult = new JsonResult<String>();
        String accessToken = RequestUtil.getString(request, "accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            log.error("---SysOfficeController.saveFile is error : accessToken is null");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
        if (BeanUtil.isEmpty(loginUser)) {
            log.error("---SysOfficeController.saveFile is error : accessToken 失效");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        download(response, officeId);
    }

    /**
     * 根据文件Id下载文件
     * @param response
     * @param fileId
     * @throws Exception
     */
    private void download(HttpServletResponse response,String fileId)throws Exception{
        SysFile sysFile=sysFileService.get(fileId);
        String fullPath =SysFileUtil.getUploadPath() + sysFile.getPath();
        File file = new File(fullPath);
        String fileName= URLEncoder.encode(sysFile.getFileName(), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" +fileName);
        FileUtil.downLoad(file,response);
    }

    /**
     * 获取文件的相对的存储路径
     * @param account
     * @param fileId
     * @param type
     * @return
     */
    private String getRelFilePath(String account, String fileId, String type) {
        String tempPath = SysOfficeUtil.formatDate(new Date(), "yyyyMM");
        // 上传的相对路径
        return account + "/" + tempPath + "/" + fileId + "." + type;
    }
}
