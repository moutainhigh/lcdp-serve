use jpaas_form;
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
/* Table: FORM_BO_ATTR                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_ATTR
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ENT_ID_              VARCHAR(64) COMMENT '实体ID',
    NAME_                VARCHAR(64) COMMENT '名称',
    FIELD_NAME_          VARCHAR(64) COMMENT '字段名',
    COMMENT_             VARCHAR(64) COMMENT '备注',
    DATA_TYPE_           VARCHAR(20) COMMENT '数据类型',
    LENGTH_              INT COMMENT '长度',
    DECIMAL_LENGTH_      INT COMMENT '小数位',
    CONTROL_             VARCHAR(20) COMMENT '控件类型',
    EXT_JSON_            VARCHAR(4000) COMMENT '扩展JSON',
    SN_                  INT COMMENT '序号',
    IS_SINGLE_           INT COMMENT '是否单字段',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    IS_PK_               INT COMMENT '是否主键',
    IS_NOT_NULL_         INT COMMENT '是否非空',
    DB_FIELD_TYPE_       VARCHAR(128) COMMENT '数据库字段类型',
    SPANS_               INT COMMENT '占列',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_ATTR COMMENT '业务实体属性';

/*==============================================================*/
/* Table: FORM_BO_DEF                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    SUPPORT_DB_          INT COMMENT '支持数据库',
    DESCRIPTION_         VARCHAR(255) COMMENT '说明',
    GEN_TYPE_            VARCHAR(20) COMMENT '生成类型(FORM,DIRECT)',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_DEF COMMENT '业务模型';

/*==============================================================*/
/* Table: FORM_BO_ENTITY                                        */
/*==============================================================*/
CREATE TABLE FORM_BO_ENTITY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    GEN_MODE_            VARCHAR(20) COMMENT '生成数据库',
    IS_MAIN_             INT COMMENT '主实体',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    ID_FIELD_            VARCHAR(64) COMMENT '主键字段',
    PARENT_FIELD_        VARCHAR(64) COMMENT '父ID',
    GENDB_               VARCHAR(40) COMMENT '生成数据库',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    DS_NAME_             VARCHAR(64) COMMENT '数据源名称',
    VERSION_FIELD_       VARCHAR(40) COMMENT '版本字段',
    TABLE_NAME_          VARCHAR(64) COMMENT '表名',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_ENTITY COMMENT '业务实体';

