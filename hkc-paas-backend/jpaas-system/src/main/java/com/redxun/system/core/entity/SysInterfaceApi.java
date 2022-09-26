
/**
 * <pre>
 *
 * 描述：接口API表实体类定义
 * 表:SYS_INTERFACE_API
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
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_INTERFACE_API")
public class SysInterfaceApi  extends BaseExtEntity<String> {

    public static final  String STATUS_DONE="DONE";
    public static final  String STATUS_UNDONE="UNDONE";

    public static final  String DATA_TYPE_FORM="form";
    public static final  String DATA_TYPE_JSON="json";
    public static final  String DATA_TYPE_XML="xml";

    public static final  String RETURN_TYPE_NONE="none";
    public static final  String RETURN_TYPE_JSON="json";
    public static final  String RETURN_TYPE_XML="xml";

    public static final  String JAVA_TYPE_NONE="none";
    public static final  String JAVA_TYPE_SCRIPT="script";
    public static final  String JAVA_TYPE_BEAN="bean";

    @JsonCreator
    public SysInterfaceApi() {
    }

    //接口ID
    @TableId(value = "API_ID_",type = IdType.INPUT)
	private String apiId;

    //接口名称
    @TableField(value = "API_NAME_")
    private String apiName;
    //分类ID
    @TableField(value = "CLASSIFICATION_ID_")
    private String classificationId;
    //项目ID
    @TableField(value = "PROJECT_ID_")
    private String projectId;
    //接口类型
    @TableField(value = "API_TYPE_")
    private String apiType;
    //接口路径
    @TableField(value = "API_PATH_")
    private String apiPath;
    //请求类型
    @TableField(value = "API_METHOD_")
    private String apiMethod;
    //是否记录日志
    @TableField(value = "IS_LOG_")
    private String isLog;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //请求路径参数
    @TableField(value = "API_PATH_PARAMS_")
    private String apiPathParams;
    //请求头参数
    @TableField(value = "API_HEADERS_")
    private String apiHeaders;
    //请求参数
    @TableField(value = "API_QUERY_")
    private String apiQuery;
    //请求体参数
    @TableField(value = "API_BODY_")
    private String apiBody;
    //请求体数据类型
    @TableField(value = "API_DATA_TYPE_")
    private String apiDataType;
    //返回数据类型
    @TableField(value = "API_RETURN_TYPE_")
    private String apiReturnType;
    //返回字段
    @TableField(value = "API_RETURN_FIELDS_")
    private String apiReturnFields;
    //数据处理类型
    @TableField(value = "JAVA_TYPE_")
    private String javaType;
    //JAVA脚本
    @TableField(value = "JAVA_CODE_")
    private String javaCode;
    //处理器BEAN
    @TableField(value = "JAVA_BEAN_")
    private String javaBean;
    //备注
    @TableField(value = "DESCRIPTION_")
    private String description;



    @Override
    public String getPkId() {
        return apiId;
    }

    @Override
    public void setPkId(String pkId) {
        this.apiId=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



