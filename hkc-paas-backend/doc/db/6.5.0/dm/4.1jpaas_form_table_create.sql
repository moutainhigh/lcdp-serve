-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE undo_log
(
    id            NUMBER(19)    NOT NULL,
    branch_id     NUMBER(19)    NOT NULL,
    xid           VARCHAR2(100) NOT NULL,
    context_       VARCHAR2(128) NOT NULL,
    rollback_info BLOB          NOT NULL,
    log_status    NUMBER(10)    NOT NULL,
    log_created   TIMESTAMP(0)  NOT NULL,
    log_modified  TIMESTAMP(0)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
);

COMMENT ON TABLE undo_log IS 'AT transaction mode undo table';

-- Generate ID using sequence and trigger
CREATE SEQUENCE UNDO_LOG_SEQ START WITH 1 INCREMENT BY 1;

/*==============================================================*/
/* Table: FORM_ATTACHMENT                                       */
/*==============================================================*/
CREATE TABLE FORM_ATTACHMENT  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  FILE_ID_             VARCHAR2(64),
                                  FILE_NAME_           VARCHAR2(64),
                                  ORIGINAL_FILE_NAME_  VARCHAR2(64),
                                  INST_ID_             VARCHAR2(64),
                                  TASK_ID_             VARCHAR2(64),
                                  NODE_ID_             VARCHAR2(64),
                                  NODE_NAME_           VARCHAR2(64),
                                  SIZE_                INTEGER,
                                  CATEGORY_ID_         VARCHAR2(64),
                                  CATEGORY_NAME_       VARCHAR2(64),
                                  VERSION_             INTEGER,
                                  CUR_VERSION_         INTEGER,
                                  UPLOAD_TIME_         DATE,
                                  MAIN_ID_             VARCHAR2(64),
                                  ATTACHMENT_ID_       VARCHAR2(64),
                                  IS_DELETE_           VARCHAR2(20),
                                  CREATE_BY_           VARCHAR2(64),
                                  CREATE_NAME_         VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  UPDATE_TIME_         DATE,
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  TENANT_ID_           VARCHAR2(64),
                                  CONSTRAINT PK_FORM_ATTACHMENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ATTACHMENT IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.ID_ IS
'??????';

COMMENT ON COLUMN FORM_ATTACHMENT.FILE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.FILE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.ORIGINAL_FILE_NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.NODE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.NODE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.SIZE_ IS
'??????????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.CATEGORY_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.VERSION_ IS
'?????????';

COMMENT ON COLUMN FORM_ATTACHMENT.CUR_VERSION_ IS
'???????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.UPLOAD_TIME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.MAIN_ID_ IS
'???ID';

COMMENT ON COLUMN FORM_ATTACHMENT.ATTACHMENT_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.IS_DELETE_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_ATTACHMENT.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_ATTACHMENT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_ATTACHMENT.TENANT_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BO_ATTR                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_ATTR  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               ENT_ID_              VARCHAR2(64),
                               NAME_                VARCHAR2(64),
                               FIELD_NAME_          VARCHAR2(64),
                               COMMENT_             VARCHAR2(64),
                               DATA_TYPE_           VARCHAR2(20),
                               LENGTH_              INTEGER,
                               DECIMAL_LENGTH_      INTEGER,
                               CONTROL_             VARCHAR2(20),
                               EXT_JSON_            VARCHAR2(4000),
                               SN_                  INTEGER,
                               IS_SINGLE_           INTEGER,
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               APP_ID_              VARCHAR2(64),
                               IS_PK_               INTEGER,
                               IS_NOT_NULL_         INTEGER,
                               DB_FIELD_TYPE_       VARCHAR2(128),
                               SPANS_               INTEGER,
                               CONSTRAINT PK_FORM_BO_ATTR PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_ATTR IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_ATTR.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ATTR.ENT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_ATTR.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ATTR.FIELD_NAME_ IS
'?????????';

COMMENT ON COLUMN FORM_BO_ATTR.COMMENT_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ATTR.DATA_TYPE_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.LENGTH_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ATTR.DECIMAL_LENGTH_ IS
'?????????';

COMMENT ON COLUMN FORM_BO_ATTR.CONTROL_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.EXT_JSON_ IS
'??????JSON';

COMMENT ON COLUMN FORM_BO_ATTR.SN_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ATTR.IS_SINGLE_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_ATTR.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_ATTR.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_ATTR.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_ATTR.IS_PK_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.IS_NOT_NULL_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ATTR.DB_FIELD_TYPE_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_ATTR.SPANS_ IS
'??????';

/*==============================================================*/
/* Table: FORM_BO_DEF                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_DEF  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(64),
                              ALIAS_               VARCHAR2(64),
                              TREE_ID_             VARCHAR2(64),
                              SUPPORT_DB_          INTEGER,
                              DESCRIPTION_         VARCHAR2(255),
                              GEN_TYPE_            VARCHAR2(20),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              APP_ID_              VARCHAR2(64),
                              CONSTRAINT PK_FORM_BO_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_DEF IS
'????????????';

COMMENT ON COLUMN FORM_BO_DEF.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_DEF.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_BO_DEF.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_BO_DEF.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_DEF.SUPPORT_DB_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_DEF.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN FORM_BO_DEF.GEN_TYPE_ IS
'????????????(FORM,DIRECT)';

COMMENT ON COLUMN FORM_BO_DEF.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_DEF.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_DEF.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BO_ENTITY                                        */
/*==============================================================*/
CREATE TABLE FORM_BO_ENTITY  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(64),
                                 ALIAS_               VARCHAR2(64),
                                 GEN_MODE_            VARCHAR2(20),
                                 IS_MAIN_             INTEGER,
                                 TREE_ID_             VARCHAR2(64),
                                 ID_FIELD_            VARCHAR2(64),
                                 PARENT_FIELD_        VARCHAR2(64),
                                 GENDB_               VARCHAR2(40),
                                 DS_ALIAS_            VARCHAR2(64),
                                 DS_NAME_             VARCHAR2(64),
                                 VERSION_FIELD_       VARCHAR2(40),
                                 TABLE_NAME_          VARCHAR2(64),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 APP_ID_              VARCHAR2(64),
                                 CONSTRAINT PK_FORM_BO_ENTITY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_ENTITY IS
'????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ENTITY.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ENTITY.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ENTITY.GEN_MODE_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.IS_MAIN_ IS
'?????????';

COMMENT ON COLUMN FORM_BO_ENTITY.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_ENTITY.ID_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.PARENT_FIELD_ IS
'???ID';

COMMENT ON COLUMN FORM_BO_ENTITY.GENDB_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.DS_ALIAS_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.DS_NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.VERSION_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.TABLE_NAME_ IS
'??????';

