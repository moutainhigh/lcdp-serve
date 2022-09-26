package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;

@Data
@TableName(value = "SYS_TREE_CAT")
public class SysTreeCat  extends BaseExtEntity<String> {

    /**
     * 流程分类管理 BPM
     */
    public final static String CAT_BPM="BPM";
    /**
     * DIC	数字字典管理
     */
    public final static String CAT_DIC="DIC";
    /**
     * SEL_QY  自定义查询分类
     */
    public final static String CAT_SEL_QY="SEL_QY";
    /**
     * DS	数据源分类
     */
    public final static String CAT_DS="DS";
    /**
     * FORM	表单分类
     */
    public final static String CAT_FORM="FORM";

    @JsonCreator
    public SysTreeCat() {
    }
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "cat_id_", type = IdType.INPUT)
    private String catId;

    /**
     * 编码
     */
    @FieldDef(comment = "编码")
    @TableField(value = "KEY_")
    private String key;
    /**
     * 名称
     */
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    /**
     * 序号
     */
    @TableField(value = "SN_")
    private Integer sn;
    /**
     * 描述
     */
    @TableField(value = "DESCP_")
    private String descp;

    @Override
    public String getPkId() {
        return catId;
    }

    @Override
    public void setPkId(String pkId) {
        this.catId=pkId;
    }
}
