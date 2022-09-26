package com.redxun.system.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import com.redxun.utils.OtherConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * fast dfs操作。
 *
 * @author zhangyg
 */
@Slf4j
@Component("aliyunOss")
public class AliyunOssOperator extends BaseFileOperator{

    @Override
    public String getTitle() {
        return "阿里云Oss文件系统";
    }

    @Override
    public String getType() {
        return "aliyunOss";
    }

    @Override
    public void downFile(HttpServletResponse response, SysFile sysFile, boolean transPdf, boolean isScale,boolean isDownload){
        try {
            String config = OtherConfigUtils.getFileContent(SysFileUtil.FILE_CONFIG);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            String bucketName = sysFileStore.getString("bucketName");

            // 创建OSSClient实例。
            OSS ossClient = buildOssClient(sysFileStore);

            String fullPath =SysFileUtil.getFilePath(sysFile,isScale,transPdf);

            String fileName=SysFileUtil.getDownloadName(sysFile,transPdf);

            String ext=new MimetypesFileTypeMap().getContentType(fileName);
            response.setContentType(ext);

            addHeader(fileName,response,isDownload);

            if(transPdf){
                response.setContentType(CONTENT_TYPE_PDF);
            }

            // 获取阿里云OSS文件file对象
            OSSObject object = ossClient.getObject(bucketName, fullPath);

            FileUtil.downLoad(object.getObjectContent(), response);

            // 关闭OSSClient。
            ossClient.shutdown();

        }catch (Exception e){
            log.error("------AliyunOssOperator.downFile is error :"+e.getMessage());
        }
    }

    /**
     * 构造阿里云客户端。
     * @param sysFileStore
     * @return
     * @throws NacosException
     */
    private OSS buildOssClient(JSONObject sysFileStore) throws NacosException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = sysFileStore.getString("endpoint");
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = sysFileStore.getString("accessKeyId");
        String accessKeySecret = sysFileStore.getString("accessKeySecret");

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        return  ossClient;
    }


    @Override
    public int delFile(String path) {

        try{
            String config = OtherConfigUtils.getFileContent(SysFileUtil.FILE_CONFIG);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            String bucketName = sysFileStore.getString("bucketName");

            // 创建OSSClient实例。
            OSS ossClient = buildOssClient(sysFileStore);

            ossClient.deleteObject(bucketName,path);

            ossClient.shutdown();
        }
        catch (Exception ex){
            log.error("------AliyunOssOperator.delFile is error :"+ ExceptionUtil.getExceptionMessage(ex));
        }

        return 0;
    }


    @Override
    public FileModel createFile(String fileName, byte[] bytes) {
        String extName = FileUtil.getFileExt(fileName);

        String relFilePath=getRealPath();

        try {
            String config = OtherConfigUtils.getFileContent(SysFileUtil.FILE_CONFIG);
            JSONObject sysFileStore = JSONObject.parseObject(config);


            String fullPath = relFilePath+"/" +fileName;
            String uploadPath = sysFileStore.getString(SysFileUtil.UPLOAD_PATH);
            if(StringUtils.isNotEmpty(uploadPath)){
                fullPath=uploadPath+fullPath;
            }
            relFilePath+="/" +fileName;


            // 创建OSSClient实例。
            OSS ossClient = buildOssClient(sysFileStore);

            String bucketName = sysFileStore.getString("bucketName");
            // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, fullPath, new ByteArrayInputStream(bytes));
            // 关闭OSSClient。
            ossClient.shutdown();
        } catch (Exception e) {
            log.error("------AliyunOssOperator.createFile is error :"+e.getMessage());
        }

        FileModel model = new FileModel();
        model.setFileName(fileName);
        model.setExtName(extName);
        model.setRelPath(relFilePath);
        return model;
    }


    @Override
    public ConverResult convertPdf(SysFile file) {
        ConverResult result = new ConverResult();
        try {
            String config = OtherConfigUtils.getFileContent(SysFileUtil.FILE_CONFIG);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            String bucketName = sysFileStore.getString("bucketName");

            // 创建OSSClient实例。
            OSS ossClient = buildOssClient(sysFileStore);

            String fullPath =SysFileUtil.getFilePath(file,false,false);
            // 获取阿里云OSS文件file对象
            OSSObject object = ossClient.getObject(bucketName, fullPath);
            byte[] bytes =  FileUtil.input2byte(object.getObjectContent());

            String tmpId= UUID.randomUUID().toString();
            String pdfTmpPath=SysFileUtil.getConfigKey(PDF_TEMP_PATH);

            pdfTmpPath=pdfTmpPath.replaceAll("\\\\","/");

            if(!pdfTmpPath.endsWith("/")){
                pdfTmpPath=pdfTmpPath +"/";
            }

            String filePath= pdfTmpPath + tmpId + "." + file.getExt();
            String pdfPath= pdfTmpPath + tmpId +".pdf";

            FileUtil.writeByte(filePath, bytes);

            JSONObject jsonObject = OpenOfficeUtil.coverFromOffice2Pdf(filePath, pdfPath);

            FileModel model= createFile(tmpId +".pdf", FileUtil.readByte(pdfPath));

            FileUtil.deleteFile(filePath);
            FileUtil.deleteFile(pdfPath);

            // 关闭OSSClient。
            ossClient.shutdown();

            result.setSuccess(jsonObject.getBoolean("success"));
            if(jsonObject.getBoolean("success")){
                result.setPath( model.getRelPath());
                return result;
            }else{
                return  result;
            }


        }catch (OSSException e){
            log.error("------AliyunOssOperator.convertPdf is error :"+e.getMessage());
            result.setSuccess(false);
            result.setFileNotFind(false);
            result.setReason(e.getErrorMessage());
            //源文件不存在
            if("NoSuchKey".equals(e.getErrorCode())){
                result.setFileNotFind(true);
                result.setReason("找不到源文件");
            }
            return result;
        }catch (Exception e){
            log.error("------AliyunOssOperator.convertPdf is error :"+e.getMessage());
            result.setSuccess(false);
            result.setFileNotFind(false);
            result.setReason("转换异常");

            return result;
        }


    }



}
