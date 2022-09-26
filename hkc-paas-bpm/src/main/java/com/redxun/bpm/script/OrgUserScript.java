package com.redxun.bpm.script;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.MethodDefine;
import com.redxun.bpm.script.cls.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsDimensionDto;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户机构
 */
@ClassScriptType(type= "OrgUserApi",description = "用户函数")
@Component("OrgUserApi")
public class OrgUserScript implements IScript {
    @Resource
    OrgClient orgClient;
    /**
     * 获取流程实例的发起用户Id
     * @return
     */
    @MethodDefine(title="获取用户主部门对象", description="本方法获取用户主部门对象，可用的属性有 groupId,dimId,name,key,rankLevel ")
    public OsGroupDto getMainDep(@ParamDefine(varName = "userId",description = "用户Id") String userId){
        String newId=getValue(userId);
        String tenantId=this.getCurUser().getTenantId();
        OsGroupDto osGroup = orgClient.getMainDeps(newId, tenantId);
        return osGroup;
    }

    @MethodDefine(title="获取用户组直属上级")
    public OsGroupDto getUpGroup(@ParamDefine(varName = "groupId",description = "用户组Id") String groupId){
        OsGroupDto osGroup = orgClient.getUpGroup(groupId);
        return osGroup;
    }

    @MethodDefine(title="获取用户组（往上查找符合等级的）的上级用户组")
    public OsGroupDto  getUpRankLevelGroup(@ParamDefine(varName = "groupId",description = "用户组Id") String groupId,
                                    @ParamDefine(varName = "rankLevel",description = "用户组等级值")  Integer rankLevel){
        return orgClient.getUpRankLevelGroup(groupId,rankLevel);
    }

    @MethodDefine(title="判断用户是否为某部门负责人",description="判断用户是否为某部门负责人")
    public boolean isGroupLeader(@ParamDefine(varName = "userId",description = "用户Id") String userId,
                                 @ParamDefine(varName = "groupId",description = "用户组Id") String groupId){
        //输入参数不合法则返回null
        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(groupId)){
            return false;
        }
        String newUserId=getValue(userId);
        String newGroupId=getValue(groupId);

