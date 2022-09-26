use jpaas_user;

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
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022-7-22 16:19:52                           */
/*==============================================================*/


/*==============================================================*/
/* Table: OS_COMPANY_AUTH                                       */
/*==============================================================*/
CREATE TABLE OS_COMPANY_AUTH
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   GROUP_ID_            VARCHAR(64) COMMENT '授权组',
   GROUP_NAME_          VARCHAR(64) COMMENT '授权组名称',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_COMPANY_AUTH COMMENT '公司授权';

/*==============================================================*/
/* Table: OS_DD_AGENT                                           */
/*==============================================================*/
CREATE TABLE OS_DD_AGENT
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(64) NOT NULL COMMENT '应用名称',
   TYPE_                VARCHAR(40) COMMENT '应用类型',
   APP_KEY_             VARCHAR(64) COMMENT '应用KEY',
   AGENT_ID_            VARCHAR(64) COMMENT '应用ID',
   CORP_ID_             VARCHAR(64) COMMENT '企业ID',
   PC_HOMEPAGE_         VARCHAR(255) COMMENT 'PC主页',
   ADMIN_PAGE_          VARCHAR(255) COMMENT '后台主页',
   H5_HOMEPAGE_         VARCHAR(255) COMMENT 'H5主页',
   IS_DEFAULT_          INT COMMENT '是否默认',
   SECRET_              VARCHAR(64) COMMENT '密钥',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           INT COMMENT '更新人',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_DD_AGENT COMMENT '钉钉应用表';

/*==============================================================*/
/* Table: OS_DIMENSION                                          */
/*==============================================================*/
CREATE TABLE OS_DIMENSION
(
   DIM_ID_              VARCHAR(64) NOT NULL COMMENT '维度ID',
   NAME_                VARCHAR(40) NOT NULL COMMENT '维度名称',
   CODE_                VARCHAR(64) NOT NULL COMMENT '维度业务主键',
   IS_SYSTEM_           VARCHAR(10) NOT NULL COMMENT '是否系统预设维度',
   STATUS_              VARCHAR(40) NOT NULL COMMENT '状态
            actived 已激活；locked 锁定（禁用）；deleted 已删除',
   SN_                  INT NOT NULL COMMENT '排序号',
   SHOW_TYPE_           VARCHAR(40) NOT NULL COMMENT '数据展示类型
            tree=树型数据。flat=平铺数据',
   IS_GRANT_            VARCHAR(10) COMMENT '是否参与授权',
   DESC_                VARCHAR(255) COMMENT '描述',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (DIM_ID_)
);

ALTER TABLE OS_DIMENSION COMMENT '用户组维度';

/*==============================================================*/
/* Table: OS_FS_AGENT                                           */
/*==============================================================*/
CREATE TABLE OS_FS_AGENT
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(64) COMMENT '应用名称',
   AGENT_ID_            VARCHAR(64) COMMENT '飞书应用ID',
   SECRET_              VARCHAR(64) COMMENT '密钥',
   PC_HOMEPAGE_         VARCHAR(100) COMMENT 'PC主页',
   ADMIN_PAGE_          VARCHAR(100) COMMENT '后台主页',
   H5_HOMEPAGE_         VARCHAR(255) COMMENT 'H5主页',
   IS_DEFAULT_          INT COMMENT '是否默认，1是，0否',
   APP_ID_              VARCHAR(64) COMMENT '应用ID',
   IS_PUSH_             VARCHAR(10) NOT NULL COMMENT '是否推送',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_FS_AGENT COMMENT '飞书集成配置表';

