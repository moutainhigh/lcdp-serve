package com.redxun.form.bo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.mapper.FormBoDefMapper;
import com.redxun.form.bo.mapper.FormBoRelationMapper;
import com.redxun.form.core.entity.FormDefPermission;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.service.FormDefPermissionServiceImpl;
import com.redxun.form.core.service.FormMobileServiceImpl;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.util.FormCacheHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [业务模型]业务服务类
 *
 * @author hujun
 */
@Service
public class FormBoDefServiceImpl extends SuperServiceImpl<FormBoDefMapper, FormBoDef> implements BaseService<FormBoDef> {

    @Resource
    private FormBoDefMapper formBoDefMapper;
    @Resource
    private FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Resource
    private FormBoRelationServiceImpl formBoRelationServiceImpl;
    @Resource
    private FormBoRelationMapper formBoRelationMapper;
    @Resource
    private FormDefPermissionServiceImpl formDefPermissionServiceImpl;
    @Resource
    private FormPcServiceImpl formPcServiceImpl;
    @Resource
    private FormMobileServiceImpl formMobileService;

    @Override
    public BaseDao<FormBoDef> getRepository() {
        return formBoDefMapper;
    }


    /**
     * 根据 boDefId 获取业务模型对象。
     * @param boDefId
     * @return
     */
    public FormBoDef getByBoDefId(String boDefId) {
        FormBoDef formBoDef = formBoDefMapper.selectById(boDefId);
        FormBoEntity entity = formBoEntityServiceImpl.getByDefId(boDefId, true);
        formBoDef.setFormBoEntity(entity);
        return formBoDef;
    }

    public JsonResult savePermission(JSONObject jsonObject) throws Exception {
        JSONArray pcAry = jsonObject.getJSONArray("pc");
        JSONArray mobileAry = jsonObject.getJSONArray("mobile");
        for (Object obj : pcAry) {
            JSON json = (JSON) JSON.toJSON(obj);
            FormDefPermission formDefPermission = JSONObject.toJavaObject(json, FormDefPermission.class);
            String boDefId=formDefPermission.getBoDefId();
            FormBoDef formBoDef=formBoDefMapper.selectById(boDefId);
            formDefPermission.setBoAlias(formBoDef.getAlias());

            if (!formDefPermissionServiceImpl.isExist(formDefPermission)) {
                formDefPermissionServiceImpl.insert(formDefPermission);
            } else {
                formDefPermissionServiceImpl.update(formDefPermission);
            }
        }
        for (Object obj : mobileAry) {
            JSON json = (JSON) JSON.toJSON(obj);
            FormDefPermission formDefPermission = JSONObject.toJavaObject(json, FormDefPermission.class);
            String boDefId=formDefPermission.getBoDefId();
            FormBoDef formBoDef=formBoDefMapper.selectById(boDefId);
            formDefPermission.setBoAlias(formBoDef.getAlias());

            if (!formDefPermissionServiceImpl.isExist(formDefPermission)) {
                formDefPermissionServiceImpl.insert(formDefPermission);
            } else {
                formDefPermissionServiceImpl.update(formDefPermission);
            }
        }
        return JsonResult.Success("保存成功！");
    }

    public void saveDef(FormBoDef formBoDef, String mainEntId) throws Exception {
        boolean flag = isExist(formBoDef);
        if (flag) {
            return;
        }
        String boDefId = formBoDef.getId();
        if (StringUtils.isEmpty(boDefId)) {
            insert(formBoDef);
            addRelation(formBoDef.getId(), mainEntId, formBoDef);
        }
        else{
            FormBoDef orignDef=getById(boDefId);
            //将新的数据复制到原来的数据上来。
            BeanUtil.copyNotNullProperties(orignDef,formBoDef);
            update(orignDef);
            //删除关系
            formBoRelationServiceImpl.delByDefId(boDefId);
            //添加关系
            addRelation(boDefId,mainEntId, formBoDef);
        }
    }

    /**
     * 添加关系。
     * @param boDefId
     */
    private void addRelation(String boDefId,String mainEntId, FormBoDef formBoDef){
        FormBoRelation formBoRelation=new FormBoRelation();
        formBoRelation.setBodefId(boDefId);
        formBoRelation.setEntId(mainEntId);
        formBoRelation.setParentEntId("0");
        formBoRelation.setAppId(formBoDef.getAppId());
        formBoRelation.setType(FormBoRelation.RELATION_MAIN);
        formBoRelationServiceImpl.insert(formBoRelation);
    }

