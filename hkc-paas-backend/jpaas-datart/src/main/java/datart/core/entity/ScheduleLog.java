package datart.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleLog extends BaseEntity {
    private String scheduleId;

    private Date start;

    private Date end;

    private Integer status;

    private String message;
}