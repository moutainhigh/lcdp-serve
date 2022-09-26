
/**
 * <pre>
 *
 * 描述：系统附件实体类定义
 * 表:sys_file
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-13 17:45:21
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_file")
public class SysFile  extends BaseExtEntity<String> {

    @JsonCreator
    public SysFile() {
    }

    //FILE_ID_
    @TableId(value = "FILE_ID_",type = IdType.INPUT)
	private String fileId;

    //分类ID
    @TableField(value = "TYPE_ID_")
    private String typeId;
    //文件名
    @TableField(value = "FILE_NAME_")
    private String fileName;
    //新文件名
    @TableField(value = "NEW_FNAME_")
    private String newFname;
    //文件路径
    @TableField(value = "PATH_")
    private String path;
    //图片缩略图
    @TableField(value = "THUMBNAIL_")
    private String thumbnail;
    //扩展名
    @TableField(value = "EXT_")
    private String ext;
    //附件类型
    @TableField(value = "MINE_TYPE_")
    private String mineType;
    //说明
    @TableField(value = "DESC_")
    private String desc;
    //总字节数
    @TableField(value = "TOTAL_BYTES_")
    private Integer totalBytes;
    //删除标识
    @TableField(value = "DEL_STATUS_")
    private String delStatus;

    @TableField(value = "FROM_")
    private String from;
    //生成PDF状态
    @TableField(value = "COVER_STATUS_")
    private String coverStatus= MBoolean.NO.val;
    //文件系统
    @TableField(value = "FILE_SYSTEM_")
    private String fileSystem;
    //PDF文件路径
    @TableField(value = "PDF_PATH_")
    private String pdfPath;

    @TableField(exist = false)
    private String createUser;


    @TableField(exist = false)
    private byte[] fileContent;
    @TableField(exist = false)
    private String timestamp;

    @Override
    public String getPkId() {
        return fileId;
    }

    @Override
    public void setPkId(String pkId) {
        this.fileId=pkId;
    }


    /**
     * 文件不存在，无法转换成PDF文件
     */
    public static String FAIL = "FAIL";
}



