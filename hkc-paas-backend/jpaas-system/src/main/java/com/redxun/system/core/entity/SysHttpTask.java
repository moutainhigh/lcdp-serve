
/**
 * <pre>
 *
 * 描述：接口调用表实体类定义
 * 表:SYS_HTTP_TASK
 * 作者：hj
 * 邮箱: hujun@redxun.cn
 * 日期:2022-03-15 18:13:59
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
@TableName(value = "SYS_HTTP_TASK")
public class SysHttpTask extends BaseExtEntity<String> {

    //类型-web请求接口
    public final static String TYPE_WEBREQ="webreq";
    //类型-第三方接口
    public final static String TYPE_INTERFACE="interface";

    //状态-执行成功
    public final static String STATUS_OK="ok";
    //状态-执行失败
    public final static String STATUS_FAIL="fail";
    //状态-失败结束
    public final static String STATUS_FAILEND="failend";

    @JsonCreator
    public SysHttpTask() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //类型
    @TableField(value = "TYPE_",jdbcType=JdbcType.VARCHAR)
    private String type;
    //关联ID
    @TableField(value = "REL_ID_",jdbcType=JdbcType.VARCHAR)
    private String relId;
    //关联名称
    @TableField(value = "REL_NAME_",jdbcType=JdbcType.VARCHAR)
    private String relName;
    //类名
    @TableField(value = "BEAN_NAME_",jdbcType=JdbcType.VARCHAR)
    private String beanName;
    //方法名
    @TableField(value = "METHOD_",jdbcType=JdbcType.VARCHAR)
    private String method;
    //方法参数
    @TableField(value = "PARAMS_",jdbcType=JdbcType.VARCHAR)
    private String params;
    //状态
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status;
    //执行次数
    @TableField(value = "EXECUTE_TIMES_",jdbcType=JdbcType.NUMERIC)
    private Integer executeTimes;
    //重试次数
    @TableField(value = "MAX_ATTEMPTS_",jdbcType=JdbcType.NUMERIC)
    private Integer maxAttempts;
    //重试时间间隔
    @TableField(value = "DELAY_",jdbcType=JdbcType.NUMERIC)
    private Integer delay;
    //延迟倍率
    @TableField(value = "MULTIPLIER_",jdbcType=JdbcType.NUMERIC)
    private Double multiplier;



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



