package com.redxun.dto.sys;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统树DTO
 */
@Data
public class SysTreeDto extends BaseDto {


    /**
     * 树节点
     */
    public final static Short IS_LEAF=new Short("1");
    /**
     * 非树节点
     */
    public final static Short IS_NOT_LEAF=new Short("0");


    //分类Id
    private String treeId;

    //同级编码
    private String code;
    //名称
    private String name;
    //路径
    private String path;
    //父节点
    private String parentId;
    //节点的分类别名
    private String alias;
    //描述
    private String descp;
    //系统树分类key
    private String catKey;
    //序号
    private Integer sn;

    //是否为叶节点
    private Short isLeaf;

    //展示类型
    private String dataShowType;

    //权限
    private String right;


}
