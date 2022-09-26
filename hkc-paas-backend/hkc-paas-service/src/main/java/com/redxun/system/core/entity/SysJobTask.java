
/**
 * <pre>
 *
 * 描述：定时任务定义实体类定义
 * 表:sys_job_task
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
@TableName(value = "sys_job_task")
public class SysJobTask  extends BaseExtEntity<String> {

    public static final String TYPE_CLASS="class";
    public static final String TYPE_SQL="sql";
    public static final String TYPE_SCRIPT="script";

    @JsonCreator
    public SysJobTask() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //配置内容
    @TableField(value = "CONTENT_",jdbcType=JdbcType.VARCHAR)
    private String content;
    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //状态
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status;
    //类型 (JOB,SCRIPT,CLASS)
    @TableField(value = "TYPE_",jdbcType=JdbcType.VARCHAR)
    private String type;





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