/*==============================================================*/
/* Table: OS_GROUP                                              */
/*==============================================================*/
CREATE TABLE OS_GROUP
(
   GROUP_ID_            VARCHAR(64) NOT NULL COMMENT '用户组ID',
   DIM_ID_              VARCHAR(64) NOT NULL COMMENT '维度ID',
   NAME_                VARCHAR(64) NOT NULL COMMENT '用户组名称',
   KEY_                 VARCHAR(64) NOT NULL COMMENT '用户组业务主键',
   RANK_LEVEL_          INT COMMENT '所属用户组等级值',
   STATUS_              VARCHAR(40) NOT NULL COMMENT '状态
            inactive 未激活；actived 已激活；locked 锁定；deleted 已删除',
   DESCP_               VARCHAR(255) COMMENT '描述',
   SN_                  INT NOT NULL COMMENT '排序号',
   PARENT_ID_           VARCHAR(64) COMMENT '父用户组ID',
   PATH_                VARCHAR(200) COMMENT '路径',
   AREA_CODE_           VARCHAR(50) COMMENT '区域编码',
   FORM_                VARCHAR(20) COMMENT '来源
            sysem,系统添加,import导入,grade,分级添加的',
   SYNC_WX_             INT COMMENT '同步到微信',
   WX_PARENT_PID_       INT COMMENT '微信内部维护父部门ID',
   WX_PID_              INT COMMENT '微信平台部门唯一ID',
   IS_DEFAULT_          VARCHAR(40) COMMENT '是否默认，代表系统自带，不允许删除',
   DD_PARENT_ID_        VARCHAR(64) COMMENT '钉钉父ID',
   DD_ID_               VARCHAR(64) COMMENT '钉钉ID',
   IS_LEAF_             CHAR(1) COMMENT '是否为子叶 Y/N',
   TYPE_                INT COMMENT '组织类型，1代表公司、2代表部门',
   SHORT_NAME_          VARCHAR(64) COMMENT '简称',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (GROUP_ID_)
);

ALTER TABLE OS_GROUP COMMENT '系统用户组';

/*==============================================================*/
/* Index: IDX_GROUP_PARENT                                      */
/*==============================================================*/
CREATE INDEX IDX_GROUP_PARENT ON OS_GROUP
(
   PARENT_ID_
);

/*==============================================================*/
/* Table: OS_GROUP_MENU                                         */
/*==============================================================*/
CREATE TABLE OS_GROUP_MENU
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   MENU_ID_             VARCHAR(64) COMMENT '菜单ID',
   GROUP_ID_            VARCHAR(64) COMMENT '用户组Id',
   APP_ID_              VARCHAR(64) COMMENT '应用ID',
   NAME_                VARCHAR(60) COMMENT '菜单名称',
   ICON_PC_             VARCHAR(100) COMMENT 'PC图标样式',
   ICON_PIC_            VARCHAR(100) COMMENT '图标',
   ICON_APP_            VARCHAR(50) COMMENT 'APP图标样式',
   PARENT_ID_           VARCHAR(64) COMMENT '上级父ID',
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
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_GROUP_MENU COMMENT '用户组菜单';

/*==============================================================*/
/* Table: OS_INST                                               */
/*==============================================================*/
CREATE TABLE OS_INST
(
   INST_ID_             VARCHAR(64) NOT NULL COMMENT '机构ID',
   NAME_CN_             VARCHAR(256) NOT NULL COMMENT '公司中文名',
   NAME_EN_             VARCHAR(256) COMMENT '公司英文名',
   INST_NO_             VARCHAR(50) COMMENT '机构编码',
   BUS_LICE_NO_         VARCHAR(50) COMMENT '营业执照编码',
   DOMAIN_              VARCHAR(100) COMMENT '公司域名',
   NAME_CN_S_           VARCHAR(80) COMMENT '公司简称(中文)',
   NAME_EN_S_           VARCHAR(80) COMMENT '公司简称(英文)',
   LEGAL_MAN_           VARCHAR(64) COMMENT '公司法人',
   DESCP_               VARCHAR(1000) COMMENT '公司描述',
   ADDRESS_             VARCHAR(128) COMMENT '地址',
   PHONE_               VARCHAR(30) COMMENT '联系电话',
   EMAIL_               VARCHAR(255) COMMENT '邮箱',
   FAX_                 VARCHAR(30) COMMENT '传真',
   CONTRACTOR_          VARCHAR(30) COMMENT '联系人',
   HOME_URL_            VARCHAR(120) COMMENT '首页URL',
   INST_TYPE_           VARCHAR(64) COMMENT '机构类型',
   STATUS_              VARCHAR(30) COMMENT '状态',
   PARENT_ID_           VARCHAR(64) COMMENT '父ID',
   PATH_                VARCHAR(1024) COMMENT '路径',
   DATASOURCE_          VARCHAR(64) COMMENT '数据源',
   LABEL_               VARCHAR(64) COMMENT '标签',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (INST_ID_)
);