    public boolean isExist(FormBoDef formBoDef){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",formBoDef.getAlias());
        if(StringUtils.isNotEmpty( formBoDef.getId())){
            queryWrapper.ne("ID_",formBoDef.getId());
        }
        int count=formBoDefMapper.selectCount(queryWrapper);

        return count>0;
    }

    /**
     * 根据别名获取BO定义。
     * @param alias
     * @return
     */
    public  FormBoDef getByAlias(String alias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",alias);
        FormBoDef formBoDef= formBoDefMapper.selectOne(wrapper);
        return formBoDef;
    }



    /**
     * 根据表单别名获取BO别名。
     * @param alias
     * @return
     */
    public String getAliasByFormAlias(String alias){
        String key=FormCacheHandler.getBoAliasKey(alias);

        Object obj=CacheUtil.get(FormCacheHandler.FORMALIAS_REGION,key);
        if(obj!=null){
            return (String) obj;
        }

        String boAlias= formBoDefMapper.getAliasByFormAlias(alias);
        CacheUtil.set(FormCacheHandler.FORMALIAS_REGION,key,boAlias);

        return boAlias;
    }


    /**
     * 保存BO
     * <pre>
     *     1. 未生成BO
     *      创建BO
     *     2. 已经生成BO
     *      1. 新增实体
     *      2. 更新实体
     *          1. 新增的BO插入
     *          2. 更新属性
     *          3. 是否删除属性
     *      3. 删除实体
     *
     * </pre>
     * @param boEntity
     * @param boDefId
     */
    public FormBoDef  saveBo(FormBoEntity boEntity,   String boDefId, boolean delField,boolean delTable){
        FormBoDef boDef=null;
        if(StringUtils.isEmpty(boDefId) ){
            boDef= createBoDef(boEntity);
        }
        else{
            boDef=updateBo(boEntity,boDefId,delField,delTable);
        }
        return boDef;
    }

    /**
     * 更新实体。
     * <pre>
     *     1. 删除关系
     *     2. 处理添加实体
     *     3. 处理更新的实体
     *     4. 处理需要删除的实体
     * </pre>
     * @param boEntity
     * @param boDefId
     * @param delField
     * @param delTable
     */
    private FormBoDef updateBo(FormBoEntity boEntity, String boDefId, boolean delField,boolean delTable){

        FormBoDef boDef=formBoDefMapper.selectById(boDefId);
        boDef.setSupportDb(boEntity.getGendb());
        boDef.setAlias(boEntity.getAlias());
        boDef.setName(boEntity.getName());
        formBoDefMapper.updateById(boDef);
        String appId=boEntity.getAppId();
        List<FormBoEntity> boEntityList=boEntity.getBoEntityList();

        //删除关系
        formBoRelationServiceImpl.delByDefId(boDefId);

        String mainEntId=boEntity.getOriginEnt().getId();
        //主表
        boEntity.setId(mainEntId);
        formBoEntityServiceImpl.updateEnt(boEntity,delField);
        formBoRelationServiceImpl.add(boDefId,mainEntId,"0",FormBoRelation.RELATION_MAIN,appId);

        //添加的实体
        List<FormBoEntity> newEnts=getByType(boEntityList,TableUtil.OP_ADD);
        for(FormBoEntity ent:newEnts){
            ent.setAppId(appId);
            formBoEntityServiceImpl.add(ent);
            formBoRelationServiceImpl.add(boDefId,ent.getId(),mainEntId,ent.getRelationType(),appId);
        }

        //处理更新的实体
        List<FormBoEntity> updEnts=getByType(boEntityList,TableUtil.OP_UPD);
        for(FormBoEntity ent:updEnts){
            formBoEntityServiceImpl.updateEnt(ent,delField);
            formBoRelationServiceImpl.add(boDefId,ent.getId(),mainEntId,ent.getRelationType(),appId);
        }

        if(delTable){
            //处理删除的实体
            List<FormBoEntity> delEnts=getByType(boEntityList,TableUtil.OP_DEL);
            for(FormBoEntity ent:delEnts){
                formBoEntityServiceImpl.delByEntId(ent.getId());
            }
        }


        return  boDef;
    }

