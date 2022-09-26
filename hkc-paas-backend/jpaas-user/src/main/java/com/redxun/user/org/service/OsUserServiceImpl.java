package com.redxun.user.org.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.redxun.api.sys.ISystemService;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.model.PageResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.*;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.dto.user.OsInstDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.profile.ProfileContext;
import com.redxun.user.org.entity.*;
import com.redxun.user.org.mapper.*;
import com.redxun.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 yjy
 */
@Slf4j
@Service
public class OsUserServiceImpl extends SuperServiceImpl<OsUserMapper, OsUser> implements BaseService<OsUser> {


    @Resource
    private OsUserMapper osUserMapper;

    @Resource
    private OsUserTypeServiceImpl osUserTypeService;

    @Override
    public BaseDao<OsUser> getRepository() {
        return osUserMapper;
    }

    @Resource
    private SystemClient systemClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private OsRelInstMapper osRelInstMapper;

    @Resource
    private OsRelTypeMapper osRelTypeMapper;

    @Resource
    private OsGroupMenuServiceImpl osGroupMenuService;

    @Resource
    private OsGroupMapper osGroupMapper;

    @Resource
    private ISystemService systemService;

    @Resource
    private OsInstUsersServiceImpl osInstUsersService;

    @Resource
    private OsInstServiceImpl osInstService;

    @Resource
    private OsInstTypeMenuServiceImpl osInstTypeMenuService;

    @Autowired
    OsGroupServiceImpl osGroupServiceImpl;

    @Resource
    private OsRelInstServiceImpl osRelInstService;

    @Resource
    private OsInstTypeMenuMapper osInstTypeMenuMapper;

    @Resource
    OsPasswordPolicyServiceImpl osPasswordPolicyService;

    @Resource
    OsPasswordInputErrorServiceImpl osPasswordInputErrorService;

    @Resource
    private OsUserPlatformServiceImpl osUserPlatformService;

    private static String YES = "YES";

    /**
     * 2021/5/21
     * @param entity
     */
    @Transactional
    public void insertByUserNo(OsUser entity){
        if(StringUtils.isEmpty( entity.getUserId())){
            entity.setPkId(IdGenerator.getIdStr());
        }
        super.save(entity);
        //创建用户关联关系。
        createInstUser(entity,entity.getTenantId());
        //创建用户组关系
        //先判断是否负责人，再加入到group里面
        createUserGroups(entity,entity.getTenantId());
    }

    public Map<String, OsGroup> handPath(Map<String, OsGroup> map, String groupid) {
        OsGroup group = map.get(groupid);
        String parentId = group.getParentId();
        String groupId = group.getGroupId();
        OsGroup parentGroup = map.get(parentId);
        if (parentId.equals("0")) {
            group.setPath("0." + groupId + ".");
        } else {
            if(BeanUtil.isEmpty(parentGroup)){
                return map;
            }
            if (parentGroup.getPath() != null && !("").equals(parentGroup.getPath())) {
                group.setPath(parentGroup.getPath() + groupId + ".");
            } else {
                handPath(map, parentId);
                /*if (parentGroup.getPath() != null && !("").equals(parentGroup.getPath())) {
                    group.setPath(parentGroup.getPath() + groupId + ".");
                }*/
            }
        }
        return map;
    }


    @Override
    @Transactional
    public int insert(OsUser entity) {
        String pwd =entity.getPwd();
        pwd = pwd.trim();
        pwd= passwordEncoder.encode(pwd);

        entity.setPwd(pwd);
        if(StringUtils.isEmpty( entity.getUserId())){
            entity.setPkId(IdGenerator.getIdStr());
        }

        super.save(entity);
        //创建用户关联关系。
        createInstUser(entity,entity.getTenantId());
        //创建用户组关系
        createUserGroups(entity,entity.getTenantId());
        return 0;
    }

    /**
     * 创建关联关系。
     * @param entity
     * @param tenantId
     */
    private void createInstUser(OsUser entity,String tenantId){
        OsInstUsers osInstUsers=new OsInstUsers();
        osInstUsers.setId(IdGenerator.getIdStr());
        osInstUsers.setUserId(entity.getUserId());
        osInstUsers.setTenantId(tenantId);
        osInstUsers.setUserType(entity.getUserType());
        osInstUsers.setStatus(OsUser.STATUS_IN_JOB);
        osInstUsers.setIsAdmin(0);
        osInstUsers.setCreateType("CREATE");
        osInstUsers.setApplyStatus("ENABLED");
        osInstUsersService.insert(osInstUsers);
    }

    private void updateInstUser(OsUser entity,String tenantId){
        OsInstUsers osInstUsers=osInstUsersService.getByUserTenant(entity.getUserId(),tenantId);
        osInstUsers.setUserType(entity.getUserType());
        osInstUsersService.update(osInstUsers);
    }



    @Override
    @Transactional
    public int update(OsUser entity) {
        super.updateById(entity);
        String tenantId=entity.getCurTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        updateInstUser(entity,tenantId);
        //删除旧的关系
        osRelInstService.deleteByParty2AndTenantId(entity.getUserId(),tenantId);
        //创建新的用户关系
        createUserGroups(entity,tenantId);
        return 0;
    }

    public void updateUser(OsUser entity){
        super.updateById(entity);
    }

    public void createUserGroups(OsUser entity,String tenantId){

        String relTypeId=entity.getRelTypeId();
        if(StringUtils.isEmpty(relTypeId)){
            relTypeId=OsRelType.REL_CAT_GROUP_USER_BELONG_ID;
        }
        OsRelType osRelType=osRelTypeMapper.selectById(relTypeId);
        //若主部门
        if(StringUtils.isNotEmpty(entity.getMainDepId())){
            OsGroup mainGroup=osGroupServiceImpl.get(entity.getMainDepId());
            OsRelInst inst=new OsRelInst();
            inst.setParty1(entity.getMainDepId());
            inst.setParty2(entity.getUserId());
            inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeId(osRelType.getId());
            inst.setDim1(OsDimension.DIM_ADMIN_ID);
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setPkId(IdGenerator.getIdStr());
            inst.setIsMain(MBoolean.YES.name());
            inst.setTenantId(mainGroup.getTenantId());
            osRelInstMapper.insert(inst);
        }

        //其他用户组
        if(StringUtils.isNotEmpty(entity.getCanGroupIds())){
            String[] canGroupIds=entity.getCanGroupIds().split("[,]");
            for(int i=0;i<canGroupIds.length;i++){
                String groupId=canGroupIds[i];
                OsGroup osGroup=osGroupMapper.selectById(groupId);
                OsRelInst inst=new OsRelInst();
                inst.setParty1(groupId);
                inst.setParty2(entity.getUserId());
                inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
                inst.setRelTypeKey(osRelType.getKey());
                inst.setRelType(osRelType.getRelType());
                inst.setRelTypeId(osRelType.getId());
                if(osGroup.getDimId()!=null){
                    inst.setDim1(osGroup.getDimId());
                }
                inst.setStatus(MBoolean.ENABLED.toString());
                inst.setPkId(IdGenerator.getIdStr());
                inst.setIsMain(MBoolean.NO.name());
                inst.setTenantId(tenantId);
                osRelInstMapper.insert(inst);
            }
        }
    }

