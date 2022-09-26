
/**
 * <pre>
 *
 * 描述：流程定义实体类定义
 * 表:bpm_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-02 14:23:15
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "bpm_def")
public class BpmDef  extends BaseExtEntity<String> {
    /**
     * 发布状态
     */
    public final static String STATUS_DEPLOYED="DEPLOYED";
    /**
     * 草稿状态
     */
    public final static String STATUS_DRAFT="DRAFT";
    /**
     * 作废状态
     */
    public final static String STATUS_INVALID="INVALID";

    /**
     * 是否主版本 YES
     */
    public final static String IS_MAIN="YES";
    /**
     * 是否非主版本 NO
     */
    public  final static String IS_NOT_MAIN="NO";

    /**
     * 正式版本
     */
    public final static String FORMAL_YES="yes";

    /**
     * 测试版本
     */
    public final static String FORMAL_NO="no";
    /**
     * 缓存统一前置Key
     */
    public final static String CACHE_PRE="PROCESS_";

    /**
     * 流程扩展节点属性前置Key=Process
     */
    public final static String PROCESS_NODE_PRE="Process";

    @JsonCreator
    public BpmDef() {
    }

    //流程定义ID
    @TableId(value = "DEF_ID_",type = IdType.INPUT)
	private String defId;

    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //标题
    @TableField(value = "NAME_")
    private String name;
    //编码
    @TableField(value = "KEY_")
    private String key;
    //描述
    @TableField(value = "DESCP_")
    private String descp;
    //ACT定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;
    //ACT发布ID
    @TableField(value = "ACT_DEP_ID_")
    private String actDepId;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //版本号
    @TableField(value = "VERSION_")
    private Integer version;
    //主版本
    @TableField(value = "IS_MAIN_")
    private String isMain;
    //设计XML
    @TableField(value = "DESIGN_XML_")
    private String designXml;
    //扩展配置
    @TableField(value = "EXT_CONFS")
    private String extConfs;
    //设计XML(临时)
    @TableField(value = "DESIGN_XML_TEMP_")
    private String designXmlTemp;
    //扩展配置(临时)
    @TableField(value = "EXT_CONFS_TEMP")
    private String extConfsTemp;
    //主定义ID
    @TableField(value = "MAIN_DEF_ID_")
    private String mainDefId;
    //BO定义ID
    @TableField(value = "BO_DEF_IDS_")
    private String boDefIds;
    //流程方案图标
    @TableField(value = "ICON_")
    private String icon;
    //流程方案图标颜色
    @TableField(value = "COLOR_")
    private String color;
    //正式测试状态
    @TableField(value = "FORMAL_")
    private String formal;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private Boolean deploy;
    @Override
    public String getPkId() {
        return defId;
    }

    @Override
    public void setPkId(String pkId) {
        this.defId=pkId;
    }
}



