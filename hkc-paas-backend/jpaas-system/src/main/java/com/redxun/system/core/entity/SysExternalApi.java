
/**
 * <pre>
 *
 * 描述：对外API接口管理实体类定义
 * 表:SYS_EXTERNAL_API
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-05-11 15:57:13
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
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_EXTERNAL_API")
public class SysExternalApi  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public SysExternalApi() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //所属服务
    @TableField(value = "SERVICE_",jdbcType=JdbcType.VARCHAR)
    private String service;
    //API接口名称
    @TableField(value = "API_NAME_",jdbcType=JdbcType.VARCHAR)
    private String apiName;
    //请求体
    @TableField(value = "BODY_",jdbcType=JdbcType.VARCHAR)
    private String body;
    //请求头
    @TableField(value = "HEADERS_",jdbcType=JdbcType.VARCHAR)
    private String headers;
    //请求方法
    @TableField(value = "METHOD_",jdbcType=JdbcType.VARCHAR)
    private String method;
    //请求参数
    @TableField(value = "PARAMS_",jdbcType=JdbcType.VARCHAR)
    private String params;
    //路径
    @TableField(value = "PATH_",jdbcType=JdbcType.VARCHAR)
    private String path;
    //类型
    @TableField(value = "TYPE_",jdbcType=JdbcType.VARCHAR)
    private String type;



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