COMMENT ON COLUMN FORM_BO_ENTITY.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_ENTITY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_ENTITY.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_ENTITY.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BO_LIST                                          */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               NAME_                VARCHAR2(64),
                               KEY_                 VARCHAR2(64),
                               DESCP_               VARCHAR2(255),
                               ID_FIELD_            VARCHAR2(64),
                               TEXT_FIELD_          VARCHAR2(64),
                               PARENT_FIELD_        VARCHAR2(64),
                               IS_TREE_DLG_         VARCHAR2(40),
                               ONLY_SEL_LEAF_       VARCHAR2(40),
                               URL_                 VARCHAR2(255),
                               MULTI_SELECT_        VARCHAR2(40),
                               IS_LEFT_TREE_        VARCHAR2(40),
                               LEFT_NAV_            VARCHAR2(1000),
                               LEFT_TREE_JSON_      VARCHAR2(300),
                               SQL_                 VARCHAR2(2000),
                               USE_COND_SQL_        VARCHAR2(20),
                               COND_SQLS_           VARCHAR2(2000),
                               DB_AS_               VARCHAR2(64),
                               FIELDS_JSON_         CLOB,
                               COLS_JSON_           CLOB,
                               LIST_HTML_           CLOB,
                               SEARCH_JSON_         CLOB,
                               BPM_SOL_ID_          VARCHAR2(64),
                               FORM_ALIAS_          VARCHAR2(64),
                               TOP_BTNS_JSON_       VARCHAR2(2000),
                               BODY_SCRIPT_         CLOB,
                               IS_DIALOG_           VARCHAR2(40),
                               IS_PAGE_             VARCHAR2(40),
                               IS_LAZY_             VARCHAR2(40),
                               IS_EXPORT_           VARCHAR2(40),
                               HEIGHT_              INTEGER,
                               WIDTH_               INTEGER,
                               ENABLE_FLOW_         VARCHAR2(40),
                               IS_GEN_              VARCHAR2(40),
                               TREE_ID_             VARCHAR2(64),
                               DRAW_CELL_SCRIPT_    VARCHAR2(128),
                               MOBILE_HTML_         CLOB,
                               DATA_STYLE_          VARCHAR2(40),
                               ROW_EDIT_            VARCHAR2(40),
                               SHOW_SUMMARY_ROW_    VARCHAR2(40),
                               IS_INIT_DATA_        VARCHAR2(40),
                               FORM_DETAIL_ALIAS_   VARCHAR2(64),
                               SOL_ID_              VARCHAR2(64),
                               TEMPLATE_TYPE_       VARCHAR2(64),
                               FORM_ADD_ALIAS_      VARCHAR2(64),
                               WEBREQ_KEY_          VARCHAR2(64),
                               WEBREQ_SCRIPT_       CLOB,
                               WEBREQ_MAPPING_JSON_ CLOB,
                               FORM_NAME_           VARCHAR2(64),
                               FORM_DETAIL_NAME_    VARCHAR2(64),
                               PUBLISH_CONF_        VARCHAR2(400),
                               FORM_ADD_NAME_       VARCHAR2(64),
                               MOBILE_BUTTON        VARCHAR2(2000),
                               MOBILE_JS            CLOB,
                               BUTTON_MAX           INTEGER,
                               IS_SEARCH_HEADER     VARCHAR2(40),
                               IS_CHECKBOX_PROPS    VARCHAR2(40),
                               EXPAND_LEVEL         VARCHAR2(20),
                               EXCEL_CONF_JSON      CLOB,
                               IS_APPROVAL          VARCHAR2(10),
                               PAGE_SIZE_           INTEGER,
                               IS_EXPAND_ROW        VARCHAR2(64),
                               EXPAND_ROW_JSON      VARCHAR2(2000),
                               IS_TEST_             VARCHAR2(80),
                               TENANT_ID_           VARCHAR2(64),
                               IS_SHARE_            VARCHAR2(40),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               APP_ID_              VARCHAR2(64),
                               INTERFACE_NAME_      VARCHAR2(64),
                               INTERFACE_MAPPING_JSON_ CLOB,
                               INTERFACE_KEY_       VARCHAR2(64),
                               ROW_DBL_CLICK_       VARCHAR2(80)                   DEFAULT 'rowDblClick',
                               ROW_CLICK_           VARCHAR2(80)                   DEFAULT 'rowClick',
                               BUS_SOLUTION_        VARCHAR2(64),
                               CONSTRAINT PK_FORM_BO_LIST PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_LIST IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.KEY_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST.DESCP_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST.ID_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.TEXT_FIELD_ IS
'????????????(???)';

COMMENT ON COLUMN FORM_BO_LIST.PARENT_FIELD_ IS
'???ID(???)';

COMMENT ON COLUMN FORM_BO_LIST.IS_TREE_DLG_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.ONLY_SEL_LEAF_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.URL_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.MULTI_SELECT_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_LEFT_TREE_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.LEFT_NAV_ IS
'??????SQL';

COMMENT ON COLUMN FORM_BO_LIST.LEFT_TREE_JSON_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.SQL_ IS
'SQL??????';

COMMENT ON COLUMN FORM_BO_LIST.USE_COND_SQL_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.COND_SQLS_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.DB_AS_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST.FIELDS_JSON_ IS
'?????????JSON';

COMMENT ON COLUMN FORM_BO_LIST.COLS_JSON_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.LIST_HTML_ IS
'??????VUE??????';

COMMENT ON COLUMN FORM_BO_LIST.SEARCH_JSON_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.BPM_SOL_ID_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ALIAS_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.TOP_BTNS_JSON_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.BODY_SCRIPT_ IS
'??????JS';

COMMENT ON COLUMN FORM_BO_LIST.IS_DIALOG_ IS
' ???????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_PAGE_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_LAZY_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_EXPORT_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.HEIGHT_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_LIST.WIDTH_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.ENABLE_FLOW_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_GEN_ IS
'???????????????HTML';

COMMENT ON COLUMN FORM_BO_LIST.TREE_ID_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST.DRAW_CELL_SCRIPT_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_HTML_ IS
'????????????HTML??????';

COMMENT ON COLUMN FORM_BO_LIST.DATA_STYLE_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.ROW_EDIT_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_LIST.SHOW_SUMMARY_ROW_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_INIT_DATA_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_DETAIL_ALIAS_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.SOL_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_LIST.TEMPLATE_TYPE_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ADD_ALIAS_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_KEY_ IS
'???????????????';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_SCRIPT_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.WEBREQ_MAPPING_JSON_ IS
'mapping??????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_DETAIL_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.PUBLISH_CONF_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.FORM_ADD_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_BUTTON IS
'???????????????';

COMMENT ON COLUMN FORM_BO_LIST.MOBILE_JS IS
'?????????JS??????';

COMMENT ON COLUMN FORM_BO_LIST.BUTTON_MAX IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_SEARCH_HEADER IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_CHECKBOX_PROPS IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.EXPAND_LEVEL IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.EXCEL_CONF_JSON IS
'EXCEL??????JSON';

COMMENT ON COLUMN FORM_BO_LIST.IS_APPROVAL IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_LIST.PAGE_SIZE_ IS
'?????????';

COMMENT ON COLUMN FORM_BO_LIST.IS_EXPAND_ROW IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.EXPAND_ROW_JSON IS
'?????????JSON';

