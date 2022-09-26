package com.redxun.api.form;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.form.FormBoDefDto;
import com.redxun.dto.form.FormMobileDto;
import com.redxun.dto.form.FormPcDto;

import java.util.List;
import java.util.Set;

/**
 * Form服务
 */
public interface IFormService {
    /**
     * 执行数据源对应的sql
     * @return
     */
    List getDataByAliasAndSql(String alias, String sql);

    /**
     * 获取pc表单数据
     * @return
     */
    FormPcDto getFormPcByAlias(String alias);

    /**
     * 获取移动端表单数据
     * @return
     */
    FormMobileDto getMobleFormByAlias(String alias);

    /**
     * 获取表单bo定义
     * @return
     */
    Object getFormBoDefByAlias(String alias);


    /**
     * 导入pc表单数据
     * @return
     */
    void importFormPc(Set<FormPcDto> formPcDto,String treeId);

    /**
     * 导入移动端表单数据
     * @return
     */
    void importMobileForm(Set<FormMobileDto> formMobileDto,String treeId);

    /**
     * 导入表单bo定义
     * @return
     */
    List<AlterSql> importFormBoDef(JSONObject sysBoDefJson);
}
