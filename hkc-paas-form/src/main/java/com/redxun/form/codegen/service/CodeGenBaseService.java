package com.redxun.form.codegen.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.BaseEntity;
import com.redxun.common.service.impl.SuperServiceImpl;

/**
 * @author ray
 */
public abstract  class CodeGenBaseService<M extends BaseMapper<T>, T extends BaseEntity> extends SuperServiceImpl<M, T> implements BaseService<T> {

}
