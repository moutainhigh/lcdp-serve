
/**
 * <pre>
 *
 * 描述：自定义列表管理实体类定义
 * 表:FORM_CODEGEN_SETTING
 * 作者：gjh
 * 邮箱: shenzhongwen@redxun.cn
 * 日期:2022-05-06 11:55:00
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
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_CODEGEN_SETTING")
public class CodeGenSetting  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public CodeGenSetting() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //列表名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //别名
    @TableField(value = "KEY_",jdbcType=JdbcType.VARCHAR)
    private String key;
    //描述
    @TableField(value = "DESCP_",jdbcType=JdbcType.VARCHAR)
    private String descp;

    //分类Id
    @TableField(value = "TREE_ID_",jdbcType=JdbcType.VARCHAR)
    private String treeId;


    //生成代码配置信息
    @TableField(value = "SETTING_JSON_",jdbcType=JdbcType.VARCHAR)
    private String settingJson;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



