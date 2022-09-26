package com.redxun.form.bo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.Tree;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import com.redxun.form.bo.entity.*;
import com.redxun.form.bo.mapper.FormBoEntityMapper;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPermission;
import com.redxun.form.core.service.FormPcServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [业务实体]业务服务类
 *
 * @author hujun
 */
@Service
public class FormBoEntityServiceImpl extends SuperServiceImpl<FormBoEntityMapper, FormBoEntity> implements BaseService<FormBoEntity> {

    @Resource
    private FormBoEntityMapper formBoEntityMapper;
    @Resource
    private FormBoAttrServiceImpl formBoAttrServiceImpl;
    @Resource
    private FormPcServiceImpl formPcServiceImpl;
    @Resource
    private FormBoDefServiceImpl formBoDefServiceImpl;
    @Resource
    private FormBoRelationServiceImpl formBoRelationServiceImpl;
    @Resource
    TableUtil tableUtil;
    @Resource
    CommonDao commonDao;


    @Override
    public BaseDao<FormBoEntity> getRepository() {
        return formBoEntityMapper;
    }

    /**
     * 添加BO实体对象。
     * <pre>
     *     1.添加实体
     *     2.添加属性。
     * </pre>
     * @param formBoEntity
     * @return
     */
    public String add(FormBoEntity formBoEntity) {
        this.insert(formBoEntity);

        String entId=formBoEntity.getId();

        for(FormBoAttr attr:formBoEntity.getBoAttrList()){
            attr.setEntId(entId);
            String fieldName= attr.getFieldName();
            if(StringUtils.isEmpty(fieldName)){
                attr.setFieldName( TableUtil.getFieldName(attr.getName()));
            }
            formBoAttrServiceImpl.insert(attr);
        }

        return formBoEntity.getId();
    }




    public JSONArray getAttrListByBoEntity(JSONArray boEntityData,FormBoEntity boEntity){
        JSONArray resultData=new JSONArray();
        JSONObject mainJson = getFieldsByFormBoEnt(boEntityData,boEntity);

        resultData.add(mainJson);

        //计算子表
        List<FormBoEntity> subBoEntityList = boEntity.getBoEntityList();
        for(FormBoEntity subBoEnt:subBoEntityList){
            JSONObject subJson = getFieldsByFormBoEnt(boEntityData,subBoEnt);
            resultData.add(subJson);
        }
        return resultData;
    }



    private JSONObject getFieldsByFormBoEnt(JSONArray boEntityData,FormBoEntity formBoEntity){
        JSONObject json = new JSONObject();
        String alias = formBoEntity.getAlias();

        json.put("alias",alias);
        json.put("name",formBoEntity.getName());
        //计算字段
        List<FormBoAttr> subAttrs = formBoEntity.getBoAttrList();

        int indexs = isContain(boEntityData,alias);
        if(indexs !=-1){
            json =boEntityData.getJSONObject(indexs);
            json.put("name",formBoEntity.getName());
        }else {
            json.put("attrData",new JSONArray());
        }
        calcFields(json, subAttrs);

        return json;
    }

    /**
     * 计算字段
     * @param boEntityObj
     * @param attrs
     */
    private void calcFields(JSONObject boEntityObj, List<FormBoAttr> attrs) {
        if(BeanUtil.isEmpty(attrs)){
            boEntityObj.put("attrData", new JSONArray());
            return;
        }
        JSONArray attrData =boEntityObj.getJSONArray("attrData");
        //添加
        for (FormBoAttr initAttr : attrs) {
            String name = initAttr.getName();
            JSONObject attr = new JSONObject();
            attr.put("alias",name);
            attr.put("name",initAttr.getComment());
            attr.put("control",initAttr.getControl());
            JSONObject initSet = new JSONObject();
            initSet.put("valType","constant");
            initSet.put("val","");
            initSet.put("text","");
            attr.put("initSet",initSet);
            attr.put("saveSet",initSet);

            attr.put("isSingle",initAttr.getIsSingle());

            int indexs = isContain(attrData,name);
            if(indexs ==-1){
                attrData.add(attr);
            }
        }
        //清除不包含的
        JSONArray newAttrData =crealNoContian(attrs, attrData);
        boEntityObj.put("attrData", newAttrData);
    }

    private JSONArray crealNoContian(List<FormBoAttr> attrs, JSONArray attrData){
        JSONArray newAttrData =new JSONArray();
        if (BeanUtil.isEmpty(attrData)){
            return newAttrData;
        }
        for(int i=0;i<attrData.size();i++){
            JSONObject attr = attrData.getJSONObject(i);
            if(isContainToFormBoAttrList(attrs,attr.getString("alias"))){
                newAttrData.add(attr);
            }
        }
        return newAttrData;
    }

    private  boolean isContainToFormBoAttrList(List<FormBoAttr> attrs,String alias){
        if (BeanUtil.isEmpty(attrs)){
            return false;
        }
        for (FormBoAttr attr : attrs) {
            if(alias.equals(attr.getName())){
                return true;
            }
        }
        return false;
    }

