
/**
 * <pre>
 *
 * 描述：应用接口授权管理表实体类定义
 * 表:SYS_AUTH_MANAGER
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-28 10:50:24
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_AUTH_MANAGER")
public class SysAuthManager extends BaseExtEntity<String> {

    @JsonCreator
    public SysAuthManager() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @FieldDef(comment = "客户名称")
    @TableField(value = "NAME_")
    private String name;
    //密钥
    @TableField(value = "SECRET_")
    private String secret;
    //是否记录日志
    @TableField(value = "IS_LOG_")
    private String isLog;
    //是否启用
    @TableField(value = "ENABLE_")
    private String enable;
    //过期时间
    @TableField(value = "EXPIRE_")
    private Integer expire=1800;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



