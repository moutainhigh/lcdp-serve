package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.mapper.FormMobileMapper;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [手机表单]业务服务类
 */
@Service
public class FormMobileServiceImpl extends SuperServiceImpl<FormMobileMapper, FormMobile> implements BaseService<FormMobile> {

    @Resource
    private FormMobileMapper formMobileMapper;

    @Resource
    private  FormDataService formDataService;

    @Override
    public BaseDao<FormMobile> getRepository() {
        return formMobileMapper;
    }

    public FormMobile getByFormId(String id){
        return formMobileMapper.getById(id);
    }

    /**
     * 根据别名查询表单数据。
     * @param alias
     * @return
     */
    public FormMobile getByAlias(String alias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",alias);
        return formMobileMapper.selectOne(wrapper);
    }

    /**
     * 根据BO定义和别名获取手机表单
     * @param boDefId
     * @return
     */
    public List<FormMobile> getByBoDefId(String boDefId,String alias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BODEF_ID_",boDefId);
        if(StringUtils.isNotEmpty(alias)){
            wrapper.eq("ALIAS_",alias);
        }
        return formMobileMapper.selectList(wrapper);
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
        Integer rtn=formMobileMapper.isExist(params);
        return rtn>0;
    }

    public void importMobileForm(MultipartFile file, String treeId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入手机表单,表单列表如下:");
        JSONArray formMobileArray  = FormExOrImportHandler.readZipFile(file);
        String appId=formDataService.getAppIdByTreeId(treeId);
        for (Object obj:formMobileArray) {
            JSONObject mobileObj = (JSONObject)obj;
            JSONObject formMobile = mobileObj.getJSONObject("formMobile");
            if(BeanUtil.isEmpty(formMobile)){
                continue;
            }

            String formMobileStr = formMobile.toJSONString();
            FormMobile formNewMobile = JSONObject.parseObject(formMobileStr,FormMobile.class);
            sb.append(formNewMobile.getName() +"("+formNewMobile.getId()+"),");
            formNewMobile.setCategoryId(treeId);
            formNewMobile.setAppId(appId);
            String id = formNewMobile.getId();
            FormMobile oldMobile = get(id);
            if(BeanUtil.isNotEmpty(oldMobile)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldMobile.getAppId())){
                    update(formNewMobile);
                }else{
                    formNewMobile.setId(IdGenerator.getIdStr());
                    insert(formNewMobile);
                }
            }
            else{
                insert(formNewMobile);
            }
        }
        sb.append("导入到分类:[" + treeId +"]下.");
        //记录日志
        LogContext.put(Audit.DETAIL,sb.toString());
    }


}
