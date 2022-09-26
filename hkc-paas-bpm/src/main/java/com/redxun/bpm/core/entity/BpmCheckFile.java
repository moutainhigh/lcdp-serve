
/**
 * <pre>
 *
 * 描述：审批意见附件实体类定义
 * 表:bpm_check_file
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-16 17:18:14
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
@TableName(value = "bpm_check_file")
public class BpmCheckFile  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmCheckFile() {
    }

    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;

    //FILE_ID_
    @TableField(value = "FILE_ID_")
	private String fileId;

    //FILE_NAME_
    @TableField(value = "FILE_NAME_")
    private String fileName;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //JUMP_ID_
    @TableField(value = "JUMP_ID_")
    private String jumpId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



