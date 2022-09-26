
/**
 * <pre>
 *
 * 描述：应用授权表实体类定义
 * 表:SYS_APP_AUTH
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-25 15:52:02
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
@TableName(value = "SYS_APP_AUTH")
public class SysAppAuth  extends BaseExtEntity<String> {

    @JsonCreator
    public SysAppAuth() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用ID
    @TableField(value = "APP_ID_")
    private String appId;
    //方法类型
    @TableField(value = "METHOD_")
    private String method;
    //接口路径
    @TableField(value = "URL_")
    private String url;
    //所属服务
    @TableField(value = "SERVICE_")
    private String service;
    //API接口名称
    @TableField(value = "API_NAME_")
    private String apiName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



