use jpaas_ureport;
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

CREATE TABLE UREPORT_FILE(
  ID_ varchar(255)  NOT NULL COMMENT '主键',
  NAME_ varchar(255)  NULL DEFAULT NULL COMMENT '报表名称',
  CONTENT_ longblob NULL COMMENT '报表内容',
  CATEGORY_ID_ varchar(64)  NULL DEFAULT NULL COMMENT '分类ID',
  TENANT_ID_ varchar(64)  NULL DEFAULT NULL COMMENT '租用用户Id',
  CREATE_DEP_ID_ varchar(64)  NULL DEFAULT NULL COMMENT '创建部门ID',
  CREATE_BY_ varchar(64)  NULL DEFAULT NULL COMMENT '创建人ID',
  CREATE_TIME_ datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  UPDATE_BY_ varchar(64)  NULL DEFAULT NULL COMMENT '更新人ID',
  UPDATE_TIME_ datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  APP_ID_              VARCHAR(64) COMMENT '应用ID',
  PRIMARY KEY (ID_) USING BTREE
);
ALTER TABLE UREPORT_FILE COMMENT 'ureport报表';








