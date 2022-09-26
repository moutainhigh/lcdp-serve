package com.redxun.dto.sys;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统文件DTO
 */
@Data
public class SysFileDto extends BaseDto {


    //FILE_ID_
    private String fileId;

    //分类ID
    private String typeId;
    //文件名
    private String fileName;
    //新文件名
    private String newFname;
    //文件路径
    private String path;
    //图片缩略图
    private String thumbnail;
    //扩展名
    private String ext;
    //附件类型
    private String mineType;
    //说明
    private String desc;
    //总字节数
    private Long totalBytes;
    //删除标识
    private String delStatus;

    private String from;
    //生成PDF状态
    private String coverStatus;
    //文件系统
    private String fileSystem;
    //PDF文件路径
    private String pdfPath;

    private String fileUid;
    private byte[] fileContent;
    private String timestamp;

}
