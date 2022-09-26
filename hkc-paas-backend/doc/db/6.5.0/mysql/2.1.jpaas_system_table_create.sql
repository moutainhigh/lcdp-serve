use jpaas_system;
-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'increment id',
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
  `context_` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime NOT NULL COMMENT 'create datetime',
  `log_modified` datetime NOT NULL COMMENT 'modify datetime',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*==============================================================*/
/* Table: SYS_APP                                               */
/*==============================================================*/
CREATE TABLE SYS_APP
(
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    CLIENT_CODE_         VARCHAR(64) NOT NULL COMMENT '应用编码',
    CLIENT_NAME_         VARCHAR(80) NOT NULL COMMENT '应用名称',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    ICON_                VARCHAR(80) COMMENT 'APP图标',
    STATUS_              VARCHAR(20) COMMENT '状态',
    DESCP_               VARCHAR(256) COMMENT '描述',
    HOME_TYPE_           VARCHAR(40) COMMENT '首页类型',
    URL_TYPE_            VARCHAR(20) COMMENT 'URL类型',
    LAYOUT_              VARCHAR(20) COMMENT '布局',
    PARENT_MODULE_       VARCHAR(100) COMMENT '父组件',
    ICON_PIC_            VARCHAR(50) COMMENT '图标',
    HOME_URL_            VARCHAR(120) COMMENT '主页地址',
    SN_                  INT COMMENT '序号',
    PARAMS_              VARCHAR(200) COMMENT '菜单参数',
    AUTH_SETTING_        TEXT COMMENT '授权数据',
    SHARE_               VARCHAR(20) COMMENT '是否共享',
    FREE_                VARCHAR(20) DEFAULT 'N' COMMENT '是否付费（Y：是，N：否）',
    IS_AUTH_             VARCHAR(20) COMMENT '是否授权',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_TYPE_            SMALLINT DEFAULT 1 COMMENT '0:系统内置，1:用户开发，2:外部',
    BACK_COLOR_          VARCHAR(20) COMMENT '图标背景颜色',
    VERSION_             VARCHAR(20) COMMENT '当前版本号',
    COPYRIGHT_           VARCHAR(50) COMMENT '版权所有',
    PC_USE_              SMALLINT DEFAULT 1 COMMENT 'PC端可用',
    MOBILE_USE_          SMALLINT DEFAULT 1 COMMENT '手机端可用',
    MOBILE_HOME_         VARCHAR(100) COMMENT '手机端首页',
    PATH_                VARCHAR(50) COMMENT '访问路径',
    MENU_NAV_TYPE_       SMALLINT NOT NULL DEFAULT 0 COMMENT '菜单导航方式：0:内置,1:微前端',
    PRIMARY KEY (APP_ID_),
    UNIQUE KEY AK_IDX_SYSAPP_CODE (CLIENT_CODE_)
);

ALTER TABLE SYS_APP COMMENT '应用系统';

/*==============================================================*/
/* Table: SYS_APP_ACTION_LOG                                    */
/*==============================================================*/
CREATE TABLE SYS_APP_ACTION_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    TYPE_                INT NOT NULL COMMENT '1启动2停止3升级4卸载5导入6导出7发布8生成前端工程9生成后端工程',
    TITLE_               VARCHAR(50) DEFAULT NULL COMMENT '标题',
    CONTENT_             TEXT COMMENT '详细内容',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_APP_ACTION_LOG COMMENT '应用操作日志';

/*==============================================================*/
/* Table: SYS_APP_AUTH                                          */
/*==============================================================*/
CREATE TABLE SYS_APP_AUTH
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    METHOD_              VARCHAR(64) COMMENT '方法',
    URL_                 VARCHAR(64) COMMENT '接口路径',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_APP_AUTH COMMENT '应用授权表';

/*==============================================================*/
/* Table: SYS_APP_AUTH_MENU                                     */
/*==============================================================*/
CREATE TABLE SYS_APP_AUTH_MENU
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) DEFAULT NULL COMMENT '应用ID',
    MENU_ID_             VARCHAR(64) DEFAULT 'N' COMMENT '菜单ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租户Id',
    USER_ID_             VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
    ENABLE_              VARCHAR(10) DEFAULT 'Y' COMMENT '是否启用（Y：是，N：否）',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_),
    KEY IDX_SYS_APP_AUTH_MENU_CODE (APP_ID_)
);

ALTER TABLE SYS_APP_AUTH_MENU COMMENT '应用授权菜单表';

/*==============================================================*/
/* Table: SYS_APP_FAVORITES                                     */
/*==============================================================*/
CREATE TABLE SYS_APP_FAVORITES
(
    FAV_ID_              VARCHAR(64) NOT NULL COMMENT '记录ID',
    USER_ID_             VARCHAR(64) NOT NULL COMMENT '用户ID',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    IS_FAV_              SMALLINT NOT NULL DEFAULT 1 COMMENT '是否收藏（0取消收藏）',
    FAV_TIME_            DATE DEFAULT NULL COMMENT '收藏时间',
    LAST_USE_TIME_       DATE DEFAULT NULL COMMENT '最近使用时间',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATE DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATE DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (FAV_ID_),
    UNIQUE KEY IDX_SYS_APP_FAVORITES_UNIQUE (USER_ID_, APP_ID_)
);

