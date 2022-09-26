package com.redxun.system.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.operator.ConverResult;
import com.redxun.system.util.FileModel;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.system.util.SysFileUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * minio操作。
 *
 * @author zfh
 */
@Slf4j
@Component("minio")
public class MinioOperator extends BaseFileOperator{

    @Override
    public String getTitle() {
        return "minio文件系统";
    }

    @Override
    public String getType() {
        return "minio";
    }

    @Autowired
    private ConfigService configService;

    @Override
    public void downFile(HttpServletResponse response, SysFile sysFile, boolean transPdf, boolean isScale,boolean isDownload){
        try {
            String config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
            JSONObject sysFileStore = JSONObject.parseObject(config);
            String bucketName = sysFileStore.getString("bucketName");

            String fileName=SysFileUtil.getDownloadName(sysFile,transPdf);
            String ext=new MimetypesFileTypeMap().getContentType(fileName);
            response.setContentType(ext);
            addHeader(fileName,response,isDownload);

            if(transPdf){
                response.setContentType(CONTENT_TYPE_PDF);
            }

            // 创建minioClient实例。
            MinioClient minioClient = buildMinioClient(sysFileStore);
            //sysFile.getPath()：存储桶里的对象名称
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(sysFile.getPath()).build();
            // 获取minio文件inputStream对象
            InputStream inputStream = minioClient.getObject(objectArgs);

            FileUtil.downLoad(inputStream, response);


        }catch (Exception e){
            log.error("------MinioOperator.downFile is error :"+e.getMessage());
        }
    }

    /**
     * 构造minio客户端。
     * @param sysFileStore
     * @return
     */
    private MinioClient buildMinioClient(JSONObject sysFileStore){
        //连接地址
        String endpoint = sysFileStore.getString("endpoint");
        //账号
        String accessKey = sysFileStore.getString("accessKey");
        //密码
        String secretKey = sysFileStore.getString("secretKey");

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();


        return  minioClient;
    }


    @Override
    public int delFile(String path) {

        try{
            String config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            //存储桶名称
            String bucketName = sysFileStore.getString("bucketName");

            // 创建OSSClient实例。
            MinioClient minioClient = buildMinioClient(sysFileStore);

            //path：存储桶里的对象名称
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());

        }
        catch (Exception ex){
            log.error("------MinioOperator.delFile is error :"+ ExceptionUtil.getExceptionMessage(ex));
        }

        return 0;
    }


    @Override
    public FileModel createFile(String fileName, byte[] bytes) {
        try {
            String config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
            JSONObject sysFileStore = JSONObject.parseObject(config);
            String bucketName = sysFileStore.getString("bucketName");

            // 创建MinioClient实例
            MinioClient minioClient = buildMinioClient(sysFileStore);

            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean flag = minioClient.bucketExists(bucketExistsArgs);
            if (!flag) {
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }

            //fileName：存储桶里的对象名称
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(bucketName).object(fileName)
                    .stream(new ByteArrayInputStream(bytes), bytes.length, -1).build();

            // 上传到minio
            minioClient.putObject(putObjectArgs);

        } catch (Exception e) {
            log.error("------MinioOperator.createFile is error :"+e.getMessage());
        }

        FileModel model = new FileModel();
        model.setFileName(fileName);
        model.setExtName(FileUtil.getFileExt(fileName));
        //存储桶里的对象名称
        model.setRelPath(fileName);
        return model;

    }


    @Override
    public ConverResult convertPdf(SysFile file) {
        ConverResult result = new ConverResult();
        try {
            String config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            String bucketName = sysFileStore.getString("bucketName");

            // 创建minioClient实例。
            MinioClient minioClient = buildMinioClient(sysFileStore);
            //file.getPath()：存储桶里的对象名称
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(file.getPath()).build();
            // 获取minio文件inputStream对象
            InputStream inputStream = minioClient.getObject(objectArgs);

            byte[] bytes =  FileUtil.input2byte(inputStream);

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

            result.setSuccess(jsonObject.getBoolean("success"));
            if(jsonObject.getBoolean("success")){
                result.setPath( model.getRelPath());
                return result;
            }else{
                return  result;
            }


        }catch (Exception e){
            log.error("------MinioOperator.convertPdf is error :"+e.getMessage());
            result.setSuccess(false);
            result.setFileNotFind(false);
            result.setReason("转换异常");

            return result;
        }


    }



}
