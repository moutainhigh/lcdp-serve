package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysAuthRights;
import com.redxun.system.core.entity.SysAuthSetting;
import com.redxun.system.core.mapper.SysAuthSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
* [权限配置表]业务服务类
*/
@Service
public class SysAuthSettingServiceImpl extends SuperServiceImpl<SysAuthSettingMapper, SysAuthSetting> implements BaseService<SysAuthSetting> {

    @Resource
    private SysAuthSettingMapper sysAuthSettingMapper;

    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Autowired
    SysAuthRightsServiceImpl sysAuthRightsService;

    @Override
    public BaseDao<SysAuthSetting> getRepository() {
        return sysAuthSettingMapper;
    }

    public void importSysAuth(MultipartFile file, String treeId) {

        StringBuilder sb=new StringBuilder();
        sb.append("导入分类权限:");

        JSONArray sysAuthArray  = sysInvokeScriptService.readZipFile(file);
        for (Object obj:sysAuthArray) {
            JSONObject settingObj = (JSONObject)obj;
            JSONObject sysAuthSet = settingObj.getJSONObject("SysAuthSetting");
            if(BeanUtil.isEmpty(sysAuthSet)){
                continue;
            }
            String sysAuthSettingStr = sysAuthSet.toJSONString();
            SysAuthSetting sysAuthSetting = JSONObject.parseObject(sysAuthSettingStr,SysAuthSetting.class);

            sb.append(sysAuthSetting.getName() +"("+sysAuthSetting.getId()+"),");

            sysAuthSetting.setType(treeId);
            String id = sysAuthSetting.getId();
            SysAuthSetting oldSetting = get(id);
            if(BeanUtil.isNotEmpty(oldSetting)) {
                update(sysAuthSetting);
            }
            else{
                insert(sysAuthSetting);
            }

            JSONArray sysAuthRightList = settingObj.getJSONArray("SysAuthRights");
            if(BeanUtil.isNotEmpty(sysAuthRightList)){
                for(Object json:sysAuthRightList) {
                    JSONObject sysAuthRight=(JSONObject)json;
                    String sysAuthRightStr = sysAuthRight.toJSONString();
                    SysAuthRights sysAuthRights = JSONObject.parseObject(sysAuthRightStr, SysAuthRights.class);
                    String authRightsId = sysAuthRights.getId();
                    SysAuthRights oldRights = sysAuthRightsService.get(authRightsId);
                    if (BeanUtil.isNotEmpty(oldRights)) {
                        sysAuthRightsService.update(sysAuthRights);
                    } else {
                        sysAuthRightsService.insert(sysAuthRights);
                    }
                }
            }
        }
        sb.append(",导入分类:"+ treeId);

        LogContext.put(Audit.DETAIL,sb.toString());

    }


    public List<SysAuthSetting> getByType(String type) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ENABLE_", MBoolean.TRUE_LOWER.val);
        queryWrapper.eq("TYPE_",type);
        return sysAuthSettingMapper.selectList(queryWrapper);
    }
}
