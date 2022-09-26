package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysSignature;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
* 签名实体数据库访问层
*/
@Mapper
public interface SysSignatureMapper extends BaseDao<SysSignature> {

    List<SysSignature> getSignatureList(String userId);
}
