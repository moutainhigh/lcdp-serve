package datart.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Link extends BaseEntity {
    private String relType;

    private String relId;

    private String url;

    private Date expiration;
}