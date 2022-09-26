
/**
 * <pre>
 *
 * 描述：工作时间段设定实体类定义
 * 表:bpm_caltime_block
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-07 11:10:26
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_caltime_block")
public class BpmCalTimeBlock  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmCalTimeBlock() {
    }

    //SETTING_ID_
    @TableId(value = "SETTING_ID_",type = IdType.INPUT)
	private String settingId;

    //SETTING_NAME_
    @FieldDef(comment = "班次名称")
    @TableField(value = "SETTING_NAME_")
    private String settingName;
    //TIME_INTERVALS_
    @TableField(value = "TIME_INTERVALS_")
    private String timeIntervals;

    @Override
    public String getPkId() {
        return settingId;
    }

    @Override
    public void setPkId(String pkId) {
        this.settingId=pkId;
    }
}



