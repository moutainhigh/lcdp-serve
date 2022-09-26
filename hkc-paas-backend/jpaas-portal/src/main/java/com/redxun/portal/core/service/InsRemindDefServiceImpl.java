package com.redxun.portal.core.service;

import com.redxun.api.org.IOrgService;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.portal.core.entity.InsRemindDef;
import com.redxun.portal.core.mapper.InsRemindDefMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* [消息提醒]业务服务类
*/
@Service
public class InsRemindDefServiceImpl extends SuperServiceImpl<InsRemindDefMapper, InsRemindDef> implements BaseService<InsRemindDef> {
    @Autowired
    IOrgService orgService;

    @Resource
    private InsRemindDefMapper insRemindDefMapper;

    @Override
    public BaseDao<InsRemindDef> getRepository() {
        return insRemindDefMapper;
    }


    public List<InsRemindDef> getOwnInsColumnDef() {
        Map<String, Set<String>> profiles = orgService.getCurrentProfile();
        List<InsRemindDef> owerAll = getByOwner(profiles);
        return owerAll;
    }

    public  List<InsRemindDef> getByOwner(Map<String, Set<String>> profiles){
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("profileMap", profiles);
        return  insRemindDefMapper.getByOwner(params);
    }
}
