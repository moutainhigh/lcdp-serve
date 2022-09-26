package com.redxun.form.bo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.mapper.FormBoAttrMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [业务实体属性]业务服务类
*/
@Service
public class FormBoAttrServiceImpl extends SuperServiceImpl<FormBoAttrMapper, FormBoAttr> implements BaseService<FormBoAttr> {

    @Resource
    private FormBoAttrMapper formBoAttrMapper;
    @Resource
    private FormBoEntityServiceImpl formBoEntityService;

    @Override
    public BaseDao<FormBoAttr> getRepository() {
        return formBoAttrMapper;
    }


    public List<FormBoAttr> getByEntId(String entId){
        QueryWrapper query=new QueryWrapper();
        query.eq("ENT_ID_",entId);
        query.orderByAsc("SN_");
        return  formBoAttrMapper.selectList(query);
    }


    /**
     * 根据实体Id删除属性。
     * @param entId
     */
    public void delByEntId(String entId){
        QueryWrapper query=new QueryWrapper();
        query.eq("ENT_ID_",entId);
        formBoAttrMapper.delete(query);
    }

    public Map<String, FormBoAttr> getAttrsMapByEntId(String tableName) {
        FormBoEntity ent = formBoEntityService.getByTableName(tableName);
        List<FormBoAttr> list = getByEntId(ent.getId());
        Map<String, FormBoAttr> map = new HashMap<>();
        for (FormBoAttr sysBoAttr : list) {
            map.put(sysBoAttr.getFieldName().toUpperCase(), sysBoAttr);
        }
        return map;
    }

    /**
     * 获取所有引用属性
     * @param entId
     * @return
     */
    public List<FormBoAttr> getAllRefByEntId(String entId){
        QueryWrapper query=new QueryWrapper();
        query.eq("ENT_ID_",entId);
        query.eq("CONTROL_","rx-ref");
        query.orderByAsc("SN_");
        return  formBoAttrMapper.selectList(query);
    }
}
