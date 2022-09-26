use jpaas_portal;
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
/* Table: INF_INBOX                                             */
/*==============================================================*/
CREATE TABLE INF_INBOX
(
    REC_ID_              VARCHAR(64) COMMENT '主键',
    MSG_ID_              VARCHAR(64) COMMENT '消息ID',
    REC_TYPE_            VARCHAR(40) COMMENT '用户=USER 用户组=GROUP',
    REC_USER_ID_         VARCHAR(64) COMMENT '接收人ID',
    REC_USER_NAME_       VARCHAR(64) COMMENT '接收人名称',
    TENANT_ID_           VARCHAR(64) COMMENT '租用机构ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建人部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间'
);

ALTER TABLE INF_INBOX COMMENT '内部短消息收件箱';

/*==============================================================*/
/* Table: INF_INNER_MSG                                         */
/*==============================================================*/
CREATE TABLE INF_INNER_MSG
(
    MSG_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    MSG_TITLE_           VARCHAR(128) COMMENT '消息标题',
    CONTENT_             TEXT COMMENT '消息内容',
    CATEGORY_            VARCHAR(40) COMMENT '消息分类',
    SENDER_ID_           VARCHAR(64) COMMENT '发送人ID',
    SENDER_              VARCHAR(64) COMMENT '发送人名称',
    DEL_FLAG_            VARCHAR(64) COMMENT '删除标记',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (MSG_ID_)
);

ALTER TABLE INF_INNER_MSG COMMENT '内部短消息';

/*==============================================================*/
/* Table: INF_INNER_MSG_LOG                                     */
/*==============================================================*/
CREATE TABLE INF_INNER_MSG_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    MSG_ID_              VARCHAR(64) COMMENT '消息ID',
    REC_USER_ID_         VARCHAR(64) COMMENT '接收人ID',
    IS_READ_             VARCHAR(40) COMMENT '是否已读',
    IS_DEL_              VARCHAR(40) COMMENT '是否删除',
    TENANT_ID_           VARCHAR(64) COMMENT '租用机构ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建人部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE INF_INNER_MSG_LOG COMMENT '内部消息查看记录';

/*==============================================================*/
/* Table: INS_APP_COLLECT                                       */
/*==============================================================*/
CREATE TABLE INS_APP_COLLECT
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(150) COMMENT '应用名称',
    URL_                 VARCHAR(200) COMMENT '应用链接地址',
    TYPE_                VARCHAR(64) COMMENT '类型 内部：interior 外部：outside',
    OWNER_ID_            VARCHAR(500) COMMENT '用户或组ID',
    DESCRIPTION_         VARCHAR(200) COMMENT '描述',
    SN_                  INT COMMENT '序号',
    ICON_                VARCHAR(200) COMMENT '图标',
    TENANT_ID_           VARCHAR(64) COMMENT '租用机构ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建人部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE INS_APP_COLLECT COMMENT '常用应用管理';

/*==============================================================*/
/* Table: INS_COLUMN_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_COLUMN_DEF
(
    COL_ID_              VARCHAR(64) NOT NULL COMMENT '栏目ID',
    NAME_                VARCHAR(100) COMMENT '栏目名',
    KEY_                 VARCHAR(64) COMMENT '栏目别名',
    IS_DEFAULT_          VARCHAR(20) COMMENT '是否默认',
    TEMPLET_             VARCHAR(4000) COMMENT '模板',
    SET_TING_            VARCHAR(4000) COMMENT 'Tab标签组',
    IS_PUBLIC_           VARCHAR(4) COMMENT '是否公共栏目',
    TYPE_                VARCHAR(50) COMMENT '类型',
    ICON_                VARCHAR(64) COMMENT '图标',
    IS_MOBILE_           VARCHAR(64) COMMENT '是否自定义移动栏目',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (COL_ID_)
);

ALTER TABLE INS_COLUMN_DEF COMMENT '栏目定义';

/*==============================================================*/
/* Table: INS_COLUMN_TEMP                                       */
/*==============================================================*/
CREATE TABLE INS_COLUMN_TEMP
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(64) COMMENT '名称',
    KEY_                 VARCHAR(64) COMMENT '编码',
    TEMPLET_             TEXT COMMENT '模板',
    IS_SYS_              VARCHAR(20) COMMENT '是否系统',
    STATUS_              VARCHAR(20) COMMENT '状态',
    TEMP_TYPE_           VARCHAR(40) COMMENT '模版类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE INS_COLUMN_TEMP COMMENT '栏目模板';

/*==============================================================*/
/* Table: INS_MSGBOX_BOX_DEF                                    */
/*==============================================================*/
CREATE TABLE INS_MSGBOX_BOX_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    BOX_ID_              VARCHAR(64) COMMENT '盒子定义ID',
    MSG_ID_              VARCHAR(64) COMMENT '消息项ID',
    SN_                  INT COMMENT '序号',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE INS_MSGBOX_BOX_DEF COMMENT '消息盒子关系定义';

