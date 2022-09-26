
/**
 * <pre>
 *
 * 描述：流程归档日志实体类定义
 * 表:bpm_archive_log
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-08-26 11:00:47
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "bpm_archive_log")
public class BpmArchiveLog extends BaseExtEntity<String> {

    @JsonCreator
    public BpmArchiveLog() {
    }

    //主键
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    //归档日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "ARCHIVE_DATE_")
    private java.util.Date archiveDate;
    //备注
    @FieldDef(comment = "备注")
    @TableField(value = "MEMO_")
    private String memo;
    //CREATE_NAME_
    @TableField(value = "CREATE_NAME_")
    private String createName;
    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "START_TIME_")
    private java.util.Date startTime;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "END_TIME_")
    private java.util.Date endTime;
    //表名ID
    @TableField(value = "TABLE_ID_")
    private Integer tableId;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //错误日志
    @TableField(value = "ERR_LOG_")
    private String errLog;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }

    public static List<String> getTables() {
        List<String> tables = new ArrayList<>();
        tables.add("{name:'BPM_INST',pk:'INST_ID_'}");
        tables.add("{name:'BPM_RU_PATH',pk:'PATH_ID_'}");
        tables.add("{name:'BPM_INST_DATA',pk:'ID_'}");
        tables.add("{name:'BPM_INST_CC',pk:'CC_ID_'}");
        tables.add("{name:'BPM_INST_CP',pk:'ID_'}");
        tables.add("{name:'BPM_INST_LOG',pk:'ID_'}");
        tables.add("{name:'BPM_INST_MSG',pk:'ID_'}");
        tables.add("{name:'BPM_REMIND_INST',pk:'ID_'}");
        tables.add("{name:'BPM_REMIND_HISTORY',pk:'ID_'}");
        tables.add("{name:'BPM_CHECK_FILE',pk:'FILE_ID_'}");
        tables.add("{name:'BPM_CHECK_HISTORY',pk:'HIS_ID_'}");
        return tables;
    }
}



