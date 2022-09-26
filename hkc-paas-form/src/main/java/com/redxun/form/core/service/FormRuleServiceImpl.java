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
import com.redxun.form.core.entity.FormCustomQuery;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.mapper.FormRuleMapper;
import com.redxun.form.util.FormExOrImportHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [表单校验配置]业务服务类
 */
@Service
public class FormRuleServiceImpl extends SuperServiceImpl<FormRuleMapper, FormRule> implements BaseService<FormRule> {

    @Resource
    private FormRuleMapper formRuleMapper;

    @Override
    public BaseDao<FormRule> getRepository() {
        return formRuleMapper;
    }

    /**
     * 表单是否存在。
     * @param alias
     * @param id
     * @return
     */
    public boolean isExist(String alias,String id){
        Map<String,Object> params=new HashMap<>();
        params.put("alias",alias);
        if(StringUtils.isNotEmpty(id)){
            params.put("id",id);
        }
        Integer rtn=formRuleMapper.isExist(params);
        return rtn>0;
    }

    /**
     * 导入表单规则。
     * @param file
     * @param appId
     */
    public void importFormRule(MultipartFile file, String appId) {
        JSONArray formRuleArray  = FormExOrImportHandler.readZipFile(file);
        for (Object obj:formRuleArray) {
            JSONObject queryObj = (JSONObject)obj;
            JSONObject formRule = queryObj.getJSONObject("formRule");
            if(BeanUtil.isEmpty(formRule)){
                continue;
            }
            String formRuleStr = formRule.toJSONString();
            FormRule formNewRule = JSONObject.parseObject(formRuleStr,FormRule.class);
            formNewRule.setAppId(appId);
            String id = formNewRule.getId();
            FormRule oldForm = get(id);
            if(BeanUtil.isNotEmpty(oldForm)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldForm.getAppId())){
                    update(formNewRule);
                }else{
                    formNewRule.setId(IdGenerator.getIdStr());
                    insert(formNewRule);
                }
            }
            else{
                insert(formNewRule);
            }
        }
    }
}
