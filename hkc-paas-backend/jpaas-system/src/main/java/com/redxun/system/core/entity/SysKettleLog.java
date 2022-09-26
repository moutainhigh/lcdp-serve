
/**
 * <pre>
 *
 * 描述：KETTLE日志实体类定义
 * 表:SYS_KETTLE_LOG
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-06-17 22:23:16
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_KETTLE_LOG")
public class SysKettleLog  extends BaseExtEntity<String> {

    @JsonCreator
    public SysKettleLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //运行时长
    @TableField(value = "DRUATION_")
    private Long druation;
    //KETTLE定义ID
    @TableField(value = "KETTLE_ID_")
    private String kettleId;
    //日志
    @TableField(value = "LOG_")
    private String log;
    //状态(1.成功,0失败)
    @TableField(value = "STATUS_")
    private Integer status;
    //任务开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "START_TIME_", fill = FieldFill.INSERT_UPDATE,jdbcType = JdbcType.TIMESTAMP)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    //任务开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "END_TIME_", fill = FieldFill.INSERT_UPDATE,jdbcType = JdbcType.TIMESTAMP)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    //任务ID
    @TableField(value = "KETTLE_JOB_ID_")
    private String kettleJobId;
    //任务名称
    @TableField(value = "KETTLE_JOB_NAME_")
    private String kettleJobName;
    //KETTLE类型
    @TableField(value = "KETTLE_TYPE_")
    private String kettleType;

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



