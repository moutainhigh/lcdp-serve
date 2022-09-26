package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.PageResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.mapper.OsRelInstMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 关系实例Service业务层处理
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@Service
public class OsRelInstServiceImpl extends SuperServiceImpl<OsRelInstMapper, OsRelInst>  implements BaseService<OsRelInst> {

    @Resource
    private OsRelInstMapper osRelInstMapper;

    @Resource
    private OsUserServiceImpl osUserServiceImpl;
    @Resource
    private OsGroupServiceImpl osGroupServiceImpl;
    @Resource
    private OsRelTypeServiceImpl osRelTypeService;

    @Override
    public BaseDao<OsRelInst> getRepository() {
        return osRelInstMapper;
    }

    /**
     *
     * @param ids
     * @param relTypeId
     * @param groupId
     */
    public void unjoinRelInst(String ids, String relTypeId, String groupId) {
        if(BeanUtil.isEmpty(groupId) || BeanUtil.isEmpty(relTypeId)){
            return;
        }
        StringBuilder sb=new StringBuilder();
        String[] uid = ids.split("[,]");
        OsRelType osRelType=osRelTypeService.get(relTypeId);
        sb.append("将用户:");
        for (String id : uid) {
            if(OsRelType.REL_TYPE_GROUP_USER.equals(osRelType.getRelType())) {
                OsUser osUser = osUserServiceImpl.getById(id);
                sb.append(osUser.getFullName() + "(" + osUser.getUserNo() + ")");
            }else if(OsRelType.REL_TYPE_GROUP_GROUP.equals(osRelType.getRelType())){
                OsGroup osGroup = osGroupServiceImpl.getById(id);
                sb.append(osGroup.getName() + "(" + osGroup.getKey() + ")");
            }
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("PARTY1_", groupId);
            wrapper.eq("PARTY2_", id);
            wrapper.eq("REL_TYPE_ID_", relTypeId);
            osRelInstMapper.delete(wrapper);
        }

        sb.append("从关系类型:");
        sb.append( osRelType.getName() +"删除!");
        //加入日志。
        LogContext.put(Audit.DETAIL,sb.toString());
    }

    /**
     * 根据Party1查询列表
     * @param party1
     * @param isMain
     * @return
     */
    public List<OsRelInst> getByParty1(String party1,String isMain){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PARTY1_",party1);
        if(StringUtils.isNotEmpty(isMain)){
            queryWrapper.eq("IS_MAIN_",isMain);
        }
        return osRelInstMapper.selectList(queryWrapper);
    }

    public PageResult<OsRelInst> getUserListByGroupIdAndRelTypeId(Map<String, Object> params) {
        Page<OsRelInst> page = new Page<>(MapUtils.getInteger(params, "pageNum"), MapUtils.getInteger(params, "pageSize"));

        List<OsRelInst> list = osRelInstMapper.getUserListByGroupIdAndRelTypeId(page, params);

        long total = page.getTotal();
        return PageResult.<OsRelInst>builder().data(list).code(0).count(total).build();
    }

    public PageResult<OsRelInst> getGroupListByGroupIdAndRelTypeId(Map<String, Object> params) {
        Page<OsRelInst> page = new Page<>(MapUtils.getInteger(params, "pageNum"), MapUtils.getInteger(params, "pageSize"));

        List<OsRelInst> list = osRelInstMapper.getGroupListByGroupIdAndRelTypeId(page, params);

        long total = page.getTotal();
        return PageResult.<OsRelInst>builder().data(list).code(0).count(total).build();
    }

    public OsRelInst getByParty1Party2RelTypeId(String party1,String party2,String relTypeId) {
        return osRelInstMapper.getByParty1Party2RelTypeId(party1,party2,relTypeId);
    }

    public List<OsRelInst> getByRelTypeIdParty2(String relTypeId,String party2) {
        return osRelInstMapper.getByRelTypeIdParty2(relTypeId,party2);
    }



    public List<OsRelInst> getByRelTypeIdParty1(String tenantId, String relTypeId,String party1) {
        return osRelInstMapper.getByRelTypeIdParty1(tenantId,relTypeId,party1);
    }

    public void delByParty1Party2RelTypeId(String party1,String party2,String relTypeId) {
        osRelInstMapper.delByParty1Party2RelTypeId(party1,party2,relTypeId);
    }

