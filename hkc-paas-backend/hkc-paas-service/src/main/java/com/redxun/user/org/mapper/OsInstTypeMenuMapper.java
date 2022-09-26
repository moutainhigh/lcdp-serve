package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsInstTypeMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 机构类型授权菜单数据库访问层
*/
@Mapper
public interface OsInstTypeMenuMapper extends BaseDao<OsInstTypeMenu> {
    /**
     * 根据机构类型获取某个机构类型下的菜单
     * @param instType
     * @return
     */
    List<OsInstTypeMenu> getAppsByInstType(String instType);

    void deleteByInstTypeId(@Param("instTypeId")String instTypeId);
}
