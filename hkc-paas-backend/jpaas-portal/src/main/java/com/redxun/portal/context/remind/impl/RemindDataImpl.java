package com.redxun.portal.context.remind.impl;

import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.portal.context.remind.RemindDataService;
import com.redxun.portal.core.entity.InsRemindDef;
import com.redxun.portal.core.service.InsRemindDefServiceImpl;
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
 * 提醒数据服务
 */
@Service
@Slf4j
public class RemindDataImpl implements RemindDataService {
    @Autowired
    InsRemindDefServiceImpl insRemindDefService;
    @Resource
    private GroovyEngine groovyEngine;

    @Resource
    private CommonDao commonDao;

    @Override
    public List<InsRemindDef> getData(){
        List<InsRemindDef> list = insRemindDefService.getOwnInsColumnDef();
        List<InsRemindDef> returnList = new ArrayList<>();
        try {
            for (InsRemindDef remind:list) {
                if(!InsRemindDef.TYPE_ENABLED.equals(remind.getEnabled())){
                    continue;
                }
                InsRemindDef remindDef=new InsRemindDef();

                int count = getCount(remind);
                if(count==0){
                    continue;
                }
                remindDef.setCount(count);
                String description = remind.getDescription();
                String countStr = "<i class=\"remindCalss\">"+String.valueOf(count)+"</i>";
                description=description.replace("[count]",countStr);
                remindDef.setDescription(description);

                remindDef.setUrl(remind.getUrl());
                remindDef.setIcon(remind.getIcon());
                returnList.add(remindDef);
            }
        } catch (Exception e) {
            log.error("----RemindDataImpl.getData() is error -----:" + e.getMessage());
        }
        return returnList;
    }

    private int getCount(InsRemindDef remind) {
        int count =0;
        if (InsRemindDef.SQL_TYPE.equals(remind.getType())) {
            count = getCountBySql(remind.getSetting(), remind.getDsAlias());
        }
        //支持查找多个数据
        else  {
            Map<String, Object> params = new HashMap<String, Object>();
            String function = remind.getSetting();
            count = (Integer) groovyEngine.executeScripts(function, params);
        }
        return count;
    }

    /**
     * 执行消息的sql脚本
     * @param handleSqlDiy
     * @param dsAlias
     * @return
     */
    public int getCountBySql(String handleSqlDiy, String dsAlias) {
        handleSqlDiy = SysUtil.replaceConstant(handleSqlDiy);
        //获取SQL
        String sql = (String) groovyEngine.executeScripts(handleSqlDiy, new HashMap<>());
        if (StringUtils.isEmpty(dsAlias)) {
            dsAlias = DataSourceUtil.LOCAL;
        }
        Long result = (Long) commonDao.queryOne(dsAlias, sql);
        return result.intValue();
    }

}
