
/**
 * <pre>
 *
 * 描述：流程意见暂存实体类定义
 * 表:bpm_temporary_opinion
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-10-27 11:04:15
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


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_temporary_opinion")
public class BpmTemporaryOpinion  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmTemporaryOpinion() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //意见
    @TableField(value = "OPINION_")
    private String opinion;
    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


}



