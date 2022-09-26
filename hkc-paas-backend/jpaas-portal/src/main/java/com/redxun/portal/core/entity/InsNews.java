
/**
 * <pre>
 *
 * 描述：信息公告实体类定义
 * 表:INS_NEWS
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-06 11:26:06
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.portal.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 信息公告
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "INS_NEWS")
public class InsNews  extends BaseExtEntity<java.lang.String> {
    /**
     * 发布状态=Issued
     */
    public static final String STATUS_ISSUED="Issued";
    @JsonCreator
    public InsNews() {
    }

    //NEW_ID_
    @TableId(value = "NEW_ID_",type = IdType.INPUT)
	private String newId;

    //标题
    @FieldDef(comment = "标题")
    @TableField(value = "SUBJECT_")
    private String subject;
    //关键字
    @TableField(value = "KEYWORDS_")
    private String keywords;
    //内容
    @TableField(value = "CONTENT_")
    private String content;
    //栏目id
    @TableField(value = "SYS_DIC_NEW_")
    private String sysDicNew;

    @TableField(exist = false)
    private String sysDicName;

    //图片文件ID
    @TableField(value = "IMG_FILE_ID_")
    private String imgFileId;
    //阅读次数
    @TableField(value = "READ_TIMES_")
    private Integer readTimes;
    //作者
    @TableField(value = "AUTHOR_")
    private String author;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //附件
    @TableField(value = "FILES_")
    private String files;

    @Override
    public String getPkId() {
        return newId;
    }

    @Override
    public void setPkId(String pkId) {
        this.newId=pkId;
    }
}



