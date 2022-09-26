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
/* Table: OS_DD_AGENT                                           */
/*==============================================================*/
CREATE TABLE OS_DD_AGENT  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(64)                    NOT NULL,
                              TYPE_                VARCHAR2(40),
                              APP_KEY_             VARCHAR2(64),
                              AGENT_ID_            VARCHAR2(64),
                              CORP_ID_             VARCHAR2(64),
                              PC_HOMEPAGE_         VARCHAR2(255),
                              ADMIN_PAGE_          VARCHAR2(255),
                              H5_HOMEPAGE_         VARCHAR2(255),
                              IS_DEFAULT_          INTEGER,
                              SECRET_              VARCHAR2(64),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           INTEGER,
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_DD_AGENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_DD_AGENT IS
'钉钉应用表';

COMMENT ON COLUMN OS_DD_AGENT.ID_ IS
'主键';

COMMENT ON COLUMN OS_DD_AGENT.NAME_ IS
'应用名称';

COMMENT ON COLUMN OS_DD_AGENT.TYPE_ IS
'应用类型';

COMMENT ON COLUMN OS_DD_AGENT.APP_KEY_ IS
'应用KEY';

COMMENT ON COLUMN OS_DD_AGENT.AGENT_ID_ IS
'应用ID';

COMMENT ON COLUMN OS_DD_AGENT.CORP_ID_ IS
'企业ID';

COMMENT ON COLUMN OS_DD_AGENT.PC_HOMEPAGE_ IS
'PC主页';

COMMENT ON COLUMN OS_DD_AGENT.ADMIN_PAGE_ IS
'后台主页';

COMMENT ON COLUMN OS_DD_AGENT.H5_HOMEPAGE_ IS
'H5主页';

COMMENT ON COLUMN OS_DD_AGENT.IS_DEFAULT_ IS
'是否默认';

COMMENT ON COLUMN OS_DD_AGENT.SECRET_ IS
'密钥';

COMMENT ON COLUMN OS_DD_AGENT.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_DEP_ID_ IS
'部门ID';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_DD_AGENT.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN OS_DD_AGENT.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_DIMENSION                                          */
/*==============================================================*/
CREATE TABLE OS_DIMENSION  (
                               DIM_ID_              VARCHAR2(64)                    NOT NULL,
                               NAME_                VARCHAR2(40)                    NOT NULL,
                               CODE_                VARCHAR2(64)                    NOT NULL,
                               IS_SYSTEM_           VARCHAR2(10)                    NOT NULL,
                               STATUS_              VARCHAR2(40)                    NOT NULL,
                               SN_                  INTEGER                         NOT NULL,
                               SHOW_TYPE_           VARCHAR2(40)                    NOT NULL,
                               IS_GRANT_            VARCHAR2(10),
                               DESC_                VARCHAR2(255),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_DIMENSION PRIMARY KEY (DIM_ID_)
);

COMMENT ON TABLE OS_DIMENSION IS
'用户组维度';

COMMENT ON COLUMN OS_DIMENSION.DIM_ID_ IS
'维度ID';

COMMENT ON COLUMN OS_DIMENSION.NAME_ IS
'维度名称';

COMMENT ON COLUMN OS_DIMENSION.CODE_ IS
'维度业务主键';

COMMENT ON COLUMN OS_DIMENSION.IS_SYSTEM_ IS
'是否系统预设维度';

COMMENT ON COLUMN OS_DIMENSION.STATUS_ IS
'状态
actived 已激活；locked 锁定（禁用）；deleted 已删除';

COMMENT ON COLUMN OS_DIMENSION.SN_ IS
'排序号';

COMMENT ON COLUMN OS_DIMENSION.SHOW_TYPE_ IS
'数据展示类型
tree=树型数据。flat=平铺数据';

COMMENT ON COLUMN OS_DIMENSION.IS_GRANT_ IS
'是否参与授权';

COMMENT ON COLUMN OS_DIMENSION.DESC_ IS
'描述';

COMMENT ON COLUMN OS_DIMENSION.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_DIMENSION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_DIMENSION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_DIMENSION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_DIMENSION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_DIMENSION.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_GRADE_ADMIN                                        */
/*==============================================================*/
CREATE TABLE OS_GRADE_ADMIN  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 GROUP_ID_            VARCHAR2(64),
                                 USER_ID_             VARCHAR2(64),
                                 FULLNAME_            VARCHAR2(64),
                                 PARENT_ID_           VARCHAR2(64),
                                 DEPTH_               INTEGER,
                                 PATH_                VARCHAR2(255),
                                 SN_                  INTEGER,
                                 CHILDS_              INTEGER,
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_OS_GRADE_ADMIN PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GRADE_ADMIN IS
'分级管理员表';

COMMENT ON COLUMN OS_GRADE_ADMIN.ID_ IS
'主键';