    private  Integer isContain(JSONArray attrData,String alias){
        if (BeanUtil.isEmpty(attrData)){
            return -1;
        }
        for(int i=0;i<attrData.size();i++){
            JSONObject attr = attrData.getJSONObject(i);
            if(alias.equals(attr.getString("alias"))){
                return i;
            }
            if(attr.containsKey(alias)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 计算主表按钮字段
     * @param attrs
     * @param buttons
     */
    private void mainBtnCalcFields(List<FormBoAttr> attrs, String buttons) {
        JSONArray btns = JSONArray.parseArray(buttons);
        for (int i = 0; i < btns.size(); i++) {
            JSONObject btn = btns.getJSONObject(i);
            String alias = btn.getString(FormPermission.PERMISSION_ALIAS);
            FormBoAttr attr = new FormBoAttr();
            attr.setName(alias);
            attr.setComment(btn.getString("name"));
        }
    }


    /**
     * 处理实体
     * @param formBoEntity
     */
    public void updateEnt(FormBoEntity formBoEntity,boolean allowDel){

        formBoEntity.setId(formBoEntity.getOriginEnt().getId());
        this.update(formBoEntity);

        List<FormBoAttr> attrList=formBoEntity.getBoAttrList();
        //新增的数据
        List<FormBoAttr> list= tableUtil.getAttrsByType(attrList,TableUtil.OP_ADD);
        for(FormBoAttr attr:list){
            attr.setEntId(formBoEntity.getId());
            String fieldName= attr.getFieldName();
            if(StringUtils.isEmpty(fieldName)){
                attr.setFieldName( TableUtil.getFieldName(attr.getName()));
            }
            formBoAttrServiceImpl.insert(attr);
        }
        //处理更新的数据
        List<FormBoAttr> updList= tableUtil.getAttrsByType(attrList,TableUtil.OP_UPD);
        for(FormBoAttr attr:updList){
            formBoAttrServiceImpl.update(attr);
        }

        //处理需要删除的数据。
        if(allowDel){
            List<FormBoAttr> delList= tableUtil.getAttrsByType(attrList,TableUtil.OP_DEL);
            for (FormBoAttr attr:delList){
                formBoAttrServiceImpl.delete(attr.getId());
            }
        }

    }

    /**
     * 根据id删除实体，同时删除关联的属性。
     * @param entId
     */
    public void delByEntId(String entId){
        formBoAttrServiceImpl.delByEntId(entId);
        formBoEntityMapper.deleteById(entId);
    }

    public List<FormBoEntity> getByDefId(String boDefId){
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        return formBoEntityMapper.getByDefId(boDefId,deleted);
    }

    /**
     * 根据boDefId 获取实体对象。
     * @param boDefId   bo定义ID
     * @param needAttr  是否需要属性
     * @return
     */
    public FormBoEntity getByDefId(String boDefId,boolean needAttr){
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        List<FormBoEntity> list= formBoEntityMapper.getByDefId(boDefId,deleted);

        if(needAttr){
            for (Iterator<FormBoEntity> it=list.iterator();it.hasNext();){
                FormBoEntity formBoEntity=it.next();
                String entId=formBoEntity.getId();
                List<FormBoAttr> attrs= formBoAttrServiceImpl.getByEntId(entId);
                //外部表情况下
                if(formBoEntity.external()){
                    for (int i = 0; i < attrs.size(); i++) {
                        FormBoAttr attr = attrs.get(i);
                        if(formBoEntity.getIdField().equals(attr.getFieldName())){
                            setIdField(formBoEntity);
                            attrs.remove(i);
                        }
                    }
                }
                formBoEntity.setBoAttrList(attrs);
            }
        }
        List<FormBoEntity> boEntityList= BeanUtil.listToTree(list);

        if(boEntityList==null||boEntityList.size()==0){
            return new FormBoEntity();
        }
        FormBoEntity boEntity= boEntityList.get(0);

        if(BeanUtil.isNotEmpty(boEntity.getChildrens())){
            List<FormBoEntity> boEntities=new ArrayList<>();
            for(int i=0;i<boEntity.getChildrens().size();i++){
                FormBoEntity child=(FormBoEntity)boEntity.getChildrens().get(i);
                if(needAttr){
                    String entId=child.getId();
                    List<FormBoAttr> attrs= formBoAttrServiceImpl.getByEntId(entId);
                    child.setBoAttrList(attrs);
                }
                boEntities.add(child);
            }
            boEntity.setBoEntityList(boEntities);
        }

        return boEntity;
    }

    public void setIdField(FormBoEntity ent){
        String entId = ent.getId();
        if(!"db".equals(ent.getGenMode())){
            setBaseIdField(ent,ent.getIdField());
            return;
        }
        List<FormBoAttr> attrs= formBoAttrServiceImpl.getByEntId(entId);
        if(BeanUtil.isEmpty(attrs)){
            return;
        }
        for (int i = 0; i < attrs.size(); i++) {
            FormBoAttr attr = attrs.get(i);
            if(ent.getIdField().equals(attr.getFieldName())){
                ent.setIdFieldId(attr.getId());
                ent.setIdFieldName(attr.getName());
                ent.setIdFieldComment(attr.getComment());
                break;
            }
        }
    }

    public void setBaseIdField(FormBoEntity ent,String idField){
        if("ID_".equals(idField)){
            ent.setIdFieldName("id");
            ent.setIdFieldComment("主键");
        }
        if("REF_ID_".equals(idField)){
            ent.setIdFieldName("refId");
            ent.setIdFieldComment("副键");
        }
        if("PARENT_ID_".equals(idField)){
            ent.setIdFieldName("parentId");
            ent.setIdFieldComment("父键");
        }
    }


    public FormBoEntity getWithAttrsByDefId(String boDefId){
        FormBoEntity boEntity=  getByDefId( boDefId,true);
        return boEntity;
    }

    /**
     * 根据实体ID 获取实体对象数据包括属性数据。
     * @param entId
     * @return
     */
    public FormBoEntity getByEntId(String entId){
        FormBoEntity boEntity=formBoEntityMapper.selectById(entId);
        List<FormBoAttr> attrs= formBoAttrServiceImpl.getByEntId(entId);
        boEntity.setBoAttrList(attrs);
        return  boEntity;
    }

    /**
     * 处理实体的变化。
     * <pre>
     *  1.新增
     *  2.更新
     *  3.删除
     * </pre>
     * @param formBoEntity
     */
    public void  handChangeAttrs(FormBoEntity  formBoEntity){
       try {
           //设置数据源
           DataSourceContextHolder.setDataSource(formBoEntity.getDsAlias());
           FormBoEntity orignEnt=formBoEntity.getOriginEnt();
           List<FormBoAttr> orignAttrs=orignEnt.getBoAttrList();
           List<FormBoAttr> attrs=tableUtil.getChangeAttrEnt(orignAttrs,formBoEntity.getBoAttrList());
           String appId=formBoEntity.getAppId();
           for(FormBoAttr attr:attrs){
               attr.setAppId(appId);
           }
           formBoEntity.setBoAttrList(attrs);
       }catch (Exception ex){
           DataSourceContextHolder.setDefaultDataSource();
       }finally {
           DataSourceContextHolder.setDefaultDataSource();
       }
    }

    private static Map<String, FormBoEntity> convertToMap(List<FormBoEntity>  attrs){
        Map<String, FormBoEntity> map = attrs.stream().collect(Collectors.toMap(p->p.getAlias().toLowerCase(), p -> p,(v1,v2)->v1));
        return map;
    }

    /**
     * 获取变更的实体数据。
     * <pre>
     *     处理实体变更。
     * </pre>
     * @param formBoEntity
     * @return
     */
    public void handEntity(FormBoEntity  formBoEntity){
        List<FormBoEntity> subList= formBoEntity.getBoEntityList();
        for(FormBoEntity subEnt:subList){
            if(StringUtils.isEmpty(subEnt.getIdField())) {
                //默认ID_
                subEnt.setIdField(FormBoEntity.FIELD_PK);
            }
            if(StringUtils.isEmpty(subEnt.getParentField())) {
                //默认PARENT_ID_
                subEnt.setParentField(FormBoEntity.FIELD_PARENTID);
            }
            if(StringUtils.isEmpty(subEnt.getVersionField())) {
                //默认UPDATE_VERSION_
                subEnt.setVersionField(FormBoEntity.FIELD_UPDATE_VERSION);
            }
            subEnt.setGenMode(formBoEntity.getGenMode());
            subEnt.setIsMain(0);
            subEnt.setAppId(formBoEntity.getAppId());
            subEnt.setDsAlias(formBoEntity.getDsAlias());
            subEnt.setDsName(formBoEntity.getDsName());
            subEnt.setGendb(formBoEntity.getGendb());
        }

        List<FormBoEntity> boEntityList=new ArrayList<>();

        FormBoEntity orignEnt=formBoEntity.getOriginEnt();
        boolean isNew=false;
        if(orignEnt==null){
            orignEnt=new FormBoEntity();
            formBoEntity.setOriginEnt(orignEnt);
            if(StringUtils.isEmpty(formBoEntity.getId()) && formBoEntity.getGendb()==1){
                isNew=true;
            }
        }
        List<FormBoEntity> orignEnts= orignEnt.getBoEntityList();
        List<FormBoEntity> ents= formBoEntity.getBoEntityList();

        Map<String, FormBoEntity> orignMap=convertToMap(orignEnts);
        Map<String, FormBoEntity> curMap=convertToMap(ents);
        //添加
        //addEnt(rtnMap,formBoEntity, FormBoRelation.RELATION_MAIN);
        //获取主实体变更的属性
        handChangeAttrs(formBoEntity);
        formBoEntity.setType(isNew?TableUtil.OP_ADD:TableUtil.OP_UPD );
        if(StringUtils.isEmpty(formBoEntity.getIdField())) {
            //默认ID_
            formBoEntity.setIdField(FormBoEntity.FIELD_PK);
        }
        if(StringUtils.isEmpty(formBoEntity.getParentField())) {
            //默认PARENT_ID_
            formBoEntity.setParentField(FormBoEntity.FIELD_PARENTID);
        }

        //处理新增和更新的实体数据。
        for(FormBoEntity ent:ents){
            if(!orignMap.containsKey( ent.getAlias().toLowerCase())){
                for(FormBoAttr attr:ent.getBoAttrList()){
                    attr.setType(TableUtil.OP_ADD);
                    attr.setAppId(formBoEntity.getAppId());
                }
                ent.setType(TableUtil.OP_ADD);
            }
            else{
                ent.setOriginEnt(orignMap.get(ent.getAlias().toLowerCase()));
                //获取子实体变更的属性数据。
                handChangeAttrs(ent);
                ent.setType(TableUtil.OP_UPD);
            }
            boEntityList.add(ent);
        }

        /**
         * 标记子实体为删除
         */
        for(FormBoEntity orign:orignEnts){
            if(!curMap.containsKey(orign.getAlias().toLowerCase())){
                orign.setType(TableUtil.OP_DEL);
                boEntityList.add(orign);
            }
        }

        formBoEntity.setBoEntityList(boEntityList);
    }

    public FormBoEntity getMainEntByDefId(String boDefId){
        FormBoEntity boEntity=formBoEntityMapper.getMainEntByDefId(boDefId);
        return  boEntity;
    }

    public JsonResult creatBoTable(String pkId){
        try{
            FormBoEntity boEnt = getById(pkId);
            String dsAlias=boEnt.getDsAlias();
            ITableOperator tableOperator= OperatorContext.getByDsAlias(dsAlias);
            if(boEnt!=null){
                List<FormBoAttr> boAttrList = formBoAttrServiceImpl.getByEntId(boEnt.getId());
                for(FormBoAttr attr : boAttrList) {
                    formBoAttrServiceImpl.update(attr);
                }
                boEnt.setBoAttrList(boAttrList);
                if(tableOperator.isTableExist(boEnt.getTableName())){
                    JsonResult result=new JsonResult(false,"【"+ boEnt.getTableName() +"】表已存在!");
                    return result;
                }
                List<AlterSql> alterSqls=tableUtil.getCreateSqlByBoEnt(boEnt,tableOperator);
                for(AlterSql alterSql:alterSqls){
                    commonDao.execute(boEnt.getDsAlias(),alterSql.getSql());
                }
                boEnt.setGendb(1);
                update(boEnt);
            }
        }catch (Exception e){
            return  new JsonResult(true,"生成表单失败!");
        }
        return  new JsonResult(true,"生成表单成功!");
    }

    public JsonResult deleteBoTable(String pkId,String tableName){
        try{
            FormBoEntity boEnt=getById(pkId);
            ITableOperator tableOperator=OperatorContext.getByDsAlias(boEnt.getDsAlias());
            tableOperator.dropTable(tableName);
            boEnt.setGendb(0);
            update(boEnt);
        }catch(Exception e){
            return  new JsonResult(true,"删除表单失败!");
        }
        return  new JsonResult(true,"删除表单成功!");
    }

    /**
     * @Description:  应用导入实体
     * @param boEnt 实体对象
     * @param  isInsert 是否插入或更新
     * @return boolean 成功则返回true;
     * @Author: Elwin ZHANG  @Date: 2021/8/3 10:19
     **/
    public  JsonResult importBoEnt(FormBoEntity boEnt,boolean isInsert) throws Exception {
        boolean isEntExist=isExist(boEnt);
        if(isEntExist){
            String message="应用导入时发现同名的实体！";
            return  JsonResult.Fail(message);
        }
        if(StringUtils.isEmpty(boEnt.getIdField())){
            //默认ID_
            boEnt.setIdField(FormBoEntity.FIELD_PK);
        }
        if(StringUtils.isEmpty(boEnt.getParentField())){
            //默认PARENT_ID_
            boEnt.setParentField(FormBoEntity.FIELD_PARENTID);
        }
        JsonResult result=null;
        String genMode=boEnt.getGenMode();
        //添加BO
        if(isInsert){
            if(FormBoEntity.GENMODE_FORM.equals(genMode) || FormBoEntity.GENMODE_EASYFORM.equals(genMode)){
                //先把表单创建的实体也生成物理表
                boEnt.setGenMode(FormBoEntity.GENMODE_CREATE);
                result = createEnt(boEnt);
                //再把表单创建的实体生成方式改回来
                if(result.isSuccess()){
                    boEnt.setGenMode(genMode);
                    formBoEntityMapper.updateById(boEnt);
                }
            }else {
                result = createEnt(boEnt);
            }
        }else {
            result = updEnt(boEnt, null,false);
        }
        return  result;
    }

    /**
     * 保存Bo实体
     * @param json
     * @return
     */
    public JsonResult saveBoEnt(JSONObject json,Boolean directlySave){
        JsonResult result=new JsonResult(true, "保存实体成功");
        try{
            FormBoEntity boEnt=JSONObject.toJavaObject(json, FormBoEntity.class);
            //检查是否更新
            boolean isUpd=StringUtils.isNotEmpty(boEnt.getId());

            if(isUpd){
                FormBoEntity sysBoEnt=this.get(boEnt.getId());
                boolean isEntExist=isExist(boEnt);
                boolean isMain=false;
                if(BeanUtil.isNotEmpty(sysBoEnt.getIsMain())){
                    isMain=sysBoEnt.getIsMain()==1;
                }
                if(isMain && isEntExist){
                    return new JsonResult(false,"【"+ boEnt.getAlias() +"】实体已存在!");
                }
            }
            else{
                boolean isEntExist=isExist(boEnt);
                if(isEntExist){
                    result=new JsonResult(false, "【"+ boEnt.getAlias() +"】实体已存在!");
                    return result;
                }
            }
            if(StringUtils.isEmpty(boEnt.getIdField())){
                //默认ID_
                boEnt.setIdField(FormBoEntity.FIELD_PK);
            }
            //添加BO
            if(StringUtils.isEmpty(boEnt.getId())){
                return createEnt(boEnt);
            }
            result=updEnt( boEnt, null,directlySave);

        }
        catch(Exception ex){
            String msg= ExceptionUtil.getExceptionMessage(ex);
            result=new JsonResult(false, msg);
        }

        return result;
    }

    /**
     * 判断业务实体是否存在。
     * @param formBoEntity
     * @return
     */
    public boolean isExist(FormBoEntity formBoEntity){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",formBoEntity.getAlias());
        if(StringUtils.isNotEmpty( formBoEntity.getId())){
            queryWrapper.ne("ID_",formBoEntity.getId());
        }
        int count=formBoEntityMapper.selectCount(queryWrapper);

        return count>0;
    }

    private JsonResult createEnt(FormBoEntity boEnt) throws Exception{
        ITableOperator tableOperator=OperatorContext.getByDsAlias(boEnt.getDsAlias());

        //生成物理表。
        if(FormBoEntity.GENMODE_CREATE.equals(boEnt.getGenMode()) && boEnt.getGendb()==1){
            if(tableOperator.isTableExist(boEnt.getTableName())){
                JsonResult result=new JsonResult(false,"【"+ boEnt.getTableName() +"】表已存在!");
                return result;
            }
            handEntity(boEnt);
            formPcServiceImpl.handTable(boEnt,true,false,true);
        }else {
            boolean handCreate = handCreateEnt(boEnt);
            //执行创建
            if(handCreate){
                formPcServiceImpl.handTable(boEnt,false,false,true);
            }
        }
        if(StringUtils.isEmpty(boEnt.getDsAlias())) {
            boEnt.setDsAlias(DataSourceUtil.LOCAL);
            boEnt.setDsName("默认");
        }
        boEnt.setIsMain(1);
        insert(boEnt);
        String appId=boEnt.getAppId();
        for(FormBoAttr attr:boEnt.getBoAttrList()) {
            attr.setEntId(boEnt.getId());
            attr.setAppId(appId);
            if (!boEnt.external()) {
                if(StringUtils.isNotEmpty(attr.getFieldName())){
                    attr.setFieldName(attr.getFieldName().toUpperCase());
                }else {
                    attr.setFieldName(TableUtil.getFieldName(attr.getName()));
                }
            }else {
                attr.setFieldName(attr.getFieldName().toUpperCase());
            }
            formBoAttrServiceImpl.insert(attr);
        }
        if(boEnt.getGendef()==1){
            try{
                FormBoDef formBoDef=new FormBoDef();
                formBoDef.setTreeId(boEnt.getTreeId());
                formBoDef.setAppId(appId);
                formBoDef.setName(boEnt.getName());
                formBoDef.setAlias(boEnt.getAlias());
                formBoDef.setSupportDb(1);
                formBoDef.setGenType(FormBoDef.GEN_TYPE_DIRECT);
                formBoDefServiceImpl.saveDef(formBoDef,boEnt.getId());
            }catch(Exception e){
                return new JsonResult(true,"实体成功创建!");
            }
            return new JsonResult(true,"实体及模型成功创建!");
        }
        return new JsonResult(true,"实体成功创建!");
    }

    private JsonResult<String> updEnt(FormBoEntity boEnt, FormBoEntity sourceBoEnt,Boolean directlySave) throws Exception {
        JsonResult result=JsonResult.Success("保存实体成功!");
        FormBoEntity oldBoEnt = null;
        if(sourceBoEnt == null) {
            oldBoEnt=this.getByEntId(boEnt.getId());
        } else {
            oldBoEnt = sourceBoEnt;
        }

        if(boEnt.getGendb()==1){
            if(directlySave){
                //更新bo实体的数据。
                updBoEnt(oldBoEnt, boEnt);
            }else {
                boEnt.setOriginEnt(oldBoEnt);
                handEntity(boEnt);
                List<AlterSql> delaySqls =formPcServiceImpl.handTable(boEnt,true,false,true);
                if(delaySqls.size()>0){
                    result.setData(delaySqls);
                    result.setShow(false);
                }else {
                    //更新bo实体的数据。
                    updBoEnt(oldBoEnt, boEnt);
                }
            }
        }
        else{
            updBoEnt( oldBoEnt, boEnt);
        }
        return result;
    }

    private void updBoEnt(FormBoEntity oldBoEnt ,FormBoEntity boEnt){
        boEnt.setDsAlias(oldBoEnt.getDsAlias());
        boEnt.setDsName(oldBoEnt.getDsName());
        oldBoEnt.setName(boEnt.getName());
        oldBoEnt.setTreeId(boEnt.getTreeId());
        oldBoEnt.setIdField(boEnt.getIdField());
        oldBoEnt.setParentField(boEnt.getParentField());
        oldBoEnt.setVersionField(boEnt.getVersionField());
        oldBoEnt.setGendb(boEnt.getGendb());
        oldBoEnt.setAlias(boEnt.getAlias());
        oldBoEnt.setTableName(boEnt.getTableName());
        //清空暂存数据
        oldBoEnt.setBoAttrTemp("");
        if(!boEnt.external()){
            oldBoEnt.setTableName(boEnt.getTableName());
        }
        String appId=boEnt.getAppId();
        update(oldBoEnt);
        formBoAttrServiceImpl.delByEntId(boEnt.getId());
        for(FormBoAttr attr:boEnt.getBoAttrList()) {
            //删除的字段则不添加
            if(!TableUtil.OP_DEL.equals(attr.getType())){
                if (!boEnt.external()) {
                    if(StringUtils.isNotEmpty(attr.getFieldName())){
                        attr.setFieldName(attr.getFieldName().toUpperCase());
                    }else {
                        attr.setFieldName(TableUtil.getFieldName(attr.getName()));
                    }
                }else {
                    attr.setFieldName(attr.getFieldName().toUpperCase());
                }
                attr.setEntId(boEnt.getId());
                attr.setAppId(appId);
                formBoAttrServiceImpl.insert(attr);
            }
        }
    }

    public void removeAttr(String attrId){
        FormBoAttr attr= formBoAttrServiceImpl.getById(attrId);
        FormBoEntity boEnt= getById( attr.getEntId());
        ITableOperator tableOperator=OperatorContext.getByDsAlias(boEnt.getDsAlias());
        //删除列
        if(boEnt.getGendb()==1){
            tableOperator.dropColumn(boEnt.getTableName(), attr.getFieldName());
        }
        formBoAttrServiceImpl.delete(attrId);
    }

    public FormBoEntity getByBoDefIdExcludeRefFields(String boDefId){
        FormBoEntity boEntity=getByDefId(boDefId, true);
        removeRefFields(boEntity,boDefId);
        return boEntity;
    }

    public void removeEntRefFields(FormBoEntity boEntity){
        removeRefFields(boEntity);
        List<FormBoEntity> ents=boEntity.getBoEntityList();
        for(FormBoEntity ent:ents){
            removeRefFields(ent);
        }
    }

    private void removeRefFields(FormBoEntity boEntity){
        if(boEntity==null || !boEntity.external()){
            return;
        }
        Iterator<FormBoAttr> it = boEntity.getBoAttrList().iterator();

        while (it.hasNext()) {
            FormBoAttr attr = it.next();
            if ("rx-ref".equals(attr.getControl()) || "rx-commonfield".equals(attr.getControl()) || attr.getFieldName().equals(boEntity.getIdField())
                    || attr.getFieldName().equals(boEntity.getParentField()) || attr.getFieldName().equals(boEntity.getVersionField()) || (boEntity.getBoRelation()!=null && attr.getFieldName().equals(boEntity.getBoRelation().getFkField()))) {
                it.remove();
            }
        }
    }

    public void removeRefFields(FormBoEntity boEntity, String boDefId){
        if(boEntity==null || !boEntity.external()){
            return;
        }
        Iterator<FormBoAttr> it = boEntity.getBoAttrList().iterator();
        FormBoRelation sysBoRelation= formBoRelationServiceImpl.getByDefEntId(boDefId,boEntity.getId());
        while (it.hasNext()) {
            FormBoAttr attr = it.next();
            //若为引用的外键
            if(sysBoRelation!=null && !FormBoRelation.RELATION_MAIN.equals(sysBoRelation.getType())) {
                it.remove();
                continue;
            }
            //标准字段或引用字段
            if ("rx-ref".equals(attr.getControl()) || "rx-commonfield".equals(attr.getControl()) || attr.getFieldName().equals(boEntity.getIdField())
                    || attr.getFieldName().equals(boEntity.getParentField()) || attr.getFieldName().equals(boEntity.getVersionField()) || (boEntity.getBoRelation()!=null && attr.getFieldName().equals(boEntity.getBoRelation().getFkField()))) {
                it.remove();
            }
        }
    }

    /**
     * 获取审批意见配置
     * @param alias
     * @param opinionArray
     * @param opinionSetting
     * @param boEntity
     * @return
     */
    public JSONArray getOpinionSettingByBoEntity(String alias,JSONArray opinionArray,JSONArray opinionSetting,FormBoEntity boEntity){
        JSONArray resultData=new JSONArray();
        //主表
        JSONObject mainJson= getOpinionSetting(opinionArray,opinionSetting,boEntity);
        resultData.add(mainJson);

        //计算子表
        List<FormBoEntity> subBoEntityList = boEntity.getBoEntityList();
        for(FormBoEntity subBoEnt:subBoEntityList){
            if(subBoEnt.getName().equals(alias)){
                JSONObject subJson = getOpinionSetting(opinionArray,opinionSetting,subBoEnt);
                resultData.add(subJson);
            }
        }
        return resultData;
    }

    /**
     * 获取审批意见控件字段配置
     * @param opinionArray 数据库中读取的意见字段
     * @param opinionSetting 原数据配置
     * @param boEntity
     * @return
     */
    public JSONObject getOpinionSetting(JSONArray opinionArray, JSONArray opinionSetting,FormBoEntity boEntity) {
        JSONObject resultObject=new JSONObject();
        resultObject.put("name", boEntity.getName());
        resultObject.put("alias", boEntity.getAlias());

        JSONArray newArray =new JSONArray();
        if(BeanUtil.isNotEmpty(opinionArray)){
            for(int i=0;i<opinionArray.size();i++){
                JSONObject opinionObj = opinionArray.getJSONObject(i);
                String name = opinionObj.getString("name");
                if(opinionSetting.size()>0){
                    JSONObject opinionSettingObj = opinionSetting.getJSONObject(0);
                    String alias = (String) opinionSettingObj.get("alias");
                    if(boEntity.getAlias().equals(alias)) {
                        JSONArray attrData=opinionSettingObj.getJSONArray("attrData");
                        JSONObject newObject=new JSONObject();
                        newObject.put("name",name);
                        newObject.put("comment",opinionObj.getString("comment"));
                        newObject.put("setOpinion",false);
                        for(int j=0;j<attrData.size();j++){
                            JSONObject attr = attrData.getJSONObject(j);
                            if(attr.get("name").equals(name)){
                                newObject.put("setOpinion",attr.getBoolean("setOpinion"));
                            }
                        }
                        newArray.add(newObject);
                    }
                }else {
                    JSONObject newObject=new JSONObject();
                    newObject.put("name",name);
                    newObject.put("comment",opinionObj.getString("comment"));
                    newObject.put("setOpinion",false);
                    newArray.add(newObject);
                }
            }
        }
        resultObject.put("attrData",newArray);
        return resultObject;
    }


    public FormBoEntity getByTableName(String tableName) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TABLE_NAME_",tableName);
        return formBoEntityMapper.selectOne(queryWrapper);
    }

    public JsonResult setFormInstStatus(JSONArray jsonArray) {
        CommonDao commonDao=new CommonDao();
        JsonResult result=new JsonResult(true, "修改成功成功");
        List<JSONObject> list = jsonArray.toJavaList(JSONObject.class);
        for (JSONObject jsonObject : list) {
            String pk = jsonObject.getString("pk");
            FormBoEntity formBoEntity = formBoEntityMapper.getMainEntByBoAlias(jsonObject.getString("bodefAlias"));
            String tableName = formBoEntity.getTableName();
            String sql="UPDATE "+tableName+" SET INST_STATUS_='DELETE' WHERE ID_='"+pk+"'";
            commonDao.execute(formBoEntity.getDsAlias(), sql);
        }
        return result;
    }

    /**
     * @Description:  将PC表单的metadata中的字段属性配置注入到实体的属性中;不然导入表单方案时遇到地址控件会报错
     * @param boEntity 业务实体对象
     * @Author: Elwin ZHANG  @Date: 2021/8/23 10:43
     **/
    public void injectMetaData4Attrs(FormBoEntity boEntity){
        if(boEntity==null ){
            return;
        }
        try {
            //获取实体的字段属性
            List<FormBoAttr> attrs = boEntity.getBoAttrList();
            if (attrs == null || attrs.size() == 0) {
                return;
            }
            //获取相关PC表单的FORM_SETTING_字段
            FormPc formPc = formPcServiceImpl.getByEntityId(boEntity.getId());
            if (formPc == null) {
                return;
            }
            if(FormPc.FORM_TYPE_EASY_DESIGN.equals( formPc.getType())){
                return;
            }

            String strFormSettings = formPc.getFormSettings();
            JSONObject formSettings = JSONObject.parseObject(strFormSettings);
            String key="main";
            if(BeanUtil.isNotEmpty(boEntity.getIsMain()) && boEntity.getIsMain()==0){
                key=boEntity.getAlias();
            }
            JSONObject settings=formSettings.getJSONObject(key);
            //循环将表单配置注入到相关属性中
            for (FormBoAttr attr:attrs  ) {
                JSONObject oldJson=attr.getFormJson();
                JSONObject newJson=settings.getJSONObject(attr.getName());
                if(oldJson==null && newJson!=null){
                    attr.setFormJson(newJson);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 处理创建时新增的字段
     * @param formBoEntity
     */
    private boolean handCreateEnt(FormBoEntity  formBoEntity){
        boolean handCreate=false;
        List<FormBoAttr> attrList = formBoEntity.getBoAttrList();
        for (int i = 0; i < attrList.size(); i++) {
            if("add".equals(attrList.get(i).getType())){
                handCreate=true;
                break;
            }
        }
        return handCreate;
    }

    /**
     * 去除冗余字段
     * @param formBoAttrs
     * @return
     */
    public  List<FormBoAttr> delRedundantFields(List<FormBoAttr> formBoAttrs){
        List<FormBoAttr> newFormBoAttrs=new ArrayList<>();
        if(BeanUtil.isEmpty(formBoAttrs) || formBoAttrs.size()==0){
            return newFormBoAttrs;
        }
        for (int i = 0; i < formBoAttrs.size(); i++) {
            FormBoAttr formBoAttr=formBoAttrs.get(i);
            //将引用字段与标准字段的去除
            if(!"rx-ref".equals(formBoAttr.getControl()) && !"rx-commonfield".equals(formBoAttr.getControl())){
                newFormBoAttrs.add(formBoAttr);
            }
        }
        return newFormBoAttrs;
    }

    /**
     * 去掉标准字段与引用字段
     * @param formBoEntity
     * @return
     */
    public FormBoEntity getFormBoAttrByEnt(FormBoEntity  formBoEntity){
        formBoEntity.setBoAttrList(delRedundantFields(formBoEntity.getBoAttrList()));
        List<Tree> children = formBoEntity.getChildren();
        if(BeanUtil.isEmpty(children) || children.size()==0){
            return formBoEntity;
        }
        for (int i = 0; i < children.size(); i++) {
            FormBoEntity entity = (FormBoEntity) children.get(i);
            entity.setBoAttrList(delRedundantFields(entity.getBoAttrList()));
        }
        return formBoEntity;
    }

    //实体暂存
    public JsonResult temporarySave(JSONObject json) {
        JsonResult result=new JsonResult(true, "实体暂存成功");
        FormBoEntity boEnt=JSONObject.toJavaObject(json, FormBoEntity.class);
        //新增
        if(StringUtils.isEmpty(boEnt.getId())){
            boolean isEntExist=isExist(boEnt);
            if(isEntExist){
                result=new JsonResult(false, "【"+ boEnt.getAlias() +"】实体已存在!");
                return result;
            }
            List<FormBoAttr> boAttrList = boEnt.getBoAttrList();
            if(BeanUtil.isNotEmpty(boAttrList) && boAttrList.size()>0){
                boEnt.setBoAttrTemp(JSON.toJSONString(boAttrList));
            }
            int genDb=0;
            if(FormBoEntity.GENMODE_DB.equals(boEnt.getGenMode())){
                genDb=1;
            }
            boEnt.setGendb(genDb);
            if(StringUtils.isEmpty(boEnt.getDsAlias())) {
                boEnt.setDsAlias(DataSourceUtil.LOCAL);
                boEnt.setDsName("默认");
            }
            insert(boEnt);
        }else {
            FormBoEntity formBoEntity = formBoEntityMapper.selectById(boEnt.getId());
            //当未生成表时，才设置别名
            if(formBoEntity.getGendb()==0){
               formBoEntity.setAlias(boEnt.getAlias());
               formBoEntity.setDsAlias(boEnt.getDsAlias());
               formBoEntity.setDsName(boEnt.getDsName());
            }
            //仅允许设置其分类跟表名
            formBoEntity.setTreeId(boEnt.getTreeId());
            formBoEntity.setName(boEnt.getName());

            List<FormBoAttr> boAttrList = boEnt.getBoAttrList();
            if(BeanUtil.isNotEmpty(boAttrList) && boAttrList.size()>0){
                formBoEntity.setBoAttrTemp(JSON.toJSONString(boAttrList));
            }
            update(formBoEntity);
        }
        return result.setData(boEnt);
    }
}
