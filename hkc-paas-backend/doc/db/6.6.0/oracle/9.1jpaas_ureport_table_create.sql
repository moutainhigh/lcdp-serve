/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2022-7-25 17:03:54                           */
/*==============================================================*/

CREATE TABLE undo_log
(
    id            NUMBER(19)    NOT NULL,
    branch_id     NUMBER(19)    NOT NULL,
    xid           VARCHAR2(128) NOT NULL,
    context_       VARCHAR2(128) NOT NULL,
    rollback_info BLOB          NOT NULL,
    log_status    NUMBER(10)    NOT NULL,
    log_created   TIMESTAMP(0)  NOT NULL,
    log_modified  TIMESTAMP(0)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
);
COMMENT ON TABLE undo_log IS 'AT transaction mode undo table';
CREATE SEQUENCE UNDO_LOG_SEQ START WITH 1 INCREMENT BY 1;
/*==============================================================*/
/* Table: UREPORT_FILE                                          */
/*==============================================================*/
CREATE TABLE UREPORT_FILE 
(
   ID_                  VARCHAR2(64)         NOT NULL,
   NAME_                VARCHAR2(64),
   CONTENT_             CLOB,
   CATEGORY_ID_         VARCHAR2(64),
   DELETED_             INTEGER              DEFAULT 0,
   COMPANY_ID_          VARCHAR2(30),
   TENANT_ID_           VARCHAR2(64),
   CREATE_DEP_ID_       VARCHAR2(64),
   CREATE_BY_           VARCHAR2(64),
   CREATE_TIME_         DATE,
   UPDATE_BY_           VARCHAR2(64),
   UPDATE_TIME_         DATE,
   APP_ID_              VARCHAR2(64),
   CONSTRAINT PK_UREPORT_FILE PRIMARY KEY (ID_)
);

COMMENT ON TABLE UREPORT_FILE IS
'ureport报表';

COMMENT ON COLUMN UREPORT_FILE.ID_ IS
'主键';

COMMENT ON COLUMN UREPORT_FILE.DELETED_ IS
'逻辑删除';

COMMENT ON COLUMN UREPORT_FILE.COMPANY_ID_ IS
'公司ID';

COMMENT ON COLUMN UREPORT_FILE.APP_ID_ IS
'应用ID';