ALTER TABLE SYS_APP_FAVORITES COMMENT '平台开发应用收藏夹';

/*==============================================================*/
/* Table: SYS_APP_INSTALL                                       */
/*==============================================================*/
CREATE TABLE SYS_APP_INSTALL
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) DEFAULT NULL COMMENT '应用ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租户ID',
    USER_ID_             VARCHAR(64) DEFAULT NULL COMMENT '用户ID',
    FREE_                VARCHAR(10) DEFAULT 'N' COMMENT '是否付费（Y：是，N：否）',
    ENABLE_              VARCHAR(10) DEFAULT 'Y' COMMENT '是否启用（Y：是，N：否）',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_),
    KEY IDX_SYS_APP_INSTALL_CODE (APP_ID_)
);

ALTER TABLE SYS_APP_INSTALL COMMENT '应用安装表';

/*==============================================================*/
/* Table: SYS_APP_LOG                                           */
/*==============================================================*/
CREATE TABLE SYS_APP_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_NAME_            VARCHAR(50) COMMENT '应用名称',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    METHOD_              VARCHAR(40) COMMENT '方法类型',
    URL_                 VARCHAR(255) COMMENT '接口路径',
    DURATION_            INT COMMENT '持续时间',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_APP_LOG COMMENT '应用日志';

/*==============================================================*/
/* Table: SYS_APP_RELATION                                      */
/*==============================================================*/
CREATE TABLE SYS_APP_RELATION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    RELATED_APP_ID_      VARCHAR(64) NOT NULL COMMENT '关联应用ID',
    STRONG_              INT NOT NULL DEFAULT 0 COMMENT '是否强依赖',
    CONTENT_             TEXT COMMENT '详细内容',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_),
    UNIQUE KEY AK_IDX_SYS_APP_RELATION (APP_ID_, RELATED_APP_ID_)
);

ALTER TABLE SYS_APP_RELATION COMMENT '应用关系';

/*==============================================================*/
/* Table: SYS_APP_RUN                                           */
/*==============================================================*/
CREATE TABLE SYS_APP_RUN
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    OS_                  VARCHAR(10) NOT NULL COMMENT '操作系统：linux/windows',
    FRONT_DEPLOY_PATH_   VARCHAR(150) DEFAULT NULL COMMENT '前端部署路径',
    BACK_DEPLOY_PATH_    VARCHAR(150) DEFAULT NULL COMMENT '后端部署路径',
    SERVICE_PORT_        INT DEFAULT NULL COMMENT '后端服务端口',
    RUN_FRONT_CMD_       VARCHAR(255) COMMENT '启动前端命令',
    RUN_BACK_CMD_        VARCHAR(255) DEFAULT NULL COMMENT '启动后端命令',
    FRONT_PID_           INT DEFAULT NULL COMMENT '前端进程ID',
    BACK_PID_            INT DEFAULT NULL COMMENT '后端进程ID',
    STOP_FRONT_CMD_      VARCHAR(150) DEFAULT NULL COMMENT '停止前端命令',
    STOP_BACK_CMD_       VARCHAR(150) COMMENT '停止后端命令',
    START_TIME_          DATETIME DEFAULT NULL COMMENT '最后启动时间',
    STOP_TIME_           DATETIME DEFAULT NULL COMMENT '最后停止时间',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_APP_RUN COMMENT '应用运行';

/*==============================================================*/
/* Table: SYS_APP_VERSION                                       */
/*==============================================================*/
CREATE TABLE SYS_APP_VERSION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    VERSION_             VARCHAR(20) NOT NULL COMMENT '版本号',
    NOTES_               TEXT COMMENT '发布说明',
    SORT_                INT DEFAULT 1 COMMENT '序号',
    LAST_VERSION_        VARCHAR(20) DEFAULT NULL COMMENT '上一版本',
    COMPLIANT_           INT DEFAULT 1 COMMENT '是否兼容上一版本：1兼容，0不兼容',
    UPGRADE_SCRIPT_      TEXT COMMENT '升级脚本',
    UNINSTALL_SCRIPT_    TEXT COMMENT '卸载脚本',
    STATUS_              VARCHAR(64) DEFAULT '1' COMMENT '1有效，0无效',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (ID_),
    UNIQUE KEY AK_IDX_SYS_APP_VERSION (APP_ID_, VERSION_)
);

ALTER TABLE SYS_APP_VERSION COMMENT '应用版本';

/*==============================================================*/
/* Table: SYS_AUTH_MANAGER                                      */
/*==============================================================*/
CREATE TABLE SYS_AUTH_MANAGER
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    SECRET_              VARCHAR(100) COMMENT '密钥',
    IS_LOG_              VARCHAR(40) COMMENT '是否记录日志',
    ENABLE_              VARCHAR(40) COMMENT '是否启用',
    EXPIRE_              INT COMMENT '过期时间',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_AUTH_MANAGER COMMENT '客户端访问授权';