/*==============================================================*/
/* Table: FORM_BO_LIST                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '列表名称',
    KEY_                 VARCHAR(64) COMMENT '别名',
    DESCP_               VARCHAR(255) COMMENT '描述',
    ID_FIELD_            VARCHAR(64) COMMENT '主键字段',
    TEXT_FIELD_          VARCHAR(64) COMMENT '显示字段(树)',
    PARENT_FIELD_        VARCHAR(64) COMMENT '父ID(树)',
    IS_TREE_DLG_         VARCHAR(40) COMMENT '是否树对话框',
    ONLY_SEL_LEAF_       VARCHAR(40) COMMENT '仅可选择树节点',
    URL_                 VARCHAR(255) COMMENT '数据地址',
    MULTI_SELECT_        VARCHAR(40) COMMENT '是否多选择',
    IS_LEFT_TREE_        VARCHAR(40) COMMENT '是否显示左树',
    LEFT_NAV_            VARCHAR(1000) COMMENT '左树SQL',
    LEFT_TREE_JSON_      VARCHAR(300) COMMENT '左树字段映射',
    SQL_                 VARCHAR(2000) COMMENT 'SQL语句',
    USE_COND_SQL_        VARCHAR(20) COMMENT '列表数据来源类型',
    COND_SQLS_           VARCHAR(2000) COMMENT '条件脚本',
    DB_AS_               VARCHAR(64) COMMENT '数据源ID',
    FIELDS_JSON_         TEXT COMMENT '列字段JSON',
    COLS_JSON_           TEXT COMMENT '显示字段配置',
    LIST_HTML_           TEXT COMMENT '列表VUE模板',
    SEARCH_JSON_         TEXT COMMENT '搜索条件',
    BPM_SOL_ID_          VARCHAR(64) COMMENT '绑定流程方案',
    FORM_ALIAS_          VARCHAR(64) COMMENT '绑定表单方案',
    TOP_BTNS_JSON_       VARCHAR(2000) COMMENT '头部按钮配置',
    BODY_SCRIPT_         TEXT COMMENT '脚本JS',
    IS_DIALOG_           VARCHAR(40) COMMENT ' 是否对话框',
    IS_PAGE_             VARCHAR(40) COMMENT '是否分页',
    IS_LAZY_             VARCHAR(40) COMMENT '树形是否懒加载',
    IS_EXPORT_           VARCHAR(40) COMMENT '是否允许导出',
    HEIGHT_              INT COMMENT '对话框高度',
    WIDTH_               INT COMMENT '对话宽度',
    ENABLE_FLOW_         VARCHAR(40) COMMENT '是否启用流程',
    IS_GEN_              VARCHAR(40) COMMENT '是否已产生HTML',
    TREE_ID_             VARCHAR(64) COMMENT '分类树ID',
    DRAW_CELL_SCRIPT_    VARCHAR(128) COMMENT '脚本',
    MOBILE_HTML_         TEXT COMMENT '手机表单HTML模版',
    DATA_STYLE_          VARCHAR(40) COMMENT '数据风格',
    ROW_EDIT_            VARCHAR(40) COMMENT '行数据编辑',
    SHOW_SUMMARY_ROW_    VARCHAR(40) COMMENT '是否显示汇总行',
    IS_INIT_DATA_        VARCHAR(40) COMMENT '是否初始显示数据',
    FORM_DETAIL_ALIAS_   VARCHAR(64) COMMENT '表单方案别名',
    SOL_ID_              VARCHAR(64) COMMENT '解决方案ID',
    TEMPLATE_TYPE_       VARCHAR(64) COMMENT '模版类型',
    FORM_ADD_ALIAS_      VARCHAR(64) COMMENT '添加表单方案',
    WEBREQ_KEY_          VARCHAR(64) COMMENT '自定义查询',
    WEBREQ_SCRIPT_       TEXT COMMENT '请求脚本',
    WEBREQ_MAPPING_JSON_ TEXT COMMENT 'mapping映射',
    FORM_NAME_           VARCHAR(64) COMMENT '编辑表单名称',
    FORM_DETAIL_NAME_    VARCHAR(64) COMMENT '明细表单名称',
    PUBLISH_CONF_        VARCHAR(400) COMMENT '发布配置',
    FORM_ADD_NAME_       VARCHAR(64) COMMENT '添加表单名称',
    MOBILE_BUTTON        VARCHAR(2000) COMMENT '手机端按钮',
    MOBILE_JS            TEXT COMMENT '手机端JS函数',
    BUTTON_MAX           INT COMMENT '按钮最大显示数',
    IS_SEARCH_HEADER     VARCHAR(40) COMMENT '搜索是否展示头部',
    IS_CHECKBOX_PROPS    VARCHAR(40) COMMENT '是否选择框配置',
    EXPAND_LEVEL         VARCHAR(20) COMMENT '树形默认展开层数',
    EXCEL_CONF_JSON      TEXT COMMENT 'EXCEL配置JSON',
    IS_APPROVAL          VARCHAR(10) COMMENT '是否可以审批',
    PAGE_SIZE_           INT COMMENT '页大小',
    IS_EXPAND_ROW        VARCHAR(64) COMMENT '是否显示扩展页',
    EXPAND_ROW_JSON      VARCHAR(2000) COMMENT '扩展页JSON',
    IS_TEST_             VARCHAR(80) COMMENT '是否测试',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    IS_SHARE_            VARCHAR(40) COMMENT '是否共享',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    INTERFACE_NAME_      VARCHAR(64) COMMENT '第三方接口名称',
    INTERFACE_MAPPING_JSON_ TEXT COMMENT '第三方接口mapping映射',
    INTERFACE_KEY_       VARCHAR(64) COMMENT '第三方接口key',
    ROW_DBL_CLICK_       VARCHAR(80) DEFAULT 'rowDblClick' COMMENT '双击行时的方法名',
    ROW_CLICK_           VARCHAR(80) DEFAULT 'rowClick' COMMENT '单击行时的方法名',
    BUS_SOLUTION_        VARCHAR(64) COMMENT '业务方案',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_LIST COMMENT '自定义列表管理';

/*==============================================================*/
/* Table: FORM_BO_LIST_HISTORY                                  */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST_HISTORY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    LIST_ID_             VARCHAR(64) COMMENT '列表ID',
    CONTENT_             TEXT COMMENT '内容',
    LIST_KEY_ID_         VARCHAR(64) COMMENT '预览用ID',
    VERSION_             INT COMMENT '版本号',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    REMARK_              VARCHAR(255) COMMENT '备注',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_LIST_HISTORY COMMENT '列表变更历史表';

