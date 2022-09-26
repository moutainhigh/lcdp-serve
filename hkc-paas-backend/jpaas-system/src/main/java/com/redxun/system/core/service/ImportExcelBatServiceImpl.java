
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.ImportExcelBat;
import com.redxun.system.core.mapper.ImportExcelBatMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [sys_excel_batmanage]业务服务类
*/
@Service
public class ImportExcelBatServiceImpl extends SuperServiceImpl<ImportExcelBatMapper, ImportExcelBat> implements BaseService<ImportExcelBat> {

    @Resource
    private ImportExcelBatMapper importExcelBatMapper;

    @Override
    public BaseDao<ImportExcelBat> getRepository() {
        return importExcelBatMapper;
    }


    /**
     * 获取批次
     * @param templateId
     * @return
     */
    public int getBatId(String templateId){
        Integer batId = importExcelBatMapper.getBatId(templateId);
        if(batId==null){
            return 1;
        }
        return  batId+1;

    }
}
