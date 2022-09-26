
/**
 * <pre>
 *
 * 描述：act_re_procdef实体类定义
 * 表:act_re_procdef
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
@TableName(value = "act_re_procdef")
public class ActReProcdef    implements BaseEntity<String> {

    @JsonCreator
    public ActReProcdef() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //REV_
    @TableField(value = "REV_")
    private Integer rev;
    //CATEGORY_
    @TableField(value = "CATEGORY_")
    private String category;
    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //KEY_
    @TableField(value = "KEY_")
    private String key;
    //VERSION_
    @TableField(value = "VERSION_")
    private Integer version;
    //DEPLOYMENT_ID_
    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;
    //RESOURCE_NAME_
    @TableField(value = "RESOURCE_NAME_")
    private String resourceName;
    //DGRM_RESOURCE_NAME_
    @TableField(value = "DGRM_RESOURCE_NAME_")
    private String dgrmResourceName;
    //DESCRIPTION_
    @TableField(value = "DESCRIPTION_")
    private String description;
    //HAS_START_FORM_KEY_
    @TableField(value = "HAS_START_FORM_KEY_")
    private Short hasStartFormKey;
    //HAS_GRAPHICAL_NOTATION_
    @TableField(value = "HAS_GRAPHICAL_NOTATION_")
    private Short hasGraphicalNotation;
    //SUSPENSION_STATE_
    @TableField(value = "SUSPENSION_STATE_")
    private Integer suspensionState;
    //ENGINE_VERSION_
    @TableField(value = "ENGINE_VERSION_")
    private String engineVersion;
    //APP_VERSION_
    @TableField(value = "APP_VERSION_")
    private String appVersion;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



