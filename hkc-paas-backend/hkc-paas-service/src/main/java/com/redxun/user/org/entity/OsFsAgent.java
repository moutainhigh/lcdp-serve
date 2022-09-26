
/**
 * <pre>
 *
 * 描述：飞书应用表实体类定义
 * 表:os_fs_agent
 * 作者：ycs
 * 邮箱: yangchangsheng@redxun.cn
 * 日期:2022-06-10 11:11:53
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
@TableName(value = "os_fs_agent")
public class OsFsAgent  extends BaseExtEntity<String> {

    @JsonCreator
    public OsFsAgent() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用名称
    @TableField(value = "NAME_",jdbcType= JdbcType.VARCHAR)
    private String name;
    //应用ID
    @TableField(value = "APP_ID_",jdbcType= JdbcType.VARCHAR)
    private String appId;
    //密钥
    @TableField(value = "SECRET_",jdbcType= JdbcType.VARCHAR)
    private String secret;
    //PC主页
    @TableField(value = "PC_HOMEPAGE_",jdbcType= JdbcType.VARCHAR)
    private String pcHomepage;
    //后台主页
    @TableField(value = "ADMIN_PAGE_",jdbcType= JdbcType.VARCHAR)
    private String adminPage;
    //H5主页
    @TableField(value = "H5_HOMEPAGE_",jdbcType= JdbcType.VARCHAR)
    private String h5Homepage;
    //是否默认
    @TableField(value = "IS_DEFAULT_",jdbcType= JdbcType.NUMERIC)
    private Integer isDefault;
    //是否推送部门和用户至飞书
    @TableField(value = "IS_PUSH_",jdbcType= JdbcType.NUMERIC)
    private Integer isPush;




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