/*==============================================================*/
/* Table: FORM_BO_PMT                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_PMT
(
    PMT_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    ALIAS_               VARCHAR(64) DEFAULT NULL COMMENT '权限别名',
    NAME_                VARCHAR(64) DEFAULT NULL COMMENT '权限名称',
    BO_LIST_ID_          VARCHAR(64) DEFAULT NULL COMMENT '表单列表ID',
    BUTTONS_             TEXT COMMENT '按钮权限',
    DATAS_               TEXT COMMENT '数据权限',
    FIELDS_              TEXT COMMENT '字段权限',
    STATUS_              VARCHAR(64) DEFAULT NULL COMMENT '是否可用(YES,NO)',
    MENU_ID_             VARCHAR(64) DEFAULT NULL COMMENT '菜单ID',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (PMT_ID_)
);

ALTER TABLE FORM_BO_PMT COMMENT '业务单据权限';

/*==============================================================*/
/* Table: FORM_BO_RELATION                                      */
/*==============================================================*/
CREATE TABLE FORM_BO_RELATION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    BODEF_ID_            VARCHAR(64) COMMENT '业务模型定义',
    ENT_ID_              VARCHAR(64) COMMENT '实体ID',
    PARENT_ENT_ID_       VARCHAR(64) COMMENT '父实体ID',
    TYPE_                VARCHAR(20) COMMENT '关系类型(onetoone,onetomany)',
    IS_REF_              INT COMMENT '是否引用',
    PK_FIELD_            VARCHAR(40) COMMENT '主键关联字段',
    FK_FIELD_            VARCHAR(64) COMMENT '关联字段',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BO_RELATION COMMENT '业务实体';

/*==============================================================*/
/* Table: FORM_BUSINESS_SOLUTION                                */
/*==============================================================*/
CREATE TABLE FORM_BUSINESS_SOLUTION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    CATEGORY_            VARCHAR(64) COMMENT '分类',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '标识',
    NAVIGATION_POSITION_ VARCHAR(64) COMMENT '导航栏位置',
    MAIN_FORM_SOLUTION_  VARCHAR(64) COMMENT '主表单方案',
    FORM_SOLUTIONS_      VARCHAR(2000) COMMENT '表单方案配置',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BUSINESS_SOLUTION COMMENT '表单业务方案';

/*==============================================================*/
/* Table: FORM_BUS_API                                          */
/*==============================================================*/
CREATE TABLE FORM_BUS_API
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    NAME_                VARCHAR(64) COMMENT '服务名称',
    DEF_KEY_             VARCHAR(40) COMMENT '流程定义KEY',
    DEF_NAME_            VARCHAR(40) COMMENT '流程定义名称',
    FIELD_SETTING_       TEXT COMMENT '字段配置',
    STATUS_              VARCHAR(64) COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BUS_API COMMENT '表单业务接口';