    /**
     * 创建用户和用户关系
     * @param entity
     * @return
     */
    public int addUserAndRelations(OsUser entity) {
        String pwd =entity.getPwd();
        pwd = pwd.trim();
        pwd= passwordEncoder.encode(pwd);

        entity.setPwd(pwd);
        if(StringUtils.isEmpty( entity.getUserId())){
            entity.setPkId(IdGenerator.getIdStr());
        }

        super.save(entity);

        //插入租户和用户的关系。
        createInstUser(entity,entity.getTenantId());

        //所有关系类型
        List<OsRelType> osRelTypes = osRelTypeMapper.selectList(new QueryWrapper<>());
        Map<String, OsRelType> osRelTypeMap = osRelTypes.stream().collect(Collectors.toMap(OsRelType::getId, OsRelType -> OsRelType));

        createUserRelations(entity,osRelTypeMap);
        return 0;
    }

    /**
     * 新增用户关系
     * @param entity
     * relations: [{"relationType":"1","dimId":"1","part1":"2"}]
     */
    /**
     * 新增用户关系
     * @param entity
     * relations: [{"relationType":"1","dimId":"1","part1":"2"}]
     */
    public void createUserRelations(OsUser entity,Map<String, OsRelType> osRelTypeMap){

        String relTypeId=OsRelType.REL_CAT_GROUP_USER_BELONG_ID;

        OsRelType osRelType=osRelTypeMapper.selectById(relTypeId);
        //主部门
        if(StringUtils.isNotEmpty(entity.getMainDepId())){
            OsRelInst inst=new OsRelInst();
            inst.setParty1(entity.getMainDepId());
            inst.setParty2(entity.getUserId());
            inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeId(osRelType.getId());
            inst.setDim1(OsDimension.DIM_ADMIN_ID);
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setPkId(IdGenerator.getIdStr());
            inst.setIsMain(MBoolean.YES.name());
            inst.setTenantId(entity.getTenantId());
            osRelInstMapper.insert(inst);
        }
        //创建其它组关系与人员关系
        if(BeanUtil.isEmpty(entity.getRelations())){
            return;
        }

        JSONArray relations = entity.getRelations();
        for(int i=0;i<relations.size();i++){
            JSONObject relation=relations.getJSONObject(i);
            //关系类型不为空
            if(!relation.containsKey("relationType")){
                continue;
            }

            OsRelType relType = osRelTypeMap.get(relation.getString("relationType"));

            if(relation.containsKey("remove") && relation.getBoolean("remove")){
                QueryWrapper wrapper=new QueryWrapper();
                wrapper.eq("PARTY2_",entity.getUserId());
                wrapper.eq("REL_TYPE_ID_",relType.getId());
                wrapper.eq("PARTY1_",relation.get("part1"));
                osRelInstMapper.delete(wrapper);
                continue;
            }

            //查询关系是否已有
            OsRelInst osRelInst = osRelInstMapper.getByParty1Party2RelTypeId(relation.getString("part1"), entity.getUserId(), relType.getId());
            //已有关系不增加
            if(BeanUtil.isNotEmpty(osRelInst)){
                continue;
            }

            OsRelInst inst=new OsRelInst();
            inst.setParty1(relation.getString("part1"));
            inst.setParty2(entity.getUserId());
            inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
            inst.setPkId(IdGenerator.getIdStr());
            inst.setIsMain(MBoolean.NO.name());
            inst.setRelTypeKey(relType.getKey());
            inst.setRelType(relType.getRelType());
            inst.setRelTypeId(relType.getId());
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setDim1(relation.getString("dimId"));
            inst.setTenantId(entity.getTenantId());
            osRelInstMapper.insert(inst);
        }

    }

    public List<OsUser> getBelongUsers(String groupId){
        return osUserMapper.getListByGroupIdAndRelTypeId(groupId,OsRelType.REL_CAT_GROUP_USER_BELONG_ID);
    }


    public JPaasUser findByUsername(String username) {
        OsUser sysUser = null;
        int index = username.indexOf("@");
        if(index > -1){
            String domain = username.substring(index + 1);
            String tenanId = osInstService.findInstIdByDomain(domain);
            sysUser = osUserMapper.getByUsernameAndTenantId(username, tenanId);
        }else {
            sysUser = osUserMapper.getByUsername(username);
        }

        return getLoginAppUser(sysUser);
    }

    public JPaasUser getUserById(String userId) {
        OsUser sysUser = osUserMapper.getById(userId);
        return getLoginAppUser(sysUser);
    }


    public JPaasUser getByWxOpenId(String wxOpenId) {
        List<OsUser> users = baseMapper.selectList(
                new QueryWrapper<OsUser>().eq("WX_OPEN_ID_", wxOpenId)
        );
        if(BeanUtil.isEmpty(users) || users.size()>1){
            return null;
        }
        OsUser sysUser =users.get(0);
        return getLoginAppUser(sysUser);
    }

    public String isOtherUserContainWxOpenId(String username,String wxOpenId) {
        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("WX_OPEN_ID_",wxOpenId);
        queryWrapper.ne("USER_NO_",username);
        List<OsUser> users = baseMapper.selectList(queryWrapper);
        if(BeanUtil.isEmpty(users)){
            return "noContain";
        }
        return "contain";
    }


    public JPaasUser findByMobile(String username) {
        OsUser sysUser = osUserMapper.getByMobile(username);
        return getLoginAppUser(sysUser);
    }

    public JPaasUser getLoginAppUser(OsUser osUser) {
        if(osUser==null){
            return null;
        }
        JPaasUser jpaasUser = new JPaasUser();
        jpaasUser.setAccount(osUser.getUserNo());

        String tenantId = osUser.getTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        jpaasUser.setUserId(osUser.getUserId());
        jpaasUser.setAccount(osUser.getUserNo());
        jpaasUser.setFullName(osUser.getFullName());
        jpaasUser.setStatus(osUser.getStatus());
        jpaasUser.setPassword(osUser.getPwd());
        jpaasUser.setEmail(osUser.getEmail());
        jpaasUser.setMobile(osUser.getMobile());
        jpaasUser.setPhoto(osUser.getPhoto());

        jpaasUser.setTenantId(tenantId);
        jpaasUser.setUserType(osUser.getUserType());
        jpaasUser.setCreateTime(osUser.getCreateTime());
        jpaasUser.setIsLock(osUser.getIsLock());
        jpaasUser.setPwdUpdateTime(osUser.getPwdUpdateTime());
        jpaasUser.setIsFirstLogin(osUser.getIsFirstLogin());
        //超管
        boolean isAdmin=osUser.getAdmin()==1 || osUser.getAdmin()==2;
        jpaasUser.setAdmin(isAdmin);

        List<String> roles=getGroupsByUser(jpaasUser);

        // 设置角色
        jpaasUser.setRoles(roles);
        //设置主部门
        OsGroup mainGroup = osGroupMapper.getMainGroup(osUser.getUserId(),tenantId);
        if(BeanUtil.isNotEmpty(mainGroup)){
            jpaasUser.setDeptId(mainGroup.getGroupId());
            jpaasUser.setDeptName(mainGroup.getName());
        }
        /**
         * 设置平台。
         */
        setOsUserPlatform(jpaasUser);
        //设置分公司
        setCompany(jpaasUser,mainGroup);

        return jpaasUser;

    }

