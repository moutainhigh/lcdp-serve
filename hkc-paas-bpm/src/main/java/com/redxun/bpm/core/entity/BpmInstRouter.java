
/**
 * <pre>
 *
 * 描述：流程实例路由实体类定义
 * 表:bpm_inst_router
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-08-27 09:21:57
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_inst_router")
public class BpmInstRouter implements BaseEntity<String> {

    @JsonCreator
    public BpmInstRouter() {
    }

    //流程实例ID
    @TableId(value = "INST_ID_",type = IdType.INPUT)
	private String instId;

    //表ID
    @TableField(value = "TABLE_ID_")
    private Integer tableId;

    @Override
    public String getPkId() {
        return instId;
    }

    @Override
    public void setPkId(String pkId) {
        this.instId=pkId;
    }
}



