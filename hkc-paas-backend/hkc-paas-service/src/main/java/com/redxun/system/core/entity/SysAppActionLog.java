
/**
 * <pre>
 *
 * 描述：应用操作日志实体类定义
 * 表:sys_app_action_log
 * 作者：Elwin ZHANG
 * 邮箱: ray@redxun.cn
 * 日期:2021-08-12 17:58:39
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
@TableName(value = "sys_app_action_log")
public class SysAppActionLog  extends BaseExtEntity<String> {

    @JsonCreator
    public SysAppActionLog() {
    }
    public static  final int TYPE_EDIT=3;
    public static  final  int TYPE_DELETE=4;
    public static  final  int TYPE_IMPORT=5;
    public static  final  int TYPE_EXPORT=6;

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;
    //1启动2停止3编辑4卸载5导入6导出7发布8生成前端工程9生成后端工程
    @TableField(value = "TYPE_",jdbcType=JdbcType.NUMERIC)
    private Integer type;
    //标题
    @TableField(value = "TITLE_",jdbcType=JdbcType.VARCHAR)
    private String title;
    //详细内容
    @TableField(value = "CONTENT_",jdbcType=JdbcType.VARCHAR)
    private String content;

    public String getTypeName(int type){
        switch (type){
            case TYPE_DELETE:
                return "删除";
            case TYPE_IMPORT:
                return "导入";
            case TYPE_EXPORT:
                return "导出";
        }
        return "编辑";
    }

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



