package com.redxun.system.mq;


import com.redxun.common.tool.IdGenerator;
import com.redxun.log.model.Audit;
import com.redxun.log.mq.LogInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogConsumer {

    @Autowired
    JdbcTemplate jdbcTemplate;


    /**
     *处理审核日志。
     */
    @StreamListener(LogInput.INPUT)
    public void handLog(Audit audit) {

        String sql="insert into sys_log (ID_,APP_NAME_,MODULE_,SUB_MODULE_,CLASS_NAME_,METHOD_NAME_," +
                "ACTION_,PK_VALUE_,IP_,OPERATION_,DETAIL_,USER_NAME_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,DURATION_,BUS_TYPE_,IS_RESUME_)" +
                " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String id= IdGenerator.getIdStr();
        jdbcTemplate.update(sql,id,audit.getAppName(),audit.getModule(),audit.getSubModule(),
                audit.getClassName(),audit.getMethodName(),audit.getAction(),audit.getPkValue(),audit.getIp(),
                audit.getOperation(),audit.getDetail(),audit.getUserName(),audit.getTenantId(),audit.getCreateDepId(),
                audit.getCreateBy(),audit.getCreateTime(),audit.getDuration(),audit.getBusType(),audit.getIsResume());


    }
}