/*==============================================================*/
/* Table: INS_MSGBOX_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_MSGBOX_DEF
(
    BOX_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    KEY_                 VARCHAR(64) COMMENT '消息盒子KEY',
    NAME_                VARCHAR(64) COMMENT '消息盒子名称',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (BOX_ID_)
);

ALTER TABLE INS_MSGBOX_DEF COMMENT '消息盒子定义';

/*==============================================================*/
/* Table: INS_MSG_DEF                                           */
/*==============================================================*/
CREATE TABLE INS_MSG_DEF
(
    MSG_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    COLOR_               VARCHAR(64) COMMENT '颜色',
    URL_                 VARCHAR(200) COMMENT 'URL',
    ICON_                VARCHAR(64) COMMENT '图标定义',
    CONTENT_             VARCHAR(128) COMMENT '标题',
    DS_NAME_             VARCHAR(64) COMMENT '数据源名称',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    SQL_FUNC_            VARCHAR(2000) COMMENT 'SQL语句',
    TYPE_                VARCHAR(64) COMMENT '类型',
    COUNT_TYPE_          VARCHAR(64) COMMENT '数量比较类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (MSG_ID_)
);

ALTER TABLE INS_MSG_DEF COMMENT '消息项目定义 ';

/*==============================================================*/
/* Table: INS_NEWS                                              */
/*==============================================================*/
CREATE TABLE INS_NEWS
(
    NEW_ID_              VARCHAR(64) NOT NULL COMMENT '主键',
    SUBJECT_             VARCHAR(128) COMMENT '标题',
    KEYWORDS_            VARCHAR(128) COMMENT '关键字',
    CONTENT_             TEXT COMMENT '内容',
    SYS_DIC_NEW_         VARCHAR(64) COMMENT '分类ID',
    IMG_FILE_ID_         VARCHAR(128) COMMENT '图片文件ID',
    READ_TIMES_          INT COMMENT '读取次数',
    AUTHOR_              VARCHAR(64) COMMENT '作者',
    STATUS_              VARCHAR(40) COMMENT '状态',
    FILES_               VARCHAR(500) COMMENT '附件',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (NEW_ID_)
);

ALTER TABLE INS_NEWS COMMENT '新闻公告';

/*==============================================================*/
/* Table: INS_PORTAL_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_PORTAL_DEF
(
    PORT_ID_             VARCHAR(64) NOT NULL COMMENT '门户ID',
    NAME_                VARCHAR(128) COMMENT '名称',
    KEY_                 VARCHAR(64) COMMENT '别名',
    IS_DEFAULT_          VARCHAR(64) COMMENT '是否默认',
    LAYOUT_HTML_         TEXT COMMENT '布局HTML',
    PRIORITY_            INT COMMENT '优先级',
    IS_MOBILE_           VARCHAR(40) COMMENT '是否手机门户',
    LAYOUT_JSON_         TEXT COMMENT '门户布局',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (PORT_ID_)
);

ALTER TABLE INS_PORTAL_DEF COMMENT '门户定义';

/*==============================================================*/
/* Table: INS_PORTAL_PERMISSION                                 */
/*==============================================================*/
CREATE TABLE INS_PORTAL_PERMISSION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '权限ID',
    LAYOUT_ID_           VARCHAR(64) COMMENT '门户ID',
    TYPE_                VARCHAR(32) COMMENT '类型',
    OWNER_ID_            VARCHAR(32) COMMENT '用户或组ID',
    OWNER_NAME_          VARCHAR(64) COMMENT '用户或组名称',
    MENU_TYPE_           VARCHAR(64) COMMENT '菜单类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE INS_PORTAL_PERMISSION COMMENT '布局权限设置';

/*==============================================================*/
/* Table: INS_REMIND_DEF                                        */
/*==============================================================*/
CREATE TABLE INS_REMIND_DEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    SUBJECT_             VARCHAR(150) COMMENT '主题',
    URL_                 VARCHAR(300) COMMENT '连接地址',
    TYPE_                VARCHAR(64) COMMENT '设置类型',
    SETTING_             TEXT COMMENT 'sql语句或者方法',
    DESCRIPTION_         VARCHAR(200) COMMENT '描述',
    SN_                  INT COMMENT '序号',
    ENABLED_             VARCHAR(64) COMMENT '是否有效',
    ICON_                VARCHAR(64) COMMENT '图标',
    DS_NAME_             VARCHAR(64) COMMENT '数据源名称',
    DS_ALIAS_            VARCHAR(64) COMMENT '数据源别名',
    TENANT_ID_           VARCHAR(64) COMMENT '租用机构ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建人部门ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE INS_REMIND_DEF COMMENT '消息提醒';
