package com.redxun.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.redxun.common.service.ISuperService;

/**
 * service实现父类
 *
 * @author yjy
 * @date 2019/1/10
 */
public class SuperServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ISuperService<T> {


}
