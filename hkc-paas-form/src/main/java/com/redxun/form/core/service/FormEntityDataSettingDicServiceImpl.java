
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormEntityDataSettingDic;
import com.redxun.form.core.mapper.FormEntityDataSettingDicMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
 * [业务实体数据配置字典]业务服务类
 */
@Service
public class FormEntityDataSettingDicServiceImpl extends SuperServiceImpl<FormEntityDataSettingDicMapper, FormEntityDataSettingDic> implements BaseService<FormEntityDataSettingDic> {

    @Resource
    private FormEntityDataSettingDicMapper formEntityDataSettingDicMapper;

    @Override
    public BaseDao<FormEntityDataSettingDic> getRepository() {
        return formEntityDataSettingDicMapper;
    }

    public List<FormEntityDataSettingDic> getBySettingId(String id) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("SETTING_ID_",id);
        queryWrapper.orderByAsc("SN_");
        return formEntityDataSettingDicMapper.selectList(queryWrapper);
    }

    public void deleteBySettingId(String id) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("SETTING_ID_",id);
        formEntityDataSettingDicMapper.delete(queryWrapper);
    }

    public List<FormEntityDataSettingDic> getByRoleIdDataTypeId(String roleId, String dataTypeId) {
        return formEntityDataSettingDicMapper.getByRoleIdDataTypeId(roleId,dataTypeId);
    }
}
