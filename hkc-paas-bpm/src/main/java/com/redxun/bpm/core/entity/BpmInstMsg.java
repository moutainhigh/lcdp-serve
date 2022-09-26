
/**
 * <pre>
 *
 * 描述：流程沟通留言板实体类定义
 * 表:bpm_inst_msg
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-03-01 17:50:22
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_inst_msg")
public class BpmInstMsg  extends BaseExtEntity<String> {

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //留言用户
    @TableField(value = "AUTHOR_")
    private String author;
    //留言用户ID
    @TableField(value = "AUTHOR_ID_")
    private String authorId;
    //留言消息
    @TableField(value = "CONTENT_")
    private String content;
    //附件ID
    @TableField(value = "FILE_ID_")
    private String fileId;
    @JsonCreator
    public BpmInstMsg() {
    }

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



