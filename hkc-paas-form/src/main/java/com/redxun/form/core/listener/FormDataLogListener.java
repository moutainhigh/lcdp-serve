package com.redxun.form.core.listener;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.dto.form.FormConst;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.entity.DataHolderEvent;
import com.redxun.form.core.entity.SubDataHolder;
import com.redxun.form.core.entity.UpdJsonEnt;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 当数据发生变更时计算数据变化。
 */
@Service
public class FormDataLogListener implements ApplicationListener<DataHolderEvent> {
    @Override
    public void onApplicationEvent(DataHolderEvent dataHolderEvent) {
        DataHolder  source= (DataHolder) dataHolderEvent.getSource();
        JSONObject json= getLog(source);
        LogContext.put(Audit.PK,source.getNewPk());
        LogContext.put(Audit.ACTION,source.getAction());
        String detail=json.toJSONString();

        if(LogContext.get().containsKey(Audit.DETAIL)){
            detail= LogContext.getByKey(Audit.DETAIL);
            detail+=json.toJSONString();
        }
        LogContext.put(Audit.DETAIL,detail);

    }

    private JSONObject getLog(DataHolder source){
        String action= source.getAction();

        JSONObject main=source.getCurMain();

        main.remove("initData");
        main.remove("UPDATE_TIME_");
        main.remove("INST_ID_");
        main.remove("INST_STATUS_");
        main.remove("UPDATE_BY_");


        if(DataHolder.ACTION_NEW.equals(action) || DataHolder.ACTION_DEL.equals(action)){
            return source.getCurMain();
        }
        /**
         * {
         *     tableName:{
         *         field:{origin:"",curVal:""}
         *     },
         *     tableName:{
         *         add:[]
         *         upd:[]
         *         del:[]
         *     }
         * }
         */
        if(DataHolder.ACTION_UPD.equals(action)){

            JSONObject resultJson=new JSONObject();
            //处理主表。
            JSONObject curMain=source.getCurMain();
            JSONObject originMain=source.getOriginMain();
            Set<String> keySet= curMain.keySet();
            //处理主表。
            JSONObject mainJson=handJson(originMain,curMain,true);

            if(mainJson.size()>0){
                resultJson.put(source.getMainTable(),mainJson);
            }


            //处理子表
            for(Iterator<String> it = keySet.iterator(); it.hasNext();){
                String key=it.next();
                if(!key.startsWith(FormConst.SUB_PRE)){
                    continue;
                }
                JSONObject subJson=new JSONObject();
                String tableName=key.substring(FormConst.SUB_PRE.length());
                SubDataHolder subData = source.getSubData(tableName);
                //一对多
                if(subData!=null && subData.isOneToMany()){
                    List<JSONObject> addList = subData.getAddList();
                    if(BeanUtil.isNotEmpty(addList)){
                        subJson.put("add",addList);
                    }
                    List<JSONObject> delList = subData.getDelList();
                    if(BeanUtil.isNotEmpty(delList)){
                        subJson.put("del",delList);
                    }
                    List<UpdJsonEnt> updList = subData.getUpdList();
                    if(BeanUtil.isNotEmpty(updList)){
                        List<JSONObject> upds=new ArrayList<>();
                        for(UpdJsonEnt updEnt:updList){
                            JSONObject curJosn=updEnt.getCurJson();
                            JSONObject originJson=updEnt.getOriginJson();
                            JSONObject rowJson=handJson(originJson,curJosn,false);
                            if(rowJson.size()>0){
                                upds.add(rowJson);
                            }
                        }
                        if(upds.size()>0){
                            subJson.put("upd",upds);
                        }

                    }
                }
                else{
                    if(subData!=null){
                        List<UpdJsonEnt> updList = subData.getUpdList();
                        if(BeanUtil.isNotEmpty(updList)){
                            UpdJsonEnt updEnt=updList.get(0);
                            JSONObject curJosn=updEnt.getCurJson();
                            JSONObject originJson=updEnt.getOriginJson();
                            JSONObject rowJson=handJson(originJson,curJosn,false);
                            if(rowJson.size()>0){
                                subJson.put("upd",rowJson);
                            }

                        }
                    }
                }
                if(subJson.size()>0){
                    resultJson.put(tableName,subJson);
                }

            }
            return resultJson;
        }

        return null;

    }

    /**
     * 获取变更数据。
     * @param orgin     源记录
     * @param curJson   当前记录
     * @return
     */
    private JSONObject handJson(JSONObject orgin,JSONObject curJson,boolean isMain){

        curJson.remove("INST_ID_");
        curJson.remove("origin__");
        curJson.remove("CREATE_TIME_");
        curJson.remove("INST_STATUS_");
        curJson.remove("UPDATE_TIME_");
        curJson.remove("UPDATE_BY_");
        curJson.remove("selected");

        JSONObject result=new JSONObject();
        Set<String> keySet=curJson.keySet();
        for(Iterator<String> it=keySet.iterator();it.hasNext();){
            String key=it.next();
            if(key.startsWith(FormConst.SUB_PRE) && isMain){
                continue;
            }
            String curVal=curJson.getString(key);
            String originVal=orgin.getString(key);
            if(curVal!=null && curVal.equals(originVal)){
                continue;
            }
            JSONObject valJson=new JSONObject();
            valJson.put("origin",originVal);
            valJson.put("curVal",curVal);
            result.put(key,valJson);
        }
        return result;
    }
}
