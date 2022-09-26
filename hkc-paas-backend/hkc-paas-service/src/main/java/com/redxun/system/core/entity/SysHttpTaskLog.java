
/**
 * <pre>
 *
 * 描述：接口调用日志表实体类定义
 * 表:SYS_HTTP_TASK_LOG
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-05-18 15:34:23
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
@TableName(value = "SYS_HTTP_TASK_LOG")
public class SysHttpTaskLog extends BaseExtEntity<String> {

    @JsonCreator
    public SysHttpTaskLog() {
    }

    //日志ID
    @TableId(value = "LOG_ID_",type = IdType.INPUT)
	private String logId;

    //任务ID
    @TableField(value = "TASK_ID_")
    private String taskId;
    //接口完整路径
    @TableField(value = "LOG_URL_")
    private String logUrl;
    //请求头数据
    @TableField(value = "LOG_HEADERS_")
    private String logHeaders;
    //请求路径数据
    @TableField(value = "LOG_QUERY_")
    private String logQuery;
    //请求体数据
    @TableField(value = "LOG_BODY_")
    private String logBody;
    //返回状态
    @TableField(value = "RESPONSE_STATE_")
    private String responseState;
    //返回数据
    @TableField(value = "RESPONSE_DATA_")
    private String responseData;
    //接口耗时
    @TableField(value = "TIME_CONSUMING_")
    private String timeConsuming;
    //错误信息
    @TableField(value = "ERROR_MESSAGE_")
    private String errorMessage;
    //日志结果
    @TableField(value = "RESULT_")
    private String result;

    @TableField(exist = false)
    private String apiName;

    @TableField(exist = false)
    private String projectName;

    @Override
    public String getPkId() {
        return logId;
    }

    @Override
    public void setPkId(String pkId) {
        this.logId=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



