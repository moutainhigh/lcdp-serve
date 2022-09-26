package com.redxun.common.base.db;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.ISuperService;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseService <E extends BaseEntity> extends ISuperService<E> {


    BaseDao<E> getRepository();

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    default E get(Serializable id) {

        return getRepository().selectById(id);
    }

    default List<E> getByIds(Collection<? extends Serializable> ids){
        return  getRepository().selectBatchIds(ids);
    }

    /**
     * 获取所有列表
     * @return
     */
    default List<E> getAll() {
        return getRepository().selectList(null);
    }

    /**
     * 获取总数
     * @return
     */
    default int getTotalCount() {
        return getRepository().selectCount(null);
    }

    /**
     * 保存
     * @param entity
     * @return
     */
    default int insert(E entity) {
        if(BeanUtil.isEmpty(entity.getPkId())) {
            entity.setPkId(IdGenerator.getIdStr());
        }
        return getRepository().insert(entity);
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    default int update(E entity) {
        return getRepository().updateById(entity);

    }


    /**
     * 根据Id删除
     * @param id
     */
    default void delete(Serializable id) {
        getRepository().deleteById(id);
    }

    /**
     * 批量删除
     * @param entities
     */
    default void delete(Collection<Serializable> entities) {
        getRepository().deleteBatchIds(entities);
    }



    /**
     * 根据条件查询获取
     * @param spec
     * @return
     */
    default List<E> findAll(Wrapper<E> spec) {
        return getRepository().selectList(spec);
    }



    /**
     * 根据查询条件分页获取
     * @param spec
     * @param pageable
     * @return
     */
    default IPage<E> findAll(Wrapper<E> spec, IPage pageable) {
        return getRepository().selectPage(pageable,spec);
    }

   /**
     * 获取查询条件的结果数
     * @param spec
     * @return
     */
   @Override
   default int count(Wrapper<E> spec) {
        return getRepository().selectCount(spec);
    }


    /**
     * 使用QueryFilter 进行查询。
     * @param queryFilter
     * @return
     */
    default  IPage query(QueryFilter queryFilter){
        BaseDao baseDao=getRepository();

        Map<String,Object> params= PageHelper.constructParams(queryFilter);

        return  baseDao.query(queryFilter.getPage(),params);
    }


    /**
     * 使用QueryFilter查询列表数据。
     * @param queryFilter
     * @return
     */
    default  List queryList(QueryFilter queryFilter){
        BaseDao baseDao=getRepository();

        Map<String,Object> params= PageHelper.constructParams(queryFilter);

        return baseDao.query(params);

    }



    default String getRedisKey(E entity) {
        return entity.getClass().getName() + ":" + entity.getPkId();
    }

    default String getRedisKey(String name, String pkdId) {
        return name + ":" + pkdId;
    }
}
