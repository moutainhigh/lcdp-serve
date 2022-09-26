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
/* Table: INF_INBOX                                             */
/*==============================================================*/
CREATE TABLE INF_INBOX (
                           REC_ID_              VARCHAR(64)          NULL,
                           MSG_ID_              VARCHAR(64)          NULL,
                           REC_TYPE_            VARCHAR(40)          NULL,
                           REC_USER_ID_         VARCHAR(64)          NULL,
                           REC_USER_NAME_       VARCHAR(64)          NULL,
                           TENANT_ID_           VARCHAR(64)          NULL,
                           CREATE_DEP_ID_       VARCHAR(64)          NULL,
                           CREATE_BY_           VARCHAR(64)          NULL,
                           CREATE_TIME_         DATE                 NULL,
                           UPDATE_BY_           VARCHAR(64)          NULL,
                           UPDATE_TIME_         DATE                 NULL
);

COMMENT ON TABLE INF_INBOX IS
'内部短消息收件箱';

COMMENT ON COLUMN INF_INBOX.REC_ID_ IS
'主键';

COMMENT ON COLUMN INF_INBOX.MSG_ID_ IS
'消息ID';

COMMENT ON COLUMN INF_INBOX.REC_TYPE_ IS
'用户=USER 用户组=GROUP';

COMMENT ON COLUMN INF_INBOX.REC_USER_ID_ IS
'接收人ID';

COMMENT ON COLUMN INF_INBOX.REC_USER_NAME_ IS
'接收人名称';

COMMENT ON COLUMN INF_INBOX.TENANT_ID_ IS
'租用机构ID';

COMMENT ON COLUMN INF_INBOX.CREATE_DEP_ID_ IS
'创建人部门ID';

COMMENT ON COLUMN INF_INBOX.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INF_INBOX.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INF_INBOX.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INF_INBOX.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INF_INNER_MSG                                         */
/*==============================================================*/
CREATE TABLE INF_INNER_MSG (
                               MSG_ID_              VARCHAR(64)          NOT NULL,
                               MSG_TITLE_           VARCHAR(128)         NULL,
                               CONTENT_             TEXT                 NULL,
                               CATEGORY_            VARCHAR(40)          NULL,
                               SENDER_ID_           VARCHAR(64)          NULL,
                               SENDER_              VARCHAR(64)          NULL,
                               DEL_FLAG_            VARCHAR(64)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               CONSTRAINT PK_INF_INNER_MSG PRIMARY KEY (MSG_ID_)
);

COMMENT ON TABLE INF_INNER_MSG IS
'内部短消息';

COMMENT ON COLUMN INF_INNER_MSG.MSG_ID_ IS
'主键';

COMMENT ON COLUMN INF_INNER_MSG.MSG_TITLE_ IS
'消息标题';

COMMENT ON COLUMN INF_INNER_MSG.CONTENT_ IS
'消息内容';

COMMENT ON COLUMN INF_INNER_MSG.CATEGORY_ IS
'消息分类';

COMMENT ON COLUMN INF_INNER_MSG.SENDER_ID_ IS
'发送人ID';

COMMENT ON COLUMN INF_INNER_MSG.SENDER_ IS
'发送人名称';

COMMENT ON COLUMN INF_INNER_MSG.DEL_FLAG_ IS
'删除标记';

