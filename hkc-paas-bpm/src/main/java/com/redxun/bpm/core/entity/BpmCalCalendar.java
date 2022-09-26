
/**
 * <pre>
 *
 * 描述：工作日历安排实体类定义
 * 表:bpm_cal_calendar
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-07 15:15:36
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
@TableName(value = "bpm_cal_calendar")
public class BpmCalCalendar  extends BaseExtEntity<String> {

    public static final int TIME_NUM=60000;

    @JsonCreator
    public BpmCalCalendar() {
    }

    //日历Id
    @TableId(value = "CALENDER_ID_",type = IdType.INPUT)
	private String calenderId;

    //设定ID
    @TableField(value = "SETTING_ID_")
    private String settingId;
    //开始时间
    @TableField(value = "START_TIME_")
    private java.util.Date startTime;
    //结束时间
    @TableField(value = "END_TIME_")
    private java.util.Date endTime;
    //连接在一起的时间段ID
    @TableField(value = "CONNECT_ID_")
    private String connectId;
    //记录选择的开始时间
    @TableField(value = "START_DAY_")
    private java.sql.Timestamp startDay;
    //记录最终计算出来的结束时间
    @TableField(value = "END_DAY_")
    private java.sql.Timestamp endDay;
    //显示在每一条条状的信息
    @TableField(value = "INFO_")
    private String info;
    //将选择的时间json放入在这里，之后如果改条状调整过的话，直接将这个json来计算新的数据
    @TableField(value = "TIME_INTERVALS_")
    private String timeIntervals;

    @Override
    public String getPkId() {
        return calenderId;
    }

    @Override
    public void setPkId(String pkId) {
        this.calenderId=pkId;
    }
}



