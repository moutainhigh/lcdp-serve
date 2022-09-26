package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmOpinionLib;
import com.redxun.bpm.core.mapper.BpmOpinionLibMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [意见收藏表]业务服务类
*/
@Service
public class BpmOpinionLibServiceImpl extends SuperServiceImpl<BpmOpinionLibMapper, BpmOpinionLib> implements BaseService<BpmOpinionLib> {

    @Resource
    private BpmOpinionLibMapper bpmOpinionLibMapper;

    @Override
    public BaseDao<BpmOpinionLib> getRepository() {
        return bpmOpinionLibMapper;
    }

    /**
     * 获得这个用户的所有收藏审批意见
     * @param userId
     * @return
     */
    public List<BpmOpinionLib> getByUserId(String userId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("USER_ID_",BpmOpinionLib.PUBLIC_OPINION,userId);
        queryWrapper.orderByDesc("CREATE_TIME_");
        List<BpmOpinionLib> opinionLibs=bpmOpinionLibMapper.selectList(queryWrapper);
        return opinionLibs;
    }

    /**
     * 判断是否已经收藏过了
     * @param userId
     * @param opText
     * @return
     */
    public boolean isOpinionSaved(String userId,String opText){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("USER_ID_",BpmOpinionLib.PUBLIC_OPINION);
        queryWrapper.eq("OP_TEXT_",opText);
        boolean publicBl = bpmOpinionLibMapper.selectCount(queryWrapper)>0;
        queryWrapper=new QueryWrapper();
        queryWrapper.eq("USER_ID_",userId);
        queryWrapper.eq("OP_TEXT_",opText);
        boolean userBl = bpmOpinionLibMapper.selectCount(queryWrapper)>0;
        return publicBl||userBl;
    }
}
