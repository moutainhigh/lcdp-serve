package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmDefaultTemplate;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* bpm_default_template数据库访问层
*/
@Mapper
public interface BpmDefaultTemplateMapper extends BaseDao<BpmDefaultTemplate> {

}
