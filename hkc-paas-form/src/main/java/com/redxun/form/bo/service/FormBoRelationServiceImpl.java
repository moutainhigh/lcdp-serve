package com.redxun.form.bo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.mapper.FormBoRelationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * [业务实体]业务服务类
 *
 * @author hujun
 */
@Service
public class FormBoRelationServiceImpl extends SuperServiceImpl<FormBoRelationMapper, FormBoRelation> implements BaseService<FormBoRelation> {

    @Resource
    private FormBoRelationMapper formBoRelationMapper;

    @Override
    public BaseDao<FormBoRelation> getRepository() {
        return formBoRelationMapper;
    }


    public String add(String boDefId, String entId,String parentEntId,String type,String appId){
        FormBoRelation relation=new FormBoRelation();
        relation.setId(IdGenerator.getIdStr())
            .setType(type)
            .setBodefId(boDefId)
            .setEntId(entId)
            .setAppId(appId)
            .setParentEntId(parentEntId);
        if(!FormBoRelation.RELATION_MAIN.equals(type)){
            relation.setPkField(FormBoEntity.FIELD_PK);
            relation.setFkField(FormBoEntity.FIELD_FK);
        }

        formBoRelationMapper.insert(relation);

        return relation.getPkId();

    }

    /**
     * 根据defid 删除
     * @param defId
     */
    public void delByDefId(String defId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BODEF_ID_", defId);
        formBoRelationMapper.delete(wrapper);
    }

    /**
     * 根据实体ID来删除关联  Elwin ZHANG 2021-4-8
     * @param entId 实体ID
     */
    public void delByEntityId(String entId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ENT_ID_", entId);
        formBoRelationMapper.delete(wrapper);
    }
    public FormBoRelation getByDefEntId(String boDefId, String entId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BODEF_ID_", boDefId);
        queryWrapper.eq("ENT_ID_", entId);
        return formBoRelationMapper.selectOne(queryWrapper);
    }

    public List<FormBoRelation> getByBoDefId(String boDefId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BODEF_ID_", boDefId);
        return formBoRelationMapper.selectList(queryWrapper);
    }

    public List<FormBoRelation> getByEntId(String entId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ENT_ID_", entId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            queryWrapper.eq("DELETED_","0");
        }
        return formBoRelationMapper.selectList(queryWrapper);
    }

    public FormBoRelation getEntByEntNameAndDefId(String boDefId, String entName) {
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        return formBoRelationMapper.getByBoDefIdAndEntName(boDefId, entName,deleted);
    }

}
