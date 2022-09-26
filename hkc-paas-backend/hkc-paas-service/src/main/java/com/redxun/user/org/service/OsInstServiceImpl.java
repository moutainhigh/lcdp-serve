package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.mail.Mail;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.user.mq.MailInputOutput;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsInst;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.mapper.OsInstMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册机构Service业务层处理
 *
 * @author yjy
 * @date 2019-11-08
 */
@Slf4j
@Service
public class OsInstServiceImpl extends SuperServiceImpl<OsInstMapper, OsInst>  implements BaseService<OsInst> {

    @Resource
    private OsInstMapper osInstMapper;
    @Resource
    private OsUserServiceImpl osUserService;
    @Resource
    private OsGroupServiceImpl osGroupService;
    @Resource
    private OsRelInstServiceImpl osRelInstService;
    @Resource
    private MailInputOutput mailInputOutput;
    @Resource
    private FtlEngine ftlEngine;
    @Value("${spring.mail.username}")
    private String username;

    @Override
    public BaseDao<OsInst> getRepository() {
        return osInstMapper;
    }

    /**
     * 根据父ID获取业务
     * @param parentId
     * @return
     */
    public List<OsInst> getByParentId(String parentId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PARENT_ID_",parentId);
        return osInstMapper.selectList(queryWrapper);
    }

    /**
     * 按路径查找机构
     * @param path
     * @return
     */
    public List<OsInst> getByPath(String path){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.likeRight("PATH_",path);
        return osInstMapper.selectList(queryWrapper);
    }


    /**
     * 是否已存在相同的中文名或编码
     * @param ent
     * @return
     */
    public boolean isExist(OsInst ent){
        QueryWrapper<OsInst> wrapper=new QueryWrapper<OsInst>();
        //检查中文名是否存在
        String instName=ent.getNameCn();
        if(StringUtils.isNotEmpty(instName)){
            wrapper.and(w -> w.eq("INST_NO_",ent.getInstNo()).or().eq("NAME_CN_",instName));
        }else {
            wrapper.eq("INST_NO_",ent.getInstNo());
        }
        if(StringUtils.isNotEmpty(ent.getInstId())){
            wrapper.ne("INST_ID_",ent.getInstId());
        }
        Integer rtn= osInstMapper.selectCount(wrapper);
        return  rtn>0;
    }

    /**
     * 按机构编码查找机构
     * @param instNo 机构编码
     * @return
     */
    public OsInst getByInstNo(String instNo){
        QueryWrapper<OsInst> wrapper=new QueryWrapper<OsInst>();
        wrapper.eq("INST_NO_",instNo);
        OsInst osInst=osInstMapper.selectOne(wrapper);
        return osInst;
    }

    /**
     * 机构编码是否存在
     * @param instNo
     * @return
     */
    public Boolean isExistByInstNo(String instNo){
        OsInst osInst=getByInstNo(instNo);
        return osInst !=null;
    }

    /**
     * 按机构域名查找机构
     * @param domain 机构域名
     * @return
     */
    public OsInst getByDomain(String domain){
        QueryWrapper<OsInst> wrapper=new QueryWrapper<OsInst>();
        wrapper.eq("DOMAIN_",domain);
        OsInst osInst=osInstMapper.selectOne(wrapper);
        return osInst;
    }

    /**
     * 检查域名是否存在
     * @param domain
     * @return
     */
    public Boolean isDomainExist(String domain){
        OsInst osInst=getByDomain(domain);
        return osInst!=null;
    }

    @Override
    public int insert(OsInst entity) {
        if(StringUtils.isEmpty(entity.getInstId()) ){
            entity.setInstId(IdGenerator.getIdStr());
        }

        int rtn=osInstMapper.insert(entity);

        //生成一个超级管理员。
        if(MBoolean.ENABLED.name().equals(entity.getStatus()) ){
            addSuperAdmin(entity);
        }

        return rtn;
    }

    @Override
    public int update(OsInst entity) {

        OsInst orignInst=osInstMapper.selectById(entity.getInstId());
        //原来为DISABLED 更新为 ENABLED
        if(MBoolean.DISABLED.name().equals( orignInst.getStatus()) &&
                MBoolean.ENABLED.name().equals(entity.getStatus())){
            //增加一个超管。
            addSuperAdmin(entity);
        }

        int rtn=osInstMapper.updateById(entity);
        return rtn;
    }

    /**
     * 添加机构下的超管
     * @param inst
     */
    private void addSuperAdmin(OsInst inst){

        //生成随机密码
        String pwd=StringUtils.genRandomString(6);
        //初始化部门
        OsGroup mainGroup= osGroupService.initInstGroup(inst);
        //初始化用户
        OsUser user= osUserService.initInstUser(inst,pwd);

        //将行政组和用户建立关联关系。
        osRelInstService.addBelongRelInst(mainGroup.getGroupId(),user.getUserId(),MBoolean.YES.name(),inst.getInstId());
        //发送邮件
        sendMail(inst,user);
    }