ALTER TABLE OS_INST COMMENT '注册机构';

/*==============================================================*/
/* Table: OS_INST_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE
(
   TYPE_ID_             VARCHAR(64) NOT NULL COMMENT '类型',
   TYPE_CODE_           VARCHAR(50) COMMENT '类型编码',
   TYPE_NAME_           VARCHAR(100) COMMENT '类型名称',
   ENABLED_             VARCHAR(20) COMMENT '是否启用',
   IS_DEFAULT_          VARCHAR(20) COMMENT '是否系统缺省',
   HOME_URL_            VARCHAR(200) COMMENT '首页URL',
   DESCP_               VARCHAR(500) COMMENT '描述',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (TYPE_ID_)
);

ALTER TABLE OS_INST_TYPE COMMENT '机构类型';

/*==============================================================*/
/* Table: OS_INST_TYPE_MENU                                     */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE_MENU
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   INST_TYPE_ID_        VARCHAR(64) COMMENT '机构类型ID',
   APP_ID_              VARCHAR(64) COMMENT '应用ID',
   MENU_ID_             VARCHAR(64) COMMENT '菜单ID',
   NAME_                VARCHAR(60) NOT NULL COMMENT '菜单名称',
   ICON_PC_             VARCHAR(100) COMMENT 'PC图标样式',
   ICON_PIC_            VARCHAR(100) COMMENT '图标',
   ICON_APP_            VARCHAR(50) COMMENT 'APP图标样式',
   PARENT_ID_           VARCHAR(64) COMMENT '上级父ID',
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
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_INST_TYPE_MENU COMMENT '机构类型授权菜单';

/*==============================================================*/
/* Table: OS_INST_USERS                                         */
/*==============================================================*/
CREATE TABLE OS_INST_USERS
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   USER_ID_             VARCHAR(64) COMMENT '用户ID',
   IS_ADMIN_            INT COMMENT '管理员',
   USER_TYPE_           VARCHAR(64) COMMENT '用户类型',
   APPLY_NOTE_          VARCHAR(255) COMMENT '申请理由',
   APPLY_STATUS_        VARCHAR(40) COMMENT '申请状态',
   CREATE_TYPE_         VARCHAR(40) COMMENT '创建类型',
   APPROVE_USER_        VARCHAR(64) COMMENT '审批用户',
   STATUS_              VARCHAR(40) COMMENT '状态',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_INST_USERS COMMENT '租户用户关联表';

/*==============================================================*/
/* Index: IDX_OS_INST_USERS_USERID                              */
/*==============================================================*/
CREATE INDEX IDX_OS_INST_USERS_USERID ON OS_INST_USERS
(
   USER_ID_
);

/*==============================================================*/
/* Table: OS_PASSWORD_INPUT_ERROR                               */
/*==============================================================*/
CREATE TABLE OS_PASSWORD_INPUT_ERROR
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   USER_NO_             VARCHAR(64) COMMENT '用户编号',
   ERROR_TIME_          INT COMMENT '用户错误输入密码错误次数',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_PASSWORD_INPUT_ERROR COMMENT '用户密码输入错误记录表';

/*==============================================================*/
/* Index: IDX_PASSWORD_USER_NO                                  */
/*==============================================================*/
CREATE INDEX IDX_PASSWORD_USER_NO ON OS_PASSWORD_INPUT_ERROR
(
   USER_NO_
);

