
/**
 * <pre>
 *
 * 描述：离职人员流程任务表实体类定义
 * 表:bpm_quit_user_task
 * 作者：zfh
 * 邮箱: zfh@redxun.cn
 * 日期:2022-05-11 18:13:08
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_quit_user_task")
public class BpmQuitUserTask  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmQuitUserTask() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //离职人员ID
    @TableField(value = "QUIT_USER_ID_",jdbcType=JdbcType.VARCHAR)
    private String quitUserId;
    //离职人员工号
    @TableField(value = "QUIT_USER_NO_",jdbcType=JdbcType.VARCHAR)
    private String quitUserNo;
    //离职人员
    @TableField(value = "QUIT_USER_NAME_",jdbcType=JdbcType.VARCHAR)
    private String quitUserName;
    //流程定义ID
    @TableField(value = "DEF_ID_",jdbcType=JdbcType.VARCHAR)
    private String defId;
    //流程定义标题
    @TableField(value = "DEF_NAME_",jdbcType=JdbcType.VARCHAR)
    private String defName;
    //流程节点ID
    @TableField(value = "NODE_ID_",jdbcType=JdbcType.VARCHAR)
    private String nodeId;
    //流程节点名称
    @TableField(value = "NODE_NAME_",jdbcType=JdbcType.VARCHAR)
    private String nodeName;



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



