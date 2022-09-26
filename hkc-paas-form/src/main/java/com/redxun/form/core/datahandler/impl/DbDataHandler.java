package com.redxun.form.core.datahandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.db.CommonDao;
import com.redxun.dto.form.DataResult;
import com.redxun.dto.form.FormConst;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.form.bo.entity.*;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.datahandler.IDataHandler;
import com.redxun.form.core.datahandler.ISqlBuilder;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.AttrHandlerContext;
import com.redxun.form.core.service.FormHelper;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.core.service.IAttrHandler;
import com.redxun.form.core.service.easyImpl.AttrEasyHandlerContext;
import com.redxun.form.core.service.easyImpl.IAttrEasyHandler;
import com.redxun.form.util.FormUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DbDataHandler implements IDataHandler {



    @Resource
    private FormPcServiceImpl formPcServiceImpl;
    @Resource
    private FormBoDefServiceImpl boDefService;
    @Resource
    private FormBoEntityServiceImpl boEntityService;

    @Resource
    CommonDao commonDao;
    @Resource
    ISqlBuilder sqlBuilder;

    @Resource
    private OsInstClient osInstClient;

    @Override
    public JSONObject getDataById( String boAlias,String pk) {
        FormBoEntity boEntity= this.getByBoAlias(boAlias);
        JSONObject json=getByFormBoEntity(boEntity,pk);
        return json;
    }

    @Override
    public List<JSONObject> getData(String formAlias, String parentId) {
        FormBoEntity boEnt = getByFormAlias(formAlias);
        String sql="";
        String tableName=boEnt.getTableName();
        String parentField=getParentField(boEnt);
        String pkField=getPkField(boEnt);
        if(StringUtils.isEmpty(parentId)){
            sql="select * from " + tableName ;
        }
        else{
            sql="SELECT a.*,(select count(*) from "+ tableName +" b where b."+parentField+"=a."+
                    pkField+" ) CHILDS_AMOUNT_  from "+tableName+" a  where a." + parentField+"=#{w."+parentField+"}" ;
        }

        SqlModel sqlModel=new SqlModel(sql);
        String dsAlias = getDatasource(boEnt);
        sqlModel.setDsName(dsAlias);

        if(StringUtils.isNotEmpty(parentId)){
            sqlModel.addParam(parentField, parentId);
        }

        List<Map<String,Object>> list;
        if(StringUtils.isEmpty(sqlModel.getDsName())){
            list=commonDao.query(sqlModel);
        }
        else{
            list=commonDao.query(sqlModel.getDsName(),sqlModel);
        }
        List<JSONObject> rtnList=new ArrayList<>();
        for(Map map:list){
            JSONObject json=getJsonByRow(boEnt,map);
            if(map.containsKey("CHILDS_AMOUNT_")){
                Integer amount=Integer.parseInt(map.get("CHILDS_AMOUNT_").toString());
                boolean isLeaf=amount==0;
                json.put("isLeaf",isLeaf);
            }
            rtnList.add(json);
        }
        return  rtnList;
    }

    @Override
    public JSONObject getInitData(String formAlias) {
        FormBoEntity boEntity = getByFormAlias(formAlias);

        return getInit(boEntity,true);
    }

    @Override
    public JSONObject getInitDataByBoAlias(String boAlias){
        FormBoEntity boEntity= getByBoAlias(boAlias);
        return getInit(boEntity,false);
    }

    private JSONObject getInit(FormBoEntity boEntity,boolean init){
        JSONObject initJson= getByBoEntity( boEntity,init);

        JSONObject initData=new JSONObject();
        for(FormBoEntity entity:boEntity.getBoEntityList()){
            JSONObject json= getByBoEntity( entity,init);
            if(entity.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOONE)){
                initJson.put(FormConst.SUB_PRE + entity.getAlias(),json);
            }
            else{
                JSONArray array=new JSONArray();
                initData.put(entity.getAlias(),json);
                initJson.put(FormConst.SUB_PRE + entity.getAlias(),array);
            }
        }
        if(initData.size()>0){
            initJson.put(FormConst.INITDATA,initData);
        }
        return initJson;
    }

    @Override
    public JSONObject getById( String formAlias,String pk) {
        FormBoEntity boEntity= this.getByFormAlias(formAlias);
        JSONObject json=getByFormBoEntity(boEntity,pk);
        return json;
    }


    @Override
    public JSONObject getByParams(String formAlias, Map<String, Object> params) {
        if(BeanUtil.isEmpty(params)){
            return null;
        }
        FormBoEntity boEntity= this.getByFormAlias(formAlias);
        JSONObject mainJson= getByParams(params,boEntity);
        if(mainJson==null){
            return null;
        }
        String pkField=getPkField(boEntity);
        String pk=mainJson.getString(pkField);

        getSubData(pk,boEntity,mainJson);

        return mainJson;
    }

    @Override
    public DataResult save(JSONObject data,String formAlias, FormBoEntity boEntity,boolean isResume) {
        String boAlias= boDefService.getAliasByFormAlias(formAlias);

        IUser user=ContextUtil.getCurrentUser();
        DataHolder dataHolder=new DataHolder();
        //是否外部表。
        DataResult result=null;
        String pkField=getPkField(boEntity);
        for(FormBoEntity subEnt: boEntity.getBoEntityList()) {
            String key = FormConst.SUB_PRE + subEnt.getAlias();
            //判断数据是否有子表数据。
            if (!data.containsKey(key)) {
                continue;
            }
            if(subEnt.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOMANY)){
                JSONArray subAry=data.getJSONArray(key);
                JSONArray treeSubAry=FormUtil.listToTree(subAry,"index_","pid_",FormBoRelation.CHILDREN);
                data.put(key,treeSubAry);
                subEnt.setTree(1);
            }
        }
        if(!data.containsKey(pkField) || StringUtils.isEmpty(data.getString(pkField))){
           dataHolder.setAction(DataHolder.ACTION_NEW);
           String pk= handAdd( boEntity,  data,user,dataHolder);
           result=DataResult.getResult(pk,DataResult.ACTION_ADD);
        }
        else{
            dataHolder.setAction(DataHolder.ACTION_UPD);
            String pk=handUpd(boEntity,data,pkField,user,dataHolder,isResume);
            result = DataResult.getResult(pk, DataResult.ACTION_UPD);
        }
        result.setBoAlias(boAlias);

        //发布事件。
        SpringUtil.publishEvent(new DataHolderEvent(dataHolder));

        return result;
    }

    @Override
    public JsonResult removeById(String formAlias, String pk) {
        FormBoEntity ent = getByFormAlias(formAlias);
        JSONObject jsonObj = getById(formAlias, pk);
        //删除时发布事件。
        DataHolder dataHolder = convertToDataHolder(jsonObj);
        SpringUtil.publishEvent(new DataHolderEvent(dataHolder));

        List<String> list = getData(ent, pk);
        for (String id : list) {
            delByPkId(ent, id);
        }

        return JsonResult.Success();
    }

    private void delByPkId(FormBoEntity ent, String pkId) {
        //逻辑删除
        if(DbLogicDelete.getLogicDelete()){
            //刪除子表
            logicDelSubById(ent, pkId);
            //根据主键删除
            logicDelById(ent, pkId);
        }else {
            //刪除子表
            delSubById(ent, pkId);
            //根据主键删除
            delById(ent, pkId);
        }
    }

    /**
     * 删除主表记录-逻辑删除。
     *
     * @param ent
     * @param id
     */
    private void logicDelById(FormBoEntity ent, String id) {
        String pkField = getPkField(ent);
        String sql = " UPDATE " + ent.getTableName() + " SET DELETED_=1 where " + pkField + "=#{" + pkField + "}";
        SqlModel model = new SqlModel(sql);
        model.addParam(pkField, id);
        //获取租户配置的数据源
        String dsAlias=getDatasource(ent);
        commonDao.execute(dsAlias, model);
    }


    /**
     * 删除子表记录-逻辑删除。
     *
     * @param ent
     * @param id
     */
    private void logicDelSubById(FormBoEntity ent, String id) {
        if (BeanUtil.isEmpty(ent.getBoEntityList())) {
            return;
        }
        //获取租户配置的数据源
        String dsAlias=getDatasource(ent);
        for (FormBoEntity subEnt : ent.getBoEntityList()) {
            String fkField = getFkField(subEnt);
            if (StringUtils.isEmpty(fkField)) {
                continue;
            }
            String sql = " UPDATE " + subEnt.getTableName() + " SET DELETED_=1  where " + fkField + "=#{" + fkField + "}";
            SqlModel model = new SqlModel(sql);
            model.addParam(fkField, id);
            commonDao.execute(dsAlias, model);
        }
    }

    /**
     * 删除子表记录。
     *
     * @param ent
     * @param id
     */
    private void delSubById(FormBoEntity ent, String id) {
        if (BeanUtil.isEmpty(ent.getBoEntityList())) {
            return;
        }
        //获取租户配置的数据源
        String dsAlias=getDatasource(ent);
        for (FormBoEntity subEnt : ent.getBoEntityList()) {
            String fkField = getFkField(subEnt);
            if (StringUtils.isEmpty(fkField)) {
                continue;
            }
            String sql = " delete from " + subEnt.getTableName() + " where " + fkField + "=#{" + fkField + "}";
            SqlModel model = new SqlModel(sql);
            model.addParam(fkField, id);
            commonDao.execute(dsAlias, model);
        }
    }

    private void delById(FormBoEntity ent, String id) {
        String pkField = getPkField(ent);
        String sql = " delete from " + ent.getTableName() + " where " + pkField + "=#{" + pkField + "}";
        SqlModel model = new SqlModel(sql);
        model.addParam(pkField, id);
        //获取租户配置的数据源
        String dsAlias=getDatasource(ent);
        commonDao.execute(dsAlias, model);
    }

    private List<String> getData(FormBoEntity boEnt, String id) {
        List<String> rtnList = new ArrayList<>();
        rtnList.add(id);
        //递归访问
        getData(boEnt, id, rtnList);

        return rtnList;
    }

    private void getData(FormBoEntity boEnt, String id, List<String> rtnList) {
        List<String> list = getListByPid(boEnt, id);
        if (BeanUtil.isEmpty(list)) {
            return;
        }
        rtnList.addAll(list);

        for (String pId : list) {
            getData(boEnt, pId, rtnList);
        }
    }


    private List<String> getListByPid(FormBoEntity boEnt, String pid) {
        List<String> rtnList = new ArrayList<String>();
        String fkField = boEnt.getParentField();
        if (StringUtils.isEmpty(fkField)) {
            return rtnList;
        }

        String pkField = boEnt.getIdField();

        String sql = "select " + pkField + " from " + boEnt.getTableName() + " where " + fkField + "=#{" + pkField + "}";
        SqlModel model = new SqlModel(sql);
        model.addParam(pkField, pid);
        String dsAlias = getDatasource(boEnt);
        List<Map<String, Object>> list = (List<Map<String, Object>>) commonDao.query(dsAlias, model);

        for (Map<String, Object> row : list) {
            String tmp = String.valueOf(row.get(pkField));
            rtnList.add(tmp);
        }
        return rtnList;
    }

    /**
     * 根据数据构建 DataHolder
     *
     * @param jsonObj
     * @return
     */
    private DataHolder convertToDataHolder(JSONObject jsonObj) {
        DataHolder dataHolder = new DataHolder();
        dataHolder.setAction(DataHolder.ACTION_DEL);
        if(BeanUtil.isEmpty(jsonObj)){
            return dataHolder;
        }
        dataHolder.setCurMain(jsonObj);
        Set<String> keySet = jsonObj.keySet();
        for (String key : keySet) {
            if (!key.startsWith(FormConst.SUB_PRE)) {
                continue;
            }
            String entName = key.substring(FormConst.SUB_PRE.length());
            Object obj = jsonObj.get(key);
            SubDataHolder subHolder = new SubDataHolder();
            if (obj instanceof JSONObject) {
                subHolder.addDelList((JSONObject) obj);
            } else {
                JSONArray ary = (JSONArray) obj;
                subHolder.addDelJsonAry(ary);
            }
            dataHolder.addSubData(entName, subHolder);
        }
        return dataHolder;
    }

    private JSONObject getByFormBoEntity(FormBoEntity boEntity, String pk) {
        //读取主表记录。
        JSONObject json = getByPk(pk, boEntity);
        //子表处理
        if(json!=null) {
            getSubData(pk, boEntity, json);
        }
        return json;
    }

    private void getSubData(String pk,FormBoEntity boEntity,JSONObject json){
        //子表处理
        List<FormBoEntity> list = boEntity.getBoEntityList();
        if(BeanUtil.isEmpty(list)){
            return;
        }

        JSONObject initData=new JSONObject();
        for(FormBoEntity subEnt:list){
            if(subEnt.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOONE)){
                JSONObject jsonObject=getOneToOneByFk(subEnt,pk);
                json.put(FormConst.SUB_PRE + subEnt.getAlias(),jsonObject);
            }
            else{
                JSONArray subList=getByFk(subEnt,pk);
                if(subEnt.tree()){
                    String pkField=getPkField(subEnt);
                    subList=FormUtil.listToTree(subList,pkField,FormBoEntity.FIELD_PARENTID,FormBoRelation.CHILDREN);
                }
                json.put(FormConst.SUB_PRE + subEnt.getAlias(),subList);
            }
            JSONObject initJson= getByBoEntity( subEnt,true);
            initData.put(subEnt.getAlias(),initJson);
        }
        json.put(FormConst.INITDATA,initData);

    }



    private Map<String,FormBoAttr>  convertAttrMap(FormBoEntity boEntity){
        List<FormBoAttr> list=boEntity.getBoAttrList();
        Map<String,FormBoAttr> attrMap=list.stream().filter(attr -> ! FormHelper.excludeCtlList.contains(attr.getControl())).collect(Collectors.toMap(p->p.getName(), p -> p));
        return attrMap;
    }

    private String  handUpd(FormBoEntity boEntity,JSONObject curJson,String pkField,IUser curUser,DataHolder dataHolder,boolean isResume){
        String pk=curJson.getString(pkField);
        JSONObject originJson= getByFormBoEntity(boEntity,  pk);
        if(isResume && originJson==null){
            originJson=curJson;
        }

        //更新主表
        Map<String,FormBoAttr> attrMap=convertAttrMap(boEntity);
        handUpdRow(boEntity,  attrMap, curJson , curUser,isResume);

        //设置表间公式更新数据处理。
        dataHolder.setCurMain(curJson);
        dataHolder.setOriginMain(originJson);
        dataHolder.setMainTable(boEntity.getTableName());
        dataHolder.setNewPk(pk);

        //更新子表
        if(BeanUtil.isEmpty(boEntity.getBoEntityList())){
            return pk;
        }

        for(FormBoEntity subEnt: boEntity.getBoEntityList()){
            String key=FormConst.SUB_PRE + subEnt.getAlias();
            //判断数据是否有子表数据。
            if(!curJson.containsKey(key)){
                continue;
            }
            Map<String,FormBoAttr> subAttrMap= convertAttrMap( subEnt);
            //处理一对一的数据。
            if(subEnt.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOONE)){
                JSONObject jsonRow= curJson.getJSONObject(key);
                String subPkField=getPkField(subEnt);
                if(jsonRow.containsKey(subPkField)){
                    String subPk=jsonRow.getString(subPkField);
                    JSONObject subOriginJson=getByPk(subPk,subEnt);
                    handUpdRow(subEnt,  subAttrMap, jsonRow, curUser,isResume);

                    //处理一对一子表表间公式数据。
                    SubDataHolder subDataHolder=new SubDataHolder();
                    subDataHolder.setOneToMany(false);
                    subDataHolder.setTableName(subEnt.getName());
                    UpdJsonEnt updJsonEnt=new UpdJsonEnt();
                    updJsonEnt.setCurJson(subOriginJson);
                    updJsonEnt.setOriginJson(jsonRow);
                    subDataHolder.addUpdJsonEnt(updJsonEnt);
                }
                else{
                    handInsert(subEnt,curJson,jsonRow,pk,"0",curUser);
                    //表间公式处理子表添加一对一。
                    addSubOneToOne(dataHolder,subEnt,jsonRow);
                }

            }
            //处理一对多的数据。
            else{
                //处理一对多情况的表间公式的数据更新。
                SubDataHolder subDataHolder=new SubDataHolder();
                subDataHolder.setOneToMany(true);
                subDataHolder.setTableName(subEnt.getName());

                JSONArray originAry =originJson.getJSONArray(key);
                JSONArray curAry =curJson.getJSONArray(key);
                handUpdRows(subEnt,subAttrMap,curAry,originAry,pk,curUser,subDataHolder,isResume);

                dataHolder.addSubData(subEnt.getAlias(),subDataHolder);
            }
        }
        return  pk;
    }

    /**
     * 处理更新。
     * @param boEntity
     * @param attrMap
     * @param curAry
     * @param originAry
     * @param refId
     * @param curUser
     * @param isResume
     */
    private void handUpdRows(FormBoEntity boEntity, Map<String,FormBoAttr> attrMap,
                             JSONArray curAry,JSONArray originAry,String refId,IUser curUser,SubDataHolder subDataHolder,boolean isResume){
        Map<String,JSONArray> changeMap= handChange(boEntity,curAry,originAry);
        //处理新增的数据
        if(changeMap.containsKey(TableUtil.OP_ADD)){
            JSONArray jsonArray=changeMap.get(TableUtil.OP_ADD);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                handRecursionInsert(boEntity ,json, json, refId,"0",curUser,subDataHolder);
            }
        }
        //处理更新的数据。
        if(changeMap.containsKey(TableUtil.OP_UPD)){
            JSONArray jsonArray=changeMap.get(TableUtil.OP_UPD);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject rowJson=jsonArray.getJSONObject(i);
                handRecursionUpd(boEntity,attrMap, rowJson,refId,curUser,subDataHolder,isResume);
            }
        }
        //删除记录
        if(changeMap.containsKey(TableUtil.OP_DEL)){
            JSONArray jsonArray=changeMap.get(TableUtil.OP_DEL);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject json=jsonArray.getJSONObject(i);
                handRecursionDel(boEntity,json,subDataHolder);
            }
        }
    }

    private void  handRecursionUpd(FormBoEntity boEntity,Map<String,FormBoAttr> attrMap, JSONObject rowJson,String refId,IUser curUser,SubDataHolder subDataHolder,boolean isResume){
        handUpdRow( boEntity,  attrMap,  rowJson, curUser,isResume);

        // 处理数据更新时表间公式数据。
        String pkField=getPkField(boEntity);
        UpdJsonEnt updJsonEnt = new UpdJsonEnt();
        updJsonEnt.setOriginJson(rowJson.getJSONObject(FormConst.ORIGIN));
        updJsonEnt.setCurJson(rowJson);
        updJsonEnt.setPk(rowJson.getString(pkField));
        subDataHolder.addUpdJsonEnt(updJsonEnt);


        if(!boEntity.tree() || !rowJson.containsKey(FormBoRelation.CHILDREN)){
            return;
        }
        JSONArray curAry= rowJson.getJSONArray(FormBoRelation.CHILDREN);
        JSONArray originAry= rowJson.getJSONObject(FormConst.ORIGIN).getJSONArray(FormBoRelation.CHILDREN);
        // 递归处理。
        handUpdRows(boEntity, attrMap,curAry, originAry, refId,curUser,subDataHolder,isResume);
    }

    /**
     * 递归删除。
     * @param boEntity
     * @param jsonObject
     */
    private void handRecursionDel(FormBoEntity boEntity, JSONObject jsonObject,SubDataHolder subDataHolder){
        handDel(boEntity,jsonObject);

        //处理删除时的表间公式的数据。
        subDataHolder.addDelList(jsonObject);

        // 判断是否有子记录。
        if(!boEntity.tree() || !jsonObject.containsKey(FormBoRelation.CHILDREN)){
            return;
        }
        JSONArray ary=jsonObject.getJSONArray(FormBoRelation.CHILDREN);
        for(int i=0;i<ary.size();i++){
            JSONObject json=ary.getJSONObject(i);
            handRecursionDel(boEntity,json,subDataHolder);
        }
    }



    private  JSONObject getByBoEntity(FormBoEntity boEntity,boolean initData){
        JSONObject jsonObject=new JSONObject();
        List<FormBoAttr> attrList=boEntity.getBoAttrList();
        for(FormBoAttr attr:attrList){
            if(FormHelper.excludeCtlList.contains(attr.getControl())){
                continue;
            }
            if(initData){
                if(FormBoEntity.GENMODE_EASYFORM.equals(boEntity.getGenMode())){
                    IAttrEasyHandler attrEasyHandler = AttrEasyHandlerContext.getAttrHandler(attr.getControl());
                    attrEasyHandler.getInitData(attr,jsonObject);
                }else {
                    IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(attr.getControl());
                    attrHandler.getInitData(attr, jsonObject);
                }
            }
            else{
                jsonObject.put(attr.getName(),"");
            }

        }
        return  jsonObject;
    }


    /**
     *
     * @param params
     * @param boEntity
     * @return
     */
    private JSONObject getByParams(Map<String,Object> params,FormBoEntity boEntity){
        String sql="select * from " + boEntity.getTableName() + " where ";

        List<String> listSql=new ArrayList<>();

        SqlModel sqlModel=new SqlModel();

        List<FormBoAttr> boAttrList = boEntity.getBoAttrList();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            //属性名称。
            String entryKey =entry.getKey();
            String fieldName= getEntryFieldName(boAttrList,entryKey);
            String tmp=fieldName +"=#{w." +entryKey +"}";
            sqlModel.addParam(entryKey,entry.getValue());
            listSql.add(tmp);
        }

        sql+= StringUtils.join(listSql," and ");
        sqlModel.setSql(sql);
        String dsAlias = getDatasource(boEntity);
        Map<String,Object> map =commonDao.queryForMap(dsAlias,sqlModel);

        if(map==null){
            return  null;
        }

        JSONObject jsonObject=getJsonByRow(boEntity, map);

        return  jsonObject;
    }

    private String getEntryFieldName(List<FormBoAttr> boAttrList,String entryKey){
        for (FormBoAttr attr:boAttrList) {
            if(entryKey.equals(attr.getName())){
                entryKey=attr.getFieldName();
                break;
            }
        }
        return entryKey;
    }

    /**
     * 检验字段的唯一性。
     * @param formKey
     * @param formKeyValue
     * @param pk
     * @param boEntity
     * @return
     */
    public boolean checkUniqueValue(String formKey,String formKeyValue,String pk,FormBoEntity boEntity){
        String sql="select count(*) as count from " + boEntity.getTableName() + " where ";
        sql+= formKey+"=#{formKey}";
        if(StringUtils.isNotEmpty(pk)){
            String pkField=getPkField(boEntity);
            sql+= " and "+pkField +"!=#{pk}";
        }

        SqlModel sqlModel=new SqlModel(sql);
        sqlModel.addParam("formKey",formKeyValue);
        if(StringUtils.isNotEmpty(pk)){
            sqlModel.addParam("pk",pk);
        }
        String dsAlias = getDatasource(boEntity);
        Long rtn = (Long) commonDao.queryOne(dsAlias,sqlModel);
        return  rtn==0;
    }

    @Override
    public JSONObject getByPk(String pk,FormBoEntity boEntity){
        String sql="select * from " + boEntity.getTableName() + " where ";
        String pkField=getPkField(boEntity);
        sql+= pkField +"=#{w.pk}";

        SqlModel sqlModel=new SqlModel(sql);
        sqlModel.addParam("pk",pk);
        String dsAlias = getDatasource(boEntity);
        Map<String,Object> map =commonDao.queryForMap(dsAlias,sqlModel);
        if(map==null){
            return  null;
        }
        JSONObject jsonObject=getJsonByRow(boEntity, map);

        return  jsonObject;
    }

    private JSONArray  getByFk(FormBoEntity boEntity,String pk){
        SqlModel sqlModel= sqlBuilder.getByFk(boEntity,pk);
        String dsAlias = getDatasource(boEntity);
        List list= commonDao.query(dsAlias,sqlModel);

        Map<String,FormBoAttr> attrMap=getAttrMap(boEntity);
        JSONArray rtnList=new JSONArray();
        for(Object row:list){
            Map<String,Object> rowMap=(Map<String,Object>)row;
            JSONObject jsonObject=getJsonByRow(boEntity,rowMap);
            jsonObject.put("index_",jsonObject.getString(boEntity.getIdField()));
            jsonObject.put("pid_",jsonObject.getString(boEntity.getParentField()));
            rtnList.add(jsonObject);
        }
        return rtnList;
    }


    private JSONObject getOneToOneByFk(FormBoEntity boEntity,String pk){
        SqlModel sqlModel= sqlBuilder.getByFk(boEntity,pk);
        String dsAlias = getDatasource(boEntity);
        Map<String,Object>  rowMap= commonDao.queryForMap(dsAlias,sqlModel);
        if(rowMap==null){
            return null;
        }
        JSONObject jsonObject=getJsonByRow(boEntity,rowMap);
        return jsonObject;
    }



    /**
     * 将属性转成Map对象数据。
     * <pre>
     *     键为 字段名称大写。
     * </pre>
     * @param boEntity
     * @return
     */
    private Map<String,FormBoAttr> getAttrMap(FormBoEntity boEntity){
        List<FormBoAttr> list=boEntity.getBoAttrList();
        return list.stream().filter(p->!FormHelper.excludeCtlList.contains(p.getControl())).collect(Collectors.toMap(p->p.getFieldName().toUpperCase() , p -> p));
    }

    private void handField(String field,Map<String,Object> rowData,JSONObject json){
        String fieldUp=field.toUpperCase();
        if(rowData.containsKey(fieldUp)){
            Object val=rowData.get(fieldUp);
            if(val instanceof Long){
                val=val.toString();
            }
            json.put(field, val);
            rowData.remove(fieldUp);
        }
    }

    /**
     * 将一行数据转成 JSONObject 对象。
     * @param boEntity
     * @param row
     * @param
     * @return
     */
    private JSONObject getJsonByRow(FormBoEntity boEntity, Map<String,Object> row){
        boolean external=boEntity.external();
        Map<String,Object> rowData=new HashMap<>();
        row.forEach((key,val)->{
            rowData.put(key.toUpperCase(),val);
        });
        List<FormBoAttr> boAttrs=boEntity.getBoAttrList();
        JSONObject json=new JSONObject();
        //主键
        String pkField=getPkField(boEntity);
        handField(pkField,rowData,json);
        //父ID
        String parentField=getParentField(boEntity);
        handField(parentField,rowData,json);

        //版本字段
        if(StringUtils.isNotEmpty(boEntity.getVersionField())){
            String versionField=boEntity.getVersionField();
            handField(versionField,rowData,json);
        }

        for(FormBoAttr attr:boAttrs){
            if(FormHelper.excludeCtlList.contains(attr.getControl())){
                //获取标准字段的映射字段
                if("default".equals(attr.getDataType())){
                    JSONObject extJsonObj = (JSONObject) JSONObject.parse(attr.getExtJson());
                    String mappingField = extJsonObj.getString("mappingField");
                    if(StringUtils.isNotEmpty(mappingField)){
                        IAttrEasyHandler attrEasyHandler = AttrEasyHandlerContext.getAttrHandler(attr.getControl());
                        ValueResult value = attrEasyHandler.getValue(attr,rowData,external);
                        attrEasyHandler.removeFields(attr,rowData,external);
                        if (value.isExist()) {
                            json.put(attr.getName(), value.getValue());
                        }
                    }
                }
                continue;
            }
            if(FormBoEntity.GENMODE_EASYFORM.equals(boEntity.getGenMode())){
                IAttrEasyHandler attrEasyHandler = AttrEasyHandlerContext.getAttrHandler(attr.getControl());
                ValueResult value = attrEasyHandler.getValue(attr,rowData,external);
                attrEasyHandler.removeFields(attr,rowData,external);
                if (value.isExist()) {
                    json.put(attr.getName(), value.getValue());
                }
            }else {
                IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(attr.getControl());
                //处理表单的值。
                ValueResult value = attrHandler.getValue(attr, rowData, external);
                attrHandler.removeFields(attr, rowData, external);
                if (value.isExist()) {
                    json.put(attr.getName(), value.getValue());
                }
            }
        }
        json.putAll(rowData);

        return  json;
    }

    private FormBoEntity getByBoAlias(String alias){
        FormBoDef  formBoDef=boDefService.getByAlias(alias);
        FormBoEntity boEntity= boEntityService.getByDefId(formBoDef.getId(),true);
        return boEntity;
    }



    private FormBoEntity getByFormAlias(String alias){
        FormPc formPc= formPcServiceImpl.getByAlias(alias);
        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= boEntityService.getByDefId(boDefId,true);
        //将字段配置从表单表单配置注入到表单属性中。
        FormHelper.injectAttrJson(boEntity,formPc.getFormSettings(),formPc.getDataSetting());

        return boEntity;
    }



    private String handAdd(FormBoEntity boEntity, JSONObject jsonObject,IUser curUser,DataHolder dataHolder){

        String parentId = jsonObject.getString(getParentField(boEntity));
        if(StringUtils.isEmpty(parentId)){
            parentId="0";
        }
        //插入主表记录。
        String pk=handInsert(boEntity,jsonObject,jsonObject,"0",parentId,curUser);
        //处理子表
        List<FormBoEntity> entityList= boEntity.getBoEntityList();

        //设置主表数据。
        dataHolder.setCurMain(jsonObject);
        dataHolder.setMainTable(boEntity.getTableName());


        for(FormBoEntity subEnt:entityList){
            String key=FormConst.SUB_PRE + subEnt.getAlias();
            if(!jsonObject.containsKey(key)){
                continue;
            }

            //一对一子表
            if(FormBoRelation.RELATION_ONETOONE.equals( subEnt.getBoRelation().getType())){
                JSONObject json=jsonObject.getJSONObject(key);
                handInsert(subEnt,jsonObject,json,pk,"0",curUser);
                //表间公式处理子表添加一对一。
                addSubOneToOne(dataHolder,subEnt,json);

            }
            else{
                JSONArray jsonAry=jsonObject.getJSONArray(key);

                SubDataHolder subDataHolder = new SubDataHolder();
                subDataHolder.setOneToMany(true);
                subDataHolder.setTableName(subEnt.getName());

                for (int i = 0; i < jsonAry.size(); i++) {
                    JSONObject rowJson = jsonAry.getJSONObject(i);
                    handRecursionInsert(subEnt, jsonObject, rowJson, pk, "0", curUser, subDataHolder);
                }

                dataHolder.addSubData(subEnt.getAlias(), subDataHolder);

            }
        }
        return  pk;
    }

    /**
     * 处理子表一对一的情况。
     * @param dataHolder
     * @param subEnt
     * @param json
     */
    private void addSubOneToOne(DataHolder dataHolder,FormBoEntity subEnt,JSONObject json){
        SubDataHolder holder=new SubDataHolder();
        holder.setOneToMany(false);
        holder.setTableName(subEnt.getName());
        holder.addAddList(json);
        dataHolder.addSubData(subEnt.getAlias(), holder);
    }



    /**
     * 递归处理子表数据。
     * @param boEntity
     * @param jsonObject    主表数据
     * @param rowJson   行数据
     * @param refId
     * @param parentId
     */
    private void handRecursionInsert(FormBoEntity boEntity,JSONObject jsonObject, JSONObject rowJson, String refId,String parentId,IUser user,SubDataHolder subDataHolder){
        String pk= handInsert(boEntity,jsonObject,  rowJson,  refId, parentId,user);
        //插入子表数据。
        subDataHolder.addAddList(rowJson);

        //非树形直接返回。
        if(!boEntity.tree() || !rowJson.containsKey(FormBoRelation.CHILDREN) ) {
            return;
        }

        JSONArray jsonArray=rowJson.getJSONArray(FormBoRelation.CHILDREN);
        for(int i=0;i<jsonArray.size();i++){
            rowJson = jsonArray.getJSONObject(i);
            handRecursionInsert(boEntity,jsonObject,rowJson,refId,pk,user, subDataHolder);
        }
    }


    /**
     * 处理一行数据插入。
     * @param boEntity
     * @param jsonObject 主表数据
     * @param rowJson   一行数据
     * @param refId
     * @param parentId
     * @param user
     * @return
     */
    private String handInsert(FormBoEntity boEntity, JSONObject jsonObject,JSONObject rowJson, String refId,String parentId,IUser user ){
        String pk= IdGenerator.getIdStr();
        String pkField=getPkField(boEntity);
        String parentField=getParentField(boEntity);
        String fieldFk="";

        //在JSON中插入主键数据，需要考虑外部表的情况。
        if(!rowJson.containsKey(pkField)) {
            rowJson.put(pkField, pk);
        }else{
            pk=rowJson.getString(pkField);
        }
        //插入
        //插入主记录
        //使用主记录的ID插入到REF_ID_

        List<String> nameFields=new ArrayList<>();
        List<String> valFields=new ArrayList<>();
        Map<String,Object> params= new HashMap<>();

        //主键
        nameFields.add(pkField);
        valFields.add(getValField(pkField));
        params.put(pkField,pk);

        params.put(FormBoEntity.FIELD_FK, refId);
        params.put(FormBoEntity.FIELD_PARENTID, parentId);
        params.put(FormBoEntity.FIELD_CREATE_TIME, new Date());
        params.put(FormBoEntity.FIELD_CREATE_BY, user.getUserId());
        params.put(FormBoEntity.FIELD_CREATE_DEP, user.getDeptId());
        params.put(FormBoEntity.FIELD_INST, rowJson.getString(FormBoEntity.FIELD_INST));
        params.put(FormBoEntity.FIELD_INST_STATUS_, rowJson.getString(FormBoEntity.FIELD_INST_STATUS_));
        params.put(FormBoEntity.FIELD_TENANT, user.getTenantId());

        Boolean supportGradeConfig=SysPropertiesUtil.getSupportGradeConfig();
        if(supportGradeConfig){
            params.put(FormBoEntity.FIELD_COMPANY, user.getCompanyId());
        }


        Boolean createField = SysPropertiesUtil.getBoolean("createField");
        String fieldFormat = SysPropertiesUtil.getString("fieldFormat");
        if(createField){
            if(StringUtils.isEmpty(fieldFormat)){
                fieldFormat="{account}-{fullname}";
            }
            fieldFormat=fieldFormat.replace("account",user.getAccount());
            fieldFormat=fieldFormat.replace("fullname",user.getFullName());
            params.put(FormBoEntity.FIELD_CREATE_BY_NAME, fieldFormat);
        }

        if (StringUtils.isNotEmpty(boEntity.getVersionField())) {
            params.put(boEntity.getVersionField(), 1);
        }

        //是否为外部数据源表
        if(!boEntity.external()) {

            nameFields.add(FormBoEntity.FIELD_FK);
            nameFields.add(FormBoEntity.FIELD_PARENTID);
            nameFields.add(FormBoEntity.FIELD_CREATE_TIME);
            nameFields.add(FormBoEntity.FIELD_CREATE_BY);
            if(supportGradeConfig){
                nameFields.add(FormBoEntity.FIELD_COMPANY);
            }


            valFields.add(getValField(FormBoEntity.FIELD_FK));
            valFields.add(getValField(FormBoEntity.FIELD_PARENTID));
            valFields.add(getValField(FormBoEntity.FIELD_CREATE_TIME));
            valFields.add(getValField(FormBoEntity.FIELD_CREATE_BY));
            if(supportGradeConfig){
                valFields.add(getValField(FormBoEntity.FIELD_COMPANY));
            }


            //添加租户数据。
            nameFields.add(FormBoEntity.FIELD_TENANT);
            valFields.add(getValField(FormBoEntity.FIELD_TENANT));

            //是否创建了字段
            if(createField){
                nameFields.add(FormBoEntity.FIELD_CREATE_BY_NAME);
                valFields.add(getValField(FormBoEntity.FIELD_CREATE_BY_NAME));
            }

            if(StringUtils.isNotEmpty( user.getDeptId())){
                nameFields.add(FormBoEntity.FIELD_CREATE_DEP);
                valFields.add(getValField(FormBoEntity.FIELD_CREATE_DEP));
            }

            if (rowJson.containsKey(FormBoEntity.FIELD_INST)) {
                nameFields.add(FormBoEntity.FIELD_INST);
                valFields.add(getValField(FormBoEntity.FIELD_INST));
            }

            if (rowJson.containsKey(FormBoEntity.FIELD_INST_STATUS_)) {
                nameFields.add(FormBoEntity.FIELD_INST_STATUS_);
                valFields.add(getValField(FormBoEntity.FIELD_INST_STATUS_));
            }

            if (StringUtils.isNotEmpty(boEntity.getVersionField())) {
                nameFields.add(boEntity.getVersionField());
                valFields.add(getValField(boEntity.getVersionField()));
            }
        }


        List<FormBoAttr> attrList= boEntity.getBoAttrList();
        for(FormBoAttr attr:attrList){
            if(FormHelper.excludeCtlList.contains(attr.getControl())){
                //获取标准字段的映射字段
                if("default".equals(attr.getDataType())){
                    JSONObject extJsonObj = (JSONObject) JSONObject.parse(attr.getExtJson());
                    String mappingField = extJsonObj.getString("mappingField");
                    if(StringUtils.isNotEmpty(mappingField) && BeanUtil.isNotEmpty(params.get(mappingField))){
                        nameFields.add(attr.getFieldName());
                        valFields.add(getValField(mappingField));
                    }
                }
                continue;
            }
            if(pkField.equals(attr.getFieldName())){
                continue;
            }
            if(parentField.equals(attr.getFieldName())){
                nameFields.add(attr.getFieldName());
                valFields.add(getValField(attr.getFieldName()));
                params.put(attr.getFieldName(), parentId);
                continue;
            }
            if(attr.getFieldName().equals(boEntity.getVersionField())){
                continue;
            }
            //外键
            if (StringUtils.isNotEmpty(fieldFk)) {
                if(fieldFk.equals(attr.getFieldName())){
                    continue;
                }
            }
            List<FieldEntity> list=new ArrayList<>();
            if(FormBoEntity.GENMODE_EASYFORM.equals(boEntity.getGenMode())){
                IAttrEasyHandler handler = AttrEasyHandlerContext.getAttrHandler(attr.getControl());
                list = handler.getFieldEntity(attr,rowJson);
            }else {
                IAttrHandler handler = AttrHandlerContext.getAttrHandler(attr.getControl());
                list = handler.getFieldEntity(attr, rowJson);
            }
            for (FieldEntity entity : list) {
                nameFields.add(entity.getFieldName().toUpperCase());
                valFields.add(getValField(entity.getName()));
                params.put(entity.getName(), entity.getValue());
            }
        }

        String sql="insert into " + boEntity.getTableName() + "("+ StringUtils.join(nameFields)
                + ") values ("+ StringUtils.join(valFields) +")";

        String dsAlias=getDatasource(boEntity);
        //插入数据库。
        commonDao.execute(dsAlias,sql,params);

        return pk;
    }



    private  String getValField(String field){
        return "#{" + field + "}";
    }

    /**
     * 处理一行数据的更新。
     * @param boEntity
     * @param attrMap
     * @param rowJson
     * @param user
     * @param isResume
     */
    private void handUpdRow(FormBoEntity boEntity, Map<String, FormBoAttr> attrMap, JSONObject rowJson,IUser user,boolean isResume){

        String pkField=getPkField(boEntity);
        String parentField=getParentField(boEntity);
        String pk=rowJson.getString(pkField);

        List<String> filedList=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();

        rowJson.forEach((key,val)->{
            if(!attrMap.containsKey(key)) {
                return;
            }
            FormBoAttr attr=attrMap.get(key);
            List< FieldEntity> list=new ArrayList<>();
            if(FormBoEntity.GENMODE_EASYFORM.equals(boEntity.getGenMode())){
                IAttrEasyHandler handler = AttrEasyHandlerContext.getAttrHandler(attr.getControl());
                list = handler.getFieldEntity(attr, rowJson);
            }else {
                IAttrHandler handler = AttrHandlerContext.getAttrHandler(attr.getControl());
                list = handler.getFieldEntity(attr, rowJson);
            }
            for(FieldEntity entity:list){
                filedList.add(entity.getFieldName().toUpperCase() +"=" + getValField(entity.getName()));
                params.put(entity.getName(),entity.getValue());
            }
        });
        //父ID
        if(rowJson.containsKey(parentField)){
            filedList.add(parentField+ "=" + getValField(parentField));
            params.put(parentField,rowJson.getString(parentField));
        }
        //版本号+1
        if (rowJson.containsKey(boEntity.getVersionField())) {
            if(isResume){
                filedList.add(boEntity.getVersionField() + "=" + getValField(boEntity.getVersionField()));
                params.put(boEntity.getVersionField(),rowJson.getInteger(boEntity.getVersionField()));
            }else {
                filedList.add(boEntity.getVersionField() + "=" + boEntity.getVersionField() + "+1");
            }
        }
        Boolean createField = SysPropertiesUtil.getBoolean("createField");
        params.put(FormBoEntity.FIELD_UPDATE_TIME, new Date());
        params.put(FormBoEntity.FIELD_UPDATE_BY, user.getUserId());
        if(createField){
            String fieldFormat = SysPropertiesUtil.getString("fieldFormat");
            if(StringUtils.isEmpty(fieldFormat)){
                fieldFormat="{account}-{fullname}";
            }
            fieldFormat=fieldFormat.replace("account",user.getAccount());
            fieldFormat=fieldFormat.replace("fullname",user.getFullName());
            params.put(FormBoEntity.FIELD_UPDATE_BY_NAME, fieldFormat);
        }
        params.put(FormBoEntity.FIELD_INST, rowJson.getString(FormBoEntity.FIELD_INST));
        params.put(FormBoEntity.FIELD_INST_STATUS_, rowJson.getString(FormBoEntity.FIELD_INST_STATUS_));

        //更新字段。
        if(!boEntity.external()) {
            filedList.add(FormBoEntity.FIELD_UPDATE_TIME + "=" + getValField(FormBoEntity.FIELD_UPDATE_TIME));
            filedList.add(FormBoEntity.FIELD_UPDATE_BY + "=" + getValField(FormBoEntity.FIELD_UPDATE_BY));
            if(createField){
                filedList.add(FormBoEntity.FIELD_UPDATE_BY_NAME + "=" + getValField(FormBoEntity.FIELD_UPDATE_BY_NAME));
            }
            //处理实例ID
            if (rowJson.containsKey(FormBoEntity.FIELD_INST)) {
                filedList.add(FormBoEntity.FIELD_INST + "=" + getValField(FormBoEntity.FIELD_INST));
            }
            /**
             * 处理流程实例。
             */
            if (rowJson.containsKey(FormBoEntity.FIELD_INST_STATUS_)) {
                filedList.add(FormBoEntity.FIELD_INST_STATUS_ + "=" + getValField(FormBoEntity.FIELD_INST_STATUS_));
            }
        }

        List<FormBoAttr> attrList= boEntity.getBoAttrList();
        for(FormBoAttr attr:attrList) {
            if (FormHelper.excludeCtlList.contains(attr.getControl())) {
                //获取标准字段的映射字段
                if ("default".equals(attr.getDataType())) {
                    JSONObject extJsonObj = (JSONObject) JSONObject.parse(attr.getExtJson());
                    String mappingField = extJsonObj.getString("mappingField");
                    if (StringUtils.isNotEmpty(mappingField) && BeanUtil.isNotEmpty(params.get(mappingField))) {
                        filedList.add(attr.getFieldName() + "=" + getValField(mappingField));
//                        nameFields.add();
//                        valFields.add(getValField(mappingField));
                    }
                }
                continue;
            }
        }

        String fileds=StringUtils.join(filedList);
        String sql="update " + boEntity.getTableName() +" set "+fileds+"  where " + pkField +"=#{pk}";
        if(!isResume && StringUtils.isNotEmpty(boEntity.getVersionField())){
            sql += " and "+boEntity.getVersionField() + "=#{pkVersion}";
            params.put("pkVersion",rowJson.getInteger(boEntity.getVersionField()));
        }
        params.put("pk",pk);
        SqlModel model=new SqlModel(sql);
        model.setParams(params);
        String dsAlias=getDatasource(boEntity);
        int count=commonDao.execute(dsAlias,model);
        if(isResume && count==0){
            String refField=getFkField(boEntity);
            handInsert(boEntity,rowJson,rowJson,rowJson.getString(refField),rowJson.getString(parentField),user);
        }
    }

    /**
     * 处理在当前数据和原数据之间的数据变化。
     * <pre>
     *     1.获取新增的数据，主键为空。
     *     2.获取更新的数据。
     *     3.不在当前列表中的数据为需要删除的数据。
     * </pre>
     * @param boEntity
     * @param originAry
     * @param curAry
     * @return
     */
    private Map<String,JSONArray> handChange(FormBoEntity boEntity,JSONArray curAry,JSONArray originAry){
        String pkField=getPkField(boEntity);
        //将历史数据转换成 map对象。
        Map<String, JSONObject> originMap = originAry.stream().collect(Collectors.toMap(p->{
            JSONObject json=(JSONObject)p;
            return  json.getString(pkField);
        }, p -> (JSONObject)p));


        Map<String,JSONArray>  map=new HashMap<>();
        for(Iterator it=curAry.iterator();it.hasNext();){
            Map row= (Map) it.next();
            //遍历当前提交的列表数据，如果没有主键这个数据就是新的。
            if(!row.containsKey(pkField)){
                handMap(map, row, TableUtil.OP_ADD);
                it.remove();
            }
            //处理更新的数据。
            else{
                String pk= (String) row.get(pkField);
                JSONObject originRow=originMap.get(pk);
                row.put(FormConst. ORIGIN,originRow);
            }
        }

        Set<String> set=curAry.stream().map(p->{
            Map json=(Map)p;
            return  (String)json.get(pkField);
        }).collect(Collectors.toSet());

        //取得删除的,遍历原纪录，如果主键不在提交的记录中就表示这个数据是被删除的。
        for(Iterator it=originAry.iterator();it.hasNext();){
            JSONObject row= (JSONObject) it.next();
            String id=row.getString(pkField);
            //获取删除的数据。
            if(!set.contains(id)){
                handMap(map, row, TableUtil.OP_DEL);
            }
        }

        //更新的数据。
        map.put(TableUtil.OP_UPD,curAry);

        return map;
    }

    private void handMap(Map<String,JSONArray> map, Map row, String action) {
        if (map.containsKey(action)) {
            map.get(action).add(row);
        } else {
            JSONArray list = new JSONArray();
            list.add(row);
            map.put(action, list);
        }
    }


    private String getPkField(FormBoEntity boEntity) {
        if (StringUtils.isNotEmpty(boEntity.getIdField())) {
            //是否为外部数据源生成
            if(boEntity.external()){
                return boEntity.getIdField();
            }
        }
        return FormBoEntity.FIELD_PK;
    }

    private String getFkField(FormBoEntity boEntity) {
        if (boEntity.getBoRelation() != null && StringUtils.isNotEmpty(boEntity.getBoRelation().getFkField())) {
            return boEntity.getBoRelation().getFkField();
        }
        return FormBoEntity.FIELD_FK;
    }

    private String getParentField(FormBoEntity boEntity) {
        if (StringUtils.isNotEmpty(boEntity.getParentField())) {
            return boEntity.getParentField();
        }
        return FormBoEntity.FIELD_PARENTID;
    }

    /**
     * 删除记录。
     *
     * @param boEntity
     * @param json
     */
    private void handDel(FormBoEntity boEntity, JSONObject json) {
        String pkField = getPkField(boEntity);
        String pk = json.getString(pkField);
        String sql = "delete from " + boEntity.getTableName() + " where " + pkField +"=#{pk}";
        SqlModel sqlModel=new SqlModel(sql);
        sqlModel.addParam("pk",pk);
        String dsAlias=getDatasource(boEntity);
        commonDao.execute(dsAlias,sqlModel);
    }


    /**
     * 获取数据源
     * @return
     */
    private String getDatasource(FormBoEntity boEntity) {
        String dsAlias = boEntity.getDsAlias();
        //判断当前是否为租户使用实体
        if("1".equals(boEntity.getIsTenant())){
            String tenantId = ContextUtil.getCurrentTenantId();
            OsInstDto instDto = osInstClient.getById(tenantId);
            if(StringUtils.isNotEmpty(instDto.getDatasource())){
                dsAlias=JSONObject.parseObject(instDto.getDatasource()).getString("value");
            }
        }
        return dsAlias;
    }






}
