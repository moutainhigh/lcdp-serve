package com.redxun.user.org.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.mapper.OsGroupMapper;
import com.redxun.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统用户组Service业务层处理
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@Service
public class OsGroupServiceImpl extends SuperServiceImpl<OsGroupMapper, OsGroup> implements BaseService<OsGroup> {

    @Resource
    private OsGroupMapper osGroupMapper;

    @Resource
    private OsRelInstServiceImpl osRelInstService;
    @Resource
    private OsGroupServiceImpl osGroupServiceImpl;
    @Resource
    private OsCompanyAuthServiceImpl osCompanyAuthService;
    @Resource
    private OsDimensionServiceImpl osDimensionService;

    @Override
    public BaseDao<OsGroup> getRepository() {
        return osGroupMapper;
    }

    /**
     * 根据用用户类型编码跟名称创建用户组
     * @param userTypeCode
     * @param userTypeName
     * @return
     */
    public OsGroup createUserTypeGroup(String userTypeCode, String userTypeName){
        String groupId= IdGenerator.getIdStr();
        OsGroup group=new OsGroup();
        group.setGroupId(groupId);
        group.setKey(userTypeCode);
        group.setName(userTypeName);

        group.setDimId(OsDimension.DIM_ROLE_ID);
        group.setStatus(MBoolean.ENABLED.name());
        group.setParentId("0");
        group.setPath("0."+groupId+".");
        group.setSn(0);
        //设置用户组是来自于用户类型
        group.setForm(OsGroup.FROM_USER_TYPE);
        save(group);

        return group;
    }


    /**
     * 组织父节点切换
     * @param groupId
     * @param parentId
     */
    public void changeParent(String groupId,String parentId){

        OsGroup osGroup=this.get(groupId);
        String path = osGroup.getPath();

        String detail="将用户组:"+osGroup.getName() +"("+osGroup.getGroupId()+")的上级组织修改为:";

        OsGroup parentGroup = this.get(parentId);

        detail+=parentGroup.getName() +"("+parentGroup.getGroupId()+")";

        LogContext.put(Audit.DETAIL,detail);

        String parentPath=parentGroup.getPath();

        String newPath=parentPath+ groupId+".";
        osGroup.setParentId(parentId);
        osGroup.setPath(newPath);
        this.update(osGroup);

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.like("PATH_",path+"%");
        List<OsGroup> childrens = osGroupMapper.selectList(queryWrapper);

        for (OsGroup group:childrens) {
            String chilPath = group.getPath();
            String[] chilPaths = chilPath.split(path);
            String newChilPath = newPath+chilPaths[1];
            group.setPath(newChilPath);
            this.update(group);
        }
    }

    public List<OsGroup> getOsGroupByExcludeGroupId(String groupId,String dimId,String parentId,String tenantId) {
        return  osGroupMapper.getOsGroupByExcludeGroupId(groupId,dimId,parentId,tenantId);
    }


    /**
     * 根据路径查询列表
     * @param path
     * @return
     */
    public List<OsGroup> getByLikePath(String path){
        return  osGroupMapper.getByLikePath(path);
    }

    /**
     * 按父类获取所有的节点Id
     * @param parentId
     * @return
     */
    public List<OsGroup> getByParentId(String parentId,Integer initRankLevel){
        return  osGroupMapper.getByParentId(parentId,initRankLevel);
    }

    /**
     * 取得用户的主部门
     * @param userId
     * @param tenantId
     * @return
     */
    public OsGroup getMainDeps(String userId,String tenantId){
        List<OsGroup> groups=osGroupMapper.getByDimIdUserIdRelTypeIdIsMain(OsDimension.DIM_ADMIN_ID,
                userId, OsRelType.REL_CAT_GROUP_USER_BELONG_ID,MBoolean.YES.name(),tenantId);
        if(groups.size()>0){
            return groups.get(0);
        }
        return null;
    }