COMMENT ON COLUMN INF_INNER_MSG.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INF_INNER_MSG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INF_INNER_MSG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INF_INNER_MSG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INF_INNER_MSG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INF_INNER_MSG.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INF_INNER_MSG_LOG                                     */
/*==============================================================*/
CREATE TABLE INF_INNER_MSG_LOG (
                                   ID_                  VARCHAR(64)          NOT NULL,
                                   MSG_ID_              VARCHAR(64)          NULL,
                                   REC_USER_ID_         VARCHAR(64)          NULL,
                                   IS_READ_             VARCHAR(40)          NULL,
                                   IS_DEL_              VARCHAR(40)          NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   CONSTRAINT PK_INF_INNER_MSG_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE INF_INNER_MSG_LOG IS
'内部消息查看记录';

COMMENT ON COLUMN INF_INNER_MSG_LOG.ID_ IS
'主键';

COMMENT ON COLUMN INF_INNER_MSG_LOG.MSG_ID_ IS
'消息ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.REC_USER_ID_ IS
'接收人ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.IS_READ_ IS
'是否已读';

COMMENT ON COLUMN INF_INNER_MSG_LOG.IS_DEL_ IS
'是否删除';

COMMENT ON COLUMN INF_INNER_MSG_LOG.TENANT_ID_ IS
'租用机构ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.CREATE_DEP_ID_ IS
'创建人部门ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INF_INNER_MSG_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INF_INNER_MSG_LOG.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INS_APP_COLLECT                                       */
/*==============================================================*/
CREATE TABLE INS_APP_COLLECT (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 NAME_                VARCHAR(150)         NULL,
                                 URL_                 VARCHAR(200)         NULL,
                                 TYPE_                VARCHAR(64)          NULL,
                                 OWNER_ID_            VARCHAR(500)         NULL,
                                 DESCRIPTION_         VARCHAR(200)         NULL,
                                 SN_                  INT4                 NULL,
                                 ICON_                VARCHAR(200)         NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_INS_APP_COLLECT PRIMARY KEY (ID_)
);

COMMENT ON TABLE INS_APP_COLLECT IS
'常用应用管理';

COMMENT ON COLUMN INS_APP_COLLECT.ID_ IS
'主键';

COMMENT ON COLUMN INS_APP_COLLECT.NAME_ IS
'应用名称';

COMMENT ON COLUMN INS_APP_COLLECT.URL_ IS
'应用链接地址';

COMMENT ON COLUMN INS_APP_COLLECT.TYPE_ IS
'类型 内部：interior 外部：outside';

COMMENT ON COLUMN INS_APP_COLLECT.OWNER_ID_ IS
'用户或组ID';

COMMENT ON COLUMN INS_APP_COLLECT.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN INS_APP_COLLECT.SN_ IS
'序号';

COMMENT ON COLUMN INS_APP_COLLECT.ICON_ IS
'图标';

COMMENT ON COLUMN INS_APP_COLLECT.TENANT_ID_ IS
'租用机构ID';

COMMENT ON COLUMN INS_APP_COLLECT.CREATE_DEP_ID_ IS
'创建人部门ID';

COMMENT ON COLUMN INS_APP_COLLECT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_APP_COLLECT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_APP_COLLECT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_APP_COLLECT.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INS_COLUMN_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_COLUMN_DEF (
                                COL_ID_              VARCHAR(64)          NOT NULL,
                                NAME_                VARCHAR(100)         NULL,
                                KEY_                 VARCHAR(64)          NULL,
                                IS_DEFAULT_          VARCHAR(20)          NULL,
                                TEMPLET_             VARCHAR(4000)        NULL,
                                SET_TING_            VARCHAR(4000)        NULL,
                                IS_PUBLIC_           VARCHAR(4)           NULL,
                                TYPE_                VARCHAR(50)          NULL,
                                ICON_                VARCHAR(64)          NULL,
                                IS_MOBILE_           VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                APP_ID_              VARCHAR(64)          NULL,
                                CONSTRAINT PK_INS_COLUMN_DEF PRIMARY KEY (COL_ID_)
);

COMMENT ON TABLE INS_COLUMN_DEF IS
'栏目定义';

COMMENT ON COLUMN INS_COLUMN_DEF.COL_ID_ IS
'栏目ID';

COMMENT ON COLUMN INS_COLUMN_DEF.NAME_ IS
'栏目名';

COMMENT ON COLUMN INS_COLUMN_DEF.KEY_ IS
'栏目别名';

COMMENT ON COLUMN INS_COLUMN_DEF.IS_DEFAULT_ IS
'是否默认';

COMMENT ON COLUMN INS_COLUMN_DEF.TEMPLET_ IS
'模板';

COMMENT ON COLUMN INS_COLUMN_DEF.SET_TING_ IS
'Tab标签组';

COMMENT ON COLUMN INS_COLUMN_DEF.IS_PUBLIC_ IS
'是否公共栏目';

COMMENT ON COLUMN INS_COLUMN_DEF.TYPE_ IS
'类型';

COMMENT ON COLUMN INS_COLUMN_DEF.ICON_ IS
'图标';

COMMENT ON COLUMN INS_COLUMN_DEF.IS_MOBILE_ IS
'是否自定义移动栏目';

COMMENT ON COLUMN INS_COLUMN_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_COLUMN_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_COLUMN_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_COLUMN_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_COLUMN_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_COLUMN_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_COLUMN_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: INS_COLUMN_TEMP                                       */
/*==============================================================*/
CREATE TABLE INS_COLUMN_TEMP (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 NAME_                VARCHAR(64)          NULL,
                                 KEY_                 VARCHAR(64)          NULL,
                                 TEMPLET_             TEXT                 NULL,
                                 IS_SYS_              VARCHAR(20)          NULL,
                                 STATUS_              VARCHAR(20)          NULL,
                                 TEMP_TYPE_           VARCHAR(40)          NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_INS_COLUMN_TEMP PRIMARY KEY (ID_)
);

COMMENT ON TABLE INS_COLUMN_TEMP IS
'栏目模板';

COMMENT ON COLUMN INS_COLUMN_TEMP.ID_ IS
'主键';

COMMENT ON COLUMN INS_COLUMN_TEMP.NAME_ IS
'名称';

COMMENT ON COLUMN INS_COLUMN_TEMP.KEY_ IS
'编码';

COMMENT ON COLUMN INS_COLUMN_TEMP.TEMPLET_ IS
'模板';

COMMENT ON COLUMN INS_COLUMN_TEMP.IS_SYS_ IS
'是否系统';

COMMENT ON COLUMN INS_COLUMN_TEMP.STATUS_ IS
'状态';

COMMENT ON COLUMN INS_COLUMN_TEMP.TEMP_TYPE_ IS
'模版类型';

COMMENT ON COLUMN INS_COLUMN_TEMP.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_COLUMN_TEMP.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_COLUMN_TEMP.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_COLUMN_TEMP.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_COLUMN_TEMP.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_COLUMN_TEMP.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INS_MSGBOX_BOX_DEF                                    */
/*==============================================================*/
CREATE TABLE INS_MSGBOX_BOX_DEF (
                                    ID_                  VARCHAR(64)          NOT NULL,
                                    BOX_ID_              VARCHAR(64)          NULL,
                                    MSG_ID_              VARCHAR(64)          NULL,
                                    SN_                  INT4                 NULL,
                                    TENANT_ID_           VARCHAR(64)          NULL,
                                    CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                    CREATE_BY_           VARCHAR(64)          NULL,
                                    CREATE_TIME_         DATE                 NULL,
                                    UPDATE_BY_           VARCHAR(64)          NULL,
                                    UPDATE_TIME_         DATE                 NULL,
                                    APP_ID_              VARCHAR(64)          NULL,
                                    CONSTRAINT PK_INS_MSGBOX_BOX_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE INS_MSGBOX_BOX_DEF IS
'消息盒子关系定义';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.ID_ IS
'主键';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.BOX_ID_ IS
'盒子定义ID';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.MSG_ID_ IS
'消息项ID';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.SN_ IS
'序号';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_MSGBOX_BOX_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: INS_MSGBOX_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_MSGBOX_DEF (
                                BOX_ID_              VARCHAR(64)          NOT NULL,
                                KEY_                 VARCHAR(64)          NULL,
                                NAME_                VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                APP_ID_              VARCHAR(64)          NULL,
                                CONSTRAINT PK_INS_MSGBOX_DEF PRIMARY KEY (BOX_ID_)
);

COMMENT ON TABLE INS_MSGBOX_DEF IS
'消息盒子定义';

COMMENT ON COLUMN INS_MSGBOX_DEF.BOX_ID_ IS
'主键';

COMMENT ON COLUMN INS_MSGBOX_DEF.KEY_ IS
'消息盒子KEY';

COMMENT ON COLUMN INS_MSGBOX_DEF.NAME_ IS
'消息盒子名称';

COMMENT ON COLUMN INS_MSGBOX_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_MSGBOX_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_MSGBOX_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_MSGBOX_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_MSGBOX_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_MSGBOX_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_MSGBOX_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: INS_MSG_DEF                                           */
/*==============================================================*/
CREATE TABLE INS_MSG_DEF (
                             MSG_ID_              VARCHAR(64)          NOT NULL,
                             COLOR_               VARCHAR(64)          NULL,
                             URL_                 VARCHAR(200)         NULL,
                             ICON_                VARCHAR(64)          NULL,
                             CONTENT_             VARCHAR(128)         NULL,
                             DS_NAME_             VARCHAR(64)          NULL,
                             DS_ALIAS_            VARCHAR(64)          NULL,
                             SQL_FUNC_            VARCHAR(2000)        NULL,
                             TYPE_                VARCHAR(64)          NULL,
                             COUNT_TYPE_          VARCHAR(64)          NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             APP_ID_              VARCHAR(64)          NULL,
                             CONSTRAINT PK_INS_MSG_DEF PRIMARY KEY (MSG_ID_)
);

COMMENT ON TABLE INS_MSG_DEF IS
'消息项目定义 ';

COMMENT ON COLUMN INS_MSG_DEF.MSG_ID_ IS
'主键';

COMMENT ON COLUMN INS_MSG_DEF.COLOR_ IS
'颜色';

COMMENT ON COLUMN INS_MSG_DEF.URL_ IS
'URL';

COMMENT ON COLUMN INS_MSG_DEF.ICON_ IS
'图标定义';

COMMENT ON COLUMN INS_MSG_DEF.CONTENT_ IS
'标题';

COMMENT ON COLUMN INS_MSG_DEF.DS_NAME_ IS
'数据源名称';

COMMENT ON COLUMN INS_MSG_DEF.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN INS_MSG_DEF.SQL_FUNC_ IS
'SQL语句';

COMMENT ON COLUMN INS_MSG_DEF.TYPE_ IS
'类型';

COMMENT ON COLUMN INS_MSG_DEF.COUNT_TYPE_ IS
'数量比较类型';

COMMENT ON COLUMN INS_MSG_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_MSG_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_MSG_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_MSG_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_MSG_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_MSG_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_MSG_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: INS_NEWS                                              */
/*==============================================================*/
CREATE TABLE INS_NEWS (
                          NEW_ID_              VARCHAR(64)          NOT NULL,
                          SUBJECT_             VARCHAR(128)         NULL,
                          KEYWORDS_            VARCHAR(128)         NULL,
                          CONTENT_             TEXT                 NULL,
                          SYS_DIC_NEW_         VARCHAR(64)          NULL,
                          IMG_FILE_ID_         VARCHAR(128)         NULL,
                          READ_TIMES_          INT4                 NULL,
                          AUTHOR_              VARCHAR(64)          NULL,
                          STATUS_              VARCHAR(40)          NULL,
                          FILES_               VARCHAR(500)         NULL,
                          TENANT_ID_           VARCHAR(64)          NULL,
                          CREATE_DEP_ID_       VARCHAR(64)          NULL,
                          CREATE_BY_           VARCHAR(64)          NULL,
                          CREATE_TIME_         DATE                 NULL,
                          UPDATE_BY_           VARCHAR(64)          NULL,
                          UPDATE_TIME_         DATE                 NULL,
                          CONSTRAINT PK_INS_NEWS PRIMARY KEY (NEW_ID_)
);

COMMENT ON TABLE INS_NEWS IS
'新闻公告';

COMMENT ON COLUMN INS_NEWS.NEW_ID_ IS
'主键';

COMMENT ON COLUMN INS_NEWS.SUBJECT_ IS
'标题';

COMMENT ON COLUMN INS_NEWS.KEYWORDS_ IS
'关键字';

COMMENT ON COLUMN INS_NEWS.CONTENT_ IS
'内容';

COMMENT ON COLUMN INS_NEWS.SYS_DIC_NEW_ IS
'分类ID';

COMMENT ON COLUMN INS_NEWS.IMG_FILE_ID_ IS
'图片文件ID';

COMMENT ON COLUMN INS_NEWS.READ_TIMES_ IS
'读取次数';

COMMENT ON COLUMN INS_NEWS.AUTHOR_ IS
'作者';

COMMENT ON COLUMN INS_NEWS.STATUS_ IS
'状态';

COMMENT ON COLUMN INS_NEWS.FILES_ IS
'附件';

COMMENT ON COLUMN INS_NEWS.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_NEWS.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_NEWS.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_NEWS.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_NEWS.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_NEWS.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INS_PORTAL_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_PORTAL_DEF (
                                PORT_ID_             VARCHAR(64)          NOT NULL,
                                NAME_                VARCHAR(128)         NULL,
                                KEY_                 VARCHAR(64)          NULL,
                                IS_DEFAULT_          VARCHAR(64)          NULL,
                                LAYOUT_HTML_         TEXT                 NULL,
                                PRIORITY_            INT4                 NULL,
                                IS_MOBILE_           VARCHAR(40)          NULL,
                                LAYOUT_JSON_         TEXT                 NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                APP_ID_              VARCHAR(64)          NULL,
                                CONSTRAINT PK_INS_PORTAL_DEF PRIMARY KEY (PORT_ID_)
);

COMMENT ON TABLE INS_PORTAL_DEF IS
'门户定义';

COMMENT ON COLUMN INS_PORTAL_DEF.PORT_ID_ IS
'门户ID';

COMMENT ON COLUMN INS_PORTAL_DEF.NAME_ IS
'名称';

COMMENT ON COLUMN INS_PORTAL_DEF.KEY_ IS
'别名';

COMMENT ON COLUMN INS_PORTAL_DEF.IS_DEFAULT_ IS
'是否默认';

COMMENT ON COLUMN INS_PORTAL_DEF.LAYOUT_HTML_ IS
'布局HTML';

COMMENT ON COLUMN INS_PORTAL_DEF.PRIORITY_ IS
'优先级';

COMMENT ON COLUMN INS_PORTAL_DEF.IS_MOBILE_ IS
'是否手机门户';

COMMENT ON COLUMN INS_PORTAL_DEF.LAYOUT_JSON_ IS
'门户布局';

COMMENT ON COLUMN INS_PORTAL_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_PORTAL_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_PORTAL_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_PORTAL_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_PORTAL_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_PORTAL_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_PORTAL_DEF.APP_ID_ IS
'应用ID';

/*==============================================================*/
/* Table: INS_PORTAL_PERMISSION                                 */
/*==============================================================*/
CREATE TABLE INS_PORTAL_PERMISSION (
                                       ID_                  VARCHAR(64)          NOT NULL,
                                       LAYOUT_ID_           VARCHAR(64)          NULL,
                                       TYPE_                VARCHAR(32)          NULL,
                                       OWNER_ID_            VARCHAR(32)          NULL,
                                       OWNER_NAME_          VARCHAR(64)          NULL,
                                       MENU_TYPE_           VARCHAR(64)          NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL,
                                       CREATE_TIME_         DATE                 NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL,
                                       UPDATE_TIME_         DATE                 NULL,
                                       CONSTRAINT PK_INS_PORTAL_PERMISSION PRIMARY KEY (ID_)
);

COMMENT ON TABLE INS_PORTAL_PERMISSION IS
'布局权限设置';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.ID_ IS
'权限ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.LAYOUT_ID_ IS
'门户ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.TYPE_ IS
'类型';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.OWNER_ID_ IS
'用户或组ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.OWNER_NAME_ IS
'用户或组名称';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.MENU_TYPE_ IS
'菜单类型';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_PORTAL_PERMISSION.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: INS_REMIND_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_REMIND_DEF (
                                ID_                  VARCHAR(64)          NOT NULL,
                                SUBJECT_             VARCHAR(150)         NULL,
                                URL_                 VARCHAR(300)         NULL,
                                TYPE_                VARCHAR(64)          NULL,
                                SETTING_             TEXT                 NULL,
                                DESCRIPTION_         VARCHAR(200)         NULL,
                                SN_                  INT4                 NULL,
                                ENABLED_             VARCHAR(64)          NULL,
                                ICON_                VARCHAR(64)          NULL,
                                DS_NAME_             VARCHAR(64)          NULL,
                                DS_ALIAS_            VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CONSTRAINT PK_INS_REMIND_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE INS_REMIND_DEF IS
'消息提醒';

COMMENT ON COLUMN INS_REMIND_DEF.ID_ IS
'主键';

COMMENT ON COLUMN INS_REMIND_DEF.SUBJECT_ IS
'主题';

COMMENT ON COLUMN INS_REMIND_DEF.URL_ IS
'连接地址';

COMMENT ON COLUMN INS_REMIND_DEF.TYPE_ IS
'设置类型';

COMMENT ON COLUMN INS_REMIND_DEF.SETTING_ IS
'sql语句或者方法';

COMMENT ON COLUMN INS_REMIND_DEF.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN INS_REMIND_DEF.SN_ IS
'序号';

COMMENT ON COLUMN INS_REMIND_DEF.ENABLED_ IS
'是否有效';

COMMENT ON COLUMN INS_REMIND_DEF.ICON_ IS
'图标';

COMMENT ON COLUMN INS_REMIND_DEF.DS_NAME_ IS
'数据源名称';

COMMENT ON COLUMN INS_REMIND_DEF.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN INS_REMIND_DEF.TENANT_ID_ IS
'租用机构ID';

COMMENT ON COLUMN INS_REMIND_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN INS_REMIND_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN INS_REMIND_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN INS_REMIND_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN INS_REMIND_DEF.CREATE_DEP_ID_ IS
'创建人部门ID';

commit ;

