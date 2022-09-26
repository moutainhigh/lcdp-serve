package com.redxun.dto.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 系统第三方API DTO
 * @author hujun
 */
@Data
public class SysInterfaceApiDto extends BaseDto {
    //接口ID
    private String apiId;

    //接口名称
    private String apiName;
    //分类ID
    private String classificationId;
    //项目ID
    private String projectId;
    //接口类型
    private String apiType;
    //接口路径
    private String apiPath;
    //请求类型
    private String apiMethod;
    //是否记录日志
    private String isLog;
    //状态
    private String status;
    //请求路径参数
    private String apiPathParams;
    //请求头参数
    private String apiHeaders;
    //请求参数
    private String apiQuery;
    //请求体参数
    private String apiBody;
    //请求体数据类型
    private String apiDataType;
    //返回数据类型
    private String apiReturnType;
    //返回字段
    private String apiReturnFields;
    //数据处理类型
    private String javaType;
    //JAVA脚本
    private String javaCode;
    //处理器BEAN
    private String javaBean;
    //备注
    private String description;
}
