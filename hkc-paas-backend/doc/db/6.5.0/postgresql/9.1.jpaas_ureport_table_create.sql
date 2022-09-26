/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/9/28 10:21:27                           */
/*==============================================================*/


/*==============================================================*/
/* Table: UREPORT_FILE                                          */
/*==============================================================*/

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

CREATE TABLE UREPORT_FILE (
   ID_                  VARCHAR(64)          NOT NULL,
   NAME_                VARCHAR(64)          NULL,
   CONTENT_             bytea                 NULL,
   CATEGORY_ID_         VARCHAR(64)          NULL,
   TENANT_ID_           VARCHAR(64)          NULL,
   CREATE_DEP_ID_       VARCHAR(64)          NULL,
   CREATE_BY_           VARCHAR(64)          NULL,
   CREATE_TIME_         DATE                 NULL,
   UPDATE_BY_           VARCHAR(64)          NULL,
   UPDATE_TIME_         DATE                 NULL,
   APP_ID_              VARCHAR(64)          NULL,
   CONSTRAINT PK_UREPORT_FILE PRIMARY KEY (ID_)
);

COMMENT ON TABLE UREPORT_FILE IS
'ureport报表';

COMMENT ON COLUMN UREPORT_FILE.ID_ IS
'主键';

COMMENT ON COLUMN UREPORT_FILE.APP_ID_ IS
'应用ID';

