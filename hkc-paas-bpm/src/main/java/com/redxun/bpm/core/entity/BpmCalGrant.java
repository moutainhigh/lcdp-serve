
/**
 * <pre>
 *
 * 描述：日历分配实体类定义
 * 表:bpm_cal_grant
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-09 11:00:51
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
@TableName(value = "bpm_cal_grant")
public class BpmCalGrant  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmCalGrant() {
    }

    //GRANT_ID_
    @TableId(value = "GRANT_ID_",type = IdType.INPUT)
	private String grantId;

    //SETTING_ID_
    @FieldDef(comment = "日历ID")
    @TableField(value = "SETTING_ID_")
    private String settingId;
    //分配类型 USER/GROUP
    @TableField(value = "GRANT_TYPE_")
    private String grantType;
    //BELONG_WHO_
    @TableField(value = "BELONG_WHO_")
    private String belongWho;
    @TableField(value = "BELONG_WHO_ID_")
    private String belongWhoId;


    @Override
    public String getPkId() {
        return grantId;
    }

    @Override
    public void setPkId(String pkId) {
        this.grantId=pkId;
    }
}