/*==============================================================*/
/* Table: FORM_BUS_INST_DATA                                    */
/*==============================================================*/
CREATE TABLE FORM_BUS_INST_DATA
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    BUS_SOL_ID_          VARCHAR(64) COMMENT '表单业务方案主键',
    REL_FORMSOL_ID_      VARCHAR(64) COMMENT '表单方案主键',
    MAIN_PK_             VARCHAR(64) COMMENT '主表单主键',
    REL_PK_              VARCHAR(64) COMMENT '关联主键',
    STATUS_              VARCHAR(20) COMMENT '状态(1：生效 0：草稿)',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_BUS_INST_DATA COMMENT '表单业务数据';

/*==============================================================*/
/* Table: FORM_CHART_DATA_MODEL                                 */
/*==============================================================*/
CREATE TABLE FORM_CHART_DATA_MODEL
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(64) COMMENT '模型名称',
    DATA_SOURCE_         VARCHAR(64) COMMENT '数据源',
    TABLES_              VARCHAR(500) COMMENT '表配置',
    MODEL_CONFIG_        TEXT COMMENT '模型配置',
    TYPE_                VARCHAR(64) COMMENT '模式',
    SQL_MODE_            VARCHAR(2000) COMMENT '自定义SQL模型',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME NOT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_CHART_DATA_MODEL COMMENT '图表数据模型';

/*==============================================================*/
/* Table: FORM_CODEGEN_GLOBALVAR                                */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_GLOBALVAR
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    CONFIG_              VARCHAR(500) COMMENT '变量配置',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_CODEGEN_GLOBALVAR COMMENT '代码生成全局变量';

/*==============================================================*/
/* Table: FORM_CODEGEN_TEMPLATE                                 */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_TEMPLATE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    FILE_NAME_           VARCHAR(128) COMMENT '文件名称',
    PATH_                VARCHAR(128) COMMENT '生成路径',
    SINGLE_              VARCHAR(40) COMMENT '生成单个文件',
    ENABLED_             VARCHAR(40) COMMENT '是否允许',
    CONTENT_             TEXT COMMENT '模板内容',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_CODEGEN_TEMPLATE COMMENT '代码生成器模板';

/*==============================================================*/
/* Table: FORM_CUSTOM                                           */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    TYPE_                VARCHAR(40) COMMENT '类型(form,表单,other,其他)',
    JSON_                TEXT COMMENT '布局定义',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    HOME_PAGE_           SMALLINT DEFAULT 0 COMMENT '是否为首页（0：否，1：是）',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_CUSTOM COMMENT '表单布局定制';

/*==============================================================*/
/* Table: FORM_CUSTOM_QUERY                                     */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM_QUERY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KEY_                 VARCHAR(64) COMMENT '别名',
    TABLE_NAME_          VARCHAR(64) COMMENT '对象名称(表名或视图名)',
    IS_PAGE_             INT COMMENT '是否分页',
    PAGE_SIZE_           INT COMMENT '分页大小',
    WHERE_FIELD_         VARCHAR(2000) COMMENT '条件字段定义',
    RESULT_FIELD_        VARCHAR(2000) COMMENT '结果字段',
    ORDER_FIELD_         VARCHAR(200) COMMENT '排序字段',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    TABLE_               VARCHAR(64) COMMENT '表名',
    SQL_DIY_             VARCHAR(2000) COMMENT 'GROOVYSQL脚本',
    SQL_                 VARCHAR(2000) COMMENT 'SQL脚本',
    SQL_BUILD_TYPE_      VARCHAR(20) COMMENT 'SQL构建类型',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_CUSTOM_QUERY COMMENT '自定义SQL查询';

/*==============================================================*/
/* Table: FORM_DATASOURCE_DEF                                   */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '数据源名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    ENABLE_              VARCHAR(20) COMMENT '是否允许',
    SETTING_             VARCHAR(3000) COMMENT '数据源设定',
    DB_TYPE_             VARCHAR(20) COMMENT '数据库类型',
    INIT_ON_START_       VARCHAR(40) COMMENT '启动时初始化',
    APP_NAME_            VARCHAR(255) COMMENT '微服务名称',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_DATASOURCE_DEF COMMENT '数据源定义';