/*==============================================================*/
/* Table: OS_PASSWORD_POLICY                                    */
/*==============================================================*/
CREATE TABLE OS_PASSWORD_POLICY
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   MIN_LENGTH_          INT COMMENT '最小密码长度',
   ERROR_TIME_          INT COMMENT '最多错误输入密码错误次数',
   ACCOUNT_LOCK_DAY_    INT COMMENT '账号锁定天数',
   IS_MIX_              VARCHAR(10) NOT NULL COMMENT '密码同时由英文大小写特殊字符和数字构成',
   IS_NEVER_TIMEOUT_    VARCHAR(10) NOT NULL COMMENT '密码永不过期',
   IS_FIRST_LOGIN_UPDATE_ VARCHAR(10) NOT NULL COMMENT '用户首次登录必须修改密码',
   IS_USERNAME_PWD_CONSISTENT_ VARCHAR(10) NOT NULL COMMENT '用户名密码能否一致',
   TIMEOUT_DAY_         INT COMMENT '密码过期时间（天）',
   INFORM_DAY_          INT COMMENT '几天后通知修改密码（天）',
   INFORM_FREQUENCY_    INT COMMENT '通知频率(每隔多少天通知一次)',
   INFORM_TYPE_         VARCHAR(64) COMMENT '密码修改通知方式',
   INFORM_CONTENT_      VARCHAR(500) COMMENT '通知内容',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_PASSWORD_POLICY COMMENT '密码安全策略';

/*==============================================================*/
/* Table: OS_PROPERTIES_DEF                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_DEF
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   GROUP_ID_            VARCHAR(64) COMMENT '分组ID',
   DIM_ID_              VARCHAR(64) COMMENT '维度ID',
   NAME_                VARCHAR(64) COMMENT '参数名称',
   DATA_TYPE_           VARCHAR(40) COMMENT '数据类型
            number,
            varchar
            date',
   CTLTYPE_             VARCHAR(20) COMMENT '控件类型',
   EXT_JSON_            VARCHAR(2000) COMMENT '扩展属性',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_PROPERTIES_DEF COMMENT '自定义属性配置';

/*==============================================================*/
/* Table: OS_PROPERTIES_GROUP                                   */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_GROUP
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(64) COMMENT '分组名称',
   ENABLED_             VARCHAR(40) COMMENT '是否允许',
   DIM_ID_              VARCHAR(64) COMMENT '维度ID
            用户的维度为-1',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_PROPERTIES_GROUP COMMENT '自定义属性分组';

/*==============================================================*/
/* Table: OS_PROPERTIES_VAL                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_VAL
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   DIM_ID_              VARCHAR(64) COMMENT '维度ID',
   GROUP_ID_            VARCHAR(64) COMMENT '分组ID',
   OWNER_ID_            VARCHAR(64) COMMENT '所有者',
   PROPERY_ID_          VARCHAR(64) COMMENT '属性ID',
   NUM_VAL_             NUMERIC(14,2) COMMENT '数据值',
   TXT_VAL              VARCHAR(200) COMMENT '文本值',
   DATE_VAL             DATETIME COMMENT '日期值',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_PROPERTIES_VAL COMMENT '扩展属性值';

/*==============================================================*/
/* Index: IDX_PROPERTY_OWNER                                    */
/*==============================================================*/
CREATE INDEX IDX_PROPERTY_OWNER ON OS_PROPERTIES_VAL
(
   OWNER_ID_
);