    /**
     * 获取用户的从属部门
     * @param userId
     * @param tenantId
     * @return
     */
    public List<OsGroup> getCanDeps(String userId,String tenantId){
        List<OsGroup> groups=osGroupMapper.getByDimIdUserIdRelTypeIdIsMain(OsDimension.DIM_ADMIN_ID,
                userId,OsRelType.REL_CAT_GROUP_USER_BELONG_ID,MBoolean.NO.name(),tenantId);
        return groups;
    }

    /**
     * 取得候选的用户组，不含主部门
     * @param userId
     * @param tenantId
     * @return
     */
    public List<OsGroup> getCanGroups(String userId,String tenantId){
        List<OsGroup> groups=osGroupMapper.getByParty2RelType(userId,OsRelType.REL_TYPE_GROUP_USER, tenantId);
        List<OsGroup> tps=new ArrayList<>();
        for(OsGroup group:groups){
            if(group!=null && !OsRelType.REL_CAT_GROUP_USER_BELONG_ID.equals(group.getDimId())){
                tps.add(group);
            }
        }
        return tps;
    }

    /**
     * 取得用户从属于的用户组列表
     * @param userId
     * @param
     * @return
     */
    public List<OsGroup> getBelongGroups(String userId){
        Map<String,Object> params=new HashMap<>(16);
        params.put("userId", userId);
        params.put("relTypeId", OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
        return osGroupMapper.getByUserIdRelTypeId(params);
    }

    /**
     * 取得用户从属的部门组
     * @param userId
     * @return
     */
    public List<OsGroup> getBelongDeps(String userId){
        Map<String,Object> params=new HashMap<>(16);
        params.put("userId", userId);
        params.put("dimId", OsDimension.DIM_ADMIN_ID);
        params.put("relTypeId", OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
        return osGroupMapper.getByDimIdUserIdRelTypeId(params);
    }

    /**
     *  功能：根据编码 查找某行政组织, 不考虑租户
     * @param groupKey 组编码
     * @return com.redxun.user.org.entity.OsGroup
     * @author  Elwin ZHANG
     * @date 2022/3/10 15:35
     **/
    public OsGroup getDeptByKey(String groupKey){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DIM_ID_","1");
        queryWrapper.eq("KEY_",groupKey);
        return osGroupMapper.selectOne(queryWrapper);
    }


    /**
     * 根据路径查找用户组
     * @param path
     * @return
     */
    public List<OsGroup> getByPath(String path){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.likeRight("PATH_",path);
        return osGroupMapper.selectList(queryWrapper);
    }

    /**
     * 根据用户组Key获取用户组
     * @param groupKey
     * @return
     */
    public OsGroup getByKey(String groupKey) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",groupKey);
        return osGroupMapper.selectOne(queryWrapper);
    }

    /**
     * 按Key与TenantId查找
     * @param groupKey
     * @param tenantId
     * @return
     */
    public OsGroup getByKey(String groupKey,String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",groupKey);
        queryWrapper.eq("TENANT_ID_",tenantId);
        return osGroupMapper.selectOne(queryWrapper);
    }

    /**
     * 按dimId、Key与TenantId查找
     * @param dimId 维度ID
     * @param groupKey
     * @param tenantId
     * @return
     */
    public OsGroup getByDimIdGroupKey(String dimId,String groupKey,String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DIM_ID_",dimId);
        queryWrapper.eq("KEY_",groupKey);
        queryWrapper.eq("TENANT_ID_",tenantId);
        return osGroupMapper.selectOne(queryWrapper);
    }

    public List<OsGroup> getByDimId(String tenantId, String dimId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DIM_ID_",dimId);
        queryWrapper.eq(CommonConstant.TENANT_ID,tenantId);
        queryWrapper.orderBy(true,true,"SN_");
        return osGroupMapper.selectList(queryWrapper);
    }

    /**
     * 根据多个用户组ID获取用户组集合
     * 逻辑删除
     * @param groupIds
     * @return
     */
    public List<OsGroup> getByGroupIds(List<String> groupIds,String deleted){
        return osGroupMapper.getByGroupIds(groupIds,deleted);
    }

    /**
     * 根据钉钉ID获了用户组
     * @param dimId
     * @return
     */
    public OsGroup getByDdId(String dimId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.in("DD_ID_",dimId);
        return osGroupMapper.selectOne(queryWrapper);
    }

    /**
     * 权限分级管理
     * @param dimId
     * @return
     */
    public List<OsGroup> getRoleByDimId(String tenantId,String dimId,String userId, String companyId){

        return osGroupMapper.getByDimIdRole(tenantId,dimId,userId, companyId);

    }



    /**
     * 判断用户组是否存在
     * @param ent
     * @return
     */
    public boolean isExist(OsGroup ent) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",ent.getKey());
        //改为按租户校验key的唯一性
        String tenantId = StringUtils.isNotEmpty(ent.getTenantId())?ent.getTenantId():ContextUtil.getCurrentUser().getTenantId();
        queryWrapper.eq("TENANT_ID_",tenantId);
        if(StringUtils.isNotEmpty(ent.getGroupId())){
            queryWrapper.ne("GROUP_ID_",ent.getGroupId());
        }
        int count=osGroupMapper.selectCount(queryWrapper);

        return count>0;
    }