/*==============================================================*/
/* Table: FORM_DATASOURCE_SHARE                                 */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_SHARE
(
    SHARE_ID_            VARCHAR(64) NOT NULL COMMENT '记录ID',
    DS_ID_               VARCHAR(64) NOT NULL COMMENT '数据源ID',
    APP_ID_              VARCHAR(64) NOT NULL COMMENT '应用ID',
    APP_NAME_            VARCHAR(64) DEFAULT NULL COMMENT '应用名称',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人ID',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (SHARE_ID_)
);

ALTER TABLE FORM_DATASOURCE_SHARE COMMENT '数据源共享';

/*==============================================================*/
/* Table: FORM_DEF_PERMISSION                                   */
/*==============================================================*/
CREATE TABLE FORM_DEF_PERMISSION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    FORM_ID_             VARCHAR(64) COMMENT '表单ID',
    BO_DEF_ID_           VARCHAR(64) COMMENT 'BO定义ID',
    BO_ALIAS_            VARCHAR(40) COMMENT 'BO定义名称',
    LEVEL_               INT COMMENT '优先级',
    PERMISSION_          TEXT COMMENT '权限配置',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_DEF_PERMISSION COMMENT '表单使用权限';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING                              */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ROLE_ID_             VARCHAR(64) COMMENT '角色ID',
    ROLE_NAME_           VARCHAR(64) COMMENT '角色名称',
    DATA_TYPE_ID_        VARCHAR(64) COMMENT '类型ID',
    DATA_TYPE_NAME_      VARCHAR(64) COMMENT '类型名称',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME NOT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_ENTITY_DATA_SETTING COMMENT '业务实体数据配置';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING_DIC                          */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING_DIC
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    SETTING_ID_          VARCHAR(64) COMMENT '配置ID',
    ID_VALUE_            VARCHAR(64) COMMENT '值内容',
    TEXT_VALUE_          VARCHAR(64) COMMENT '文本内容',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME NOT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_ENTITY_DATA_SETTING_DIC COMMENT '业务实体数据配置字典 ';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_TYPE                                 */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_TYPE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '类型名称',
    DIALOG_ALIAS_        VARCHAR(64) COMMENT '对话框别名',
    DIALOG_NAME_         VARCHAR(64) COMMENT '对话框名称',
    ID_FIELD_            VARCHAR(64) COMMENT '值字段',
    TEXT_FIELD_          VARCHAR(64) COMMENT '文本字段',
    STATUS_              VARCHAR(40) COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人',
    CREATE_TIME_         DATETIME NOT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_ENTITY_DATA_TYPE COMMENT '业务实体数据类型';

/*==============================================================*/
/* Table: FORM_MOBILE                                           */
/*==============================================================*/
CREATE TABLE FORM_MOBILE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(40) COMMENT '名称',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    ALIAS_               VARCHAR(40) COMMENT '别名',
    FORM_HTML_           TEXT COMMENT '表单HTML',
    SCRIPT_              TEXT COMMENT '表单脚本',
    DEPLOYED_            INT COMMENT '是否发布',
    METADATA_            TEXT COMMENT '元数据',
    BODEF_ALIAS_         VARCHAR(64) COMMENT 'BO定义别名',
    BODEF_ID_            VARCHAR(64) COMMENT 'BO定义ID',
    GROUP_PERMISSIONS_   TEXT COMMENT '分组权限',
    TYPE_                VARCHAR(40) COMMENT '类型',
    BUTTON_DEF_          TEXT COMMENT '按钮定义',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    FORM_PC_ALIAS_       VARCHAR(64) COMMENT 'PC表单别名',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_MOBILE COMMENT '手机表单定义';

