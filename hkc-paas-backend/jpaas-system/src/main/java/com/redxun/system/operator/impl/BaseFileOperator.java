package com.redxun.system.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.operator.IFileOperator;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

@Slf4j
public abstract class BaseFileOperator implements IFileOperator {

    public static String PDF_TEMP_PATH="pdfTmpPath";

    public static String CONTENT_TYPE_PDF="application/pdf";

    /**
     * 构建路径
     * 1.当上下文有用户的情况返回
     * admin/202105 这样的路径
     * 2.否则返回 20210508的路径。
     * @return
     */
    protected String getRealPath(){
        // 上传的相对路径
        String tempPath = DateUtils.parseDateToStr("yyyyMM", new Date());
        String relFilePath = tempPath;
        IUser curUser = ContextUtil.getCurrentUser();
        if (curUser != null) {
            String account = curUser.getAccount();
            relFilePath = account + "/" + tempPath;
        } else {
            relFilePath =  DateUtils.parseDateToStr("yyyyMMdd", new Date());
        }
        return relFilePath;
    }

    protected void addHeader(String fileName , HttpServletResponse response,boolean attachment) throws UnsupportedEncodingException {
        fileName = URLEncoder.encode(fileName,"UTF-8");

        String type=attachment?"attachment":"inline";
        response.setHeader("Content-Disposition",  type+";filename=" +fileName);
    }





    @Override
    public ConverResult convertPdf(SysFile sysFile) {

        String tmpId= UUID.randomUUID().toString();
        String pdfTmpPath=SysFileUtil.getConfigKey(PDF_TEMP_PATH);

        pdfTmpPath=pdfTmpPath.replaceAll("\\\\","/");

        if(!pdfTmpPath.endsWith("/")){
            pdfTmpPath=pdfTmpPath +"/";
        }

        String filePath=pdfTmpPath + tmpId + "." + sysFile.getExt();
        String pdfPath=pdfTmpPath + tmpId +".pdf";

        FileUtil.writeByte(filePath, sysFile.getFileContent());
        JSONObject jsonObject = OpenOfficeUtil.coverFromOffice2Pdf(filePath, pdfPath);

        byte[] bytes= FileUtil.readByte(pdfPath);

        FileModel model= createFile(tmpId +".pdf", bytes);

        FileUtil.deleteFile(filePath);
        FileUtil.deleteFile(pdfPath);

        ConverResult result = new ConverResult();
        result.setSuccess(jsonObject.getBoolean("success"));
        result.setFileNotFind(jsonObject.getBoolean("fileNotFind"));
        result.setPath(model.getRelPath());

        return result;

    }

    public static void main(String[] args) {
        String fileName="a.docx";

        fileName =fileName.substring(0,fileName.indexOf(".")) + ".pdf";
        System.err.println(fileName);
    }
}
