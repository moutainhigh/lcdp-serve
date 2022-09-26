
/**
 * <pre>
 *
 * 描述：代理配置实体类定义
 * 表:bpm_agent
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_agent")
public class BpmAgent  extends BaseExtEntity<String> {

    public static String TYPE_ALL="all";

    public static String TYPE_FLOWDEF="flowdef";

    @JsonCreator
    public BpmAgent() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //代理名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //所有人
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //类型
    @TableField(value = "TYPE_")
    private String type;
    //代理给的用户
    @TableField(value = "TO_USER_")
    private String toUser;

    @TableField(value = "TO_USER_NAME_" )
    private String toUserName;
    //是否启用
    @TableField(value = "STATUS_")
    private Integer status;
    //开始时间
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "START_TIME_")
    private java.util.Date startTime;
    //结束时间
    @JsonFormat(pattern="yyyy-MM-dd")
    @TableField(value = "END_TIME_")
    private java.util.Date endTime;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

    @TableField(exist=false)
    public List<BpmAgentFlowDef> bpmAgentFlowDefs=new ArrayList<>();
}



