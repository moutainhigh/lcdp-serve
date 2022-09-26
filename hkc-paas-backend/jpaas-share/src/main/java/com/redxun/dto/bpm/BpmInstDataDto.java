package com.redxun.dto.bpm;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 流程实例数据DTO
 */
@Setter
@Getter
@Accessors(chain = true)
public class BpmInstDataDto {

    @JsonCreator
    public BpmInstDataDto() {
    }


    /**
     * 流程实例ID
     */
    private String instId;

    /**
     * 业务主键
     */
    private String pk;

    /**
     * BO定义名称。
     */
    private String bodefAlias;

    /**
     * 数据状态。
     */
    private String status="";


}
