
/**
 * <pre>
 *
 * 描述：KETTLE任务调度实体类定义
 * 表:SYS_KETTLE_JOB
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2021-08-27 17:58:21
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
@TableName(value = "SYS_KETTLE_JOB")
public class SysKettleJob  extends BaseExtEntity<String> {

    @JsonCreator
    public SysKettleJob() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //KETTLE定义ID
    @TableField(value = "KETTLE_ID_",jdbcType=JdbcType.VARCHAR)
    private String kettleId;
    //策略
    @TableField(value = "STRATEGY_",jdbcType=JdbcType.VARCHAR)
    private String strategy;
    //日志级别
    @TableField(value = "LOGLEVEL_",jdbcType=JdbcType.VARCHAR)
    private String loglevel;
    //状态(0为禁用,1为启用)
    @TableField(value = "STATUS_",jdbcType=JdbcType.NUMERIC)
    private Integer status;
    //备注
    @TableField(value = "REMARK_",jdbcType=JdbcType.VARCHAR)
    private String remark;



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



