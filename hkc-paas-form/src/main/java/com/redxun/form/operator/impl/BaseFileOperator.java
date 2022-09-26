package com.redxun.form.operator.impl;

import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.operator.IFileOperator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

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
            relFilePath = tempPath = DateUtils.parseDateToStr("yyyyMMdd", new Date());
        }
        return relFilePath;
    }

    protected void addHeader(String fileName , HttpServletResponse response,boolean attachment) throws UnsupportedEncodingException {
        fileName = URLEncoder.encode(fileName,"UTF-8");

        String type=attachment?"attachment":"inline";
        response.setHeader("Content-Disposition",  type+";filename=" +fileName);
    }


}