/*==============================================================*/
/* Table: SYS_AUTH_RIGHTS                                       */
/*==============================================================*/
CREATE TABLE SYS_AUTH_RIGHTS
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TREE_ID_             VARCHAR(2000) COMMENT '分类IDS',
    TREE_NAME_           VARCHAR(2000) COMMENT '分类名称',
    SETTING_ID_          VARCHAR(64) COMMENT '权限ID',
    RIGHT_JSON_          TEXT COMMENT '权限设置',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_AUTH_RIGHTS COMMENT '系统授权权限';

/*==============================================================*/
/* Table: SYS_AUTH_SETTING                                      */
/*==============================================================*/
CREATE TABLE SYS_AUTH_SETTING
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ENABLE_              VARCHAR(10) COMMENT '启用',
    TYPE_                VARCHAR(64) COMMENT '类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_AUTH_SETTING COMMENT '系统授权设置';

/*==============================================================*/
/* Table: SYS_DIC                                               */
/*==============================================================*/
CREATE TABLE SYS_DIC
(
    DIC_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    TREE_ID_             VARCHAR(64) COMMENT '分类Id',
    NAME_                VARCHAR(64) NOT NULL COMMENT '项名',
    VALUE_               VARCHAR(100) NOT NULL COMMENT '项值',
    DESCP_               VARCHAR(256) COMMENT '描述',
    SN_                  INT COMMENT '序号',
    PATH_                VARCHAR(256) COMMENT '路径',
    PARENT_ID_           VARCHAR(64) COMMENT '父ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (DIC_ID_)
);

ALTER TABLE SYS_DIC COMMENT '数据字典';

/*==============================================================*/
/* Table: SYS_ERROR_LOG                                         */
/*==============================================================*/
CREATE TABLE SYS_ERROR_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TRACE_ID_            VARCHAR(64) COMMENT '跟踪ID',
    APP_NAME_            VARCHAR(64) COMMENT '应用名称',
    URL_                 VARCHAR(255) COMMENT '访问地址',
    CONTENT_             TEXT COMMENT '错误内容',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_ERROR_LOG COMMENT '错误日志';

/*==============================================================*/
/* Table: SYS_EXCEL                                             */
/*==============================================================*/
CREATE TABLE SYS_EXCEL
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KEY_                 VARCHAR(64) COMMENT '别名',
    TEMPLATE_ID_         VARCHAR(64) COMMENT '模板ID',
    COMMENT_             VARCHAR(255) COMMENT '备注',
    GRID_DATA_           TEXT COMMENT '映射表内容',
    FIELD_               TEXT COMMENT 'Excel表内容',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_EXCEL COMMENT 'EXCEL导入';

/*==============================================================*/
/* Table: SYS_EXCEL_BATMANAGE                                   */
/*==============================================================*/
CREATE TABLE SYS_EXCEL_BATMANAGE
(
    ID_                  VARCHAR(64) COMMENT '主键',
    TABLE_               VARCHAR(64) COMMENT '表名',
    DS_ALIAS_            VARCHAR(64) COMMENT '列名',
    TEMPLATE_ID_         VARCHAR(64) COMMENT '模板ID',
    BAT_ID_              VARCHAR(64) COMMENT '批次ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间'
);

ALTER TABLE SYS_EXCEL_BATMANAGE COMMENT 'EXCEL导入批次管理';

/*==============================================================*/
/* Table: SYS_EXCEL_LOG                                         */
/*==============================================================*/
CREATE TABLE SYS_EXCEL_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TEMPLATED_           VARCHAR(64) COMMENT '模板ID',
    LOG_                 TEXT COMMENT '日志',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_EXCEL_LOG COMMENT '导出错误日志';

/*==============================================================*/
/* Table: SYS_FILE                                              */
/*==============================================================*/
CREATE TABLE SYS_FILE
(
    FILE_ID_             VARCHAR(64) NOT NULL COMMENT '主键',
    TYPE_ID_             VARCHAR(64) COMMENT '分类Id',
    FILE_NAME_           VARCHAR(200) COMMENT '文件名',
    NEW_FNAME_           VARCHAR(200) COMMENT '新文件名',
    PATH_                VARCHAR(255) COMMENT '路径',
    THUMBNAIL_           VARCHAR(255) COMMENT '缩略图',
    EXT_                 VARCHAR(32) COMMENT '扩展名',
    MINE_TYPE_           VARCHAR(50) COMMENT '附件类型',
    DESC_                VARCHAR(255) COMMENT '说明',
    TOTAL_BYTES_         INT COMMENT '总字节数',
    DEL_STATUS_          VARCHAR(20) COMMENT '删除状态',
    FROM_                VARCHAR(20) COMMENT '来源',
    COVER_STATUS_        VARCHAR(20) COMMENT '转换状态（PDF)',
    FILE_SYSTEM_         VARCHAR(20) COMMENT '文件系统',
    PDF_PATH_            VARCHAR(255) COMMENT 'PDF文件路径',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (FILE_ID_)
);

ALTER TABLE SYS_FILE COMMENT '系统文件';