    public void saveBoRelation(FormBoDef formBoDef){
        String boDefId=formBoDef.getId();
        //删除关系
        formBoRelationServiceImpl.delByDefId(boDefId);

        FormBoRelation main=formBoDef.getFormBoEntity().getBoRelation();
        main.setBodefId(boDefId);
        main.setEntId(formBoDef.getFormBoEntity().getId());
        formBoRelationServiceImpl.insert(main);
        for(FormBoEntity formBoEntity:formBoDef.getFormBoEntity().getBoEntityList()){
            FormBoRelation sub=formBoEntity.getBoRelation();
            sub.setBodefId(boDefId);
            sub.setEntId(formBoEntity.getId());
            formBoRelationServiceImpl.insert(sub);
            List<String> sunBoRelations=sub.getSunBoRelations();
            if(BeanUtil.isEmpty(sunBoRelations)){
                continue;
            }
            for (String sunRelation:sunBoRelations) {
                JSONObject sunJson = JSONObject.parseObject(sunRelation);
                FormBoRelation sun = JSONObject.parseObject(sunJson.getString("boRelation"),FormBoRelation.class);
                sun.setBodefId(boDefId);
                sun.setEntId(sunJson.getString("id"));
                formBoRelationServiceImpl.insert(sun);
            }
        }
    }

    private List<FormBoEntity> getByType(List<FormBoEntity> formBoEntityList, String type){
        List<FormBoEntity> boEntities= formBoEntityList.stream().filter(p->p.getType().equals(type)).collect(Collectors.toList());
        return  boEntities;
    }

    private FormBoDef  createBoDef(FormBoEntity boEntity){

        //创建bo定义
        FormBoDef boDef=new FormBoDef();
        boDef.setName(boEntity.getName());
        boDef.setSupportDb(boEntity.getGendb());
        boDef.setAlias(boEntity.getAlias());
        boDef.setGenType(FormBoEntity.GENMODE_EASYFORM.equals(boEntity.getGenMode())?FormBoDef.GEN_TYPE_EASY_FORM:FormBoDef.GEN_TYPE_FORM);
        String boDefId= IdGenerator.getIdStr();
        boDef.setId(boDefId);
        boDef.setTreeId(boEntity.getTreeId());
        String appId=boEntity.getAppId();
        boDef.setAppId(appId);
        formBoDefMapper.insert(boDef);

        //创建主实体
        formBoEntityServiceImpl.add(boEntity);
        String entId=boEntity.getId();

        //添加主关系
        formBoRelationServiceImpl.add(boDefId,entId,"0", FormBoRelation.RELATION_MAIN,appId);

        //添加子实体
        for(FormBoEntity entity:boEntity.getBoEntityList()){
            //插入属性和实体。
            entity.setAppId(appId);
            formBoEntityServiceImpl.add(entity);
            String subEntId=entity.getId();
            String relationType=entity.getBoRelation()==null?entity.getRelationType():entity.getBoRelation().getType();
            formBoRelationServiceImpl.add(boDefId,subEntId,entId,relationType,appId);
        }

        return boDef;

    }

    public JSONArray getBoDefConstruct(String boDefId,boolean isMain,String entName){
        FormBoEntity  boEnt= formBoEntityServiceImpl.getByDefId(boDefId, true);

        formBoEntityServiceImpl.removeRefFields(boEnt,boDefId);

        List<FormBoAttr> attrs=boEnt.getBoAttrList();

        JSONArray jsonAry=new JSONArray();

        for(FormBoAttr attr:attrs){
            JSONObject obj=getByAttr(attr, boEnt);
            jsonAry.add(obj);
        }
        //只要主表
        if(isMain){
            return jsonAry;
        }

        List<FormBoEntity> boEnts=boEnt.getBoEntityList();
        for(FormBoEntity ent:boEnts){
            if(StringUtils.isEmpty(entName)){
                JSONObject entJson= getByEnt(ent);
                jsonAry.add(entJson);
            }
            //非空。
            else{
                if(entName.equals(ent.getName())){
                    JSONObject entJson= getByEnt(ent);
                    jsonAry.add(entJson);
                }
            }
        }
        return jsonAry;
    }

    private JSONObject getByEnt(FormBoEntity ent){
        JSONObject entJson=new JSONObject();

        entJson.put("key", ent.getId());
        entJson.put("name", ent.getAlias());
        entJson.put("title", ent.getName());
        entJson.put("dataType", "");
        entJson.put("isField", false);
        entJson.put("type", ent.getBoRelation().getType());
        entJson.put("boEntId", ent.getId());
        entJson.put("iconCls", "icon-table");

        List<FormBoAttr> boAttrs=ent.getBoAttrList();
        List<FormBoAttr> newBoAttrs=formBoEntityServiceImpl.delRedundantFields(boAttrs);
        JSONArray subAry=new JSONArray();

        for(FormBoAttr attr:newBoAttrs){
            JSONObject obj=getByAttr(attr, ent);
            subAry.add(obj);
            entJson.put("children", subAry);
        }
        return entJson;
    }

