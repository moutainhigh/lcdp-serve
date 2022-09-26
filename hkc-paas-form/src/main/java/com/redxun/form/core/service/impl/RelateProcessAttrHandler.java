package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dboperator.model.Column;
import com.redxun.dto.bpm.BpmInstDto;
import com.redxun.feign.BpmInstClient;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RelateProcessAttrHandler extends  BaseAttrHandler {

    @Override
    public String getPluginName() {
        return "rx-relate-process";
    }

    @Override
    public String getDescription() {
        return "关联流程";
    }

    @Resource
    BpmInstClient bpmInstClient;

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject setting1=setting;
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        JSONObject setting1=jsonObject;
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        JSONObject setting1=jsonObject;
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        List<FieldEntity> list=new ArrayList<>();
        FieldEntity entity=new FieldEntity();
        JSONArray newArray =new JSONArray();
        String val = (String) json.get(attr.getName());
        if(StringUtils.isNotEmpty(val)){
            JSONArray jsonArray = JSONArray.parseArray(val);
            String date= DateUtils.dateTimeNow(DateUtils.switchFormat("yyyy-MM-dd HH:mm:ss"));
            IUser currentUser = ContextUtil.getCurrentUser();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject newJsonObj=new JSONObject();
                newJsonObj.put("instId",jsonObject.getString("instId"));
                //新增时间
                if(StringUtils.isEmpty(jsonObject.getString("addDate"))){
                    newJsonObj.put("addDate",date);
                }else {
                    newJsonObj.put("addDate",jsonObject.getString("addDate"));
                }
                //新增人
                if(BeanUtil.isEmpty(jsonObject.getJSONObject("addUser"))){
                    JSONObject user=new JSONObject();
                    user.put("userId",currentUser.getUserId());
                    user.put("userName",currentUser.getFullName());
                    newJsonObj.put("addUser",user);
                }else {
                    newJsonObj.put("addUser",jsonObject.getJSONObject("addUser"));
                }
                newArray.add(newJsonObj);
            }
        }

        entity.setName(attr.getName());
        if(StringUtils.isNotEmpty(attr.getFieldName())){
            entity.setFieldName(attr.getFieldName().toUpperCase());
        }else {
            entity.setFieldName(TableUtil.getFieldName(attr.getName()));
        }
        entity.setValue(newArray.toString());
        list.add(entity);
        return list;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String fieldName=attr.getFieldName().toUpperCase();
        Object val=null;
        if(rowData.containsKey(fieldName)){
            val=rowData.get(fieldName);
            //如果数据为空直接显示为空。
            if(val==null || "".equals(val) ){
                return ValueResult.exist("");
            }
            JSONArray jsonArray = JSONArray.parseArray(val.toString());
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String instId = jsonObject.getString("instId");
                BpmInstDto bpmInstDto = bpmInstClient.getById(instId);
                jsonObject.put("subject",bpmInstDto.getSubject());
            }
            return  ValueResult.exist(jsonArray.toString());
        } else{
            return  ValueResult.noExist();
        }
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_CLOB);
    }
}
