package com.redxun.form.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.redxun.dto.sys.SysFileDto;
import com.redxun.form.util.SysFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

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

    @Autowired
    private ConfigService configService;

    @Override
    public InputStream getInputStream(SysFileDto sysFile, boolean transPdf, boolean isScale) {
        try {
            String config =configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
            JSONObject sysFileStore = JSONObject.parseObject(config);

            String bucketName = sysFileStore.getString("bucketName");

            // 创建OSSClient实例。
            OSS ossClient = buildOssClient(sysFileStore);

            String fullPath = SysFileUtil.getFilePath(sysFile,isScale,transPdf);

            // 获取阿里云OSS文件file对象
            OSSObject object = ossClient.getObject(bucketName, fullPath);
            return object.getObjectContent();
        }catch (Exception e){
            log.error("------AliyunOssOperator.getInputStream is error :"+e.getMessage());
        }
        return null;
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



}
