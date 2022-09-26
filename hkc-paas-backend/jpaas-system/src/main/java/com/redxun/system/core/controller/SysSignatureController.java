package com.redxun.system.core.controller;

import com.redxun.cache.CacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.entity.SysSignature;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.core.service.SysSignatureServiceImpl;
import com.redxun.system.operator.FileOperatorFactory;
import com.redxun.system.operator.IFileOperator;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.SysFileUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/system/core/sysSignature")
@ClassDefine(title = "签名实体",alias = "sysSignatureController",path = "/system/core/sysSignature",packages = "core",packageName = "系统管理")
@Api(tags = "签名实体")
public class SysSignatureController extends BaseController<SysSignature> {

    @Autowired
    SysSignatureServiceImpl sysSignatureService;

    @Autowired
    SysFileServiceImpl sysFileServiceImpl;
    @Autowired
    FileOperatorFactory fileOperatorFactory;

    @Override
    public BaseService getBaseService() {
        return sysSignatureService;
    }

    @Override
    public String getComment() {
        return "签名实体";
    }

    //获取手机扫码后的签名
    @MethodDefine(title = "获取手机扫码后的签名", path = "/getSignature", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "时间戳", varName = "timestamp")})
    @ApiOperation("获取手机扫码后的签名")
    @PostMapping("/getSignature")
    public JsonResult getSignature(@RequestParam String timestamp) throws Exception {
        Map<String, SysFile> map = null;
        SysFile sysFile = null;
        int i=1;
        while (sysFile == null&&i<=60) {
            map = (Map<String, SysFile>) CacheUtil.get("signature", "signature");
            if (map != null) {
                sysFile = map.get(timestamp);
            }
            i++;
            Thread.sleep(1000);
        }
        if (sysFile != null) {
            map = (Map<String, SysFile>) CacheUtil.get("signature", "signature");
            map.remove(timestamp);
            CacheUtil.set("signature", "signature", map);
            JsonResult jr = new JsonResult();
            jr.setSuccess(true);
            jr.setData(sysFile);
            return jr;
        }
        return new JsonResult().setShow(false).setSuccess(false);
    }

    //上传签名
    @MethodDefine(title = "上传签名", path = "/uploadSignature", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("上传签名")
    @AuditLog(operation = "上传签名")
    @RequestMapping("/uploadSignature")
    public JsonResult uploadSignature(MultipartHttpServletRequest request) throws Exception {
        String timestamp = request.getParameter("timestamp");
        String userId = request.getParameter("userId");
        boolean falg = Boolean.parseBoolean(request.getParameter("falg"));
        //手机扫码签名  没有登录
        if(StringUtils.isEmpty(userId)){
            IUser user = ContextUtil.getCurrentUser();
            userId=user.getUserId();
        }
        JsonResult jsonResult = new JsonResult();
        Map<String, MultipartFile> files = request.getFileMap();
        Iterator<MultipartFile> it = files.values().iterator();
        List<SysFile> fileList = new ArrayList<SysFile>();

        String fileSystem = SysFileUtil.getConfigKey("fileSystem");
        IFileOperator operator= fileOperatorFactory.getByType(fileSystem);
        while (it.hasNext()) {
            String fileId = IdGenerator.getIdStr();
            SysFile file = new SysFile();
            file.setFileId(fileId);
            MultipartFile multipartFile = it.next();
            String oriFileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            String[] split = contentType.split("/");
            String type = split[0];
            if (!"image".equals(type)) {
                return new JsonResult().setMessage("上传失败").setSuccess(false);
            }
            String extName = FileUtil.getFileExt(oriFileName);
            // 新文件名
            String newFileName = fileId + "." + extName;
            InputStream is = multipartFile.getInputStream();
            byte[] bytes = FileUtil.input2byte(is);
            FileModel fileModel=operator.createFile(newFileName, bytes);
            file.setPath(fileModel.getRelPath());
            file.setFileSystem(fileSystem);
            file.setFileName(oriFileName);

            file.setExt(extName);
            file.setDelStatus("undeleted");
            file.setCreateBy(userId);
            String curTenantId = ContextUtil.getCurrentTenantId();
            file.setTenantId(curTenantId);
            boolean save = sysFileServiceImpl.save(file);
            if(!save){
                return new JsonResult().setSuccess(false).setMessage("上传失败!").setShow(false);
            }
            fileList.add(file);
            Map<String, SysFile> map = (Map<String, SysFile>) CacheUtil.get("signature", "signature");
            if (map == null) {
                map = new HashMap<String, SysFile>();
            }
            if(falg){
                SysSignature signObj = new SysSignature();
                signObj.setFileId(fileId);
                signObj.setFileName(oriFileName);
                signObj.setCreateTime(new Date());
                signObj.setCreateBy(userId);
                sysSignatureService.insert(signObj);
            }
            map.put(timestamp, file);
            CacheUtil.set("signature", "signature", map);

            LogContext.put(Audit.DETAIL,"上传用户:"+ userId +",文件ID:"+ fileId);
        }
        jsonResult.setSuccess(true);
        jsonResult.setData(fileList);
        jsonResult.setShow(false);
        jsonResult.setMessage("成功上传!");
        return jsonResult;
    }

    @ApiOperation("获取手机扫码后的签名集合")
    @MethodDefine(title = "获取手机扫码后的签名集合", path = "/getSignatureList", method = HttpMethodConstants.GET)
    @GetMapping("/getSignatureList")
    public List<SysSignature> getSignatureList() {
        //获取当前userId
        String userId = ContextUtil.getCurrentUserId();
        List<SysSignature> signaturesList = sysSignatureService.getSignatureList(userId);
        return signaturesList;
    }

    @MethodDefine(title = "获取手机扫码后的签名地址", path = "/getSignatureAddress", method = HttpMethodConstants.GET)
    @ApiOperation("获取手机扫码后的签名地址")
    @GetMapping("/getSignatureAddress")
    public Map<String,String> getSignatureAddress() {
        //获取当前userId
        String userId = ContextUtil.getCurrentUserId();
        String signAddress= SysPropertiesUtil.getString("signatureAddress");
        Map<String,String> map=new HashMap<>();
        map.put("userId",userId);
        map.put("signAddress",signAddress);
        return map;
    }


}