COMMENT ON COLUMN FORM_BO_LIST.IS_TEST_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_LIST.IS_SHARE_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_NAME_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_MAPPING_JSON_ IS
'???????????????mapping??????';

COMMENT ON COLUMN FORM_BO_LIST.INTERFACE_KEY_ IS
'???????????????key';

COMMENT ON COLUMN FORM_BO_LIST.ROW_DBL_CLICK_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.ROW_CLICK_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_BO_LIST.BUS_SOLUTION_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_BO_LIST_HISTORY                                  */
/*==============================================================*/
CREATE TABLE FORM_BO_LIST_HISTORY  (
                                       ID_                  VARCHAR2(64)                    NOT NULL,
                                       LIST_ID_             VARCHAR2(64),
                                       CONTENT_             CLOB,
                                       LIST_KEY_ID_         VARCHAR2(64),
                                       VERSION_             INTEGER,
                                       TENANT_ID_           VARCHAR2(64),
                                       CREATE_DEP_ID_       VARCHAR2(64),
                                       CREATE_BY_           VARCHAR2(64),
                                       CREATE_TIME_         DATE,
                                       UPDATE_BY_           VARCHAR2(64),
                                       UPDATE_TIME_         DATE,
                                       REMARK_              VARCHAR2(255),
                                       CONSTRAINT PK_FORM_BO_LIST_HISTORY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_LIST_HISTORY IS
'?????????????????????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.LIST_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CONTENT_ IS
'??????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.LIST_KEY_ID_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.VERSION_ IS
'?????????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_LIST_HISTORY.REMARK_ IS
'??????';

/*==============================================================*/
/* Table: FORM_BO_PMT                                           */
/*==============================================================*/
CREATE TABLE FORM_BO_PMT  (
                              PMT_ID_              VARCHAR2(64)                    NOT NULL,
                              ALIAS_               VARCHAR2(64)                   DEFAULT NULL,
                              NAME_                VARCHAR2(64)                   DEFAULT NULL,
                              BO_LIST_ID_          VARCHAR2(64)                   DEFAULT NULL,
                              BUTTONS_             CLOB,
                              DATAS_               CLOB,
                              FIELDS_              CLOB,
                              STATUS_              VARCHAR2(64)                   DEFAULT NULL,
                              MENU_ID_             VARCHAR2(64)                   DEFAULT NULL,
                              TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                              CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                              CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                              CREATE_TIME_         DATE                           DEFAULT NULL,
                              UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                              UPDATE_TIME_         DATE                           DEFAULT NULL,
                              CONSTRAINT PK_FORM_BO_PMT PRIMARY KEY (PMT_ID_)
);

COMMENT ON TABLE FORM_BO_PMT IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_PMT.PMT_ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_PMT.ALIAS_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.BO_LIST_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_PMT.BUTTONS_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.DATAS_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.FIELDS_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.STATUS_ IS
'????????????(YES,NO)';

COMMENT ON COLUMN FORM_BO_PMT.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_PMT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_PMT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_PMT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_PMT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_BO_RELATION                                      */
/*==============================================================*/
CREATE TABLE FORM_BO_RELATION  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   BODEF_ID_            VARCHAR2(64),
                                   ENT_ID_              VARCHAR2(64),
                                   PARENT_ENT_ID_       VARCHAR2(64),
                                   TYPE_                VARCHAR2(20),
                                   IS_REF_              INTEGER,
                                   PK_FIELD_            VARCHAR2(40),
                                   FK_FIELD_            VARCHAR2(64),
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   APP_ID_              VARCHAR2(64),
                                   CONSTRAINT PK_FORM_BO_RELATION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BO_RELATION IS
'????????????';

COMMENT ON COLUMN FORM_BO_RELATION.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BO_RELATION.BODEF_ID_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_RELATION.ENT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_RELATION.PARENT_ENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_RELATION.TYPE_ IS
'????????????(onetoone,onetomany)';

COMMENT ON COLUMN FORM_BO_RELATION.IS_REF_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_RELATION.PK_FIELD_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BO_RELATION.FK_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_RELATION.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_RELATION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_RELATION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BO_RELATION.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BO_RELATION.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BUSINESS_SOLUTION                                */
/*==============================================================*/
CREATE TABLE FORM_BUSINESS_SOLUTION  (
                                         ID_                  VARCHAR2(64)                    NOT NULL,
                                         CATEGORY_            VARCHAR2(64),
                                         NAME_                VARCHAR2(64),
                                         ALIAS_               VARCHAR2(64),
                                         NAVIGATION_POSITION_ VARCHAR2(64),
                                         MAIN_FORM_SOLUTION_  VARCHAR2(64),
                                         FORM_SOLUTIONS_      VARCHAR2(2000),
                                         CREATE_BY_           VARCHAR2(64),
                                         CREATE_TIME_         DATE,
                                         UPDATE_BY_           VARCHAR2(64),
                                         UPDATE_TIME_         DATE,
                                         CREATE_DEP_ID_       VARCHAR2(64),
                                         TENANT_ID_           VARCHAR2(64),
                                         CONSTRAINT PK_FORM_BUSINESS_SOLUTION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUSINESS_SOLUTION IS
'??????????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CATEGORY_ IS
'??????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.NAVIGATION_POSITION_ IS
'???????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.MAIN_FORM_SOLUTION_ IS
'???????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.FORM_SOLUTIONS_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BUSINESS_SOLUTION.TENANT_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BUS_API                                          */
/*==============================================================*/
CREATE TABLE FORM_BUS_API  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               ALIAS_               VARCHAR2(64),
                               NAME_                VARCHAR2(64),
                               DEF_KEY_             VARCHAR2(40),
                               DEF_NAME_            VARCHAR2(40),
                               FIELD_SETTING_       CLOB,
                               STATUS_              VARCHAR2(64),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               APP_ID_              VARCHAR2(64),
                               CONSTRAINT PK_FORM_BUS_API PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUS_API IS
'??????????????????';

COMMENT ON COLUMN FORM_BUS_API.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BUS_API.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_BUS_API.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_API.DEF_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN FORM_BUS_API.DEF_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BUS_API.FIELD_SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_API.STATUS_ IS
'??????';

COMMENT ON COLUMN FORM_BUS_API.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BUS_API.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_API.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_BUS_API.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_API.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_BUS_INST_DATA                                    */
/*==============================================================*/
CREATE TABLE FORM_BUS_INST_DATA  (
                                     ID_                  VARCHAR2(64)                    NOT NULL,
                                     BUS_SOL_ID_          VARCHAR2(64),
                                     REL_FORMSOL_ID_      VARCHAR2(64),
                                     MAIN_PK_             VARCHAR2(64),
                                     REL_PK_              VARCHAR2(64),
                                     STATUS_              VARCHAR2(20),
                                     CREATE_BY_           VARCHAR2(64),
                                     CREATE_TIME_         DATE,
                                     UPDATE_BY_           VARCHAR2(64),
                                     UPDATE_TIME_         DATE,
                                     CREATE_DEP_ID_       VARCHAR2(64),
                                     TENANT_ID_           VARCHAR2(64),
                                     CONSTRAINT PK_FORM_BUS_INST_DATA PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_BUS_INST_DATA IS
'??????????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.ID_ IS
'??????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.BUS_SOL_ID_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.REL_FORMSOL_ID_ IS
'??????????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.MAIN_PK_ IS
'???????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.REL_PK_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.STATUS_ IS
'??????(1????????? 0?????????)';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_BUS_INST_DATA.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_BUS_INST_DATA.TENANT_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_CHART_DATA_MODEL                                 */
/*==============================================================*/
CREATE TABLE FORM_CHART_DATA_MODEL  (
                                        ID_                  VARCHAR2(64)                    NOT NULL,
                                        CATEGORY_ID_         VARCHAR2(64),
                                        NAME_                VARCHAR2(64),
                                        DATA_SOURCE_         VARCHAR2(64),
                                        TABLES_              VARCHAR2(500),
                                        MODEL_CONFIG_        CLOB,
                                        TYPE_                VARCHAR2(64),
                                        SQL_MODE_            VARCHAR2(2000),
                                        TENANT_ID_           VARCHAR2(64),
                                        CREATE_DEP_ID_       VARCHAR2(64),
                                        CREATE_BY_           VARCHAR2(64),
                                        CREATE_TIME_         DATE                            NOT NULL,
                                        UPDATE_BY_           VARCHAR2(64),
                                        UPDATE_TIME_         DATE,
                                        APP_ID_              VARCHAR2(64),
                                        CONSTRAINT PK_FORM_CHART_DATA_MODEL PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CHART_DATA_MODEL IS
'??????????????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.ID_ IS
'??????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.DATA_SOURCE_ IS
'?????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TABLES_ IS
'?????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.MODEL_CONFIG_ IS
'????????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TYPE_ IS
'??????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.SQL_MODE_ IS
'?????????SQL??????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CHART_DATA_MODEL.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_CODEGEN_GLOBALVAR                                */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_GLOBALVAR  (
                                         ID_                  VARCHAR2(64)                    NOT NULL,
                                         CONFIG_              VARCHAR2(500),
                                         TENANT_ID_           VARCHAR2(64),
                                         CREATE_DEP_ID_       VARCHAR2(64),
                                         CREATE_BY_           VARCHAR2(64),
                                         CREATE_TIME_         DATE,
                                         UPDATE_BY_           VARCHAR2(64),
                                         UPDATE_TIME_         DATE,
                                         CONSTRAINT PK_FORM_CODEGEN_GLOBALVAR PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CODEGEN_GLOBALVAR IS
'????????????????????????';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.ID_ IS
'??????';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CONFIG_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CODEGEN_GLOBALVAR.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_CODEGEN_TEMPLATE                                 */
/*==============================================================*/
CREATE TABLE FORM_CODEGEN_TEMPLATE  (
                                        ID_                  VARCHAR2(64)                    NOT NULL,
                                        NAME_                VARCHAR2(64),
                                        FILE_NAME_           VARCHAR2(128),
                                        PATH_                VARCHAR2(128),
                                        SINGLE_              VARCHAR2(40),
                                        ENABLED_             VARCHAR2(40),
                                        CONTENT_             CLOB,
                                        TENANT_ID_           VARCHAR2(64),
                                        CREATE_DEP_ID_       VARCHAR2(64),
                                        CREATE_BY_           VARCHAR2(64),
                                        CREATE_TIME_         DATE,
                                        UPDATE_BY_           VARCHAR2(64),
                                        UPDATE_TIME_         DATE,
                                        CONSTRAINT PK_FORM_CODEGEN_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CODEGEN_TEMPLATE IS
'?????????????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.FILE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.PATH_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.SINGLE_ IS
'??????????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.ENABLED_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CONTENT_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CODEGEN_TEMPLATE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_CUSTOM                                           */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              CATEGORY_ID_         VARCHAR2(64),
                              NAME_                VARCHAR2(64),
                              ALIAS_               VARCHAR2(64),
                              TYPE_                VARCHAR2(40),
                              JSON_                CLOB,
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              APP_ID_              VARCHAR2(64),
                              HOME_PAGE_           SMALLINT                       DEFAULT 0,
                              CONSTRAINT PK_FORM_CUSTOM PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CUSTOM IS
'??????????????????';

COMMENT ON COLUMN FORM_CUSTOM.ID_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CUSTOM.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM.TYPE_ IS
'??????(form,??????,other,??????)';

COMMENT ON COLUMN FORM_CUSTOM.JSON_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CUSTOM.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CUSTOM.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CUSTOM.HOME_PAGE_ IS
'??????????????????0?????????1?????????';

/*==============================================================*/
/* Table: FORM_CUSTOM_QUERY                                     */
/*==============================================================*/
CREATE TABLE FORM_CUSTOM_QUERY  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    NAME_                VARCHAR2(64),
                                    KEY_                 VARCHAR2(64),
                                    TABLE_NAME_          VARCHAR2(64),
                                    IS_PAGE_             INTEGER,
                                    PAGE_SIZE_           INTEGER,
                                    WHERE_FIELD_         VARCHAR2(2000),
                                    RESULT_FIELD_        VARCHAR2(2000),
                                    ORDER_FIELD_         VARCHAR2(200),
                                    DS_ALIAS_            VARCHAR2(64),
                                    TABLE_               VARCHAR2(64),
                                    SQL_DIY_             VARCHAR2(2000),
                                    SQL_                 VARCHAR2(2000),
                                    SQL_BUILD_TYPE_      VARCHAR2(20),
                                    TREE_ID_             VARCHAR2(64),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    APP_ID_              VARCHAR2(64),
                                    CONSTRAINT PK_FORM_CUSTOM_QUERY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_CUSTOM_QUERY IS
'?????????SQL??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.ID_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.KEY_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TABLE_NAME_ IS
'????????????(??????????????????)';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.IS_PAGE_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.PAGE_SIZE_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.WHERE_FIELD_ IS
'??????????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.RESULT_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.ORDER_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.DS_ALIAS_ IS
'???????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TABLE_ IS
'??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_DIY_ IS
'GROOVYSQL??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_ IS
'SQL??????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.SQL_BUILD_TYPE_ IS
'SQL????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_CUSTOM_QUERY.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_DATASOURCE_DEF                                   */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_DEF  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      NAME_                VARCHAR2(64),
                                      ALIAS_               VARCHAR2(64),
                                      ENABLE_              VARCHAR2(20),
                                      SETTING_             VARCHAR2(3000),
                                      DB_TYPE_             VARCHAR2(20),
                                      INIT_ON_START_       VARCHAR2(40),
                                      APP_NAME_            VARCHAR2(255),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE,
                                      APP_ID_              VARCHAR2(64),
                                      CONSTRAINT PK_FORM_DATASOURCE_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_DATASOURCE_DEF IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ID_ IS
'??????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.ENABLE_ IS
'????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.SETTING_ IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.DB_TYPE_ IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.INIT_ON_START_ IS
'??????????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.APP_NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_DATASOURCE_DEF.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_DATASOURCE_SHARE                                 */
/*==============================================================*/
CREATE TABLE FORM_DATASOURCE_SHARE  (
                                        SHARE_ID_            VARCHAR2(64)                    NOT NULL,
                                        DS_ID_               VARCHAR2(64)                    NOT NULL,
                                        APP_ID_              VARCHAR2(64)                    NOT NULL,
                                        APP_NAME_            VARCHAR2(64)                   DEFAULT NULL,
                                        TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_TIME_         DATE                           DEFAULT NULL,
                                        UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                        UPDATE_TIME_         DATE                           DEFAULT NULL,
                                        CONSTRAINT PK_FORM_DATASOURCE_SHARE PRIMARY KEY (SHARE_ID_)
);

COMMENT ON TABLE FORM_DATASOURCE_SHARE IS
'???????????????';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.SHARE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.DS_ID_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.APP_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DATASOURCE_SHARE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_DEF_PERMISSION                                   */
/*==============================================================*/
CREATE TABLE FORM_DEF_PERMISSION  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      FORM_ID_             VARCHAR2(64),
                                      BO_DEF_ID_           VARCHAR2(64),
                                      BO_ALIAS_            VARCHAR2(40),
                                      LEVEL_               INTEGER,
                                      PERMISSION_          CLOB,
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE,
                                      APP_ID_              VARCHAR2(64),
                                      CONSTRAINT PK_FORM_DEF_PERMISSION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_DEF_PERMISSION IS
'??????????????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.ID_ IS
'??????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.FORM_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.BO_DEF_ID_ IS
'BO??????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.BO_ALIAS_ IS
'BO????????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.LEVEL_ IS
'?????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.PERMISSION_ IS
'????????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_DEF_PERMISSION.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_DEF_PERMISSION.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING                              */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING  (
                                           ID_                  VARCHAR2(64)                    NOT NULL,
                                           ROLE_ID_             VARCHAR2(64),
                                           ROLE_NAME_           VARCHAR2(64),
                                           DATA_TYPE_ID_        VARCHAR2(64),
                                           DATA_TYPE_NAME_      VARCHAR2(64),
                                           TENANT_ID_           VARCHAR2(64),
                                           CREATE_DEP_ID_       VARCHAR2(64),
                                           CREATE_BY_           VARCHAR2(64),
                                           CREATE_TIME_         DATE                            NOT NULL,
                                           UPDATE_BY_           VARCHAR2(64),
                                           UPDATE_TIME_         DATE,
                                           CONSTRAINT PK_FORM_ENTITY_DATA_SETTING PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_SETTING IS
'????????????????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ID_ IS
'??????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ROLE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.ROLE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.DATA_TYPE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.DATA_TYPE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_SETTING_DIC                          */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_SETTING_DIC  (
                                               ID_                  VARCHAR2(64)                    NOT NULL,
                                               SETTING_ID_          VARCHAR2(64),
                                               ID_VALUE_            VARCHAR2(64),
                                               TEXT_VALUE_          VARCHAR2(64),
                                               TENANT_ID_           VARCHAR2(64),
                                               CREATE_DEP_ID_       VARCHAR2(64),
                                               CREATE_BY_           VARCHAR2(64),
                                               CREATE_TIME_         DATE                            NOT NULL,
                                               UPDATE_BY_           VARCHAR2(64),
                                               UPDATE_TIME_         DATE,
                                               CONSTRAINT PK_FORM_ENTITY_DATA_SETTING_DI PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_SETTING_DIC IS
'?????????????????????????????? ';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.ID_ IS
'??????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.SETTING_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.ID_VALUE_ IS
'?????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.TEXT_VALUE_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_SETTING_DIC.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_ENTITY_DATA_TYPE                                 */
/*==============================================================*/
CREATE TABLE FORM_ENTITY_DATA_TYPE  (
                                        ID_                  VARCHAR2(64)                    NOT NULL,
                                        NAME_                VARCHAR2(64),
                                        DIALOG_ALIAS_        VARCHAR2(64),
                                        DIALOG_NAME_         VARCHAR2(64),
                                        ID_FIELD_            VARCHAR2(64),
                                        TEXT_FIELD_          VARCHAR2(64),
                                        STATUS_              VARCHAR2(40),
                                        TENANT_ID_           VARCHAR2(64),
                                        CREATE_DEP_ID_       VARCHAR2(64),
                                        CREATE_BY_           VARCHAR2(64),
                                        CREATE_TIME_         DATE                            NOT NULL,
                                        UPDATE_BY_           VARCHAR2(64),
                                        UPDATE_TIME_         DATE,
                                        CONSTRAINT PK_FORM_ENTITY_DATA_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_ENTITY_DATA_TYPE IS
'????????????????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.DIALOG_ALIAS_ IS
'???????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.DIALOG_NAME_ IS
'???????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.ID_FIELD_ IS
'?????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.TEXT_FIELD_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.STATUS_ IS
'??????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_ENTITY_DATA_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_FILE_TYPE                                        */
/*==============================================================*/
CREATE TABLE FORM_FILE_TYPE  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 TYPE_NAME_           VARCHAR2(64),
                                 CATEGORY_ID_         VARCHAR2(64),
                                 CATEGORY_NAME_       VARCHAR2(64),
                                 IS_REQUIRED_         INTEGER,
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 TENANT_ID_           VARCHAR2(64),
                                 CONSTRAINT PK_FORM_FILE_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_FILE_TYPE IS
'??????????????????';

COMMENT ON COLUMN FORM_FILE_TYPE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_FILE_TYPE.TYPE_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_FILE_TYPE.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_FILE_TYPE.CATEGORY_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_FILE_TYPE.IS_REQUIRED_ IS
'????????????(1?????????0??????)';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_FILE_TYPE.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN FORM_FILE_TYPE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_FILE_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_FILE_TYPE.TENANT_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_MOBILE                                           */
/*==============================================================*/
CREATE TABLE FORM_MOBILE  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(40),
                              CATEGORY_ID_         VARCHAR2(64),
                              ALIAS_               VARCHAR2(40),
                              FORM_HTML_           CLOB,
                              SCRIPT_              CLOB,
                              DEPLOYED_            INTEGER,
                              METADATA_            CLOB,
                              BODEF_ALIAS_         VARCHAR2(64),
                              BODEF_ID_            VARCHAR2(64),
                              GROUP_PERMISSIONS_   CLOB,
                              TYPE_                VARCHAR2(40),
                              BUTTON_DEF_          CLOB,
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              APP_ID_              VARCHAR2(64),
                              FORM_PC_ALIAS_       VARCHAR2(64),
                              CONSTRAINT PK_FORM_MOBILE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_MOBILE IS
'??????????????????';

COMMENT ON COLUMN FORM_MOBILE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_MOBILE.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_MOBILE.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_MOBILE.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_MOBILE.FORM_HTML_ IS
'??????HTML';

COMMENT ON COLUMN FORM_MOBILE.SCRIPT_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.DEPLOYED_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.METADATA_ IS
'?????????';

COMMENT ON COLUMN FORM_MOBILE.BODEF_ALIAS_ IS
'BO????????????';

COMMENT ON COLUMN FORM_MOBILE.BODEF_ID_ IS
'BO??????ID';

COMMENT ON COLUMN FORM_MOBILE.GROUP_PERMISSIONS_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.TYPE_ IS
'??????';

COMMENT ON COLUMN FORM_MOBILE.BUTTON_DEF_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_MOBILE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_MOBILE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_MOBILE.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_MOBILE.FORM_PC_ALIAS_ IS
'PC????????????';

/*==============================================================*/
/* Table: FORM_PC                                               */
/*==============================================================*/
CREATE TABLE FORM_PC  (
                          ID_                  VARCHAR2(64)                    NOT NULL,
                          NAME_                VARCHAR2(64),
                          CATEGORY_ID_         VARCHAR2(64),
                          ALIAS_               VARCHAR2(64),
                          TYPE_                VARCHAR2(40),
                          TEMPLATE_            CLOB,
                          JAVASCRIPT_          CLOB,
                          JAVASCRIPT_KEY_      CLOB,
                          METADATA_            CLOB,
                          DEPLOYED_            INTEGER,
                          BODEF_ID_            VARCHAR2(64),
                          BODEF_ALIAS_         VARCHAR2(128),
                          VERSION_             INTEGER,
                          MAIN_                INTEGER,
                          OPINION_DEF_         VARCHAR2(255),
                          BUTTON_DEF_          VARCHAR2(1000),
                          TABLE_BUTTON_DEF_    VARCHAR2(1000),
                          DATA_SETTING_        CLOB,
                          FORM_SETTING_        CLOB,
                          COMPONENT_           VARCHAR2(128),
                          WIZARD_              INTEGER,
                          TAB_DEF_             VARCHAR2(1000),
                          COPYED_              INTEGER,
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          APP_ID_              VARCHAR2(64),
                          DATASOURCE_          VARCHAR2(64),
                          CONSTRAINT PK_FORM_PC PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PC IS
'????????????';

COMMENT ON COLUMN FORM_PC.ID_ IS
'??????';

COMMENT ON COLUMN FORM_PC.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_PC.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PC.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_PC.TYPE_ IS
'????????????
ONLINE-DESIGN,GENBYBO';

COMMENT ON COLUMN FORM_PC.TEMPLATE_ IS
'??????';

COMMENT ON COLUMN FORM_PC.JAVASCRIPT_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.JAVASCRIPT_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN FORM_PC.METADATA_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.DEPLOYED_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.BODEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PC.BODEF_ALIAS_ IS
'??????????????????';

COMMENT ON COLUMN FORM_PC.VERSION_ IS
'?????????';

COMMENT ON COLUMN FORM_PC.MAIN_ IS
'?????????';

COMMENT ON COLUMN FORM_PC.OPINION_DEF_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.BUTTON_DEF_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.TABLE_BUTTON_DEF_ IS
'??????????????????';

COMMENT ON COLUMN FORM_PC.DATA_SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.FORM_SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.COMPONENT_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.WIZARD_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.TAB_DEF_ IS
'TAB??????';

COMMENT ON COLUMN FORM_PC.COPYED_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PC.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PC.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PC.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PC.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PC.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PC.DATASOURCE_ IS
'?????????';

/*==============================================================*/
/* Table: FORM_PC_HISTORY                                       */
/*==============================================================*/
CREATE TABLE FORM_PC_HISTORY  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  FORM_PC_ID_          VARCHAR2(64)                    NOT NULL,
                                  NAME_                VARCHAR2(64),
                                  ALIAS_               VARCHAR2(64),
                                  TEMPLATE_            CLOB,
                                  JAVASCRIPT_          CLOB,
                                  JAVASCRIPT_KEY_      CLOB,
                                  METADATA_            CLOB,
                                  OPINION_DEF_         VARCHAR2(255),
                                  BUTTON_DEF_          VARCHAR2(1000),
                                  TABLE_BUTTON_DEF_    VARCHAR2(1000),
                                  DATA_SETTING_        CLOB,
                                  FORM_SETTING_        CLOB,
                                  COMPONENT_           VARCHAR2(128),
                                  WIZARD_              INTEGER,
                                  TAB_DEF_             VARCHAR2(1000),
                                  REMARK_              VARCHAR2(255),
                                  TENANT_ID_           VARCHAR2(64),
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  CREATE_BY_           VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  UPDATE_TIME_         DATE,
                                  CONSTRAINT PK_FORM_PC_HISTORY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PC_HISTORY IS
'??????????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.ID_ IS
'??????';

COMMENT ON COLUMN FORM_PC_HISTORY.FORM_PC_ID_ IS
'??????????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_PC_HISTORY.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_PC_HISTORY.TEMPLATE_ IS
'??????';

COMMENT ON COLUMN FORM_PC_HISTORY.JAVASCRIPT_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.JAVASCRIPT_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN FORM_PC_HISTORY.METADATA_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.OPINION_DEF_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.BUTTON_DEF_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.TABLE_BUTTON_DEF_ IS
'??????????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.DATA_SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.FORM_SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.COMPONENT_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.WIZARD_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.TAB_DEF_ IS
'TAB??????';

COMMENT ON COLUMN FORM_PC_HISTORY.REMARK_ IS
'??????';

COMMENT ON COLUMN FORM_PC_HISTORY.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PC_HISTORY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PC_HISTORY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PC_HISTORY.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_PDF_TEMPLATE                                     */
/*==============================================================*/
CREATE TABLE FORM_PDF_TEMPLATE  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    NAME_                VARCHAR2(64),
                                    KEY_                 VARCHAR2(64),
                                    TREE_ID_             VARCHAR2(64),
                                    BO_DEF_ID_           VARCHAR2(64),
                                    PDF_HTML_            CLOB,
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    APP_ID_              VARCHAR2(64),
                                    CONSTRAINT PK_FORM_PDF_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PDF_TEMPLATE IS
'??????PDF??????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.KEY_ IS
'??????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.BO_DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.PDF_HTML_ IS
'??????HTML';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PDF_TEMPLATE.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_PERMISSION                                       */
/*==============================================================*/
CREATE TABLE FORM_PERMISSION  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  TYPE_                VARCHAR2(64),
                                  CONFIG_ID_           VARCHAR2(64),
                                  PERMISSION_          CLOB,
                                  TENANT_ID_           VARCHAR2(64),
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  CREATE_BY_           VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  UPDATE_TIME_         DATE,
                                  APP_ID_              VARCHAR2(64),
                                  CONSTRAINT PK_FORM_PERMISSION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PERMISSION IS
'??????????????????';

COMMENT ON COLUMN FORM_PERMISSION.ID_ IS
'??????';

COMMENT ON COLUMN FORM_PERMISSION.TYPE_ IS
'????????????
form,formSol';

COMMENT ON COLUMN FORM_PERMISSION.CONFIG_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PERMISSION.PERMISSION_ IS
'??????';

COMMENT ON COLUMN FORM_PERMISSION.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PERMISSION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PERMISSION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PERMISSION.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PERMISSION.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_PRINT_LODOP                                      */
/*==============================================================*/
CREATE TABLE FORM_PRINT_LODOP  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   NAME_                VARCHAR2(64),
                                   ALIAS_               VARCHAR2(20),
                                   BACK_IMG_            VARCHAR2(128),
                                   FORM_ID_             VARCHAR2(64),
                                   FORM_NAME_           VARCHAR2(64),
                                   TEMPLATE_            CLOB,
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   APP_ID_              VARCHAR2(64),
                                   CONSTRAINT PK_FORM_PRINT_LODOP PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_PRINT_LODOP IS
'??????????????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.ID_ IS
'??????';

COMMENT ON COLUMN FORM_PRINT_LODOP.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_PRINT_LODOP.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_PRINT_LODOP.BACK_IMG_ IS
'?????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.FORM_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.FORM_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.TEMPLATE_ IS
'????????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_PRINT_LODOP.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_PRINT_LODOP.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_QUERY_STRATEGY                                   */
/*==============================================================*/
CREATE TABLE FORM_QUERY_STRATEGY  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      NAME_                VARCHAR2(64),
                                      IS_PUBLIC_           VARCHAR2(40),
                                      QUERY_CONDITION_     VARCHAR2(2000),
                                      IS_USER_             VARCHAR2(64),
                                      LIST_ID_             VARCHAR2(64),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE,
                                      APP_ID_              VARCHAR2(64),
                                      CONSTRAINT PK_FORM_QUERY_STRATEGY PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_QUERY_STRATEGY IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.ID_ IS
'??????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.IS_PUBLIC_ IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.QUERY_CONDITION_ IS
'??????????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.IS_USER_ IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.LIST_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_QUERY_STRATEGY.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_REG_LIB                                          */
/*==============================================================*/
CREATE TABLE FORM_REG_LIB  (
                               REG_ID_              VARCHAR2(64)                    NOT NULL,
                               USER_ID_             VARCHAR2(64),
                               REG_TEXT_            VARCHAR2(512),
                               NAME_                VARCHAR2(64),
                               TYPE_                VARCHAR2(64),
                               KEY_                 VARCHAR2(64),
                               MENT_TEXT_           VARCHAR2(512),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               APP_ID_              VARCHAR2(64),
                               CONSTRAINT PK_FORM_REG_LIB PRIMARY KEY (REG_ID_)
);

COMMENT ON TABLE FORM_REG_LIB IS
'???????????????????????????';

COMMENT ON COLUMN FORM_REG_LIB.REG_ID_ IS
'??????';

COMMENT ON COLUMN FORM_REG_LIB.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_REG_LIB.REG_TEXT_ IS
'????????????';

COMMENT ON COLUMN FORM_REG_LIB.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_REG_LIB.TYPE_ IS
'??????';

COMMENT ON COLUMN FORM_REG_LIB.KEY_ IS
'??????';

COMMENT ON COLUMN FORM_REG_LIB.MENT_TEXT_ IS
'????????????';

COMMENT ON COLUMN FORM_REG_LIB.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_REG_LIB.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_REG_LIB.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_REG_LIB.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_REG_LIB.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_RULE                                             */
/*==============================================================*/
CREATE TABLE FORM_RULE  (
                            ID_                  VARCHAR2(64)                    NOT NULL,
                            NAME_                VARCHAR2(20),
                            PROMPT_              VARCHAR2(40),
                            ALIAS_               VARCHAR2(20),
                            REGULAR_             VARCHAR2(64),
                            TENANT_ID_           VARCHAR2(64),
                            CREATE_DEP_ID_       VARCHAR2(64),
                            CREATE_BY_           VARCHAR2(64),
                            CREATE_TIME_         DATE,
                            UPDATE_BY_           VARCHAR2(64),
                            UPDATE_TIME_         DATE,
                            APP_ID_              VARCHAR2(64),
                            CONSTRAINT PK_FORM_RULE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_RULE IS
'??????????????????';

COMMENT ON COLUMN FORM_RULE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_RULE.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_RULE.PROMPT_ IS
'?????????';

COMMENT ON COLUMN FORM_RULE.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_RULE.REGULAR_ IS
'???????????????';

COMMENT ON COLUMN FORM_RULE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_RULE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_RULE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_RULE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_RULE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_RULE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_RULE.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_SAVE_EXPORT                                      */
/*==============================================================*/
CREATE TABLE FORM_SAVE_EXPORT  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   NAME_                VARCHAR2(64),
                                   DATA_LIST_           VARCHAR2(64),
                                   SETTING_             CLOB,
                                   IS_PUBLIC_           INTEGER,
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   APP_ID_              VARCHAR2(64),
                                   MAX_COUNT_           INTEGER                         NOT NULL,
                                   CONSTRAINT PK_FORM_SAVE_EXPORT PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SAVE_EXPORT IS
'EXCEL????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.ID_ IS
'??????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.DATA_LIST_ IS
'BO????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.SETTING_ IS
'????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.IS_PUBLIC_ IS
'????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_SAVE_EXPORT.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SAVE_EXPORT.MAX_COUNT_ IS
'??????????????????';

/*==============================================================*/
/* Table: FORM_SOLUTION                                         */
/*==============================================================*/
CREATE TABLE FORM_SOLUTION  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                CATEGORY_ID_         VARCHAR2(64),
                                NAME_                VARCHAR2(64),
                                ALIAS_               VARCHAR2(64),
                                BODEF_ID_            VARCHAR2(64),
                                FORM_ID_             VARCHAR2(64),
                                FORM_NAME_           VARCHAR2(128),
                                MOBILE_FORM_ID_      VARCHAR2(64),
                                MOBILE_FORM_NAME_    VARCHAR2(128),
                                DATA_HANDLER_        VARCHAR2(255),
                                TREE_                INTEGER,
                                LOAD_MODE_           VARCHAR2(40),
                                DISPLAY_FIELDS_      VARCHAR2(64),
                                FORMULAS_            VARCHAR2(200),
                                FORMULAS_NAME_       VARCHAR2(200),
                                BUTTONS_SETTING_     CLOB,
                                NO_PK_SETTING_       VARCHAR2(255),
                                JAVA_CODE_           CLOB,
                                IS_GENERATE_TABLE_   INTEGER,
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                APP_ID_              VARCHAR2(64),
                                FLOW_DEF_MAPPING_    CLOB,
                                CONSTRAINT PK_FORM_SOLUTION PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SOLUTION IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SOLUTION.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SOLUTION.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_SOLUTION.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_SOLUTION.BODEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_SOLUTION.FORM_ID_ IS
'??????';

COMMENT ON COLUMN FORM_SOLUTION.FORM_NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.MOBILE_FORM_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_SOLUTION.MOBILE_FORM_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_SOLUTION.DATA_HANDLER_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_SOLUTION.TREE_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.LOAD_MODE_ IS
'??????????????????0,???????????????,1,?????????';

COMMENT ON COLUMN FORM_SOLUTION.DISPLAY_FIELDS_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.FORMULAS_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.FORMULAS_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_SOLUTION.BUTTONS_SETTING_ IS
'???????????????';

COMMENT ON COLUMN FORM_SOLUTION.NO_PK_SETTING_ IS
'??????????????????????????????';

COMMENT ON COLUMN FORM_SOLUTION.JAVA_CODE_ IS
'JAVA??????';

COMMENT ON COLUMN FORM_SOLUTION.IS_GENERATE_TABLE_ IS
'?????????????????????';

COMMENT ON COLUMN FORM_SOLUTION.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SOLUTION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SOLUTION.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_SOLUTION.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SOLUTION.FLOW_DEF_MAPPING_ IS
'??????????????????';

/*==============================================================*/
/* Table: FORM_SQL_LOG                                          */
/*==============================================================*/
CREATE TABLE FORM_SQL_LOG  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               TYPE_                VARCHAR2(40),
                               SQL_                 CLOB,
                               PARAMS_              CLOB,
                               REMARK_              CLOB,
                               IS_SUCCESS_          VARCHAR2(40),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_FORM_SQL_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_SQL_LOG IS
'??????????????????';

COMMENT ON COLUMN FORM_SQL_LOG.ID_ IS
'??????';

COMMENT ON COLUMN FORM_SQL_LOG.TYPE_ IS
'????????????';

COMMENT ON COLUMN FORM_SQL_LOG.SQL_ IS
'SQL??????';

COMMENT ON COLUMN FORM_SQL_LOG.PARAMS_ IS
'????????????';

COMMENT ON COLUMN FORM_SQL_LOG.REMARK_ IS
'????????????';

COMMENT ON COLUMN FORM_SQL_LOG.IS_SUCCESS_ IS
'????????????';

COMMENT ON COLUMN FORM_SQL_LOG.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SQL_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_SQL_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_SQL_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: FORM_TABLE_FORMULA                                    */
/*==============================================================*/
CREATE TABLE FORM_TABLE_FORMULA  (
                                     ID_                  VARCHAR2(64)                    NOT NULL,
                                     NAME_                VARCHAR2(256)                   NOT NULL,
                                     DESCP_               VARCHAR2(512),
                                     TREE_ID_             VARCHAR2(64),
                                     FILL_CONF_           CLOB,
                                     DS_NAME_             VARCHAR2(100),
                                     BO_DEF_ID_           VARCHAR2(64)                    NOT NULL,
                                     BO_DEF_NAME_         VARCHAR2(64),
                                     ACTION_              VARCHAR2(80),
                                     SYS_ID_              VARCHAR2(64),
                                     IS_TEST_             VARCHAR2(80),
                                     ENABLED_             VARCHAR2(20),
                                     TENANT_ID_           VARCHAR2(64),
                                     CREATE_DEP_ID_       VARCHAR2(64),
                                     CREATE_TIME_         DATE,
                                     CREATE_BY_           VARCHAR2(64),
                                     UPDATE_BY_           VARCHAR2(64),
                                     UPDATE_TIME_         DATE,
                                     APP_ID_              VARCHAR2(64),
                                     CONSTRAINT PK_FORM_TABLE_FORMULA PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_TABLE_FORMULA IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ID_ IS
'??????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.NAME_ IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.DESCP_ IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.FILL_CONF_ IS
'??????????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.DS_NAME_ IS
'?????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.BO_DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.BO_DEF_NAME_ IS
'??????????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ACTION_ IS
'??????????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.SYS_ID_ IS
'?????????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.IS_TEST_ IS
'????????????????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.ENABLED_ IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_TABLE_FORMULA.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_TABLE_FORMULA.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: FORM_TEMPLATE                                         */
/*==============================================================*/
CREATE TABLE FORM_TEMPLATE  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                NAME_                VARCHAR2(64),
                                ALIAS_               VARCHAR2(20),
                                TEMPLATE_            CLOB,
                                TYPE_                VARCHAR2(64),
                                CATEGORY_            VARCHAR2(64),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                APP_ID_              VARCHAR2(64),
                                CONSTRAINT PK_FORM_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE FORM_TEMPLATE IS
'????????????';

COMMENT ON COLUMN FORM_TEMPLATE.ID_ IS
'??????';

COMMENT ON COLUMN FORM_TEMPLATE.NAME_ IS
'??????';

COMMENT ON COLUMN FORM_TEMPLATE.ALIAS_ IS
'??????';

COMMENT ON COLUMN FORM_TEMPLATE.TEMPLATE_ IS
'????????????';

COMMENT ON COLUMN FORM_TEMPLATE.TYPE_ IS
'??????';

COMMENT ON COLUMN FORM_TEMPLATE.CATEGORY_ IS
'??????';

COMMENT ON COLUMN FORM_TEMPLATE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_TEMPLATE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_TEMPLATE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN FORM_TEMPLATE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN FORM_TEMPLATE.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: GRID_REPORT_DESIGN                                    */
/*==============================================================*/
CREATE TABLE GRID_REPORT_DESIGN  (
                                     ID_                  VARCHAR2(64)                    NOT NULL,
                                     NAME_                VARCHAR2(64)                   DEFAULT NULL,
                                     KEY_                 VARCHAR2(64)                   DEFAULT NULL,
                                     GRF_                 VARCHAR2(64)                   DEFAULT NULL,
                                     QUERY_CONFIG_        CLOB,
                                     CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                     CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                     CREATE_TIME_         DATE                           DEFAULT NULL,
                                     UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                     UPDATE_TIME_         DATE                           DEFAULT NULL,
                                     TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                     REF_ID_              VARCHAR2(64)                   DEFAULT NULL,
                                     PARENT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                     TREE_ID_             VARCHAR2(64)                   DEFAULT NULL,
                                     DB_AS_               VARCHAR2(64)                   DEFAULT NULL,
                                     DOC_ID_              VARCHAR2(64)                   DEFAULT NULL,
                                     SQL_                 VARCHAR2(500)                  DEFAULT NULL,
                                     USE_COND_SQL_TYPE_   VARCHAR2(16)                   DEFAULT NULL,
                                     USE_COND_SQL_        VARCHAR2(16)                   DEFAULT NULL,
                                     COND_SQLS_           VARCHAR2(500)                  DEFAULT NULL,
                                     WEB_REQ_SCRIPT_      CLOB,
                                     WEB_REQ_MAPPING_JSON_ CLOB,
                                     WEB_REQ_KEY_         CLOB,
                                     INTERFACE_KEY_       CLOB,
                                     INTERFACE_MAPPING_JSON_ CLOB,
                                     CONSTRAINT PK_GRID_REPORT_DESIGN PRIMARY KEY (ID_),
                                     CONSTRAINT "IDX_GRID_REPORT_KEY" UNIQUE (KEY_)
);

COMMENT ON TABLE GRID_REPORT_DESIGN IS
'GridReport????????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.ID_ IS
'??????ID';

COMMENT ON COLUMN GRID_REPORT_DESIGN.NAME_ IS
'??????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.KEY_ IS
'??????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.GRF_ IS
'??????????????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.QUERY_CONFIG_ IS
'??????????????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN GRID_REPORT_DESIGN.TENANT_ID_ IS
'????????????ID';

COMMIT ;

