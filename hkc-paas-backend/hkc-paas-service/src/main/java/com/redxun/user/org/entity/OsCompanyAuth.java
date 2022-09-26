
/**
 * <pre>
 *
 * 描述：分公司授权表实体类定义
 * 表:OS_COMPANY_AUTH
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-06-28 15:53:32
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
@TableName(value = "OS_COMPANY_AUTH")
public class OsCompanyAuth  extends BaseExtEntity<String> {

    @JsonCreator
    public OsCompanyAuth() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //GROUP_ID_
    @TableField(value = "GROUP_ID_",jdbcType=JdbcType.VARCHAR)
    private String groupId;
    //GROUP_NAME_
    @TableField(value = "GROUP_NAME_",jdbcType=JdbcType.VARCHAR)
    private String groupName;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }



}



