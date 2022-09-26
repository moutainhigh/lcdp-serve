package com.redxun.form.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.core.service.easyImpl.AttrEasyHandlerContext;
import com.redxun.form.core.service.easyImpl.IAttrEasyHandler;

/**
 *  表单常用工具类
 */
public class FormUtil {
    /**
     * [
     *      {//普通控件
     * 					"type": "inpput",
     * 					"label": "标题栏",
     * 					"key": "title_1628824267039"
     * 		},
     * 	    {//子表控件-子表控件要找到 fields下面的list
     * 	                "type": "formtable",
     * 					"label": "子表",
     * 				    "fields":[
     * 				        {
     * 				            key:'type_1628835652633',
     * 				            list:{
     * 				                "type": "inpput",
     * 				                "label": "标题栏",
     * 				                "key": "title_1628824267039"
     * 				            }
     * 				        }
     * 				    ]
     * 	    }
     * ]
     * @param list
     * @return
     */
    public static FormBoEntity parseList(JSONArray list,String formtype){
        FormBoEntity mainEnt=new FormBoEntity();
        for(Object obj:list){
            JSONObject json=(JSONObject)obj;
            if("formtable".equals(formtype) ){
                json=(JSONObject)((JSONObject) obj).getJSONObject("list");
            }

            String type=json.getString("type");

            if("formtable".equals(type)){
                //一对多子表
                FormBoEntity subEnt=parseList(json.getJSONArray("fields"),"formtable");
                subEnt.setRelationType(FormBoRelation.RELATION_ONETOMANY);
                subEnt.setName(json.getString("label"));
                subEnt.setAlias(json.getString("model"));
                JSONArray buttonsAry=json.getJSONArray("buttons");
                JSONArray buttons=new JSONArray();
                for(int i=0;i<buttonsAry.size();i++){
                    JSONObject btn=buttonsAry.getJSONObject(i);
                    JSONObject button=new JSONObject();
                    button.put("alias",btn.getString("alias"));
                    button.put("name",btn.getString("name"));
                    buttons.add(button);
                }
                subEnt.setButtons(buttons.toJSONString());
                mainEnt.addBoEnt(subEnt);
                continue;
            }else if("subForm".equals(type)){
                //一对一子表
                JSONArray columns=json.getJSONArray("columns");
                JSONArray subAry=new JSONArray();
                for(Object col:columns){
                    JSONObject colJson=(JSONObject)col;
                    subAry.addAll(colJson.getJSONArray("list"));
                }
                FormBoEntity subEnt=parseList(subAry,"");
                subEnt.setRelationType(FormBoRelation.RELATION_ONETOONE);
                subEnt.setName(json.getString("label"));
                subEnt.setAlias(json.getString("model"));
                mainEnt.addBoEnt(subEnt);
                continue;
            }else if("table".equals(type)){
                //表格布局
                JSONArray trs=json.getJSONArray("trs");
                for(Object tr:trs){
                    JSONObject trObj=(JSONObject)tr;
                    JSONArray tds=trObj.getJSONArray("tds");
                    for(Object td:tds){
                        JSONObject tdObj=(JSONObject)td;
                        FormBoEntity tabEntity=parseList(tdObj.getJSONArray("list"),"");
                        //主表字段
                        mainEnt.getBoAttrList().addAll(tabEntity.getBoAttrList());
                        //子表增加
                        mainEnt.getBoEntityList().addAll(tabEntity.getBoEntityList());
                    }
                }
                continue;
            }else if("grid".equals(type)){
                //栅格布局
                JSONArray columns=json.getJSONArray("columns");
                for(Object col:columns){
                    JSONObject colJson=(JSONObject)col;
                    FormBoEntity tabEntity=parseList(colJson.getJSONArray("list"),"");
                    //主表字段
                    mainEnt.getBoAttrList().addAll(tabEntity.getBoAttrList());
                    //子表增加
                    mainEnt.getBoEntityList().addAll(tabEntity.getBoEntityList());
                }
                continue;
            }else if("formTabs".equals(type)){
                JSONArray columns=json.getJSONObject("options").getJSONArray("columns");
                for(Object col:columns) {
                    JSONObject colJson = (JSONObject) col;
                    FormBoEntity tabEntity=parseList(colJson.getJSONArray("list"),"");
                    //主表字段
                    mainEnt.getBoAttrList().addAll(tabEntity.getBoAttrList());
                    //子表增加
                    mainEnt.getBoEntityList().addAll(tabEntity.getBoEntityList());
                }
            }
            if("button".equals(type)){
                JSONArray buttons=new JSONArray();
                if(StringUtils.isNotEmpty(mainEnt.getButtons())){
                    buttons=JSONArray.parseArray(mainEnt.getButtons());
                }
                JSONObject button=new JSONObject();
                button.put("alias",json.getString("key"));
                button.put("name",json.getString("label"));
                buttons.add(button);
                mainEnt.setButtons(buttons.toJSONString());
            }
            IAttrEasyHandler handler= AttrEasyHandlerContext.getAttrHandler(type);
            if(handler!=null && json.containsKey("model")) {
                FormBoAttr attr = handler.parse(json);
                mainEnt.addAttr(attr);
            }
        }
        return mainEnt;
    }

    public static JSONArray listToTree(JSONArray arr,String id,String pid,String child){
        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        //将数组转为Object的形式，key为数组中的id
        for(int i=0;i<arr.size();i++){
            JSONObject json = arr.getJSONObject(i);
            if(json.containsKey(id)){
                hash.put(json.getString(id), json);
            }

        }
        //遍历结果集
        for(int j=0;j<arr.size();j++){
            //单条记录
            JSONObject aVal = arr.getJSONObject(j);
            //在hash中取出key为单条记录中pid的值
            JSONObject hashVP = hash.getJSONObject(aVal.getString(pid));
            //如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if(hashVP!=null){
                //检查是否有child属性
                if(hashVP.containsKey(child)){
                    JSONArray ch = hashVP.getJSONArray(child);
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }else{
                    JSONArray ch = new JSONArray();
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }
            }else{
                r.add(aVal);
            }
        }
        return r;
    }
}
