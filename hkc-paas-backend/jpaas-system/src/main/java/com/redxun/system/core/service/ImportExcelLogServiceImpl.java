
package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.system.core.entity.ImportExcelLog;
import com.redxun.system.core.mapper.ImportExcelLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [Excel导入日志]业务服务类
*/
@Service
public class ImportExcelLogServiceImpl extends SuperServiceImpl<ImportExcelLogMapper, ImportExcelLog> implements BaseService<ImportExcelLog> {

    @Resource
    private ImportExcelLogMapper importExcelLogMapper;

    @Override
    public BaseDao<ImportExcelLog> getRepository() {
        return importExcelLogMapper;
    }

    public void handleError(String errorMsg, String Id){

        ImportExcelLog importExcelLog = new ImportExcelLog();

        importExcelLog.setId(IdGenerator.getIdStr());
        importExcelLog.setLog(errorMsg);
        importExcelLog.setTemplated(Id);
        insert(importExcelLog);

    }

    public List<ImportExcelLog> getByTemplate(String id){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TEMPLATED_",id);
        return importExcelLogMapper.selectList(queryWrapper);
    }

    public void delByTemplate(String id) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TEMPLATED_",id);
        importExcelLogMapper.delete(queryWrapper);
    }


}
