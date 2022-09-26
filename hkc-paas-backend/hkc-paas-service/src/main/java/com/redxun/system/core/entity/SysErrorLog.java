
/**
 * <pre>
 *
 * 描述：错误日志实体类定义
 * 表:SYS_ERROR_LOG
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-07-19 13:53:58
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
@TableName(value = "SYS_ERROR_LOG")
public class SysErrorLog  extends BaseExtEntity<String> {

    @JsonCreator
    public SysErrorLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //跟踪ID
    @TableField(value = "TRACE_ID_",jdbcType=JdbcType.VARCHAR)
    private String traceId;
    //APP_NAME_
    @TableField(value = "APP_NAME_",jdbcType=JdbcType.VARCHAR)
    private String appName;
    //访问地址
    @TableField(value = "URL_",jdbcType=JdbcType.VARCHAR)
    private String url;
    //错误内容
    @TableField(value = "CONTENT_",jdbcType=JdbcType.VARCHAR)
    private String content;



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



