/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2022-7-25 17:03:15                           */
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
/* Table: BRANCH_TABLE                                          */
/*==============================================================*/
CREATE TABLE BRANCH_TABLE 
(
   BRANCH_ID            INTEGER              NOT NULL,
   XID                  VARCHAR2(128),
   TRANSACTION_ID       INTEGER,
   RESOURCE_GROUP_ID    VARCHAR2(40),
   RESOURCE_ID          VARCHAR2(256),
   BRANCH_TYPE          VARCHAR2(8),
   STATUS               INTEGER,
   CLIENT_ID            VARCHAR2(64),
   APPLICATION_DATA     VARCHAR2(2000),
   GMT_CREATE           DATE,
   GMT_MODIFIED         DATE,
   CONSTRAINT PK_BRANCH_TABLE PRIMARY KEY (BRANCH_ID)
);

/*==============================================================*/
/* Index: IDX_XID                                               */
/*==============================================================*/
CREATE INDEX IDX_XID ON BRANCH_TABLE (
   XID ASC
);

/*==============================================================*/
/* Table: GLOBAL_TABLE                                          */
/*==============================================================*/
CREATE TABLE GLOBAL_TABLE 
(
   XID                  VARCHAR2(128)        NOT NULL,
   TRANSACTION_ID       INTEGER,
   STATUS               INTEGER,
   APPLICATION_ID       VARCHAR2(32),
   TRANSACTION_SERVICE_GROUP VARCHAR2(40),
   TRANSACTION_NAME     VARCHAR2(128),
   TIMEOUT              INTEGER,
   BEGIN_TIME           INTEGER,
   APPLICATION_DATA     VARCHAR2(2000),
   GMT_CREATE           DATE,
   GMT_MODIFIED         DATE,
   CONSTRAINT PK_GLOBAL_TABLE PRIMARY KEY (XID)
);

/*==============================================================*/
/* Index: IDX_GMT_MODIFIED_STATUS                               */
/*==============================================================*/
CREATE INDEX IDX_GMT_MODIFIED_STATUS ON GLOBAL_TABLE (
   GMT_MODIFIED ASC,
   STATUS ASC
);

/*==============================================================*/
/* Index: IDX_GLOBAL_TRANSACTIONID                              */
/*==============================================================*/
CREATE INDEX IDX_GLOBAL_TRANSACTIONID ON GLOBAL_TABLE (
   TRANSACTION_ID ASC
);

/*==============================================================*/
/* Table: LOCK_TABLE                                            */
/*==============================================================*/
CREATE TABLE LOCK_TABLE 
(
   ROW_KEY              VARCHAR2(128)        NOT NULL,
   XID                  VARCHAR2(96),
   TRANSACTION_ID       INTEGER,
   BRANCH_ID            INTEGER,
   RESOURCE_ID          VARCHAR2(256),
   TABLE_NAME           VARCHAR2(32),
   PK                   VARCHAR2(36),
   GMT_CREATE           DATE,
   GMT_MODIFIED         DATE,
   CONSTRAINT PK_LOCK_TABLE PRIMARY KEY (ROW_KEY)
);

/*==============================================================*/
/* Index: IDX_BRANCH_ID                                         */
/*==============================================================*/
CREATE INDEX IDX_BRANCH_ID ON LOCK_TABLE (
   BRANCH_ID ASC
);

