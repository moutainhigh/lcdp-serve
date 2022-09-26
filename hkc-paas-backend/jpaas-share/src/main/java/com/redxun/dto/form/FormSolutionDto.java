package com.redxun.dto.form;


import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 单据方案DTO
 */
@Getter
@Setter
public class FormSolutionDto extends BaseDto {
    //方案ID
    private String id;

    //分类ID
    private String categoryId;
    //名称
    private String name;
    //别名
    private String alias;
    //业务模型ID
    private String bodefId;
    private String boAlias;
    //表单
    private String formId;
    private String formAlias;
    private String formName;
    //表单权限
    private String permission;
    //手机表单ID
    private String mobileFormId;

    private String mobileFormName;

    //表单数据处理器
    private String dataHandler;
    //TREE_
    private Integer tree=0;
    //表间公式
    private String formulas;
    //表间公式名称
    private String formulasName;
    //BUTTONS_SETTING_
    private String buttonsSetting;
    //NO_PK_SETTING_
    private String noPkSetting;
    //流程定义配置
    private String flowDefMapping;
    //JAVA脚本
    private String javaCode;
    //树形加载方式0,一次性加载,1,懒加载
    private Integer loadMode=0;
    //树显示字段
    private String displayFields="";

    private String appId;

    /**
     * 是否生成物理表
     */
    private int isGenerateTable;
}
