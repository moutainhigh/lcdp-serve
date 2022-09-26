
/**
 * <pre>
 *
 * 描述：平台开发应用收藏夹实体类定义
 * 表:sys_app_favorites
 * 作者：Elwin ZHANG
 * 邮箱: elwin.zhang@qq.com
 * 日期:2022-01-14 14:53:55
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

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
@TableName(value = "sys_app_favorites")
public class SysAppFavorites  extends BaseExtEntity<String> {

    @JsonCreator
    public SysAppFavorites() {
    }

    //记录ID
    @TableId(value = "FAV_ID_",type = IdType.INPUT)
	private String favId;

    //用户ID
    @TableField(value = "USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String userId;
    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;
    //是否收藏（0取消收藏）
    @TableField(value = "IS_FAV_",jdbcType=JdbcType.NUMERIC)
    private Short isFav;
    //收藏时间
    @TableField(value = "FAV_TIME_",jdbcType=JdbcType.TIMESTAMP)
    private java.util.Date favTime;
    //最近使用时间
    @TableField(value = "LAST_USE_TIME_",jdbcType=JdbcType.TIMESTAMP,updateStrategy = FieldStrategy.IGNORED)
    private java.util.Date lastUseTime;


    @Override
    public String getPkId() {
        return favId;
    }

    @Override
    public void setPkId(String pkId) {
        this.favId=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