        List<OsRelInstDto> list=orgClient.getByRelTypeKeyParty1("GROUP-USER-LEADER",newGroupId);
        if(list==null){
            return  false;
        }
        for(int i=0;i<list.size();i++){
           if (newUserId.equals( list.get(i).getParty2())){
               return true;
            }
        }
        return  false;
    }

    @MethodDefine(title="按上级 Or 部门负责人 Or 跳过",description="先找上级，找不到则找部门负责人，还找不到则跳过")
    public List <TaskExecutor> getByUpperOrGroupLeaderOrSkip(@ParamDefine(varName = "userId",description = "用户Id") String userId
    ,@ParamDefine(varName = "isUpper",description = "是否上级部门负责人，大于1为是，否则取当前部门负责人") int isUpper){
        //输入参数不合法则返回null
        if(StringUtils.isEmpty(userId) ){
            return null;
        }
        String newId=getValue(userId);
        //先找上级
        List<OsRelInstDto> relInstDtos = orgClient.getByRelTypeKeyParty2("USER-UP-LOWER", newId);
        if(relInstDtos!=null && relInstDtos.size()>0){
            OsUserDto upperUser= orgClient.getUserById(relInstDtos.get(0).getParty1());
            if(upperUser!=null) {
                if(upperUser.getUserId().equals("-1")){
                    String userId2=ContextUtil.getCurrentUserId();
                    return changeUser2Executor(orgClient.getUserById(userId2));
                }
                return changeUser2Executor(upperUser);
            }
        }
        //找不到, 则找上级部门负责人
        OsGroupDto group=getMainDep(newId);
        if(group!=null){
            String groupId=group.getGroupId();
            if(isUpper>0) {
                OsGroupDto upGroup = orgClient.getUpGroup(groupId);
                if(upGroup!=null){
                    groupId= upGroup.getGroupId();
                }else{
                    groupId="";
                }
            }
            if(StringUtils.isNotEmpty(groupId)) {
                List<TaskExecutor> list = getGroupLeader(groupId);
                if (list != null && list.size() > 0) {
                    return list;
                }
            }
        }
        //还找不到，返回上级节点审批人，即会跳过
        String  curUserId = ContextUtil.getCurrentUserId();
        OsUserDto curUser=orgClient.getUserById(curUserId);
        return  changeUser2Executor(curUser);
    }

    /**
    * @Author: Elwin ZHANG  @Date: 2021/10/15 9:31
    **/
    @MethodDefine(title = "根据组等级连续N次找上级用户组",description = "连续N次找上级用户组，要求返回的用户组为指定的组等级，可以控制返回第一个找到的组或者是最上层的组")
    public OsGroupDto getParentGroup(@ParamDefine(varName = "groupId",description = "组ID") String groupId,
            @ParamDefine(varName = "rankLevels",description = "用户组等级值，可传多个值用英文逗号分隔；对应os_group表的RANK_LEVEL_字段") String rankLevels,
            @ParamDefine(varName = "times",description = "向上查找几次，为1只找到上级部门，2则找到上上级部门，……") int times,
            @ParamDefine(varName = "isTop",description = "是否从顶部取值，传1返回符合等级的最上层的用户组，传0则返回符合等级的第一个找到的用户组") int isTop){
        //输入参数不合法则返回null
        if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(rankLevels) || times<0){
            return null;
        }
        if(isTop!=0 && isTop!=1){
            return null;
        }
        String newId=getValue(groupId);
        OsGroupDto group= orgClient.getGroupById(newId);
        if(group==null){
            return null;
        }
        //循环往上查找用户组，找到就存在数组中
        String [] arrLevels=rankLevels.split(",");  //判断的组等级数组
        ArrayList<Object> groupLink=new ArrayList<>(times+1);
        groupLink.add(group);
        for(int i=0;i<times;i++){
            //不存在父节点，则提前跳出循环
            String pId=group.getParentId();
            if(StringUtils.isEmpty(pId) || "0".equals(pId)){
                break;
            }
            //没查到父节点
            group=orgClient.getGroupById(pId);
            if(group==null){
                return null ;
            }
            groupLink.add(group);
        }
        Object[] arrGroups=groupLink.toArray();
        //如果从顶部开始取值,则将数据反转
        if(isTop==1){
            ArrayUtils.reverse(arrGroups);
        }
        //循环从数组中找到符合条件的用户组
        for (int j=0;j<arrGroups.length;j++){
            OsGroupDto curGroup=(OsGroupDto)arrGroups[j];
            Integer level=curGroup.getRankLevel();
            String curLevel="";
            if(level!=null){
                curLevel=level.toString();
            }
            if(ArrayUtils.contains(arrLevels,curLevel)){
                return curGroup;
            }
        }
        return null;
    }
    /**
    * @Description: 如果传入的值为JSON格式，则返回字段的value属性值
    * @param filed 字段值
    * @Author: Elwin ZHANG  @Date: 2021/10/15 10:09
    **/
    public String getValue(String filed){
        if(StringUtils.isEmpty(filed)){
            return null;
        }
        if(filed.indexOf("}")<=0){
            return filed;
        }
        try{
            JSONObject object=JSONObject.parseObject(filed);
            return object.getString("value");
        }catch (Exception e){}
        return "";
    }




    @MethodDefine(title = "获取某个部门的负责人",description = "获取某个部门的负责人作为任务节点执行人")
    public List <TaskExecutor> getGroupLeader(@ParamDefine(varName = "groupId",description = "组ID") String groupId){
        List<OsRelInstDto> list=orgClient.getByRelTypeKeyParty1("GROUP-USER-LEADER",groupId);
        if(list==null){
            return  null;
        }
        Set<String> userIds=new HashSet();
        for(int i=0;i<list.size();i++){
            userIds.add(list.get(i).getParty2());
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }

    @MethodDefine(title = "获取某个部门下的关系用户",description = "获取某个部门下的关系用户作为任务节点执行人")
    public List<TaskExecutor> getGroupIdRelKeyByUser(@ParamDefine(varName = "groupId",description = "组ID") String groupId,
                                 @ParamDefine(varName = "relKey",description = "组关系") String relKey){
        List<OsRelInstDto> list=orgClient.getByRelTypeKeyParty1(relKey,groupId);
        if(list==null){
            return  null;
        }
        Set<String> userIds=new HashSet();
        for(int i=0;i<list.size();i++){
            userIds.add(list.get(i).getParty2());
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }

    /**
     * 获取当前用户Id
     * @return
     */
    @MethodDefine(title="获取当前用户Id")
    public String getCurUserId(){
        return ContextUtil.getCurrentUserId();
    }

    /**
     * 获取当前用户姓名
     * @return
     */
    @MethodDefine(title="获取当前用户姓名")
    public String getCurUserName(){
        return ContextUtil.getCurrentUser().getFullName();
    }
    /**
     * 获取当前用户对象
     * @return
     */
    @MethodDefine(title="获取当前用户")
    public IUser getCurUser(){
        return ContextUtil.getCurrentUser();
    }


    @MethodDefine(title="获取当前用户账号")
    public String getCurUserAccount(){
        return ContextUtil.getCurrentUser().getAccount();
    }

    /**
     * 获取当前用户的类型。
     * @return
     */
    @MethodDefine(title="获取当前用户类型")
    public String getCurUserType(){
        String userId=ContextUtil.getCurrentUserId();
        OsUserDto userDto=orgClient.getUserById(userId);
        return userDto.getUserType();
    }

    @MethodDefine(title="获取用户的用户类型")
    public String getUserType(@ParamDefine(varName = "userId",description = "用户Id") String userId){
        OsUserDto userDto=orgClient.getUserById(userId);
        return userDto.getUserType();
    }

    @MethodDefine(title="判断用户是否在指定组中")
    public boolean isInGroup(@ParamDefine(varName = "userId",description = "用户Id") String userId,
                             @ParamDefine(varName = "groupKey",description = "用户组Key") String groupKey){
       Collection<OsGroupDto> groups= orgClient.getBelongGroups(userId);
       if(groups==null){
           return false;
       }
       for(OsGroupDto dto:groups){
           if(dto.getKey().equalsIgnoreCase(groupKey)){
               return true;
           }
       }
       return false;
    }

    @MethodDefine(title="判断用户是否在指定组中")
    public boolean isInSubGroup(@ParamDefine(varName = "userId",description = "用户Id") String userId,
                             @ParamDefine(varName = "groupKey",description = "用户组ID") String groupId){
        Collection<OsGroupDto> groups= orgClient.getBelongGroups(userId);
        if(groups==null){
            return false;
        }
        List<String> groupIds=orgClient.getDownDeps(groupId);
        if(groupIds==null){
            return  false;
        }
        for(OsGroupDto dto:groups){
            if(groupIds.contains(dto.getGroupId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前用户是否在某个组中。
     * @param groupKey
     * @return
     */
    @MethodDefine(title="判断当前用户是否在指定组中")
    public boolean isInGroup(@ParamDefine(varName = "groupKey",description = "用户组ID") String groupKey){
        String userId=ContextUtil.getCurrentUserId();
        Collection<OsGroupDto> groups= orgClient.getBelongGroups(userId);
        if(groups==null){
            return false;
        }
        for(OsGroupDto dto:groups){
            if(dto.getKey().equalsIgnoreCase(groupKey)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前用户姓名
     * @return
     */
    @MethodDefine(title="获取当前用户上级")
    public IUser getCurUserSuperior(@ParamDefine(varName = "relKey",description = "用户关系类型key") String relKey){
        IUser curUser = ContextUtil.getCurrentUser();
        List<OsRelInstDto> relInstDtos = orgClient.getByRelTypeKeyParty2(relKey, curUser.getUserId());
        if(relInstDtos!=null && relInstDtos.size()>0){
            return orgClient.getUserById(relInstDtos.get(0).getParty1());
        }else {
            return null;
        }
    }

    @MethodDefine(title="查找某用户组下某角色用户作为执行人")
    public List<TaskExecutor> getTaskExecutorsByGroupIdsRoleIds(@ParamDefine(varName = "groupIds",description = "用户组IDs") String groupIds,
                                                              @ParamDefine(varName = "roleIds",description = "角色IDs") String roleIds){
        List<String> userIds = orgClient.getUserIdsByGroupIdsRoleIds(groupIds,roleIds);
        if(userIds==null){
            return  null;
        }
        List<TaskExecutor> taskExecutors=new ArrayList<>();
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        if(userIds==null){
            return null;
        }
        for(OsUserDto user : users ){
            taskExecutors.add(TaskExecutor.getUser(user.getUserId(),user.getFullName(),user.getAccount()));
        }
        return taskExecutors;
    }


    @MethodDefine(title = "获取用户组",description = "根据用户组ID获取用户组")
    public OsGroupDto getOsGroup(@ParamDefine(varName = "groupId",description = "组ID") String groupId){
        OsGroupDto groupDto=orgClient.getGroupById(groupId);
        return groupDto;
    }

    @MethodDefine(title = "通过组织维度Key组Key获取用户组",description = "通过组织维度Key组Key获取用户组")
    public OsGroupDto getByDimKeyGroupKey(@ParamDefine(varName = "dimKey",description = "维度Key") String dimKey,
                                 @ParamDefine(varName = "groupKey",description = "组Key") String groupKey){
        OsGroupDto groupDto=orgClient.getByDimKeyGroupKey(dimKey,groupKey);
        return groupDto;
    }

    @MethodDefine(title="查找存在于两个用户组下的用户作为执行人")
    public List<TaskExecutor> getTaskExecutorsByGroupId1GroupId2(@ParamDefine(varName = "groupId1",description = "用户组ID1") String groupId1,
                                                                 @ParamDefine(varName = "groupId2",description = "用户组ID2") String groupId2){
        List<String> userIds = orgClient.getUserIdsByGroupId1GroupId2(groupId1,groupId2);
        if(userIds==null){
            return  null;
        }
        List<TaskExecutor> taskExecutors=new ArrayList<>();
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        if(users==null){
            return null;
        }
        for(OsUserDto user : users ){
            taskExecutors.add(TaskExecutor.getUser(user.getUserId(),user.getFullName(),user.getAccount()));
        }
        return taskExecutors;
    }

    @MethodDefine(title="查找与某用户组存在某一关系的用户作为执行人")
    public List<TaskExecutor> getTaskExecutorsByGroupIdRelTypeKey(@ParamDefine(varName = "groupId",description = "用户组ID") String groupId,
                                                                  @ParamDefine(varName = "relTypeKey",description = "关系标识Key") String relTypeKey){
        List<String> userIds = orgClient.getUserIdsByGroupIdRelTypeKey(groupId,relTypeKey);
        if(userIds==null){
            return  null;
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }

    @MethodDefine(title="查找与某用户存在某一关系的用户作为执行人")
    public List<TaskExecutor> getTaskExecutorsByUserIdRelTypeKey(@ParamDefine(varName = "userId",description = "用户ID") String userId,
                                                                 @ParamDefine(varName = "relTypeKey",description = "关系标识Key") String relTypeKey){
        List<String> userIds = orgClient.getUserIdsByUserIdRelTypeKey(userId,relTypeKey);
        if(userIds==null){
            return  null;
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }

    @MethodDefine(title = "将单个用户转换成任务执行人")
    public  List<TaskExecutor> changeUser2Executor( @ParamDefine(varName = "user",description = "单个用户")OsUserDto user){
        List<TaskExecutor> tasks=new ArrayList<>();
        tasks.add(TaskExecutor.getUser(user));
        return tasks;
    }
    @MethodDefine(title = "将用户列表转换成任务执行人")
    public  List<TaskExecutor> changeUsers2Executors(@ParamDefine(varName = "users",description = "用户列表")List<OsUserDto>  users){
        if(users==null){
            return null;
        }
        List<TaskExecutor> tasks=new ArrayList<>();
        for(int i=0;i<users.size();i++) {
            tasks.add(TaskExecutor.getUser(users.get(i)));
        }
        return tasks;
    }
    @MethodDefine(title = "将逗号分隔的用户列表转换成任务执行人")
    public  List<TaskExecutor> changeUserStr2Executors(@ParamDefine(varName = "users",description = "用户列表")String users){
        if(StringUtils.isEmpty(users)){
            return null;
        }
        String [] arrUsers=users.split(",");
        List<TaskExecutor> tasks=new ArrayList<>();
        for(int i=0;i<arrUsers.length;i++) {
            String userId=arrUsers[i];
            if (userId.length()>0) {
                OsUserDto userdto=orgClient.getUserById(userId);
                if(userdto!=null) {
                    tasks.add(TaskExecutor.getUser(userdto));
                }
            }
        }
        return tasks;
    }
    @MethodDefine(title = "获取当前人指定维度下的组关系用户",description = "获取当前人指定维度下的组关系用户")
    public List<TaskExecutor> getUserByDimCodeAndRelKey(@ParamDefine(varName = "dimCode",description = "维度编码") String dimCode,
                                                        @ParamDefine(varName = "relKey",description = "组关系") String relKey,
                                                        @ParamDefine(varName = "upward",description = "是否向上查询") boolean upward){

        //根据维度编码获取维度
        OsDimensionDto dimension = orgClient.getDimensionByCode(dimCode);
        List<OsRelInstDto> list =new ArrayList<>();
        //当前人指定维度所在的组织
        List<OsRelInstDto> osRelInstDtos = orgClient.getByRelTypeKeyParty2AndDim1("", ContextUtil.getCurrentUserId(), dimension.getDimId());
        if(osRelInstDtos==null){
            return  null;
        }
        //根据关系定义KEY和当前方ID和维度Id查找关系数据
        for(int i=0;i<osRelInstDtos.size();i++){
            String groupId = osRelInstDtos.get(i).getParty1();
            //需要向上查询
            if(upward){
                OsGroupDto group = orgClient.getGroupById(groupId);
                groupId=group.getParentId();
                //无上级则直接返回
                if("0".equals(groupId)){
                    return new ArrayList<>();
                }
            }
            do{
                list = orgClient.getByRelTypeKeyParty1AndDim1(relKey,groupId , dimension.getDimId());
                if(list.size()==0){
                    OsGroupDto osGroupDto = orgClient.getGroupById(groupId);
                    groupId=osGroupDto.getParentId();
                    //无上级则直接返回
                    if("0".equals(groupId)){
                        return new ArrayList<>();
                    }
                }
            }while (list.size()==0);
        }
        Set<String> userIds=new HashSet();
        for(int i=0;i<list.size();i++){
            userIds.add(list.get(i).getParty2());
        }
        List<OsUserDto> users=orgClient.getUsersByIds(StringUtils.join(userIds,","));
        return changeUsers2Executors(users);
    }
}
