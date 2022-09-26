
/**
 * <pre>
 *
 * 描述：act_ge_bytearray实体类定义
 * 表:act_ge_bytearray
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-23 15:59:56
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.flow.entity;

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
@TableName(value = "act_ge_bytearray")
public class ActGeBytearray   implements BaseEntity<String> {

    @JsonCreator
    public ActGeBytearray() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //REV_
    @TableField(value = "REV_")
    private Integer rev;
    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //DEPLOYMENT_ID_
    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;
    //BYTES_
    @TableField(value = "BYTES_")
    private byte[] bytes;
    //GENERATED_
    @TableField(value = "GENERATED_")
    private Short generated;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