/*==============================================================*/
/* Table: FORM_PC                                               */
/*==============================================================*/
CREATE TABLE FORM_PC
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    TYPE_                VARCHAR(40) COMMENT '表单类型
            ONLINE-DESIGN,GENBYBO',
    TEMPLATE_            TEXT COMMENT '模版',
    JAVASCRIPT_          TEXT COMMENT '表单脚本',
    JAVASCRIPT_KEY_      TEXT COMMENT '表单脚本KEY',
    METADATA_            TEXT COMMENT '表单数据',
    DEPLOYED_            INT COMMENT '发布状态',
    BODEF_ID_            VARCHAR(64) COMMENT '业务模型ID',
    BODEF_ALIAS_         VARCHAR(128) COMMENT '业务模型别名',
    VERSION_             INT COMMENT '版本号',
    MAIN_                INT COMMENT '主版本',
    OPINION_DEF_         VARCHAR(255) COMMENT '意见定义',
    BUTTON_DEF_          VARCHAR(1000) COMMENT '按钮定义',
    TABLE_BUTTON_DEF_    VARCHAR(1000) COMMENT '表自定义按钮',
    DATA_SETTING_        TEXT COMMENT '数据设定',
    FORM_SETTING_        TEXT COMMENT '表单设定',
    COMPONENT_           VARCHAR(128) COMMENT '表单组件',
    WIZARD_              INT COMMENT '支持向导',
    TAB_DEF_             VARCHAR(1000) COMMENT 'TAB定义',
    COPYED_              INT COMMENT '是否表单',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    DATASOURCE_          VARCHAR(64) COMMENT '数据源',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_PC COMMENT '表单设计';

/*==============================================================*/
/* Table: FORM_PC_HISTORY                                       */
/*==============================================================*/
CREATE TABLE FORM_PC_HISTORY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    FORM_PC_ID_          VARCHAR(64) NOT NULL COMMENT '表单设计主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    TEMPLATE_            TEXT COMMENT '模版',
    JAVASCRIPT_          TEXT COMMENT '表单脚本',
    JAVASCRIPT_KEY_      TEXT COMMENT '表单脚本KEY',
    METADATA_            TEXT COMMENT '表单数据',
    OPINION_DEF_         VARCHAR(255) COMMENT '意见定义',
    BUTTON_DEF_          VARCHAR(1000) COMMENT '按钮定义',
    TABLE_BUTTON_DEF_    VARCHAR(1000) COMMENT '表自定义按钮',
    DATA_SETTING_        TEXT COMMENT '数据设定',
    FORM_SETTING_        TEXT COMMENT '表单设定',
    COMPONENT_           VARCHAR(128) COMMENT '表单组件',
    WIZARD_              INT COMMENT '支持向导',
    TAB_DEF_             VARCHAR(1000) COMMENT 'TAB定义',
    REMARK_              VARCHAR(255) COMMENT '备注',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_PC_HISTORY COMMENT '表单设计历史';

/*==============================================================*/
/* Table: FORM_PDF_TEMPLATE                                     */
/*==============================================================*/
CREATE TABLE FORM_PDF_TEMPLATE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KEY_                 VARCHAR(64) COMMENT '别名',
    TREE_ID_             VARCHAR(64) COMMENT '树形ID',
    BO_DEF_ID_           VARCHAR(64) COMMENT '业务模型ID',
    PDF_HTML_            TEXT COMMENT '表单HTML',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_PDF_TEMPLATE COMMENT '表单PDF模板';

/*==============================================================*/
/* Table: FORM_PERMISSION                                       */
/*==============================================================*/
CREATE TABLE FORM_PERMISSION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TYPE_                VARCHAR(64) COMMENT '权限类型
            form,formSol',
    CONFIG_ID_           VARCHAR(64) COMMENT '配置ID',
    PERMISSION_          TEXT COMMENT '权限',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_PERMISSION COMMENT '表单权限配置';

/*==============================================================*/
/* Table: FORM_PRINT_LODOP                                      */
/*==============================================================*/
CREATE TABLE FORM_PRINT_LODOP
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(20) COMMENT '别名',
    BACK_IMG_            VARCHAR(128) COMMENT '背景图',
    FORM_ID_             VARCHAR(64) COMMENT '表单ID',
    FORM_NAME_           VARCHAR(64) COMMENT '表单名称',
    TEMPLATE_            TEXT COMMENT '套打模板',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_PRINT_LODOP COMMENT '表单套打模板';

