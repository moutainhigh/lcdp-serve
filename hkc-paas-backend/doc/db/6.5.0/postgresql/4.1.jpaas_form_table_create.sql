CREATE TABLE IF NOT EXISTS public.undo_log
(
    id            SERIAL       NOT NULL,
    branch_id     BIGINT       NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info BYTEA        NOT NULL,
    log_status    INT          NOT NULL,
    log_created   TIMESTAMP(0) NOT NULL,
    log_modified  TIMESTAMP(0) NOT NULL,
    CONSTRAINT pk_undo_log PRIMARY KEY (id),
    CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
);

CREATE SEQUENCE IF NOT EXISTS undo_log_id_seq INCREMENT BY 1 MINVALUE 1 ;

/*==============================================================*/
/* Table: FORM_ATTACHMENT                                       */
/*==============================================================*/
CREATE TABLE FORM_ATTACHMENT (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 FILE_ID_             VARCHAR(64)          NULL,
                                 FILE_NAME_           VARCHAR(64)          NULL,
                                 ORIGINAL_FILE_NAME_  VARCHAR(64)          NULL,
                                 INST_ID_             VARCHAR(64)          NULL,
                                 TASK_ID_             VARCHAR(64)          NULL,
                                 NODE_ID_             VARCHAR(64)          NULL,
                                 NODE_NAME_           VARCHAR(64)          NULL,
                                 SIZE_                INT4                 NULL,
                                 CATEGORY_ID_         VARCHAR(64)          NULL,
                                 CATEGORY_NAME_       VARCHAR(64)          NULL,
                                 VERSION_             INT4                 NULL,
                                 CUR_VERSION_         INT4                 NULL,
                                 UPLOAD_TIME_         DATE                 NULL,
                                 MAIN_ID_             VARCHAR(64)          NULL,
                                 ATTACHMENT_ID_       VARCHAR(64)          NULL,
                                 IS_DELETE_           VARCHAR(20)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_NAME_         VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CONSTRAINT PK_FORM_ATTACHMENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ATTACHMENT IS
'表单附件';

COMMENT ON COLUMN FORM_ATTACHMENT.ID_ IS
'主键';

COMMENT ON COLUMN FORM_ATTACHMENT.FILE_ID_ IS
'文件ID';

COMMENT ON COLUMN FORM_ATTACHMENT.FILE_NAME_ IS
'文件名称';

COMMENT ON COLUMN FORM_ATTACHMENT.ORIGINAL_FILE_NAME_ IS
'文件原名称';

COMMENT ON COLUMN FORM_ATTACHMENT.INST_ID_ IS
'流程实例ID';

COMMENT ON COLUMN FORM_ATTACHMENT.TASK_ID_ IS
'任务ID';

COMMENT ON COLUMN FORM_ATTACHMENT.NODE_ID_ IS
'节点ID';

COMMENT ON COLUMN FORM_ATTACHMENT.NODE_NAME_ IS
'节点名称';

COMMENT ON COLUMN FORM_ATTACHMENT.SIZE_ IS
'附件大小字节';

COMMENT ON COLUMN FORM_ATTACHMENT.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_ATTACHMENT.CATEGORY_NAME_ IS
'分类名称';

COMMENT ON COLUMN FORM_ATTACHMENT.VERSION_ IS
'版本号';

COMMENT ON COLUMN FORM_ATTACHMENT.CUR_VERSION_ IS
'当前版本号';

COMMENT ON COLUMN FORM_ATTACHMENT.UPLOAD_TIME_ IS
'首次上传时间';

COMMENT ON COLUMN FORM_ATTACHMENT.MAIN_ID_ IS
'主ID';

COMMENT ON COLUMN FORM_ATTACHMENT.ATTACHMENT_ID_ IS
'关联字段ID';

COMMENT ON COLUMN FORM_ATTACHMENT.IS_DELETE_ IS
'是否删除';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_NAME_ IS
'创建人名称';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_ATTACHMENT.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN FORM_ATTACHMENT.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_ATTACHMENT.TENANT_ID_ IS
'租户ID';

/*==============================================================*/
/* Table: FORM_BO_ATTR                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_ATTR (
                              ID_                  VARCHAR(64)          NOT NULL,
                              ENT_ID_              VARCHAR(64)          NULL,
                              NAME_                VARCHAR(64)          NULL,
                              FIELD_NAME_          VARCHAR(64)          NULL,
                              COMMENT_             VARCHAR(64)          NULL,
                              DATA_TYPE_           VARCHAR(20)          NULL,
                              LENGTH_              INT4                 NULL,
                              DECIMAL_LENGTH_      INT4                 NULL,
                              CONTROL_             VARCHAR(20)          NULL,
                              EXT_JSON_            VARCHAR(4000)        NULL,
                              SN_                  INT4                 NULL,
                              IS_SINGLE_           INT4                 NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              APP_ID_              VARCHAR(64)          NULL,
                              IS_PK_               INT4                 NULL,
                              IS_NOT_NULL_         INT4                 NULL,
                              DB_FIELD_TYPE_       VARCHAR(128)         NULL,
                              SPANS_               INT4                 NULL,
                              CONSTRAINT PK_FORM_BO_ATTR PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_ATTR IS
'业务实体属性';

COMMENT ON COLUMN FORM_BO_ATTR.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_ATTR.ENT_ID_ IS
'实体ID';

COMMENT ON COLUMN FORM_BO_ATTR.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_BO_ATTR.FIELD_NAME_ IS
'字段名';

COMMENT ON COLUMN FORM_BO_ATTR.COMMENT_ IS
'备注';

COMMENT ON COLUMN FORM_BO_ATTR.DATA_TYPE_ IS
'数据类型';

COMMENT ON COLUMN FORM_BO_ATTR.LENGTH_ IS
'长度';

COMMENT ON COLUMN FORM_BO_ATTR.DECIMAL_LENGTH_ IS
'小数位';

COMMENT ON COLUMN FORM_BO_ATTR.CONTROL_ IS
'控件类型';

COMMENT ON COLUMN FORM_BO_ATTR.EXT_JSON_ IS
'扩展JSON';

COMMENT ON COLUMN FORM_BO_ATTR.SN_ IS
'序号';

COMMENT ON COLUMN FORM_BO_ATTR.IS_SINGLE_ IS
'是否单字段';

COMMENT ON COLUMN FORM_BO_ATTR.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_ATTR.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_ATTR.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_ATTR.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_BO_ATTR.IS_PK_ IS
'是否主键';

COMMENT ON COLUMN FORM_BO_ATTR.IS_NOT_NULL_ IS
'是否非空';

COMMENT ON COLUMN FORM_BO_ATTR.DB_FIELD_TYPE_ IS
'数据库字段类型';

COMMENT ON COLUMN FORM_BO_ATTR.SPANS_ IS
'占列';

/*==============================================================*/
/* Table: FORM_BO_DEF                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_DEF (
                             ID_                  VARCHAR(64)          NOT NULL,
                             NAME_                VARCHAR(64)          NULL,
                             ALIAS_               VARCHAR(64)          NULL,
                             TREE_ID_             VARCHAR(64)          NULL,
                             SUPPORT_DB_          INT4                 NULL,
                             DESCRIPTION_         VARCHAR(255)         NULL,
                             GEN_TYPE_            VARCHAR(20)          NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             APP_ID_              VARCHAR(64)          NULL,
                             CONSTRAINT PK_FORM_BO_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_DEF IS
'业务模型';

COMMENT ON COLUMN FORM_BO_DEF.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_DEF.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_BO_DEF.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_BO_DEF.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_BO_DEF.SUPPORT_DB_ IS
'支持数据库';

COMMENT ON COLUMN FORM_BO_DEF.DESCRIPTION_ IS
'说明';

COMMENT ON COLUMN FORM_BO_DEF.GEN_TYPE_ IS
'生成类型(FORM,DIRECT)';

COMMENT ON COLUMN FORM_BO_DEF.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_BO_ENTITY                                        */
/*==============================================================*/
CREATE TABLE FORM_BO_ENTITY (
                                ID_                  VARCHAR(64)          NOT NULL,
                                NAME_                VARCHAR(64)          NULL,
                                ALIAS_               VARCHAR(64)          NULL,
                                GEN_MODE_            VARCHAR(20)          NULL,
                                IS_MAIN_             INT4                 NULL,
                                TREE_ID_             VARCHAR(64)          NULL,
                                ID_FIELD_            VARCHAR(64)          NULL,
                                PARENT_FIELD_        VARCHAR(64)          NULL,
                                GENDB_               VARCHAR(40)          NULL,
                                DS_ALIAS_            VARCHAR(64)          NULL,
                                DS_NAME_             VARCHAR(64)          NULL,
                                VERSION_FIELD_       VARCHAR(40)          NULL,
                                TABLE_NAME_          VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                APP_ID_              VARCHAR(64)          NULL,
                                CONSTRAINT PK_FORM_BO_ENTITY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_ENTITY IS
'业务实体';

COMMENT ON COLUMN FORM_BO_ENTITY.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_ENTITY.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_BO_ENTITY.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_BO_ENTITY.GEN_MODE_ IS
'生成数据库';

COMMENT ON COLUMN FORM_BO_ENTITY.IS_MAIN_ IS
'主实体';

COMMENT ON COLUMN FORM_BO_ENTITY.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_BO_ENTITY.ID_FIELD_ IS
'主键字段';

COMMENT ON COLUMN FORM_BO_ENTITY.PARENT_FIELD_ IS
'父ID';

COMMENT ON COLUMN FORM_BO_ENTITY.GENDB_ IS
'生成数据库';

COMMENT ON COLUMN FORM_BO_ENTITY.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN FORM_BO_ENTITY.DS_NAME_ IS
'数据源名称';

COMMENT ON COLUMN FORM_BO_ENTITY.VERSION_FIELD_ IS
'版本字段';

COMMENT ON COLUMN FORM_BO_ENTITY.TABLE_NAME_ IS
'表名';

COMMENT ON COLUMN FORM_BO_ENTITY.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_ENTITY.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_ENTITY.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_ENTITY.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_BO_LIST                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST (
                              ID_                  VARCHAR(64)          NOT NULL,
                              NAME_                VARCHAR(64)          NULL,
                              KEY_                 VARCHAR(64)          NULL,
                              DESCP_               VARCHAR(255)         NULL,
                              ID_FIELD_            VARCHAR(64)          NULL,
                              TEXT_FIELD_          VARCHAR(64)          NULL,
                              PARENT_FIELD_        VARCHAR(64)          NULL,
                              IS_TREE_DLG_         VARCHAR(40)          NULL,
                              ONLY_SEL_LEAF_       VARCHAR(40)          NULL,
                              URL_                 VARCHAR(255)         NULL,
                              MULTI_SELECT_        VARCHAR(40)          NULL,
                              IS_LEFT_TREE_        VARCHAR(40)          NULL,
                              LEFT_NAV_            VARCHAR(1000)        NULL,
                              LEFT_TREE_JSON_      VARCHAR(300)         NULL,
                              SQL_                 VARCHAR(2000)        NULL,
                              USE_COND_SQL_        VARCHAR(20)          NULL,
                              COND_SQLS_           VARCHAR(2000)        NULL,
                              DB_AS_               VARCHAR(64)          NULL,
                              FIELDS_JSON_         TEXT                 NULL,
                              COLS_JSON_           TEXT                 NULL,
                              LIST_HTML_           TEXT                 NULL,
                              SEARCH_JSON_         TEXT                 NULL,
                              BPM_SOL_ID_          VARCHAR(64)          NULL,
                              FORM_ALIAS_          VARCHAR(64)          NULL,
                              TOP_BTNS_JSON_       VARCHAR(2000)        NULL,
                              BODY_SCRIPT_         TEXT                 NULL,
                              IS_DIALOG_           VARCHAR(40)          NULL,
                              IS_PAGE_             VARCHAR(40)          NULL,
                              IS_LAZY_             VARCHAR(40)          NULL,
                              IS_EXPORT_           VARCHAR(40)          NULL,
                              HEIGHT_              INT4                 NULL,
                              WIDTH_               INT4                 NULL,
                              ENABLE_FLOW_         VARCHAR(40)          NULL,
                              IS_GEN_              VARCHAR(40)          NULL,
                              TREE_ID_             VARCHAR(64)          NULL,
                              DRAW_CELL_SCRIPT_    VARCHAR(128)         NULL,
                              MOBILE_HTML_         TEXT                 NULL,
                              DATA_STYLE_          VARCHAR(40)          NULL,
                              ROW_EDIT_            VARCHAR(40)          NULL,
                              SHOW_SUMMARY_ROW_    VARCHAR(40)          NULL,
                              IS_INIT_DATA_        VARCHAR(40)          NULL,
                              FORM_DETAIL_ALIAS_   VARCHAR(64)          NULL,
                              SOL_ID_              VARCHAR(64)          NULL,
                              TEMPLATE_TYPE_       VARCHAR(64)          NULL,
                              FORM_ADD_ALIAS_      VARCHAR(64)          NULL,
                              WEBREQ_KEY_          VARCHAR(64)          NULL,
                              WEBREQ_SCRIPT_       TEXT                 NULL,
                              WEBREQ_MAPPING_JSON_ TEXT                 NULL,
                              FORM_NAME_           VARCHAR(64)          NULL,
                              FORM_DETAIL_NAME_    VARCHAR(64)          NULL,
                              PUBLISH_CONF_        VARCHAR(400)         NULL,
                              FORM_ADD_NAME_       VARCHAR(64)          NULL,
                              MOBILE_BUTTON        VARCHAR(2000)        NULL,
                              MOBILE_JS            TEXT                 NULL,
                              BUTTON_MAX           INT4                 NULL,
                              IS_SEARCH_HEADER     VARCHAR(40)          NULL,
                              IS_CHECKBOX_PROPS    VARCHAR(40)          NULL,
                              EXPAND_LEVEL         VARCHAR(20)          NULL,
                              EXCEL_CONF_JSON      TEXT                 NULL,
                              IS_APPROVAL          VARCHAR(10)          NULL,
                              PAGE_SIZE_           INT4                 NULL,
                              IS_EXPAND_ROW        VARCHAR(64)          NULL,
                              EXPAND_ROW_JSON      VARCHAR(2000)        NULL,
                              IS_TEST_             VARCHAR(80)          NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              IS_SHARE_            VARCHAR(40)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              APP_ID_              VARCHAR(64)          NULL,
                              INTERFACE_NAME_      VARCHAR(64)          NULL,
                              INTERFACE_MAPPING_JSON_ TEXT                 NULL,
                              INTERFACE_KEY_       VARCHAR(64)          NULL,
                              ROW_DBL_CLICK_       VARCHAR(80)          NULL DEFAULT 'rowDblClick',
                              ROW_CLICK_           VARCHAR(80)          NULL DEFAULT 'rowClick',
                              BUS_SOLUTION_        VARCHAR(64)          NULL,
                              CONSTRAINT PK_FORM_BO_LIST PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_LIST IS
'自定义列表管理';

COMMENT ON COLUMN FORM_BO_LIST.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_LIST.NAME_ IS
'列表名称';

COMMENT ON COLUMN FORM_BO_LIST.KEY_ IS
'别名';

COMMENT ON COLUMN FORM_BO_LIST.DESCP_ IS
'描述';

COMMENT ON COLUMN FORM_BO_LIST.ID_FIELD_ IS
'主键字段';

COMMENT ON COLUMN FORM_BO_LIST.TEXT_FIELD_ IS
'显示字段(树)';

COMMENT ON COLUMN FORM_BO_LIST.PARENT_FIELD_ IS
'父ID(树)';

COMMENT ON COLUMN FORM_BO_LIST.IS_TREE_DLG_ IS
'是否树对话框';

COMMENT ON COLUMN FORM_BO_LIST.ONLY_SEL_LEAF_ IS
'仅可选择树节点';

COMMENT ON COLUMN FORM_BO_LIST.URL_ IS
'数据地址';

COMMENT ON COLUMN FORM_BO_LIST.MULTI_SELECT_ IS
'是否多选择';

COMMENT ON COLUMN FORM_BO_LIST.IS_LEFT_TREE_ IS
'是否显示左树';

COMMENT ON COLUMN FORM_BO_LIST.LEFT_NAV_ IS
'左树SQL';

COMMENT ON COLUMN FORM_BO_LIST.LEFT_TREE_JSON_ IS
'左树字段映射';

COMMENT ON COLUMN FORM_BO_LIST.SQL_ IS
'SQL语句';

COMMENT ON COLUMN FORM_BO_LIST.USE_COND_SQL_ IS
'列表数据来源类型';

COMMENT ON COLUMN FORM_BO_LIST.COND_SQLS_ IS
'条件脚本';

COMMENT ON COLUMN FORM_BO_LIST.DB_AS_ IS
'数据源ID';

COMMENT ON COLUMN FORM_BO_LIST.FIELDS_JSON_ IS
'列字段JSON';

COMMENT ON COLUMN FORM_BO_LIST.COLS_JSON_ IS
'显示字段配置';

COMMENT ON COLUMN FORM_BO_LIST.LIST_HTML_ IS
'列表VUE模板';

COMMENT ON COLUMN FORM_BO_LIST.SEARCH_JSON_ IS
'搜索条件';

COMMENT ON COLUMN FORM_BO_LIST.BPM_SOL_ID_ IS
'绑定流程方案';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ALIAS_ IS
'绑定表单方案';

COMMENT ON COLUMN FORM_BO_LIST.TOP_BTNS_JSON_ IS
'头部按钮配置';

COMMENT ON COLUMN FORM_BO_LIST.BODY_SCRIPT_ IS
'脚本JS';

COMMENT ON COLUMN FORM_BO_LIST.IS_DIALOG_ IS
' 是否对话框';

COMMENT ON COLUMN FORM_BO_LIST.IS_PAGE_ IS
'是否分页';

COMMENT ON COLUMN FORM_BO_LIST.IS_LAZY_ IS
'树形是否懒加载';

COMMENT ON COLUMN FORM_BO_LIST.IS_EXPORT_ IS
'是否允许导出';

COMMENT ON COLUMN FORM_BO_LIST.HEIGHT_ IS
'对话框高度';

COMMENT ON COLUMN FORM_BO_LIST.WIDTH_ IS
'对话宽度';

COMMENT ON COLUMN FORM_BO_LIST.ENABLE_FLOW_ IS
'是否启用流程';

COMMENT ON COLUMN FORM_BO_LIST.IS_GEN_ IS
'是否已产生HTML';

COMMENT ON COLUMN FORM_BO_LIST.TREE_ID_ IS
'分类树ID';

COMMENT ON COLUMN FORM_BO_LIST.DRAW_CELL_SCRIPT_ IS
'脚本';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_HTML_ IS
'手机表单HTML模版';

COMMENT ON COLUMN FORM_BO_LIST.DATA_STYLE_ IS
'数据风格';

COMMENT ON COLUMN FORM_BO_LIST.ROW_EDIT_ IS
'行数据编辑';

COMMENT ON COLUMN FORM_BO_LIST.SHOW_SUMMARY_ROW_ IS
'是否显示汇总行';

COMMENT ON COLUMN FORM_BO_LIST.IS_INIT_DATA_ IS
'是否初始显示数据';

COMMENT ON COLUMN FORM_BO_LIST.FORM_DETAIL_ALIAS_ IS
'表单方案别名';

COMMENT ON COLUMN FORM_BO_LIST.SOL_ID_ IS
'解决方案ID';

COMMENT ON COLUMN FORM_BO_LIST.TEMPLATE_TYPE_ IS
'模版类型';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ADD_ALIAS_ IS
'添加表单方案';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_KEY_ IS
'自定义查询';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_SCRIPT_ IS
'请求脚本';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_MAPPING_JSON_ IS
'mapping映射';

COMMENT ON COLUMN FORM_BO_LIST.FORM_NAME_ IS
'编辑表单名称';

COMMENT ON COLUMN FORM_BO_LIST.FORM_DETAIL_NAME_ IS
'明细表单名称';

COMMENT ON COLUMN FORM_BO_LIST.PUBLISH_CONF_ IS
'发布配置';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ADD_NAME_ IS
'添加表单名称';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_BUTTON IS
'手机端按钮';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_JS IS
'手机端JS函数';

COMMENT ON COLUMN FORM_BO_LIST.BUTTON_MAX IS
'按钮最大显示数';

COMMENT ON COLUMN FORM_BO_LIST.IS_SEARCH_HEADER IS
'搜索是否展示头部';

COMMENT ON COLUMN FORM_BO_LIST.IS_CHECKBOX_PROPS IS
'是否选择框配置';

COMMENT ON COLUMN FORM_BO_LIST.EXPAND_LEVEL IS
'树形默认展开层数';

COMMENT ON COLUMN FORM_BO_LIST.EXCEL_CONF_JSON IS
'EXCEL配置JSON';

COMMENT ON COLUMN FORM_BO_LIST.IS_APPROVAL IS
'是否可以审批';

COMMENT ON COLUMN FORM_BO_LIST.PAGE_SIZE_ IS
'页大小';

COMMENT ON COLUMN FORM_BO_LIST.IS_EXPAND_ROW IS
'是否显示扩展页';

COMMENT ON COLUMN FORM_BO_LIST.EXPAND_ROW_JSON IS
'扩展页JSON';

COMMENT ON COLUMN FORM_BO_LIST.IS_TEST_ IS
'是否测试';

COMMENT ON COLUMN FORM_BO_LIST.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_LIST.IS_SHARE_ IS
'是否共享';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_LIST.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_LIST.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_LIST.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_NAME_ IS
'第三方接口名称';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_MAPPING_JSON_ IS
'第三方接口mapping映射';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_KEY_ IS
'第三方接口key';

COMMENT ON COLUMN FORM_BO_LIST.ROW_DBL_CLICK_ IS
'双击行时的方法名';

COMMENT ON COLUMN FORM_BO_LIST.ROW_CLICK_ IS
'单击行时的方法名';

COMMENT ON COLUMN FORM_BO_LIST.BUS_SOLUTION_ IS
'业务方案';

/*==============================================================*/
/* Table: FORM_BO_LIST_HISTORY                                  */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST_HISTORY (
                                      ID_                  VARCHAR(64)          NOT NULL,
                                      LIST_ID_             VARCHAR(64)          NULL,
                                      CONTENT_             TEXT                 NULL,
                                      LIST_KEY_ID_         VARCHAR(64)          NULL,
                                      VERSION_             INT4                 NULL,
                                      TENANT_ID_           VARCHAR(64)          NULL,
                                      CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                      CREATE_BY_           VARCHAR(64)          NULL,
                                      CREATE_TIME_         DATE                 NULL,
                                      UPDATE_BY_           VARCHAR(64)          NULL,
                                      UPDATE_TIME_         DATE                 NULL,
                                      REMARK_              VARCHAR(255)         NULL,
                                      CONSTRAINT PK_FORM_BO_LIST_HISTORY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_LIST_HISTORY IS
'列表变更历史表';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.LIST_ID_ IS
'列表ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CONTENT_ IS
'内容';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.LIST_KEY_ID_ IS
'预览用ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.VERSION_ IS
'版本号';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.REMARK_ IS
'备注';

/*==============================================================*/
/* Table: FORM_BO_PMT                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_PMT (
                             PMT_ID_              VARCHAR(64)          NOT NULL,
                             ALIAS_               VARCHAR(64)          NULL DEFAULT NULL,
                             NAME_                VARCHAR(64)          NULL DEFAULT NULL,
                             BO_LIST_ID_          VARCHAR(64)          NULL DEFAULT NULL,
                             BUTTONS_             TEXT                 NULL,
                             DATAS_               TEXT                 NULL,
                             FIELDS_              TEXT                 NULL,
                             STATUS_              VARCHAR(64)          NULL DEFAULT NULL,
                             MENU_ID_             VARCHAR(64)          NULL DEFAULT NULL,
                             TENANT_ID_           VARCHAR(64)          NULL DEFAULT NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL DEFAULT NULL,
                             CREATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                             CREATE_TIME_         DATE                 NULL DEFAULT NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                             UPDATE_TIME_         DATE                 NULL DEFAULT NULL,
                             CONSTRAINT PK_FORM_BO_PMT PRIMARY KEY (PMT_ID_)
);

COMMENT ON TABLE FORM_BO_PMT IS
'业务单据权限';

COMMENT ON COLUMN FORM_BO_PMT.PMT_ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_PMT.ALIAS_ IS
'权限别名';

COMMENT ON COLUMN FORM_BO_PMT.NAME_ IS
'权限名称';

COMMENT ON COLUMN FORM_BO_PMT.BO_LIST_ID_ IS
'表单列表ID';

COMMENT ON COLUMN FORM_BO_PMT.BUTTONS_ IS
'按钮权限';

COMMENT ON COLUMN FORM_BO_PMT.DATAS_ IS
'数据权限';

COMMENT ON COLUMN FORM_BO_PMT.FIELDS_ IS
'字段权限';

COMMENT ON COLUMN FORM_BO_PMT.STATUS_ IS
'是否可用(YES,NO)';

COMMENT ON COLUMN FORM_BO_PMT.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN FORM_BO_PMT.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_PMT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_PMT.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_BO_RELATION                                      */
/*==============================================================*/
CREATE TABLE FORM_BO_RELATION (
                                  ID_                  VARCHAR(64)          NOT NULL,
                                  BODEF_ID_            VARCHAR(64)          NULL,
                                  ENT_ID_              VARCHAR(64)          NULL,
                                  PARENT_ENT_ID_       VARCHAR(64)          NULL,
                                  TYPE_                VARCHAR(20)          NULL,
                                  IS_REF_              INT4                 NULL,
                                  PK_FIELD_            VARCHAR(40)          NULL,
                                  FK_FIELD_            VARCHAR(64)          NULL,
                                  TENANT_ID_           VARCHAR(64)          NULL,
                                  CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                  CREATE_BY_           VARCHAR(64)          NULL,
                                  CREATE_TIME_         DATE                 NULL,
                                  UPDATE_BY_           VARCHAR(64)          NULL,
                                  UPDATE_TIME_         DATE                 NULL,
                                  APP_ID_              VARCHAR(64)          NULL,
                                  CONSTRAINT PK_FORM_BO_RELATION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_RELATION IS
'业务实体';

COMMENT ON COLUMN FORM_BO_RELATION.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BO_RELATION.BODEF_ID_ IS
'业务模型定义';

COMMENT ON COLUMN FORM_BO_RELATION.ENT_ID_ IS
'实体ID';

COMMENT ON COLUMN FORM_BO_RELATION.PARENT_ENT_ID_ IS
'父实体ID';

COMMENT ON COLUMN FORM_BO_RELATION.TYPE_ IS
'关系类型(onetoone,onetomany)';

COMMENT ON COLUMN FORM_BO_RELATION.IS_REF_ IS
'是否引用';

COMMENT ON COLUMN FORM_BO_RELATION.PK_FIELD_ IS
'主键关联字段';

COMMENT ON COLUMN FORM_BO_RELATION.FK_FIELD_ IS
'关联字段';

COMMENT ON COLUMN FORM_BO_RELATION.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BO_RELATION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_BO_RELATION.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BO_RELATION.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_BUSINESS_SOLUTION                                */
/*==============================================================*/
CREATE TABLE FORM_BUSINESS_SOLUTION (
                                        ID_                  VARCHAR(64)          NOT NULL,
                                        CATEGORY_            VARCHAR(64)          NULL,
                                        NAME_                VARCHAR(64)          NULL,
                                        ALIAS_               VARCHAR(64)          NULL,
                                        NAVIGATION_POSITION_ VARCHAR(64)          NULL,
                                        MAIN_FORM_SOLUTION_  VARCHAR(64)          NULL,
                                        FORM_SOLUTIONS_      VARCHAR(2000)        NULL,
                                        CREATE_BY_           VARCHAR(64)          NULL,
                                        CREATE_TIME_         DATE                 NULL,
                                        UPDATE_BY_           VARCHAR(64)          NULL,
                                        UPDATE_TIME_         DATE                 NULL,
                                        CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                        TENANT_ID_           VARCHAR(64)          NULL,
                                        CONSTRAINT PK_FORM_BUSINESS_SOLUTION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUSINESS_SOLUTION IS
'表单业务方案';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CATEGORY_ IS
'分类';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.ALIAS_ IS
'标识';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.NAVIGATION_POSITION_ IS
'导航栏位置';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.MAIN_FORM_SOLUTION_ IS
'主表单方案';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.FORM_SOLUTIONS_ IS
'表单方案配置';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.TENANT_ID_ IS
'租户ID';

/*==============================================================*/
/* Table: FORM_BUS_API                                          */
/*==============================================================*/
CREATE TABLE FORM_BUS_API (
                              ID_                  VARCHAR(64)          NOT NULL,
                              ALIAS_               VARCHAR(64)          NULL,
                              NAME_                VARCHAR(64)          NULL,
                              DEF_KEY_             VARCHAR(40)          NULL,
                              DEF_NAME_            VARCHAR(40)          NULL,
                              FIELD_SETTING_       TEXT                 NULL,
                              STATUS_              VARCHAR(64)          NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              APP_ID_              VARCHAR(64)          NULL,
                              CONSTRAINT PK_FORM_BUS_API PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUS_API IS
'表单业务接口';

COMMENT ON COLUMN FORM_BUS_API.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BUS_API.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_BUS_API.NAME_ IS
'服务名称';

COMMENT ON COLUMN FORM_BUS_API.DEF_KEY_ IS
'流程定义KEY';

COMMENT ON COLUMN FORM_BUS_API.DEF_NAME_ IS
'流程定义名称';

COMMENT ON COLUMN FORM_BUS_API.FIELD_SETTING_ IS
'字段配置';

COMMENT ON COLUMN FORM_BUS_API.STATUS_ IS
'状态';

COMMENT ON COLUMN FORM_BUS_API.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BUS_API.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN FORM_BUS_API.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BUS_API.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_BUS_INST_DATA                                    */
/*==============================================================*/
CREATE TABLE FORM_BUS_INST_DATA (
                                    ID_                  VARCHAR(64)          NOT NULL,
                                    BUS_SOL_ID_          VARCHAR(64)          NULL,
                                    REL_FORMSOL_ID_      VARCHAR(64)          NULL,
                                    MAIN_PK_             VARCHAR(64)          NULL,
                                    REL_PK_              VARCHAR(64)          NULL,
                                    STATUS_              VARCHAR(20)          NULL,
                                    CREATE_BY_           VARCHAR(64)          NULL,
                                    CREATE_TIME_         DATE                 NULL,
                                    UPDATE_BY_           VARCHAR(64)          NULL,
                                    UPDATE_TIME_         DATE                 NULL,
                                    CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                    TENANT_ID_           VARCHAR(64)          NULL,
                                    CONSTRAINT PK_FORM_BUS_INST_DATA PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUS_INST_DATA IS
'表单业务数据';

COMMENT ON COLUMN FORM_BUS_INST_DATA.ID_ IS
'主键';

COMMENT ON COLUMN FORM_BUS_INST_DATA.BUS_SOL_ID_ IS
'表单业务方案主键';

COMMENT ON COLUMN FORM_BUS_INST_DATA.REL_FORMSOL_ID_ IS
'表单方案主键';

COMMENT ON COLUMN FORM_BUS_INST_DATA.MAIN_PK_ IS
'主表单主键';

COMMENT ON COLUMN FORM_BUS_INST_DATA.REL_PK_ IS
'关联主键';

COMMENT ON COLUMN FORM_BUS_INST_DATA.STATUS_ IS
'状态(1：生效 0：草稿)';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_BUS_INST_DATA.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN FORM_BUS_INST_DATA.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_BUS_INST_DATA.TENANT_ID_ IS
'租户ID';

/*==============================================================*/
/* Table: FORM_CHART_DATA_MODEL                                 */
/*==============================================================*/
CREATE TABLE FORM_CHART_DATA_MODEL (
                                       ID_                  VARCHAR(64)          NOT NULL,
                                       CATEGORY_ID_         VARCHAR(64)          NULL,
                                       NAME_                VARCHAR(64)          NULL,
                                       DATA_SOURCE_         VARCHAR(64)          NULL,
                                       TABLES_              VARCHAR(500)         NULL,
                                       MODEL_CONFIG_        TEXT                 NULL,
                                       TYPE_                VARCHAR(64)          NULL,
                                       SQL_MODE_            VARCHAR(2000)        NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL,
                                       CREATE_TIME_         DATE                 NOT NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL,
                                       UPDATE_TIME_         DATE                 NULL,
                                       APP_ID_              VARCHAR(64)          NULL,
                                       CONSTRAINT PK_FORM_CHART_DATA_MODEL PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CHART_DATA_MODEL IS
'图表数据模型';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.ID_ IS
'主键';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.NAME_ IS
'模型名称';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.DATA_SOURCE_ IS
'数据源';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TABLES_ IS
'表配置';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.MODEL_CONFIG_ IS
'模型配置';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TYPE_ IS
'模式';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.SQL_MODE_ IS
'自定义SQL模型';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_DEP_ID_ IS
'部门ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_CODEGEN_GLOBALVAR                                */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_GLOBALVAR (
                                        ID_                  VARCHAR(64)          NOT NULL,
                                        CONFIG_              VARCHAR(500)         NULL,
                                        TENANT_ID_           VARCHAR(64)          NULL,
                                        CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                        CREATE_BY_           VARCHAR(64)          NULL,
                                        CREATE_TIME_         DATE                 NULL,
                                        UPDATE_BY_           VARCHAR(64)          NULL,
                                        UPDATE_TIME_         DATE                 NULL,
                                        CONSTRAINT PK_FORM_CODEGEN_GLOBALVAR PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CODEGEN_GLOBALVAR IS
'代码生成全局变量';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.ID_ IS
'主键';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CONFIG_ IS
'变量配置';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_CODEGEN_TEMPLATE                                 */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_TEMPLATE (
                                       ID_                  VARCHAR(64)          NOT NULL,
                                       NAME_                VARCHAR(64)          NULL,
                                       FILE_NAME_           VARCHAR(128)         NULL,
                                       PATH_                VARCHAR(128)         NULL,
                                       SINGLE_              VARCHAR(40)          NULL,
                                       ENABLED_             VARCHAR(40)          NULL,
                                       CONTENT_             TEXT                 NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL,
                                       CREATE_TIME_         DATE                 NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL,
                                       UPDATE_TIME_         DATE                 NULL,
                                       CONSTRAINT PK_FORM_CODEGEN_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CODEGEN_TEMPLATE IS
'代码生成器模板';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.FILE_NAME_ IS
'文件名称';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.PATH_ IS
'生成路径';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.SINGLE_ IS
'生成单个文件';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.ENABLED_ IS
'是否允许';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CONTENT_ IS
'模板内容';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_CUSTOM                                           */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM (
                             ID_                  VARCHAR(64)          NOT NULL,
                             CATEGORY_ID_         VARCHAR(64)          NULL,
                             NAME_                VARCHAR(64)          NULL,
                             ALIAS_               VARCHAR(64)          NULL,
                             TYPE_                VARCHAR(40)          NULL,
                             JSON_                TEXT                 NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             APP_ID_              VARCHAR(64)          NULL,
                             HOME_PAGE_           INT2                 NULL DEFAULT 0,
                             CONSTRAINT PK_FORM_CUSTOM PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CUSTOM IS
'表单布局定制';

COMMENT ON COLUMN FORM_CUSTOM.ID_ IS
'主键';

COMMENT ON COLUMN FORM_CUSTOM.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_CUSTOM.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_CUSTOM.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_CUSTOM.TYPE_ IS
'类型(form,表单,other,其他)';

COMMENT ON COLUMN FORM_CUSTOM.JSON_ IS
'布局定义';

COMMENT ON COLUMN FORM_CUSTOM.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_CUSTOM.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_CUSTOM.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_CUSTOM.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_CUSTOM.HOME_PAGE_ IS
'是否为首页（0：否，1：是）';

/*==============================================================*/
/* Table: FORM_CUSTOM_QUERY                                     */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM_QUERY (
                                   ID_                  VARCHAR(64)          NOT NULL,
                                   NAME_                VARCHAR(64)          NULL,
                                   KEY_                 VARCHAR(64)          NULL,
                                   TABLE_NAME_          VARCHAR(64)          NULL,
                                   IS_PAGE_             INT4                 NULL,
                                   PAGE_SIZE_           INT4                 NULL,
                                   WHERE_FIELD_         VARCHAR(2000)        NULL,
                                   RESULT_FIELD_        VARCHAR(2000)        NULL,
                                   ORDER_FIELD_         VARCHAR(200)         NULL,
                                   DS_ALIAS_            VARCHAR(64)          NULL,
                                   TABLE_               VARCHAR(64)          NULL,
                                   SQL_DIY_             VARCHAR(2000)        NULL,
                                   SQL_                 VARCHAR(2000)        NULL,
                                   SQL_BUILD_TYPE_      VARCHAR(20)          NULL,
                                   TREE_ID_             VARCHAR(64)          NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   APP_ID_              VARCHAR(64)          NULL,
                                   CONSTRAINT PK_FORM_CUSTOM_QUERY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CUSTOM_QUERY IS
'自定义SQL查询';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.ID_ IS
'主键';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.KEY_ IS
'别名';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TABLE_NAME_ IS
'对象名称(表名或视图名)';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.IS_PAGE_ IS
'是否分页';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.PAGE_SIZE_ IS
'分页大小';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.WHERE_FIELD_ IS
'条件字段定义';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.RESULT_FIELD_ IS
'结果字段';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.ORDER_FIELD_ IS
'排序字段';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TABLE_ IS
'表名';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_DIY_ IS
'GROOVYSQL脚本';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_ IS
'SQL脚本';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_BUILD_TYPE_ IS
'SQL构建类型';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_DATASOURCE_DEF                                   */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_DEF (
                                     ID_                  VARCHAR(64)          NOT NULL,
                                     NAME_                VARCHAR(64)          NULL,
                                     ALIAS_               VARCHAR(64)          NULL,
                                     ENABLE_              VARCHAR(20)          NULL,
                                     SETTING_             VARCHAR(3000)        NULL,
                                     DB_TYPE_             VARCHAR(20)          NULL,
                                     INIT_ON_START_       VARCHAR(40)          NULL,
                                     APP_NAME_            VARCHAR(255)         NULL,
                                     TENANT_ID_           VARCHAR(64)          NULL,
                                     CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                     CREATE_BY_           VARCHAR(64)          NULL,
                                     CREATE_TIME_         DATE                 NULL,
                                     UPDATE_BY_           VARCHAR(64)          NULL,
                                     UPDATE_TIME_         DATE                 NULL,
                                     APP_ID_              VARCHAR(64)          NULL,
                                     CONSTRAINT PK_FORM_DATASOURCE_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_DATASOURCE_DEF IS
'数据源定义';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ID_ IS
'主键';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.NAME_ IS
'数据源名称';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ENABLE_ IS
'是否允许';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.SETTING_ IS
'数据源设定';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.DB_TYPE_ IS
'数据库类型';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.INIT_ON_START_ IS
'启动时初始化';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.APP_NAME_ IS
'微服务名称';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_DATASOURCE_SHARE                                 */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_SHARE (
                                       SHARE_ID_            VARCHAR(64)          NOT NULL,
                                       DS_ID_               VARCHAR(64)          NOT NULL,
                                       APP_ID_              VARCHAR(64)          NOT NULL,
                                       APP_NAME_            VARCHAR(64)          NULL DEFAULT NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL DEFAULT NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL DEFAULT NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                                       CREATE_TIME_         DATE                 NULL DEFAULT NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                                       UPDATE_TIME_         DATE                 NULL DEFAULT NULL,
                                       CONSTRAINT PK_FORM_DATASOURCE_SHARE PRIMARY KEY (SHARE_ID_)
);

COMMENT ON TABLE FORM_DATASOURCE_SHARE IS
'数据源共享';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.SHARE_ID_ IS
'记录ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.DS_ID_ IS
'数据源ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.APP_NAME_ IS
'应用名称';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_DEF_PERMISSION                                   */
/*==============================================================*/
CREATE TABLE FORM_DEF_PERMISSION (
                                     ID_                  VARCHAR(64)          NOT NULL,
                                     FORM_ID_             VARCHAR(64)          NULL,
                                     BO_DEF_ID_           VARCHAR(64)          NULL,
                                     BO_ALIAS_            VARCHAR(40)          NULL,
                                     LEVEL_               INT4                 NULL,
                                     PERMISSION_          TEXT                 NULL,
                                     TENANT_ID_           VARCHAR(64)          NULL,
                                     CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                     CREATE_BY_           VARCHAR(64)          NULL,
                                     CREATE_TIME_         DATE                 NULL,
                                     UPDATE_BY_           VARCHAR(64)          NULL,
                                     UPDATE_TIME_         DATE                 NULL,
                                     APP_ID_              VARCHAR(64)          NULL,
                                     CONSTRAINT PK_FORM_DEF_PERMISSION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_DEF_PERMISSION IS
'表单使用权限';

COMMENT ON COLUMN FORM_DEF_PERMISSION.ID_ IS
'主键';

COMMENT ON COLUMN FORM_DEF_PERMISSION.FORM_ID_ IS
'表单ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.BO_DEF_ID_ IS
'BO定义ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.BO_ALIAS_ IS
'BO定义名称';

COMMENT ON COLUMN FORM_DEF_PERMISSION.LEVEL_ IS
'优先级';

COMMENT ON COLUMN FORM_DEF_PERMISSION.PERMISSION_ IS
'权限配置';

COMMENT ON COLUMN FORM_DEF_PERMISSION.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_DEF_PERMISSION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_DEF_PERMISSION.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING                              */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING (
                                          ID_                  VARCHAR(64)          NOT NULL,
                                          ROLE_ID_             VARCHAR(64)          NULL,
                                          ROLE_NAME_           VARCHAR(64)          NULL,
                                          DATA_TYPE_ID_        VARCHAR(64)          NULL,
                                          DATA_TYPE_NAME_      VARCHAR(64)          NULL,
                                          TENANT_ID_           VARCHAR(64)          NULL,
                                          CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                          CREATE_BY_           VARCHAR(64)          NULL,
                                          CREATE_TIME_         DATE                 NOT NULL,
                                          UPDATE_BY_           VARCHAR(64)          NULL,
                                          UPDATE_TIME_         DATE                 NULL,
                                          CONSTRAINT PK_FORM_ENTITY_DATA_SETTING PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_SETTING IS
'业务实体数据配置';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ID_ IS
'主键';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ROLE_ID_ IS
'角色ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ROLE_NAME_ IS
'角色名称';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.DATA_TYPE_ID_ IS
'类型ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.DATA_TYPE_NAME_ IS
'类型名称';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_DEP_ID_ IS
'部门ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING_DIC                          */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING_DIC (
                                              ID_                  VARCHAR(64)          NOT NULL,
                                              SETTING_ID_          VARCHAR(64)          NULL,
                                              ID_VALUE_            VARCHAR(64)          NULL,
                                              TEXT_VALUE_          VARCHAR(64)          NULL,
                                              TENANT_ID_           VARCHAR(64)          NULL,
                                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                              CREATE_BY_           VARCHAR(64)          NULL,
                                              CREATE_TIME_         DATE                 NOT NULL,
                                              UPDATE_BY_           VARCHAR(64)          NULL,
                                              UPDATE_TIME_         DATE                 NULL,
                                              CONSTRAINT PK_FORM_ENTITY_DATA_SETTING_DI PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_SETTING_DIC IS
'业务实体数据配置字典 ';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.ID_ IS
'主键';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.SETTING_ID_ IS
'配置ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.ID_VALUE_ IS
'值内容';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.TEXT_VALUE_ IS
'文本内容';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_DEP_ID_ IS
'部门ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_TYPE                                 */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_TYPE (
                                       ID_                  VARCHAR(64)          NOT NULL,
                                       NAME_                VARCHAR(64)          NULL,
                                       DIALOG_ALIAS_        VARCHAR(64)          NULL,
                                       DIALOG_NAME_         VARCHAR(64)          NULL,
                                       ID_FIELD_            VARCHAR(64)          NULL,
                                       TEXT_FIELD_          VARCHAR(64)          NULL,
                                       STATUS_              VARCHAR(40)          NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL,
                                       CREATE_TIME_         DATE                 NOT NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL,
                                       UPDATE_TIME_         DATE                 NULL,
                                       CONSTRAINT PK_FORM_ENTITY_DATA_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_TYPE IS
'业务实体数据类型';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.NAME_ IS
'类型名称';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.DIALOG_ALIAS_ IS
'对话框别名';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.DIALOG_NAME_ IS
'对话框名称';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.ID_FIELD_ IS
'值字段';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.TEXT_FIELD_ IS
'文本字段';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.STATUS_ IS
'状态';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_DEP_ID_ IS
'部门ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_FILE_TYPE                                        */
/*==============================================================*/
CREATE TABLE FORM_FILE_TYPE (
                                ID_                  VARCHAR(64)          NOT NULL,
                                TYPE_NAME_           VARCHAR(64)          NULL,
                                CATEGORY_ID_         VARCHAR(64)          NULL,
                                CATEGORY_NAME_       VARCHAR(64)          NULL,
                                IS_REQUIRED_         INT4                 NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CONSTRAINT PK_FORM_FILE_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_FILE_TYPE IS
'表单附件类型';

COMMENT ON COLUMN FORM_FILE_TYPE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_FILE_TYPE.TYPE_NAME_ IS
'类型名称';

COMMENT ON COLUMN FORM_FILE_TYPE.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_FILE_TYPE.CATEGORY_NAME_ IS
'分类名称';

COMMENT ON COLUMN FORM_FILE_TYPE.IS_REQUIRED_ IS
'是否必填(1为是，0为否)';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_FILE_TYPE.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN FORM_FILE_TYPE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_FILE_TYPE.TENANT_ID_ IS
'租户ID';

/*==============================================================*/
/* Table: FORM_MOBILE                                           */
/*==============================================================*/
CREATE TABLE FORM_MOBILE (
                             ID_                  VARCHAR(64)          NOT NULL,
                             NAME_                VARCHAR(40)          NULL,
                             CATEGORY_ID_         VARCHAR(64)          NULL,
                             ALIAS_               VARCHAR(40)          NULL,
                             FORM_HTML_           TEXT                 NULL,
                             SCRIPT_              TEXT                 NULL,
                             DEPLOYED_            INT4                 NULL,
                             METADATA_            TEXT                 NULL,
                             BODEF_ALIAS_         VARCHAR(64)          NULL,
                             BODEF_ID_            VARCHAR(64)          NULL,
                             GROUP_PERMISSIONS_   TEXT                 NULL,
                             TYPE_                VARCHAR(40)          NULL,
                             BUTTON_DEF_          TEXT                 NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             APP_ID_              VARCHAR(64)          NULL,
                             FORM_PC_ALIAS_       VARCHAR(64)          NULL,
                             CONSTRAINT PK_FORM_MOBILE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_MOBILE IS
'手机表单定义';

COMMENT ON COLUMN FORM_MOBILE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_MOBILE.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_MOBILE.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_MOBILE.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_MOBILE.FORM_HTML_ IS
'表单HTML';

COMMENT ON COLUMN FORM_MOBILE.SCRIPT_ IS
'表单脚本';

COMMENT ON COLUMN FORM_MOBILE.DEPLOYED_ IS
'是否发布';

COMMENT ON COLUMN FORM_MOBILE.METADATA_ IS
'元数据';

COMMENT ON COLUMN FORM_MOBILE.BODEF_ALIAS_ IS
'BO定义别名';

COMMENT ON COLUMN FORM_MOBILE.BODEF_ID_ IS
'BO定义ID';

COMMENT ON COLUMN FORM_MOBILE.GROUP_PERMISSIONS_ IS
'分组权限';

COMMENT ON COLUMN FORM_MOBILE.TYPE_ IS
'类型';

COMMENT ON COLUMN FORM_MOBILE.BUTTON_DEF_ IS
'按钮定义';

COMMENT ON COLUMN FORM_MOBILE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_MOBILE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_MOBILE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_MOBILE.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_MOBILE.FORM_PC_ALIAS_ IS
'PC表单别名';

/*==============================================================*/
/* Table: FORM_PC                                               */
/*==============================================================*/
CREATE TABLE FORM_PC (
                         ID_                  VARCHAR(64)          NOT NULL,
                         NAME_                VARCHAR(64)          NULL,
                         CATEGORY_ID_         VARCHAR(64)          NULL,
                         ALIAS_               VARCHAR(64)          NULL,
                         TYPE_                VARCHAR(40)          NULL,
                         TEMPLATE_            TEXT                 NULL,
                         JAVASCRIPT_          TEXT                 NULL,
                         JAVASCRIPT_KEY_      TEXT                 NULL,
                         METADATA_            TEXT                 NULL,
                         DEPLOYED_            INT4                 NULL,
                         BODEF_ID_            VARCHAR(64)          NULL,
                         BODEF_ALIAS_         VARCHAR(128)         NULL,
                         VERSION_             INT4                 NULL,
                         MAIN_                INT4                 NULL,
                         OPINION_DEF_         VARCHAR(255)         NULL,
                         BUTTON_DEF_          VARCHAR(1000)        NULL,
                         TABLE_BUTTON_DEF_    VARCHAR(1000)        NULL,
                         DATA_SETTING_        TEXT                 NULL,
                         FORM_SETTING_        TEXT                 NULL,
                         COMPONENT_           VARCHAR(128)         NULL,
                         WIZARD_              INT4                 NULL,
                         TAB_DEF_             VARCHAR(1000)        NULL,
                         COPYED_              INT4                 NULL,
                         TENANT_ID_           VARCHAR(64)          NULL,
                         CREATE_DEP_ID_       VARCHAR(64)          NULL,
                         CREATE_BY_           VARCHAR(64)          NULL,
                         CREATE_TIME_         DATE                 NULL,
                         UPDATE_BY_           VARCHAR(64)          NULL,
                         UPDATE_TIME_         DATE                 NULL,
                         APP_ID_              VARCHAR(64)          NULL,
                         DATASOURCE_          VARCHAR(64)          NULL,
                         CONSTRAINT PK_FORM_PC PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PC IS
'表单设计';

COMMENT ON COLUMN FORM_PC.ID_ IS
'主键';

COMMENT ON COLUMN FORM_PC.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_PC.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_PC.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_PC.TYPE_ IS
'表单类型
ONLINE-DESIGN,GENBYBO';

COMMENT ON COLUMN FORM_PC.TEMPLATE_ IS
'模版';

COMMENT ON COLUMN FORM_PC.JAVASCRIPT_ IS
'表单脚本';

COMMENT ON COLUMN FORM_PC.JAVASCRIPT_KEY_ IS
'表单脚本KEY';

COMMENT ON COLUMN FORM_PC.METADATA_ IS
'表单数据';

COMMENT ON COLUMN FORM_PC.DEPLOYED_ IS
'发布状态';

COMMENT ON COLUMN FORM_PC.BODEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN FORM_PC.BODEF_ALIAS_ IS
'业务模型别名';

COMMENT ON COLUMN FORM_PC.VERSION_ IS
'版本号';

COMMENT ON COLUMN FORM_PC.MAIN_ IS
'主版本';

COMMENT ON COLUMN FORM_PC.OPINION_DEF_ IS
'意见定义';

COMMENT ON COLUMN FORM_PC.BUTTON_DEF_ IS
'按钮定义';

COMMENT ON COLUMN FORM_PC.TABLE_BUTTON_DEF_ IS
'表自定义按钮';

COMMENT ON COLUMN FORM_PC.DATA_SETTING_ IS
'数据设定';

COMMENT ON COLUMN FORM_PC.FORM_SETTING_ IS
'表单设定';

COMMENT ON COLUMN FORM_PC.COMPONENT_ IS
'表单组件';

COMMENT ON COLUMN FORM_PC.WIZARD_ IS
'支持向导';

COMMENT ON COLUMN FORM_PC.TAB_DEF_ IS
'TAB定义';

COMMENT ON COLUMN FORM_PC.COPYED_ IS
'是否表单';

COMMENT ON COLUMN FORM_PC.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_PC.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_PC.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_PC.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_PC.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_PC.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_PC.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_PC.DATASOURCE_ IS
'数据源';

/*==============================================================*/
/* Table: FORM_PC_HISTORY                                       */
/*==============================================================*/
CREATE TABLE FORM_PC_HISTORY (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 FORM_PC_ID_          VARCHAR(64)          NOT NULL,
                                 NAME_                VARCHAR(64)          NULL,
                                 ALIAS_               VARCHAR(64)          NULL,
                                 TEMPLATE_            TEXT                 NULL,
                                 JAVASCRIPT_          TEXT                 NULL,
                                 JAVASCRIPT_KEY_      TEXT                 NULL,
                                 METADATA_            TEXT                 NULL,
                                 OPINION_DEF_         VARCHAR(255)         NULL,
                                 BUTTON_DEF_          VARCHAR(1000)        NULL,
                                 TABLE_BUTTON_DEF_    VARCHAR(1000)        NULL,
                                 DATA_SETTING_        TEXT                 NULL,
                                 FORM_SETTING_        TEXT                 NULL,
                                 COMPONENT_           VARCHAR(128)         NULL,
                                 WIZARD_              INT4                 NULL,
                                 TAB_DEF_             VARCHAR(1000)        NULL,
                                 REMARK_              VARCHAR(255)         NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_FORM_PC_HISTORY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PC_HISTORY IS
'表单设计历史';

COMMENT ON COLUMN FORM_PC_HISTORY.ID_ IS
'主键';

COMMENT ON COLUMN FORM_PC_HISTORY.FORM_PC_ID_ IS
'表单设计主键';

COMMENT ON COLUMN FORM_PC_HISTORY.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_PC_HISTORY.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_PC_HISTORY.TEMPLATE_ IS
'模版';

COMMENT ON COLUMN FORM_PC_HISTORY.JAVASCRIPT_ IS
'表单脚本';

COMMENT ON COLUMN FORM_PC_HISTORY.JAVASCRIPT_KEY_ IS
'表单脚本KEY';

COMMENT ON COLUMN FORM_PC_HISTORY.METADATA_ IS
'表单数据';

COMMENT ON COLUMN FORM_PC_HISTORY.OPINION_DEF_ IS
'意见定义';

COMMENT ON COLUMN FORM_PC_HISTORY.BUTTON_DEF_ IS
'按钮定义';

COMMENT ON COLUMN FORM_PC_HISTORY.TABLE_BUTTON_DEF_ IS
'表自定义按钮';

COMMENT ON COLUMN FORM_PC_HISTORY.DATA_SETTING_ IS
'数据设定';

COMMENT ON COLUMN FORM_PC_HISTORY.FORM_SETTING_ IS
'表单设定';

COMMENT ON COLUMN FORM_PC_HISTORY.COMPONENT_ IS
'表单组件';

COMMENT ON COLUMN FORM_PC_HISTORY.WIZARD_ IS
'支持向导';

COMMENT ON COLUMN FORM_PC_HISTORY.TAB_DEF_ IS
'TAB定义';

COMMENT ON COLUMN FORM_PC_HISTORY.REMARK_ IS
'备注';

COMMENT ON COLUMN FORM_PC_HISTORY.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_PC_HISTORY.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_PC_HISTORY.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_PDF_TEMPLATE                                     */
/*==============================================================*/
CREATE TABLE FORM_PDF_TEMPLATE (
                                   ID_                  VARCHAR(64)          NOT NULL,
                                   NAME_                VARCHAR(64)          NULL,
                                   KEY_                 VARCHAR(64)          NULL,
                                   TREE_ID_             VARCHAR(64)          NULL,
                                   BO_DEF_ID_           VARCHAR(64)          NULL,
                                   PDF_HTML_            TEXT                 NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   APP_ID_              VARCHAR(64)          NULL,
                                   CONSTRAINT PK_FORM_PDF_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PDF_TEMPLATE IS
'表单PDF模板';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.KEY_ IS
'别名';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.TREE_ID_ IS
'树形ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.BO_DEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.PDF_HTML_ IS
'表单HTML';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_PERMISSION                                       */
/*==============================================================*/
CREATE TABLE FORM_PERMISSION (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 TYPE_                VARCHAR(64)          NULL,
                                 CONFIG_ID_           VARCHAR(64)          NULL,
                                 PERMISSION_          TEXT                 NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 APP_ID_              VARCHAR(64)          NULL,
                                 CONSTRAINT PK_FORM_PERMISSION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PERMISSION IS
'表单权限配置';

COMMENT ON COLUMN FORM_PERMISSION.ID_ IS
'主键';

COMMENT ON COLUMN FORM_PERMISSION.TYPE_ IS
'权限类型
form,formSol';

COMMENT ON COLUMN FORM_PERMISSION.CONFIG_ID_ IS
'配置ID';

COMMENT ON COLUMN FORM_PERMISSION.PERMISSION_ IS
'权限';

COMMENT ON COLUMN FORM_PERMISSION.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_PERMISSION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_PERMISSION.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_PERMISSION.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_PRINT_LODOP                                      */
/*==============================================================*/
CREATE TABLE FORM_PRINT_LODOP (
                                  ID_                  VARCHAR(64)          NOT NULL,
                                  NAME_                VARCHAR(64)          NULL,
                                  ALIAS_               VARCHAR(20)          NULL,
                                  BACK_IMG_            VARCHAR(128)         NULL,
                                  FORM_ID_             VARCHAR(64)          NULL,
                                  FORM_NAME_           VARCHAR(64)          NULL,
                                  TEMPLATE_            TEXT                 NULL,
                                  TENANT_ID_           VARCHAR(64)          NULL,
                                  CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                  CREATE_BY_           VARCHAR(64)          NULL,
                                  CREATE_TIME_         DATE                 NULL,
                                  UPDATE_BY_           VARCHAR(64)          NULL,
                                  UPDATE_TIME_         DATE                 NULL,
                                  APP_ID_              VARCHAR(64)          NULL,
                                  CONSTRAINT PK_FORM_PRINT_LODOP PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PRINT_LODOP IS
'表单套打模板';

COMMENT ON COLUMN FORM_PRINT_LODOP.ID_ IS
'主键';

COMMENT ON COLUMN FORM_PRINT_LODOP.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_PRINT_LODOP.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_PRINT_LODOP.BACK_IMG_ IS
'背景图';

COMMENT ON COLUMN FORM_PRINT_LODOP.FORM_ID_ IS
'表单ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.FORM_NAME_ IS
'表单名称';

COMMENT ON COLUMN FORM_PRINT_LODOP.TEMPLATE_ IS
'套打模板';

COMMENT ON COLUMN FORM_PRINT_LODOP.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_PRINT_LODOP.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_PRINT_LODOP.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_QUERY_STRATEGY                                   */
/*==============================================================*/
CREATE TABLE FORM_QUERY_STRATEGY (
                                     ID_                  VARCHAR(64)          NOT NULL,
                                     NAME_                VARCHAR(64)          NULL,
                                     IS_PUBLIC_           VARCHAR(40)          NULL,
                                     QUERY_CONDITION_     VARCHAR(2000)        NULL,
                                     IS_USER_             VARCHAR(64)          NULL,
                                     LIST_ID_             VARCHAR(64)          NULL,
                                     TENANT_ID_           VARCHAR(64)          NULL,
                                     CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                     CREATE_BY_           VARCHAR(64)          NULL,
                                     CREATE_TIME_         DATE                 NULL,
                                     UPDATE_BY_           VARCHAR(64)          NULL,
                                     UPDATE_TIME_         DATE                 NULL,
                                     APP_ID_              VARCHAR(64)          NULL,
                                     CONSTRAINT PK_FORM_QUERY_STRATEGY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_QUERY_STRATEGY IS
'查询策略';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.ID_ IS
'主键';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.NAME_ IS
'策略名称';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.IS_PUBLIC_ IS
'是否公开';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.QUERY_CONDITION_ IS
'查询条件配置';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.IS_USER_ IS
'是否常用';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.LIST_ID_ IS
'列表ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_REG_LIB                                          */
/*==============================================================*/
CREATE TABLE FORM_REG_LIB (
                              REG_ID_              VARCHAR(64)          NOT NULL,
                              USER_ID_             VARCHAR(64)          NULL,
                              REG_TEXT_            VARCHAR(512)         NULL,
                              NAME_                VARCHAR(64)          NULL,
                              TYPE_                VARCHAR(64)          NULL,
                              KEY_                 VARCHAR(64)          NULL,
                              MENT_TEXT_           VARCHAR(512)         NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              APP_ID_              VARCHAR(64)          NULL,
                              CONSTRAINT PK_FORM_REG_LIB PRIMARY KEY (REG_ID_)
);

COMMENT ON TABLE FORM_REG_LIB IS
'正则表达式替换规则';

COMMENT ON COLUMN FORM_REG_LIB.REG_ID_ IS
'主键';

COMMENT ON COLUMN FORM_REG_LIB.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN FORM_REG_LIB.REG_TEXT_ IS
'正则公式';

COMMENT ON COLUMN FORM_REG_LIB.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_REG_LIB.TYPE_ IS
'类型';

COMMENT ON COLUMN FORM_REG_LIB.KEY_ IS
'别名';

COMMENT ON COLUMN FORM_REG_LIB.MENT_TEXT_ IS
'替换公式';

COMMENT ON COLUMN FORM_REG_LIB.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_REG_LIB.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_REG_LIB.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_REG_LIB.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_RULE                                             */
/*==============================================================*/
CREATE TABLE FORM_RULE (
                           ID_                  VARCHAR(64)          NOT NULL,
                           NAME_                VARCHAR(20)          NULL,
                           PROMPT_              VARCHAR(40)          NULL,
                           ALIAS_               VARCHAR(20)          NULL,
                           REGULAR_             VARCHAR(64)          NULL,
                           TENANT_ID_           VARCHAR(64)          NULL,
                           CREATE_DEP_ID_       VARCHAR(64)          NULL,
                           CREATE_BY_           VARCHAR(64)          NULL,
                           CREATE_TIME_         DATE                 NULL,
                           UPDATE_BY_           VARCHAR(64)          NULL,
                           UPDATE_TIME_         DATE                 NULL,
                           APP_ID_              VARCHAR(64)          NULL,
                           CONSTRAINT PK_FORM_RULE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_RULE IS
'表单验证规则';

COMMENT ON COLUMN FORM_RULE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_RULE.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_RULE.PROMPT_ IS
'提示语';

COMMENT ON COLUMN FORM_RULE.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_RULE.REGULAR_ IS
'正则表达式';

COMMENT ON COLUMN FORM_RULE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_RULE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_RULE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_RULE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_RULE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_RULE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_RULE.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_SAVE_EXPORT                                      */
/*==============================================================*/
CREATE TABLE FORM_SAVE_EXPORT (
                                  ID_                  VARCHAR(64)          NOT NULL,
                                  NAME_                VARCHAR(64)          NULL,
                                  DATA_LIST_           VARCHAR(64)          NULL,
                                  SETTING_             TEXT                 NULL,
                                  IS_PUBLIC_           INT4                 NULL,
                                  TENANT_ID_           VARCHAR(64)          NULL,
                                  CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                  CREATE_BY_           VARCHAR(64)          NULL,
                                  CREATE_TIME_         DATE                 NULL,
                                  UPDATE_BY_           VARCHAR(64)          NULL,
                                  UPDATE_TIME_         DATE                 NULL,
                                  APP_ID_              VARCHAR(64)          NULL,
                                  MAX_COUNT_           INT4                 NOT NULL,
                                  CONSTRAINT PK_FORM_SAVE_EXPORT PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SAVE_EXPORT IS
'EXCEL导出配置';

COMMENT ON COLUMN FORM_SAVE_EXPORT.ID_ IS
'主键';

COMMENT ON COLUMN FORM_SAVE_EXPORT.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_SAVE_EXPORT.DATA_LIST_ IS
'BO列表别名';

COMMENT ON COLUMN FORM_SAVE_EXPORT.SETTING_ IS
'导出设置';

COMMENT ON COLUMN FORM_SAVE_EXPORT.IS_PUBLIC_ IS
'是否公开';

COMMENT ON COLUMN FORM_SAVE_EXPORT.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_SAVE_EXPORT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_SAVE_EXPORT.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.MAX_COUNT_ IS
'导出最大数量';

/*==============================================================*/
/* Table: FORM_SOLUTION                                         */
/*==============================================================*/
CREATE TABLE FORM_SOLUTION (
                               ID_                  VARCHAR(64)          NOT NULL,
                               CATEGORY_ID_         VARCHAR(64)          NULL,
                               NAME_                VARCHAR(64)          NULL,
                               ALIAS_               VARCHAR(64)          NULL,
                               BODEF_ID_            VARCHAR(64)          NULL,
                               FORM_ID_             VARCHAR(64)          NULL,
                               FORM_NAME_           VARCHAR(128)         NULL,
                               MOBILE_FORM_ID_      VARCHAR(64)          NULL,
                               MOBILE_FORM_NAME_    VARCHAR(128)         NULL,
                               DATA_HANDLER_        VARCHAR(255)         NULL,
                               TREE_                INT4                 NULL,
                               LOAD_MODE_           VARCHAR(40)          NULL,
                               DISPLAY_FIELDS_      VARCHAR(64)          NULL,
                               FORMULAS_            VARCHAR(200)         NULL,
                               FORMULAS_NAME_       VARCHAR(200)         NULL,
                               BUTTONS_SETTING_     TEXT                 NULL,
                               NO_PK_SETTING_       VARCHAR(255)         NULL,
                               JAVA_CODE_           TEXT                 NULL,
                               IS_GENERATE_TABLE_   VARCHAR(64)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               APP_ID_              VARCHAR(64)          NULL,
                               FLOW_DEF_MAPPING_    TEXT                 NULL,
                               CONSTRAINT PK_FORM_SOLUTION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SOLUTION IS
'表单方案';

COMMENT ON COLUMN FORM_SOLUTION.ID_ IS
'方案ID';

COMMENT ON COLUMN FORM_SOLUTION.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_SOLUTION.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_SOLUTION.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_SOLUTION.BODEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN FORM_SOLUTION.FORM_ID_ IS
'表单';

COMMENT ON COLUMN FORM_SOLUTION.FORM_NAME_ IS
'表单名称';

COMMENT ON COLUMN FORM_SOLUTION.MOBILE_FORM_ID_ IS
'手机表单ID';

COMMENT ON COLUMN FORM_SOLUTION.MOBILE_FORM_NAME_ IS
'手机表单名称';

COMMENT ON COLUMN FORM_SOLUTION.DATA_HANDLER_ IS
'表单数据处理器';

COMMENT ON COLUMN FORM_SOLUTION.TREE_ IS
'树形表单';

COMMENT ON COLUMN FORM_SOLUTION.LOAD_MODE_ IS
'树形加载方式0,一次性加载,1,懒加载';

COMMENT ON COLUMN FORM_SOLUTION.DISPLAY_FIELDS_ IS
'显示字段';

COMMENT ON COLUMN FORM_SOLUTION.FORMULAS_ IS
'表间公式';

COMMENT ON COLUMN FORM_SOLUTION.FORMULAS_NAME_ IS
'表间公式名称';

COMMENT ON COLUMN FORM_SOLUTION.BUTTONS_SETTING_ IS
'自定义按钮';

COMMENT ON COLUMN FORM_SOLUTION.NO_PK_SETTING_ IS
'无主键传入时参数配置';

COMMENT ON COLUMN FORM_SOLUTION.JAVA_CODE_ IS
'JAVA脚本';

COMMENT ON COLUMN FORM_SOLUTION.IS_GENERATE_TABLE_ IS
'是否生成物理表';

COMMENT ON COLUMN FORM_SOLUTION.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_SOLUTION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_SOLUTION.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_SOLUTION.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN FORM_SOLUTION.FLOW_DEF_MAPPING_ IS
'流程定义配置';

/*==============================================================*/
/* Table: FORM_SQL_LOG                                          */
/*==============================================================*/
CREATE TABLE FORM_SQL_LOG (
                              ID_                  VARCHAR(64)          NOT NULL,
                              TYPE_                VARCHAR(40)          NULL,
                              SQL_                 TEXT                 NULL,
                              PARAMS_              TEXT                 NULL,
                              REMARK_              TEXT                 NULL,
                              IS_SUCCESS_          VARCHAR(40)          NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              CONSTRAINT PK_FORM_SQL_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SQL_LOG IS
'表间公式日志';

COMMENT ON COLUMN FORM_SQL_LOG.ID_ IS
'主键';

COMMENT ON COLUMN FORM_SQL_LOG.TYPE_ IS
'日志类型';

COMMENT ON COLUMN FORM_SQL_LOG.SQL_ IS
'SQL详情';

COMMENT ON COLUMN FORM_SQL_LOG.PARAMS_ IS
'参数说明';

COMMENT ON COLUMN FORM_SQL_LOG.REMARK_ IS
'执行备注';

COMMENT ON COLUMN FORM_SQL_LOG.IS_SUCCESS_ IS
'是否成功';

COMMENT ON COLUMN FORM_SQL_LOG.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_SQL_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_SQL_LOG.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: FORM_TABLE_FORMULA                                    */
/*==============================================================*/
CREATE TABLE FORM_TABLE_FORMULA (
                                    ID_                  VARCHAR(64)          NOT NULL,
                                    NAME_                VARCHAR(256)         NOT NULL,
                                    DESCP_               VARCHAR(512)         NULL,
                                    TREE_ID_             VARCHAR(64)          NULL,
                                    FILL_CONF_           TEXT                 NULL,
                                    DS_NAME_             VARCHAR(100)         NULL,
                                    BO_DEF_ID_           VARCHAR(64)          NOT NULL,
                                    BO_DEF_NAME_         VARCHAR(64)          NULL,
                                    ACTION_              VARCHAR(80)          NULL,
                                    SYS_ID_              VARCHAR(64)          NULL,
                                    IS_TEST_             VARCHAR(80)          NULL,
                                    ENABLED_             VARCHAR(20)          NULL,
                                    TENANT_ID_           VARCHAR(64)          NULL,
                                    CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                    CREATE_TIME_         DATE                 NULL,
                                    CREATE_BY_           VARCHAR(64)          NULL,
                                    UPDATE_BY_           VARCHAR(64)          NULL,
                                    UPDATE_TIME_         DATE                 NULL,
                                    APP_ID_              VARCHAR(64)          NULL,
                                    CONSTRAINT PK_FORM_TABLE_FORMULA PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_TABLE_FORMULA IS
'表间公式';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ID_ IS
'主键';

COMMENT ON COLUMN FORM_TABLE_FORMULA.NAME_ IS
'公式名称';

COMMENT ON COLUMN FORM_TABLE_FORMULA.DESCP_ IS
'公式描述';

COMMENT ON COLUMN FORM_TABLE_FORMULA.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.FILL_CONF_ IS
'数据填充配置';

COMMENT ON COLUMN FORM_TABLE_FORMULA.DS_NAME_ IS
'数据源';

COMMENT ON COLUMN FORM_TABLE_FORMULA.BO_DEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.BO_DEF_NAME_ IS
'业务模型名称';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ACTION_ IS
'表单触发时机';

COMMENT ON COLUMN FORM_TABLE_FORMULA.SYS_ID_ IS
'子系统ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.IS_TEST_ IS
'是否开启调试模式';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ENABLED_ IS
'是否生效';

COMMENT ON COLUMN FORM_TABLE_FORMULA.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_TABLE_FORMULA.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: FORM_TEMPLATE                                         */
/*==============================================================*/
CREATE TABLE FORM_TEMPLATE (
                               ID_                  VARCHAR(64)          NOT NULL,
                               NAME_                VARCHAR(64)          NULL,
                               ALIAS_               VARCHAR(20)          NULL,
                               TEMPLATE_            TEXT                 NULL,
                               TYPE_                VARCHAR(64)          NULL,
                               CATEGORY_            VARCHAR(64)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               APP_ID_              VARCHAR(64)          NULL,
                               CONSTRAINT PK_FORM_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_TEMPLATE IS
'表单模版';

COMMENT ON COLUMN FORM_TEMPLATE.ID_ IS
'主键';

COMMENT ON COLUMN FORM_TEMPLATE.NAME_ IS
'名称';

COMMENT ON COLUMN FORM_TEMPLATE.ALIAS_ IS
'别名';

COMMENT ON COLUMN FORM_TEMPLATE.TEMPLATE_ IS
'模板内容';

COMMENT ON COLUMN FORM_TEMPLATE.TYPE_ IS
'类型';

COMMENT ON COLUMN FORM_TEMPLATE.CATEGORY_ IS
'分类';

COMMENT ON COLUMN FORM_TEMPLATE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN FORM_TEMPLATE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN FORM_TEMPLATE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN FORM_TEMPLATE.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: GRID_REPORT_DESIGN                                    */
/*==============================================================*/
CREATE TABLE GRID_REPORT_DESIGN (
                                    ID_                  VARCHAR(64)          NOT NULL,
                                    NAME_                VARCHAR(64)          NULL DEFAULT NULL,
                                    KEY_                 VARCHAR(64)          NULL DEFAULT NULL,
                                    GRF_                 VARCHAR(64)          NULL DEFAULT NULL,
                                    QUERY_CONFIG_        TEXT                 NULL,
                                    CREATE_DEP_ID_       VARCHAR(64)          NULL DEFAULT NULL,
                                    CREATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                                    CREATE_TIME_         DATE                 NULL DEFAULT NULL,
                                    UPDATE_BY_           VARCHAR(64)          NULL DEFAULT NULL,
                                    UPDATE_TIME_         DATE                 NULL DEFAULT NULL,
                                    TENANT_ID_           VARCHAR(64)          NULL DEFAULT NULL,
                                    REF_ID_              VARCHAR(64)          NULL DEFAULT NULL,
                                    PARENT_ID_           VARCHAR(64)          NULL DEFAULT NULL,
                                    TREE_ID_             VARCHAR(64)          NULL DEFAULT NULL,
                                    DB_AS_               VARCHAR(64)          NULL DEFAULT NULL,
                                    DOC_ID_              VARCHAR(64)          NULL DEFAULT NULL,
                                    SQL_                 VARCHAR(500)         NULL DEFAULT NULL,
                                    USE_COND_SQL_TYPE_   VARCHAR(16)          NULL DEFAULT NULL,
                                    USE_COND_SQL_        VARCHAR(16)          NULL DEFAULT NULL,
                                    COND_SQLS_           VARCHAR(500)         NULL DEFAULT NULL,
                                    WEB_REQ_SCRIPT_      TEXT                 NULL,
                                    WEB_REQ_MAPPING_JSON_ TEXT                 NULL,
                                    WEB_REQ_KEY_         TEXT                 NULL,
                                    INTERFACE_KEY_       TEXT                 NULL,
                                    INTERFACE_MAPPING_JSON_ TEXT                 NULL,
                                    CONSTRAINT PK_GRID_REPORT_DESIGN PRIMARY KEY (ID_),
                                    CONSTRAINT IDX_GRID_REPORT_KEY UNIQUE (KEY_)
);

COMMENT ON TABLE GRID_REPORT_DESIGN IS
'GridReport报表管理';

COMMENT ON COLUMN GRID_REPORT_DESIGN.ID_ IS
'主键ID';

COMMENT ON COLUMN GRID_REPORT_DESIGN.NAME_ IS
'名称';

COMMENT ON COLUMN GRID_REPORT_DESIGN.KEY_ IS
'标识';

COMMENT ON COLUMN GRID_REPORT_DESIGN.GRF_ IS
'报表模板文件';

COMMENT ON COLUMN GRID_REPORT_DESIGN.QUERY_CONFIG_ IS
'查询条件配置';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_DEP_ID_ IS
'机构ID';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN GRID_REPORT_DESIGN.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN GRID_REPORT_DESIGN.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN GRID_REPORT_DESIGN.TENANT_ID_ IS
'租用用户ID';

commit ;

