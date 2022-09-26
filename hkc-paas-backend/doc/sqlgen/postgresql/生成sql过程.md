## pg脚本生成过程：

1. 使用**PowerDesign** 打开 该项目下的pdm文件夹 的pdm文件。
2. 改变当前的DBMS 为PostgreSQL 9.x
3. 点击生成数据库，生成对应的DDL语句（注意要删除 生成的语句里面开头的几个包含domain的语句，pg不支持这些语句，**还要按照下面修改一些语句**）
4.  去到sqlgen文件夹，修改jdbc.properties文件，其中的templatefile为模板路径，模板在该目录下的postgresql文件夹下。（由于pg转换不了html语句，所以设置了特殊的模板），用ant执行build.xml文件，生成对应的DML语句。



由于pg脚本的不同，需要修改对应的DDL语句（格式： 表 参数 类型  ）

```
表SYS_INVOKE_SCRIPT       PARAMS_   TEXT  
表SYS_MENU                SN_       VARCHAR(64) 
表XXL_JOB_INFO            EXECUTOR_TIMEOUT     INT8                                                                                   EXECUTOR_FAIL_RETRY_COUNT INT8                    
                        TRIGGER_LAST_TIME    INT8                                  
                        TRIGGER_NEXT_TIME    INT8   
表XXL_JOB_LOG             ALARM_STATUS     INT2   NULL(主要是这个为空)  
表UREPORT_FILE           CONTENT_         bytea
```

 