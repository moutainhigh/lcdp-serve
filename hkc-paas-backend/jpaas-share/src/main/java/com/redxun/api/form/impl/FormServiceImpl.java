package com.redxun.api.form.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.form.IFormService;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.form.FormMobileDto;
import com.redxun.dto.form.FormPcDto;
import com.redxun.feign.form.FormClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 单据服务实现类
 */
@Component
@Primary
public class FormServiceImpl implements IFormService {

    @Resource
    @Lazy
    FormClient formClient;

    @Override
    public List getDataByAliasAndSql(String alias, String sql) {
        return formClient.getDataByAliasAndSql(alias,sql);
    }

    @Override
    public FormPcDto getFormPcByAlias(String alias){
        return formClient.getFormPcByAlias(alias);
    }

    @Override
    public FormMobileDto getMobleFormByAlias(String alias){
        return formClient.getMobleFormByAlias(alias);
    }

    @Override
    public Object getFormBoDefByAlias(String alias){
        return formClient.getFormBoDefByAlias(alias);
    }


    /**
     * 导入pc表单数据
     * @return
     */
    @Override
    public void importFormPc(Set<FormPcDto> formPcDto,String treeId){
        JSONObject bpmFormJson = new JSONObject();
        bpmFormJson.put("formPcDto",formPcDto);
        bpmFormJson.put("treeId",treeId);
        formClient.importFormPc(bpmFormJson);
    }

    /**
     * 导入移动端表单数据
     * @return
     */
    @Override
    public void importMobileForm(Set<FormMobileDto> formMobileDto,String treeId){
        JSONObject formMobileJson = new JSONObject();
        formMobileJson.put("formMobileDto",formMobileDto);
        formMobileJson.put("treeId",treeId);
        formClient.importMobileForm(formMobileJson);
    }

    /**
     * 导入表单bo定义
     * @return
     */
    @Override
    public List<AlterSql> importFormBoDef(JSONObject sysBoDefJson){
        return  formClient.importFormBoDef(sysBoDefJson);
    }
}
