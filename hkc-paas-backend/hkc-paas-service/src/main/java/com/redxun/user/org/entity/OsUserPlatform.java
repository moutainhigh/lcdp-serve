
/**
 * <pre>
 *
 * 描述：第三方平台登陆绑定实体类定义
 * 表:os_user_platform
 * 作者：ycs
 * 邮箱: yangchangsheng@redxun.cn
 * 日期:2022-06-10 11:02:46
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
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_user_platform")
public class OsUserPlatform  extends BaseExtEntity<String> {

    @JsonCreator
    public OsUserPlatform() {
    }

    //主键ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //用户ID
    @TableField(value = "USER_ID_",jdbcType= JdbcType.VARCHAR)
    private String userId;
    //第三方平台类型：1微信公众号，2企业微信，3钉钉，4飞书
    @TableField(value = "PLATFORM_TYPE_",jdbcType= JdbcType.NUMERIC)
    private Integer platformType;
    //第三方平台唯一id
    @TableField(value = "OPEN_ID_",jdbcType= JdbcType.VARCHAR)
    private String openId;



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



