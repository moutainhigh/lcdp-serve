package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.db.mapper.SuperMapper;
import com.redxun.user.org.entity.OsInst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 注册机构Mapper接口
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Mapper
public interface OsInstMapper extends BaseDao<OsInst> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsInst> findList(Page<OsInst> page, @Param("p") Map<String, Object> params);

    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    IPage getPageByUserIdAndStatus(IPage<OsInst> page, @Param("w") Map<String, Object> params);

    /**
     * 查询
     * @param params
     * @return
     */
    List<OsInst> getByUserIdAndStatus(@Param("w") Map<String, Object> params);


}
