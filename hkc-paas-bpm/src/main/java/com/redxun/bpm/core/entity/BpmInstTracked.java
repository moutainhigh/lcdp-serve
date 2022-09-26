
/**
 * <pre>
 *
 * 描述：流程实例跟踪实体类定义
 * 表:BPM_INST_TRACKED
 * 作者：gjh
 * 邮箱: hujun@redxun.cn
 * 日期:2022-06-15 16:08:37
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
@TableName(value = "BPM_INST_TRACKED")
public class BpmInstTracked  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public BpmInstTracked() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //当前节点
    @TableField(value = "CUR_NODE_",jdbcType=JdbcType.VARCHAR)
    private String curNode;
    //流程实例
    @TableField(value = "INST_ID_",jdbcType=JdbcType.VARCHAR)
    private String instId;

    @TableField(exist = false)
    private BpmInst bpmInst;

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



