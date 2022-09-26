
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.datahandler.IDataHandler;
import com.redxun.form.core.entity.FormEntRelation;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.mapper.FormEntRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
* [数据关联删除约束]业务服务类
*/
@Service
public class FormEntRelationServiceImpl extends SuperServiceImpl<FormEntRelationMapper, FormEntRelation> implements BaseService<FormEntRelation> {

    @Resource
    private FormEntRelationMapper formEntRelationMapper;
    @Resource
    FormSolutionServiceImpl formSolutionService;
    @Resource
    FormPcServiceImpl formPcServiceImpl;
    @Resource
    FormBoEntityServiceImpl formBoEntityService;
    @Autowired
    private IDataHandler dataHandler;
    @Resource
    CommonDao commonDao;
    @Resource
    OsInstClient osInstClient;

    @Override
    public BaseDao<FormEntRelation> getRepository() {
        return formEntRelationMapper;
    }

    /**
     * 根据表单方案别名获取判断数据是否能删除
     * @param jsonObject
     * {
     *     alias:表单方案别名,
     *     ids:删除的数据的主键
     * }
     * @return
     */
    public JsonResult isDeleteByFormSolAlias(JSONObject jsonObject) {
        JSONArray jsonArray=new JSONArray();
        String ids = jsonObject.getString("ids");
        String[] deleteIds = ids.split(",");
        String alias = jsonObject.getString("alias");
        FormSolution formSolution = formSolutionService.getByAlias(alias);
        FormPc formPc = formPcServiceImpl.get(formSolution.getFormId());
        FormBoEntity boEntity= formBoEntityService.getByDefId(formPc.getBodefId(),false);
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq ("ENT_ID_",boEntity.getId());
        FormEntRelation formEntRelation = formEntRelationMapper.selectOne(queryWrapper);
        //有配置关联删除约束，则需要判断能否删除
        if(BeanUtil.isNotEmpty(formEntRelation)){
            for (int  i= 0; i <deleteIds.length; i++) {
                JSONObject jsonResult=new JSONObject();
                String deleteId = deleteIds[i];
                JSONObject data=dataHandler.getByPk(deleteId,boEntity);
                //获取关联的实体数据
                JSONArray relationData = getRelationData(data, formEntRelation);
                if(relationData.size()==0){
                    continue;
                }
                //提示字段
                String promptField = formEntRelation.getPromptField();
                JSONObject promptObj = JSONObject.parseObject(promptField);
                jsonResult.put("deleteId",deleteId);
                jsonResult.put("prompt",promptObj.getString("label"));
                jsonResult.put("value",data.getString(promptObj.getString("value")));
                jsonResult.put("relationData",relationData);
                jsonArray.add(jsonResult);
            }

        }
        return new JsonResult(true,jsonArray,"");
    }

