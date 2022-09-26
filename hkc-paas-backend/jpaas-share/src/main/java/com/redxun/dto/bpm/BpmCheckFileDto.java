package com.redxun.dto.bpm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 审批附件DTO
 */
@Data
public class BpmCheckFileDto extends BaseDto {
    private String id;

    //FILE_ID_
    private String fileId;

    //FILE_NAME_
    private String fileName;
    //流程实例ID
    private String instId;
    //JUMP_ID_
    private String jumpId;
}