    /**
     * 根据用户组ID跟用户等级
     * @param groupId
     * @param initRankLevel
     * @return
     */
    public List<OsGroup> getOsGroupByGroupId(String groupId, Integer initRankLevel) {
        String path = "%";
        if(StringUtils.isNotEmpty(groupId)){
            path = getById(groupId).getPath() + path;
        }
        return  osGroupMapper.getOsGroupByGroupId(path,initRankLevel);
    }



    /**
     * 查询组织列表
     * @param dimId 维度id
     * @param parentId 父节点id
     * @return
     */
    public List<OsGroup> queryGroups(String tenantId,String dimId,String parentId) {
        return  osGroupMapper.queryGroups(tenantId,dimId,parentId);
    }


    /**
     * 添加机构时初始化行政组。
     * @param inst
     * @return
     */
    public OsGroup initInstGroup(OsInst inst){
        OsGroup osGroup=new OsGroup();
        String id=IdGenerator.getIdStr();
        osGroup.setGroupId(id);
        osGroup.setName(inst.getNameCn());
        osGroup.setKey(inst.getInstNo());
        osGroup.setRankLevel(1);
        osGroup.setParentId("0");
        osGroup.setPath("0."+id+".");
        osGroup.setDimId(OsDimension.DIM_ADMIN_ID);
        osGroup.setTenantId(inst.getInstId());
        osGroup.setStatus(MBoolean.ENABLED.name());
        osGroup.setSn(1);
        insert(osGroup);
        return osGroup;
    }

    /**
     * 获取当前人
     * @param userId
     * @param tenantId
     * @return
     */
    public List<String> getGroupsByUser(String userId,String tenantId){
        List<OsRelInst> osRelInsts= osRelInstService.getByPart2(userId, OsDimension.DIM_ADMIN_ID,tenantId);
        List<String> groupIds=new ArrayList<>();

        for(OsRelInst relInst:osRelInsts){
            OsGroup group= getById(relInst.getParty1());
            //若当前的目录路径为空,则让他找不到数据
            String groupPath= StringUtils.isEmpty(group.getPath())?"-1":group.getPath();
            List<OsGroup> list= getByPath(groupPath);
            for(OsGroup osGroup:list){
                groupIds.add(osGroup.getGroupId());
            }
        }
        return groupIds;
    }