COMMENT ON COLUMN OS_GRADE_ADMIN.GROUP_ID_ IS
'管理的组';

COMMENT ON COLUMN OS_GRADE_ADMIN.USER_ID_ IS
'管理员ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.FULLNAME_ IS
'管理员名称';

COMMENT ON COLUMN OS_GRADE_ADMIN.PARENT_ID_ IS
'父ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.DEPTH_ IS
'深度';

COMMENT ON COLUMN OS_GRADE_ADMIN.PATH_ IS
'路径';

COMMENT ON COLUMN OS_GRADE_ADMIN.SN_ IS
'序号';

COMMENT ON COLUMN OS_GRADE_ADMIN.CHILDS_ IS
'子节点个数';

COMMENT ON COLUMN OS_GRADE_ADMIN.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_GRADE_ADMIN.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_GRADE_ROLE                                         */
/*==============================================================*/
CREATE TABLE OS_GRADE_ROLE  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                ADMIN_ID_            VARCHAR2(64),
                                GROUP_ID_            VARCHAR2(64),
                                NAME_                VARCHAR2(20),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_GRADE_ROLE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GRADE_ROLE IS
'分级管理员角色';

COMMENT ON COLUMN OS_GRADE_ROLE.ID_ IS
'主键';

COMMENT ON COLUMN OS_GRADE_ROLE.ADMIN_ID_ IS
'管理ID';

COMMENT ON COLUMN OS_GRADE_ROLE.GROUP_ID_ IS
'角色ID';

COMMENT ON COLUMN OS_GRADE_ROLE.NAME_ IS
'角色名';

COMMENT ON COLUMN OS_GRADE_ROLE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_GRADE_ROLE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_GRADE_ROLE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_GROUP                                              */
/*==============================================================*/
CREATE TABLE OS_GROUP  (
                           GROUP_ID_            VARCHAR2(64)                    NOT NULL,
                           DIM_ID_              VARCHAR2(64)                    NOT NULL,
                           NAME_                VARCHAR2(64)                    NOT NULL,
                           KEY_                 VARCHAR2(64)                    NOT NULL,
                           RANK_LEVEL_          INTEGER,
                           STATUS_              VARCHAR2(40)                    NOT NULL,
                           DESCP_               VARCHAR2(255),
                           SN_                  INTEGER                         NOT NULL,
                           PARENT_ID_           VARCHAR2(64),
                           PATH_                VARCHAR2(200),
                           AREA_CODE_           VARCHAR2(50),
                           FORM_                VARCHAR2(20),
                           SYNC_WX_             INTEGER,
                           WX_PARENT_PID_       INTEGER,
                           WX_PID_              INTEGER,
                           IS_DEFAULT_          VARCHAR2(40),
                           DD_PARENT_ID_        VARCHAR2(64),
                           DD_ID_               VARCHAR2(64),
                           IS_LEAF_             CHAR(1),
                           TENANT_ID_           VARCHAR2(64),
                           CREATE_DEP_ID_       VARCHAR2(64),
                           CREATE_BY_           VARCHAR2(64),
                           CREATE_TIME_         DATE,
                           UPDATE_BY_           VARCHAR2(64),
                           UPDATE_TIME_         DATE,
                           CONSTRAINT PK_OS_GROUP PRIMARY KEY (GROUP_ID_)
);

COMMENT ON TABLE OS_GROUP IS
'系统用户组';

COMMENT ON COLUMN OS_GROUP.GROUP_ID_ IS
'用户组ID';

COMMENT ON COLUMN OS_GROUP.DIM_ID_ IS
'维度ID';

COMMENT ON COLUMN OS_GROUP.NAME_ IS
'用户组名称';

COMMENT ON COLUMN OS_GROUP.KEY_ IS
'用户组业务主键';

COMMENT ON COLUMN OS_GROUP.RANK_LEVEL_ IS
'所属用户组等级值';

COMMENT ON COLUMN OS_GROUP.STATUS_ IS
'状态
inactive 未激活；actived 已激活；locked 锁定；deleted 已删除';

COMMENT ON COLUMN OS_GROUP.DESCP_ IS
'描述';

COMMENT ON COLUMN OS_GROUP.SN_ IS
'排序号';

COMMENT ON COLUMN OS_GROUP.PARENT_ID_ IS
'父用户组ID';

COMMENT ON COLUMN OS_GROUP.PATH_ IS
'路径';

COMMENT ON COLUMN OS_GROUP.AREA_CODE_ IS
'区域编码';

COMMENT ON COLUMN OS_GROUP.FORM_ IS
'来源
sysem,系统添加,import导入,grade,分级添加的';

COMMENT ON COLUMN OS_GROUP.SYNC_WX_ IS
'同步到微信';

COMMENT ON COLUMN OS_GROUP.WX_PARENT_PID_ IS
'微信内部维护父部门ID';

COMMENT ON COLUMN OS_GROUP.WX_PID_ IS
'微信平台部门唯一ID';