    /**
     * 获取关联的数据
     * @param data
     * @param formEntRelation
     */
    private JSONArray getRelationData(JSONObject data,FormEntRelation formEntRelation) {
        JSONArray jsonArray=new JSONArray();
        String relationConfig = formEntRelation.getRelationConfig();

        if(StringUtils.isEmpty(relationConfig)) {
            return jsonArray;
        }
            JSONArray relationConfigs = JSONArray.parseArray(relationConfig);
            for (int i = 0; i < relationConfigs.size(); i++) {
                JSONObject relatedData=new JSONObject();
                JSONObject config = relationConfigs.getJSONObject(i);
                //当前方字段
                String fieldAlias = config.getString("fieldAlias");
                //关联方实体
                String relatedEnt = config.getString("relatedEnt");
                //关联方字段
                String relFieldAlias = config.getString("relFieldAlias");
                //删除类型 1(不允许删除)、2(允许删除不级联删除)、3(允许删除且允许级联删除)
                String deleteType = config.getString("deleteType");
                //提示信息
                String prompt = config.getString("prompt");
                if(StringUtils.isEmpty(fieldAlias) || StringUtils.isEmpty(relatedEnt) || StringUtils.isEmpty(relFieldAlias)){
                    continue;
                }
                //当前方字段值
                String value = (String) data.get(fieldAlias);
                if(StringUtils.isEmpty(value)){
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(relatedEnt);
                String relatedEntId = jsonObject.getString("value");
                FormBoEntity relatedEntity = formBoEntityService.get(relatedEntId);
                Long rtn =getData(relatedEntity,relFieldAlias,value);
                relatedData.put("relatedEntId",relatedEntity.getId());
                relatedData.put("relatedEntName",relatedEntity.getName());
                relatedData.put("data",rtn);

                //没用关联数据则直接允许删除
                if(rtn==0){
                    relatedData.put("isDelete",true);
                    relatedData.put("cascade",false);
                }else {
                    //1(不允许删除)、2(允许删除不级联删除)、3(允许删除且允许级联删除)
                    if("1".equals(deleteType)){
                        relatedData.put("isDelete",false);
                        relatedData.put("cascade",false);
                        //有不允许删除则直接返回
                        jsonArray.add(relatedData);
                        return jsonArray;
                    }else if("2".equals(deleteType)){
                        relatedData.put("isDelete",true);
                        relatedData.put("cascade",false);
                    }else if("3".equals(deleteType)){
                        relatedData.put("isDelete",true);
                        relatedData.put("cascade",true);
                    }else {
                        relatedData.put("isDelete",true);
                        relatedData.put("cascade",false);
                    }
                    relatedData.put("prompt",prompt);
                }
                jsonArray.add(relatedData);
            }

        return jsonArray;
    }

    public boolean isExist(FormEntRelation formEntRelation){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ENT_ID_",formEntRelation.getEntId());
        if(StringUtils.isNotEmpty(formEntRelation.getId())){
            wrapper.ne("ID_",formEntRelation.getId());
        }
        Integer rtn= formEntRelationMapper.selectCount(wrapper);
        return  rtn>0;
    }


    /**
     * 获取数据
     * @param relatedEntity
     * @param relFieldAlias
     * @param value
     * @return
     */
    private Long getData(FormBoEntity relatedEntity,String relFieldAlias,String value){
        Long rtn;
        //获取关联的数据
        String sql=" select count(*) from " + relatedEntity.getTableName()+" where "+ relFieldAlias+"="+value;
        SqlModel sqlModel=new SqlModel(sql);
        String dsAlias = getDatasource(relatedEntity);
        sqlModel.setDsName(dsAlias);

        if(StringUtils.isEmpty(sqlModel.getDsName())){
            rtn=(Long) commonDao.queryOne(sqlModel);
        }
        else{
            rtn=(Long) commonDao.queryOne(sqlModel.getDsName(),sqlModel);
        }
        return rtn;
    }

    /**
     * 获取数据源
     * @return
     */
    public String getDatasource(FormBoEntity boEntity) {
        String dsAlias = boEntity.getDsAlias();
        //判断当前是否为租户使用实体
        if("1".equals(boEntity.getIsTenant())){
            String tenantId = ContextUtil.getCurrentTenantId();
            OsInstDto instDto = osInstClient.getById(tenantId);
            if(StringUtils.isNotEmpty(instDto.getDatasource())){
                dsAlias=JSONObject.parseObject(instDto.getDatasource()).getString("value");
            }
        }
        return dsAlias;
    }

    /**
     * 根据实体与主键，删除关联数据
     * @param boEntity 实体
     * @param data  当前数据
     */
    public void delRelationData(FormBoEntity boEntity, JSONObject data) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq ("ENT_ID_",boEntity.getId());
        FormEntRelation formEntRelation = formEntRelationMapper.selectOne(queryWrapper);
        String relationConfig = formEntRelation.getRelationConfig();
        if(StringUtils.isNotEmpty(relationConfig)) {
            JSONArray relationConfigs = JSONArray.parseArray(relationConfig);
            for (int i = 0; i < relationConfigs.size(); i++) {
                JSONObject config = relationConfigs.getJSONObject(i);
                //当前方字段
                String fieldAlias = config.getString("fieldAlias");
                //关联方实体
                String relatedEnt = config.getString("relatedEnt");
                //关联方字段
                String relFieldAlias = config.getString("relFieldAlias");
                if (StringUtils.isEmpty(fieldAlias) || StringUtils.isEmpty(relatedEnt) || StringUtils.isEmpty(relFieldAlias)) {
                    continue;
                }
                //当前方字段值
                String value = (String) data.get(fieldAlias);
                if (StringUtils.isEmpty(value)) {
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(relatedEnt);
                String relatedEntId = jsonObject.getString("value");
                FormBoEntity relatedEntity = formBoEntityService.get(relatedEntId);
                delData(relatedEntity,relFieldAlias,value);
            }
        }
    }

    /**
     * 删除数据
     * @param relatedEntity
     * @param relFieldAlias
     * @param value
     * @return
     */
    private void delData(FormBoEntity relatedEntity,String relFieldAlias,String value){
        //获取关联的数据
        String sql="delete FROM " + relatedEntity.getTableName()+" where "+ relFieldAlias+"="+value;
        SqlModel sqlModel=new SqlModel(sql);
        String dsAlias = getDatasource(relatedEntity);
        sqlModel.setDsName(dsAlias);
        if(StringUtils.isEmpty(sqlModel.getDsName())){
             commonDao.execute(sqlModel);
        }
        else{
            commonDao.execute(sqlModel.getDsName(),sqlModel);
        }
    }
}
