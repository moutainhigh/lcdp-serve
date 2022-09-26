package com.redxun.user.org.mapper;


import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsUserType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* 用户类型数据库访问层
*/
@Mapper
public interface OsUserTypeMapper extends BaseDao<OsUserType> {
    /**
     * 按照用户类型编码映射
     * @param code
     * @return
     */
    OsUserType getByCode(@Param("code") String code,@Param("tenantId")String tenantId);


}
