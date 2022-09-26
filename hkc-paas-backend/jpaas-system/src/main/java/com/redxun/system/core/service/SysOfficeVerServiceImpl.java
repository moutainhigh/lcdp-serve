package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysOfficeVer;
import com.redxun.system.core.mapper.SysOfficeVerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [SYS_OFFICE_VER]业务服务类
*/
@Service
public class SysOfficeVerServiceImpl extends SuperServiceImpl<SysOfficeVerMapper, SysOfficeVer> implements BaseService<SysOfficeVer> {

    @Resource
    private SysOfficeVerMapper sysOfficeVerMapper;

    @Override
    public BaseDao<SysOfficeVer> getRepository() {
        return sysOfficeVerMapper;
    }

    /**
     * 根据officeId 获取文件ID。
     * @param officeId
     * @return
     */
    public String getFileIdByOfficeId(String officeId){
        List<SysOfficeVer> list=sysOfficeVerMapper.getByOfficeId(officeId);
        if(list.size()>0){
            return list.get(0).getFileId();
        }
        return "";

    }
}
