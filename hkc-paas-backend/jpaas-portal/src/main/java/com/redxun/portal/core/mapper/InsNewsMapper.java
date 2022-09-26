package com.redxun.portal.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.portal.core.entity.InsNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 信息公告数据库访问层
*/
@Mapper
public interface InsNewsMapper extends BaseDao<InsNews> {
    /**
     * 根据新闻类型查看
     * @param page
     * @param params
     * @return
     */
    List<InsNews> getBySysDicNew(IPage page,@Param("w") Map<String,Object> params);

    /**
     * 查看所有发布的新闻
     * @param page
     * @param params
     * @return
     */
    List<InsNews> getAllByStatus(IPage page,@Param("w") Map<String,Object> params);
}
