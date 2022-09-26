
/**
 * <pre>
 *
 * 描述：Excel生成任务实体类定义
 * 表:FORM_EXCEL_GEN_TASK
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
@TableName(value = "FORM_EXCEL_GEN_TASK")
public class FormExcelGenTask  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormExcelGenTask() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //创建人名称
    @TableField(value = "CREATE_BY_NAME_",jdbcType=JdbcType.VARCHAR)
    private String createByName;
    //文件ID
    @TableField(value = "FILE_ID_",jdbcType=JdbcType.VARCHAR)
    private String fileId;
    //文件名称
    @TableField(value = "FILE_NAME_",jdbcType=JdbcType.VARCHAR)
    private String fileName;
    //生成状态(0为失败、1为成功、2为正在生成,进度：25/50/75/100)
    @TableField(value = "GEN_STATUS_",jdbcType=JdbcType.VARCHAR)
    private String genStatus;
    //列表主键
    @TableField(value = "LIST_ID_",jdbcType=JdbcType.VARCHAR)
    private String listId;
    //列表名称
    @TableField(value = "LIST_NAME_",jdbcType=JdbcType.VARCHAR)
    private String listName;
    //备注
    @TableField(value = "REMARK_",jdbcType=JdbcType.VARCHAR)
    private String remark;



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



