package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmCalGrant;
import com.redxun.bpm.core.mapper.BpmCalGrantMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [日历分配]业务服务类
*/
@Service
public class BpmCalGrantServiceImpl extends SuperServiceImpl<BpmCalGrantMapper, BpmCalGrant> implements BaseService<BpmCalGrant> {

    @Resource
    private BpmCalGrantMapper bpmCalGrantMapper;

    @Override
    public BaseDao<BpmCalGrant> getRepository() {
        return bpmCalGrantMapper;
    }
    /**
     * 通过groupId或者userId查询分配的日历
     * @param type
     * @param groupOrUserId
     * @return
     */
    public BpmCalGrant getByGroupIdOrUserId(String type,String groupOrUserId){

        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("GRANT_TYPE_",type);
        queryWrapper.eq("BELONG_WHO_ID_",groupOrUserId);
        List<BpmCalGrant> list = bpmCalGrantMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(list)){
            return  list.get(0);
        }
        return  null;
    }
}
