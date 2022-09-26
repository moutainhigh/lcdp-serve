
/**
 * <pre>
 *
 * 描述：数据源共享实体类定义
 * 表:form_datasource_share
 * 作者：Elwin ZHANG
 * 邮箱: elwin.zhang@qq.com
 * 日期:2021-12-23 16:39:04
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

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
@TableName(value = "form_datasource_share")
public class FormDatasourceShare extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormDatasourceShare() {
    }

    //记录ID
    @TableId(value = "SHARE_ID_", type = IdType.INPUT)
    private String shareId;

    //数据源ID
    @TableField(value = "DS_ID_", jdbcType = JdbcType.VARCHAR)
    private String dsId;
    //应用ID
    @TableField(value = "APP_ID_", jdbcType = JdbcType.VARCHAR)
    private String appId;

    //应用名
    @TableField(value = "APP_NAME_", jdbcType = JdbcType.VARCHAR)
    private String appName;

    @Override
    public String getPkId() {
        return shareId;
    }

    @Override
    public void setPkId(String pkId) {
        this.shareId = pkId;
    }

}



