
/**
 * <pre>
 *
 * 描述：系统定时任务实体类定义
 * 表:sys_job
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-12-30 17:21:18
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
@TableName(value = "sys_job")
public class SysJob  extends BaseExtEntity<String> {

    @JsonCreator
    public SysJob() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //JOB任务ID
    @TableField(value = "JOB_TASK_ID_",jdbcType=JdbcType.VARCHAR)
    private String jobTaskId;
    //任务名称
    @TableField(value = "JOB_TASK_",jdbcType=JdbcType.VARCHAR)
    private String jobTask;

    //策略
    @TableField(value = "STRATEGY_",jdbcType=JdbcType.VARCHAR)
    private String strategy;
    //状态(0为禁用,1为启用)
    @TableField(value = "STATUS_",jdbcType=JdbcType.NUMERIC)
    private Integer status;




    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }




}



