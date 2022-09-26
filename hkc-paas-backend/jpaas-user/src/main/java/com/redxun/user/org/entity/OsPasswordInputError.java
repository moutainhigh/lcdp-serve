
/**
 * <pre>
 *
 * 描述：用户密码输入错误记录表实体类定义
 * 表:os_password_input_error
 * 作者：zfh
 * 邮箱: zfh@redxun.cn
 * 日期:2022-05-10 16:51:29
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_password_input_error")
public class OsPasswordInputError  extends BaseExtEntity<String> {

    @JsonCreator
    public OsPasswordInputError() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //用户错误输入密码错误次数
    @TableField(value = "ERROR_TIME_",jdbcType=JdbcType.NUMERIC)
    private Integer errorTime;
    //用户编号
    @TableField(value = "USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String userNo;



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



