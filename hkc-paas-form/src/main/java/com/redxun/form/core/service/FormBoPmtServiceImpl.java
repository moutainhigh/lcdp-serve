package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.feign.SysMenuClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.core.entity.FormBoPmt;
import com.redxun.form.core.mapper.FormBoPmtMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* [业务单据数据权限]业务服务类
*/
@Service
public class FormBoPmtServiceImpl extends SuperServiceImpl<FormBoPmtMapper, FormBoPmt> implements BaseService<FormBoPmt> {

    @Resource
    private FormBoPmtMapper formBoPmtMapper;
    @Resource
    SysMenuClient sysMenuClient;


    @Override
    public BaseDao<FormBoPmt> getRepository() {
        return formBoPmtMapper;
    }

    public void deleteByBoListIdGroupId(String boListId) {
        //逻辑删除
        formBoPmtMapper.deleteByBoListId(boListId);
    }

    public List<FormBoPmt> getListByBoListIdAndGroupId(String boListId) {
        List<String> ids = new ArrayList<>();
        if (BeanUtil.isEmpty(ids)) {
            return null;
        }
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("PMT_ID_", ids);
        return formBoPmtMapper.selectList(queryWrapper);
    }



    public FormBoPmt get(String pmtId) {
        return formBoPmtMapper.selectById(pmtId);
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateFormBoPmt(FormBoPmt formBoPmt) {
        if(StringUtils.isNotBlank(formBoPmt.getPmtId())){
            update(formBoPmt);
        }else{
            formBoPmt.setPmtId(IdGenerator.getIdStr());
            save(formBoPmt);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(String id) {
        FormBoPmt formBoPmt=get(id);
        if(StringUtils.isNotEmpty(formBoPmt.getMenuId())){
            sysMenuClient.delSysMenus(formBoPmt.getMenuId());
        }
        //逻辑删除
        formBoPmtMapper.deleteByPmtId(id);
    }

    public List<FormBoPmt> getList(String boListId,String tenantId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("BO_LIST_ID_", boListId);
        queryWrapper.eq("TENANT_ID_", tenantId);
        List<FormBoPmt> list=formBoPmtMapper.selectList(queryWrapper);
        Set<String> menuIds=list.stream()
                .filter(item->StringUtils.isNotEmpty(item.getMenuId()))
                .map(item->item.getMenuId()).collect(Collectors.toSet());
        List<SysMenuDto> menuDtos=sysMenuClient.getMenuMenuIds(StringUtils.join(menuIds,","));
        Map<String,String> menuNameMap=new HashMap<>();
        for(SysMenuDto sysMenuDto:menuDtos){
            menuNameMap.put(sysMenuDto.getId(),sysMenuDto.getName());
        }
        for(int i=0;i<list.size();i++){
            FormBoPmt formBoPmt=list.get(i);
            if(StringUtils.isNotEmpty(formBoPmt.getMenuId())){
                String menuName=menuNameMap.get(formBoPmt.getMenuId());
                if(StringUtils.isEmpty(menuName)){
                    formBoPmt.setMenuId("");
                    formBoPmt.setStatus(MBoolean.NO.name());
                    update(formBoPmt);
                }
                formBoPmt.setMenuName(menuName);
            }
        }
        return list;
    }

    public FormBoPmt getByBoListIdMenuId(String boListId, String menuId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("BO_LIST_ID_", boListId);
        queryWrapper.eq("MENU_ID_",menuId);
        return formBoPmtMapper.selectOne(queryWrapper);
    }

    public void saveConfig(String dataRightJson,String boListId,String boListKey) {
        JSONObject json=JSONObject.parseObject(dataRightJson);
        if(!json.containsKey("loadStart") || !json.getBoolean("loadStart")){
            return;
        }
        deleteByBoListIdGroupId(boListId);
        JSONArray array=json.getJSONArray("data");
        for(Object obj:array){
            JSONObject jsonObject=(JSONObject)obj;
            FormBoPmt formBoPmt=JSONObject.toJavaObject(jsonObject,FormBoPmt.class);
            if(jsonObject.containsKey("isNew") && jsonObject.getBoolean("isNew")){
                formBoPmt.setPkId(IdGenerator.getIdStr());
            }
            insert(formBoPmt);
        }
    }

    public FormBoPmt getByAlias(String boListId, String pmtAlias) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("BO_LIST_ID_", boListId);
        queryWrapper.eq("ALIAS_",pmtAlias);
        return formBoPmtMapper.selectOne(queryWrapper);
    }
}
