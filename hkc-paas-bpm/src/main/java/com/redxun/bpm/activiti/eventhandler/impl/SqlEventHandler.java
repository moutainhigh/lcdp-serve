package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.db.CommonDao;
import com.redxun.util.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * SQL执行处理器
 */
@Component
public class SqlEventHandler implements IEventHandler {
    @Resource
    CommonDao commonDao;
    @Resource
    FtlEngine freemarkEngine;

    @Override
    public EventHanderType getType() {
        return new EventHanderType("sql","SQL脚本");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }

        String sql = config.getString("sql");
        String dsAlias=config.getString("dsAlias");
        if(StringUtils.isEmpty(sql)) {
            return;
        }
        Map<String,Object> vars=message.getVars();
        //获取上下文变量数据。
        Map<String,Object> contextData= ActivitiUtil.getConextData(vars);
        sql = SysUtil.replaceConstant(sql);
        try {
            sql = "<#setting number_format=\"#\">"+sql;
            sql = freemarkEngine.parseByStringTemplate(contextData,sql);

            String[] arySql= sql.split(";");
            for(String strSql:arySql){
                commonDao.execute(dsAlias,strSql,vars);
            }
        }
        catch (Exception e1) {
            MessageUtil.triggerException("SQL事件执行出错!","执行SQL语句出错,SQL:" + sql +"," + ExceptionUtil.getExceptionMessage(e1));
        }
    }


}