/*==============================================================*/
/* Table: FORM_QUERY_STRATEGY                                   */
/*==============================================================*/
CREATE TABLE FORM_QUERY_STRATEGY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '策略名称',
    IS_PUBLIC_           VARCHAR(40) COMMENT '是否公开',
    QUERY_CONDITION_     VARCHAR(2000) COMMENT '查询条件配置',
    IS_USER_             VARCHAR(64) COMMENT '是否常用',
    LIST_ID_             VARCHAR(64) COMMENT '列表ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_QUERY_STRATEGY COMMENT '查询策略';

/*==============================================================*/
/* Table: FORM_REG_LIB                                          */
/*==============================================================*/
CREATE TABLE FORM_REG_LIB
(
    REG_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    USER_ID_             VARCHAR(64) COMMENT '用户ID',
    REG_TEXT_            VARCHAR(512) COMMENT '正则公式',
    NAME_                VARCHAR(64) COMMENT '名称',
    TYPE_                VARCHAR(64) COMMENT '类型',
    KEY_                 VARCHAR(64) COMMENT '别名',
    MENT_TEXT_           VARCHAR(512) COMMENT '替换公式',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (REG_ID_)
);

ALTER TABLE FORM_REG_LIB COMMENT '正则表达式替换规则';

/*==============================================================*/
/* Table: FORM_RULE                                             */
/*==============================================================*/
CREATE TABLE FORM_RULE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(20) COMMENT '名称',
    PROMPT_              VARCHAR(40) COMMENT '提示语',
    ALIAS_               VARCHAR(20) COMMENT '别名',
    REGULAR_             VARCHAR(64) COMMENT '正则表达式',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_RULE COMMENT '表单验证规则';

/*==============================================================*/
/* Table: FORM_SAVE_EXPORT                                      */
/*==============================================================*/
CREATE TABLE FORM_SAVE_EXPORT
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    DATA_LIST_           VARCHAR(64) COMMENT 'BO列表别名',
    SETTING_             TEXT COMMENT '导出设置',
    IS_PUBLIC_           INT COMMENT '是否公开',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    MAX_COUNT_           INT NOT NULL COMMENT '导出最大数量',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_SAVE_EXPORT COMMENT 'EXCEL导出配置';

/*==============================================================*/
/* Table: FORM_SOLUTION                                         */
/*==============================================================*/
CREATE TABLE FORM_SOLUTION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '方案ID',
    CATEGORY_ID_         VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(64) COMMENT '别名',
    BODEF_ID_            VARCHAR(64) COMMENT '业务模型ID',
    FORM_ID_             VARCHAR(64) COMMENT '表单',
    FORM_NAME_           VARCHAR(128) COMMENT '表单名称',
    MOBILE_FORM_ID_      VARCHAR(64) COMMENT '手机表单ID',
    MOBILE_FORM_NAME_    VARCHAR(128) COMMENT '手机表单名称',
    DATA_HANDLER_        VARCHAR(255) COMMENT '表单数据处理器',
    TREE_                INT COMMENT '树形表单',
    LOAD_MODE_           VARCHAR(40) COMMENT '树形加载方式0,一次性加载,1,懒加载',
    DISPLAY_FIELDS_      VARCHAR(64) COMMENT '显示字段',
    FORMULAS_            VARCHAR(200) COMMENT '表间公式',
    FORMULAS_NAME_       VARCHAR(200) COMMENT '表间公式名称',
    BUTTONS_SETTING_     TEXT COMMENT '自定义按钮',
    NO_PK_SETTING_       VARCHAR(255) COMMENT '无主键传入时参数配置',
    JAVA_CODE_           TEXT COMMENT 'JAVA脚本',
    IS_GENERATE_TABLE_   INT COMMENT '是否生成物理表',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    FLOW_DEF_MAPPING_    TEXT COMMENT '流程定义配置',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_SOLUTION COMMENT '表单方案';

