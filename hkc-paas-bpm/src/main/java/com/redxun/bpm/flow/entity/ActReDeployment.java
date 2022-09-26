
/**
 * <pre>
 *
 * 描述：act_re_deployment实体类定义
 * 表:act_re_deployment
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
@TableName(value = "act_re_deployment")
public class ActReDeployment    implements BaseEntity<String> {

    @JsonCreator
    public ActReDeployment() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //NAME_
    @TableField(value = "NAME_")
    private String name;
    //CATEGORY_
    @TableField(value = "CATEGORY_")
    private String category;
    //KEY_
    @TableField(value = "KEY_")
    private String key;
    //DEPLOY_TIME_
    @TableField(value = "DEPLOY_TIME_")
    private Date deployTime;
    //ENGINE_VERSION_
    @TableField(value = "ENGINE_VERSION_")
    private String engineVersion;
    //version_
    @TableField(value = "version_")
    private Integer version;
    //PROJECT_RELEASE_VERSION_
    @TableField(value = "PROJECT_RELEASE_VERSION_")
    private Integer projectReleaseVersion;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



