package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.feign.org.OrgClient;
import com.redxun.system.core.entity.SysAuthRights;
import com.redxun.system.core.entity.SysAuthSetting;
import com.redxun.system.core.entity.SysTree;
import com.redxun.system.core.mapper.SysAuthRightsMapper;
import com.redxun.util.SysUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
* [权限定义表]业务服务类
*/
@Service
public class SysAuthRightsServiceImpl extends SuperServiceImpl<SysAuthRightsMapper, SysAuthRights> implements BaseService<SysAuthRights> {

    @Resource
    private SysAuthRightsMapper sysAuthRightsMapper;
    @Resource
    private SysAuthSettingServiceImpl sysAuthSettingService;
    @Resource
    private SysTreeServiceImpl sysTreeService;
    @Resource
    private OrgClient orgClient;

    @Override
    public BaseDao<SysAuthRights> getRepository() {
        return sysAuthRightsMapper;
    }

    /**
     * 权限查询
     * @param type 权限类型
     * @param readKey 权限字段
     * @param treeId 树ID
     * @return
     */
    public boolean findAuthRight(String type,String readKey,String treeId){
        JPaasUser user=(JPaasUser) ContextUtil.getCurrentUser();
        if(user.isAdmin()){
            return true;
        }
        SysTree sysTree = sysTreeService.get(treeId);
        Map<String,JSONObject> map = sysTree==null?null:getAllRightTreeId(type,readKey);
        if(BeanUtil.isEmpty(map)){
            return true;
        }
        return map.containsKey(treeId);
    }

    private String getRightSetting(JSONArray right,String[] ary){
        for(int i=0;i<ary.length;i++) {
            String setting = "";
            for (Object obj : right) {
                JSONObject json = (JSONObject) obj;
                if (json.getString("type").equals(ary[i])) {
                    if(json.containsKey("children") && i<(ary.length-1)) {
                        setting = getRightSetting(json.getJSONArray("children"), Arrays.copyOfRange(ary, i+1, ary.length));
                    }else{
                        setting = json.getString("setting");
                    }
                    return setting;
                }
            }
        }
        return null;
    }

    public List<SysAuthRights> getBySettingId(String pkId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("SETTING_ID_",pkId);
        return sysAuthRightsMapper.selectList(queryWrapper);
    }


    public Map<String,JSONObject> getAllRightTreeId(String type,String readKey){
        Map<String,JSONObject> authTreeIdMap=new HashMap<>();
        Map<String,JSONObject> authMap=new HashMap<>();
        //根据类型获取授权信息。
        List<SysAuthSetting> list=sysAuthSettingService.getByType(type);
        if(BeanUtil.isEmpty(list)){
            return authTreeIdMap;
        }
        Map<String, Set<String>> profiles = orgClient.getCurrentProfile();

        for(SysAuthSetting sysAuthSetting:list){
            String rightJson=sysAuthSetting.getRightJson();
            JSONArray right= JSONArray.parseArray(rightJson);
            String setting = getRightSetting(right,readKey.split("\\."));
            if(StringUtils.isNotEmpty(setting)){
                boolean hasRight = SysUtil.hasRights(setting, profiles);
                JSONObject rightSetting=setRightSetting(right, profiles);
                List<SysAuthRights> authRights=getBySettingId(sysAuthSetting.getId());
                for(SysAuthRights sysAuthRights:authRights){
                    String treeId=sysAuthRights.getTreeId();
                    authMap.put(treeId,rightSetting);
                    if(!hasRight){
                        continue;
                    }
                    JSONObject authSetting=authMap.get(treeId);
                    if(BeanUtil.isNotEmpty(authMap)){
                        for(String key:authSetting.keySet()){
                            Object obj=authSetting.get(key);
                            if(obj instanceof JSONObject){
                                JSONObject json=(JSONObject)obj;
                                for(String subKey:json.keySet()){
                                    Boolean flag=json.getBoolean(subKey);
                                    if(flag){
                                        rightSetting.getJSONObject(key).put(subKey,flag);
                                    }
                                }
                            }else if(obj instanceof Boolean){
                                if((Boolean)obj){
                                    rightSetting.put(key,obj);
                                }
                            }
                        }
                    }
                    authTreeIdMap.put(treeId,rightSetting);
                }

            }
        }
        return authTreeIdMap;
    }

    /**
     * 计算下一级分类权限
     * @param trees
     * @param sysTree
     * @param readKey
     * @param isAdmin
     * @param async
     * @param flag
     */
    private void parseSubAuthRight(Map<String,JSONObject> authTreeIdMap,List<SysTree> trees,SysTree sysTree,String readKey,boolean isAdmin,boolean async,boolean flag){
        String companyId=ContextUtil.getComanyId();
        //异步请求
        if(async && sysTree.getChildAmount()>0) {
            //获取下一级分类
            List<SysTree> subTree = sysTreeService.getByParentId(sysTree.getTreeId(),companyId);
            List<SysTree> grantSubTree = parseAuthRight(authTreeIdMap,subTree, readKey, isAdmin,async);
            if(!grantSubTree.isEmpty()) {
                if (!flag) {
                    trees.addAll(grantSubTree);
                }
            }
        }
    }

    public List<SysTree> parseAuthRight(Map<String,JSONObject> authTreeIdMap,List<SysTree> sysTrees,String readKey,boolean isAdmin,boolean async) {
        JPaasUser user=(JPaasUser) ContextUtil.getCurrentUser();
        Set<SysTree> trees = new HashSet<>();
        for (int i = 0; i < sysTrees.size(); i++) {
            SysTree sysTree = sysTrees.get(i);
            boolean adminFlag = isAdmin && user.isAdmin();
            if (adminFlag) {
                trees.add(sysTree);
                //异步请求
                parseSubAuthRight(authTreeIdMap,sysTrees, sysTree, readKey, isAdmin, async,true);
                continue;
            }
            //是否添加成功
            boolean flag = false;
            if(authTreeIdMap.containsKey(sysTree.getTreeId())){
               trees.add(sysTree);
               flag=true;
               sysTree.setRight(authTreeIdMap.get(sysTree.getTreeId()).toJSONString());
            }
            parseSubAuthRight(authTreeIdMap,sysTrees,sysTree,readKey,isAdmin,async,flag);
        }
        return new ArrayList<>(trees);
    }

    private JSONObject setRightSetting(JSONArray right,Map<String, Set<String>> profiles){
        JSONObject json = new JSONObject();
        for(Object obj:right) {
            JSONObject object = (JSONObject) obj;
            String type = object.getString("type");
            if(object.containsKey("children")){
                json.put(type,setRightSetting(object.getJSONArray("children"),profiles));
            }else{
                String setting = object.getString("setting");
                boolean hasRight = SysUtil.hasRights(setting, profiles);
                if (!json.containsKey(type)) {
                    json.put(type, hasRight);
                } else {
                    if (!json.getBoolean(type)) {
                        json.put(type, hasRight);
                    }
                }
            }
        }
        return json;
    }

    public void delBySettingId(String ids) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("SETTING_ID_",ids.split(","));
        sysAuthRightsMapper.delete(queryWrapper);
    }




}