    /**
     * 按父类获取所有的节点Id
     * @param parentId
     * @return
     */
    public JsonResult getGroupAndUserByParentId(String parentId,String dimId,String tenantId){
        JsonResult jsonResult=JsonResult.getSuccessResult("返回数据成功!").setShow(false);
        JSONObject jsonObject=new JSONObject();
        List<OsGroup> groups = osGroupMapper.getGroupAndUserByParentId(parentId, dimId, tenantId);
        jsonObject.put("groups",groups);
        //父ID为0时不需要拿下级组织
        if(!"0".equals(parentId)){
            Map<String, Object> params=new HashMap<>();
            params.put("tenantId",tenantId);
            params.put("groupId",parentId);
            List<OsRelInst> list = osRelInstService.getUserListByGroupId("1",params);
            jsonObject.put("users",list);
        }
        jsonResult.setData(jsonObject);
        return  jsonResult;
    }

    /**
     * 获取该组织所在的公司
     * <pre>
     *     1.向上查找找到类型为公司的用户组。
     *
     * </pre>
     * @param osGroup
     * @return
     */
    public OsGroup getCompany(OsGroup osGroup) {
        while(osGroup.getType() ==null || ( osGroup.getType()!=null &&  osGroup.getType().intValue()!=1)){
            if("0".equals( osGroup.getParentId())){
                return null;
            }
            osGroup=osGroupMapper.selectById(osGroup.getParentId());
        }
        return osGroup;
    }



    /**
     * 获取所有的下级用户组ids
     *
     * @param groupId
     * @return
     */
    public List<String> getAllSubGroupIds(String groupId,boolean includeSelf){
        List<OsGroup> all = new ArrayList<>();
        getSubGroupByParentId(groupId, all);
        List<String> list = all.stream().map(OsGroup::getGroupId).collect(Collectors.toList());
        if(includeSelf){
            list.add(groupId);
        }
        return list;
    }

