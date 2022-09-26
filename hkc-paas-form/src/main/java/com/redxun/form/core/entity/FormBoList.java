
/**
 * <pre>
 *
 * 描述：系统自定义业务管理列表实体类定义
 * 表:form_bo_list
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-04 17:53:40
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hujun
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_bo_list")
public class FormBoList  extends FormBoListExt<String> {

    @JsonCreator
    public FormBoList() {
    }

    /**
     * ID
     */
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    /**
     * 名称
     */
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    /**
     * 标识键
     */
    @TableField(value = "KEY_", jdbcType = JdbcType.VARCHAR)
    private String key;
    /**
     * 描述
     */
    @TableField(value = "DESCP_", jdbcType = JdbcType.VARCHAR)
    private String descp;
    /**
     * 是否树对话框
     */
    @TableField(value = "IS_TREE_DLG_", jdbcType = JdbcType.VARCHAR)
    private String isTreeDlg = "NO";
    /**
     * 数据源ID
     */
    @TableField(value = "DB_AS_", jdbcType = JdbcType.VARCHAR)
    private String dbAs;
    /**
     * 是否对话框
     */
    @TableField(value = "IS_DIALOG_", jdbcType = JdbcType.VARCHAR)
    private String isDialog;
    /**
     * 是否已产生HTML
     */
    @TableField(value = "IS_GEN_", jdbcType = JdbcType.VARCHAR)
    private String isGen;
    /**
     * 分类ID
     */
    @TableField(value = "TREE_ID_", jdbcType = JdbcType.VARCHAR)
    private String treeId;
    /**
     * 列表显示模板
     */
    @TableField(value = "LIST_HTML_", jdbcType = JdbcType.VARCHAR)
    private String listHtml;
    /**
     * 手机端HTML
     */
    @TableField(value = "MOBILE_HTML_", jdbcType = JdbcType.VARCHAR)
    private String mobileHtml;
    /**
     * 应用ID
     */
    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_",jdbcType = JdbcType.VARCHAR)
    private String appId;

    @TableField(exist = false)
    Map<String,FormBoTopButton> topButtonMap=new HashMap<String, FormBoTopButton>();

    @TableField(exist = false)
    Map<String,TreeConfig> leftTreeMap=new HashMap<String, TreeConfig>();

    @TableField(exist = false)
    Map<String, GridHeader> columnHeaderMap=new HashMap<String,GridHeader>();

    //是否已授权
    @TableField(exist = false)
    String isGranted;

    @TableField(exist = false)
    private List<ListHistory> listHistory = new ArrayList<>();

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

}