/*==============================================================*/
/* Table: OS_RANK_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_RANK_TYPE
(
   RK_ID_               VARCHAR(64) NOT NULL COMMENT '等级ID',
   DIM_ID_              VARCHAR(64) COMMENT '维度ID',
   NAME_                VARCHAR(255) NOT NULL COMMENT '名称',
   KEY_                 VARCHAR(64) NOT NULL COMMENT '分类业务键',
   LEVEL_               INT NOT NULL COMMENT '级别数值',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (RK_ID_)
);

ALTER TABLE OS_RANK_TYPE COMMENT '用户组等级分类，放置组织的等级分类定义
如集团，分公司，部门等级别';

/*==============================================================*/
/* Table: OS_REL_INST                                           */
/*==============================================================*/
CREATE TABLE OS_REL_INST
(
   INST_ID_             VARCHAR(64) NOT NULL COMMENT '用户组关系ID',
   REL_TYPE_ID_         VARCHAR(64) COMMENT '关系类型ID',
   REL_TYPE_KEY_        VARCHAR(64) COMMENT '关系类型KEY_
            ',
   PARTY1_              VARCHAR(64) NOT NULL COMMENT '当前方ID',
   PARTY2_              VARCHAR(64) NOT NULL COMMENT '关联方ID',
   DIM1_                VARCHAR(64) COMMENT '当前方维度',
   DIM2_                VARCHAR(64) COMMENT '关联方维度ID',
   IS_MAIN_             VARCHAR(20) NOT NULL DEFAULT 'NO' COMMENT '是否为主关系',
   STATUS_              VARCHAR(40) NOT NULL COMMENT '状态
            ENABLED
            DISABLED',
   ALIAS_               VARCHAR(80) COMMENT '别名',
   REL_TYPE_            VARCHAR(64) COMMENT '关系类型',
   PATH_                VARCHAR(200) COMMENT '路径',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (INST_ID_)
);

ALTER TABLE OS_REL_INST COMMENT '关系实例';

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY2                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY2 ON OS_REL_INST
(
   PARTY2_
);

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY1                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY1 ON OS_REL_INST
(
   PARTY1_
);

/*==============================================================*/
/* Table: OS_REL_TYPE                                           */
/*==============================================================*/
CREATE TABLE OS_REL_TYPE
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '关系类型ID',
   NAME_                VARCHAR(64) NOT NULL COMMENT '关系名',
   KEY_                 VARCHAR(64) NOT NULL COMMENT '关系业务主键',
   REL_TYPE_            VARCHAR(40) NOT NULL COMMENT '关系类型。用户关系=USER-USER；用户组关系=GROUP-GROUP；用户与组关系=USER-GROUP；组与用户关系=GROUP-USER',
   CONST_TYPE_          VARCHAR(40) COMMENT '关系约束类型。1对1=one2one；1对多=one2many；多对1=many2one；多对多=many2many',
   PARTY1_              VARCHAR(128) NOT NULL COMMENT '关系当前方名称',
   PARTY2_              VARCHAR(128) NOT NULL COMMENT '关系关联方名称',
   DIM_ID1_             VARCHAR(64) COMMENT '当前方维度ID（仅对用户组关系）',
   DIM_ID2_             VARCHAR(64) COMMENT '关联方维度ID（用户关系忽略此值）',
   LEVEL_               INT,
   STATUS_              VARCHAR(40) NOT NULL COMMENT '状态。actived 已激活；locked 锁定；deleted 已删除',
   IS_SYSTEM_           VARCHAR(10) NOT NULL COMMENT '是否系统预设',
   IS_DEFAULT_          VARCHAR(10) NOT NULL COMMENT '是否默认',
   IS_TWO_WAY_          VARCHAR(10) COMMENT '是否双向关系',
   MEMO_                VARCHAR(255) COMMENT '关系备注',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_REL_TYPE COMMENT '关系类型定义';

