
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.FormEntityDataSetting;
import com.redxun.form.core.entity.FormEntityDataSettingDic;
import com.redxun.form.core.entity.FormEntityDataType;
import com.redxun.form.core.mapper.FormEntityDataSettingMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

/**
 * [业务实体数据配置]业务服务类
 */
@Service
public class FormEntityDataSettingServiceImpl extends SuperServiceImpl<FormEntityDataSettingMapper, FormEntityDataSetting> implements BaseService<FormEntityDataSetting> {

    @Resource
    private FormEntityDataSettingMapper formEntityDataSettingMapper;
    @Resource
    FormEntityDataTypeServiceImpl formEntityDataTypeService;
    @Resource
    FormEntityDataSettingDicServiceImpl formEntityDataSettingDicService;

    @Override
    public BaseDao<FormEntityDataSetting> getRepository() {
        return formEntityDataSettingMapper;
    }

    public String queryDataByValue(String typeId) {
        List<String> values=new ArrayList<>();
        JsonResult queryData=queryData(typeId);
        if(queryData.isSuccess()){
            JSONArray data=(JSONArray)queryData.getData();
            for(Object obj:data){
                JSONObject json=(JSONObject)obj;
                values.add(json.getString("value"));
            }
        }
        return StringUtils.join(values,",");
    }

    public JsonResult queryData(String typeId) {
        FormEntityDataType formEntityDataType=formEntityDataTypeService.get(typeId);
        if(formEntityDataType==null){
            return JsonResult.getFailResult("权限类型不存在！");
        }
        JSONArray list=new JSONArray();
        //启用的类型进行查询
        if(MBoolean.ENABLED.val.equals(formEntityDataType.getStatus())){
            List<FormEntityDataSetting> settings=getByDataTypeId(typeId);
            if(settings==null){
                return JsonResult.getFailResult("无权限数据配置！");
            }
            parseGrantData(list,settings);
        }
        return JsonResult.getSuccessResult(list);
    }

    @Override
    public int insert(FormEntityDataSetting entity) {
        entity.setPkId(IdGenerator.getIdStr());
        createFormEntityDataSettingDicList(entity);
        return super.save(entity)?1:0;
    }

    private void createFormEntityDataSettingDicList(FormEntityDataSetting entity){
        formEntityDataSettingDicService.deleteBySettingId(entity.getId());
        if(BeanUtil.isNotEmpty(entity.getFormEntityDataSettingDicList())){
            List<FormEntityDataSettingDic> dicList=entity.getFormEntityDataSettingDicList();
            for(FormEntityDataSettingDic formEntityDataSettingDic:dicList){
                formEntityDataSettingDic.setSettingId(entity.getPkId());
                formEntityDataSettingDic.setPkId(IdGenerator.getIdStr());
            }
            formEntityDataSettingDicService.saveBatch(dicList);
        }
    }

    private void parseGrantData(JSONArray list, List<FormEntityDataSetting> settings){
        List<String> roles=ContextUtil.getCurrentUser().getRoles();
        List<String> ids=new ArrayList<>();
        List<String> names=new ArrayList<>();
        List<String> parentIds=new ArrayList<>();
        for(FormEntityDataSetting formEntityDataSetting:settings){
            String[] grantRoles=StringUtils.split(formEntityDataSetting.getRoleId());
            for(String role:grantRoles){
                if(roles.contains(role)){
                    if(BeanUtil.isNotEmpty(formEntityDataSetting.getFormEntityDataSettingDicList())) {
                        for(FormEntityDataSettingDic formEntityDataSettingDic:formEntityDataSetting.getFormEntityDataSettingDicList()){
                            String id=formEntityDataSettingDic.getIdValue();
                            String text=formEntityDataSettingDic.getTextValue();
                            String parentId=formEntityDataSettingDic.getParentValue();
                            if(!ids.contains(id)){
                                ids.add(id);
                                names.add(text);
                                parentIds.add(parentId);
                            }
                        }
                    }
                }
            }
        }
        for(int i=0;i<ids.size();i++){
            JSONObject json=new JSONObject();
            json.put("value",ids.get(i));
            json.put("label",names.get(i));
            json.put("parentId",parentIds.get(i));
            list.add(json);
        }
    }

    public List<FormEntityDataSetting> getByDataTypeId(String dataTypeId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DATA_TYPE_ID_",dataTypeId);
        List<FormEntityDataSetting> list=formEntityDataSettingMapper.selectList(queryWrapper);
        for(FormEntityDataSetting formEntityDataSetting:list){
            List<FormEntityDataSettingDic> dicList=formEntityDataSettingDicService.getBySettingId(formEntityDataSetting.getId());
            formEntityDataSetting.setFormEntityDataSettingDicList(dicList);
        }
        return list;
    }

    public IPage queryRole(QueryFilter filter) {
        Map<String, Object> params = PageHelper.constructParams(filter);
        IPage iPage = formEntityDataSettingMapper.queryRole(filter.getPage(), params);
        return iPage;
    }

    public FormEntityDataSetting getByRoleId(String roleId) {
        return formEntityDataSettingMapper.getByRoleId(roleId);
    }

    public FormEntityDataSetting getByRoleType(String roleId,String dataTypeId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ROLE_ID_",roleId);
        queryWrapper.eq("DATA_TYPE_ID_",dataTypeId);
        return formEntityDataSettingMapper.selectOne(queryWrapper);
    }

    public void deleteByRoleId(String roleId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ROLE_ID_",roleId);
        formEntityDataSettingMapper.delete(queryWrapper);
    }

    public JsonResult saveRole(String roleId, String roleName, JSONArray dataTypeList) {
        deleteByRoleId(roleId);
        for(Object obj:dataTypeList) {
            JSONObject dataType = (JSONObject) obj;
            String dataTypeId = dataType.getString("dataTypeId");
            String dataTypeName = dataType.getString("dataTypeName");
            JSONArray array = dataType.getJSONArray("dicList");
            List<FormEntityDataSettingDic> dicList = array.toJavaList(FormEntityDataSettingDic.class);
            FormEntityDataSetting formEntityDataSetting = new FormEntityDataSetting();
            formEntityDataSetting.setRoleId(roleId).setRoleName(roleName)
                    .setDataTypeId(dataTypeId).setDataTypeName(dataTypeName);
            formEntityDataSetting.setFormEntityDataSettingDicList(dicList);
            insert(formEntityDataSetting);
        }
        return JsonResult.Success("保存成功！");
    }
}
