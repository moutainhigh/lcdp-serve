
/**
 * <pre>
 *
 * 描述：系统日志实体类定义
 * 表:SYS_LOG
 * 作者：gjh
 * 邮箱: zyg@redxun.cn
 * 日期:2021-01-20 22:32:33
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


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_LOG")
public class SysLog  extends BaseExtEntity<String> {

    @JsonCreator
    public SysLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用名称
    @TableField(value = "APP_NAME_")
    private String appName;
    //模块
    @TableField(value = "MODULE_")
    private String module;
    //子模块
    @TableField(value = "SUB_MODULE_")
    private String subModule;
    //类名
    @TableField(value = "CLASS_NAME_")
    private String className;
    //方法名
    @TableField(value = "METHOD_NAME_")
    private String methodName;
    //动作
    @TableField(value = "ACTION_")
    private String action;
    //业务主键
    @TableField(value = "PK_VALUE_")
    private String pkValue;
    //IP_
    @TableField(value = "IP_")
    private String ip;
    //操作
    @TableField(value = "OPERATION_")
    private String operation;
    //操作明细
    @TableField(value = "DETAIL_")
    private String detail;
    //用户名
    @TableField(value = "USER_NAME_")
    private String userName;
    //时长
    @TableField(value = "DURATION_")
    private Integer duration;
    //业务类型
    @TableField(value = "BUS_TYPE_")
    private String busType;
    //是否已恢复
    @TableField(value = "IS_RESUME_")
    private String isResume;



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



