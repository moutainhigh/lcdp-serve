
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysAppManager;
import com.redxun.system.core.mapper.SysAppManagerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [平台开发应用授权管理]业务服务类
*/
@Service
public class SysAppManagerServiceImpl extends SuperServiceImpl<SysAppManagerMapper, SysAppManager> implements BaseService<SysAppManager> {

    @Resource
    private SysAppManagerMapper sysAppManagerMapper;

    @Override
    public BaseDao<SysAppManager> getRepository() {
        return sysAppManagerMapper;
    }


    /**
    *  功能：批量保存某个应用的授权配置
    * @param appId  应用ID
    * @param rows 要保存记录集
    * @return boolean
    * @author  Elwin ZHANG
    * @date 2022/2/25 9:33
    **/
    public boolean batchSave(String appId, JSONArray rows ){
        if(rows==null || rows.size()==0){
            return  false;
        }
        if(StringUtils.isEmpty(appId)){
            return  false;
        }
        //先删除该应用之前的授权记录
        deleteByAppId(appId);

        //循环插入记录
        for(Object manager :rows){
            String text=JSONObject.toJSONString(manager);
            SysAppManager  row= JSONObject.parseObject(text,SysAppManager.class );
            if(StringUtils.isEmpty(row.getId())){
                row.setId(IdGenerator.getIdStr());
            }
            sysAppManagerMapper.insert(row);
        }
        return true;
    }

    /**
    *  功能：删除某个应用的权限配置
    * @param appId 应用Id
    * @author  Elwin ZHANG
    * @date 2022/2/28 16:44
    **/
    public void deleteByAppId(String appId){
        //先删除该应用之前的授权记录
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("APP_ID_", appId);
        sysAppManagerMapper.delete(wrapper);
    }

    /**
    *  功能：为某个应用添加当前用户为管理员角色
    * @param appId  应用ID
    * @author  Elwin ZHANG
    * @date 2022/2/28 10:46
    **/
    public void AddManager4CurUser(String appId){
        if(StringUtils.isEmpty(appId)){
            return  ;
        }
        IUser curUser=ContextUtil.getCurrentUser();
        if(curUser==null ){
            return;
        }
        SysAppManager manager=new SysAppManager();
        manager.setId(IdGenerator.getIdStr());
        manager.setAppId(appId);
        manager.setAuthType(2);
        manager.setIsGroup(0);
        manager.setUserOrGroupId(curUser.getUserId());
        manager.setUserOrGroupName(curUser.getFullName());
        sysAppManagerMapper.insert(manager);
    }

    /**
     *  功能：查找某用户的应用管理员权限
     * @param userId 用户ID
     * @param groupIds 用户所在组ID列表
     * @return com.redxun.system.core.entity.SysAppManager
     * @author  Elwin ZHANG
     * @date 2022/2/28 10:00
     **/
    public List<SysAppManager>  getManagerAuth(String userId,List<String>groupIds){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("AUTH_TYPE_", 2);
        String sql="(USER_OR_GROUP_ID_ = '" + userId + "' AND IS_GROUP_=0) ";
        //再按用户组过滤
        if(groupIds!=null && groupIds.size()>0){
            sql ="(" + sql + " OR ( USER_OR_GROUP_ID_ IN (";
            int cnt=groupIds.size();
            for(int i=0;i<cnt;i++){
                if(i<cnt-1){
                    sql += "'" +groupIds.get(i) + "',";
                }else {
                    sql += "'" +groupIds.get(i) + "')";
                }
            }
            sql += " AND IS_GROUP_=1 )) ";
        }
        wrapper.apply(sql);
        return sysAppManagerMapper.selectList(wrapper);
    }

    /**
    *  功能：查找某用户针对某个应用的权限,如果应用ID为空则查全部应用
    * @param appId 应用ID，如果传为空，则为所有应用
    * @param userId 用户ID
    * @param groupIds 用户所在组ID列表
     * @return com.redxun.system.core.entity.SysAppManager
    * @author  Elwin ZHANG
    * @date 2022/2/27 14:00
    **/
    public List<SysAppManager>  getByUserAndAppId(String appId,String userId,List<String>groupIds){
        QueryWrapper wrapper=new QueryWrapper();
        if(StringUtils.isNotEmpty(appId)) {
            wrapper.eq("APP_ID_", appId);
        }
        String sql="(USER_OR_GROUP_ID_ = '" + userId + "' AND IS_GROUP_=0) ";
        //再按用户组过滤
        if(groupIds!=null && groupIds.size()>0){
                sql ="(" + sql + " OR ( USER_OR_GROUP_ID_ IN (";
                int cnt=groupIds.size();
                for(int i=0;i<cnt;i++){
                    if(i<cnt-1){
                        sql += "'" +groupIds.get(i) + "',";
                    }else {
                        sql += "'" +groupIds.get(i) + "')";
                    }
                }
                sql += " AND IS_GROUP_=1 )) ";
        }
        wrapper.apply(sql);
        wrapper.orderByDesc("AUTH_TYPE_");
        return sysAppManagerMapper.selectList(wrapper);
    }


    /**
    *  功能：查找某个应用的所有开发者和管理员
    * @param appId 应用ID
    * @author  Elwin ZHANG
    * @date 2022/2/17 14:05
    **/
    public  List<SysAppManager> getByAppId(String appId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("APP_ID_",appId);
        wrapper.orderByAsc("AUTH_TYPE_","IS_GROUP_");
        return sysAppManagerMapper.selectList(wrapper);
    }

}
