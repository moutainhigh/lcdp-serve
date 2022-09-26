
/**
 * <pre>
 *
 * 描述：act_re_model实体类定义
 * 表:act_re_model
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

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "act_re_model")
public class ActReModel   implements BaseEntity<String> {

    @JsonCreator
    public ActReModel() {
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
    //KEY_
    @TableField(value = "KEY_")
    private String key;
    //CATEGORY_
    @TableField(value = "CATEGORY_")
    private String category;
    //LAST_UPDATE_TIME_
    @TableField(value = "LAST_UPDATE_TIME_")
    private Date lastUpdateTime;
    //VERSION_
    @TableField(value = "VERSION_")
    private Integer version;
    //META_INFO_
    @TableField(value = "META_INFO_")
    private String metaInfo;
    //DEPLOYMENT_ID_
    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;
    //EDITOR_SOURCE_VALUE_ID_
    @TableField(value = "EDITOR_SOURCE_VALUE_ID_")
    private String editorSourceValueId;
    //EDITOR_SOURCE_EXTRA_VALUE_ID_
    @TableField(value = "EDITOR_SOURCE_EXTRA_VALUE_ID_")
    private String editorSourceExtraValueId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