    /**
     * 递归获取下级组织。
     * @param parentId
     * @param all
     */
    private void getSubGroupByParentId(String parentId, List<OsGroup> all){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("PARENT_ID_",parentId);
        List<OsGroup> subs =  osGroupMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(subs)){
            all.addAll(subs);
            for(OsGroup osGroup : subs){
                getSubGroupByParentId(osGroup.getGroupId(), all);
            }
        }
    }

    /**
     * 根据公司ID获取下级所有的分公司。
     * @param groupId
     * @param includeSelf
     * @return
     */
    public List<OsGroup> getCompanysById(String groupId,boolean includeSelf){
        List<OsGroup> all = new ArrayList<>();
        //递归查找
        getSubGroupByParentId(groupId, all);

        List<OsGroup> groups=all.stream().filter(item->{
            return OsGroup.TYPE_COMPANY.equals( item.getType());
        }).collect(Collectors.toList());

        if(includeSelf && !CommonConstant.COMPANY_ZERO.equals(groupId)){
            OsGroup osGroup=get(groupId);
            groups.add(0,osGroup);
        }

        return groups;

    }


    /**
     * 查询组织列表
     * @param dimId 维度id
     * @param parentId 父节点id
     * @return
     */
    public List<OsGroup> queryAllGroups(String tenantId,String dimId,String parentId) {
        return  osGroupMapper.queryGroups(tenantId,dimId,parentId);
    }

    /**
    * @Description: 根据传入的组IDs，获取其全部子孙组
    * @param groupIds 用户组id
    * @Author: Elwin ZHANG  @Date: 2021/11/24 11:45
    **/
    public List<OsGroup> getByRecursion(HashMap groupIds) {
        QueryWrapper queryWrapper=new QueryWrapper();
        groupIds.forEach((k, v) -> {
            String path="." + v+ ".";
            queryWrapper.or();
            queryWrapper.like("PATH_",path);
        });
        return osGroupMapper.selectList(queryWrapper);
    }
    /**
     * @description //同步组织架构批量更新
     **/
    @Transactional
    public void updateGroupBySync(List<OsGroup> detpartMents) {
        //部门数据处理
        for (OsGroup osGroup : detpartMents) {
            OsGroup groups = this.get(osGroup.getGroupId());
            if (groups != null) {
                //存在即更新
                this.update(osGroup);
            } else {
                //插入
                this.insert(osGroup);
            }
        }
    }

    /**
     *  功能：用户组批量导入
     * @param maps Excel中读取的数据集
     * @param override 相同编号的记录是否覆盖
     * @return java.lang.String 处理结果消息
     * @author  Elwin ZHANG
     * @date 2022/3/9 11:34
     **/
    @Transactional
    public String importData(List<Map<Integer, String>> maps ,boolean override){
        Map<Integer, String>  colTitle=maps.get(0);
        checkColumnTitle(colTitle);
        //保存成功的行号
        List<String> lstSuccess=new ArrayList<>( );
        //保存成失败的行号
        List<String> lstFail=new ArrayList<>( );
        //保存成跳过的行号
        List<String> lstSkip=new ArrayList<>( );
        int size=maps.size();
        //循环导入行
        for(int i=1;i<size;i++){
            importOne(maps.get(i),override,i+2,lstSuccess,lstFail,lstSkip);
        }
        String message="成功导入【" + lstSuccess.size() + "】条记录；\n";
        if(lstSkip.size()>0){
            message +="跳过【"+ lstSkip.size() + "】条记录；\n";
        }
        if(lstFail.size()>0){
            message +="失败【"+ lstFail.size() + "】条记录，失败的行号为：（" + StringUtils.join(lstFail,",") + ")";
        }
        return message;
    }

    /**
     *  功能：导入一条记录
     * @param map Excel中读取的数据行
     * @param override 相同编号的记录是否覆盖
     * @param lineNo 当前行号
     * @param lstSuccess 保存成功的行号
     * @param lstFail 保存成失败的行号
     * @param lstSkip  保存成跳过的行号
     * @author  Elwin ZHANG
     * @date 2022/3/9 16:05
     **/
    private void importOne(Map<Integer, String> map ,boolean override,int lineNo, List<String> lstSuccess, List<String> lstFail, List<String> lstSkip){
        boolean isAdd=true;     //是否新记录
        try{
            // 按顺序获取字段值，"组名称&组编码&行政等级&上级编码&维度&排序&描述"
            String name=map.get(0);
            String key=map.get(1);
            String dimId=map.get(2);
            String parentKey=map.get(3);
            String levelName=map.get(4);
            String sn=map.get(5);

            String tenantId=ContextUtil.getCurrentTenantId();
            //必填字段校验
            if(StringUtils.isEmpty(dimId) || StringUtils.isEmpty(name) || StringUtils.isEmpty(key)  ){
                lstFail.add(lineNo+"");
                return;
            }
            key=key.trim();
            if(StringUtils.isEmpty(parentKey)){
                parentKey="0";
            }
            parentKey=parentKey.trim();
            if(StringUtils.isEmpty(parentKey)){
                parentKey="0";
            }
            name=name.trim();
            OsGroup group=new OsGroup();
            //是否存在相同Key的记录
            OsGroup oldGroup= getByDimIdGroupKey(dimId,key,tenantId) ;
            if(oldGroup!=null){
                //不覆盖，则跳过
                if(!override){
                    lstSkip.add(lineNo+"");
                    return;
                }
                group=oldGroup;
                isAdd=false;
            }else {
                group.setGroupId(IdGenerator.getIdStr());
            }
            String groupId=group.getGroupId();
            //父节点编码和路径
            if("0".equals(parentKey)){
                group.setParentId("0");
                group.setPath("0."+groupId+".");
            }else {
                OsGroup parentGroup = getByDimIdGroupKey(dimId,parentKey,tenantId) ;
                //父节点Key不存在，则失败
                if(parentGroup==null){
                    lstFail.add(lineNo+"");
                    return;
                }
                group.setParentId(parentGroup.getGroupId());
                group.setPath(parentGroup.getPath() + groupId + ".");
            }
            group.setDimId(dimId);
            group.setName(name);
            group.setKey(key);
            //
            int level=0;

            if(StringUtils.isNotEmpty(levelName)){
                try {
                    level=Integer.parseInt(levelName);
                }catch (Exception ex){}
            }

            group.setRankLevel(level);

            int sort=1;
            try {
                sort=Integer.parseInt(sn);
            }catch (Exception ee){}
            group.setSn(sort);
            group.setStatus("ENABLED");
            group.setForm("import");
            if(isAdd){
                insert(group);
            }else {
                update(group);
            }
            lstSuccess.add(lineNo+"");
        }catch (Exception e){
            lstFail.add(lineNo+"");
        }
    }

    /**
     *  功能：校验EXCEL列头信息
     * @param map 列头信息
     * @author  Elwin ZHANG
     * @date 2022/3/9 11:56
     **/
    private void checkColumnTitle(Map<Integer, String> map){
        //组名称	组编码	维度	上级编码	等级	排序
        String strTitles="组名称&组编码&维度&上级编码&等级&排序";
        String [] titles=strTitles.split("&");
        if(titles.length!=map.size()){
            throw new RuntimeException("列数与模板文件中的不一致！");
        }
        for (int i=0;i<titles.length;i++  ) {
            if(!titles[i].equals(map.get(i))){
                throw new RuntimeException("列名【" +map.get(i) + "】与模板文件中的不一致！");
            }
        }
    }




    public List<OsGroup> getTopGroup(String tenantId,String dimId,String parentId){
        return osGroupMapper.getTopGroup(tenantId,dimId,parentId);
    }

    /**
     * 获取顶级的组织。
     * @param tenantId
     * @param dimId
     * @param companyId
     * @return
     */
    //逻辑删除
    public List<OsGroup> getByFlatCompany(String tenantId,String dimId,String companyId,String deleted){
        return osGroupMapper.getByFlatCompany(tenantId,dimId,companyId,deleted);
    }

    /**
     * 获取顶级的组织。
     * @param tenantId
     * @param dimId
     * @param parentId
     * @return
     */
    public List<OsGroup> getByTreeCompany(String tenantId,String dimId,String parentId,String companyId,String deleted){
        return osGroupMapper.getByTreeCompany(tenantId,dimId,parentId,companyId,deleted);
    }



    /**
     * 通过关系定义主键获取关联
     * @param relTypeId
     * @return
     */
    public List<OsGroup> getOsGroupsByRelTypeId(String relTypeId){
        return osGroupMapper.getOsGroupsByRelTypeId(relTypeId);
    }

    /**
     * 根据userId获取主部门
     * @param userId
     * @return
     */
    public OsGroup getMainGroup(String userId,String tenantId){
        return osGroupMapper.getMainGroup(userId,tenantId);
    }


    /**
     * 根据维度Id与用户组Key或者用户组名称返回用户组Id
     * @param groupJson
     * @return
     */
    public List<OsGroup> getByDimIdAndKeyOrName(JSONObject groupJson){
        Map<String,Object> params=new HashMap<>(16);
        params.put("dimId", groupJson.getString("dimId"));
        params.put("tenantId", groupJson.getString("tenantId"));
        String controlValType = groupJson.getString("controlValType");
        String groupVal = groupJson.getString("groupVal");
        if(StringUtils.isEmpty(groupVal)){
            return new ArrayList<>();
        }

        if("groupKey".equals(controlValType)){
            params.put("groupKey",groupVal);
        }else{
            params.put("groupName",groupVal);
        }

        return osGroupMapper.getByDimIdAndKeyOrName(params);
    }


    /**
     * 根据维度分公司来显示用户组。
     * <pre>
     *     这里分为了几种情况
     *     1.行政维度的情况
     *          1.分公司情况
     *              1.先返回器所在的分公司。
     *              2.点击返回其下的组织。
     *          2.总公司的情况
     *              1.返回父ID为0的公司
     *              2.点击返回其下的组织。
     *     2.维度为角色的情况
     *          1.分公司
     *              1.返回授权给分公司的角色
     *              2.返回公司自建的角色
     *          2.总公司
     *              1.返回公司ID为0的角色
     *     3.其他维度的情况
     *        1.按照维度类型各自获取本公司创建的数据。
     *
     * </pre>
     *
     * @param tenantId
     * @param dimId
     * @param parentId
     * @param companyId
     * @return
     */
    public List<OsGroup> getByDimId(String tenantId,String dimId,String parentId,String companyId){
        //逻辑删除
        String deleted=null;
        if (DbLogicDelete.getLogicDelete()) {
            deleted="0";
        }
        OsDimension osDimension=osDimensionService.getById(dimId);
        List<OsGroup> list=null;
        //公司分级管理，并且为第一次加载的情况。
        if(OsDimension.DIM_ADMIN_ID.equals(dimId)){
            //分公司
            if(!companyId.equals(CommonConstant.COMPANY_ZERO) && "0".equals(parentId) ){
                List<String> groups=new ArrayList<>();
                groups.add(companyId);
                list= osGroupServiceImpl.getByGroupIds(groups,deleted);
            }
            else{
                list=osGroupServiceImpl.queryGroups(tenantId,dimId,parentId);
            }
        }
        //角色处理
        else if(OsDimension.DIM_ROLE_ID.equals(dimId)){
            list=new ArrayList<>();
            //获取授权到的角色
            if(!CommonConstant.COMPANY_ZERO.equals( companyId)){
                List<OsCompanyAuth> auths= osCompanyAuthService.getByCompanyId(companyId);
                List<String> groups=auths.stream().map(item->item.getGroupId()).collect(Collectors.toList());
                if(BeanUtil.isNotEmpty(groups)){
                    List<OsGroup> roles= osGroupServiceImpl.getByGroupIds(groups,deleted);
                    roles.forEach(item->item.setIsPublic(true));
                    list.addAll(roles);
                }

            }
            //获取自己的角色。 //逻辑删除
            List<OsGroup> roles = osGroupServiceImpl.getByFlatCompany(tenantId, dimId, companyId,deleted);
            list.addAll(roles);
        }
        //其他维度，各自获取分公司创建的数据。
        else{
            companyId= WebUtil.getCompanyId(companyId);
            if(osDimension.getShowType().equals(OsDimension.SHOW_TYPE_FLAT)){
                //逻辑删除
                list=osGroupServiceImpl.getByFlatCompany(tenantId,dimId,companyId,deleted);
            }
            else{
                //逻辑删除
                list=osGroupServiceImpl.getByTreeCompany(tenantId,dimId,parentId,companyId,deleted);
            }
            list.forEach(item->{
                if(CommonConstant.COMPANY_ZERO.equals( item.getCompanyId())){
                    item.setIsPublic(true);
                }
            });
        }
        return list;
    }

    /**
     * 根据级别往上查找。
     * <pre>
     *     根据指定等级往上查找，一直找到指定等级的组织。
     * </pre>
     * @param groupId   指定的用户组ID
     * @param rankType  等级
     * @return
     */
    public OsGroup getByRankLevel(String groupId,Integer rankType){
        OsGroup osGroup=osGroupMapper.getByGroupId(groupId);
        if(rankType.equals(osGroup.getRankLevel())){
            return osGroup;
        }
        while(!"0".equals( osGroup.getParentId())){
            osGroup=osGroupMapper.getByGroupId(osGroup.getParentId());
            if(rankType.equals(osGroup.getRankLevel())){
                return osGroup;
            }
        }
        if(rankType.equals(osGroup.getRankLevel())){
            return osGroup;
        }
        return null;
    }

}
