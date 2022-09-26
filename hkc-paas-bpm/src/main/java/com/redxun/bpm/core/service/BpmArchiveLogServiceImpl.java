package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.entity.BpmArchiveLog;
import com.redxun.bpm.core.mapper.*;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
* [流程归档日志]业务服务类
*/
@Service
public class BpmArchiveLogServiceImpl extends SuperServiceImpl<BpmArchiveLogMapper, BpmArchiveLog> implements BaseService<BpmArchiveLog> {

    @Resource
    private BpmArchiveLogMapper bpmArchiveLogMapper;

    @Resource
    private BpmInstRouterMapper bpmInstRouterMapper;

    @Resource
    private BpmInstMapper bpmInstMapper;
    @Resource
    private BpmRuPathMapper bpmRuPathMapper;
    @Resource
    private BpmInstDataMapper bpmInstDataMapper;
    @Resource
    private BpmInstCcMapper bpmInstCcMapper;
    @Resource
    private BpmInstCpMapper bpmInstCpMapper;
    @Resource
    private BpmRemindInstMapper bpmRemindInstMapper;
    @Resource
    private BpmRemindHistoryMapper bpmRemindHistoryMapper;
    @Resource
    private BpmCheckFileMapper bpmCheckFileMapper;
    @Resource
    private BpmCheckHistoryMapper bpmCheckHistoryMapper;
    @Resource
    private BpmInstLogMapper bpmInstLogMapper;
    @Resource
    private BpmInstMsgMapper bpmInstMsgMapper;

    @Override
    public BaseDao<BpmArchiveLog> getRepository() {
        return bpmArchiveLogMapper;
    }

    //根据归档时间获取是否已有归档记录
    public Integer getFinishTimes(Date archiveDate) {
        return bpmArchiveLogMapper.getFinishTimes(archiveDate);
    }
    public BpmArchiveLog insertBpmArchiveLog(Date archiveDate, String memo, IUser user) {
        //获取子表ID
        Integer tableId=getMaxTableId("1");
        BpmArchiveLog archiveLog=new BpmArchiveLog();
        archiveLog.setArchiveDate(archiveDate);
        archiveLog.setTableId(tableId);
        archiveLog.setMemo(memo);
        archiveLog.setCreateBy(user.getUserId());
        archiveLog.setCreateName(user.getFullName());
        archiveLog.setTenantId(user.getTenantId());
        insert(archiveLog);
        return archiveLog;
    }


    //归档
    public void archive(Date archiveDate, String memo, IUser user) {
        BpmArchiveLog archiveLog = insertBpmArchiveLog(archiveDate, memo, user);
        String tenantId=user.getTenantId();
        try{
            //创建物理表。
            createTable(archiveLog.getTableId());

            Integer tableId = archiveLog.getTableId();
            archiveLog.setStartTime(new Date());
            archiveLog.setCreateTime(new Date());
            //插入路由表。
            bpmInstRouterMapper.archiveByArchiveDate(archiveLog.getArchiveDate(),tableId,tenantId);
            //数据归档
            dataArchive(archiveLog.getArchiveDate(),tableId,tenantId);
            removeByArchiveDate(archiveLog.getArchiveDate(),100000);
            archiveLog.setEndTime(new Date());
            archiveLog.setStatus("1");
            bpmArchiveLogMapper.updateById(archiveLog);
        }
        catch (Exception ex){
            ex.printStackTrace();
            archiveLog.setStatus("0");
            archiveLog.setErrLog(ExceptionUtil.getExceptionMessage(ex));
            bpmArchiveLogMapper.updateById(archiveLog);
        }
    }


    private Integer getMaxTableId(String status){
        Integer rtn=bpmArchiveLogMapper.getMaxTableId(status);
        if(rtn==null) {
            return 1;
        }
        return  rtn +1;
    }

    /**
     * 创建归档表。
     * @param tableId
     */
    private void createTable(Integer tableId){
        String ds= DataSourceContextHolder.getDataSource();
        ITableOperator tableOperator= OperatorContext.getByDsAlias(ds);
        List<String> tables = BpmArchiveLog.getTables();
        for(String tableInfo:tables){
            JSONObject json=JSONObject.parseObject(tableInfo);
            String tableName=json.getString("name");
            String pk=json.getString("pk");
            //创建表
            String tbName=tableName +"_" + tableId;
            tableOperator.createTableLike(tbName,tableName,pk);
        }
    }

    /**
     * 数据归档。
     * @param date
     * @param tableId
     */
    private void dataArchive(Date date,Integer tableId,String tenantId){
        bpmInstMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmRuPathMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmInstDataMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmInstCpMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmInstCcMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmRemindInstMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmRemindHistoryMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmCheckFileMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmInstMsgMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmInstLogMapper.archiveByArchiveDate(date,tableId, tenantId);
        bpmCheckHistoryMapper.archiveByArchiveDate(date,tableId, tenantId);
    }

    /**
     * 将归档了的数据删除。
     * @param date
     * @param batSize
     */
    private void removeByArchiveDate(Date date,Integer batSize){
        DataSource dataSource= null;
        try {
            String ds= DataSourceContextHolder.getDataSource();
            dataSource = DataSourceUtil.getDataSourcesByAlias(ds);
            DataSourceProxy dsProxy=(DataSourceProxy)dataSource;
            String dbType = dsProxy.getDbType();
            int  count=0;
            //删除意见附件。
            count= bpmCheckFileMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmCheckFileMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmRuPathMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmRuPathMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmInstDataMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstDataMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmInstCpMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstCpMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmInstCcMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstCcMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmRemindInstMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmRemindInstMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmRemindHistoryMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmRemindHistoryMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmInstMsgMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstMsgMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmInstLogMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstLogMapper.removeByArchiveDate(date,batSize,dbType);
            }
            count= bpmCheckHistoryMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmCheckHistoryMapper.removeByArchiveDate(date,batSize,dbType);
            }
            //删除流程实例
            count= bpmInstMapper.removeByArchiveDate(date,batSize,dbType);
            while (count>0 && count==batSize){
                count= bpmInstMapper.removeByArchiveDate(date,batSize,dbType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取归档记录
     * @return
     */
    public List<BpmArchiveLog> getBpmArchiveLogs() {
        return bpmArchiveLogMapper.getBpmArchiveLogs();
    }
}
