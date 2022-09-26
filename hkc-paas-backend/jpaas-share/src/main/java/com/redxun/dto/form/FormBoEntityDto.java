package com.redxun.dto.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bo定义实体
 */
@Data
public class FormBoEntityDto {

    public static  final String FIELD_INST="INST_ID_";
    public static  final String FIELD_INST_STATUS_="INST_STATUS_";

    //主键
    private String id;
    //名称
    private String name;
    //ALIAS_
    private String alias;
    //是否主表
    private Integer isMain;
    //表名
    private String tableName;
    //关联关系
    private String relationType;

    /**
     * Bo属性列表
     */
    private List<FormBoAttrDto> boAttrs=new ArrayList<>();
    /**
     * 子Bo列表
     */
    private List<FormBoEntityDto> subBoList=new ArrayList<>();
}
