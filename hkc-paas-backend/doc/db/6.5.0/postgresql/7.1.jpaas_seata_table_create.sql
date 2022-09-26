/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/9/28 10:22:06                           */
/*==============================================================*/


/*==============================================================*/
/* Table: BRANCH_TABLE                                          */
/*==============================================================*/
CREATE TABLE BRANCH_TABLE (
   BRANCH_ID            INT8                 NOT NULL,
   XID                  VARCHAR(128)         NULL,
   TRANSACTION_ID       INT8                 NULL,
   RESOURCE_GROUP_ID    VARCHAR(40)          NULL,
   RESOURCE_ID          VARCHAR(256)         NULL,
   BRANCH_TYPE          VARCHAR(8)           NULL,
   STATUS               INT8                 NULL,
   CLIENT_ID            VARCHAR(64)          NULL,
   APPLICATION_DATA     VARCHAR(2000)        NULL,
   GMT_CREATE           DATE                 NULL,
   GMT_MODIFIED         DATE                 NULL,
   CONSTRAINT PK_BRANCH_TABLE PRIMARY KEY (BRANCH_ID)
);

/*==============================================================*/
/* Index: IDX_XID                                               */
/*==============================================================*/
CREATE  INDEX IDX_XID ON BRANCH_TABLE (
XID
);

/*==============================================================*/
/* Table: GLOBAL_TABLE                                          */
/*==============================================================*/
CREATE TABLE GLOBAL_TABLE (
   XID                  VARCHAR(128)         NOT NULL,
   TRANSACTION_ID       INT8                 NULL,
   STATUS               INT8                 NULL,
   APPLICATION_ID       VARCHAR(32)          NULL,
   TRANSACTION_SERVICE_GROUP VARCHAR(40)          NULL,
   TRANSACTION_NAME     VARCHAR(128)         NULL,
   TIMEOUT              INT8                 NULL,
   BEGIN_TIME           INT8                 NULL,
   APPLICATION_DATA     VARCHAR(2000)        NULL,
   GMT_CREATE           DATE                 NULL,
   GMT_MODIFIED         DATE                 NULL,
   CONSTRAINT PK_GLOBAL_TABLE PRIMARY KEY (XID)
);

/*==============================================================*/
/* Index: IDX_GMT_MODIFIED_STATUS                               */
/*==============================================================*/
CREATE  INDEX IDX_GMT_MODIFIED_STATUS ON GLOBAL_TABLE (
GMT_MODIFIED,
STATUS
);

/*==============================================================*/
/* Index: IDX_GLOBAL_TRANSACTIONID                              */
/*==============================================================*/
CREATE  INDEX IDX_GLOBAL_TRANSACTIONID ON GLOBAL_TABLE (
TRANSACTION_ID
);

/*==============================================================*/
/* Table: LOCK_TABLE                                            */
/*==============================================================*/
CREATE TABLE LOCK_TABLE (
   ROW_KEY              VARCHAR(128)         NOT NULL,
   XID                  VARCHAR(96)          NULL,
   TRANSACTION_ID       INT8                 NULL,
   BRANCH_ID            INT8                 NULL,
   RESOURCE_ID          VARCHAR(256)         NULL,
   TABLE_NAME           VARCHAR(32)          NULL,
   PK                   VARCHAR(36)          NULL,
   status               VARCHAR(36)          NULL,
   GMT_CREATE           DATE                 NULL,
   GMT_MODIFIED         DATE                 NULL,
   CONSTRAINT PK_LOCK_TABLE PRIMARY KEY (ROW_KEY)
);

/*==============================================================*/
/* Index: IDX_BRANCH_ID                                         */
/*==============================================================*/
CREATE  INDEX IDX_BRANCH_ID ON LOCK_TABLE (
BRANCH_ID
);