/*==============================================================*/
/* Table: SYS_INFORM                                            */
/*==============================================================*/
CREATE TABLE SYS_INFORM
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    DESCP_               VARCHAR(255) COMMENT '描述',
    TREE_ID_             VARCHAR(64) COMMENT '分类',
    ACTION_              VARCHAR(64) COMMENT '审批动作',
    TEXT_TEMPLATE_       VARCHAR(2000) COMMENT '纯文本模板',
    RICH_TEXT_TEMPLATE   TEXT COMMENT '富文本框',
    BO_DEF_ID_           VARCHAR(64) COMMENT '业务模型ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_INFORM COMMENT '通知模板表';

/*==============================================================*/
/* Table: SYS_INFORM_PDF                                        */
/*==============================================================*/
CREATE TABLE SYS_INFORM_PDF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KEY_                 VARCHAR(40) COMMENT '标识',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    BO_DEF_ID_           VARCHAR(64) COMMENT '业务模型ID',
    PDF_HTML_            TEXT COMMENT '表单模板',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_INFORM_PDF COMMENT '通知表单模板表';

/*==============================================================*/
/* Table: SYS_INTERFACE_API                                     */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_API
(
    API_ID_              VARCHAR(64) NOT NULL COMMENT '接口ID',
    API_NAME_            VARCHAR(64) DEFAULT NULL COMMENT '接口名称',
    CLASSIFICATION_ID_   VARCHAR(64) DEFAULT NULL COMMENT '分类ID',
    PROJECT_ID_          VARCHAR(64) DEFAULT NULL COMMENT '项目ID',
    API_TYPE_            VARCHAR(10) DEFAULT NULL COMMENT '接口类型',
    API_PATH_            VARCHAR(120) DEFAULT NULL COMMENT '接口路径',
    API_METHOD_          VARCHAR(10) DEFAULT NULL COMMENT '请求类型',
    STATUS_              VARCHAR(10) DEFAULT NULL COMMENT '状态',
    API_PATH_PARAMS_     TEXT COMMENT '请求路径参数',
    API_HEADERS_         TEXT COMMENT '请求头参数',
    API_QUERY_           TEXT COMMENT '请求参数',
    API_BODY_            TEXT COMMENT '请求体参数',
    API_DATA_TYPE_       VARCHAR(10) DEFAULT NULL COMMENT '请求体数据类型',
    API_RETURN_TYPE_     VARCHAR(10) DEFAULT NULL COMMENT '返回数据类型',
    API_RETURN_FIELDS_   TEXT COMMENT '返回字段',
    JAVA_TYPE_           VARCHAR(10) DEFAULT NULL COMMENT '数据处理类型',
    JAVA_CODE_           TEXT COMMENT 'JAVA脚本',
    JAVA_BEAN_           VARCHAR(64) DEFAULT NULL COMMENT '处理器BEAN',
    DESCRIPTION_         VARCHAR(255) DEFAULT NULL COMMENT '备注',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    IS_LOG_              VARCHAR(10) COMMENT '是否记录日志',
    PRIMARY KEY (API_ID_)
);

ALTER TABLE SYS_INTERFACE_API COMMENT '接口API表';

/*==============================================================*/
/* Table: SYS_INTERFACE_CALL_LOGS                               */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_CALL_LOGS
(
    LOG_ID_              VARCHAR(64) NOT NULL COMMENT '日志ID',
    INTERFACE_ID_        VARCHAR(64) DEFAULT NULL COMMENT '接口ID',
    LOG_URL_             VARCHAR(120) DEFAULT NULL COMMENT '接口完整路径',
    LOG_HEADERS_         TEXT COMMENT '请求头数据',
    LOG_QUERY_           TEXT COMMENT '请求路径数据',
    LOG_BODY_            TEXT COMMENT '请求体数据',
    RESPONSE_STATE_      VARCHAR(10) DEFAULT NULL COMMENT '返回状态',
    RESPONSE_DATA_       TEXT COMMENT '返回数据',
    TIME_CONSUMING_      VARCHAR(64) DEFAULT NULL COMMENT '接口耗时',
    ERROR_MESSAGE_       TEXT COMMENT '错误信息',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (LOG_ID_)
);

ALTER TABLE SYS_INTERFACE_CALL_LOGS COMMENT '接口调用日志表';

/*==============================================================*/
/* Table: SYS_INTERFACE_CLASS                                   */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_CLASS
(
    CLASSIFICATION_ID_   VARCHAR(64) NOT NULL COMMENT '分类ID',
    CLASSIFICATION_NAME_ VARCHAR(64) DEFAULT NULL COMMENT '分类名称',
    DESCRIPTION_         VARCHAR(255) DEFAULT NULL COMMENT '描述',
    PROJECT_ID_          VARCHAR(64) DEFAULT NULL COMMENT '项目ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (CLASSIFICATION_ID_)
);

ALTER TABLE SYS_INTERFACE_CLASS COMMENT '接口分类表';

