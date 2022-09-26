package com.redxun.common.tool.constant;

/**
 * 全局公共常量
 *
 * @author yjy
 * @date 2018/10/29
 */
public interface CommonConstant {

    /**
     * UTF-8 字符集
     */
    String UTF8  = "UTF-8";

    /**
     * 项目版本号(banner使用)
     */
    String PROJECT_VERSION = "1.0.0";

    /**
     * token请求头名称
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * The access token issued by the authorization server. This value is REQUIRED.
     */
    String ACCESS_TOKEN = "access_token";

    String BEARER_TYPE = "Bearer";

    /**
     * 标签 header key
     */
    String HEADER_LABEL = "x-label";

    /**
     * 标签 header 分隔符
     */
    String HEADER_LABEL_SPLIT = ",";

    /**
     * 标签或 名称
     */
    String LABEL_OR = "labelOr";

    /**
     * 标签且 名称
     */
    String LABEL_AND = "labelAnd";

    /**
     * 权重key
     */
    String WEIGHT_KEY = "weight";

    /**
     * 删除
     */
    String STATUS_DEL = "1";

    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 目录
     */
    Integer CATALOG = -1;

    /**
     * 菜单
     */
    Integer MENU = 1;

    /**
     * 权限
     */
    Integer PERMISSION = 2;

    /**
     * 删除标记
     */
    String DEL_FLAG = "is_del";

    /**
     * 超级管理员用户名
     */
    String ADMIN_USER_NAME = "admin";

    /**
     * 公共日期格式
     */
    String MONTH_FORMAT = "yyyy-MM";
    String DATE_FORMAT = "yyyy-MM-dd";
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String SIMPLE_MONTH_FORMAT = "yyyyMM";
    String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";



    String LOCK_KEY_PREFIX = "LOCK_KEY:";

    /**
     * 租户id参数
     */
    String TENANT_ID_PARAM = "tenantId";


    /**
     * 日志链路追踪id信息头
     */
    String TRACE_ID_HEADER = "x-traceId-header";
    /**
     * 日志链路追踪id日志标志
     */
    String LOG_TRACE_ID = "traceId";
    /**
     * 负载均衡策略-版本号 信息头
     */
    String Z_L_T_VERSION = "j-p-a-a-s-version";


    String ROOT_ID="0";

    String TENANT_ID="TENANT_ID_";

    String COMPANY_ID="COMPANY_ID_";

    String DEVELOPER="developer";

    /**
     * 平台租户ID
     */
    String PLATFORM_TENANT_ID_VAL="1";

    /**
     * api缓存区域
     */
    String API_REGION="api";

    /**
     * api缓存KEY
     */
    String API_KEY = "apiMap_";

    /**
     * 当没有分公司的情况默认为0。
     */
    String COMPANY_ZERO="0";

    /**
     * 公司查询前缀
     */
    String COMPANY_PREFIX="COMPANY_PREFIX";
    /**
     * 租户查询前缀
     */
    String TENANT_PREFIX="TENANT_PREFIX";
    /**
     * 逻辑删除查询前缀
     */
    String DELETED_PREFIX="DELETED_PREFIX";


}