/*==============================================================*/
/* Table: FORM_SQL_LOG                                          */
/*==============================================================*/
CREATE TABLE FORM_SQL_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TYPE_                VARCHAR(40) COMMENT '日志类型',
    SQL_                 TEXT COMMENT 'SQL详情',
    PARAMS_              TEXT COMMENT '参数说明',
    REMARK_              TEXT COMMENT '执行备注',
    IS_SUCCESS_          VARCHAR(40) COMMENT '是否成功',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_SQL_LOG COMMENT '表间公式日志';

/*==============================================================*/
/* Table: FORM_TABLE_FORMULA                                    */
/*==============================================================*/
CREATE TABLE FORM_TABLE_FORMULA
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(256) NOT NULL COMMENT '公式名称',
    DESCP_               VARCHAR(512) COMMENT '公式描述',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    FILL_CONF_           TEXT COMMENT '数据填充配置',
    DS_NAME_             VARCHAR(100) COMMENT '数据源',
    BO_DEF_ID_           VARCHAR(64) NOT NULL COMMENT '业务模型ID',
    BO_DEF_NAME_         VARCHAR(64) COMMENT '业务模型名称',
    ACTION_              VARCHAR(80) COMMENT '表单触发时机',
    SYS_ID_              VARCHAR(64) COMMENT '子系统ID',
    IS_TEST_             VARCHAR(80) COMMENT '是否开启调试模式',
    ENABLED_             VARCHAR(20) COMMENT '是否生效',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_TABLE_FORMULA COMMENT '表间公式';

/*==============================================================*/
/* Table: FORM_TEMPLATE                                         */
/*==============================================================*/
CREATE TABLE FORM_TEMPLATE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    ALIAS_               VARCHAR(20) COMMENT '别名',
    TEMPLATE_            TEXT COMMENT '模板内容',
    TYPE_                VARCHAR(64) COMMENT '类型',
    CATEGORY_            VARCHAR(64) COMMENT '分类',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE FORM_TEMPLATE COMMENT '表单模版';

/*==============================================================*/
/* Table: GRID_REPORT_DESIGN                                    */
/*==============================================================*/
CREATE TABLE GRID_REPORT_DESIGN
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键ID',
    NAME_                VARCHAR(64) DEFAULT NULL COMMENT '名称',
    KEY_                 VARCHAR(64) DEFAULT NULL COMMENT '标识',
    GRF_                 VARCHAR(64) DEFAULT NULL COMMENT '报表模板文件',
    QUERY_CONFIG_        TEXT COMMENT '查询条件配置',
    CREATE_DEP_ID_       VARCHAR(64) DEFAULT NULL COMMENT '机构ID',
    CREATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    CREATE_TIME_         DATETIME DEFAULT NULL COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    UPDATE_TIME_         DATETIME DEFAULT NULL COMMENT '更新时间',
    TENANT_ID_           VARCHAR(64) DEFAULT NULL COMMENT '租用用户ID',
    REF_ID_              VARCHAR(64) DEFAULT NULL,
    PARENT_ID_           VARCHAR(64) DEFAULT NULL,
    TREE_ID_             VARCHAR(64) DEFAULT NULL,
    DB_AS_               VARCHAR(64) DEFAULT NULL,
    DOC_ID_              VARCHAR(64) DEFAULT NULL,
    SQL_                 VARCHAR(500) DEFAULT NULL,
    USE_COND_SQL_TYPE_   VARCHAR(16) DEFAULT NULL,
    USE_COND_SQL_        VARCHAR(16) DEFAULT NULL,
    COND_SQLS_           VARCHAR(500) DEFAULT NULL,
    WEB_REQ_SCRIPT_      TEXT,
    WEB_REQ_MAPPING_JSON_ TEXT,
    WEB_REQ_KEY_         TEXT,
    INTERFACE_KEY_       TEXT,
    INTERFACE_MAPPING_JSON_ TEXT,
    PRIMARY KEY (ID_),
    KEY IDX_GRID_REPORT_KEY (KEY_)
);

ALTER TABLE GRID_REPORT_DESIGN COMMENT 'GridReport报表管理';

commit ;
