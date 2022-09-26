package com.redxun.dto.sys;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 数字字典DTO
 */
@Getter
@Setter
public class SysDicDto {

    public SysDicDto(){}

    //主键
    private String dicId;

    //分类Id
    private String treeId;
    //项名
    private String name;
    //项值
    private String value;
    //描述
    private String descp;
    //序号
    private Integer sn = 1;
    //路径
    private String path;
    //父ID
    private String parentId;


    private List<SysDicDto> children;


    private Integer childAmount;


}