    private JSONObject getByAttr(FormBoAttr attr,FormBoEntity boEnt){
        JSONObject obj=new JSONObject();

        JSONObject jsonType=new JSONObject();
        jsonType.put("varchar", "icon-varchar");
        jsonType.put("clob", "icon-clob");
        jsonType.put("date", "icon-date");
        jsonType.put("number", "icon-number");

        obj.put("key", attr.getId());
        obj.put("name", attr.getName());
        obj.put("title", attr.getComment());
        obj.put("dataType", attr.getDataType());
        obj.put("isField", true);
        obj.put("entName", boEnt.getAlias());
        obj.put("type", boEnt.getBoRelation().getType());
        obj.put("boEntId", boEnt.getId());
        obj.put("attrId", attr.getId());
        obj.put("iconCls", jsonType.getString(attr.getDataType()));
        obj.put("control", attr.getControl());

        return obj;
    }


    /**
     * 删除业务模型
     * @param ids
     */
    public void delBoDef(String ids) {
        String[] aryId = ids.split(",");
        for (int i = 0; i < aryId.length; i++) {
            FormBoDef formBoDef = getByBoDefId(aryId[i]);
            if(formBoDef!=null){
                FormBoRelation formBoRelation = formBoRelationServiceImpl.getEntByEntNameAndDefId(formBoDef.getId(), formBoDef.getFormBoEntity().getAlias());
                //删除物理表, 从模型生成的不删除
                if(formBoRelation!=null && formBoDef.getSupportDb()==1&& (!"direct".equals(formBoDef.getGenType()))){
                    String entId = formBoRelation.getEntId();
                    FormBoEntity formBoEntity = formBoEntityServiceImpl.get(entId);
                    formBoEntityServiceImpl.deleteBoTable(entId, formBoEntity.getTableName());
                }
                //删除业务实体.
                if(BeanUtil.isNotEmpty(formBoRelation) && StringUtils.isNotEmpty(formBoRelation.getEntId())){
                    formBoEntityServiceImpl.delByEntId(formBoRelation.getEntId());
                }
                //删除业务模型
                formBoDefMapper.deleteById(formBoDef.getId());
                //删除关系
                formBoRelationServiceImpl.delByDefId(formBoDef.getId());
                //删除表单
                List<FormPc> formPcList = formPcServiceImpl.getByBoDefId(formBoDef.getId());
                for (int j = 0; j < formPcList.size(); j++) {
                    formPcServiceImpl.delete(formPcList.get(j).getId());
                }
                //删除移动表单
                List<FormMobile> formMobileList = formMobileService.getByBoDefId(formBoDef.getId(),null);
                for (int j = 0; j < formMobileList.size(); j++) {
                    formMobileService.delete(formMobileList.get(j).getId());
                }
            }
        }
    }

    public void getSunBoRelation(FormBoDef boDef){
        FormBoEntity boEntity=boDef.getFormBoEntity();
        if(BeanUtil.isEmpty(boEntity)){
            return;
        }
        List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
        if(BeanUtil.isEmpty(boEntityList)){
            return;
        }
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BODEF_ID_",boDef.getId());
        for (FormBoEntity sub:boEntityList) {
            wrapper.eq("PARENT_ENT_ID_",sub.getId());
            List<FormBoRelation> suns=formBoRelationMapper.selectList(wrapper);
            if(BeanUtil.isEmpty(suns)){
               continue;
            }
            JSONArray sunBoRelations = new JSONArray();
            for (FormBoRelation relation:suns) {
                String id= relation.getEntId();
                FormBoEntity sunBoEntity = formBoEntityServiceImpl.getById(id);
                JSONObject boRelation = new JSONObject();
                boRelation.put("id",id);
                boRelation.put("name",sunBoEntity.getName());
                boRelation.put("boRelation",JSONObject.parseObject(JSONObject.toJSONString(relation)));
                sunBoRelations.add(boRelation);
            }
            sub.setSunBoRelations(sunBoRelations);
        }
    }

}
