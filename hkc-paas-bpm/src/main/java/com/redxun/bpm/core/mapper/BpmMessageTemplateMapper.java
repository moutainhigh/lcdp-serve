package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmMessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* bpm_message_template数据库访问层
*/
@Mapper
public interface BpmMessageTemplateMapper extends BaseDao<BpmMessageTemplate> {

}
