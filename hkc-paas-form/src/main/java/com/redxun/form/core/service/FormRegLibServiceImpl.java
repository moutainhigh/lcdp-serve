package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.core.entity.FormRegLib;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.mapper.FormRegLibMapper;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * [正则表达式]业务服务类
 */
@Service
public class FormRegLibServiceImpl extends SuperServiceImpl<FormRegLibMapper, FormRegLib> implements BaseService<FormRegLib> {

    @Resource
    private FormRegLibMapper formRegLibMapper;

    @Override
    public BaseDao<FormRegLib> getRepository() {
        return formRegLibMapper;
    }

    public FormRegLib getByKey(String reg, String cacheName) {
        FormRegLib formRegLib=(FormRegLib)CacheUtil.get(cacheName,reg);
        if(formRegLib==null){
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("KEY_",reg);
            formRegLib=formRegLibMapper.selectOne(queryWrapper);
        }
        if(formRegLib!=null){
            CacheUtil.set(cacheName,reg,formRegLib);
        }
        return formRegLib;
    }

    public boolean isExist(FormRegLib ent) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty(ent.getRegId())){
            wrapper.ne("REG_ID_",ent.getRegId());
        }
        Integer rtn= formRegLibMapper.selectCount(wrapper);
        return  rtn>0;
    }

    public void importRegLib(MultipartFile file, String treeId,String appId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入正则表达式列表:");

        JSONArray formRgeArray  = FormExOrImportHandler.readZipFile(file);

        for (Object obj:formRgeArray) {
            JSONObject regObj = (JSONObject)obj;
            JSONObject formReg = regObj.getJSONObject("formRegLib");
            if(BeanUtil.isEmpty(formReg)){
                continue;
            }

            String formRgeStr = formReg.toJSONString();
            FormRegLib formNewReg = JSONObject.parseObject(formRgeStr,FormRegLib.class);
            //导入正则表达式。
            sb.append(formNewReg.getName() +"("+formNewReg.getRegId()+"),");
            formNewReg.setAppId(appId);
            String id = formNewReg.getRegId();
            FormRegLib oldRge = get(id);
            if(BeanUtil.isNotEmpty(oldRge)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldRge.getAppId())) {
                    update(formNewReg);
                }else{
                    formNewReg.setRegId(IdGenerator.getIdStr());
                    insert(formNewReg);
                }
            }
            else{
                insert(formNewReg);
            }
            sb.append(",导入到分类:" + treeId);

            LogContext.put(Audit.DETAIL,sb.toString());
        }

    }
}
