package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysInterfaceProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* 接口项目表数据库访问层
*/
@Mapper
public interface SysInterfaceProjectMapper extends BaseDao<SysInterfaceProject> {

    /**
     * 接口项目是否存在
     * @param params
     * @return
     */
    int isExist(@Param("w") Map<String,Object> params);
}
