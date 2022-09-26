package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 流程定义数据库访问层
*/
@Mapper
public interface BpmDefMapper extends BaseDao<BpmDef> {

    /**
     * 获得某个流程的主版本
     * @param key
     * @return
     */
    BpmDef getMainByKey(String key);


    /**
     * 更新流程定义
     * @param params
     */
    void updateMainDefIdIsMain(@Param("w") Map<String, Object> params);

    /**
     * 获取当前流程中最大的版本
     * @param mainDefId
     * @return
     */
    Integer getMaxVersion(String mainDefId);

    /**
     * 根据流程Id获得流程的所有版本号与DefId
     * @param mainDefId
     * @return
     */
    List<BpmDef> getAllVersionsByMainDefId(String mainDefId);

}