/*==============================================================*/
/* Table: OS_USER                                               */
/*==============================================================*/
CREATE TABLE OS_USER
(
   USER_ID_             VARCHAR(64) NOT NULL COMMENT '用户ID',
   FULLNAME_            VARCHAR(64) NOT NULL COMMENT '姓名',
   USER_NO_             VARCHAR(64) NOT NULL COMMENT '用户编号',
   PWD_                 VARCHAR(64) COMMENT '密码',
   ENTRY_TIME_          DATETIME COMMENT '入职时间',
   QUIT_TIME_           DATETIME COMMENT '离职时间',
   USER_TYPE_           VARCHAR(20) COMMENT '用户类型',
   FROM_                VARCHAR(20) COMMENT '来源
            system,系统添加,import,导入,grade,分级添加的',
   BIRTHDAY_            DATETIME COMMENT '出生日期',
   SEX_                 VARCHAR(10) COMMENT '姓别',
   MOBILE_              VARCHAR(32) COMMENT '手机',
   EMAIL_               VARCHAR(100) COMMENT '邮件',
   ADDRESS_             VARCHAR(255) COMMENT '地址',
   URGENT_              VARCHAR(64) COMMENT '紧急联系人',
   SYNC_WX_             INT COMMENT '是否同步到微信',
   URGENT_MOBILE_       VARCHAR(20) COMMENT '紧急联系人手机',
   QQ_                  VARCHAR(20) COMMENT 'QQ号',
   PHOTO_               VARCHAR(255) COMMENT '照片',
   ENABLED_             VARCHAR(20) COMMENT '是否允许',
   DD_ID_               VARCHAR(64) COMMENT '钉钉ID',
   OPEN_ID_             VARCHAR(64),
   WX_OPEN_ID_          VARCHAR(64) COMMENT '企业微信OPENID',
   IS_LOCK_             VARCHAR(10) COMMENT '账号是否锁定',
   STATUS_              VARCHAR(50) COMMENT '用户在当前机构的状态
            IN_JOB=在职
            OUT_JOB=离职',
   PWD_UPDATE_TIME_     DATETIME COMMENT '密码修改时间',
   SN_                  INT COMMENT '序号',
   IS_FIRST_LOGIN_      VARCHAR(10) COMMENT '用户是否首次登录',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (USER_ID_)
);

ALTER TABLE OS_USER COMMENT '用户信息表';

/*==============================================================*/
/* Index: IDX_USER_NO                                           */
/*==============================================================*/
CREATE INDEX IDX_USER_NO ON OS_USER
(
   USER_NO_
);

/*==============================================================*/
/* Table: OS_USER_PLATFORM                                      */
/*==============================================================*/
CREATE TABLE OS_USER_PLATFORM
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   USER_ID_             VARCHAR(64) COMMENT '用户ID',
   PLATFORM_TYPE_       INT COMMENT '第三方平台类型：1微信，2企业微信，3钉钉，4飞书',
   OPEN_ID_             VARCHAR(64) COMMENT '第三方平台唯一id',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_USER_PLATFORM COMMENT '第三方平台登陆信息绑定表';

/*==============================================================*/
/* Index: IDX_PLATFORM_USERID                                   */
/*==============================================================*/
CREATE INDEX IDX_PLATFORM_USERID ON OS_USER_PLATFORM
(
   USER_ID_
);

/*==============================================================*/
/* Table: OS_USER_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_USER_TYPE
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   CODE_                VARCHAR(50) NOT NULL COMMENT '编码',
   NAME_                VARCHAR(50) NOT NULL COMMENT '名称',
   DESCP_               VARCHAR(500) COMMENT '描述',
   GROUP_ID_            VARCHAR(64) COMMENT '关联角色ID',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_USER_TYPE COMMENT '用户类型';

/*==============================================================*/
/* Table: OS_WX_ENT_AGENT                                       */
/*==============================================================*/
CREATE TABLE OS_WX_ENT_AGENT
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(100) COMMENT '名称',
   DESCRIPTION_         VARCHAR(200) COMMENT '描述',
   DOMAIN_              VARCHAR(64) COMMENT '信任域名',
   HOME_URL_            VARCHAR(400) COMMENT '主页地址',
   CORP_ID_             VARCHAR(64) COMMENT '企业ID',
   AGENT_ID_            VARCHAR(64) COMMENT '应用ID',
   SECRET_              VARCHAR(64) COMMENT '密钥',
   DEFAULT_AGENT_       INT COMMENT '是否默认',
   TOKEN_               VARCHAR(64) DEFAULT NULL COMMENT '发送消息TOKEN',
   AESKEY_              VARCHAR(64) DEFAULT NULL COMMENT '发送消息AESKEY',
   UPDATE_TIME_         DATETIME COMMENT '更新时间',
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   UPDATE_BY_           VARCHAR(64) COMMENT '更新人',
   CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
   CREATE_TIME_         DATETIME COMMENT '创建时间',
   TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
   CREATE_BY_           VARCHAR(64) COMMENT '创建人',
   PRIMARY KEY (ID_)
);

ALTER TABLE OS_WX_ENT_AGENT COMMENT '企业微信应用表';

