
/**
 * <pre>
 *
 * 描述：流程实例权限表实体类定义
 * 表:BPM_INST_PERMISSION
 * 作者：hj
 * 邮箱: hujun@redxun.cn
 * 日期:2022-08-01 11:18:45
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "BPM_INST_PERMISSION")
public class BpmInstPermission  extends BaseExtEntity<java.lang.String> {
    //流程管理员
    public static final String TYPE_ADMIN="admin";
    //经办人
    public static final String TYPE_HANDLER="handler";

    @JsonCreator
    public BpmInstPermission() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;
    //授权人ID
    @TableField(value = "AUTH_ID_",jdbcType=JdbcType.VARCHAR)
    private String authId;
    //授权人名称
    @TableField(value = "AUTH_NAME_",jdbcType=JdbcType.VARCHAR)
    private String authName;
    //流程定义ID
    @TableField(value = "DEF_ID_",jdbcType=JdbcType.VARCHAR)
    private String defId;
    //流程定义KEY
    @TableField(value = "DEF_KEY_",jdbcType=JdbcType.VARCHAR)
    private String defKey;
    //备注
    @TableField(value = "DESC_",jdbcType=JdbcType.VARCHAR)
    private String desc;
    //实例ID
    @TableField(value = "INST_ID_",jdbcType=JdbcType.VARCHAR)
    private String instId;
    //是否删除
    @TableField(value = "IS_DELETE_",jdbcType=JdbcType.VARCHAR)
    private String isDelete="0";
    //状态
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status=MBoolean.YES.name();
    //标题
    @TableField(value = "SUBJECT_",jdbcType=JdbcType.VARCHAR)
    private String subject;
    //任务ID
    @TableField(value = "TASK_ID_",jdbcType=JdbcType.VARCHAR)
    private String taskId;
    //类型
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



