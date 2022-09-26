package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormTableFormula;
import com.redxun.form.core.listener.FieldValueHandlerContext;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import com.redxun.form.core.mapper.FormTableFormulaMapper;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * [表间公式]业务服务类
 */
@Service
public class FormTableFormulaServiceImpl extends SuperServiceImpl<FormTableFormulaMapper, FormTableFormula> implements BaseService<FormTableFormula> {

    @Resource
    private FormTableFormulaMapper formTableFormulaMapper;
    @Resource
    private FormBoEntityServiceImpl formBoEntityService;
    @Resource
    private FormBoAttrServiceImpl formBoAttrService;
    @Resource
    private  FormDataService formDataService;

    @Override
    public BaseDao<FormTableFormula> getRepository() {
        return formTableFormulaMapper;
    }

    public JSONObject getFormDataByFormConfig(JSONObject formJson,JSONArray formConfig){
        DataHolder dataHolder=new DataHolder();
        dataHolder.setCurMain(formJson);
        JSONObject formData=new JSONObject();
        for(Object obj:formConfig){
            JSONObject json=(JSONObject)obj;
            JSONObject config=json.getJSONObject("conf");
            String tableName=config.getString("tableName");
            String relType = config.getString("relationType");
            JSONArray gridData = config.getJSONArray("gridData");
            JSONArray mapper= getJsonAry(gridData);
            Map<String,FormBoAttr> map = formBoAttrService.getAttrsMapByEntId(tableName);
            FormBoEntity ent = formBoEntityService.getByTableName(tableName);
            if(FormBoRelation.RELATION_MAIN.equals(relType)) {
                formData.putAll(getFormDataByConfig(mapper, map, dataHolder,config));
            }else if(FormBoRelation.RELATION_ONETOONE.equals(relType)){
                formData.put("sub__"+ent.getAlias(),getFormDataByConfig(mapper, map, dataHolder,config));
            }else if(FormBoRelation.RELATION_ONETOMANY.equals(relType)) {
                String userSubTable = config.getString("userSubTable");
                String userMainTable = config.getString("userMainTable");
                FormBoEntity subEnt = formBoEntityService.get(userSubTable);
                if(subEnt!=null) {
                    JSONArray array = dataHolder.getCurMain().getJSONObject(userMainTable).getJSONArray("sub__"+subEnt.getAlias());
                    if(array!=null) {
                        JSONArray temp = new JSONArray();
                        for (int i = 0; i < array.size(); i++) {
                            config.put("index", i);
                            temp.add(getFormDataByConfig(mapper, map, dataHolder, config));
                        }
                        formData.put("sub__" + ent.getAlias(), temp);
                    }
                }
            }
        }
        return formData;
    }

    public JSONObject getTableFieldValueHandler(DataHolder dataHolder, JSONObject obj) {
        JSONArray gridData = obj.getJSONArray("gridData");
        String tableName = obj.getString("tableName");
        String relType = obj.getString("relationType");
        JSONArray mapper= getJsonAry(gridData);
        String pre = "sub__";
        FormBoEntity ent = formBoEntityService.getByTableName(tableName);
        JSONObject formData = new JSONObject();
        Map<String,FormBoAttr> map = formBoAttrService.getAttrsMapByEntId(tableName);


        if(FormBoRelation.RELATION_MAIN.equals(relType) || FormBoRelation.RELATION_ONETOONE.equals(relType)) {
            formData = getFormData(mapper, map, dataHolder,obj);
        }
        if(FormBoRelation.RELATION_ONETOMANY.equals(relType)) {
            String userSubTable = obj.getString("userSubTable");
            String userMainTable = obj.getString("userMainTable");
            FormBoEntity subEnt = formBoEntityService.get(userSubTable);
            if(subEnt!=null) {
                JSONArray array = dataHolder.getCurMain().getJSONObject(userMainTable).getJSONArray(pre+subEnt.getAlias());
                if(array!=null) {
                    JSONArray temp = new JSONArray();
                    for (int i = 0; i < array.size(); i++) {
                        obj.put("index", i);
                        temp.add(getFormData(mapper, map, dataHolder, obj));
                    }
                    formData.put(pre + ent.getAlias(), temp);
                }
                return formData;
            }
        }
        return formData;
    }

