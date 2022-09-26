use jpaas_ureport;

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
/* Created on:     2022-7-22 16:21:06                           */
/*==============================================================*/


/*==============================================================*/
/* Table: UREPORT_FILE                                          */
/*==============================================================*/
CREATE TABLE UREPORT_FILE
(
   ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
   NAME_                VARCHAR(64),
   CONTENT_             LONGTEXT,
   CATEGORY_ID_         VARCHAR(64),
   DELETED_             INT DEFAULT 0 COMMENT '逻辑删除',
   COMPANY_ID_          VARCHAR(30) COMMENT '公司ID',
   TENANT_ID_           VARCHAR(64),
   CREATE_DEP_ID_       VARCHAR(64),
   CREATE_BY_           VARCHAR(64),
   CREATE_TIME_         DATETIME,
   UPDATE_BY_           VARCHAR(64),
   UPDATE_TIME_         DATETIME,
   APP_ID_              VARCHAR(64) COMMENT '应用ID',
   PRIMARY KEY (ID_)
);

ALTER TABLE UREPORT_FILE COMMENT 'ureport报表';