/*==============================================================*/
/* Table: SYS_INTERFACE_PROJECT                                 */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_PROJECT
(
    PROJECT_ID_          VARCHAR(64) NOT NULL COMMENT '项目ID',
    PROJECT_ALIAS_       VARCHAR(64) DEFAULT NULL COMMENT '项目别名',
    PROJECT_NAME_        VARCHAR(64) DEFAULT NULL COMMENT '项目名称',
    TREE_ID_             VARCHAR(64) DEFAULT NULL COMMENT '分类ID',
    DESCRIPTION_         VARCHAR(255) DEFAULT NULL COMMENT '描述',
    DOMAIN_TCP_          VARCHAR(10) DEFAULT NULL COMMENT '接口通讯协议',
    DOMAIN_PATH_         VARCHAR(120) DEFAULT NULL COMMENT '接口域名路径',
    BASE_PATH_           VARCHAR(64) DEFAULT NULL COMMENT '接口基本路径',
    STATUS_              VARCHAR(10) DEFAULT NULL COMMENT '项目状态',
    GLOBAL_HEADERS_      TEXT COMMENT '全局请求头',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (PROJECT_ID_)
);

ALTER TABLE SYS_INTERFACE_PROJECT COMMENT '接口项目表';

/*==============================================================*/
/* Table: SYS_INVOKE_SCRIPT                                     */
/*==============================================================*/
CREATE TABLE SYS_INVOKE_SCRIPT
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(20) COMMENT '别名',
    PARAMS_              VARCHAR(400) COMMENT '参数定义',
    CONTENT_             TEXT COMMENT '脚本定义',
    DESCP_               VARCHAR(255) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_INVOKE_SCRIPT COMMENT '表单调用脚本';

/*==============================================================*/
/* Table: SYS_JOB                                               */
/*==============================================================*/
CREATE TABLE SYS_JOB
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    JOB_TASK_            VARCHAR(64) COMMENT 'JOB任务ID',
    JOB_TASK_ID_         VARCHAR(64) COMMENT 'JOB任务ID',
    STRATEGY_            VARCHAR(255) NOT NULL COMMENT '策略',
    STATUS_              INT COMMENT '状态(0为禁用,1为启用)',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_JOB COMMENT '系统定时任务';

/*==============================================================*/
/* Table: SYS_JOB_TASK                                          */
/*==============================================================*/
CREATE TABLE SYS_JOB_TASK
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    TYPE_                VARCHAR(40) COMMENT '类型 (JOB,SCRIPT,CLASS)',
    CONTENT_             TEXT COMMENT '配置内容',
    STATUS_              VARCHAR(40) COMMENT '状态',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_JOB_TASK COMMENT '定时任务定义';

/*==============================================================*/
/* Table: SYS_KETTLE_DBDEF                                      */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_DBDEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    RES_USER_            VARCHAR(64) COMMENT '资源库用户',
    RES_PWD_             VARCHAR(64) COMMENT '资源库密码',
    DB_TYPE_             VARCHAR(20) COMMENT '数据库类型',
    HOST_                VARCHAR(64) COMMENT '主机地址',
    PORT_                VARCHAR(20) COMMENT '端口号',
    DATABASE_            VARCHAR(20) COMMENT '数据库名',
    USER_                VARCHAR(40) COMMENT '用户名',
    PASSWORD_            VARCHAR(64) COMMENT '密码',
    COMMENT_             VARCHAR(500) COMMENT '备注',
    STATUS_              INT COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_KETTLE_DBDEF COMMENT 'KETTLE资源库定义';

/*==============================================================*/
/* Table: SYS_KETTLE_DEF                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    GATEGORY_            VARCHAR(64) COMMENT '分类',
    TYPE_                VARCHAR(40) COMMENT '类型(job,trans)',
    STORE_TYPE_          VARCHAR(40) COMMENT '存储类型(文件:file，资源库:resource)',
    PARAMETERS_          VARCHAR(500) COMMENT '变量配置',
    STORE_SETTING_       VARCHAR(400) COMMENT '存储设定',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_KETTLE_DEF COMMENT 'KETTLE定义';

/*==============================================================*/
/* Table: SYS_KETTLE_JOB                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_JOB
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KETTLE_ID_           VARCHAR(64) COMMENT 'KETTLE定义ID',
    STRATEGY_            VARCHAR(255) NOT NULL COMMENT '策略',
    LOGLEVEL_            VARCHAR(64) COMMENT '日志级别',
    STATUS_              INT COMMENT '状态(0为禁用,1为启用)',
    REMARK_              VARCHAR(255) COMMENT '备注',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_KETTLE_JOB COMMENT 'KETTLE任务';

/*==============================================================*/
/* Table: SYS_KETTLE_LOG                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    KETTLE_ID_           VARCHAR(64) COMMENT 'KETTLE定义ID',
    DRUATION_            INT COMMENT '运行时长',
    STATUS_              INT COMMENT '状态(1.成功,0失败)',
    LOG_                 TEXT COMMENT '日志',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    KETTLE_JOB_ID_       VARCHAR(64) COMMENT '任务ID',
    KETTLE_JOB_NAME_     VARCHAR(64) COMMENT '任务名称',
    KETTLE_TYPE_         VARCHAR(64) COMMENT 'Kettle类型',
    START_TIME_          DATETIME COMMENT '任务开始时间',
    END_TIME_            DATETIME COMMENT '任务结束时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_KETTLE_LOG COMMENT 'KETTLE日志';

/*==============================================================*/
/* Table: SYS_LOG                                               */
/*==============================================================*/
CREATE TABLE SYS_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    APP_NAME_            VARCHAR(64) COMMENT '应用名称',
    MODULE_              VARCHAR(64) COMMENT '一级模块',
    SUB_MODULE_          VARCHAR(64) COMMENT '子模块',
    CLASS_NAME_          VARCHAR(128) COMMENT '类名',
    METHOD_NAME_         VARCHAR(64) COMMENT '方法名',
    ACTION_              VARCHAR(64) COMMENT '动作',
    PK_VALUE_            VARCHAR(64) COMMENT '业务主键',
    IP_                  VARCHAR(40) COMMENT '访问IP',
    DETAIL_              TEXT COMMENT '日志明细',
    OPERATION_           VARCHAR(64) COMMENT '操作',
    USER_NAME_           VARCHAR(40) COMMENT '操作用户',
    DURATION_            INT COMMENT '操作时长',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    BUS_TYPE_            VARCHAR(32) COMMENT '业务类型',
    IS_RESUME_           VARCHAR(4) COMMENT '是否已恢复',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_LOG COMMENT '系统日志';

