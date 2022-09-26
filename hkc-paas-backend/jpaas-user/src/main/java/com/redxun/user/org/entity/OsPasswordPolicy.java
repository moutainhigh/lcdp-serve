
/**
 * <pre>
 *
 * 描述：密码安全策略管理配置实体类定义
 * 表:os_password_policy_configure
 * 作者：zfh
 * 邮箱: zfh@redxun.cn
 * 日期:2022-05-10 16:51:29
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName(value = "os_password_policy")
public class OsPasswordPolicy extends BaseExtEntity<String> {

    @JsonCreator
    public OsPasswordPolicy() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //账号锁定天数
    @TableField(value = "ACCOUNT_LOCK_DAY_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer accountLockDay;
    //最多错误输入密码错误次数
    @TableField(value = "ERROR_TIME_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer errorTime;
    //通知内容
    @TableField(value = "INFORM_CONTENT_",jdbcType=JdbcType.VARCHAR)
    private String informContent;
    //几天后通知修改密码（天）
    @TableField(value = "INFORM_DAY_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer informDay;
    //通知频率(每隔多少天通知一次)
    @TableField(value = "INFORM_FREQUENCY_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer informFrequency;
    //密码修改通知方式
    @TableField(value = "INFORM_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String informType;
    //用户首次登录必须修改密码
    @TableField(value = "IS_FIRST_LOGIN_UPDATE_",jdbcType=JdbcType.VARCHAR)
    private String isFirstLoginUpdate;
    //密码同时由英文大小写特殊字符和数字构成
    @TableField(value = "IS_MIX_",jdbcType=JdbcType.VARCHAR)
    private String isMix;
    //密码永不过期
    @TableField(value = "IS_NEVER_TIMEOUT_",jdbcType=JdbcType.VARCHAR)
    private String isNeverTimeout;
    //用户名密码能否一致
    @TableField(value = "IS_USERNAME_PWD_CONSISTENT_",jdbcType=JdbcType.VARCHAR)
    private String isUsernamePwdConsistent;
    //最小密码长度
    @TableField(value = "MIN_LENGTH_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer minLength;
    //密码过期时间（天）
    @TableField(value = "TIMEOUT_DAY_",jdbcType=JdbcType.NUMERIC, updateStrategy = FieldStrategy.IGNORED)
    private Integer timeoutDay;



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



