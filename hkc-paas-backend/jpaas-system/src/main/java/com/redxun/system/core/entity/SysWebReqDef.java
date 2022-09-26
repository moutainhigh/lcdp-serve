
/**
 * <pre>
 *
 * 描述：WEB请求调用定义实体类定义
 * 表:SYS_WEBREQ_DEF
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-23 10:07:13
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_WEBREQ_DEF")
public class SysWebReqDef extends BaseExtEntity<String> {

    @JsonCreator
    public SysWebReqDef() {
    }

    //主键
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_")
    private String alias;
    //请求地址
    @TableField(value = "URL_")
    private String url;
    //请求方式
    @TableField(value = "MODE_")
    private String mode;
    //请求类型
    @TableField(value = "TYPE_")
    private String type;
    //数据类型
    @TableField(value = "DATA_TYPE_")
    private String dataType;
    //参数配置
    @TableField(value = "PARAMS_SET_")
    private String paramsSet;
    //传递数据
    @TableField(value = "DATA_")
    private String data;
    //请求报文模板
    @TableField(value = "TEMP_")
    private String temp;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //是否记录日志
    @TableField(value = "IS_LOG_")
    private String isLog;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



