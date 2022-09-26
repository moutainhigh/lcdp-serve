package com.redxun.form.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.form.core.entity.FormBoPmt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 业务单据数据权限数据库访问层
*/
@Mapper
public interface FormBoPmtMapper extends BaseDao<FormBoPmt> {

    void deleteByPmtId(@Param("pmtId") String pmtId);

    void deleteByBoListId(@Param("boListId") String boListId);
}
