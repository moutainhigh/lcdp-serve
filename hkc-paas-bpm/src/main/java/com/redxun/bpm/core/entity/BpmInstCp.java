
/**
 * <pre>
 *
 * 描述：流程抄送人员实体类定义
 * 表:BPM_INST_CP
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-12 17:46:56
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
@TableName(value = "BPM_INST_CP")
public class BpmInstCp  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmInstCp() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //抄送ID
    @TableField(value = "CC_ID_")
    private String ccId;
    //流程实例ID
    @TableField(value = "INST_ID_")
    private String instId;
    //用户ID
    @TableField(value = "USER_ID_")
    private String userId;
    //是否已读
    @TableField(value = "IS_READ_")
    private String isRead;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



