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
@ClassDefine(title = "OFFICE????????????",alias = "sysOfficeController",path = "/system/core/sysOffice",packages = "core",packageName = "????????????")
@Api(tags = "OFFICE????????????")
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

    @MethodDefine(title = "??????OFFICE??????", path = "/saveVerFile", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @AuditLog(operation = "??????OFFICE??????")
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
            LogContext.addError("---SysOfficeController.saveFile is error : accessToken ??????");

            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String account = loginUser.getAccount();

        String officeId = RequestUtil.getString(request, "officeId", "");
        // ?????????????????????
        String type = RequestUtil.getString(request, "type", "docx");
        String name = RequestUtil.getString(request, "name", "");
        boolean ver = RequestUtil.getBoolean(request, "ver", true);

        //????????????
        // 1.????????????
        // ?????? SYS_OFFICE, ??????SYS_FILE, ??????SYS_OFFICE_VAR
        // 2.????????? ?????????

        // ???????????????
        // 1.????????????
        // 	1.??????SYS_OFFICE , 2?????? SYS_FILE 3. ??????SYS_OFFICE_VAR
        // 2.?????????
        //  1.??????SYS_OFFICE??? ?????? SYS_FILE
        boolean isAdd = StringUtil.isEmpty(officeId);

        SysFile file = saveFile(account, officeId, ver, request);

        if (isAdd) {
            sb.append("??????OFFICE??????:");
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
            sb.append("??????OFFICE??????:");
            SysOffice sysOffice = sysOfficeService.get(officeId);
            if (SysOffice.SUPPORT_VER.equals(sysOffice.getSupportVersion())) {
                //sys_office
                Integer maxVersion = sysOfficeService.getVersionByOfficeId(officeId) + 1;
                sysOffice.setVersion(maxVersion);
                sysOffice.setName(name);

                sb.append(name +"("+officeId+")");

                sysOfficeService.update(sysOffice);
                //??????
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
        jsonResult.setMessage("????????????!");
        jsonResult.setData(officeId + "," + type);
        response.getWriter().print(jsonResult.toString());
    }

    /**
     * ???????????????
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
        // ?????????
        String fullPath = SysFileUtil.getUploadPath() + relFilePath;
        String path = fullPath.substring(0, fullPath.lastIndexOf("/"));
        FileUtil.createFolder(path, false);

        Long fileSize = multipartFile.getSize();

        InputStream is = multipartFile.getInputStream();
        //????????????
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

    @MethodDefine(title = "????????????ID??????Office??????", path = "/openOfficeById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("????????????ID??????Office??????")
    @GetMapping("/openOfficeById")
    public void openOfficeById(HttpServletRequest request) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JsonResult<String> jsonResult = new JsonResult<String>();
        String accessToken = RequestUtil.getString(request, "accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            log.error("---SysOfficeController.saveFile is error : accessToken is null");
            jsonResult.setSuccess(false);
            jsonResult.setMessage("????????????token");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
        if (BeanUtil.isEmpty(loginUser)) {
            log.error("---SysOfficeController.saveFile is error : accessToken ??????");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        if(StringUtils.isEmpty(officeId)){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("??????????????????ID");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String fileId=sysOfficeVerService.getFileIdByOfficeId(officeId);
        if(StringUtils.isEmpty(fileId)){
            jsonResult.setSuccess(false);
            jsonResult.setMessage("??????????????????!");
            response.getWriter().print(jsonResult.toString());
            return;
        }
        download(response, fileId);
    }

    @MethodDefine(title = "????????????ID??????Office??????", path = "/downloadById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("????????????ID??????Office??????")
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
            log.error("---SysOfficeController.saveFile is error : accessToken ??????");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        String fileId=sysOfficeVerService.getFileIdByOfficeId(officeId);
        download(response,fileId);
    }

    @MethodDefine(title = "????????????ID??????Office??????", path = "/downloadOfficeById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("????????????ID??????Office??????")
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
            log.error("---SysOfficeController.saveFile is error : accessToken ??????");
            jsonResult.setSuccess(false);
            response.getWriter().print(jsonResult.toString());
            return;
        }
        String officeId = RequestUtil.getString(request, "officeId");
        download(response, officeId);
    }

    /**
     * ????????????Id????????????
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
     * ????????????????????????????????????
     * @param account
     * @param fileId
     * @param type
     * @return
     */
    private String getRelFilePath(String account, String fileId, String type) {
        String tempPath = SysOfficeUtil.formatDate(new Date(), "yyyyMM");
        // ?????????????????????
        return account + "/" + tempPath + "/" + fileId + "." + type;
    }
}
