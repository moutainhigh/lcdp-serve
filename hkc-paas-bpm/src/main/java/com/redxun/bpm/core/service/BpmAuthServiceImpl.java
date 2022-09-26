
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmInstPermission;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmAuth;
import com.redxun.bpm.core.mapper.BpmAuthMapper;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.annotation.Resource;

/**
* [流程授权表]业务服务类
*/
@Service
public class BpmAuthServiceImpl extends SuperServiceImpl<BpmAuthMapper, BpmAuth> implements BaseService<BpmAuth> {

    @Resource
    private BpmAuthMapper bpmAuthMapper;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    IOrgService orgService;
    @Resource
    BpmInstPermissionServiceImpl bpmInstPermissionService;

    @Override
    public BaseDao<BpmAuth> getRepository() {
        return bpmAuthMapper;
    }

    public boolean isExistProcess(String userId,String processKey){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TO_AUTH_USER_ID_",userId);
        queryWrapper.eq("PROCESS_KEY_",processKey);
        queryWrapper.eq("STATUS_", MBoolean.YES.name());
        int count=bpmAuthMapper.selectCount(queryWrapper);

        return count>0;
    }

    public void saveAddProcessArray(String userId, String defIds) {
        String[] defIdAry=defIds.split(",");
        OsUserDto osUserDto=orgService.getUserById(userId);
        for(String defId:defIdAry){
            BpmDef bpmDef=bpmDefService.get(defId);
            //不重复添加
            if(isExistProcess(userId,bpmDef.getKey())){
                continue;
            }
            BpmAuth bpmAuth=new BpmAuth();
            bpmAuth.setToAuthUserId(userId);
            bpmAuth.setToAuthUserName(osUserDto.getFullName());
            bpmAuth.setAuthUserId(ContextUtil.getCurrentUserId());
            bpmAuth.setAuthUserName(ContextUtil.getCurrentUser().getFullName());
            bpmAuth.setProcessKey(bpmDef.getKey());
            bpmAuth.setProcessName(bpmDef.getName());
            bpmAuth.setActiveTime("长期");

            insert(bpmAuth);
            bpmInstPermissionService.create(bpmDef,bpmAuth.getId(), BpmInstPermission.TYPE_ADMIN,osUserDto,"");
        }
    }

    public void saveAddUserArray(String defKey, String userIds) {
        String[] userIdAry=userIds.split(",");
        for(String userId:userIdAry){
            //不重复添加
            if(isExistProcess(userId,defKey)){
                continue;
            }
            BpmDef bpmDef=bpmDefService.getMainByKey(defKey);
            BpmAuth bpmAuth=new BpmAuth();
            OsUserDto osUserDto=orgService.getUserById(userId);
            bpmAuth.setToAuthUserId(userId);
            bpmAuth.setToAuthUserName(osUserDto.getFullName());
            bpmAuth.setAuthUserId(ContextUtil.getCurrentUserId());
            bpmAuth.setAuthUserName(ContextUtil.getCurrentUser().getFullName());
            bpmAuth.setProcessKey(bpmDef.getKey());
            bpmAuth.setProcessName(bpmDef.getName());
            bpmAuth.setActiveTime("长期");

            insert(bpmAuth);
            bpmInstPermissionService.create(bpmDef,bpmAuth.getId(),BpmInstPermission.TYPE_ADMIN,osUserDto,"");
        }
    }

    public void delAuth(String ids) {
        String[] aryId = ids.split(",");
        List<String> list = Arrays.asList(aryId);
        for(String authId:list){
            BpmAuth bpmAuth=get(authId);
            //失效
            bpmAuth.setStatus(MBoolean.NO.name());
            bpmAuth.setDelTime(new Date());
            bpmAuth.setDelUserId(ContextUtil.getCurrentUserId());
            bpmAuth.setDelUserName(ContextUtil.getCurrentUser().getFullName());
            update(bpmAuth);
            BpmInstPermission bpmInstPermission=bpmInstPermissionService.getById(authId);
            if(bpmInstPermission==null){
                continue;
            }
            bpmInstPermission.setStatus(MBoolean.NO.name());
            bpmInstPermission.setUpdateBy(ContextUtil.getCurrentUserId());
            bpmInstPermission.setUpdateTime(new Date());
            bpmInstPermissionService.update(bpmInstPermission);
        }
    }

    public List<BpmAuth> getByToAuthUserId(String currentUserId) {
        Map<String,Object> params=new HashMap<>();
        params.put("curUserId",currentUserId);
        params.put("status",MBoolean.YES.name());
        params.put("activeTime", DateUtils.getDate());
        return bpmAuthMapper.getByToAuthUserId(params);
    }

    public List<BpmAuth> getByToAuthUserId(String currentUserId,String treeId) {
        Map<String,Object> params=new HashMap<>();
        params.put("curUserId",currentUserId);
        params.put("treeId",treeId);
        params.put("status",MBoolean.YES.name());
        params.put("activeTime",DateUtils.getDate());
        return bpmAuthMapper.getByToAuthUserId(params);
    }

    public Integer getCountByToAuthUserId(String currentUserId,String treeId){
        Map<String,Object> params=new HashMap<>();
        params.put("curUserId",currentUserId);
        params.put("treeId",treeId);
        params.put("status",MBoolean.YES.name());
        params.put("activeTime",DateUtils.getDate());
        return bpmAuthMapper.getCountByToAuthUserId(params);
    }
}