COMMENT ON COLUMN OS_GROUP.IS_DEFAULT_ IS
'是否默认，代表系统自带，不允许删除';

COMMENT ON COLUMN OS_GROUP.DD_PARENT_ID_ IS
'钉钉父ID';

COMMENT ON COLUMN OS_GROUP.DD_ID_ IS
'钉钉ID';

COMMENT ON COLUMN OS_GROUP.IS_LEAF_ IS
'是否为子叶 Y/N';

COMMENT ON COLUMN OS_GROUP.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_GROUP.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_GROUP.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_GROUP.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_GROUP.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_GROUP.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_GROUP_API                                          */
/*==============================================================*/
CREATE TABLE OS_GROUP_API  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               GROUP_ID_            VARCHAR2(64),
                               API_PATH_            VARCHAR2(100),
                               API_METHOD_          VARCHAR2(64),
                               SERVICE_             VARCHAR2(64),
                               CONSTRAINT PK_OS_GROUP_API PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GROUP_API IS
'用户组API';

COMMENT ON COLUMN OS_GROUP_API.ID_ IS
'主键';

COMMENT ON COLUMN OS_GROUP_API.GROUP_ID_ IS
'用户组Id';

COMMENT ON COLUMN OS_GROUP_API.API_PATH_ IS
'API 路径';

COMMENT ON COLUMN OS_GROUP_API.API_METHOD_ IS
'HTTP方法';

COMMENT ON COLUMN OS_GROUP_API.SERVICE_ IS
'微服务名称';

/*==============================================================*/
/* Table: OS_GROUP_MENU                                         */
/*==============================================================*/
CREATE TABLE OS_GROUP_MENU  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                MENU_ID_             VARCHAR2(64),
                                GROUP_ID_            VARCHAR2(64),
                                APP_ID_              VARCHAR2(64),
                                NAME_                VARCHAR2(60),
                                ICON_PC_             VARCHAR2(100),
                                ICON_PIC_            VARCHAR2(100),
                                ICON_APP_            VARCHAR2(50),
                                PARENT_ID_           VARCHAR2(64)                    NOT NULL,
                                PATH_                VARCHAR2(256),
                                SN_                  INTEGER,
                                SHOW_TYPE_           VARCHAR2(20),
                                MENU_KEY_            VARCHAR2(50),
                                MENU_TYPE_           VARCHAR2(20),
                                COMPONENT_           VARCHAR2(255),
                                SETTING_TYPE_        VARCHAR2(20),
                                BO_LIST_KEY_         VARCHAR2(64),
                                URL_                 VARCHAR2(128),
                                METHOD_              VARCHAR2(20),
                                PARAMS_              VARCHAR2(1000),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_GROUP_MENU PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GROUP_MENU IS
'用户组菜单';

COMMENT ON COLUMN OS_GROUP_MENU.ID_ IS
'主键';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN OS_GROUP_MENU.GROUP_ID_ IS
'用户组Id';

COMMENT ON COLUMN OS_GROUP_MENU.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN OS_GROUP_MENU.NAME_ IS
'菜单名称';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_PC_ IS
'PC图标样式';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_PIC_ IS
'图标';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_APP_ IS
'APP图标样式';

COMMENT ON COLUMN OS_GROUP_MENU.PARENT_ID_ IS
'上级父ID';

COMMENT ON COLUMN OS_GROUP_MENU.PATH_ IS
'路径';

COMMENT ON COLUMN OS_GROUP_MENU.SN_ IS
'序号';

COMMENT ON COLUMN OS_GROUP_MENU.SHOW_TYPE_ IS
'访问方式';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_KEY_ IS
'菜单唯一标识';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_TYPE_ IS
'菜单类型';

COMMENT ON COLUMN OS_GROUP_MENU.COMPONENT_ IS
'展示组件';

COMMENT ON COLUMN OS_GROUP_MENU.SETTING_TYPE_ IS
'配置类型(custom,iframe)';

COMMENT ON COLUMN OS_GROUP_MENU.BO_LIST_KEY_ IS
'单据列表KEY';

COMMENT ON COLUMN OS_GROUP_MENU.URL_ IS
'接口地址';

COMMENT ON COLUMN OS_GROUP_MENU.METHOD_ IS
'接口方法';

COMMENT ON COLUMN OS_GROUP_MENU.PARAMS_ IS
'菜单参数';

