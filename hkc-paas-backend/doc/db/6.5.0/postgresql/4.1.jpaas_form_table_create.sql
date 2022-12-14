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

commit ;

