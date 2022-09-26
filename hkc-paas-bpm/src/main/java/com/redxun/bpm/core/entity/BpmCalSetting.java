
/**
 * <pre>
 *
 * 描述：日历设定实体类定义
 * 表:bpm_cal_setting
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-07 15:14:04
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
@TableName(value = "bpm_cal_setting")
public class BpmCalSetting  extends BaseExtEntity<String> {

    public static final String IS_COMMON_YES="YES";
    public static final String IS_COMMON_NO="NO";

    @JsonCreator
    public BpmCalSetting() {
    }

    //设定ID
    @TableId(value = "SETTING_ID_",type = IdType.INPUT)
	private String settingId;

    //日历名称
    @FieldDef(comment = "日历名称")
    @TableField(value = "CAL_NAME_")
    private String calName;
    //默认
    @TableField(value = "IS_COMMON_")
    private String isCommon;

    @Override
    public String getPkId() {
        return settingId;
    }

    @Override
    public void setPkId(String pkId) {
        this.settingId=pkId;
    }
}