    public List<OsRelInst> getByRelTypeIdPath(String relTypeId,String path){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("REL_TYPE_ID_",relTypeId);
        queryWrapper.eq("PATH_",path);
        return osRelInstMapper.selectList(queryWrapper);
    }

    public List<OsRelInst> getByPath(String path){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.likeRight("PATH_",path);
        queryWrapper.ne("PATH_",path);
        return osRelInstMapper.selectList(queryWrapper);
    }

    public List<OsRelInst> getByRelTypeIdParty1AndDim1(String id, String party1,String dimId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("REL_TYPE_ID_",id);
        queryWrapper.eq("PARTY1_",party1);
        queryWrapper.eq("DIM1_",dimId);
        return osRelInstMapper.selectList(queryWrapper);
    }
    public List<OsRelInst> getByRelTypeIdParty2AndDim1(String id, String party2,String dimId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        if(StringUtils.isNotEmpty(id)){
            queryWrapper.eq("REL_TYPE_ID_",id);
        }
        queryWrapper.eq("PARTY2_",party2);
        queryWrapper.eq("DIM1_",dimId);
        return osRelInstMapper.selectList(queryWrapper);
    }

    public void getRelInstChildren(String tenantId, String relTypeId,List<OsRelInst> relInstList) {
        for (OsRelInst relInst : relInstList) {
            List<OsRelInst> relInstChildren = getByRelTypeIdParty1(tenantId,relTypeId, relInst.getParty2());
            OsUser user = osUserServiceImpl.getById(relInst.getParty2());
            relInst.setFullname(user.getFullName());
            relInst.setChildren(relInstChildren);
            getRelInstChildren(tenantId,relTypeId,relInstChildren);
        }
    }

    /**
     * 判断当前关系方是否已经出现在上级的里，以使关系不要形成闭环.自身不跟自身建立关系
     * @param party1
     * @param party2
     * @param relTypeId
     * @return
     */
    public boolean isExistInParent(String party1, String party2, String relTypeId) {
        if(party2.equals(party1)){
            return true;
        }
        List<OsRelInst> osRelInsts= osRelInstMapper.getByRelTypeIdParty2(relTypeId,party1);

        for(OsRelInst is:osRelInsts){
            if(is.getParty1().equals(party2)){
                return true;
            }
            boolean isExist=isExistInParent(is.getParty1(), party2, relTypeId);
            if(isExist){
                return true;
            }
        }

        return false;
    }

    /**
     * 是否存在于关系中
     * @param relTypeId
     * @param party
     * @return
     */
    public boolean isPartyExistInRelation(String relTypeId,String party){
        Long cnt=osRelInstMapper.isPartyExistInRelation(party,party,relTypeId);
        if(cnt!=null && cnt>0){
            return true;
        }
        return false;
    }

    /**
     * 级联删除
     * @param osRelInst
     */
    public void delInstCascade(OsRelInst osRelInst) {
        if(StringUtils.isNotEmpty(osRelInst.getParty2())){
            delete(osRelInst.getInstId());
        }
        //取得其下级
        List<OsRelInst> childrenList=getByRelTypeIdParty1(osRelInst.getTenantId(), osRelInst.getRelTypeId(), osRelInst.getParty2());
        if(childrenList.size()==0){
            delete(osRelInst.getInstId());
        }
        for(OsRelInst childrenRel :childrenList){
            delInstCascade(childrenRel);
        }
    }

    /**
     * 更改当前关系的实例
     * @param instId
     * @param userId
     */
    public void doChangeInst(String instId,String userId){
        OsRelInst osRelInst=get(instId);
        String orgParty2=osRelInst.getParty2();
        if(StringUtils.isNotEmpty(userId)){
            String path = osRelInst.getPath();
            if(StringUtils.isNotEmpty(osRelInst.getPath())){
                String newPath=osRelInst.getPath().replace(osRelInst.getParty2()+".",userId+".");
                osRelInst.setPath(newPath);
            }
            osRelInst.setParty2(userId);
            update(osRelInst);
            List<OsRelInst> insts=getByPathRelTypeId(osRelInst.getRelTypeId(), path+"%");
            for(OsRelInst inst:insts){
                if(orgParty2.equals(inst.getParty1())) {
                    inst.setParty1(userId);
                }
                String newPath=inst.getPath().replace(orgParty2+".",userId+".");
                inst.setPath(newPath);
                update(inst);
            }
        }
    }