COMMENT ON COLUMN OS_GROUP_MENU.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_GROUP_MENU.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_GROUP_MENU.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_INST                                               */
/*==============================================================*/
CREATE TABLE OS_INST  (
                          INST_ID_             VARCHAR2(64)                    NOT NULL,
                          NAME_CN_             VARCHAR2(256)                   NOT NULL,
                          NAME_EN_             VARCHAR2(256),
                          INST_NO_             VARCHAR2(50),
                          BUS_LICE_NO_         VARCHAR2(50),
                          DOMAIN_              VARCHAR2(100),
                          NAME_CN_S_           VARCHAR2(80),
                          NAME_EN_S_           VARCHAR2(80),
                          LEGAL_MAN_           VARCHAR2(64),
                          DESCP_               VARCHAR2(1000),
                          ADDRESS_             VARCHAR2(128),
                          PHONE_               VARCHAR2(30),
                          EMAIL_               VARCHAR2(255),
                          FAX_                 VARCHAR2(30),
                          CONTRACTOR_          VARCHAR2(30),
                          HOME_URL_            VARCHAR2(120),
                          INST_TYPE_           VARCHAR2(64),
                          STATUS_              VARCHAR2(30),
                          PARENT_ID_           VARCHAR2(64),
                          PATH_                VARCHAR2(1024),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          CONSTRAINT PK_OS_INST PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE OS_INST IS
'注册机构';

COMMENT ON COLUMN OS_INST.INST_ID_ IS
'机构ID';

COMMENT ON COLUMN OS_INST.NAME_CN_ IS
'公司中文名';

COMMENT ON COLUMN OS_INST.NAME_EN_ IS
'公司英文名';

COMMENT ON COLUMN OS_INST.INST_NO_ IS
'机构编码';

COMMENT ON COLUMN OS_INST.BUS_LICE_NO_ IS
'营业执照编码';

COMMENT ON COLUMN OS_INST.DOMAIN_ IS
'公司域名';

COMMENT ON COLUMN OS_INST.NAME_CN_S_ IS
'公司简称(中文)';

COMMENT ON COLUMN OS_INST.NAME_EN_S_ IS
'公司简称(英文)';

COMMENT ON COLUMN OS_INST.LEGAL_MAN_ IS
'公司法人';

COMMENT ON COLUMN OS_INST.DESCP_ IS
'公司描述';

COMMENT ON COLUMN OS_INST.ADDRESS_ IS
'地址';

COMMENT ON COLUMN OS_INST.PHONE_ IS
'联系电话';

COMMENT ON COLUMN OS_INST.EMAIL_ IS
'邮箱';

COMMENT ON COLUMN OS_INST.FAX_ IS
'传真';

COMMENT ON COLUMN OS_INST.CONTRACTOR_ IS
'联系人';

COMMENT ON COLUMN OS_INST.HOME_URL_ IS
'首页URL';

COMMENT ON COLUMN OS_INST.INST_TYPE_ IS
'机构类型';

COMMENT ON COLUMN OS_INST.STATUS_ IS
'状态';

COMMENT ON COLUMN OS_INST.PARENT_ID_ IS
'父ID';

COMMENT ON COLUMN OS_INST.PATH_ IS
'路径';

COMMENT ON COLUMN OS_INST.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_INST.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_INST.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_INST.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_INST.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_INST_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE  (
                               TYPE_ID_             VARCHAR2(64)                    NOT NULL,
                               TYPE_CODE_           VARCHAR2(50),
                               TYPE_NAME_           VARCHAR2(100),
                               ENABLED_             VARCHAR2(20),
                               IS_DEFAULT_          VARCHAR2(20),
                               HOME_URL_            VARCHAR2(200),
                               DESCP_               VARCHAR2(500),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_INST_TYPE PRIMARY KEY (TYPE_ID_)
);

COMMENT ON TABLE OS_INST_TYPE IS
'机构类型';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_ID_ IS
'类型';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_CODE_ IS
'类型编码';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_NAME_ IS
'类型名称';

COMMENT ON COLUMN OS_INST_TYPE.ENABLED_ IS
'是否启用';

COMMENT ON COLUMN OS_INST_TYPE.IS_DEFAULT_ IS
'是否系统缺省';

COMMENT ON COLUMN OS_INST_TYPE.HOME_URL_ IS
'首页URL';

COMMENT ON COLUMN OS_INST_TYPE.DESCP_ IS
'描述';

COMMENT ON COLUMN OS_INST_TYPE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_INST_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_INST_TYPE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_INST_TYPE_MENU                                     */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE_MENU  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    INST_TYPE_ID_        VARCHAR2(64),
                                    APP_ID_              VARCHAR2(64),
                                    MENU_ID_             VARCHAR2(64),
                                    NAME_                VARCHAR2(60)                    NOT NULL,
                                    ICON_PC_             VARCHAR2(100),
                                    ICON_PIC_            VARCHAR2(100),
                                    ICON_APP_            VARCHAR2(50),
                                    PARENT_ID_           VARCHAR2(64)                    NOT NULL,
                                    PATH_                VARCHAR2(256),
                                    SN_                  INTEGER,
                                    SHOW_TYPE_           VARCHAR2(20),
                                    MENU_KEY_            VARCHAR2(50),
                                    MENU_TYPE_           VARCHAR2(20),
                                    COMPONENT_           VARCHAR2(255),
                                    SETTING_TYPE_        VARCHAR2(20),
                                    BO_LIST_KEY_         VARCHAR2(64),
                                    URL_                 VARCHAR2(128),
                                    METHOD_              VARCHAR2(20),
                                    PARAMS_              VARCHAR2(1000),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_INST_TYPE_MENU PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_INST_TYPE_MENU IS
'机构类型授权菜单';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ID_ IS
'主键';

COMMENT ON COLUMN OS_INST_TYPE_MENU.INST_TYPE_ID_ IS
'机构类型ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.NAME_ IS
'菜单名称';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_PC_ IS
'PC图标样式';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_PIC_ IS
'图标';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_APP_ IS
'APP图标样式';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PARENT_ID_ IS
'上级父ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PATH_ IS
'路径';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SN_ IS
'序号';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SHOW_TYPE_ IS
'访问方式';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_KEY_ IS
'菜单唯一标识';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_TYPE_ IS
'菜单类型';

COMMENT ON COLUMN OS_INST_TYPE_MENU.COMPONENT_ IS
'展示组件';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SETTING_TYPE_ IS
'配置类型(custom,iframe)';

COMMENT ON COLUMN OS_INST_TYPE_MENU.BO_LIST_KEY_ IS
'单据列表KEY';

COMMENT ON COLUMN OS_INST_TYPE_MENU.URL_ IS
'接口地址';

COMMENT ON COLUMN OS_INST_TYPE_MENU.METHOD_ IS
'接口方法';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PARAMS_ IS
'菜单参数';

COMMENT ON COLUMN OS_INST_TYPE_MENU.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_INST_TYPE_MENU.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_INST_USERS                                         */
/*==============================================================*/
CREATE TABLE OS_INST_USERS  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                USER_ID_             VARCHAR2(64),
                                IS_ADMIN_            INTEGER,
                                USER_TYPE_           VARCHAR2(64),
                                APPLY_NOTE_          VARCHAR2(255),
                                APPLY_STATUS_        VARCHAR2(40),
                                CREATE_TYPE_         VARCHAR2(40),
                                APPROVE_USER_        VARCHAR2(64),
                                STATUS_              VARCHAR2(40),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_INST_USERS PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_INST_USERS IS
'租户用户关联表';

COMMENT ON COLUMN OS_INST_USERS.ID_ IS
'主键';

COMMENT ON COLUMN OS_INST_USERS.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN OS_INST_USERS.IS_ADMIN_ IS
'管理员';

COMMENT ON COLUMN OS_INST_USERS.USER_TYPE_ IS
'用户类型';

COMMENT ON COLUMN OS_INST_USERS.APPLY_NOTE_ IS
'申请理由';

COMMENT ON COLUMN OS_INST_USERS.APPLY_STATUS_ IS
'申请状态';

COMMENT ON COLUMN OS_INST_USERS.CREATE_TYPE_ IS
'创建类型';

COMMENT ON COLUMN OS_INST_USERS.APPROVE_USER_ IS
'审批用户';

COMMENT ON COLUMN OS_INST_USERS.STATUS_ IS
'状态';

COMMENT ON COLUMN OS_INST_USERS.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_INST_USERS.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_INST_USERS.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_INST_USERS.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_INST_USERS.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_INST_USERS.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Index: IDX_OS_INST_USERS_USERID                              */
/*==============================================================*/
CREATE INDEX IDX_OS_INST_USERS_USERID ON OS_INST_USERS (
                                                        USER_ID_ ASC
    );

/*==============================================================*/
/* Table: OS_PROPERTIES_DEF                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_DEF  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    GROUP_ID_            VARCHAR2(64),
                                    DIM_ID_              VARCHAR2(64),
                                    NAME_                VARCHAR2(64),
                                    DATA_TYPE_           VARCHAR2(40),
                                    CTLTYPE_             VARCHAR2(20),
                                    EXT_JSON_            VARCHAR2(2000),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_PROPERTIES_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_DEF IS
'自定义属性配置';

COMMENT ON COLUMN OS_PROPERTIES_DEF.ID_ IS
'主键';

COMMENT ON COLUMN OS_PROPERTIES_DEF.GROUP_ID_ IS
'分组ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.DIM_ID_ IS
'维度ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.NAME_ IS
'参数名称';

COMMENT ON COLUMN OS_PROPERTIES_DEF.DATA_TYPE_ IS
'数据类型
number,
varchar
date';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CTLTYPE_ IS
'控件类型';

COMMENT ON COLUMN OS_PROPERTIES_DEF.EXT_JSON_ IS
'扩展属性';

COMMENT ON COLUMN OS_PROPERTIES_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_PROPERTIES_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_PROPERTIES_GROUP                                   */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_GROUP  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      NAME_                VARCHAR2(64),
                                      ENABLED_             VARCHAR2(40),
                                      DIM_ID_              VARCHAR2(64),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE,
                                      CONSTRAINT PK_OS_PROPERTIES_GROUP PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_GROUP IS
'自定义属性分组';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.ID_ IS
'主键';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.NAME_ IS
'分组名称';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.ENABLED_ IS
'是否允许';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.DIM_ID_ IS
'维度ID
用户的维度为-1';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_PROPERTIES_VAL                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_VAL  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    DIM_ID_              VARCHAR2(64),
                                    GROUP_ID_            VARCHAR2(64),
                                    OWNER_ID_            VARCHAR2(64),
                                    PROPERY_ID_          VARCHAR2(64),
                                    NUM_VAL_             NUMBER(14,2),
                                    TXT_VAL              VARCHAR2(200),
                                    DATE_VAL             DATE,
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_PROPERTIES_VAL PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_VAL IS
'扩展属性值';

COMMENT ON COLUMN OS_PROPERTIES_VAL.ID_ IS
'主键';

COMMENT ON COLUMN OS_PROPERTIES_VAL.DIM_ID_ IS
'维度ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.GROUP_ID_ IS
'分组ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.OWNER_ID_ IS
'所有者';

COMMENT ON COLUMN OS_PROPERTIES_VAL.PROPERY_ID_ IS
'属性ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.NUM_VAL_ IS
'数据值';

COMMENT ON COLUMN OS_PROPERTIES_VAL.TXT_VAL IS
'文本值';

COMMENT ON COLUMN OS_PROPERTIES_VAL.DATE_VAL IS
'日期值';

COMMENT ON COLUMN OS_PROPERTIES_VAL.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_PROPERTIES_VAL.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_RANK_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_RANK_TYPE  (
                               RK_ID_               VARCHAR2(64)                    NOT NULL,
                               DIM_ID_              VARCHAR2(64),
                               NAME_                VARCHAR2(255)                   NOT NULL,
                               KEY_                 VARCHAR2(64)                    NOT NULL,
                               LEVEL_               INTEGER                         NOT NULL,
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_RANK_TYPE PRIMARY KEY (RK_ID_)
);

COMMENT ON TABLE OS_RANK_TYPE IS
'用户组等级分类，放置组织的等级分类定义
如集团，分公司，部门等级别';

COMMENT ON COLUMN OS_RANK_TYPE.RK_ID_ IS
'等级ID';

COMMENT ON COLUMN OS_RANK_TYPE.DIM_ID_ IS
'维度ID';

COMMENT ON COLUMN OS_RANK_TYPE.NAME_ IS
'名称';

COMMENT ON COLUMN OS_RANK_TYPE.KEY_ IS
'分类业务键';

COMMENT ON COLUMN OS_RANK_TYPE.LEVEL_ IS
'级别数值';

COMMENT ON COLUMN OS_RANK_TYPE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_RANK_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_RANK_TYPE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_REL_INST                                           */
/*==============================================================*/
CREATE TABLE OS_REL_INST  (
                              INST_ID_             VARCHAR2(64)                    NOT NULL,
                              REL_TYPE_ID_         VARCHAR2(64),
                              REL_TYPE_KEY_        VARCHAR2(64),
                              PARTY1_              VARCHAR2(64)                    NOT NULL,
                              PARTY2_              VARCHAR2(64)                    NOT NULL,
                              DIM1_                VARCHAR2(64),
                              DIM2_                VARCHAR2(64),
                              IS_MAIN_             VARCHAR2(20)                   DEFAULT 'NO' NOT NULL,
                              STATUS_              VARCHAR2(40)                    NOT NULL,
                              ALIAS_               VARCHAR2(80),
                              REL_TYPE_            VARCHAR2(64),
                              PATH_                VARCHAR2(200),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_REL_INST PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE OS_REL_INST IS
'关系实例';

COMMENT ON COLUMN OS_REL_INST.INST_ID_ IS
'用户组关系ID';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_ID_ IS
'关系类型ID';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_KEY_ IS
'关系类型KEY_
';

COMMENT ON COLUMN OS_REL_INST.PARTY1_ IS
'当前方ID';

COMMENT ON COLUMN OS_REL_INST.PARTY2_ IS
'关联方ID';

COMMENT ON COLUMN OS_REL_INST.DIM1_ IS
'当前方维度';

COMMENT ON COLUMN OS_REL_INST.DIM2_ IS
'关联方维度ID';

COMMENT ON COLUMN OS_REL_INST.IS_MAIN_ IS
'是否为主关系';

COMMENT ON COLUMN OS_REL_INST.STATUS_ IS
'状态
ENABLED
DISABLED';

COMMENT ON COLUMN OS_REL_INST.ALIAS_ IS
'别名';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_ IS
'关系类型';

COMMENT ON COLUMN OS_REL_INST.PATH_ IS
'路径';

COMMENT ON COLUMN OS_REL_INST.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_REL_INST.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_REL_INST.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_REL_INST.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_REL_INST.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_REL_INST.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY2                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY2 ON OS_REL_INST (
                                                    PARTY2_ ASC
    );

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY1                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY1 ON OS_REL_INST (
                                                    PARTY1_ ASC
    );

/*==============================================================*/
/* Table: OS_REL_TYPE                                           */
/*==============================================================*/
CREATE TABLE OS_REL_TYPE  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(64)                    NOT NULL,
                              KEY_                 VARCHAR2(64)                    NOT NULL,
                              REL_TYPE_            VARCHAR2(40)                    NOT NULL,
                              CONST_TYPE_          VARCHAR2(40),
                              PARTY1_              VARCHAR2(128)                   NOT NULL,
                              PARTY2_              VARCHAR2(128)                   NOT NULL,
                              DIM_ID1_             VARCHAR2(64),
                              DIM_ID2_             VARCHAR2(64),
                              LEVEL_               INTEGER,
                              STATUS_              VARCHAR2(40)                    NOT NULL,
                              IS_SYSTEM_           VARCHAR2(10)                    NOT NULL,
                              IS_DEFAULT_          VARCHAR2(10)                    NOT NULL,
                              IS_TWO_WAY_          VARCHAR2(10),
                              MEMO_                VARCHAR2(255),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_REL_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_REL_TYPE IS
'关系类型定义';

COMMENT ON COLUMN OS_REL_TYPE.ID_ IS
'关系类型ID';

COMMENT ON COLUMN OS_REL_TYPE.NAME_ IS
'关系名';

COMMENT ON COLUMN OS_REL_TYPE.KEY_ IS
'关系业务主键';

COMMENT ON COLUMN OS_REL_TYPE.REL_TYPE_ IS
'关系类型。用户关系=USER-USER；用户组关系=GROUP-GROUP；用户与组关系=USER-GROUP；组与用户关系=GROUP-USER';

COMMENT ON COLUMN OS_REL_TYPE.CONST_TYPE_ IS
'关系约束类型。1对1=one2one；1对多=one2many；多对1=many2one；多对多=many2many';

COMMENT ON COLUMN OS_REL_TYPE.PARTY1_ IS
'关系当前方名称';

COMMENT ON COLUMN OS_REL_TYPE.PARTY2_ IS
'关系关联方名称';

COMMENT ON COLUMN OS_REL_TYPE.DIM_ID1_ IS
'当前方维度ID（仅对用户组关系）';

COMMENT ON COLUMN OS_REL_TYPE.DIM_ID2_ IS
'关联方维度ID（用户关系忽略此值）';

COMMENT ON COLUMN OS_REL_TYPE.STATUS_ IS
'状态。actived 已激活；locked 锁定；deleted 已删除';

COMMENT ON COLUMN OS_REL_TYPE.IS_SYSTEM_ IS
'是否系统预设';

COMMENT ON COLUMN OS_REL_TYPE.IS_DEFAULT_ IS
'是否默认';

COMMENT ON COLUMN OS_REL_TYPE.IS_TWO_WAY_ IS
'是否双向关系';

COMMENT ON COLUMN OS_REL_TYPE.MEMO_ IS
'关系备注';

COMMENT ON COLUMN OS_REL_TYPE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_REL_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_REL_TYPE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_USER                                               */
/*==============================================================*/
CREATE TABLE OS_USER  (
                          USER_ID_             VARCHAR2(64)                    NOT NULL,
                          FULLNAME_            VARCHAR2(64)                    NOT NULL,
                          USER_NO_             VARCHAR2(64)                    NOT NULL,
                          PWD_                 VARCHAR2(64),
                          ENTRY_TIME_          DATE,
                          QUIT_TIME_           DATE,
                          USER_TYPE_           VARCHAR2(20),
                          FROM_                VARCHAR2(20),
                          BIRTHDAY_            DATE,
                          SEX_                 VARCHAR2(10),
                          MOBILE_              VARCHAR2(32),
                          EMAIL_               VARCHAR2(100),
                          ADDRESS_             VARCHAR2(255),
                          URGENT_              VARCHAR2(64),
                          SYNC_WX_             INTEGER,
                          URGENT_MOBILE_       VARCHAR2(20),
                          QQ_                  VARCHAR2(20),
                          PHOTO_               VARCHAR2(255),
                          ENABLED_             VARCHAR2(20),
                          DD_ID_               VARCHAR2(64),
                          OPEN_ID_             VARCHAR2(64),
                          WX_OPEN_ID_          VARCHAR2(64),
                          STATUS_              VARCHAR2(50),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          CONSTRAINT PK_OS_USER PRIMARY KEY (USER_ID_)
);

COMMENT ON TABLE OS_USER IS
'用户信息表';

COMMENT ON COLUMN OS_USER.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN OS_USER.FULLNAME_ IS
'姓名';

COMMENT ON COLUMN OS_USER.USER_NO_ IS
'用户编号';

COMMENT ON COLUMN OS_USER.PWD_ IS
'密码';

COMMENT ON COLUMN OS_USER.ENTRY_TIME_ IS
'入职时间';

COMMENT ON COLUMN OS_USER.QUIT_TIME_ IS
'离职时间';

COMMENT ON COLUMN OS_USER.USER_TYPE_ IS
'用户类型';

COMMENT ON COLUMN OS_USER.FROM_ IS
'来源
system,系统添加,import,导入,grade,分级添加的';

COMMENT ON COLUMN OS_USER.BIRTHDAY_ IS
'出生日期';

COMMENT ON COLUMN OS_USER.SEX_ IS
'姓别';

COMMENT ON COLUMN OS_USER.MOBILE_ IS
'手机';

COMMENT ON COLUMN OS_USER.EMAIL_ IS
'邮件';

COMMENT ON COLUMN OS_USER.ADDRESS_ IS
'地址';

COMMENT ON COLUMN OS_USER.URGENT_ IS
'紧急联系人';

COMMENT ON COLUMN OS_USER.SYNC_WX_ IS
'是否同步到微信';

COMMENT ON COLUMN OS_USER.URGENT_MOBILE_ IS
'紧急联系人手机';

COMMENT ON COLUMN OS_USER.QQ_ IS
'QQ号';

COMMENT ON COLUMN OS_USER.PHOTO_ IS
'照片';

COMMENT ON COLUMN OS_USER.ENABLED_ IS
'是否允许';

COMMENT ON COLUMN OS_USER.DD_ID_ IS
'钉钉ID';

COMMENT ON COLUMN OS_USER.WX_OPEN_ID_ IS
'企业微信OPENID';

COMMENT ON COLUMN OS_USER.STATUS_ IS
'用户在当前机构的状态
IN_JOB=在职
OUT_JOB=离职';

COMMENT ON COLUMN OS_USER.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_USER.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_USER.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_USER.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_USER.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_USER.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_USER_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_USER_TYPE  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               CODE_                VARCHAR2(50)                    NOT NULL,
                               NAME_                VARCHAR2(50)                    NOT NULL,
                               DESCP_               VARCHAR2(500),
                               GROUP_ID_            VARCHAR2(64),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_USER_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_USER_TYPE IS
'用户类型';

COMMENT ON COLUMN OS_USER_TYPE.ID_ IS
'主键';

COMMENT ON COLUMN OS_USER_TYPE.CODE_ IS
'编码';

COMMENT ON COLUMN OS_USER_TYPE.NAME_ IS
'名称';

COMMENT ON COLUMN OS_USER_TYPE.DESCP_ IS
'描述';

COMMENT ON COLUMN OS_USER_TYPE.GROUP_ID_ IS
'关联角色ID';

COMMENT ON COLUMN OS_USER_TYPE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_USER_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN OS_USER_TYPE.UPDATE_TIME_ IS
'更新时间';

/*==============================================================*/
/* Table: OS_WX_ENT_AGENT                                       */
/*==============================================================*/
CREATE TABLE OS_WX_ENT_AGENT  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  NAME_                VARCHAR2(100),
                                  DESCRIPTION_         VARCHAR2(200),
                                  DOMAIN_              VARCHAR2(64),
                                  HOME_URL_            VARCHAR2(400),
                                  CORP_ID_             VARCHAR2(64),
                                  AGENT_ID_            VARCHAR2(64),
                                  SECRET_              VARCHAR2(64),
                                  DEFAULT_AGENT_       INTEGER,
                                  UPDATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  TENANT_ID_           VARCHAR2(64),
                                  CREATE_BY_           VARCHAR2(64),
                                  TOKEN_               VARCHAR2(64)                   DEFAULT NULL,
                                  AESKEY_              VARCHAR2(64)                   DEFAULT NULL,
                                  CONSTRAINT PK_OS_WX_ENT_AGENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_WX_ENT_AGENT IS
'企业微信应用表';

COMMENT ON COLUMN OS_WX_ENT_AGENT.ID_ IS
'主键';

COMMENT ON COLUMN OS_WX_ENT_AGENT.NAME_ IS
'名称';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DOMAIN_ IS
'信任域名';

COMMENT ON COLUMN OS_WX_ENT_AGENT.HOME_URL_ IS
'主页地址';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CORP_ID_ IS
'企业ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.AGENT_ID_ IS
'应用ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.SECRET_ IS
'密钥';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DEFAULT_AGENT_ IS
'是否默认';

COMMENT ON COLUMN OS_WX_ENT_AGENT.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN OS_WX_ENT_AGENT.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN OS_WX_ENT_AGENT.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN OS_WX_ENT_AGENT.TOKEN_ IS
'发送消息TOKEN';

COMMENT ON COLUMN OS_WX_ENT_AGENT.AESKEY_ IS
'发送消息AESKEY';

COMMIT ;