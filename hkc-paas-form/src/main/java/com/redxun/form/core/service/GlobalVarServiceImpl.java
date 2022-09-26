
package com.redxun.form.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.GlobalVar;
import com.redxun.form.core.mapper.GloalVarMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [代码生成全局变量]业务服务类
 * @author ASUS
 */
@Service
public class GlobalVarServiceImpl extends SuperServiceImpl<GloalVarMapper, GlobalVar> implements BaseService<GlobalVar> {

    @Resource
    private GloalVarMapper gloalVarMapper;

    @Override
    public BaseDao<GlobalVar> getRepository() {
        return gloalVarMapper;
    }

    /**
     * 根据当前用户获取全局变量。
     * @param userId
     * @return
     */
    public GlobalVar getByUserId(String userId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("CREATE_BY_",userId);
        return gloalVarMapper.selectOne(wrapper);
    }

}
