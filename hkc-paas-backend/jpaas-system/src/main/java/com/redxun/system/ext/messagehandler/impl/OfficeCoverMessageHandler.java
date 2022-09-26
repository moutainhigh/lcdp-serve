package com.redxun.system.ext.messagehandler.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.ext.messagehandler.IMessageHandler;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.operator.FileOperatorFactory;
import com.redxun.system.operator.IFileOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ray
 */
@Component
public class OfficeCoverMessageHandler implements IMessageHandler {

    @Autowired
    FileOperatorFactory fileOperatorFactory;
    @Autowired
    SysFileServiceImpl sysFileService;

    /**
     * file: 将文件直接转换成PDF文件
     * fastdfs: 将文件内容写入到 临时文件。 将临时文件转换成PDF ,将文件进行上传，删除临时文件，删除临时PDF文件
     * aliyun : 将文件写入到临时文件，将临时文件转换成PDF ,将文件进行上传，删除临时文件，删除临时PDF文件
     * @param messageObj
     */

    @Override
    public void handOpenOfficeFile(Object messageObj) {
        String fileStr = (String)messageObj;
        SysFile sysFile=JSONObject.parseObject(fileStr,SysFile.class);
        IFileOperator fileOperator=fileOperatorFactory.getByType(sysFile.getFileSystem());

        ConverResult result = fileOperator.convertPdf(sysFile);
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