    /**
     * 根据类型id与路径获取关系定义集合
     * @param relTypeId
     * @param path
     * @return
     */
    public List<OsRelInst> getByPathRelTypeId(String relTypeId, String path) {
        return osRelInstMapper.getByPathRelTypeId(relTypeId, path);
    }

    /**
     * 转移关系定义到指定用户下
     * @param osRelInst 需要转移的关系
     * @param transferRel 需要转移到此关系下
     */
    public JsonResult transferRelation(OsRelInst osRelInst, OsRelInst transferRel) {
        osRelInst.setParty1(transferRel.getParty2());
        osRelInst.setPath(transferRel.getPath()+osRelInst.getParty2()+".");
        //取得其下级
        List<OsRelInst> childrenList=getByRelTypeIdParty1(osRelInst.getTenantId(), osRelInst.getRelTypeId(), osRelInst.getParty2());
        for (OsRelInst relInst : childrenList) {
            if(transferRel.getParty2().equals(relInst.getParty2())){
                return new JsonResult(false,"关系节点不能转移到该节点的下级！");
            }
        }
        if(childrenList.size()!=0){
            updatePathCascade(childrenList,osRelInst);
        }
        update(osRelInst);
        return new JsonResult(true,"关系节点转移成功！");
    }

    /**
     * 级联修改关系为指定路径下
     * @param osRelInstList //下级关系定义集合
     * @param parentInst 上级关系定义
     */
    public void updatePathCascade(List<OsRelInst> osRelInstList,OsRelInst parentInst) {
        for (OsRelInst curRelInst : osRelInstList) {
            curRelInst.setPath(parentInst.getPath()+curRelInst.getParty2()+".");
            List<OsRelInst> childrenList=getByRelTypeIdParty1(curRelInst.getTenantId(), curRelInst.getRelTypeId(), curRelInst.getParty2());
            if(childrenList.size()!=0){
                updatePathCascade(childrenList,curRelInst);
            }
            update(curRelInst);
        }

    }

    /**
     * 转移关系定义到主节点下
     * @param osRelInst 需要转移的关系
     */
    public JsonResult transferToMain(OsRelInst osRelInst) {
        osRelInst.setParty1(osRelInst.getRelTypeId());
        osRelInst.setPath("0."+osRelInst.getParty2()+".");
        List<OsRelInst> childrenList=getByRelTypeIdParty1(osRelInst.getTenantId(), osRelInst.getRelTypeId(), osRelInst.getParty2());
        if(childrenList.size()!=0){
            updatePathCascade(childrenList,osRelInst);
        }
        update(osRelInst);
        return new JsonResult(true,"关系节点转移成功！");
    }


    /**
     * 根据用户id删除关系
     * @param userId
     */
    public void deleteByUserId(String userId) {
        osRelInstMapper.deleteByUserId(userId);
    }