/*==============================================================*/
/* Table: SYS_MENU                                              */
/*==============================================================*/
CREATE TABLE SYS_MENU
(
    MENU_ID_             VARCHAR(64) NOT NULL COMMENT '菜单ID',
    APP_ID_              VARCHAR(64) COMMENT '所属子系统',
    NAME_                VARCHAR(60) NOT NULL COMMENT '菜单名称',
    ICON_PC_             VARCHAR(100) COMMENT 'PC图标样式',
    ICON_PIC_            VARCHAR(100) COMMENT '图标',
    ICON_APP_            VARCHAR(50) COMMENT 'APP图标样式',
    PARENT_ID_           VARCHAR(64) NOT NULL COMMENT '上级父ID',
    PATH_                VARCHAR(256) COMMENT '路径',
    SN_                  INT COMMENT '序号',
    SHOW_TYPE_           VARCHAR(20) COMMENT '访问方式',
    MENU_KEY_            VARCHAR(50) COMMENT '菜单唯一标识',
    MENU_TYPE_           VARCHAR(20) COMMENT '菜单类型',
    COMPONENT_           VARCHAR(255) COMMENT '展示组件',
    SETTING_TYPE_        VARCHAR(20) COMMENT '配置类型(custom,iframe)',
    BO_LIST_KEY_         VARCHAR(64) COMMENT '单据列表KEY',
    URL_                 VARCHAR(128) COMMENT '接口地址',
    METHOD_              VARCHAR(20) COMMENT '接口方法',
    PARAMS_              VARCHAR(1000) COMMENT '菜单参数',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (MENU_ID_)
);

ALTER TABLE SYS_MENU COMMENT '系统菜单';

/*==============================================================*/
/* Table: SYS_MENU_RELEASE                                      */
/*==============================================================*/
CREATE TABLE SYS_MENU_RELEASE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    RELEASE_ID_          VARCHAR(64) COMMENT 'BO列表ID',
    MENU_ID_             VARCHAR(64) COMMENT '菜单ID',
    MENU_NAME_           VARCHAR(64) COMMENT '菜单名称',
    RELEASE_URL_         VARCHAR(100) COMMENT '发布路径',
    MENU_URL_            VARCHAR(100) COMMENT '当前路径',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_MENU_RELEASE COMMENT '发布菜单路径记录表';

/*==============================================================*/
/* Table: SYS_OFFICE                                            */
/*==============================================================*/
CREATE TABLE SYS_OFFICE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    SUPPORT_VERSION_     VARCHAR(64) COMMENT '是否支持版本',
    VERSION_             INT COMMENT '版本',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_OFFICE COMMENT 'office表';

/*==============================================================*/
/* Table: SYS_OFFICE_TEMPLATE                                   */
/*==============================================================*/
CREATE TABLE SYS_OFFICE_TEMPLATE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(200) COMMENT '名称',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    DOC_NAME_            VARCHAR(255) COMMENT '文件名',
    DESCRIPTION_         VARCHAR(255) COMMENT '描述',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    TYPE_                VARCHAR(20) COMMENT '类型(normal,red)',
    DOC_ID_              VARCHAR(64) COMMENT '文档ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_OFFICE_TEMPLATE COMMENT 'office模板表';

/*==============================================================*/
/* Table: SYS_OFFICE_VER                                        */
/*==============================================================*/
CREATE TABLE SYS_OFFICE_VER
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    OFFICE_ID_           VARCHAR(64) DEFAULT NULL COMMENT 'OFFICE主键',
    VERSION_             INT DEFAULT NULL COMMENT '版本',
    FILE_ID_             VARCHAR(64) DEFAULT NULL COMMENT '附件ID',
    FILE_NAME_           VARCHAR(255) DEFAULT NULL COMMENT '文件名',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_OFFICE_VER COMMENT 'office版本表';

