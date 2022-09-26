
/**
 * <pre>
 *
 * 描述：sys_excel_log实体类定义
 * 表:sys_excel_log
 * 作者：Ventus
 * 邮箱: hzh@redxun.cn
 * 日期:2020-11-27 11:14:19
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_excel_log")
public class ImportExcelLog extends BaseExtEntity<String> {

    @JsonCreator
    public ImportExcelLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //日志
    @TableField(value = "LOG_",jdbcType= JdbcType.VARCHAR)
    private String log;
    //模板ID
    @TableField(value = "TEMPLATED_",jdbcType=JdbcType.VARCHAR)
    private String templated;



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



