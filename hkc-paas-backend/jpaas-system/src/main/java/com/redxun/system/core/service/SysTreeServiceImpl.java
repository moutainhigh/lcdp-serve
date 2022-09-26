package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.dto.form.AlterSql;
import com.redxun.system.core.entity.SysTree;
import com.redxun.system.core.mapper.SysTreeMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* [系统分类树]业务服务类
*/
@Service
public class SysTreeServiceImpl extends SuperServiceImpl<SysTreeMapper, SysTree> implements BaseService<SysTree> {

    @Resource
    private SysTreeMapper sysTreeMapper;

    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Override
    public BaseDao<SysTree> getRepository() {
        return sysTreeMapper;
    }

    /**
     * 查找该分类一级节点下的所有树
     * <pre>
     *     1.第一等级的PARENT_ID_为0
     *     2.如果有子节点，增加children 属性。
     * </pre>
     * @param catKey
     * @return
     */
    public List<SysTree> getTopNodesByCatKey(String catKey,String tenantId,String appId,String companyId){
        List<SysTree> list= sysTreeMapper.getTopNodesByCatKey(catKey,tenantId,appId,companyId);
        list.forEach(p->{
            if(p.getChildAmount()>0){
                p.setChildren(new ArrayList<>());
            }
        });
        return list;
    }

    /**
     * 查找某个树节点下的所有的节点列表
     * @param parentId
     * @return
     */
    public List<SysTree> getByParentId(String parentId,String companyId){
        List<SysTree> list= sysTreeMapper.getByParentId(parentId,companyId);
        list.forEach(p->{
              if(p.getChildAmount()>0){
                  p.setChildren(new ArrayList<>());
              }
        });
        return  list;
    }

    /**
    * @Description:  根据树ID，获取相应的appId
    * @param treeId 树ID
    * @Author: Elwin ZHANG  @Date: 2021/9/17 11:55
    **/
    public String getAppId(String treeId){
        if(StringUtils.isEmpty(treeId)){
            return null;
        }
        try {
            SysTree tree = sysTreeMapper.getById(treeId);
            return tree.getAppId();
        }catch (Exception e){
        }
        return null;
    }
    /**
     * 按树路径进行数据查询
     * @param path
     * @return
     */
    public List<SysTree> getByLeftLikePath(String path){
        return sysTreeMapper.getByLikePath(path+"%");
    }

    /**
     * 按左模糊匹配树进行删除
     * @param path
     */
    public void delByLeftPath(String path){
        sysTreeMapper.delByLeftPath(path+"%");
    }

    /**
     * 按左模糊匹配树进行删除-逻辑删除
     * @param path
     */
    public void updateByLeftPath(String path){
        sysTreeMapper.updateByLeftPath(path+"%");
    }

    /**
     * 取得其下所有子节点的数量
     * @param parentId
     * @return
     */
    public Long getChildCounts(String parentId){
        return  sysTreeMapper.getChildCounts(parentId);
    }

    public void delCascade(String treeId){
        SysTree sysTree=get(treeId);
        if(StringUtils.isNotEmpty(sysTree.getPath())) {
            //逻辑删除
            if (DbLogicDelete.getLogicDelete()) {
                updateByLeftPath(sysTree.getPath());
            }else {
                delByLeftPath(sysTree.getPath());
            }
            //若对于树节点的路径为空，同时删除本身
            delete(treeId);
        }
        //更新父节点Id是否为子节点
        if(StringUtils.isNotEmpty(sysTree.getParentId())
                && !"0".equals(sysTree.getParentId())){
            SysTree parentTree=get(sysTree.getParentId());
            if(parentTree!=null){
                Long count=getChildCounts(parentTree.getTreeId());
                if(count==0){
                    update(parentTree);
                }
            }
        }
    }
    /**
     * 取得catKey下节点
     * @param catKey 标识
     * @param tenantId  租户ID
     * @param appId 应用ID
     * @param isDic 应用取数据字典则获取全量
     * @return
     */
    public List<SysTree> getByCatKey(String catKey,String tenantId,String appId,boolean isDic) {
        String companyId= ContextUtil.getComanyId();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("CAT_KEY_",catKey);

        wrapper.eq(CommonConstant.TENANT_ID,tenantId);

        if(StringUtils.isNotEmpty(appId)) {
            if(isDic){
                String sql=" (APP_ID_='" + appId+ "' or APP_ID_ IS NULL OR APP_ID_ ='' ) ";
                wrapper.apply(sql);
            }else {
                wrapper.eq("APP_ID_ ", appId);
            }
        }
        //处理分公司
        if(!CommonConstant.COMPANY_ZERO.equals(companyId)){
            String sql=" (COMPANY_ID_='0' OR COMPANY_ID_ IS NULL OR COMPANY_ID_='"+companyId+"' ) ";
            wrapper.apply(sql);
        }

        wrapper.orderByDesc("SN_");
        wrapper.orderByDesc("CREATE_TIME_");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        return  sysTreeMapper.selectList(wrapper);
    }