/*==============================================================*/
/* Table: SYS_PROPERTIES                                        */
/*==============================================================*/
CREATE TABLE SYS_PROPERTIES
(
    PRO_ID_              VARCHAR(64) NOT NULL COMMENT '属性ID',
    NAME_                VARCHAR(64) NOT NULL COMMENT '名称',
    ALIAS_               VARCHAR(64) NOT NULL COMMENT '别名',
    GLOBAL_              VARCHAR(64) COMMENT '是否全局',
    ENCRYPT_             VARCHAR(64) COMMENT '是否加密',
    VALUE_               VARCHAR(2000) COMMENT '属性值',
    CATEGORY_            VARCHAR(100) COMMENT '分类',
    DESCRIPTION_         VARCHAR(200) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (PRO_ID_)
);

ALTER TABLE SYS_PROPERTIES COMMENT '系统属性表';

/*==============================================================*/
/* Table: SYS_ROUTE_TYPE                                        */
/*==============================================================*/
CREATE TABLE SYS_ROUTE_TYPE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ROUTE_TYPE_NAME_     VARCHAR(64) COMMENT '类型',
    DESCRIBE_            VARCHAR(255) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_ROUTE_TYPE COMMENT '动态路由配置';

/*==============================================================*/
/* Table: SYS_ROUTING                                           */
/*==============================================================*/
CREATE TABLE SYS_ROUTING
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ROUTING_NAME_        VARCHAR(128) COMMENT '路由名称',
    ROUTE_TYPE_          VARCHAR(64) COMMENT '路由类型',
    CONDITION_           VARCHAR(100) COMMENT '条件',
    CONDITION_PARAMETERS_ VARCHAR(1000) COMMENT '条件参数',
    FILTER_              VARCHAR(1000) COMMENT '过滤器',
    FILTER_PARAMETERS_   VARCHAR(1000) COMMENT '过滤器参数',
    URI_                 VARCHAR(255) COMMENT '目标URL',
    REMARK_              VARCHAR(512) COMMENT '备注',
    STATUS_              VARCHAR(20) COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_ROUTING COMMENT '网关路由';

/*==============================================================*/
/* Table: SYS_SEQ_ID                                            */
/*==============================================================*/
CREATE TABLE SYS_SEQ_ID
(
    SEQ_ID_              VARCHAR(64) NOT NULL COMMENT '流水号ID',
    NAME_                VARCHAR(80) NOT NULL COMMENT '名称',
    ALIAS_               VARCHAR(50) COMMENT '别名',
    CUR_DATE_            DATETIME COMMENT '当前日期',
    RULE_                VARCHAR(100) NOT NULL COMMENT '规则',
    RULE_CONF_           VARCHAR(512) COMMENT '规则配置',
    INIT_VAL_            INT COMMENT '初始值',
    GEN_TYPE_            VARCHAR(20) COMMENT '生成方式
            DAY=每天
            WEEK=每周
            MONTH=每月
            YEAR=每年
            AUTO=一直增长',
    LEN_                 INT COMMENT '流水号长度',
    CUR_VAL              INT COMMENT '当前值',
    STEP_                INT COMMENT '步长',
    MEMO_                VARCHAR(512) COMMENT '备注',
    IS_DEFAULT_          VARCHAR(20) COMMENT '系统缺省
            YES
            NO',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    SYS_ID_              VARCHAR(64),
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID_',
    PRIMARY KEY (SEQ_ID_)
);

ALTER TABLE SYS_SEQ_ID COMMENT '系统流水号';

/*==============================================================*/
/* Table: SYS_SIGNATURE                                         */
/*==============================================================*/
CREATE TABLE SYS_SIGNATURE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    FILE_ID_             VARCHAR(64) COMMENT '文件ID',
    FILE_NAME_           VARCHAR(50) COMMENT '图片名称',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_SIGNATURE COMMENT '签名';

/*==============================================================*/
/* Table: SYS_TRANSFER_LOG                                      */
/*==============================================================*/
CREATE TABLE SYS_TRANSFER_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    OP_DESCP_            VARCHAR(2000) COMMENT '操作描述',
    AUTHOR_PERSON_       VARCHAR(64) COMMENT '权限转移人',
    TARGET_PERSON_       VARCHAR(64) COMMENT '目标转移人',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_TRANSFER_LOG COMMENT '权限转移日志表';

/*==============================================================*/
/* Table: SYS_TRANSFER_SETTING                                  */
/*==============================================================*/
CREATE TABLE SYS_TRANSFER_SETTING
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    STATUS_              VARCHAR(64) COMMENT '状态',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    ID_FIELD_            VARCHAR(64) COMMENT 'ID字段',
    NAME_FIELD_          VARCHAR(64) COMMENT '名称字段',
    SELECT_SQL_          VARCHAR(1000) COMMENT 'SELECTSQL语句',
    UPDATE_SQL_          VARCHAR(1000) COMMENT 'UPDATESQL语句',
    LOG_TEMPLET_         VARCHAR(1000) COMMENT '日志内容模板',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_TRANSFER_SETTING COMMENT '权限转移设置表';