    private JSONObject getFormDataByConfig(JSONArray mapper, Map<String, FormBoAttr> map, DataHolder dataHolder, JSONObject obj) {
        JSONObject formData = new JSONObject();
        for(int i=0;i<mapper.size();i++){
            JSONObject json=mapper.getJSONObject(i);
            String mapType=json.getString("mapType");
            ITableFieldValueHandler valHandler= FieldValueHandlerContext.getValueHandler(mapType);
            String fieldName=json.getString("fieldName");
            if(valHandler==null || "ID_".equals(fieldName)){
                continue;
            }
            fieldName=fieldName.toUpperCase();
            String columnType=json.getString("columnType");
            String mapValue=json.getString("mapValue");
            Object val= valHandler.getFieldValue(columnType, null,
                    dataHolder, dataHolder.getCurMain(), dataHolder.getOriginMain(), mapValue, obj.getInnerMap());
            if(map.containsKey(fieldName)) {
                FormBoAttr attr = map.get(fieldName);
                Integer single = attr.getIsSingle();
                if(single==0) {
                    FormBoAttr nameAttr = new FormBoAttr();
                    String name = (attr.getFieldName() + "_name").toUpperCase();
                    nameAttr.setName(attr.getName() + "_name");
                    nameAttr.setOrignAttr(attr);
                    map.put(name, nameAttr);
                    JSONObject fieldJson = new JSONObject();
                    fieldJson.put("value", val);
                    val = fieldJson.toJSONString();
                }
                if(attr.getOrignAttr()!=null){
                    attr=attr.getOrignAttr();
                }
                mapValue = attr.getName();
                if(formData.containsKey(mapValue)) {
                    JSONObject fieldJson=formData.getJSONObject(mapValue);
                    single = attr.getIsSingle();
                    if (single == 0) {
                        fieldJson.put("label", val);
                        val = fieldJson.toJSONString();
                    }
                }
            }
            formData.put(mapValue, val);
        }
        return formData;
    }

    private JSONObject getFormData(JSONArray mapper, Map<String, FormBoAttr> map, DataHolder dataHolder, JSONObject obj) {
        JSONObject formData = new JSONObject();
        for(int i=0;i<mapper.size();i++){
            JSONObject json=mapper.getJSONObject(i);
            String mapType=json.getString("mapType");
            ITableFieldValueHandler valHandler= FieldValueHandlerContext.getValueHandler(mapType);
            String fieldName=json.getString("fieldName");
            if(valHandler==null || "ID_".equals(fieldName)){
                continue;
            }
            String columnType=json.getString("columnType");
            String mapValue=json.getString("mapValue");
            Object val= valHandler.getFieldValue(columnType, null,
                    dataHolder, dataHolder.getCurMain(), dataHolder.getOriginMain(), mapValue, obj.getInnerMap());
            if(map.containsKey(fieldName)) {
                FormBoAttr attr = map.get(fieldName);
                Integer single = attr.getIsSingle();
                if(single==0) {
                    FormBoAttr nameAttr = new FormBoAttr();
                    String name = attr.getFieldName().toUpperCase()+"_name";
                    nameAttr.setName(attr.getName()+"_name");
                    map.put(name, nameAttr);
                }
                mapValue = attr.getName();
            }
            formData.put(mapValue, val);
        }
        return formData;
    }

    private JSONArray getJsonAry(JSONArray mapper){
        JSONArray ary=new JSONArray();
        for(int i=0;i<mapper.size();i++){
            JSONObject json=mapper.getJSONObject(i);
            String mapType=json.getString("mapType");
            if(StringUtils.isEmpty(mapType)) {
                continue;
            }
            ary.add(json);
        }
        return ary;
    }


    public void importFormula(MultipartFile file, String treeId) {

        StringBuilder sb=new StringBuilder();
        sb.append("导入表间公式:");

        JSONArray formFormulaArray  = FormExOrImportHandler.readZipFile(file);
        String appId=formDataService.getAppIdByTreeId(treeId);
        for (Object obj:formFormulaArray) {
            JSONObject formulaObj = (JSONObject)obj;
            JSONObject formFormula = formulaObj.getJSONObject("formTableFormula");
            if(BeanUtil.isEmpty(formFormula)){
                continue;
            }

            String formFormulaStr = formFormula.toJSONString();
            FormTableFormula formNewFormula = JSONObject.parseObject(formFormulaStr,FormTableFormula.class);

            sb.append(formNewFormula.getName() +"("+formNewFormula.getId()+"),");

            formNewFormula.setTreeId(treeId);
            formNewFormula.setAppId(appId);
            String id = formNewFormula.getId();
            FormTableFormula oldFormula = get(id);
            if(BeanUtil.isNotEmpty(oldFormula)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldFormula.getAppId())) {
                    update(formNewFormula);
                }else {
                    formNewFormula.setId(IdGenerator.getIdStr());
                    insert(formNewFormula);
                }
            }
            else{
                insert(formNewFormula);
            }
        }
        sb.append("导入到分类:" + treeId);

        LogContext.put(Audit.DETAIL,sb.toString());

    }


}
