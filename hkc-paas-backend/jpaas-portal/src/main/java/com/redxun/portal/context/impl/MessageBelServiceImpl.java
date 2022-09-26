package com.redxun.portal.context.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.portal.context.IColumnDataService;
import com.redxun.portal.core.entity.InsMsgDef;
import com.redxun.portal.core.service.InsMsgDefServiceImpl;
import com.redxun.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门户消息盒子的数据获取实现类
 */
@Service
@Slf4j
public class MessageBelServiceImpl extends BaseColumnDataServiceImpl {
    //注册消息服务类
    @Autowired
    InsMsgDefServiceImpl insMsgDefService;
    //注册数据库底层操作类
    @Resource
    private CommonDao commonDao;
    @Resource
    private GroovyEngine groovyEngine;

    @Override
    public String getType() {
        return "MessageBel";
    }

    @Override
    public String getName() {
        return "消息盒子";
    }

    /**
     * 返回消息数据
     * @return
     */
    @Override
    public Object getData() {
        JSONObject settingObj = JSONObject.parseObject(this.getSetting());

        List<InsMsgDef> list=new ArrayList<>();
        try {
            //根据界面设置中获取的消息盒子获取消息定义列表
            List<InsMsgDef>  defList = insMsgDefService.getDataByBoxId(settingObj.getString("function"));
            for (InsMsgDef msg : defList) {
                InsMsgDef msgDef= handMessageDef(msg);
                list.add(msgDef);
            }
        } catch (Exception e) {
            log.error("----MessageService.getData() is error -----:" + e.getMessage());
        }
        return list;
    }

    /**
     * 处理消息定义。
     * @param msg
     */
    private InsMsgDef handMessageDef(InsMsgDef msg) {
        InsMsgDef insMsgDef=new InsMsgDef();
        insMsgDef.setColor(msg.getColor());
        insMsgDef.setContent(msg.getContent());
        insMsgDef.setUrl(msg.getUrl());
        insMsgDef.setIcon(msg.getIcon());

        if (IColumnDataService.SQL_TYPE .equals(msg.getType())) {
            Object count = getCountByType(msg, "sql");
            insMsgDef.setCount(count);
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            String function = msg.getSqlFunc();
            Integer count = (Integer) groovyEngine.executeScripts(function, params);
            if(count!=null) {
                insMsgDef.setCount(count);
            }
        }
        return insMsgDef;
    }


    /**
     * 执行消息的sql脚本
     *
     * @param msgBox
     * @param type
     * @return
     */
    private Object getCountByType(InsMsgDef msgBox, String type) {
        //处理常量
        String handleSqlDiy = msgBox.getSqlFunc();
        handleSqlDiy = SysUtil.replaceConstant(handleSqlDiy);
        //获取SQL
        String sql = (String) groovyEngine.executeScripts(handleSqlDiy,  new HashMap<>());

        String ds = msgBox.getDsAlias();
        if (StringUtils.isEmpty(ds)) {
            ds = DataSourceUtil.LOCAL;
        }
        Object  result = commonDao.queryOne(ds, sql);
        return result;
    }


}
