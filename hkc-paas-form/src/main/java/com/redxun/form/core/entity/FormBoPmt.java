
/**
 * <pre>
 *
 * 描述：业务单据数据权限实体类定义
 * 表:SYS_BO_PMT
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-08-26 15:17:40
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_BO_PMT")
public class FormBoPmt extends BaseExtEntity<String> {

    /**
     * TYPE_=LIST 数据权限
     */
    public final static String TYPE_LIST="LIST";
    /**
     * TYPE_=FIELD 字段权限
     */
    public final static String TYPE_FIELD="FIELD";

    /**
     * TYPE_=BUTTON 按钮权限
     */
    public final static String TYPE_BUTTON="BUTTON";

    @JsonCreator
    public FormBoPmt() {
    }

    //权限ID
    @TableId(value = "PMT_ID_",type = IdType.INPUT)
	private String pmtId;

    //权限别名
    @TableField(value = "ALIAS_",jdbcType = JdbcType.VARCHAR)
    private String alias;
    //权限名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;

    //BO列表ID
    @TableField(value = "BO_LIST_ID_", jdbcType = JdbcType.VARCHAR)
    private String boListId;

    //按钮权限
    @TableField(value = "BUTTONS_", jdbcType = JdbcType.VARCHAR)
    private String buttons;

    //数据权限
    @TableField(value = "DATAS_", jdbcType = JdbcType.VARCHAR)
    private String datas;

    //字段权限
    @TableField(value = "FIELDS_", jdbcType = JdbcType.VARCHAR)
    private String fields;

    //是否可用(YES,NO)
    @TableField(value = "STATUS_", jdbcType = JdbcType.VARCHAR)
    private String status;

    //菜单ID
    @TableField(value = "MENU_ID_",jdbcType = JdbcType.VARCHAR)
    private String menuId;

    //BO列表名称
    @TableField(exist = false)
    private String boListName;
    //菜单名称
    @TableField(exist = false)
    private String menuName;

    @Override
    public String getPkId() {
        return pmtId;
    }

    @Override
    public void setPkId(String pkId) {
        this.pmtId=pkId;
    }
}



