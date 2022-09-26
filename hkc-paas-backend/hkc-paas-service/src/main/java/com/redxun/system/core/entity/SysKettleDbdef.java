
/**
 * <pre>
 *
 * 描述：KETTLE资源库定义实体类定义
 * 表:SYS_KETTLE_DBDEF
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-06-19 21:48:25
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
@TableName(value = "SYS_KETTLE_DBDEF")
public class SysKettleDbdef  extends BaseExtEntity<String> {

    @JsonCreator
    public SysKettleDbdef() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //数据库名
    @TableField(value = "DATABASE_",jdbcType=JdbcType.VARCHAR)
    private String database;
    //数据库类型
    @TableField(value = "DB_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String dbType;
    //主机地址
    @TableField(value = "HOST_",jdbcType=JdbcType.VARCHAR)
    private String host;
    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //密码
    @TableField(value = "PASSWORD_",jdbcType=JdbcType.VARCHAR)
    private String password;
    //端口号
    @TableField(value = "PORT_",jdbcType=JdbcType.VARCHAR)
    private String port;
    //资源库密码
    @TableField(value = "RES_PWD_",jdbcType=JdbcType.VARCHAR)
    private String resPwd;
    //资源库用户
    @TableField(value = "RES_USER_",jdbcType=JdbcType.VARCHAR)
    private String resUser;
    //状态
    @TableField(value = "STATUS_",jdbcType=JdbcType.NUMERIC)
    private Integer status;
    //用户名
    @TableField(value = "USER_",jdbcType=JdbcType.VARCHAR)
    private String user;



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



