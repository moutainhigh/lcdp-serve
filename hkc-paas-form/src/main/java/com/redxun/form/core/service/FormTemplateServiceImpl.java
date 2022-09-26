package com.redxun.form.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormTemplate;
import com.redxun.form.core.mapper.FormTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [表单模版]业务服务类
*/
@Service
public class FormTemplateServiceImpl extends SuperServiceImpl<FormTemplateMapper, FormTemplate> implements BaseService<FormTemplate> {

    @Resource
    private FormTemplateMapper formTemplateMapper;

    @Override
    public BaseDao<FormTemplate> getRepository() {
        return formTemplateMapper;
    }

    /**
     * 根据别名与类型获取表单模板
     * @param alias
     * @param type
     * @return
     */
    public String getByAliasAndType(String alias, String type) {
        return formTemplateMapper.getByAliasAndType(alias,type);
    }

    /**
     * 根据类型与类别获取表单模板
     * @param type
     * @param category
     * @return
     */
    public List<FormTemplate> getByTypeAndCategory(String type, String category) {
        return formTemplateMapper.getByTypeAndCategory(type,category);
    }

    /**
     * 根据参数获取代码生成模板
     * @param genMode
     * @param fileName
     * @param mainSubType
     * @return
     */
    public List<FormTemplate> getCodeGenByParams(String genMode, String fileName, String mainSubType) {
        return formTemplateMapper.getCodeGenByParams(genMode,fileName);
    }
}
