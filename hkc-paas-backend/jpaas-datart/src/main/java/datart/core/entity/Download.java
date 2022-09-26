package datart.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Download extends BaseEntity {
    private String name;

    private String path;

    private Date lastDownloadTime;

    private Byte status;
}