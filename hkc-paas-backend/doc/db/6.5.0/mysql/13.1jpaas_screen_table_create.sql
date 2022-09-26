use jpaas_screen;

/*==============================================================*/
/* Table: AREA_MAPPING                                          */
/*==============================================================*/
CREATE TABLE AREA_MAPPING
(
   ID                   BIGINT(20) NOT NULL COMMENT '主键',
   PROVINCE_NAME        VARCHAR(255) COMMENT '省名称',
   PROVINCE_CODE        VARCHAR(255) COMMENT '省代码',
   CITY_NAME            VARCHAR(255) COMMENT '市名称',
   CITY_CODE            VARCHAR(255) COMMENT '市代码',
   COUNTY_NAME          VARCHAR(255) COMMENT '县名称',
   COUNTY_CODE          CHAR(10) COMMENT '县代码',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE AREA_MAPPING COMMENT 'AREA_MAPPING';


/*==============================================================*/
/* Table: CHART_GROUP                                           */
/*==============================================================*/
CREATE TABLE CHART_GROUP
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(64) COMMENT '名称',
   PID                  VARCHAR(50) COMMENT '父级ID',
   LEVEL                INT(11) COMMENT '当前分组处于第几级',
   TYPE                 VARCHAR(50) COMMENT 'group or scene',
   CREATE_BY            VARCHAR(50) COMMENT '创建人ID',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE CHART_GROUP COMMENT 'CHART_GROUP';

/*==============================================================*/
/* Table: CHART_VIEW                                            */
/*==============================================================*/
CREATE TABLE CHART_VIEW
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(1024) COMMENT '名称',
   TITLE                VARCHAR(1024) COMMENT 'Echart标题',
   SCENE_ID             VARCHAR(50) NOT NULL COMMENT '场景ID',
   TABLE_ID             VARCHAR(50) NOT NULL COMMENT '数据集表ID',
   TYPE                 VARCHAR(50) COMMENT '图标类型',
   RENDER               VARCHAR(50) COMMENT '视图渲染方式',
   RESULT_COUNT         INT(11) COMMENT '展示结果',
   RESULT_MODE          VARCHAR(50) COMMENT '展示模式',
   X_AXIS               LONGTEXT COMMENT '横轴FILED',
   Y_AXIS               LONGTEXT COMMENT '纵轴FIELD',
   EXT_STACK            LONGTEXT COMMENT '堆叠项',
   EXT_BUBBLE           LONGTEXT COMMENT '气泡大小',
   Y_AXIS_EXT           LONGTEXT COMMENT '副轴',
   CUSTOM_ATTR          LONGTEXT COMMENT '图形属性',
   CUSTOM_STYLE         LONGTEXT COMMENT '组件样式',
   CUSTOM_FILTER        LONGTEXT COMMENT '结果过滤',
   DRILL_FIELDS         LONGTEXT COMMENT '钻取字段',
   CREATE_BY            VARCHAR(50) COMMENT '创建ID',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   UPDATE_TIME          BIGINT(20) COMMENT '更新时间',
   SNAPSHOT             LONGTEXT COMMENT '缩略图',
   STYLE_PRIORITY       VARCHAR(255) DEFAULT 'panel' COMMENT '样式优先级 PANEL 仪表板 VIEW 视图',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE CHART_VIEW COMMENT 'chart_view';

/*==============================================================*/
/* Table: DATASET_GROUP                                         */
/*==============================================================*/
CREATE TABLE DATASET_GROUP
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(64) NOT NULL COMMENT '名称',
   PID                  VARCHAR(50) COMMENT '父ID',
   LEVEL                INT(11) COMMENT '当前分组处于第几级 ',
   TYPE                 VARCHAR(50) COMMENT 'group or scene',
   CREATE_BY            VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME          BIGINT(11) COMMENT '创建时间',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_GROUP COMMENT 'dataset_group';

/*==============================================================*/
/* Table: DATAEASE_VERSION                                      */
/*==============================================================*/
CREATE TABLE DATAEASE_VERSION
(
   INSTALLED_RANK       INT(11) NOT NULL COMMENT 'installed_rank',
   VERSION              VARCHAR(50) NOT NULL COMMENT 'version',
   DESCRIPTION          VARCHAR(255) COMMENT 'description',
   TYPE                 VARCHAR(50) NOT NULL COMMENT 'type',
   SCRIPT               VARCHAR(200) NOT NULL COMMENT 'script',
   CHECKSUM             INT(11) COMMENT 'checksum',
   INSTALLED_BY         VARCHAR(100) NOT NULL COMMENT 'installed_by',
   INSTALLED_ON         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'installed_on',
   EXECUTION_TIME       INT(11) NOT NULL COMMENT 'execution_time',
   SUCCESS              TINYINT(1) NOT NULL,
   PRIMARY KEY (INSTALLED_RANK),
   KEY DATAEASE_VERSION_S_IDX (SUCCESS)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATAEASE_VERSION COMMENT 'dataease_version';


/*==============================================================*/
/* Table: DATASET_TABLE                                         */
/*==============================================================*/
CREATE TABLE DATASET_TABLE
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '名称',
   SCENE_ID             VARCHAR(50) NOT NULL COMMENT '场景ID',
   DATA_SOURCE_ID       VARCHAR(50) NOT NULL COMMENT '数据源ID',
   TYPE                 VARCHAR(50) COMMENT '类型（db,sql,excel,custom）',
   MODE                 VARCHAR(50) DEFAULT '0' COMMENT '连接模式：0-直连，1-定时同步',
   INFO                 LONGTEXT COMMENT '表原始信息',
   CREATE_BY            VARCHAR(50) COMMENT '创建人ID',
   CREATE_TIME          BIGINT(11) COMMENT '创建时间',
   QRTZ_INSTANCE        VARCHAR(255) COMMENT 'qrtz_instance',
   SYNC_STATUS          VARCHAR(45) COMMENT 'sync_status',
   LAST_UPDATE_TIME     BIGINT(20) DEFAULT 0 COMMENT 'last_update_time',
   PRIMARY KEY (ID),
   KEY dataset_table_index (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE COMMENT 'dataset_table';

/*==============================================================*/
/* Table: DATASET_TABLE_FIELD                                   */
/*==============================================================*/
CREATE TABLE DATASET_TABLE_FIELD
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   TABLE_ID             VARCHAR(50) COMMENT '表ID',
   ORIGIN_NAME          VARCHAR(255) NOT NULL COMMENT '原始名',
   NAME                 VARCHAR(255) NOT NULL COMMENT '字段名',
   DATAEASE_NAME        VARCHAR(255) NOT NULL COMMENT '数据库名',
   GROUP_TYPE           VARCHAR(50) COMMENT '维度/指标标识 d:维度，q:指标',
   TYPE                 VARCHAR(50) NOT NULL COMMENT '原始字段类型',
   SIZE                 INT(11) COMMENT 'size',
   DE_TYPE              INT(11) COMMENT 'DATAEASE字段类型:0-文本，1-时间，2-整型数值，3-浮点数值',
   DE_TYPE_FORMAT       INT(11)  COMMENT '类型格式',
   DE_EXTRACT_TYPE      INT(11) NOT NULL COMMENT 'de_extract_type',
   EXT_FIELD            INT(11) COMMENT '是否扩展字段 0否 1是',
   CHECKED              TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否选中',
   COLUMN_INDEX         INT(11) COMMENT '列位置',
   LAST_SYNC_TIME       BIGINT(11) COMMENT '同步时间',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE_FIELD COMMENT 'DATASET_TABLE_FIELD';

/*==============================================================*/
/* Table: DATASET_TABLE_INCREMENTAL_CONFIG                      */
/*==============================================================*/
CREATE TABLE DATASET_TABLE_INCREMENTAL_CONFIG
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   TABLE_ID             VARCHAR(50) NOT NULL COMMENT '表ID',
   INCREMENTAL_DELETE   LONGTEXT COMMENT '详细信息删除',
   INCREMENTAL_ADD      LONGTEXT COMMENT '详细信息添加',
   COLUMN_5             CHAR(10),
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE_INCREMENTAL_CONFIG COMMENT 'dataset_table_incremental_config';

/*==============================================================*/
/* Table: DATASET_TABLE_TASK                                    */
/*==============================================================*/
CREATE TABLE DATASET_TABLE_TASK
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   TABLE_ID             VARCHAR(50) NOT NULL COMMENT '表ID',
   NAME                 CHAR(10) NOT NULL COMMENT '任务名称',
   TYPE                 VARCHAR(50) NOT NULL COMMENT '更新方式：0-全量更新 1-增量更新',
   START_TIME           BIGINT(20) COMMENT '开始时间',
   RATE                 VARCHAR(50) NOT NULL COMMENT '执行频率：0 一次性 1 cron',
   CRON                 VARCHAR(255) COMMENT 'cron表达式',
   END                  VARCHAR(50) NOT NULL COMMENT '结束限制 0 无限制 1 设定结束时间',
   END_TIME             BIGINT(20) COMMENT '结束时间',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   LAST_EXEC_TIME       BIGINT(20) COMMENT '上次执行时间',
   LAST_EXEC_STATUS     VARCHAR(50) COMMENT '上次执行结果',
   EXTRA_DATA           LONGTEXT COMMENT 'extra_data',
   STATUS               VARCHAR(50) COMMENT '任务状态',
   PRIMARY KEY (ID),
   KEY dataset_table_task_index (ID)

)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE_TASK COMMENT 'dataset_table_task';

/*==============================================================*/
/* Table: DATASET_TABLE_TASK_LOG                                */
/*==============================================================*/
CREATE TABLE DATASET_TABLE_TASK_LOG
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   TABLE_ID             VARCHAR(50) NOT NULL COMMENT '表ID',
   TASK_ID              VARCHAR(50) COMMENT '任务ID',
   START_TIME           BIGINT(20) COMMENT '开始时间',
   END_TIME             BIGINT(20) COMMENT '结束时间',
   STATUS               VARCHAR(50) NOT NULL COMMENT '执行状态',
   INFO                 LONGTEXT COMMENT '错误信息',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   TRIGGER_TYPE         VARCHAR(50) COMMENT 'trigger_type',
   PRIMARY KEY (ID),
   KEY dataset_table_task_log_index (ID,TASK_ID,TABLE_ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE_TASK_LOG COMMENT 'dataset_table_task_log';


/*==============================================================*/
/* Table: DATASET_TABLE_UNION                                   */
/*==============================================================*/
CREATE TABLE DATASET_TABLE_UNION
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   SOURCE_TABLE_ID      VARCHAR(50) COMMENT '关联表ID',
   SOURCE_TABLE_FIELD_ID VARCHAR(50) COMMENT '关联表字段ID',
   SOURCE_UNION_RELATION VARCHAR(50) COMMENT '关联关系',
   TARGET_TABLE_ID      VARCHAR(50) COMMENT '被关联表ID',
   TARGET_TABLE_FIELD_ID VARCHAR(50) COMMENT '被关联字段ID',
   TARGET_UNION_RELATION VARCHAR(50) COMMENT '被关联关系',
   CREATE_BY            VARCHAR(50) COMMENT '创建人ID',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASET_TABLE_UNION COMMENT 'dataset_table_union';


/*==============================================================*/
/* Table: DATASOURCE                                            */
/*==============================================================*/
CREATE TABLE DATASOURCE
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(50) NOT NULL COMMENT '名称',
   `DESC`               VARCHAR(50) COMMENT '描述',
   `TYPE`                VARCHAR(50) NOT NULL COMMENT '类型',
   CONFIGURATION        LONGTEXT NOT NULL COMMENT '详细信息',
   CREATE_TIME          BIGINT(20) NOT NULL COMMENT '创建时间',
   UPDATE_TIME          BIGINT(20) NOT NULL COMMENT '更新时间',
   CREATE_BY            VARCHAR(50) COMMENT '创建人ID',
   STATUS               VARCHAR(45)  COMMENT '状态',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE DATASOURCE COMMENT 'datasource';

/*==============================================================*/
/* Table: FILE_CONTENT                                          */
/*==============================================================*/
CREATE TABLE FILE_CONTENT
(
   FILE_ID              VARCHAR(64) NOT NULL COMMENT 'file_id',
   FILE                 LONGBLOB COMMENT 'file',
   PRIMARY KEY (FILE_ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE FILE_CONTENT COMMENT 'FILE_CONTENT';

/*==============================================================*/
/* Table: FILE_METADATA                                         */
/*==============================================================*/
CREATE TABLE FILE_METADATA
(
   ID                   VARCHAR(64) NOT NULL COMMENT 'File ID',
   NAME                 VARCHAR(64) COMMENT 'File NAME',
   TYPE                 VARCHAR(64) COMMENT 'file type',
   SIZE                 BIGINT(20) NOT NULL COMMENT 'file size',
   CREATE_TIME          BIGINT(20) NOT NULL COMMENT 'create_time',
   UPDATE_TIME          BIGINT(20) NOT NULL COMMENT 'update_time',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE FILE_METADATA COMMENT 'file_metadata';


/*==============================================================*/
/* Table: LICENSE                                               */
/*==============================================================*/
CREATE TABLE LICENSE
(
   ID                   VARCHAR(64) NOT NULL COMMENT 'id',
   UPDATE_TIME          DATETIME COMMENT '更新时间',
   LICENSE              LONGTEXT COMMENT 'license',
   F2C_LICENSE          LONGTEXT COMMENT 'f2c_license',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE LICENSE COMMENT 'license';

/*==============================================================*/
/* Table: MY_PLUGIN                                             */
/*==============================================================*/
CREATE TABLE MY_PLUGIN
(
   PLUGIN_ID            BIGINT(20) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '插件名称',
   STORE                VARCHAR(255) COMMENT '商家',
   FREE                 TINYINT(1) DEFAULT 0 COMMENT '是否免费',
   COST                 INT(11) COMMENT '费用',
   CATEGORY             VARCHAR(255) COMMENT '列别',
   DESCRIPT             VARCHAR(255) COMMENT '描述',
   VERSION              VARCHAR(255) COMMENT '版本号',
   INSTALL_TYPE         INT(11) COMMENT '安装类型',
   CREATOR              VARCHAR(255) COMMENT '开发者',
   LOAD_MYBATIS         TINYINT(1) DEFAULT 0 COMMENT '是否需要加载MYNBATIS',
   RELEASE_TIME         BIGINT(20) COMMENT '发布时间',
   INSTALL_TIME         BIGINT(20) COMMENT '安装时间',
   MODULE_NAME          VARCHAR(255) COMMENT 'jar名称',
   ICON                 VARCHAR(255) COMMENT '图标',
   PRIMARY KEY (PLUGIN_ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE MY_PLUGIN COMMENT '插件表';


/*==============================================================*/
/* Table: PANEL_DESIGN                                          */
/*==============================================================*/
CREATE TABLE PANEL_DESIGN
(
   ID                   VARCHAR(100) NOT NULL COMMENT '主键',
   PANEL_ID             VARCHAR(100) COMMENT 'panel_id',
   COMPONENT_ID         VARCHAR(100) COMMENT '组件ID',
   COMPONENT_STYLE      VARCHAR(2000) COMMENT '组件样式',
   COMPONENT_POSITION   VARCHAR(2000) COMMENT '组件样式定位',
   COMPONENT_TYPE       VARCHAR(255) COMMENT '组件类型 view 视图 public 公共组件',
   COMPONENT_DETAILS    VARCHAR(2000) COMMENT '组件明细',
   UPDATE_TIME          BIGINT(20) COMMENT '修改时间',
   UPDATE_PERSON        VARCHAR(255) COMMENT '修改人',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_DESIGN COMMENT '仪表板和组件的关联关系 组件分为普通视图和系统组件';

/*==============================================================*/
/* Table: PANEL_GROUP                                           */
/*==============================================================*/
CREATE TABLE PANEL_GROUP
(
   ID                   VARCHAR(64) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '名称',
   PID                  VARCHAR(255) COMMENT '父级ID',
   LEVEL                INT(11) COMMENT '层级',
   NODE_TYPE            VARCHAR(255) COMMENT '节点名称',
   CREATE_BY            VARCHAR(255) COMMENT '创建人',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   PANEL_TYPE           VARCHAR(255) NOT NULL COMMENT '仪表板类型',
   PANEL_STYLE          LONGTEXT NOT NULL COMMENT 'PANEL样式',
   PANEL_DATA           LONGTEXT COMMENT 'PANEL数据',
   SOURCE               VARCHAR(255) COMMENT '数据来源',
   EXTEND1              VARCHAR(255) COMMENT 'extend1',
   EXTEND2              VARCHAR(255) COMMENT 'extend1',
   REMARK               VARCHAR(255) COMMENT '备注',
   PRIMARY KEY (ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_GROUP COMMENT 'panel_group';

/*==============================================================*/
/* Table: PANEL_LINK                                            */
/*==============================================================*/
CREATE TABLE PANEL_LINK
(
   RESOURCE_ID          VARCHAR(64) NOT NULL COMMENT '资源ID',
   VALID                TINYINT(1) DEFAULT 0 COMMENT '启用链接',
   ENABLE_PWD           TINYINT(1) DEFAULT 0 COMMENT '启用密码',
   PWD                  VARCHAR(255) COMMENT '密码',
   OVER_TIME            BIGINT(20) COMMENT '有效截止时间',
   PRIMARY KEY (RESOURCE_ID)
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_LINK COMMENT '仪表板链接';


/*==============================================================*/
/* Table: PANEL_LINK_JUMP                                       */
/*==============================================================*/
CREATE TABLE PANEL_LINK_JUMP
(
   ID                   VARCHAR(64) NOT NULL COMMENT 'ID',
   SOURCE_PANEL_ID      VARCHAR(50) COMMENT '源仪表板ID',
   SOURCE_VIEW_ID       VARCHAR(255) COMMENT '源视图ID',
   LINK_JUMP_INFO       VARCHAR(2000) COMMENT '跳转信息',
   CHECKED              TINYINT(1) COMMENT '是否启用',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_LINK_JUMP COMMENT 'panel_link_jump';


/*==============================================================*/
/* Table: PANEL_LINK_JUMP_INFO                                  */
/*==============================================================*/
CREATE TABLE PANEL_LINK_JUMP_INFO
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   LINK_JUMP_ID         VARCHAR(50) COMMENT 'link_jump_id',
   LINK_TYPE            VARCHAR(255) COMMENT 'inner 内部仪表板，outer 外部链接',
   JUMP_TYPE            VARCHAR(255) COMMENT '_blank 新开页面 _self 当前窗口',
   TARGET_PANEL_ID      VARCHAR(255) COMMENT '关联仪表板ID',
   SOURCE_FIELD_ID      VARCHAR(255) COMMENT '字段ID',
   CONTENT              VARCHAR(2000) COMMENT '内容',
   CHECKED              TINYINT(1) COMMENT '是否可用',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_LINK_JUMP_INFO COMMENT 'panel_link_jump_info';


/*==============================================================*/
/* Table: PANEL_LINK_JUMP_TARGET_VIEW_INFO                      */
/*==============================================================*/
CREATE TABLE PANEL_LINK_JUMP_TARGET_VIEW_INFO
(
   TARGET_ID            VARCHAR(50) NOT NULL COMMENT 'target_id',
   LINK_JUMP_INFO_ID    VARCHAR(50) COMMENT 'link_jump_info_id',
   TARGET_VIEW_ID       VARCHAR(50) COMMENT 'target_view_id',
   TARGET_FIELD_ID      VARCHAR(50) COMMENT 'target_field_id',
   PRIMARY KEY (TARGET_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_LINK_JUMP_TARGET_VIEW_INFO COMMENT 'panel_link_jump_target_view_info';


/*==============================================================*/
/* Table: PANEL_LINK_MAPPING                                    */
/*==============================================================*/
CREATE TABLE PANEL_LINK_MAPPING
(
   ID                   BIGINT(20) NOT NULL COMMENT '主键',
   RESOURCE_ID          VARCHAR(255) COMMENT '仪表板ID',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_LINK_MAPPING COMMENT 'panel_link_mapping';


/*==============================================================*/
/* Table: PANEL_PDF_TEMPLATE                                    */
/*==============================================================*/
CREATE TABLE PANEL_PDF_TEMPLATE
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '模板名称',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   CREATE_USER          VARCHAR(255) COMMENT '创建人',
   TEMPLATE_CONTENT     LONGTEXT COMMENT '模板内容',
   SORT                 INT(11) COMMENT '排序',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_PDF_TEMPLATE COMMENT 'panel_pdf_template';


/*==============================================================*/
/* Table: PANEL_SHARE                                           */
/*==============================================================*/
CREATE TABLE PANEL_SHARE
(
   SHARE_ID             BIGINT(20) NOT NULL COMMENT '分享ID',
   PANEL_GROUP_ID       VARCHAR(255) COMMENT '仪表板ID',
   TARGET_ID            BIGINT(20) COMMENT '目标ID',
   GRANTER              VARCHAR(255) COMMENT '分享人',
   CREATE_TIME          BIGINT(20) COMMENT '创建日期',
   TYPE                 INT(11) COMMENT '类型0:user,1:role,2dept',
   PRIMARY KEY (SHARE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_SHARE COMMENT '仪表板分享';


/*==============================================================*/
/* Table: PANEL_STORE                                           */
/*==============================================================*/
CREATE TABLE PANEL_STORE
(
   STORE_ID             BIGINT(20) NOT NULL COMMENT '主键',
   PANEL_GROUP_ID       VARCHAR(50) COMMENT '仪表板ID',
   USER_ID              BIGINT(20) COMMENT '用户ID',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   PRIMARY KEY (STORE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_STORE COMMENT '仪表板收藏';


/*==============================================================*/
/* Table: PANEL_SUBJECT                                         */
/*==============================================================*/
CREATE TABLE PANEL_SUBJECT
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '主题名称',
   TYPE                 VARCHAR(255) COMMENT '主题类型 system 系统主题，self 自定义主题',
   DETAILS              LONGTEXT COMMENT '主题内容',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   CREATE_BY            VARCHAR(255) COMMENT '创建人',
   UPDATE_TIME          BIGINT(20) COMMENT '更新时间',
   UPDATE_BY            VARCHAR(255) COMMENT '跟新人',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_SUBJECT COMMENT 'panel_subject';


/*==============================================================*/
/* Table: PANEL_TEMPLATE                                        */
/*==============================================================*/
CREATE TABLE PANEL_TEMPLATE
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) COMMENT '名称',
   PID                  VARCHAR(255) COMMENT '父级ID',
   LEVEL                INT(11) COMMENT '层级',
   NODE_TYPE            VARCHAR(255) COMMENT '节点类型',
   CREATE_BY            VARCHAR(255) COMMENT '创建人',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   SNAPSHOT             LONGTEXT COMMENT '缩略图',
   TEMPLATE_TYPE        VARCHAR(255) COMMENT '仪表板类型',
   TEMPLATE_STYLE       LONGTEXT COMMENT '样式',
   TEMPLATE_DATA        LONGTEXT COMMENT '数据',
   DYNAMIC_DATA         LONGTEXT COMMENT '预存数据',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_TEMPLATE COMMENT 'panel_template';


/*==============================================================*/
/* Table: PANEL_VIEW                                            */
/*==============================================================*/
CREATE TABLE PANEL_VIEW
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   PANEL_ID             VARCHAR(50) COMMENT 'panel_id',
   CHART_VIEW_ID        VARCHAR(50) COMMENT 'chart_view_id',
   CONTENT              BLOB COMMENT '内容',
   CREATE_BY            VARCHAR(255) COMMENT '创建人',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   UPDATE_BY            VARCHAR(255) COMMENT '跟新人',
   UPDATE_TIME          BIGINT(20) COMMENT '更新时间',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



ALTER TABLE PANEL_VIEW COMMENT 'panel_view';



/*==============================================================*/
/* Table: PANEL_VIEW_LINKAGE                                    */
/*==============================================================*/
CREATE TABLE PANEL_VIEW_LINKAGE
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   PANEL_ID             VARCHAR(50) COMMENT 'panel_id',
   SOURCE_VIEW_ID       VARCHAR(50) COMMENT '源视图id',
   TARGET_VIEW_ID       VARCHAR(50) COMMENT '联动视图id',
   UPDATE_TIME          BIGINT(20) COMMENT '更新时间',
   UPDATE_PEOPLE        VARCHAR(255) COMMENT '更新人',
   EXT1                 VARCHAR(2000) COMMENT 'ext1',
   EXT2                 VARCHAR(2000) COMMENT 'ext1',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_VIEW_LINKAGE COMMENT 'panel_view_linkage';


/*==============================================================*/
/* Table: PANEL_VIEW_LINKAGE_FIELD                              */
/*==============================================================*/
CREATE TABLE PANEL_VIEW_LINKAGE_FIELD
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   LINKAGE_ID           VARCHAR(50) COMMENT '联动ID',
   SOURCE_FIELD         VARCHAR(255) COMMENT '源视图字段',
   TARGET_FIELD         VARCHAR(255) COMMENT '目标视图字段',
   UPDATE_TIME          BIGINT(20) COMMENT '更新时间',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PANEL_VIEW_LINKAGE_FIELD COMMENT 'panel_view_linkage_field';


/*==============================================================*/
/* Table: PLUGIN_SYS_MENU                                       */
/*==============================================================*/
CREATE TABLE PLUGIN_SYS_MENU
(
   MENU_ID              BIGINT(11) NOT NULL COMMENT 'menu_id',
   PID                  BIGINT(20) COMMENT 'pid',
   SUB_COUNT            INT(11) COMMENT 'sub_count',
   TYPE                 VARCHAR(255) COMMENT 'type',
   TITLE                VARCHAR(255) COMMENT 'title',
   COMPONENT            VARCHAR(255) COMMENT 'component',
   MENU_SORT            VARCHAR(255) COMMENT 'menu_sort',
   ICON                 VARCHAR(2555) COMMENT 'icon',
   PATH                 VARCHAR(255) COMMENT 'path',
   I_FRAME              TINYINT(1) COMMENT 'i_frame',
   CACHE                TINYINT(1) COMMENT 'cache',
   HIDDEN               TINYINT(1) COMMENT 'hidden',
   PERMISSION           VARCHAR(255) COMMENT 'permission',
   CREATE_BY            VARCHAR(255) COMMENT 'create_by',
   UPDATE_BY            VARCHAR(255) COMMENT 'update_by',
   CREATE_TIME          BIGINT(20) COMMENT 'create_time',
   UPDATE_TIME          BIGINT(20) COMMENT 'update_time',
   NO_LAYOUT            TINYINT(1) COMMENT 'no_layout',
   PRIMARY KEY (MENU_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE PLUGIN_SYS_MENU COMMENT 'plugin_sys_menu';

/*==============================================================*/
/* Table: QRTZ_JOB_DETAILS                                      */
/*==============================================================*/
CREATE TABLE QRTZ_JOB_DETAILS
(
   SCHED_NAME           VARCHAR(120) NOT NULL,
   JOB_NAME             VARCHAR(200) NOT NULL COMMENT 'JOB_NAME',
   JOB_GROUP            VARCHAR(200) NOT NULL COMMENT 'JOB_GROUP',
   DESCRIPTION          VARCHAR(250) COMMENT 'DESCRIPTION',
   JOB_CLASS_NAME       VARCHAR(250) NOT NULL COMMENT 'JOB_CLASS_NAME',
   IS_DURABLE           VARCHAR(1) NOT NULL COMMENT 'IS_DURABLE',
   IS_NONCONCURRENT     VARCHAR(1) NOT NULL COMMENT 'IS_NONCONCURRENT',
   IS_UPDATE_DATA       VARCHAR(1) NOT NULL COMMENT 'IS_UPDATE_DATA',
   REQUESTS_RECOVERY    VARCHAR(1) NOT NULL COMMENT 'REQUESTS_RECOVERY',
   JOB_DATA             BLOB COMMENT 'JOB_DATA',
   PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_JOB_DETAILS COMMENT 'qrtz_job_details';

/*==============================================================*/
/* Table: QRTZ_TRIGGERS                                         */
/*==============================================================*/
CREATE TABLE QRTZ_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(200) NOT NULL COMMENT 'TRIGGER_GROUP',
   JOB_NAME             VARCHAR(200) NOT NULL COMMENT 'JOB_NAME',
   JOB_GROUP            VARCHAR(200) NOT NULL COMMENT 'JOB_GROUP',
   DESCRIPTION          VARCHAR(250) COMMENT 'DESCRIPTION',
   NEXT_FIRE_TIME       BIGINT(20) COMMENT 'NEXT_FIRE_TIME',
   PREV_FIRE_TIME       BIGINT(20) COMMENT 'PREV_FIRE_TIME',
   PRIORITY             INT(111) COMMENT 'PRIORITY',
   TRIGGER_STATE        VARCHAR(16) NOT NULL COMMENT 'TRIGGER_STATE',
   TRIGGER_TYPE         VARCHAR(8) NOT NULL COMMENT 'TRIGGER_TYPE',
   START_TIME           BIGINT(20) COMMENT 'START_TIME',
   END_TIME             BIGINT(20) COMMENT 'END_TIME',
   CALENDAR_NAME        VARCHAR(200) COMMENT 'CALENDAR_NAME',
   MISFIRE_INSTR        SMALLINT(6) COMMENT 'MISFIRE_INSTR',
   JOB_DATA             BLOB COMMENT 'JOB_DATA',
   PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
   KEY IDX_QRTZ_T_J (SCHED_NAME,JOB_NAME,JOB_GROUP),
  KEY IDX_QRTZ_T_JG (SCHED_NAME,JOB_GROUP),
  KEY IDX_QRTZ_T_C (SCHED_NAME,CALENDAR_NAME),
  KEY IDX_QRTZ_T_G (SCHED_NAME,TRIGGER_GROUP),
  KEY IDX_QRTZ_T_STATE (SCHED_NAME,TRIGGER_STATE),
  KEY IDX_QRTZ_T_N_STATE (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE),
  KEY IDX_QRTZ_T_N_G_STATE (SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE),
  KEY IDX_QRTZ_T_NEXT_FIRE_TIME (SCHED_NAME,NEXT_FIRE_TIME),
  KEY IDX_QRTZ_T_NFT_ST (SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME),
  KEY IDX_QRTZ_T_NFT_MISFIRE (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME),
  KEY IDX_QRTZ_T_NFT_ST_MISFIRE (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE),
  KEY IDX_QRTZ_T_NFT_ST_MISFIRE_GRP (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE),
  CONSTRAINT qrtz_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES qrtz_job_details (SCHED_NAME, JOB_NAME, JOB_GROUP)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_TRIGGERS COMMENT 'qrtz_triggers';

/*==============================================================*/
/* Table: QRTZ_BLOB_TRIGGERS                                    */
/*==============================================================*/
CREATE TABLE QRTZ_BLOB_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(200) NOT NULL COMMENT 'TRIGGER_GROUP',
   BLOB_DATA            BLOB COMMENT 'BLOB_DATA',
   PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
   CONSTRAINT qrtz_blob_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_BLOB_TRIGGERS COMMENT 'qrtz_blob_triggers';

/*==============================================================*/
/* Table: QRTZ_CALENDARS                                        */
/*==============================================================*/
CREATE TABLE QRTZ_CALENDARS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   CALENDAR_NAME        VARCHAR(200) NOT NULL COMMENT 'CALENDAR_NAME',
   CALENDAR             BLOB NOT NULL COMMENT 'CALENDAR',
   PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_CALENDARS COMMENT 'qrtz_calendars';


/*==============================================================*/
/* Table: QRTZ_CRON_TRIGGERS                                    */
/*==============================================================*/
CREATE TABLE QRTZ_CRON_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(200) NOT NULL COMMENT 'TRIGGER_GROUP',
   CRON_EXPRESSION      VARCHAR(120) NOT NULL COMMENT 'CRON_EXPRESSION',
   TIME_ZONE_ID         VARCHAR(80) COMMENT 'TIME_ZONE_ID',
   PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
   CONSTRAINT qrtz_cron_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_CRON_TRIGGERS COMMENT 'qrtz_cron_triggers';



/*==============================================================*/
/* Table: QRTZ_FIRED_TRIGGERS                                   */
/*==============================================================*/
CREATE TABLE QRTZ_FIRED_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   ENTRY_ID             VARCHAR(95) NOT NULL COMMENT 'ENTRY_ID',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(255) NOT NULL COMMENT 'TRIGGER_GROUP',
   INSTANCE_NAME        VARCHAR(200) NOT NULL COMMENT 'INSTANCE_NAME',
   FIRED_TIME           BIGINT(20) NOT NULL COMMENT 'FIRED_TIME',
   SCHED_TIME           BIGINT(20) NOT NULL COMMENT 'SCHED_TIME',
   PRIORITY             INT(11) COMMENT 'PRIORITY',
   STATE                VARCHAR(16) NOT NULL COMMENT 'STATE',
   JOB_NAME             VARCHAR(200) COMMENT 'JOB_NAME',
   JOB_GROUP            VARCHAR(200) COMMENT 'JOB_GROUP',
   IS_NONCONCURRENT     VARCHAR(1) COMMENT 'IS_NONCONCURRENT',
   REQUESTS_RECOVERY    VARCHAR(1) COMMENT 'REQUESTS_RECOVERY',
   PRIMARY KEY (SCHED_NAME, ENTRY_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_FIRED_TRIGGERS COMMENT 'qrtz_fired_triggers';

/*==============================================================*/
/* Table: QRTZ_LOCKS                                            */
/*==============================================================*/
CREATE TABLE QRTZ_LOCKS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   LOCK_NAME            VARCHAR(40) NOT NULL COMMENT 'LOCK_NAME',
   PRIMARY KEY (SCHED_NAME, LOCK_NAME)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_LOCKS COMMENT 'qrtz_locks';

/*==============================================================*/
/* Table: QRTZ_PAUSED_TRIGGER_GRPS                              */
/*==============================================================*/
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_GROUP        VARCHAR(200) NOT NULL COMMENT 'TRIGGER_GROUP',
   PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS COMMENT 'qrtz_paused_trigger_grps';

/*==============================================================*/
/* Table: QRTZ_SCHEDULER_STATE                                  */
/*==============================================================*/
CREATE TABLE QRTZ_SCHEDULER_STATE
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   INSTANCE_NAME        VARCHAR(200) NOT NULL COMMENT 'INSTANCE_NAME',
   LAST_CHECKIN_TIME    BIGINT(20) NOT NULL COMMENT 'LAST_CHECKIN_TIME',
   CHECKIN_INTERVAL     BIGINT(20) NOT NULL COMMENT 'CHECKIN_INTERVAL',
   PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_SCHEDULER_STATE COMMENT 'qrtz_scheduler_state';




/*==============================================================*/
/* Table: QRTZ_SIMPLE_TRIGGERS                                  */
/*==============================================================*/
CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(200) COMMENT 'TRIGGER_GROUP',
   REPEAT_COUNT         BINARY(20) NOT NULL COMMENT 'REPEAT_COUNT',
   REPEAT_INTERVAL      BIGINT(20) NOT NULL COMMENT 'REPEAT_INTERVAL',
   TIMES_TRIGGERED      BIGINT(20) NOT NULL COMMENT 'TIMES_TRIGGERED',
   PRIMARY KEY (SCHED_NAME, TRIGGER_NAME),
   CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_SIMPLE_TRIGGERS COMMENT 'qrtz_simple_triggers';

/*==============================================================*/
/* Table: QRTZ_SIMPROP_TRIGGERS                                 */
/*==============================================================*/
CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
   SCHED_NAME           VARCHAR(120) NOT NULL COMMENT 'SCHED_NAME',
   TRIGGER_NAME         VARCHAR(200) NOT NULL COMMENT 'TRIGGER_NAME',
   TRIGGER_GROUP        VARCHAR(200) NOT NULL COMMENT 'TRIGGER_GROUP',
   STR_PROP_1           VARCHAR(512) COMMENT 'STR_PROP_1',
   STR_PROP_2           VARCHAR(512) COMMENT 'STR_PROP_2',
   STR_PROP_3           VARCHAR(512) COMMENT 'STR_PROP_3',
   INT_PROP_1           INT(11) COMMENT 'INT_PROP_1',
   INT_PROP_2           INT(11) COMMENT 'INT_PROP_2',
   LONG_PROP_1          BIGINT(20) COMMENT 'LONG_PROP_1',
   LONG_PROP_2          BIGINT(20) COMMENT 'LONG_PROP_2',
   DEC_PROP_1           DECIMAL(13,4) COMMENT 'DEC_PROP_1',
   DEC_PROP_2           DECIMAL(13,4) COMMENT 'DEC_PROP_2',
   BOOL_PROP_1          VARCHAR(1) COMMENT 'BOOL_PROP_1',
   BOOL_PROP_2          VARCHAR(1) COMMENT 'BOOL_PROP_2',
   PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
   CONSTRAINT qrtz_simprop_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)

) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE QRTZ_SIMPROP_TRIGGERS COMMENT 'qrtz_simprop_triggers';



/*==============================================================*/
/* Table: SCHEDULE                                              */
/*==============================================================*/
CREATE TABLE `SCHEDULE`
(
   `ID`                  VARCHAR(50) NOT NULL COMMENT 'Schedule ID',
  `KEY`                  VARCHAR(50) NOT NULL COMMENT 'Schedule Key',
   `TYPE`                 VARCHAR(50) NOT NULL COMMENT 'Schedule Type',
   `VALUE`                VARCHAR(255) NOT NULL COMMENT 'Schedule value',
   `GROUP`                VARCHAR(50) COMMENT 'group',
   `JOB`                  VARCHAR(64) COMMENT 'job',
   `ENABLE`               TINYINT(1) COMMENT 'enable',
   `RESOURCE_ID`          VARCHAR(64) NOT NULL COMMENT 'resource_id',
   `USER_ID`              VARCHAR(50) NOT NULL COMMENT 'user_id',
   `CUSTOM_DATA`          LONGTEXT COMMENT 'custom_data',
   PRIMARY KEY (`ID`),
   KEY RESOURCE_ID (`RESOURCE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SCHEDULE COMMENT 'schedule';



/*==============================================================*/
/* Table: SYS_AUTH                                              */
/*==============================================================*/
CREATE TABLE SYS_AUTH
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   AUTH_SOURCE          VARCHAR(255) COMMENT '授权资产源 数据集 视图 仪表板',
   AUTH_SOURCE_TYPE     VARCHAR(255) COMMENT '授权资产类型dataset 数据集 view 视图 panel 仪表板',
   AUTH_TARGET          VARCHAR(255) COMMENT '授权目标 用户 角色 组织',
   AUTH_TARGET_TYPE     VARCHAR(255) COMMENT '授权目标类型 user 用户 role 角色 org dept 组织''',
   AUTH_TIME            BIGINT(20) COMMENT '授权时间',
   AUTH_DETAILS         VARCHAR(2000) COMMENT '授权明细 privilegename 名称 privilegeType 权限类型 1 查看 2 管理 3 导出 4 使用 ; privilegeValue 1 不可用 2 可用 3 部分可用',
   AUTH_USER            VARCHAR(255) COMMENT '授权人员',
   UPDATE_TIME          DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_AUTH COMMENT 'sys_auth';

/*==============================================================*/
/* Table: SYS_AUTH_DETAIL                                       */
/*==============================================================*/
CREATE TABLE SYS_AUTH_DETAIL
(
   ID                   VARCHAR(50) NOT NULL COMMENT '主键',
   AUTH_ID              VARCHAR(50) COMMENT 'auth_id',
   PRIVILEGE_NAME       VARCHAR(255) COMMENT '授权名称',
   PRIVILEGE_TYPE       INT(11) COMMENT '权限类型',
   PRIVILEGE_VALUE      INT(11) COMMENT '权限值 1 可用 2 不用',
   PRIVILEGE_EXTEND     VARCHAR(2000) COMMENT '权限扩展',
   REMARK               VARCHAR(2000) COMMENT '备注',
   CREATE_USER          VARCHAR(255) COMMENT 'create_user',
   CREATE_TIME          BIGINT(20) COMMENT 'create_time',
   UPDATE_TIME          BIGINT(20) COMMENT 'update_time',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_AUTH_DETAIL COMMENT 'sys_auth_detail';

/*==============================================================*/
/* Table: SYS_DEPT                                              */
/*==============================================================*/
CREATE TABLE SYS_DEPT
(
   DEPT_ID              BIGINT(20) NOT NULL COMMENT '主键',
   PID                  BIGINT(20) COMMENT '上级部门',
   SUB_COUNT            INT(11) DEFAULT 0 COMMENT '子部门数目',
   NAME                 VARCHAR(255) COMMENT '名称',
   DEPT_SORT            INT(11) DEFAULT 999 COMMENT '排序',
   CREATE_BY            VARCHAR(255) COMMENT '创建者',
   UPDATE_BY            VARCHAR(255) COMMENT '更新者',
   CREATE_TIME          BIGINT(20) COMMENT '创建日期',
   UPDATE_TIME          BIGINT(20) COMMENT '更新日期',
   PRIMARY KEY (DEPT_ID),
   KEY AK_INX_PID (PID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_DEPT COMMENT '组织机构';


/*==============================================================*/
/* Table: SYS_MENU                                         */
/*==============================================================*/
CREATE TABLE SYS_MENU
(
   MENU_ID              BIGINT(20) NOT NULL COMMENT 'menu_id',
   PID                  BIGINT(20) COMMENT '上级菜单ID',
   SUB_COUNT            INT(11) DEFAULT 0 COMMENT '子菜单数目',
   TYPE                 INT(11) COMMENT '菜单类型',
   TITLE                VARCHAR(255) COMMENT '菜单标题',
   NAME                 VARCHAR(255) COMMENT '组件名称',
   COMPONENT            VARCHAR(255) COMMENT '组件',
   MENU_SORT            INT(11) COMMENT '排序',
   ICON                 VARCHAR(255) COMMENT '图标',
   PATH                 VARCHAR(255) COMMENT '链接地址',
   I_FRAME              BIT(1) COMMENT '是否链接',
   CACHE                BIT(1) DEFAULT 0 COMMENT '缓存',
   HIDDEN               BIT(1) DEFAULT 0 COMMENT '隐藏',
   PERMISSION           VARCHAR(255) COMMENT '权限',
   CREATE_BY            VARCHAR(255) COMMENT '创建者',
   UPDATE_BY            VARCHAR(255) COMMENT '更新者',
   CREATE_TIME          BIGINT(20) COMMENT '创建日期',
   UPDATE_TIME          BIGINT(20) COMMENT '更新日期',
   PRIMARY KEY (MENU_ID),
   KEY AK_INX_PID (PID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_MENU COMMENT '系统菜单';


/*==============================================================*/
/* Table: SYS_MSG                                               */
/*==============================================================*/
CREATE TABLE SYS_MSG
(
   MSG_ID               BIGINT(20) NOT NULL COMMENT '信息主键',
   USER_ID              BIGINT(20) NOT NULL COMMENT '用户ID',
   TYPE_ID              BIGINT(20) NOT NULL COMMENT '类型',
   STATUS               TINYINT(1) NOT NULL COMMENT '状态',
   PARAM                VARCHAR(255) COMMENT '路由参数',
   CREATE_TIME          BIGINT(20) COMMENT '发送时间',
   READ_TIME            BIGINT(20) COMMENT '读取时间',
   CONTENT              VARCHAR(255) COMMENT '消息内容',
   PRIMARY KEY (MSG_ID),
   KEY AK_INX_MSG_USERID (USER_ID),
   KEY AK_INX_MSG_TYPE (TYPE_ID),
   KEY AK_INX_MSG_STATUS (STATUS)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_MSG COMMENT '消息通知表';


/*==============================================================*/
/* Table: SYS_MSG_CHANNEL                                       */
/*==============================================================*/
CREATE TABLE SYS_MSG_CHANNEL
(
   MSG_CHANNEL_ID       BIGINT(20) NOT NULL COMMENT '主键',
   CHANNEL_NAME         VARCHAR(255) COMMENT '渠道名称',
   SERVICE_NAME         VARCHAR(255) COMMENT '策略名称',
   PRIMARY KEY (MSG_CHANNEL_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_MSG_CHANNEL COMMENT '消息渠道表';


/*==============================================================*/
/* Table: SYS_MSG_SETTING                                       */
/*==============================================================*/
CREATE TABLE SYS_MSG_SETTING
(
   MSG_SETTING_ID       BIGINT(20) NOT NULL COMMENT '主键',
   USER_ID              BIGINT(20) NOT NULL COMMENT '用户ID',
   TYPE_ID              BIGINT(20) NOT NULL COMMENT '类型ID',
   CHANNEL_ID           BIGINT(20) NOT NULL COMMENT '渠道ID',
   ENABLE               TINYINT(1) COMMENT '是否启用',
   PRIMARY KEY (MSG_SETTING_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_MSG_SETTING COMMENT '消息设置表';


/*==============================================================*/
/* Table: SYS_MSG_TYPE                                          */
/*==============================================================*/
CREATE TABLE SYS_MSG_TYPE
(
   MSG_TYPE_ID          BIGINT(20) NOT NULL COMMENT '主键',
   PID                  BIGINT(20) NOT NULL COMMENT '父类ID',
   TYPE_NAME            VARCHAR(255) COMMENT '类型名称',
   ROUTER               VARCHAR(255) COMMENT '跳转路由',
   CALLBACK             VARCHAR(255) COMMENT '回调方法',
   PRIMARY KEY (MSG_TYPE_ID),
   KEY AK_INX_MSGTYPE_PID (PID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_MSG_TYPE COMMENT '消息类型表';


/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
CREATE TABLE SYS_ROLE
(
   ROLE_ID              BIGINT(20) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) NOT NULL COMMENT '名称',
   DESCRIPTION          VARCHAR(255) COMMENT '描述',
   CREATE_BY            VARCHAR(255) COMMENT '创建者',
   UPDATE_BY            VARCHAR(255) COMMENT '更新者',
   CREATE_TIME          BIGINT(20) COMMENT '创建日期',
   UPDATE_TIME          BIGINT(20) COMMENT '更新日期',
   PRIMARY KEY (ROLE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_ROLE COMMENT '角色表';


/*==============================================================*/
/* Table: SYS_ROLES_MENUS                                       */
/*==============================================================*/
CREATE TABLE SYS_ROLES_MENUS
(
   MENU_ID              BIGINT(20) NOT NULL COMMENT '菜单ID',
   ROLE_ID              BIGINT(20) NOT NULL COMMENT '角色ID',
   PRIMARY KEY (MENU_ID, ROLE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_ROLES_MENUS COMMENT '角色菜单关联';


/*==============================================================*/
/* Table: SYS_TASK                                              */
/*==============================================================*/
CREATE TABLE SYS_TASK
(
   TASK_ID              BIGINT(20) NOT NULL COMMENT '任务ID',
   TASK_NAME            VARCHAR(255) NOT NULL COMMENT '任务名称',
   TASK_TYPE            VARCHAR(100) NOT NULL COMMENT '任务类型',
   START_TIME           BIGINT(20) COMMENT '开始时间',
   END_TIME             BIGINT(20) COMMENT '结束时间',
   RATE_TYPE            INT(11) NOT NULL COMMENT '频率方式',
   RATE_VAL             VARCHAR(255) COMMENT '频率值',
   CREATOR              BIGINT(20) NOT NULL COMMENT '创建者ID',
   CREATE_TIME          BIGINT(20) COMMENT '创建时间',
   PRIMARY KEY (TASK_ID),
   KEY AK_SYS_TASK_TYPE (TASK_TYPE)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_TASK COMMENT 'sys_task';

/*==============================================================*/
/* Table: SYS_TASK_EMAIL                                        */
/*==============================================================*/
CREATE TABLE SYS_TASK_EMAIL
(
   ID                   BIGINT(20) NOT NULL,
   TITLE                VARCHAR(255) COMMENT '模版标题',
   PANEL_ID             VARCHAR(255) COMMENT '仪表板ID',
   RECIPIENTS           VARCHAR(255) COMMENT '收件人',
   CONTENT              BLOB COMMENT '内容',
   PIXEL                VARCHAR(255) COMMENT '像素',
   TASK_ID              BIGINT(20) NOT NULL COMMENT '任务ID',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

/*==============================================================*/
/* Table: SYS_TASK_INSTANCE                                     */
/*==============================================================*/
CREATE TABLE SYS_TASK_INSTANCE
(
   INSTANCE_ID          BIGINT(20) NOT NULL COMMENT '任务实例ID',
   TASK_ID              BIGINT(20) NOT NULL COMMENT '任务id',
   EXECUTE_TIME         BIGINT(20) COMMENT '执行时间',
   FINISH_TIME          BIGINT(20) COMMENT '完成时间',
   STATUS               INT(11) COMMENT '实例状态',
   INFO                 LONGTEXT COMMENT '执行信息',
   PRIMARY KEY (INSTANCE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_TASK_INSTANCE COMMENT 'sys_task_instance';


/*==============================================================*/
/* Table: SYS_THEME                                             */
/*==============================================================*/
CREATE TABLE SYS_THEME
(
   ID                   BIGINT(20) NOT NULL COMMENT '主键',
   NAME                 VARCHAR(255) NOT NULL COMMENT '主题名称',
   IMG_ID               VARCHAR(255) COMMENT '文件ID',
   IMG                  VARCHAR(255) COMMENT '主题缩略图',
   STATUS               TINYINT(1) COMMENT '状态',
   PRIMARY KEY (ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_THEME COMMENT 'sys_theme';

/*==============================================================*/
/* Table: SYS_THEME_ITEM                                        */
/*==============================================================*/
CREATE TABLE `SYS_THEME_ITEM`
(
   `THEME_ID`             BIGINT(20) NOT NULL COMMENT '主题ID',
   `KEY`                  VARCHAR(255) COMMENT 'key',
   `VAL`                  VARCHAR(255) COMMENT '样式val'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_THEME_ITEM COMMENT 'sys_theme_item';


/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
CREATE TABLE SYS_USER
(
   USER_ID              BIGINT(20) NOT NULL COMMENT '用户ID',
   DEPT_ID              BIGINT(20) COMMENT '部门名称',
   USERNAME             VARCHAR(255) COMMENT '用户名',
   NICK_NAME            VARCHAR(255) COMMENT '昵称',
   GENDER               VARCHAR(2) COMMENT '性别',
   PHONE                VARCHAR(255) COMMENT '手机号码',
   EMAIL                VARCHAR(255) COMMENT '邮箱',
   PASSWORD             VARCHAR(255) COMMENT '密码',
   IS_ADMIN             BIT(1) DEFAULT 0 COMMENT '是否为admin账号',
   ENABLED              BIGINT(20) COMMENT '状态：1启用、0禁用',
   CREATE_BY            VARCHAR(255) COMMENT '创建者',
   UPDATE_BY            VARCHAR(255) COMMENT '更新者',
   PWD_RESET_TIME       BIGINT(20) COMMENT '修改密码的时间',
   CREATE_TIME          BIGINT(20) COMMENT '创建日期',
   UPDATE_TIME          BIGINT(20) COMMENT '更新日期',
   LANGUAGE             VARCHAR(20) COMMENT '语言',
   `FROM`                 INT(11) NOT NULL COMMENT '来源',
   SUB                  VARCHAR(255) COMMENT 'OIDC用户ID',
   PRIMARY KEY (USER_ID),
   KEY AK_FK5RWMRYNY6JTHAAXKOGOWNKNQP (DEPT_ID),
   KEY AK_INX_ENABLED (ENABLED)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_USER COMMENT '系统用户';


/*==============================================================*/
/* Table: SYS_USERS_ROLES                                       */
/*==============================================================*/
CREATE TABLE SYS_USERS_ROLES
(
   USER_ID              BIGINT(20) NOT NULL COMMENT '用户ID',
   ROLE_ID              BIGINT(20) NOT NULL COMMENT '角色ID',
   PRIMARY KEY (USER_ID, ROLE_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYS_USERS_ROLES COMMENT 'sys_users_roles';


/*==============================================================*/
/* Table: SYSTEM_PARAMETER                                      */
/*==============================================================*/
CREATE TABLE SYSTEM_PARAMETER
(
   PARAM_KEY            VARCHAR(64) NOT NULL COMMENT 'param_key',
   PARAM_VALUE          VARCHAR(255) COMMENT 'param_value',
   TYPE                 VARCHAR(100) COMMENT 'type',
   SORT                 INT(11) COMMENT 'sort',
   PRIMARY KEY (PARAM_KEY)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE SYSTEM_PARAMETER COMMENT 'system_parameter';


/*==============================================================*/
/* Table: USER_KEY                                              */
/*==============================================================*/
CREATE TABLE USER_KEY
(
   ID                   BIGINT(20) NOT NULL COMMENT 'id',
   USER_ID              BIGINT(20) COMMENT 'user_id',
   ACCESS_KEY           VARCHAR(50) NOT NULL COMMENT 'access_key',
   SECRET_KEY           VARCHAR(50) NOT NULL COMMENT 'secret_key',
   CREATE_TIME          BIGINT(20) NOT NULL COMMENT 'create_time',
   STATUS               VARCHAR(10) COMMENT 'status',
   PRIMARY KEY (ID),
   UNIQUE KEY IDX_AK (ACCESS_KEY),
   KEY IDX_USER_K_ID (USER_ID)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE USER_KEY COMMENT 'user_key';



DROP VIEW IF EXISTS `v_auth_model`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_auth_model` AS select `sys_user`.`user_id` AS `id`,`sys_user`.`username` AS `name`,`sys_user`.`username` AS `label`,'0' AS `pid`,'leaf' AS `node_type`,'user' AS `model_type`,'user' AS `model_inner_type`,'target' AS `auth_type`,`sys_user`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `sys_user` where (`sys_user`.`is_admin` <> 1) union all select `sys_role`.`role_id` AS `id`,`sys_role`.`name` AS `name`,`sys_role`.`name` AS `label`,'0' AS `pid`,'leaf' AS `node_type`,'role' AS `model_type`,'role' AS `model_inner_type`,'target' AS `auth_type`,`sys_role`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `sys_role` union all select `sys_dept`.`dept_id` AS `id`,`sys_dept`.`name` AS `name`,`sys_dept`.`name` AS `lable`,(cast(`sys_dept`.`pid` as char charset utf8mb4) collate utf8mb4_general_ci) AS `pid`,if((`sys_dept`.`sub_count` = 0),'leaf','spine') AS `node_type`,'dept' AS `model_type`,'dept' AS `model_inner_type`,'target' AS `auth_type`,`sys_dept`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `sys_dept` union all select `datasource`.`id` AS `id`,`datasource`.`name` AS `NAME`,`datasource`.`name` AS `label`,'0' AS `pid`,'leaf' AS `node_type`,'link' AS `model_type`,`datasource`.`type` AS `model_inner_type`,'source' AS `auth_type`,`datasource`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `datasource` union all select `dataset_group`.`id` AS `id`,`dataset_group`.`name` AS `NAME`,`dataset_group`.`name` AS `lable`,if(isnull(`dataset_group`.`pid`),'0',`dataset_group`.`pid`) AS `pid`,'spine' AS `node_type`,'dataset' AS `model_type`,`dataset_group`.`type` AS `model_inner_type`,'source' AS `auth_type`,`dataset_group`.`create_by` AS `create_by`,`dataset_group`.`level` AS `level`,0 AS `mode`,'0' AS `data_source_id` from `dataset_group` union all select `dataset_table`.`id` AS `id`,`dataset_table`.`name` AS `NAME`,`dataset_table`.`name` AS `lable`,`dataset_table`.`scene_id` AS `pid`,'leaf' AS `node_type`,'dataset' AS `model_type`,`dataset_table`.`type` AS `model_inner_type`,'source' AS `auth_type`,`dataset_table`.`create_by` AS `create_by`,0 AS `level`,`dataset_table`.`mode` AS `mode`,`dataset_table`.`data_source_id` AS `data_source_id` from `dataset_table` union all select `chart_group`.`id` AS `id`,`chart_group`.`name` AS `name`,`chart_group`.`name` AS `label`,if(isnull(`chart_group`.`pid`),'0',`chart_group`.`pid`) AS `pid`,'spine' AS `node_type`,'chart' AS `model_type`,`chart_group`.`type` AS `model_inner_type`,'source' AS `auth_type`,`chart_group`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `chart_group` union all select `chart_view`.`id` AS `id`,`chart_view`.`name` AS `name`,`chart_view`.`name` AS `label`,`chart_view`.`scene_id` AS `pid`,'leaf' AS `node_type`,'chart' AS `model_type`,`chart_view`.`type` AS `model_inner_type`,'source' AS `auth_type`,`chart_view`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `chart_view` union all select `panel_group`.`id` AS `id`,`panel_group`.`name` AS `NAME`,`panel_group`.`name` AS `label`,(case `panel_group`.`id` when 'panel_list' then '0' when 'default_panel' then '0' else `panel_group`.`pid` end) AS `pid`,if((`panel_group`.`node_type` = 'folder'),'spine','leaf') AS `node_type`,'panel' AS `model_type`,`panel_group`.`panel_type` AS `model_inner_type`,'source' AS `auth_type`,`panel_group`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `panel_group` union all select `sys_menu`.`menu_id` AS `menu_id`,`sys_menu`.`title` AS `name`,`sys_menu`.`title` AS `label`,`sys_menu`.`pid` AS `pid`,if((`sys_menu`.`sub_count` > 0),'spine','leaf') AS `node_type`,'menu' AS `model_type`,(case `sys_menu`.`type` when 0 then 'folder' when 1 then 'menu' when 2 then 'button' end) AS `model_inner_type`,'source' AS `auth_type`,`sys_menu`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `sys_menu` where ((`sys_menu`.`i_frame` <> 1) or isnull(`sys_menu`.`i_frame`)) union all select `plugin_sys_menu`.`menu_id` AS `menu_id`,`plugin_sys_menu`.`title` AS `name`,`plugin_sys_menu`.`title` AS `label`,`plugin_sys_menu`.`pid` AS `pid`,if((`plugin_sys_menu`.`sub_count` > 0),'spine','leaf') AS `node_type`,'menu' AS `model_type`,(case `plugin_sys_menu`.`type` when 0 then 'folder' when 1 then 'menu' when 2 then 'button' end) AS `model_inner_type`,'source' AS `auth_type`,`plugin_sys_menu`.`create_by` AS `create_by`,0 AS `level`,0 AS `mode`,'0' AS `data_source_id` from `plugin_sys_menu` where ((`plugin_sys_menu`.`i_frame` <> 1) or isnull(`plugin_sys_menu`.`i_frame`)) ;

-- ----------------------------
-- View structure for `v_auth_privilege`
-- ----------------------------
DROP VIEW IF EXISTS `v_auth_privilege`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_auth_privilege` AS select `sys_auth`.`auth_source` AS `auth_source`,`sys_auth`.`auth_source_type` AS `auth_source_type`,group_concat(`sys_auth_detail`.`privilege_extend` separator ',') AS `privileges` from (`sys_auth` left join `sys_auth_detail` on((`sys_auth`.`id` = `sys_auth_detail`.`auth_id`))) where ((`sys_auth_detail`.`privilege_value` = 1) and (((`sys_auth`.`auth_target_type` = 'dept') and (`sys_auth`.`auth_target` = (select `sys_user`.`dept_id` from `sys_user` where (`sys_user`.`user_id` = '4')))) or ((`sys_auth`.`auth_target_type` = 'user') and (`sys_auth`.`auth_target` = '4')) or ((`sys_auth`.`auth_target_type` = 'role') and (`sys_auth`.`auth_target` = (select `sys_users_roles`.`role_id` from `sys_users_roles` where (`sys_users_roles`.`user_id` = '4')))))) group by `sys_auth`.`auth_source`,`sys_auth`.`auth_source_type` ;

-- ----------------------------
-- Function structure for `CHECK_TREE_NO_MANAGE_PRIVILEGE`
-- ----------------------------
DROP FUNCTION IF EXISTS `CHECK_TREE_NO_MANAGE_PRIVILEGE`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `CHECK_TREE_NO_MANAGE_PRIVILEGE`(userId varchar(255),modelType varchar(255),nodeId varchar(255)) RETURNS int(11)
    READS SQL DATA
BEGIN
DECLARE privilegeType INTEGER;
DECLARE allTreeIds longtext;
DECLARE allPrivilegeTreeIds longtext;
DECLARE result INTEGER;
select privilege_type into privilegeType from sys_auth_detail where auth_id =modelType and privilege_extend ='manage';
select GET_V_AUTH_MODEL_WITH_CHILDREN( nodeId ,modelType) into allTreeIds;
select GET_V_AUTH_MODEL_WITH_PRIVILEGE(userId,modelType,privilegeType) into allPrivilegeTreeIds;
select count(id) into result from v_auth_model where v_auth_model.model_type=modelType and FIND_IN_SET(v_auth_model.id,allTreeIds) and (!FIND_IN_SET(v_auth_model.id,allPrivilegeTreeIds) or ISNULL(allPrivilegeTreeIds));
RETURN result;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `copy_auth`
-- ----------------------------
DROP FUNCTION IF EXISTS `copy_auth`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `copy_auth`(authSource varchar(255),authSourceType varchar(255),authUser varchar(255)) RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE authId varchar(255);
DECLARE userId  varchar(255);
select uuid() into authId;
select max(sys_user.user_id) into userId from sys_user where username= authUser;
delete from sys_auth_detail where auth_id in (
select id from  sys_auth where sys_auth.auth_source=authSource and sys_auth.auth_source_type=authSourceType
);
delete from sys_auth where sys_auth.auth_source=authSource and sys_auth.auth_source_type=authSourceType;
INSERT INTO sys_auth (
	id,
	auth_source,
	auth_source_type,
	auth_target,
	auth_target_type,
	auth_time,
	auth_user
)
VALUES
	(
		authId,
		authSource,
		authSourceType,
		userId,
		'user',
	unix_timestamp(
	now())* 1000,'auto');
	INSERT INTO  sys_auth_detail (
            id,
            auth_id,
            privilege_name,
            privilege_type,
            privilege_value,
            privilege_extend,
            remark,
            create_user,
            create_time
        ) SELECT
        uuid() AS id,
        authId AS auth_id,
        sys_auth_detail.privilege_name,
        sys_auth_detail.privilege_type,
        1,
        sys_auth_detail.privilege_extend,
        sys_auth_detail.remark,
        'auto' AS create_user,
        unix_timestamp(now())* 1000 AS create_time
        FROM
            sys_auth_detail where auth_id =authSourceType;
RETURN 'sucess';
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `delete_auth_source`
-- ----------------------------
DROP FUNCTION IF EXISTS `delete_auth_source`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `delete_auth_source`(authSource varchar(255),authSourceType varchar(255)) RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
delete from sys_auth_detail where auth_id in (
select id from  sys_auth where sys_auth.auth_source=authSource and sys_auth.auth_source_type=authSourceType
);
delete from sys_auth where sys_auth.auth_source=authSource and sys_auth.auth_source_type=authSourceType;
RETURN 'sucess';
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `delete_auth_target`
-- ----------------------------
DROP FUNCTION IF EXISTS `delete_auth_target`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `delete_auth_target`(authTarget varchar(255),authTargetType varchar(255)) RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
delete from sys_auth_detail where auth_id in (
select id from  sys_auth where sys_auth.auth_target=authTarget and sys_auth.auth_target_type=authTargetType
);
delete from sys_auth where sys_auth.auth_target=authTarget and sys_auth.auth_target_type=authTargetType;
RETURN 'sucess';
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `get_auths`
-- ----------------------------
DROP FUNCTION IF EXISTS `get_auths`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `get_auths`(authSource varchar(255),modelType varchar(255),userId varchar(255)) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTemp longtext;
SELECT
	group_concat( DISTINCT sys_auth_detail.privilege_extend) into oTemp
FROM
	(
		`sys_auth`
		LEFT JOIN `sys_auth_detail` ON ((
				`sys_auth`.`id` = `sys_auth_detail`.`auth_id`
			)))
			where sys_auth_detail.privilege_value =1
			and sys_auth.auth_source=authSource
			AND (
				(
					sys_auth.auth_target_type = 'dept'
					AND sys_auth.auth_target in ( SELECT dept_id FROM sys_user WHERE user_id = userId )
				)
				OR (
					sys_auth.auth_target_type = 'user'
					AND sys_auth.auth_target = userId
				)
				OR (
					sys_auth.auth_target_type = 'role'
					AND sys_auth.auth_target in ( SELECT role_id FROM sys_users_roles WHERE user_id = userId )
				)
			)
GROUP BY
	`sys_auth`.`auth_source`,
	`sys_auth`.`auth_source_type`;
RETURN oTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `get_auth_children_count`
-- ----------------------------
DROP FUNCTION IF EXISTS `get_auth_children_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `get_auth_children_count`(pidInfo varchar(255),modelType varchar(255),userName varchar(255)) RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE childrenCount INTEGER;
select count(1)-1 into childrenCount from v_auth_model where FIND_IN_SET(
		v_auth_model.id,
	GET_V_AUTH_MODEL_WITH_CHILDREN ( pidInfo, modelType ))
	AND create_by = userName
	AND v_auth_model.node_type = 'leaf';
RETURN childrenCount;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_CHART_VIEW_COPY_NAME`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_CHART_VIEW_COPY_NAME`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_CHART_VIEW_COPY_NAME`(chartId varchar(255)) RETURNS varchar(255) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE chartName varchar(255);
DECLARE pid varchar(255);
DECLARE regexpInfo varchar(255);
DECLARE chartNameCount INTEGER;
select `name` ,`scene_id` into chartName, pid from chart_view where id =chartId;

select (count(1)+1) into chartNameCount from chart_view
where (LENGTH(REPLACE(name,chartName,''))-LENGTH(replace(REPLACE(name,chartName,''),'-',''))=1)
and REPLACE(name,chartName,'') REGEXP '-copy\\(([0-9])+\\)$' and name like CONCAT(chartName,'%') and chart_view.scene_id=pid ;
RETURN concat(chartName,'-copy(',chartNameCount,')');
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `get_grant_auths`
-- ----------------------------
DROP FUNCTION IF EXISTS `get_grant_auths`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `get_grant_auths`(modelType VARCHAR ( 255 ),
	userId VARCHAR ( 255 )) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
	DECLARE
		oTemp LONGTEXT;
	SELECT
		GROUP_CONCAT( DISTINCT v_auth_model.id ) into oTemp
	FROM
		v_auth_model
		LEFT JOIN sys_auth ON v_auth_model.id = sys_auth.auth_source
		AND v_auth_model.model_type = sys_auth.auth_source_type
		LEFT JOIN sys_auth_detail ON sys_auth.id = sys_auth_detail.auth_id
	WHERE
		privilege_type = 15
		AND privilege_value = 1
		AND v_auth_model.model_type = modelType
		AND (
			(
				sys_auth.auth_target_type = 'dept'
				AND sys_auth.auth_target IN ( SELECT dept_id FROM sys_user WHERE user_id = userId )
			)
			OR (
				sys_auth.auth_target_type = 'user'
				AND sys_auth.auth_target = userId
			)
			OR (
				sys_auth.auth_target_type = 'role'
				AND sys_auth.auth_target IN ( SELECT role_id FROM sys_users_roles WHERE user_id = userId )
			)
		);
	RETURN oTemp;
	END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_PANEL_GROUP_WITH_CHILDREN`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_PANEL_GROUP_WITH_CHILDREN`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_PANEL_GROUP_WITH_CHILDREN`(parentId varchar(8000)) RETURNS varchar(8000) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTemp VARCHAR(8000);
DECLARE oTempChild VARCHAR(8000);
SET oTemp = '';
SET oTempChild = CAST(parentId AS CHAR CHARACTER set utf8mb4) COLLATE utf8mb4_general_ci;
WHILE oTempChild IS NOT NULL
DO
SET oTemp = CONCAT(oTemp,',',oTempChild);
SELECT GROUP_CONCAT(id) INTO oTempChild FROM panel_group WHERE FIND_IN_SET(pid,oTempChild) > 0;
END WHILE;
RETURN oTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_PANEL_TEMPLATE_WITH_CHILDREN`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_PANEL_TEMPLATE_WITH_CHILDREN`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_PANEL_TEMPLATE_WITH_CHILDREN`(parentId varchar(8000)) RETURNS varchar(8000) CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTemp VARCHAR(8000);
DECLARE oTempChild VARCHAR(8000);
SET oTemp = '';
SET oTempChild = CAST(parentId AS CHAR CHARACTER set utf8mb4) COLLATE utf8mb4_general_ci;
WHILE oTempChild IS NOT NULL
DO
SET oTemp = CONCAT(oTemp,',',oTempChild);
SELECT GROUP_CONCAT(id) INTO oTempChild FROM panel_template WHERE FIND_IN_SET(pid,oTempChild) > 0;
END WHILE;
RETURN oTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_V_AUTH_MODEL_ID_P_USE`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_V_AUTH_MODEL_ID_P_USE`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_V_AUTH_MODEL_ID_P_USE`(userId longtext,modelType varchar(255)) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTempLeafIds longtext;
DECLARE oTempAllIds longtext;
select GET_V_AUTH_MODEL_WITH_PRIVILEGE(userId,modelType,1) into oTempLeafIds;
select GROUP_CONCAT(id) into oTempAllIds from (select GET_V_AUTH_MODEL_WITH_PARENT ( oTempLeafIds ,modelType) cids) t, v_auth_model where v_auth_model.model_type=modelType and FIND_IN_SET(v_auth_model.id,cids) order by id asc;
RETURN oTempAllIds;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_V_AUTH_MODEL_WITH_CHILDREN`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_V_AUTH_MODEL_WITH_CHILDREN`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_V_AUTH_MODEL_WITH_CHILDREN`(parentId longtext,modelType varchar(255)) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTemp longtext;
DECLARE oTempChild longtext;
SET oTemp = '';
SET oTempChild = CAST(parentId AS CHAR CHARACTER set utf8mb4) COLLATE utf8mb4_general_ci;
WHILE oTempChild IS NOT NULL
DO
SET oTemp = CONCAT(oTemp,',',oTempChild);
SELECT GROUP_CONCAT(id) INTO oTempChild FROM V_AUTH_MODEL WHERE FIND_IN_SET(pid,oTempChild) > 0 and V_AUTH_MODEL.model_type=modelType order by id asc;
END WHILE;
RETURN oTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_V_AUTH_MODEL_WITH_PARENT`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_V_AUTH_MODEL_WITH_PARENT`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_V_AUTH_MODEL_WITH_PARENT`(childrenId longtext,modelType varchar(255)) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTemp longtext;
DECLARE oTempParent longtext;
SET oTemp = '';
SET oTempParent = CAST(childrenId AS CHAR CHARACTER set utf8mb4) COLLATE utf8mb4_general_ci;
WHILE oTempParent IS NOT NULL
DO
SET oTemp = CONCAT(oTemp,',',oTempParent);
SELECT GROUP_CONCAT(distinct pid) INTO oTempParent FROM V_AUTH_MODEL WHERE FIND_IN_SET(id,oTempParent) > 0 and V_AUTH_MODEL.model_type=modelType order by pid asc;
END WHILE;
RETURN oTemp;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `GET_V_AUTH_MODEL_WITH_PRIVILEGE`
-- ----------------------------
DROP FUNCTION IF EXISTS `GET_V_AUTH_MODEL_WITH_PRIVILEGE`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GET_V_AUTH_MODEL_WITH_PRIVILEGE`(userId longtext,modelType varchar(255),privilegeType varchar(255)) RETURNS longtext CHARSET utf8mb4 COLLATE utf8mb4_general_ci
    READS SQL DATA
BEGIN
DECLARE oTempLeafIds longtext;
select GROUP_CONCAT(auth_source) into oTempLeafIds from (
SELECT
			sys_auth.auth_source_type,
			sys_auth.auth_source
		FROM
			sys_auth
			LEFT JOIN sys_auth_detail ON sys_auth.id = sys_auth_detail.auth_id
		WHERE
			sys_auth_detail.privilege_type = privilegeType
			and sys_auth.auth_source_type = modelType
			AND (
				(
					sys_auth.auth_target_type = 'dept'
					AND sys_auth.auth_target in ( SELECT dept_id FROM sys_user WHERE user_id = userId )
				)
				OR (
					sys_auth.auth_target_type = 'user'
					AND sys_auth.auth_target = userId
				)
				OR (
					sys_auth.auth_target_type = 'role'
					AND sys_auth.auth_target in ( SELECT role_id FROM sys_users_roles WHERE user_id = userId )
				)
				OR (1 = ( SELECT is_admin FROM sys_user WHERE user_id = userId ))
			)
		GROUP BY
			sys_auth.auth_source_type,
			sys_auth.auth_source
			having  (sum( sys_auth_detail.privilege_value )> 0 or 1 = ( SELECT is_admin FROM sys_user WHERE user_id = userId ))) temp;
RETURN oTempLeafIds;
END
;;
DELIMITER ;




