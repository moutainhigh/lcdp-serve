
/**
 * <pre>
 *
 * 描述：系统分类树
实体类定义
 * 表:sys_tree
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-01 17:05:29
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "sys_tree")
public class SysTree  extends BaseExtEntity<String> {
    /**
     * 树节点
     */
    public final static Short IS_LEAF=1;
    /**
     * 非树节点
     */
    public final static Short IS_NOT_LEAF=0;

    @JsonCreator
    public SysTree() {
    }

    //分类Id
    @TableId(value = "TREE_ID_",type = IdType.INPUT)
	private String treeId;

    //同级编码

    @TableField(value = "CODE_")
    private String code;
    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //路径
    @TableField(value = "PATH_")
    private String path;
    //父节点
    @TableField(value = "PARENT_ID_")
    private String parentId;
    //节点的分类Key
    @FieldDef(comment = "分类Key")
    @TableField(value = "ALIAS_")
    private String alias;
    //描述
    @TableField(value = "DESCP_")
    private String descp;
    //系统树分类key
    @TableField(value = "CAT_KEY_")
    private String catKey;
    //序号
    @TableField(value = "SN_")
    private Integer sn;


    @TableField(exist = false)
    private List<SysTree> children;

    @TableField(exist = false)
    private Integer childAmount;

    @TableField(exist = false)
    private String right;

    //展示类型
    @TableField(value = "DATA_SHOW_TYPE_")
    private String dataShowType;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private String appName; //应用名称

    @Override
    public String getPkId() {
        return treeId;
    }

    @Override
    public void setPkId(String pkId) {
        this.treeId=pkId;
    }
}



