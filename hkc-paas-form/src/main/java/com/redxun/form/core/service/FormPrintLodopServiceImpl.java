package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmCheckHistoryDto;
import com.redxun.feign.BpmInstClient;
import com.redxun.feign.bpm.BpmClient;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPrintLodop;
import com.redxun.form.core.mapper.FormPrintLodopMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [表单套打模板]业务服务类
*/
@Service
public class FormPrintLodopServiceImpl extends SuperServiceImpl<FormPrintLodopMapper, FormPrintLodop> implements BaseService<FormPrintLodop> {

    @Resource
    private FormPrintLodopMapper formPrintLodopMapper;
    @Resource
    private FormBoDefServiceImpl formBoDefService;
    @Resource
    private FormPcServiceImpl formPcService;
    @Resource
    private FtlEngine ftlEngine;
    @Resource
    private BpmClient bpmClient;
    @Resource
    private BpmInstClient bpmInstClient;

    @Override
    public BaseDao<FormPrintLodop> getRepository() {
        return formPrintLodopMapper;
    }

    public String printHtml(String pkId,String formData) throws Exception{
        FormPrintLodop formPrintLodop = get(pkId);
        String template = formPrintLodop.getTemplate();
        Map<String,Object> params = parseFormData(formData,formPrintLodop.getFormId());
        //审批意见
        List<BpmCheckHistoryDto> opinionHistoryList= new ArrayList<>();
        if(params.containsKey(FormBoEntity.FIELD_INST) && BeanUtil.isNotEmpty(params.get(FormBoEntity.FIELD_INST))){
            //流程变量
            Map<String,Object> vars=bpmInstClient.getVariablesByInstId((String)params.get(FormBoEntity.FIELD_INST));
            if(BeanUtil.isNotEmpty(vars)){
                params.putAll(vars);
            }
            opinionHistoryList=bpmClient.getOpinionNameNotEmpty((String)params.get(FormBoEntity.FIELD_INST));
        }
        Map<String,String> opinionMap=new HashMap<>(SysConstant.INIT_CAPACITY_32);
        opinionMap.put("AGREE","通过");
        opinionMap.put("OVERTIME_AUTO_AGREE","超时审批");
        opinionMap.put("SKIP","跳过");
        opinionMap.put("RECOVER","撤回");
        opinionMap.put("TIMEOUT_SKIP","超时跳过");
        opinionMap.put("REFUSE","不同意");
        opinionMap.put("COMMUNICATE","沟通");
        opinionMap.put("REPLY_COMMUNICATE","回复沟通");
        opinionMap.put("CANCEL_COMMUNICATE","取消沟通");
        opinionMap.put("BACK","驳回");
        opinionMap.put("BACK_TO_STARTOR","驳回发起人");
        opinionMap.put("BACK_SPEC","驳回节点");
        opinionMap.put("BACK_GATEWAY","驳回网关");
        opinionMap.put("ABSTAIN","弃权");
        opinionMap.put("BACK_CANCEL","驳回撤销");
        opinionMap.put("TRANSFER","撤回撤销");
        opinionMap.put("TRANSFER","转办");
        opinionMap.put("INTERPOSE","干预");
        params.put("opinionList",opinionHistoryList);
        params.put("opinionMap",opinionMap);
        params.put("curUser", ContextUtil.getCurrentUser());
        template = ftlEngine.parseByStringTemplate(params,template);
        return template;
    }

    private Map<String,Object> parseFormData(String formData,String formId){
        Map<String,Object> params=JSONObject.parseObject(formData).getInnerMap();
        FormPc formPc=formPcService.get(formId);
        FormBoDef formBoDef=formBoDefService.getByBoDefId(formPc.getBodefId());
        FormBoEntity formBoEntity=formBoDef.getFormBoEntity();
        List<FormBoAttr> mainAttr=formBoEntity.getBoAttrList();
        for(FormBoAttr attr:mainAttr){
            if(!attr.single()){
                JSONObject json=JSONObject.parseObject((String)params.get(attr.getName()));
                params.put(attr.getName(),json);
            }
        }
        for(FormBoEntity subEntity:formBoEntity.getBoEntityList()){
            List<FormBoAttr> subAttr=subEntity.getBoAttrList();
            if(subEntity.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOONE)){
                JSONObject subJson=(JSONObject)params.get("sub__"+subEntity.getAlias());
                if(subJson!=null) {
                    for (FormBoAttr attr : subAttr) {
                        if (!attr.single()) {
                            JSONObject json = subJson.getJSONObject(attr.getName());
                            subJson.put(attr.getName(), json);
                        }
                    }
                }
                params.put("sub__"+subEntity.getAlias(),subJson);
            }else if(subEntity.getBoRelation().getType().equals(FormBoRelation.RELATION_ONETOMANY)){
                JSONArray subArr=(JSONArray)params.get("sub__"+subEntity.getAlias());
                if(subArr!=null) {
                    for (FormBoAttr attr : subAttr) {
                        if (!attr.single()) {
                            for (Object sub : subArr) {
                                JSONObject subJson = (JSONObject) sub;
                                JSONObject json = subJson.getJSONObject(attr.getName());
                                subJson.put(attr.getName(), json);
                            }
                        }
                    }
                }
                params.put("sub__"+subEntity.getAlias(),subArr);
            }
        }
        return params;
    }
}