    /**
     * 取得key下节点
     *
     * @param catKey
     * @param key
     * @return
     */
    public SysTree getByKey(String key,String catKey) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("CAT_KEY_", catKey);
        wrapper.eq("ALIAS_",key);
        return  sysTreeMapper.selectOne(wrapper);
    }
    public SysTree getById(String treeId){
        SysTree sysTree= sysTreeMapper.getById(treeId);
        if(sysTree.getChildAmount()>0){
            sysTree.setChildren(new ArrayList<>());
        }
        return sysTree;
    }

    public boolean isExist(SysTree sysTree){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("CAT_KEY_", sysTree.getCatKey());
        wrapper.eq("ALIAS_", sysTree.getAlias());
        if(StringUtils.isNotEmpty(sysTree.getTreeId())){
            wrapper.ne("TREE_ID_",sysTree.getTreeId());
        }
        int count=sysTreeMapper.selectCount(wrapper);
        return  count>0;
    }



    public List<AlterSql> importSysTreeZip(MultipartFile file, String treeId,String appId) {
        List<AlterSql> delaySqlList = new ArrayList<>();
        JSONArray sysTreeArray  = sysInvokeScriptService.readZipFile(file);
        for (Object obj:sysTreeArray) {
            JSONObject treeObj = (JSONObject)obj;
            JSONObject sysTree = treeObj.getJSONObject("sysTree");
            if(BeanUtil.isNotEmpty(sysTree)){
                String sysTreeStr = sysTree.toJSONString();
                SysTree sysNewTree = JSONObject.parseObject(sysTreeStr,SysTree.class);
                //sysNewTree.setCatKey(treeId);
                sysNewTree.setAppId(appId);
                String id = sysNewTree.getTreeId();
                SysTree oldTree = get(id);
                if(BeanUtil.isNotEmpty(oldTree)) {
                    //应用外，或应用ID相同才更新
                    if(StringUtils.isEmpty(appId) || appId.equals(oldTree.getAppId())){
                        update(sysNewTree);
                    }else{
                        String newId=IdGenerator.getIdStr();
                        sysNewTree.setTreeId(newId);
                        String newPath=sysNewTree.getPath().replace(id,newId);
                        sysNewTree.setPath(newPath);
                        insert(sysNewTree);
                    }
                }
                else{
                    insert(sysNewTree);
                }
            }
        }
        return delaySqlList;
    }

    public JSONObject doExportById(String id) {
        JSONObject json = new JSONObject();
        SysTree sysTree = get(id);
        json.put("sysTree", sysTree);
        return json;
    }


    /**
     * 查找该分类一级节点下的所有树
     * <pre>
     *     1.第一等级的PARENT_ID_为0
     *     2.如果有子节点，增加children 属性。
     * </pre>
     * @param catKey
     * @return
     */
    public List<SysTree> getNodesByCatKey(String catKey,String tenantId,String appId,String companyId){
        List<SysTree> list= sysTreeMapper.getNodesByCatKey(catKey,tenantId,appId,companyId);
        list.forEach(p->{
            if(p.getChildAmount()>0){
                p.setChildren(new ArrayList<>());
            }
        });
        return list;
    }
}