    /**
     * 发送邮件。
     * @param inst
     * @param user
     */
    private void sendMail(OsInst inst,OsUser user){
        String email=inst.getEmail();
        if(StringUtils.isEmpty(email)){
            return;
        }
        String appName= SysPropertiesUtil.getString("appName");
        String baseUrl=SysPropertiesUtil.getString("serverAddress") + SysPropertiesUtil.getString("ctxPath");


        Map<String,String> vars=new HashMap<>();
        vars.put("instCode",inst.getInstNo());
        vars.put("account",user.getUserNo());
        vars.put("password",user.getPwd());
        vars.put("baseUrl",baseUrl);
        vars.put("appName",appName);

        try{
            String content= ftlEngine.mergeTemplateIntoString("mail.ftl", vars);

            Mail mail=new Mail();
            mail.setFrom(username);
            mail.setReceiver(new String[]{email});
            mail.setSubject("您在["+appName +"]平台注册租户成功!");
            mail.setContent(content);
            mailInputOutput.output().send(MessageBuilder.withPayload(mail)
                    .build());
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }

    }

    /**
     * 添加实体
     * @param entity
     * @return
     */
    public int add(OsInst entity) {
        if(StringUtils.isEmpty(entity.getInstId()) ){
            entity.setInstId(IdGenerator.getIdStr());
        }
        int rtn=osInstMapper.insert(entity);
        return rtn;
    }

    /**
     * 更新实体
     * @param entity
     * @return
     */
    public int updInst(OsInst entity) {
        int rtn=osInstMapper.updateById(entity);
        return rtn;
    }

    /**
     * 通过当前用户和状态获取机构申请数据
     * @param filter
     * @param status
     * @return
     */
    public IPage getByUserIdAndStatus(QueryFilter filter,String status) {
        Map<String,Object> params= PageHelper.constructParams(filter);
        params.put("userId", ContextUtil.getCurrentUserId());
        params.put("status",status);
        return osInstMapper.getPageByUserIdAndStatus(filter.getPage(),params);
    }

    /**
     * 通过当前用户和状态获取机构申请数据
     * @param userId
     * @param status
     * @return
     */
    public List<OsInst> getByUserIdAndStatus(String userId,String status) {
        Map<String,Object> params= new HashMap<>();
        params.put("userId", userId);
        params.put("status",status);
        return osInstMapper.getByUserIdAndStatus(params);
    }

    /**
     * 获取所有启用机构数据
     * @return
     */
    public List<OsInst> getList() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("STATUS_","ENABLED");
        return osInstMapper.selectList(queryWrapper);
    }

    /**
     * 当前用户机构切换
     * @param tenantId
     */
    public void updateCurUserTenantId(String tenantId) {
        IUser curUser=ContextUtil.getCurrentUser();
        OsUser user = new OsUser();
        user.setUserId(curUser.getUserId());
        user.setTenantId(tenantId);
        //修改用户表的tenant_id_字段
        osUserService.updateUser(user);
        //设置当前登录人
        JPaasUser jPaasUser=osUserService.getUserById(curUser.getUserId());
        //修改缓存的租户信息
        RedisTemplate<String, Object> redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        String accessToken = TokenUtil.getToken();
        //获取用户信息
        Object authObj = redisTemplate.opsForValue().get(SecurityConstants.REDIS_TOKEN_AUTH + accessToken);
        if(authObj instanceof OAuth2Authentication){
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authObj;
            Object principal = oAuth2Authentication.getPrincipal();

            if(principal instanceof JPaasUser){
                JPaasUser curJpaasUser=(JPaasUser)principal;
                curJpaasUser.setUserType(jPaasUser.getUserType());
                curJpaasUser.setAdmin(jPaasUser.isAdmin());
                // 设置角色
                curJpaasUser.setRoles(jPaasUser.getRoles());
                curJpaasUser.setDeptId(jPaasUser.getDeptId());
                curJpaasUser.setDeptName(jPaasUser.getDeptName());
                redisTemplate.opsForValue().set(SecurityConstants.REDIS_TOKEN_AUTH + accessToken,authObj);
            }
        }
        redisTemplate.opsForValue().set(SecurityConstants.REDIS_TENANT_ID_AUTH + accessToken,tenantId);
    }

    /**
     * 根据机构域名查找机构ID
     * @param domain
     * @return
     */
    public String findInstIdByDomain(String domain) {
        String instId = null;
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("DOMAIN_",domain);
        List<OsInst> list = osInstMapper.selectList(queryWrapper);
        if(list != null && list.size() > 0){
            instId = list.get(0).getInstId();
        }
        return instId;
    }

    /**
     * 根据机构编码查找机构ID
     * @param instNo
     * @return
     */
    public String findInstIdByInstNo(String instNo) {
        String instId = null;
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_NO_",instNo);
        OsInst osInst = osInstMapper.selectOne(queryWrapper);
        if(osInst!=null){
            return osInst.getInstId();
        }
        return instId;
    }

    /**
    * @Description:  注册机构（租户），同时创建一个登录用户
    * @param inst 机构注册信息
    * @return com.redxun.common.base.entity.JsonResult
    * @Author: Elwin ZHANG  @Date: 2021/8/20 10:07
    **/
    public JsonResult register(OsInst inst){
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        if(isExist(inst)){
            return  JsonResult.Fail("系统已存在编号或名称相同的机构！");
        }
        //保存机构
        inst.setInstId(IdGenerator.getIdStr());
        inst.setStatus("ENABLED");
        int i=osInstMapper.insert(inst);
        if(i<=0){
            return  JsonResult.Fail("保存机构信息失败！");
        }
        //保存用户信息
        OsUser user =osUserService.initInstUser(inst,inst.getPassword());
        user.setUserNo(inst.getAccount());
        user.setFullName("管理员" +inst.getInstNo());
        user.setMobile(inst.getPhone());
        osUserService.updateById(user);
        //成功则返回
        result.setData(inst);
        return result;
    }
}