    /**
     * 设置当前用户的公司和子公司情况。
     * @param jPaasUser
     * @param mainGroup
     */
    private void setCompany(JPaasUser jPaasUser, OsGroup mainGroup){
        jPaasUser.setCompanyId(CommonConstant.COMPANY_ZERO);
        jPaasUser.setOriginCompanyId(CommonConstant.COMPANY_ZERO);
        //组织分级管理开关配置
        Boolean supportGrade = SysPropertiesUtil.getSupportGradeConfig();
        //不支持公司分级管理
        if(supportGrade==null || !supportGrade){
            return;
        }
        //不是超管
        if(jPaasUser.isAdmin()){
            return;
        }
        OsGroup company =osGroupServiceImpl.getCompany(mainGroup);
        if(company==null || StringUtils.isEmpty(company.getGroupId())){
            return;
        }
        jPaasUser.setOriginCompanyId(company.getGroupId());
        jPaasUser.setCompanyId(company.getGroupId());
    }

    /**
     * 设置用户平台
     * @param user
     */
    private void setOsUserPlatform(JPaasUser user ){
        List<OsUserPlatform> osUserPlatformList = osUserPlatformService.getOsUserPlatformByUserId(user.getUserId(),user.getTenantId());
        if(BeanUtil.isEmpty(osUserPlatformList)){
            return;
        }
        List<OsUserPlatformDto> platformDtos=new ArrayList<>();
        for(OsUserPlatform userPlatform:osUserPlatformList){
            OsUserPlatformDto dto=new OsUserPlatformDto(userPlatform.getPlatformType(),userPlatform.getOpenId());
            platformDtos.add(dto);
        }
        user.setPlatforms(platformDtos);

    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public OsUser getByUsername(String username) {
        OsUser osUser=osUserMapper.getByUsername(username);
        return osUser;
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public OsUser selectByDdId(String username) {
        List<OsUser> users = baseMapper.selectList(
                new QueryWrapper<OsUser>().eq("DD_ID_", username)
        );
        return getUser(users);
    }

    /**
     * @Description: 根据租户ID和DDID来查询用户
     * @param ddId 钉钉ID
     * @param tenantId 租户ID
     * @return com.redxun.user.org.entity.OsUser
     * @Author: Elwin ZHANG  @Date: 2021/4/15 9:49
     **/
    public JPaasUser selectByDDidAndTenantId(String ddId,String tenantId){
        QueryWrapper<OsUser> wrapper=new QueryWrapper<OsUser>();
        wrapper.eq("DD_ID_", ddId).eq("TENANT_ID_",tenantId);

        List<OsUser> users =osUserMapper.selectList(wrapper);
        if(BeanUtil.isEmpty(users) ){
            return null;
        }
        return getLoginAppUser(users.get(0));
    }

    /**
    * @Description: 根据租户ID和OpenId来查询用户
    * @param openId  企业微信的用户ID或OpenId
     * @param tenantId 租户ID
    * @return com.redxun.user.org.entity.OsUser
    * @Author: Elwin ZHANG  @Date: 2021/4/15 9:49
    **/
    public JPaasUser selectByOpenIdAndTenantId(String openId,String tenantId){
        QueryWrapper<OsUser> wrapper=new QueryWrapper<OsUser>();
        wrapper.eq("WX_OPEN_ID_", openId);
        if(tenantId!=null){
            wrapper.eq("TENANT_ID_", tenantId);
        }

        List<OsUser> users =osUserMapper.selectList(wrapper);
        if(BeanUtil.isEmpty(users) ){
            return null;
        }
        return getLoginAppUser(users.get(0));
    }

    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    public OsUser selectByDDId(String openId) {
        List<OsUser> users = baseMapper.selectList(
                new QueryWrapper<OsUser>().eq("DD_ID_", openId)
        );
        return getUser(users);
    }

    private OsUser getUser(List<OsUser> users) {
        OsUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    /**
     * 给用户设置角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void setRoleToUser(Long id, Set<Long> roleIds) {
        OsUser sysUser = baseMapper.selectById(id);
        if (sysUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult updatePassword(String id, String oldPassword, String newPassword) {
        JsonResult jsonResult=new JsonResult();
        OsUser sysUser = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(oldPassword)) {
            if (!passwordEncoder.matches(oldPassword, sysUser.getPwd())) {
                return jsonResult.setMessage("旧密码错误").setSuccess(false);
            }
        }
        if (StrUtil.isBlank(newPassword)) {
            return jsonResult.setMessage("新密码不能为空").setSuccess(false);
        }

        JsonResult checkPasswordResult = osPasswordPolicyService.checkPassword(ContextUtil.getCurrentUser().getAccount(), newPassword);
        if(!checkPasswordResult.getSuccess()){
            return checkPasswordResult;
        }

        OsUser user = new OsUser();
        user.setUserId(id);
        user.setPwd(passwordEncoder.encode(newPassword));
        osUserMapper.updatePassword(user.getPwd(),new Date(),ContextUtil.getCurrentUserId(),id);
        //baseMapper.updateById(user);
        return jsonResult.setMessage("修改密码成功").setSuccess(true);
    }

    public PageResult<OsUser> findUsers(Map<String, Object> params) {
        Page<OsUser> page = new Page<>(MapUtils.getInteger(params, "pageNum"), MapUtils.getInteger(params, "pageSize"));
        List<OsUser> list = baseMapper.findList(page, params);
        long total = page.getTotal();
        return PageResult.<OsUser>builder().data(list).code(0).count(total).build();
    }

    public Result updateEnabled(Map<String, Object> params) {
        Long id = MapUtils.getLong(params, "id");
        String status = MapUtils.getString(params, "status");

        OsUser osUser = baseMapper.selectById(id);
        if (osUser == null) {
            return Result.failed("用户不存在");
        }

        String statusStr=OsUser.STATUS_IN_JOB.equals(status)?"启用":"禁用";
        String detail="修改用户:" +osUser.getFullName() +"("+osUser.getUserNo()+")的状态为:" +statusStr;


        osUser.setStatus(status);
        osUser.setUpdateTime(new Date());

        int i = baseMapper.updateById(osUser);

        String result=i>0?"更新成功":"更新失败";
        detail+=result;

        LogContext.put(Audit.DETAIL,detail);

        return i > 0 ? Result.succeed(osUser, "更新成功") : Result.failed("更新失败");
    }



    public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
        List<SysUserExcel> sysUserExcels = new ArrayList<>();
        List<OsUser> list = baseMapper.findList(new Page<>(1, -1), params);

        for (OsUser sysUser : list) {
            SysUserExcel sysUserExcel = new SysUserExcel();
            BeanUtils.copyProperties(sysUser, sysUserExcel);
            sysUserExcels.add(sysUserExcel);
        }
        return sysUserExcels;
    }

    /**
     * 按条件（含用户组Id)查找其下用户并分页返回
     * @param filter
     * @return
     */
    public IPage<OsUser> searchByBelongGroupId(QueryFilter filter){
        Map<String,Object> params=new HashMap<>();
        String groupId=(String)filter.getParams().get("groupId");
        String tenantId=(String)filter.getParams().get("tenantId");
        IUser user=ContextUtil.getCurrentUser();
        if(StringUtils.isNotEmpty(groupId)){

            Iterator<String> it=filter.getParams().keySet().iterator();
            while (it.hasNext()){
                String key=it.next();
                if(key.startsWith("Q_")) {
                    params.put("u." + key, filter.getParams().get(key));
                }
            }
            filter.setParams(params);
            filter.getParams().put("groupId",groupId);
            filter.getParams().put("tenantId",tenantId);
        }


        //启用组织分级管理 && 非超管
        if( !CommonConstant.COMPANY_ZERO.equals(user.getCompanyId())){
            //获取所有分公司id
            List<String> groupIdList = osGroupServiceImpl.getAllSubGroupIds(user.getCompanyId(),true);


            filter.getParams().put("groupIds",  String.join(",", groupIdList));
        }

        return osUserMapper.searchByBelongGroupId(filter.getPage(), filter.getSearchParams());
    }


    /**
     * 租户管理员获取菜单资源。
     * <pre>
     *     1.根据 机构类型获取菜单。
     *     2.根据当前租户获取私有菜单。
     * </pre>
     * @param jPaasUser
     * @return
     */
    private List<SysMenuDto> getTenantAdminMenu(JPaasUser jPaasUser,String appId){
        String tenantId=jPaasUser.getTenantId();
        OsInst osInst=osInstService.getById(tenantId);
        String instTypeId=osInst.getInstType();

        Set<SysMenuDto> menuList=new HashSet<>();
        //如果是非根租户管理员的情况，获取他私有的菜单 在和机构类型的菜单进行合并。
        if(!OsInstDto.ROOT_INST .equals( tenantId)){
            List<SysMenuDto> tenantMenus= systemService.getMenusByTenantId(tenantId);
            if(tenantMenus!=null){
                menuList.addAll(tenantMenus);
            }
        }

        List<SysMenuDto> instTypeMenus  =  osInstTypeMenuService.getMenuDaoByInstTypeId(instTypeId,appId);
        menuList.addAll(instTypeMenus);

        // 查询租户安装应用菜单
//        List<SysMenuDto> tenantOrUserAppInstallMenu = systemClient.getTenantOrUserAppInstallMenu(tenantId, "");
//        if(BeanUtil.isNotEmpty(tenantOrUserAppInstallMenu)){
//            menuList.addAll(tenantOrUserAppInstallMenu);
//        }

        // 菜单应用ID
        Collection<String> appIdList = menuList.stream().map(SysMenuDto::getAppId).distinct().collect(Collectors.toList());

        List<SysMenuDto> result = Lists.newArrayList();

        if(BeanUtil.isEmpty(appIdList)){
            return result;
        }

        List<SysAppDto> apps = systemService.getAppsByIds(String.join(",", appIdList));
        //构建菜单。
        constructMenu(result,apps,menuList);

        return result;
    }


    /**
     * 获取当前用户的用户组。
     * <pre>
     *     1.获取用户的组关系
     *     2.根据用户类型获取。
     * </pre>
     * @param jPaasUser
     * @return
     */
    private List<String> getGroupsByUser(JPaasUser jPaasUser){
        List<OsRelInst> relInsts = osRelInstService.getByPart2(jPaasUser.getUserId(),jPaasUser.getTenantId());
        //组ID集合
        Set<String> userGroupIds = new HashSet<>();
        if(BeanUtil.isNotEmpty(relInsts)){
            userGroupIds = relInsts.stream().map(OsRelInst::getParty1).collect(Collectors.toSet());
        }
        List<String> roles=new ArrayList<>();
        roles.addAll(userGroupIds);
        //获取用户类型组ID
        if(StringUtils.isNotEmpty(jPaasUser.getUserType())){
            OsUserType osUserType=osUserTypeService.getByCode(jPaasUser.getUserType(),jPaasUser.getTenantId());
            if(osUserType!=null && StringUtils.isNotEmpty(osUserType.getGroupId())) {
                roles.add(osUserType.getGroupId());
            }
        }

        return roles;
    }


    private List<SysMenuDto> getCommonUserMenu(JPaasUser jPaasUser,String appId){

        List<String> roles=getGroupsByUser(jPaasUser);

        if(BeanUtil.isEmpty(roles)){
            return Lists.newArrayList();
        }

        Set<SysMenuDto> menuDtoSet=new HashSet<>();

        if(BeanUtil.isNotEmpty(roles)){
            List<SysMenuDto> groupMenus = osGroupMenuService.getSysMenuByGroupIds(roles,appId);
            if(BeanUtil.isNotEmpty(groupMenus)){
                menuDtoSet.addAll(groupMenus);
            }
        }

        // 查询租户安装应用菜单
//        List<SysMenuDto> tenantOrUserAppInstallMenu = systemClient.getTenantOrUserAppInstallMenu("", jPaasUser.getUserId());
//        if(BeanUtil.isNotEmpty(tenantOrUserAppInstallMenu)){
//            menuDtoSet.addAll(tenantOrUserAppInstallMenu);
//        }

        if(BeanUtil.isEmpty(menuDtoSet)){
            return Collections.EMPTY_LIST;
        }


        // 安装应用ID
        List<String> appIdList = menuDtoSet.stream().map(item -> item.getAppId()).distinct().collect(Collectors.toList());

        if(BeanUtil.isEmpty(appIdList)){
            return Collections.EMPTY_LIST;
        }

        List<SysAppDto> apps = systemService.getAppsByIds(String.join(",", appIdList));

        List<SysMenuDto> result = Lists.newArrayList();
        //构建菜单。
        constructMenu(result,apps,menuDtoSet);

        return result;
    }

    /**
     * 根租户管理员获取所有菜单。
     * @return
     */
    private List<SysMenuDto> getRootAdminMenu(){
        // 查询租户安装应用菜单
        return systemClient.getAllMenus();
    }



    /**
     * 根据用户查询菜单
     * @param user 用户信息
     * @return 菜单列表
     */
    public List<SysMenuDto> selectMenusByUser(IUser user) {
        JPaasUser jPaasUser = (JPaasUser) user;
        //不启用组织分级管理
        //根租户管理员获取所有菜单
        if (jPaasUser.isRootAdmin()) {
            List<SysMenuDto>  list= getRootAdminMenu();
            return  list;
        }
        // 管理员显示所有菜单信息
        if (jPaasUser.isAdmin()){
            List<SysMenuDto>  list= getTenantAdminMenu(jPaasUser,"");
            return  list;
        }
        //获取普通用户的数据。
        List<SysMenuDto>  result=getCommonUserMenu(jPaasUser,"");
        return result;

    }
    /**
     * 根据公司管理员查询菜单
     *
     * @param jPaasUser 用户信息
     * @return 菜单列表
     */
    private List<SysMenuDto> getCompanyUserMenu(JPaasUser jPaasUser) {
        List<SysMenuDto> menus = systemService.getCompanyMenus(jPaasUser.getCompanyId());
        List<SysAppDto> apps = systemService.getCompanyApps(jPaasUser.getCompanyId());

        ArrayList<String> objects = new ArrayList<>();

        List<SysMenuDto> result = Lists.newArrayList();
        Collection<String> parentIds = menus.stream().map(SysMenuDto::getParentId).distinct().collect(Collectors.toList());
        Collection<String> newParentIds = BeanUtil.deepCopyBean(parentIds);
        for (String parentId : parentIds) {
            for (SysMenuDto sysMenuDto : menus) {
                if (parentId.equals(sysMenuDto.getId())) {
                    newParentIds.remove(parentId);
                }
            }
        }

        objects.clear();
        for (String newParentId : newParentIds) {
            newParentId = "'" + newParentId + "'";
            objects.add(newParentId);
        }

        if(objects.size()==0){
            return null;
        }

        List<SysMenuDto> parentMenus = systemService.getMenusByIdsAndType(String.join(",", objects), "");
        if (BeanUtil.isNotEmpty(parentMenus)) {
            menus.addAll(parentMenus);
        }
        //构建菜单。
        constructMenu(result, apps, menus);

        return result;
    }

    private SysMenuDto constructMenu(SysAppDto app){
        SysMenuDto appMenu = new SysMenuDto();

        appMenu.setId(app.getAppId());
        appMenu.setAppId(app.getAppId());
        appMenu.setName(app.getClientName());
        appMenu.setParentId("0");
        appMenu.setParams(app.getParams());
        appMenu.setSettingType(app.getHomeType());
        if("custom".equals(app.getHomeType())){
            appMenu.setComponent(app.getHomeUrl());
        }else if("iframe".equals(app.getHomeType())){
            appMenu.setComponent("UrlView");
            appMenu.setParams(app.getHomeUrl());
        }else{
            appMenu.setComponent("PageView");
        }
        appMenu.setShowType(app.getUrlType());
        appMenu.setMenuKey(app.getClientCode());
        appMenu.setIconPc(app.getIcon());
        appMenu.setMenuType("C");
        if(StringUtils.isNotEmpty(app.getLayout())){
            appMenu.setLayout(app.getLayout());
        }
        if(StringUtils.isNotEmpty(app.getParentModule())){
            appMenu.setParentModule(app.getParentModule());
        }
        appMenu.setAppType(app.getAppType());
        appMenu.setBackColor(app.getBackColor());
        appMenu.setIsFav(app.getIsFav());
        appMenu.setLastUseTime(app.getLastUseTime());
        appMenu.setAppPath(app.getPath());
        appMenu.setMenuNavType(app.getMenuNavType());
        appMenu.setCategoryId(app.getCategoryId());
        return appMenu;
    }


    /**
     * 构建菜单。
     * @param result
     * @param apps
     * @param menus
     */
    private void constructMenu(List<SysMenuDto> result, List<SysAppDto> apps,Collection<SysMenuDto> menus){
        if(BeanUtil.isEmpty(apps)){
            return;
        }
        List<SysAppDto> appDtos= apps.stream().sorted(Comparator.comparing(item->{
            Integer sn= item.getSn();
            if(sn==null){
                sn=1;
            }
            return sn;
        })).collect(Collectors.toList());
        appDtos.forEach(app->{
            //解析应用首页
            parseAuthSysApp(app);

            SysMenuDto appMenu = constructMenu(app);

            result.add(appMenu);

            List<SysMenuDto> menuDtos = menus.stream()
                    .filter(menu -> menu.getAppId().equals(app.getAppId()) &&
                            ! CommonConstant.ROOT_ID.equals( menu.getParentId())
                    ).sorted(Comparator.comparing(menu->{
                        String sn=menu.getSn();
                        if(sn==null){
                            return 1;
                        }
                        return Integer.parseInt(sn);

                    }))
                    .collect(Collectors.toList());



            result.addAll(menuDtos);
        });

    }

    private void parseAuthSysApp(SysAppDto app){

        if(!MBoolean.YES.name().equals(app.getIsAuth())){
            return;
        }
        //清空应用首页默认的配置
        app.setLayout(null);
        app.setParentModule(null);
        app.setHomeType(null);
        app.setHomeUrl(null);
        app.setParams(null);
        app.setUrlType(null);
        Map<String, Set<String>> profiles = ProfileContext.getCurrentProfile();
        JSONArray setting =JSONArray.parseArray(app.getAuthSetting());
        for(Object obj:setting){
            JSONObject json=(JSONObject)obj;
            //满足条件
            String setVal=json.containsKey("setting")?json.getJSONObject("setting").getString("value"):"";
            boolean flag= SysUtil.hasRights(setVal,profiles);
            if(flag){
                app.setLayout(json.getString("layout"));
                app.setParentModule(json.getString("parentModule"));
                app.setHomeType(json.getString("homeType"));
                app.setHomeUrl(json.getString("homeUrl"));
                app.setParams(json.getString("params"));
                app.setUrlType(json.getString("urlType"));
                break;
            }
        }

    }

    public OsUserDto convertOsUser(OsUser osUser){
        OsUserDto osUserDto = new OsUserDto();
        if(BeanUtil.isNotEmpty(osUser)) {
            BeanUtil.copyProperties(osUserDto, osUser);
        }
        String tenantId = osUser.getTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        osUserDto.setTenantId(tenantId);
        //查询用户分组信息
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq ("PARTY2_", osUserDto.getUserId());
        wrapper.eq ("REL_TYPE_", "GROUP-USER");
        wrapper.eq("TENANT_ID_",osUserDto.getTenantId());
        List<OsRelInst> relInsts = osRelInstMapper.selectList(wrapper);

        //组ID集合
        Set<String> userGroupIds = new HashSet<>();
        if(BeanUtil.isNotEmpty(relInsts)){
            userGroupIds = relInsts.stream().map(OsRelInst::getParty1).collect(Collectors.toSet());
        }
        List<String> roles=new ArrayList<>();
        roles.addAll(userGroupIds);
        //获取用户类型组ID
        if(StringUtils.isNotEmpty(osUserDto.getUserType())){
            try{
                OsUserType osUserType=osUserTypeService.getByCode(osUserDto.getUserType(),tenantId);
                if(osUserType!=null && StringUtils.isNotEmpty(osUserType.getGroupId())) {
                    roles.add(osUserType.getGroupId());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        // 设置角色
        osUserDto.setRoles(roles);
        //设置主部门
        OsGroup mainGroup = osGroupMapper.getMainGroup(osUserDto.getUserId(),tenantId);
        if(BeanUtil.isNotEmpty(mainGroup)){
            osUserDto.setDeptId(mainGroup.getGroupId());
            osUserDto.setDeptName(mainGroup.getName());
        }
        List<OsUserPlatform> osUserPlatformList = osUserPlatformService.getOsUserPlatformByUserId(osUser.getUserId(),tenantId);
        if(osUserPlatformList!=null){
            osUserPlatformList.stream().forEach(o -> {
                if(o.getPlatformType()==2){
                    osUserDto.setWxOpenId(o.getOpenId());
                }else if(o.getPlatformType()==3){
                    osUserDto.setDdId(o.getOpenId());
                }else if(o.getPlatformType()==4){
                    osUserDto.setFsOpenId(o.getOpenId());
                }
            });
        }
        return osUserDto;
    }

    public List<OsUserDto> convertOsUsers(List<OsUser> osUsers){
        List<OsUserDto> osUserDtos = new ArrayList<>();
        if(BeanUtil.isNotEmpty(osUsers)) {
            for(OsUser osUser:osUsers){
                OsUserDto osUserDto=new OsUserDto();
                BeanUtil.copyProperties(osUserDto, osUser);
                List<OsUserPlatform> osUserPlatformList = osUserPlatformService.getOsUserPlatformByUserId(osUser.getUserId(),osUser.getTenantId());
                if(osUserPlatformList!=null){
                    osUserPlatformList.stream().forEach(o -> {
                        if(o.getPlatformType()==2){
                            osUserDto.setWxOpenId(o.getOpenId());
                        }else if(o.getPlatformType()==3){
                            osUserDto.setDdId(o.getOpenId());
                        }else if(o.getPlatformType()==4){
                            osUserDto.setFsOpenId(o.getOpenId());
                        }
                    });
                }
                osUserDtos.add(osUserDto);
            }
        }
        return osUserDtos;
    }


    /**
     * 根据用户ID（多个使用逗号分隔 ）获取用户列表
     * @param userIds
     * @return
     */
    public List<OsUserDto> getByUsers(String userIds){
        String[] aryUserId=userIds.split(",");
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.in("USER_ID_",aryUserId);
        List<OsUser> users= osUserMapper.selectList(wrapper);
        for (int i = 0; i <users.size(); i++) {
            OsUser osUser = users.get(i);
            OsGroup mainGroup = osGroupMapper.getMainGroup(osUser.getUserId(),osUser.getTenantId());
            if(mainGroup == null){
                osUser.setMainDepId("");
                osUser.setDeptName("");
            }else{
                osUser.setMainDepId(mainGroup.getGroupId());
                osUser.setDeptName(mainGroup.getName());
            }

        }
        List<OsUserDto> userDtos=convertOsUsers(users);
        return  userDtos;
    }

    /**
     * 账号是否存在。
     * @param user
     * @return
     */
    public boolean isUserNotExist(OsUser user) {
        QueryWrapper wrapper= new QueryWrapper();
        if(StringUtils.isNotEmpty(user.getUserId())){
            wrapper.ne("USER_ID_",user.getUserId());
        }
        wrapper.eq("USER_NO_",user.getUserNo());
        wrapper.eq("TENANT_ID_",user.getTenantId());

        int count = count(wrapper);

        return count>0;

    }

    public JsonResult setStatusAndQuitTime(String[] userIds,String quitTime) throws Exception {
        Date date = new Date();
        //离职状态
        String status="0";
        //为空 取当前时间
        if(StringUtils.isNotEmpty(quitTime)){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date quitDate = dateFormat.parse(quitTime);
            if(quitDate.after(date)){
                //在职状态
                status="1";
            }
            date=quitDate;
        }
        JsonResult jsonResult=new JsonResult();
        for (String userId : userIds) {
            osUserMapper.setStatusAndQuitTime(userId,status,date);
        }
        return  jsonResult.setSuccess(true).setMessage("操作成功!");
    }

    @Transactional(rollbackFor = Exception.class)
    public JsonResult setPassword(String id, String newPassword) {
        JsonResult jsonResult=new JsonResult();
        if (StrUtil.isBlank(newPassword)) {
            return jsonResult.setMessage("新密码不能为空").setSuccess(false);
        }

        OsUser osUser = this.getById(id);
        JsonResult checkPasswordResult = osPasswordPolicyService.checkPassword(osUser.getUserNo(), newPassword);
        if(!checkPasswordResult.getSuccess()){
            return checkPasswordResult;
        }

        OsUser user = new OsUser();
        user.setUserId(id);
        user.setPwd(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
        return jsonResult.setMessage("修改密码成功").setSuccess(true);
    }

    public OsUserDto getByAccount(String account) {
        OsUser osUser = osUserMapper.getByUsername(account);
        if(osUser==null){
            return null;
        }
        OsUserDto osUserDto= convertOsUser(osUser);
        return  osUserDto;

    }

    /**
     * 初始化租户管理员。
     * @param osInst
     * @return
     */
    public OsUser initInstUser(OsInst osInst,String pwd){
        //创建用户
        String userId=IdGenerator.getIdStr();
        OsUser user=new OsUser();
        user.setUserId(userId);
        String tenantAdmin= SysPropertiesUtil.getString("tenantAdmin");
        if(StringUtils.isNotEmpty(tenantAdmin)){
            user.setUserNo(tenantAdmin);
        }else {
            user.setUserNo(osInst.getRootAdmin());
        }
        user.setFullName("管理员");

        String encPwd= passwordEncoder.encode(pwd);
        user.setPwd(encPwd);
        user.setEmail(osInst.getEmail());

        user.setTenantId(osInst.getInstId());
        user.setStatus(OsUser.STATUS_IN_JOB);
        user.setFrom(OsUser.FROM_SYS);
        user.setSex(OsUser.MALE);
        //添加用户
        osUserMapper.insert(user);
        //添加关联用户。
        osInstUsersService.initUser(user);

        return user;

    }


    @Transactional
    public void delByIds(List<String> userIds,String tenantId) {
        String detail="删除用户:";

        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);
        LogContext.put(Audit.OPERATION,"删除用户");

        Iterator<String> it = userIds.iterator();
        while(it.hasNext()){
            String userId= (String) it.next();
            OsUser osUser=osUserMapper.selectById(userId);
            detail += EntityUtil.getInfo(osUser,false) +",";

            osInstUsersService.removeByUserId(userId,tenantId);
            osRelInstService.deleteByUserId(userId);
        }
        LogContext.put(Audit.DETAIL,detail);
        getRepository().deleteBatchIds(userIds);
    }


    public List<OsUser> getAdmin(String tenantId){
        List<OsUser> list=osUserMapper.getAdmin(tenantId);
        return list;
    }

    public OsUser getById(String userId){
        OsUser user=osUserMapper.getById(userId);
        return user;
    }

    /**
     * 更新默认登陆机构
     */
    public void updateTenantIdFromDomain(String userId, String defaultDomain) {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("userId", userId);
        if(StringUtils.isNotEmpty(defaultDomain)){
            params.put("defaultDomain", defaultDomain);
        }
        osUserMapper.updateTenantIdFromDomain(params);
    }

    public List<OsUser> getQuitTimeUsers(Date quitTime) {
        return osUserMapper.getQuitTimeUsers(quitTime);
    }

    /**
     * 通过组id查看该用户组及子组的用户
     */
    public IPage<OsUser> getAllUserByGroupId(QueryFilter filter,Map params) {

        String groupId = (String) params.get("groupId");
        String dimId = (String) params.get("dim");
        String tenantId = ContextUtil.getCurrentTenantId();

        List<String> groups=getByGroupId(tenantId,groupId,dimId);
        if(BeanUtil.isNotEmpty(groups)){
            params.put("groupIds",groups);
        }

        IPage<OsUser> osUserIPage = osUserMapper.getAllUserbyGroupId(filter.getPage(),params);


        List<OsUser> osList = new ArrayList<>();
        List<OsUser> osUsers = osUserIPage.getRecords();
        for (OsUser user : osUsers) {
            OsGroup deps = osGroupServiceImpl.getMainDeps(user.getUserId(), tenantId);
            if (deps != null) {
                user.setDeptName(deps.getName());
            }
            osList.add(user);
        }
        osUserIPage.setRecords(osList);
        return osUserIPage;
    }

    /**
     * 获取分公司ID
     * @param tenantId      租户ID
     * @param groupId       用户组ID
     * @param dimId         维度ID
     * @return
     */
    private List<String> getByGroupId(String tenantId, String groupId,String dimId){
        JPaasUser curUser = (JPaasUser) ContextUtil.getCurrentUser();
        List<String> groups=new ArrayList<>();
        //分组ID，不能为空
        if (StringUtils.isNotEmpty(groupId)) {
            //获取当前分组
            if ("curOrg".equals(groupId)) {
                if (curUser != null) {
                    groupId = curUser.getDeptId();
                    groups.add(groupId);
                }
            }
            //指定公司ID，使用逗号分隔。
            else{
                List<String> groupIds = Arrays.asList(groupId.split(","));
                groups.addAll(groupIds);
            }
        }
        else{
            String companyId=curUser.getCompanyId();
            groups = osGroupServiceImpl.getAllSubGroupIds(companyId,true);
        }
        return groups;
    }


    public JPaasUser findByUsernameAndTenantId(String username, String tenantId) {
        OsUser sysUser = osUserMapper.getByUsernameAndTenantId(username, tenantId);
        return getLoginAppUser(sysUser);
    }

    /**
     * 根据应用Id 查询当前用户的菜单
     *
     * @param appId 应用Id
     * @return 菜单列表
     */
    public List<SysMenuDto> getMenusByAppId(String appId){
        JPaasUser jPaasUser =(JPaasUser) ContextUtil.getCurrentUser();
        List<SysMenuDto>  list=new ArrayList<>();
        // 管理员显示所有菜单信息
        if (jPaasUser.isAdmin()){
             list= getTenantAdminMenu(jPaasUser,appId);
        }else {
            //获取普通用户的数据。
            list=getCommonUserMenu(jPaasUser,appId);
        }
        return list;
    }

    /**
     * 根据权限去获取用户的应用
     * @param appType
     * @return
     */
    public List<SysAppDto> getAppByCurUser(int appType) {
        JPaasUser jPaasUser =(JPaasUser) ContextUtil.getCurrentUser();
        List<SysAppDto>  list=new ArrayList<>();
        Collection<String> appIds=null;
        // 管理员显示所有应用
        if (jPaasUser.isAdmin()){
            String tenantId=jPaasUser.getTenantId();
            OsInst osInst=osInstService.getById(tenantId);
            List<OsInstTypeMenu> instTypeMenus=osInstTypeMenuMapper.getAppsByInstType(osInst.getInstType());
            appIds= instTypeMenus.parallelStream().map(OsInstTypeMenu::getAppId)
                    .distinct().collect(Collectors.toList());

        }else {
            //获取普通用户的应用
            List<String> roles=getGroupsByUser(jPaasUser);
            if(BeanUtil.isNotEmpty(roles)){
                List<OsGroupMenu> groupMenus = osGroupMenuService.getAppsByGroups(roles);
                appIds= groupMenus.parallelStream().map(OsGroupMenu::getAppId)
                        .distinct().collect(Collectors.toList());
            }
        }
        if(BeanUtil.isNotEmpty(appIds)){
            list = systemClient.getAppsByIdsAndType(String.join(",", appIds),appType);
        }
        return list;
    }

    /**
     * 同步微信时更新用户
     * @param
     * @return
     **/
    @Transactional
    public int updateBySync(OsUser entity) {
        super.updateById(entity);
        String tenantId=entity.getTenantId();
        if(tenantId==null){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        updateInstUser(entity,tenantId);
        //更新主部门关系
        updateMainGroup(entity, tenantId);
        return 0;
    }

    private void updateMainGroup(OsUser entity, String tenantId) {
        String relTypeId= entity.getRelTypeId();
        if(StringUtils.isEmpty(relTypeId)){
            relTypeId=OsRelType.REL_CAT_GROUP_USER_BELONG_ID;
        }
        OsRelType osRelType=osRelTypeMapper.selectById(relTypeId);
        if(StringUtils.isNotEmpty(entity.getMainDepId())){
            //输出旧主部门关系
            osRelInstService.deleteByParty2AndTenantIdAndIsMain(entity.getUserId(), tenantId);
            //插入新主部门关系
            OsGroup mainGroup=osGroupServiceImpl.get(entity.getMainDepId());
            OsRelInst inst=new OsRelInst();
            inst.setParty1(entity.getMainDepId());
            inst.setParty2(entity.getUserId());
            inst.setPath("0."+inst.getParty1()+"."+inst.getParty2()+".");
            inst.setRelTypeKey(osRelType.getKey());
            inst.setRelType(osRelType.getRelType());
            inst.setRelTypeId(osRelType.getId());
            inst.setDim1(OsDimension.DIM_ADMIN_ID);
            inst.setStatus(MBoolean.ENABLED.toString());
            inst.setPkId(IdGenerator.getIdStr());
            inst.setIsMain(MBoolean.YES.name());
            inst.setTenantId(mainGroup.getTenantId());
            osRelInstMapper.insert(inst);
        }
    }

    /**
     *  功能：用户批量导入
     * @param maps Excel中读取的数据集
     * @param override 相同编号的记录是否覆盖
     * @return java.lang.String 处理结果消息
     * @author  Elwin ZHANG
     * @date 2022/3/10 11:34
     **/
    @Transactional
    public String importData(List<Map<Integer, String>> maps ,boolean override){
        Map<Integer, String>  colTitle=maps.get(0);
        checkColumnTitle(colTitle);
        List<String> lstSuccess=new ArrayList<>( );     //保存成功的行号
        List<String> lstFail=new ArrayList<>( );    //保存成失败的行号
        List<String> lstSkip=new ArrayList<>( );  //保存成跳过的行号
        int size=maps.size();
        //循环导入行
        for(int i=1;i<size;i++){
            importOne(maps.get(i),override,i+2,lstSuccess,lstFail,lstSkip);
        }
        String message="成功导入【" + lstSuccess.size() + "】条记录；";
        if(lstSkip.size()>0){
            message +="跳过【"+ lstSkip.size() + "】条记录；";
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
     * @date 2022/3/10 16:05
     **/
    private void importOne(Map<Integer, String> map ,boolean override,int lineNo, List<String> lstSuccess, List<String> lstFail, List<String> lstSkip){
        boolean isAdd=true;     //是否新记录
        try{
            // 按顺序获取字段值，"姓名&工号&初始密码&所属部门编码&性别&出生日期&入职日期&离职日期&用户类型&手机号码";
            String name=map.get(0);
            String userNo=map.get(1);
            String password=map.get(2);
            String deptCode=map.get(3);
            String sexName=map.get(4);
            String birthday=map.get(5);
            String entryDate=map.get(6);
            String quitDate=map.get(7);
            String userType=map.get(8);
            String mobile=map.get(9);
            String tenantId=ContextUtil.getCurrentTenantId();
            //必填字段校验
            if(StringUtils.isEmpty(userNo) || StringUtils.isEmpty(name) || StringUtils.isEmpty(password) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(tenantId) ){
                lstFail.add(lineNo+"");
                return;
            }
            userNo=userNo.trim();
            name=name.trim();
            password=password.trim();
            deptCode=deptCode.trim();
            OsUser user=new OsUser();
            //是否存在相同账号/工号的记录
            OsUserDto oldUser=getByAccount(userNo);
            if(oldUser!=null){
                //不覆盖，则跳过
                if(!override){
                    lstSkip.add(lineNo+"");
                    return;
                }
                user.setPwd(oldUser.getPwd());
                user.setUserId(oldUser.getUserId());
                user.setTenantId(oldUser.getTenantId());
                isAdd=false;
            }else {
                user.setUserId(IdGenerator.getIdStr());
                user.setTenantId(tenantId);
                user.setPwd(password);
            }
            String userId=user.getUserId();
            //校验主部门编码
            OsGroup mainGroup=osGroupServiceImpl.getDeptByKey(deptCode);
            //主部门编码不存在，则失败
            if(mainGroup==null){
                lstFail.add(lineNo+"");
                return;
            }
            user.setMainDepId(mainGroup.getGroupId());
            user.setUserNo(userNo);
            user.setFullName(name);
            user.setMobile(mobile);
            //性别
            user.setSex("male");
            if("女".equals(sexName)){
                user.setSex("female");
            }
            //生日
            try {
                if(StringUtils.isNotEmpty(birthday)){
                    Date date1= DateUtils.parseDate(birthday.replace('/','-'));
                    user.setBirthday(date1);
                }
            }catch (Exception ee){}
            //入职日期
            try {
                if(StringUtils.isNotEmpty(entryDate)){
                    Date date1=DateUtils.parseDate(entryDate.replace('/','-'));
                    user.setEntryTime(date1);
                }
            }catch (Exception ee){}
            //离职日期
            try {
                if(StringUtils.isNotEmpty(quitDate)){
                    Date date1=DateUtils.parseDate(quitDate.replace('/','-'));
                    user.setQuitTime(date1);
                }
            }catch (Exception ee){}
            //状态
            user.setStatus(OsUser.STATUS_IN_JOB);
            if(user.getQuitTime()!=null){
                user.setStatus(OsUser.STATUS_OUT_JOB);
            }
            //用户类型
            if(StringUtils.isNotEmpty(userType)){
                OsUserType object=osUserTypeService.getByName(userType,tenantId);
                if(object!=null){
                    user.setUserType(object.getCode());
                }
            }
            user.setFrom("import");
            if(isAdd){
                insert(user);
            }else {
                update(user);
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
        String strTitles="姓名&工号&初始密码&所属部门编码&性别&出生日期&入职日期&离职日期&用户类型&手机号码";
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


    /**
     * 锁定用户账号
     * @param username
     * @param tenantId
     */
    public OsUser getByUsernameAndTenantId(String username, String tenantId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("USER_NO_", username);
        queryWrapper.eq("TENANT_ID_", tenantId);
        List<OsUser> list = osUserMapper.selectList(queryWrapper);
        if(BeanUtil.isEmpty(list)){
            return null;
        }else{
            return list.get(0);
        }

    }


    /**
     * 锁定用户账号
     * @param username
     * @param tenantId
     */
    public void lockAccount(String username, String tenantId){
        OsUser user = this.getByUsernameAndTenantId(username, tenantId);
        if(StringUtils.isNotEmpty(user.getIsLock()) && YES.equals(user.getIsLock())){
            return;
        }

        user.setIsLock(YES);
        this.updateUser(user);
    }

    /**
     * 解锁用户账号
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult unlockAccount(String userId) {
        OsUser osUser = this.get(userId);
        if(BeanUtil.isEmpty(osUser)){
            JsonResult.getSuccessResult("解锁用户账号成功");
        }

        //清除该账号的密码输入次数记录
        osPasswordInputErrorService.removeRecordByUsernameAndTenantId(osUser.getUserNo(), osUser.getTenantId());

        //将锁定标记修改成NO
        osUser.setIsLock("NO");
        //将密码修改时间重置成当前时间
        osUser.setPwdUpdateTime(new Date());
        this.updateById(osUser);
        return JsonResult.getSuccessResult("解锁用户账号成功");
    }


    /**
     * 用户首次登录修改密码
     * @param userId
     * @param password
     * @return
     */
    public JsonResult changePassword(String userId, String password) {
        if (StrUtil.isBlank(password)) {
            return JsonResult.getFailResult("新密码不能为空");
        }

        OsUser user = this.getById(userId);
        if(BeanUtil.isEmpty(user)){
            return JsonResult.getFailResult("用户不存在");
        }

        user.setPwd(passwordEncoder.encode(password));
        user.setIsFirstLogin("NO");
        baseMapper.updateById(user);

        return JsonResult.getSuccessResult("修改密码成功");

    }

    public JPaasUser findByOpenId(String openId,Integer platformType,String tenantId) {
        OsUser sysUser = osUserMapper.getUserByOpenId(openId,platformType,tenantId);
        return getLoginAppUser(sysUser);
    }

    /**
     * 获取最大的序号
     * @return
     */
    public int getMaxSn() {
        OsUser osUser = osUserMapper.getMaxSn();
        return osUser.getSn();
    }
}