    /**
     *  添加组的从属关系。
     * @param groupId
     * @param userId
     * @param isMain
     */
    public void addBelongRelInst(String groupId,String userId,String isMain,String tenantId){
        OsRelInst inst=new OsRelInst();
        inst.setInstId(IdGenerator.getIdStr());
        inst.setDim1(OsDimension.DIM_ADMIN_ID);
        inst.setDim2(null);
        inst.setParty1(groupId);
        inst.setParty2(userId);
        inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
        inst.setRelTypeId(OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
        inst.setRelTypeKey(OsRelType.REL_CAT_GROUP_USER_BELONG);
        inst.setIsMain(isMain);
        inst.setRelType(OsRelType.REL_TYPE_GROUP_USER);
        inst.setStatus(MBoolean.ENABLED.name());
        inst.setTenantId(tenantId);

        osRelInstMapper.insert(inst);

    }



    public void deleteByParty2AndTenantId(String userId,String tenantId){
        /**
         * 删除用户的从属关系。
         */
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("party2_",userId);
        wrapper.eq("REL_TYPE_ID_",OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
        wrapper.eq("TENANT_ID_",tenantId);

        this.osRelInstMapper.delete(wrapper);
    }

    /**
     * 根据用户获取用户的组关系。
     * @param part2
     * @param tenantId
     * @return
     */
    public List<OsRelInst> getByPart2(String part2,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PARTY2_", part2);
        wrapper.eq("REL_TYPE_", "GROUP-USER");
        wrapper.eq("TENANT_ID_",tenantId);
        List<OsRelInst> relInsts = osRelInstMapper.selectList(wrapper);

        return  relInsts;
    }

    public List<OsRelInst> getByPart2(String part2,String dimId, String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PARTY2_", part2);
        wrapper.eq("DIM1_",dimId);
        wrapper.eq("TENANT_ID_",tenantId);
        List<OsRelInst> relInsts = osRelInstMapper.selectList(wrapper);

        return  relInsts;
    }

    /**
     * 通过用户组ID，关系ID，获取用户关系数据
     * @param relTypeId 关系ID
     * @param params
     * @return
     */
    public List<OsRelInst> getUserListByGroupId(String relTypeId,Map<String, Object> params) {
        return  osRelInstMapper.getUserListByGroupId(relTypeId,params);
    }

    /**
     * 查找某些部门或组下的某个角色的用户ID列表
     * @param groupIds 组Ids,如 1,2
     * @param roleIds  角色Ids 如 2,3
     * @return
     */
    public List<String> getUserIdsByGroupIdsRoleIds(String groupIds, String roleIds){
        List<String> groupIdList=Arrays.asList(groupIds.split("[,]"));
        List<String> roleIdList=Arrays.asList(roleIds.split("[,]"));
        return osRelInstMapper.getUserIdsByGroupIdsRoleIds(groupIdList,roleIdList);
    }

    /**
     * 查找同时处于组1与组2的
     * @param groupId 单用户组ID
     * @param roleId  单用户ID
     * @return
     */
    public List<String> getUserIdsByGroupId1GroupId2(String groupId1,String groupId2){
        return osRelInstMapper.getUserIdsByGroupId1GroupId2(groupId1,groupId2);
    }

    /**
     * 根据用户组ID或用户ID及关系标识Key查找用户ID
     * @param party1 用户组ID或用户ID
     * @param relTypeKey 关系标识Key
     * @return
     */
    public List<OsRelInst> getByParty1RelTypeKey(@Param("party1") String party1,@Param("relTypeKey") String relTypeKey){
        return osRelInstMapper.getByParty1RelTypeKey(party1,relTypeKey);
    }

    /**
     * 根据用户组ID或用户ID及关系标识Key查找用户ID
     * @param party1 用户组ID或用户ID
     * @param relTypeKey 关系标识Key
     * @return
     */
    public List<String> getUserIdsByParty1RelTypeKey(@Param("party1") String party1,@Param("relTypeKey") String relTypeKey){
        return osRelInstMapper.getUserIdsByParty1RelTypeKey(party1,relTypeKey);
    }

    /**
     * 删除主部门关系
     *
     **/
    public void deleteByParty2AndTenantIdAndIsMain(String userId,String tenantId){
        /**
         * 删除用户的从属关系。
         */
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("party2_",userId);
        wrapper.eq("REL_TYPE_ID_",OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
        wrapper.eq("IS_MAIN_",MBoolean.YES.name());
        wrapper.eq("TENANT_ID_",tenantId);
        this.osRelInstMapper.delete(wrapper);
    }

    public OsRelInst getGroupListByGroupIdAndRelTypeIdAndParty2(Map<String, Object> params) {
        return osRelInstMapper.getGroupListByGroupIdAndRelTypeIdAndParty2(params);
    }
    public List<OsRelInst> getUserListByGroupIdAndRelTypeIdAndDimId(String dimId,String groupId,String relTypeId,String tenantId) {
        Map<String, Object> params = new HashMap<>();
        params.put("dimId",dimId);
        params.put("groupId",groupId);
        params.put("relTypeId",relTypeId);
        params.put("tenantId",tenantId);
        List<OsRelInst> list = osRelInstMapper.getUserListByGroupIdAndRelTypeIdAndDimId(params);
        return list;
    }

    public IPage<OsRelInst> getUserAccessList(QueryFilter filter, Map<String,Object> params){
        return osRelInstMapper.getUserListByGroupIdAndRelTypeIdPage(filter.getPage(), params);
    }
}
