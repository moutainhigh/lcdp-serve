
/**
 * <pre>
 *
 * 描述：平台开发应用管理员实体类定义
 * 表:sys_app_manager
 * 作者：Elwin ZHANG
 * 邮箱: elwin.zhang@qq.com
 * 日期:2022-02-17 11:50:53
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_app_manager")
public class SysAppManager extends BaseExtEntity<String> {

    @JsonCreator
    public SysAppManager() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;

    //授权类型：1开发权限；2管理权限
    @TableField(value = "AUTH_TYPE_",jdbcType=JdbcType.SMALLINT)
    private Integer authType;

    //是否用户组：0为用户，1为用户组
    @TableField(value = "IS_GROUP_",jdbcType=JdbcType.SMALLINT)
    private Integer isGroup;

    //用户或组ID
    @TableField(value = "USER_OR_GROUP_ID_",jdbcType=JdbcType.VARCHAR)
    private String userOrGroupId;

    //用户或组名
    @TableField(value = "USER_OR_GROUP_NAME_",jdbcType=JdbcType.VARCHAR)
    private String userOrGroupName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



