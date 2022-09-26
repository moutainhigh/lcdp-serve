
/**
 * <pre>
 *
 * 描述：意见收藏表实体类定义
 * 表:bpm_opinion_lib
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-13 11:39:35
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
@TableName(value = "bpm_opinion_lib")
public class BpmOpinionLib  extends BaseExtEntity<String> {

    public static final String PUBLIC_OPINION ="0";

    @JsonCreator
    public BpmOpinionLib() {
    }

    //主键
    @TableId(value = "OP_ID_",type = IdType.INPUT)
	private String opId;

    //用户ID
    @TableField(value = "USER_ID_")
    private String userId;
    //审批意见
    @TableField(value = "OP_TEXT_")
    private String opText;

    @Override
    public String getPkId() {
        return opId;
    }

    @Override
    public void setPkId(String pkId) {
        this.opId=pkId;
    }
}