/*==============================================================*/
/* Table: SYS_TREE                                              */
/*==============================================================*/
CREATE TABLE SYS_TREE
(
    TREE_ID_             VARCHAR(64) NOT NULL COMMENT '主键',
    CODE_                VARCHAR(50) COMMENT '同级编码',
    NAME_                VARCHAR(128) NOT NULL COMMENT '名称',
    PATH_                VARCHAR(1024) COMMENT '路径',
    PARENT_ID_           VARCHAR(64) COMMENT '父节点',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    DESCP_               VARCHAR(512) COMMENT '描述',
    CAT_KEY_             VARCHAR(64) NOT NULL COMMENT '树分类key',
    SN_                  INT NOT NULL COMMENT '序号',
    DATA_SHOW_TYPE_      VARCHAR(20) DEFAULT 'FLAT' COMMENT '展示类型
            默认为:
            FLAT=平铺类型
            TREE=树类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (TREE_ID_)
);

ALTER TABLE SYS_TREE COMMENT '系统分类树
用于显示树层次结构的分类
可以允许任何层次结构';

/*==============================================================*/
/* Table: SYS_TREE_CAT                                          */
/*==============================================================*/
CREATE TABLE SYS_TREE_CAT
(
    CAT_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    KEY_                 VARCHAR(64) NOT NULL COMMENT '分类Key',
    NAME_                VARCHAR(64) NOT NULL COMMENT '分类名称',
    SN_                  INT COMMENT '序号',
    DESCP_               VARCHAR(512) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (CAT_ID_)
);

ALTER TABLE SYS_TREE_CAT COMMENT '系统树分类类型';

/*==============================================================*/
/* Table: SYS_WEBREQ_DEF                                        */
/*==============================================================*/
CREATE TABLE SYS_WEBREQ_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(200) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT 'KEY_',
    URL_                 VARCHAR(200) COMMENT '请求地址',
    MODE_                VARCHAR(20) COMMENT '请求方式',
    TYPE_                VARCHAR(20) COMMENT '请求类型',
    DATA_TYPE_           VARCHAR(64) COMMENT '数据类型',
    PARAMS_SET_          VARCHAR(400) COMMENT '参数配置',
    DATA_                TEXT COMMENT '传递数据',
    TEMP_                TEXT COMMENT '请求报文模板',
    STATUS_              VARCHAR(20) COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_WEBREQ_DEF COMMENT 'WEB请求调用定义';

/*==============================================================*/
/* Table: SYS_WORD_TEMPLATE                                     */
/*==============================================================*/
CREATE TABLE SYS_WORD_TEMPLATE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    TYPE_                VARCHAR(40) COMMENT '数据来源类(BO/SQL)',
    BO_DEF_ID_           VARCHAR(64) COMMENT 'BO定义ID',
    BO_DEF_NAME_         VARCHAR(64) COMMENT 'BO定义名称',
    SETTING_             VARCHAR(4000) COMMENT 'SQL设置',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    DS_NAME_             VARCHAR(64) COMMENT '数据源名称',
    TEMPLATE_ID_         VARCHAR(64) COMMENT '模板ID',
    TEMPLATE_NAME_       VARCHAR(64) COMMENT '模板名称',
    DESCRIPTION_         VARCHAR(255) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE SYS_WORD_TEMPLATE COMMENT 'WORD模板';

/*==============================================================*/
/* Table: sys_app_manager                                     */
/*==============================================================*/
CREATE TABLE jpaas_system.sys_app_manager (
                                              ID_ varchar(64) NOT NULL COMMENT '主键',
                                              APP_ID_ varchar(64) NOT NULL COMMENT '应用ID',
                                              AUTH_TYPE_ smallint(6) NOT NULL DEFAULT '1' COMMENT '1开发权限；2管理权限',
                                              IS_GROUP_ smallint(6) DEFAULT '0' COMMENT '是否用户组',
                                              USER_OR_GROUP_ID_ varchar(64) DEFAULT NULL COMMENT '用户或组ID',
                                              USER_OR_GROUP_NAME_ varchar(64) DEFAULT NULL COMMENT '用户或组名',
                                              TENANT_ID_ varchar(64) DEFAULT NULL COMMENT '租户ID',
                                              CREATE_DEP_ID_ varchar(64) DEFAULT NULL COMMENT '创建部门ID',
                                              CREATE_BY_ varchar(64) DEFAULT NULL COMMENT '创建人ID',
                                              CREATE_TIME_ datetime DEFAULT NULL COMMENT '创建时间',
                                              UPDATE_TIME_ datetime DEFAULT NULL COMMENT '更新时间',
                                              UPDATE_BY_ varchar(64) DEFAULT NULL COMMENT '更新人ID',
                                              PRIMARY KEY (ID_)
) ENGINE=InnoDB COMMENT='平台开发应用授权管理';

COMMIT ;
