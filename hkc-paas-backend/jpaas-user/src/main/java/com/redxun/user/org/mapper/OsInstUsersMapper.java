package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.user.org.entity.OsInstUsers;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* 租户关联用户表数据库访问层
*/
@Mapper
public interface OsInstUsersMapper extends BaseDao<OsInstUsers> {


    /**
     *查询所有申请加入本机构的人员名单
     * @param page
     * @param params
     * @return
     */
    IPage getByDomain(IPage page,@Param("w") Map<String, Object> params);
}
