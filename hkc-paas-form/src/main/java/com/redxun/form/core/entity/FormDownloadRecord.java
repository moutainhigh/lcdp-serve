
/**
 * <pre>
 *
 * 描述：Excel下载记录(异步)实体类定义
 * 表:FORM_DOWNLOAD_RECORD
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-06-14 17:06:14
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
@TableName(value = "FORM_DOWNLOAD_RECORD")
public class FormDownloadRecord  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormDownloadRecord() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //创建人名称
    @TableField(value = "CREATE_BY_NAME_",jdbcType=JdbcType.VARCHAR)
    private String createByName;
    //生成记录ID
    @TableField(value = "GEN_RECORD_",jdbcType=JdbcType.VARCHAR)
    private String genRecord;
    //列表ID
    @TableField(value = "LIST_ID_",jdbcType=JdbcType.VARCHAR)
    private String listId;
    //列表名称
    @TableField(value = "LIST_NAME_",jdbcType=JdbcType.VARCHAR)
    private String listName;



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



