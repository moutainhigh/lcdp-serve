
/**
 * <pre>
 *
 * 描述：代理流程定义实体类定义
 * 表:bpm_agent_flowdef
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-22 09:57:24
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
@TableName(value = "bpm_agent_flowdef")
public class BpmAgentFlowDef  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmAgentFlowDef() {
    }

    //代理ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //代理定义ID
    @TableField(value = "AGENT_ID_")
    private String agentId;
    //流程定义ID
    @TableField(value = "DEF_ID_")
    private String defId;
    //流程定义名称
    @TableField(value = "DEF_NAME_")
    private String defName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



