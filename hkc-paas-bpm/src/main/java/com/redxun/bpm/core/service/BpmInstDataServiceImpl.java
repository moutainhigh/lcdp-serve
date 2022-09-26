package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmInstData;
import com.redxun.bpm.core.mapper.BpmInstDataMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [BPM_INST_DATA]业务服务类
*/
@Service
public class BpmInstDataServiceImpl extends SuperServiceImpl<BpmInstDataMapper, BpmInstData> implements BaseService<BpmInstData> {

    @Resource
    private BpmInstDataMapper bpmInstDataMapper;

    @Override
    public BaseDao<BpmInstData> getRepository() {
        return bpmInstDataMapper;
    }

    /**
     * 根据流程实例和bo别名获取主键数据。
     * @param instId
     * @return
     */
    public List<BpmInstData> getByInstId(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        List<BpmInstData> instDataList=bpmInstDataMapper.selectList(wrapper);
        return instDataList;
    }

    /**
     * 判断数据添加过。
     * @param instId
     * @param boAlias
     * @return
     */
    public Integer getCountByInstId(String instId,String boAlias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.eq("BODEF_ALIAS_",boAlias);


        Integer rtn= bpmInstDataMapper.selectCount(wrapper);
        return rtn;
    }

    /**
     * 根据流程实例ID删除数据成功。
     * @param instId
     */
    public void removeByInstId(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);

        bpmInstDataMapper.delete(wrapper);
    }

    /**
     * 根据instId获取备份数据
     * @param instId
     * @param tableId
     * @return
     */
    public List<BpmInstData> getByArchiveLog(String instId, Integer tableId) {
        return bpmInstDataMapper.getByArchiveLog(instId,tableId);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmInstDataMapper.delArchiveByInstId(instId,tableId);
    }
}
