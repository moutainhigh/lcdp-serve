package com.redxun.form.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsInstDto;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.feign.OsGroupClient;
import com.redxun.feign.OsInstClient;
import com.redxun.feign.OsUserClient;
import com.redxun.feign.org.OrgClient;
import com.redxun.form.core.entity.FormBoPmt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionSqlService {

    @Resource
    FormEntityDataSettingServiceImpl formEntityDataSettingService;

    @Resource
    OsUserClient osUserClient;
    @Resource
    OsGroupClient osGroupClient;
    @Resource
    OsInstClient osInstClient;

    @Resource
    OrgClient orgClient;

    @Resource
    IOrgService orgService;



    public static String conditionAndStr = "conditionAnd";
    public static String conditionOrStr = "conditionOr";
    public static String fieldStr = "field";
    public static String SELF = "SELF";
    public static String DUP_USERS = "DUP_USERS";
    public static String UP_USERS = "UP_USERS";
    public static String DDOWN_USERS = "DDOWN_USERS";
    public static String DOWN_USERS = "DOWN_USERS";
    public static String SELF_DEP = "SELF_DEP";
    public static String DUP_DEPS = "DUP_DEPS";
    public static String UP_DEPS = "UP_DEPS";
    public static String DDOWN_DEPS = "DDOWN_DEPS";
    public static String DOWN_DEPS = "DOWN_DEPS";
    public static String SELF_TENANT = "SELF_TENANT";
    public static String DUP_TENANTS = "DUP_TENANTS";
    public static String UP_TENANTS = "UP_TENANTS";
    public static String DDOWN_TENANTS = "DDOWN_TENANTS";
    public static String DOWN_TENANTS = "DOWN_TENANTS";
    public static String SELF_DOWN = "SELF_DOWN";


    public static String YES = "YES";

    /**
    * 用户如果为部门负责人，则返回该部门下的所有成员
    **/
    public  static  String CHARGE_DEPT_USERS="CHARGE_DEPT_USERS";

    public static String TYPE_USER = "user";
    public static String TYPE_GROUP = "group";
    public static String TYPE_INST = "inst";
    public static String TYPE_COMPANY = "company";
    public static String TYPE_LOGIC_DEL = "logicDel";



    @Resource
    FormBoPmtServiceImpl formBoPmtService;


    public String getPermissionSql(JSONArray dataRight, JSONArray arr, String condition,JSONObject json){
        StringBuffer conSb=new StringBuffer();
        String cond="and";
        if(conditionOrStr.equals(condition)){
            cond="or";
        }
        for(int i=0;i<dataRight.size();i++) {
            JSONObject right = dataRight.getJSONObject(i);
            String rightId = right.getString("idx_");
            String rightType = right.getString("type");
            String rightScope = right.getString("scope");
            if(right.containsKey("children") && (conditionAndStr.equals(rightType) || conditionOrStr.equals(rightType))){
                String type="and";
                if(conditionOrStr.equals(rightType)){
                    type="or";
                }
                JSONObject child;
                if(json.containsKey(type)){
                    child=json.getJSONObject(type);
                }else{
                    child=new JSONObject();
                    json.put(type,child);
                }
                String perrmissionSql=getPermissionSql(right.getJSONArray("children"),right.getJSONArray("children"),rightType,child);
                if(StringUtils.isNotEmpty(perrmissionSql)) {
                    if(conSb.length() > 0 ){
                        conSb.append(" " + type + " (" + perrmissionSql + ")");
                    }else{
                        conSb.append("  (" + perrmissionSql + ")");
                    }
                }
                continue;
            }
            if(fieldStr.equals(rightType) && "grant".equals(rightScope)){
                String rightField = right.getString("field");
                //表前缀
                String rightTablePre = right.getString("tablePre");
                String rightOp = right.getString("op");
                String grant=right.getJSONObject("configs").getString("grant");
                String rightScope2=formEntityDataSettingService.queryDataByValue(grant);
                String rightDataType = right.getString("dataType");
                if(StringUtils.isNotEmpty(rightScope2)){
                    getFieldPremission(conSb, rightField, rightTablePre, rightScope, rightOp, rightScope2, rightDataType,cond,json);
                    continue;
                }

            }
            for (int j = 0; j < arr.size(); j++) {
                JSONObject obj = arr.getJSONObject(j);
                String objId=obj.getString("idx_");
                if(!rightId.equals(objId)){
                    continue;
                }
                String field = obj.getString("field");
                //表前缀
                String tablePre = obj.getString("tablePre");
                String scope = obj.getString("scope");
                String type = obj.getString("type");
                String op = obj.getString("op");
                String scope2 = obj.getString("scope2");
                String dataType = obj.getString("dataType");

                if (TYPE_USER.equals(type)) {
                    //按创建用户查询
                    getUserPermission(conSb, field, tablePre, scope, op, scope2,cond,json);
                } else if (TYPE_GROUP.equals(type)) {
                    //按用户组查找
                    getGroupPremission(conSb, field, tablePre, scope, op, scope2,cond,json);
                } else if (TYPE_INST.equals(type)) {
                    //按机构查询
                    getInstPremission(conSb, field, tablePre, scope, op, scope2,cond,json);
                } else if (fieldStr.equals(type) && !"grant".equals(scope)) {
                    getFieldPremission(conSb, field, tablePre, scope, op, scope2, dataType,cond,json);
                }
                else if (TYPE_LOGIC_DEL .equals(type) ) {
                    //按逻辑删除过滤查询
                    getLogicDelPremission(conSb, field, tablePre, cond, json);
                }
                else if (TYPE_COMPANY.equals(type) ) {
                    //按逻辑删除过滤查询
                    getCompanyPremission(conSb, field, tablePre, cond,scope, json);
                }
            }
        }
        return conSb.toString();
    }

    /**
     * 构造分公司权限。
     * @param conSb
     * @param field
     * @param tablePre
     * @param cond
     * @param scope
     * @param json
     */
    private void getCompanyPremission(StringBuffer conSb,String field,String tablePre,String cond,String scope,JSONObject json){

        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        String companyId=ContextUtil.getComanyId();
        //当前公司
        if("CURRENT".equals(scope)){
            conSb.append( field+"='").append(companyId).append("'");
            json.put("Q_"+field+"_S_EQ",companyId);
        }
        //下级公司
        else if("DDOWN".equals(scope)){
            JSONObject companyJson= orgService.getCompanys();
            if(!companyJson.getBoolean("supportGrade")){
                return;
            }
            JSONArray jsonAry=companyJson.getJSONArray("companys");
            List<String> companys=new ArrayList<>();
            for(int i=0;i<jsonAry.size();i++){
                JSONObject company=jsonAry.getJSONObject(i);
                String tmpId=company.getString("groupId");
                companys.add(tmpId);
            }
            if(BeanUtil.isNotEmpty(companys)){

                String companyIds=listToInSqlWithBrackets(companys);

                conSb.append(field+" in "+companyIds);
                json.put("Q_"+field+"_S_IN",StringUtils.join(companys,","));
            }

        }


    }

    //逻辑删除
    private void getLogicDelPremission(StringBuffer conSb,String field,String tablePre,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append(field+"=0");
        json.put("Q_"+field+"_S_EQ",0);
        return;
    }

    private void getEqualsInst(StringBuffer conSb,String field,String tablePre,String scope,String cond,JSONObject json){
        IUser curUser= ContextUtil.getCurrentUser();
        String curUserId=curUser.getUserId();
        String curTenantId=curUser.getTenantId();
        if(curTenantId==null){
            curTenantId="0";
            return;
        }
        OsInstDto itenant=osInstClient.getById(curTenantId);

        if(SELF_TENANT.equals(scope)){
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append(field+"='").append(curTenantId).append("'");
            json.put("Q_"+field+"_S_EQ",curTenantId);
            return;
        }else if(DUP_TENANTS.equals(scope)){
            //取得上级机构
            OsInstDto pInst=osInstClient.getById(itenant.getParentId());
            String pInstId=(pInst!=null)?pInst.getInstId():"0";
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append( field+"='").append(pInstId).append("'");
            json.put("Q_"+field+"_S_EQ",pInstId);
        }else if(UP_TENANTS.equals(scope)){
            //取得上级机构
            OsInstDto pInst=osInstClient.getById(itenant.getParentId());
            //若当前的目录路径为空,则让他找不到数据
            String pPath= StringUtils.isEmpty(pInst.getPath())?"-1":pInst.getPath();
            String parentPath=StringUtils.getParentPath(pPath);
            String idArr=StringUtils.getArrCharString(parentPath);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append( field+" in ("+idArr+")");
            json.put("Q_"+field+"_S_IN",idArr);
        }else if(DDOWN_TENANTS.equals(scope)){
            List<String> ddownTenantIds=osInstClient.getDdownTenantIds(curTenantId);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String tenantIds=listToInSqlWithBrackets(ddownTenantIds);

            conSb.append(field+" in "+tenantIds);
            json.put("Q_"+field+"_S_IN",tenantIds);
        }else if(DOWN_TENANTS.equals(scope)){
            List<String> downTenantIds=osInstClient.getDownTenantIds(curTenantId);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String tenantIds=listToInSqlWithBrackets(downTenantIds);

            conSb.append( field+" in "+tenantIds);
            json.put("Q_"+field+"_S_IN",tenantIds);
        }
    }

    private void getFieldPremission(StringBuffer conSb,String field,String tablePre,String scope,String op,String scope2,String dataType,String cond,JSONObject json){
        if("=".equals(op)){
            getEqualsField(conSb,field,tablePre,scope,cond,json);
        }else if("in".equals(op)){
            getInField(conSb,field,tablePre,dataType,scope,scope2,cond,json);
        }
    }


    private void getEqualsGroup(StringBuffer conSb,String field,String tablePre,String scope,String cond,JSONObject json){
        IUser curUser= ContextUtil.getCurrentUser();
        String curUserId=curUser.getUserId();
        OsGroupDto mainGroup=osGroupClient.getMainDeps(curUserId,curUser.getTenantId());
        String curDepId;
        if(mainGroup==null){
            curDepId="0";
            return;
        }
        curDepId= mainGroup.getGroupId();
        //默认部门
        if(SELF_DEP.equals(scope)){
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append(  field +"='").append(curDepId).append("'");
            json.put("Q_"+field+"_S_EQ",curDepId);
            return;
        }
        //直属上级部门
        if(DUP_DEPS.equals(scope)){
            //取得上级部门
            OsGroupDto upGroup=osGroupClient.getById(mainGroup.getParentId());
            String parentGpId=(upGroup!=null)?upGroup.getGroupId():"0";
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append( field+"='").append(parentGpId).append("'");
            json.put("Q_"+field+"_S_EQ",parentGpId);
        }else if(UP_DEPS.equals(scope)){
            //所有上级部门
            //若当前的目录路径为空,则让他找不到数据
            String groupPath= StringUtils.isEmpty(mainGroup.getPath())?"-1":mainGroup.getPath();
            String parentPath=StringUtils.getParentPath(groupPath);
            String idArr=StringUtils.getArrCharString(parentPath);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }

            appendTablePre(tablePre,conSb);
            if(StringUtils.isNotEmpty(idArr)){
                conSb.append( field+" in ("+idArr+")");
                json.put("Q_"+field+"_S_IN",idArr);
            }
        }
        //直接下级
        else if(DDOWN_DEPS.equals(scope)){
            List<String> ddownDeps=osGroupClient.getDdownDeps(mainGroup.getGroupId());
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String groups=listToInSqlWithBrackets(ddownDeps);

            conSb.append (field+" in "+groups );
            json.put("Q_"+field+"_S_IN",groups);
        }else if(DOWN_DEPS.equals(scope)){
            List<String> downDeps=osGroupClient.getDownDeps(mainGroup.getGroupId());
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String groups=listToInSqlWithBrackets(downDeps);

            conSb.append(  field +" in "+groups );
            json.put("Q_"+field+"_S_IN",groups);
        }
        //当前人的部门的下级组织
        else if(SELF_DOWN.equals(scope)){
            List<String> subDeps=orgClient.getGroupsByUser(curUser.getUserId(),curUser.getTenantId());

            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String groups=listToInSqlWithBrackets(subDeps);

            conSb.append(  field +" in " +groups );
            json.put("Q_"+field+"_S_IN",groups);
        }
    }

    /**
     * 将列表转成 '1','2','3' 的格式。
     * @param list
     * @return
     */
    private String listToInSql(List<String> list,boolean isString){
        if(!isString){
            return StringUtils.join(list,",");
        }
        List<String> groups=new ArrayList<>();
        for(String groupId:list){
            groups.add("'" + groupId +"'");
        }
        return StringUtils.join(groups,",");
    }

    private String listToInSqlWithBrackets(List<String> list){
        String str=listToInSql(list,true);
        return "(" + str +")";
    }


    private void getUserPermission(StringBuffer conSb,String field,String tablePre,String scope,String op,String scope2,String cond,JSONObject json){
        if("=".equals(op)){
            getEqualsUser(conSb,field,tablePre,scope,cond,json);
        }else if("in".equals(op)){
            getInUser(conSb,field,tablePre,scope,scope2,cond,json);
        }
    }

    public JSONObject parsePermissionJson(FormBoPmt formBoPmt){
        JSONObject json=new JSONObject();
        String dataRight = formBoPmt.getDatas();
        JSONArray arr = JSONArray.parseArray(dataRight);
        if (arr != null && arr.size() > 0) {
            getPermissionSql(arr, arr, conditionAndStr,json);
        }
        return json;
    }

    public String parsePermissionSql(String boListId, String menuId,String pmtAlias) {
        JPaasUser user = (JPaasUser) ContextUtil.getCurrentUser();
        String permissonSql = "";
        if(StringUtils.isEmpty(pmtAlias)) {
            if (user == null || (user.isAdmin() && !user.isTenantAdmin() && !user.isSwitchedCompany()) || StringUtils.isEmpty(menuId)) {
                return permissonSql;
            }
        }
        boolean flag = false;
        FormBoPmt formBoPmt=null;
        if(StringUtils.isNotEmpty(pmtAlias)){
            formBoPmt=formBoPmtService.getByAlias(boListId,pmtAlias);
        }else {
            formBoPmt=formBoPmtService.getByBoListIdMenuId(boListId, menuId);
        }
        if(BeanUtil.isNotEmpty(formBoPmt)) {
            String dataRight = formBoPmt.getDatas();
            JSONArray arr = JSONArray.parseArray(dataRight);
            if (arr != null && arr.size() > 0) {
                String perSql = getPermissionSql(arr, arr, conditionAndStr,new JSONObject());
                if (StringUtils.isNotEmpty(perSql)) {
                    permissonSql += "(" + perSql + ") or ";
                    flag = true;
                }
            }
        }
        if (flag) {
            permissonSql = permissonSql.substring(0, permissonSql.lastIndexOf("or"));
        }

        return permissonSql;
    }

    private void getInUser(StringBuffer conSb,String field,String tablePre,String scope,String scope2,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append( field+" in ").append("("+insertListCharStartEnd(scope,"'")+")");
        json.put("Q_"+field+"_S_IN",scope);
    }

    private void getEqualsUser(StringBuffer conSb,String field,String tablePre,String scope,String cond,JSONObject json){
        IUser curUser= ContextUtil.getCurrentUser();
        String curUserId=curUser.getUserId();
        //本人
        if(SELF.equals(scope)){
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append( field+"='").append(curUserId).append("'");
            json.put("Q_"+field+"_S_EQ",curUserId);
            return;
        }
        //用户如果为部门负责人，则返回该部门下的所有成员
        if(CHARGE_DEPT_USERS.equals(scope)){
            dealChargeDeptUsers(conSb,field,tablePre,cond,json);
            return;
        }
        String upLowPath=osUserClient.getUserUpLowPath(curUser.getUserId());
        //若路径为空，则只能看到自己本身的数据
        if(StringUtils.isEmpty(upLowPath)){
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            conSb.append(  field +"='").append(curUserId).append("'");
            json.put("Q_"+field+"_S_EQ",curUserId);
            return;
        }
        //直属上级用户
        if(DUP_USERS.equals(scope)){
            List<String> dupUserIds=osUserClient.getDupUserIds(upLowPath);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String users=listToInSqlWithBrackets(dupUserIds);

            conSb.append( field+" in ").append(users);
            json.put("Q_"+field+"_S_IN",users);
        }
        //所有上级用户
        else if(UP_USERS.equals(scope)){
            String parentPath=StringUtils.getParentPath(upLowPath);
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);
            String rootParenId="0.";
            if(rootParenId.equals(parentPath)){
                conSb.append( field+" in ").append("(").append(parentPath).append(")");
                json.put("Q_"+field+"_S_IN",parentPath);
            }else{
                String idAry=StringUtils.getArrCharString(parentPath);
                conSb.append( field+" in ").append("(").append(idAry).append(")");
                json.put("Q_"+field+"_S_IN",idAry);
            }

        }
        //直属下级用户
        else if(DDOWN_USERS.equals(scope)){
            List<String> ddownUserIds=osUserClient.getDdownUserIds(curUserId);
            //需要添加判断，如没有直属下级，则要直接返回，否则会导致sql拼接出问题
            if(BeanUtil.isEmpty(ddownUserIds) || ddownUserIds.size() < 1){
                return;
            }
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String users=listToInSqlWithBrackets(ddownUserIds);

            conSb.append( field+" in ").append(users);
            json.put("Q_"+field+"_S_IN",users);
        }else if(DOWN_USERS.equals(scope)){
            List<String> downUserIds=osUserClient.getDownUserIds(upLowPath);
            //需要添加判断，如没有下级，则要直接返回，否则会导致sql拼接出问题
            if(BeanUtil.isEmpty(downUserIds) || downUserIds.size() < 1){
                return;
            }
            if(conSb.length()>0){
                conSb.append(" "+cond+" ");
            }
            appendTablePre(tablePre,conSb);

            String users=listToInSqlWithBrackets(downUserIds);

            conSb.append( field +" in "+users);
            json.put("Q_"+field+"_S_IN",users);
        }
    }

    /**
     * @Description:  如果用户为部门负责人，则返回该部门下的所有成员
     * @param conSb 条件结果
     * @param  field 字段名t称
     * @param  tablePre  表前缀
     * @param   cond  条件连接符AND或OR
     * @Author: Elwin ZHANG  @Date: 2021/11/22 17:42
     **/
    private void dealChargeDeptUsers(StringBuffer conSb,String field,String tablePre,String cond,JSONObject json){
        String curUserId=ContextUtil.getCurrentUserId();
        List<OsRelInstDto>  lstGroup=orgClient.getByRelTypeKeyParty2("GROUP-USER-LEADER",curUserId);
        //当前用户不是负责人时，则退出
        if(lstGroup==null || lstGroup.size()<1){
            return;
        }
        String users="";
        //循环获取多个主管的部门下的成员
        for(int i=0;i<lstGroup.size();i++){
            String groupId=lstGroup.get(i).getParty1();
            //查找组内用户
            List<OsRelInstDto>  lstUsers=orgClient.getByRelTypeKeyParty1("GROUP-USER-BELONG",groupId);
            if(lstUsers==null || lstUsers.size()<1){
                continue;
            }
            for(int j=0;j<lstUsers.size();j++){
                users += ",'" + lstUsers.get(j).getParty2() +"'";
            }
        }
        //没有找到主管部门的组内用户
        if(users.length()==0){
            return;
        }
        users= "(" + users.substring(1) + ")";
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append( field +" in "+users);
        json.put("Q_"+field+"_S_IN",users);
    }

    private void getGroupPremission(StringBuffer conSb,String field,String tablePre,String scope,String op,String scope2,String cond,JSONObject json){
        if("=".equals(op)){
            getEqualsGroup(conSb,field,tablePre,scope,cond,json);
        }else if("in".equals(op)){
            getInGroup(conSb,field,tablePre,scope,scope2,cond,json);
        }
    }

    private void getInGroup(StringBuffer conSb,String field,String tablePre,String scope,String scope2,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append( field+" in ").append("("+insertListCharStartEnd(scope,"'")+")");
        json.put("Q_"+field+"_S_IN",scope);
    }



    private void getInstPremission(StringBuffer conSb,String field,String tablePre,String scope,String op,String scope2,String cond,JSONObject json){
        if("=".equals(op)){
            getEqualsInst(conSb,field,tablePre,scope,cond,json);
        }else if("in".equals(op)){
            getInInst(conSb,field,tablePre,scope,scope2,cond,json);
        }
    }

    private void getInInst(StringBuffer conSb,String field,String tablePre,String scope,String scope2,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append( field+" in ").append("("+insertListCharStartEnd(scope,"'")+")");
        json.put("Q_"+field+"_S_IN",scope);
    }


    private void getEqualsField(StringBuffer conSb,String field,String tablePre,String scope,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        appendTablePre(tablePre,conSb);
        conSb.append( field+" ='").append(scope).append("'");
        json.put("Q_"+field+"_S_EQ",scope);
    }

    private void getInField(StringBuffer conSb,String field,String tablePre,String dataType,String scope,String scope2,String cond,JSONObject json){
        if(conSb.length()>0){
            conSb.append(" "+cond+" ");
        }
        if(StringUtils.isEmpty(dataType) || "string".equals(dataType) || "varchar".equals(dataType) || "clob".equals(dataType)){
            appendTablePre(tablePre,conSb);
            conSb.append( field+" in ").append("("+insertListCharStartEnd(scope2,"'")+")");
            json.put("Q_"+field+"_S_IN",scope2);
        }else if("number".equals(dataType) || "int".equals(dataType)){
            conSb.append("(");
            appendTablePre(tablePre,conSb);
            conSb.append( field+" >= ").append(scope);
            json.put("Q_"+field+"_I_GE",scope);
            if(StringUtils.isNotEmpty(scope2)){
                conSb.append(" "+cond+" ");
                appendTablePre(tablePre,conSb);
                conSb.append( field+" <= ").append(scope2);
                json.put("Q_"+field+"_I_LE",scope2);
            }
            conSb.append(")");
        }else if("date".equals(dataType)){
            conSb.append("(");
            appendTablePre(tablePre,conSb);
            conSb.append( field+" >= ").append("'"+scope+"'");
            json.put("Q_"+field+"_D_GE",scope);
            if(StringUtils.isNotEmpty(scope2)){
                conSb.append(" "+cond+" ");
                appendTablePre(tablePre,conSb);
                conSb.append( field+" <= ").append("'"+scope2+"'");
                json.put("Q_"+field+"_D_LE",scope2);
            }
            conSb.append(")");
        }
    }



    private void appendTablePre(String tablePre,StringBuffer sb){
        if(StringUtils.isNotEmpty(tablePre)){
            sb.append(" "+ tablePre +".");
        }
    }


    private String insertListCharStartEnd(String str,String charStr){
        String[] strAry=str.split(",");
        List<String> newStr=new ArrayList<>();
        for(String s:strAry){
            newStr.add(charStr + s + charStr);
        }
        return StringUtils.join(newStr,",");
    }

    public String insertWhereSql(String sql,String whereSql){
        sql=sql.replaceAll("\r", " ");
        sql=sql.replaceAll("\n", " ");
        sql=sql.replaceAll("(?is)\\s+", " ");
        //判断语句中有没有 where
        if(sql.indexOf(CommonDao.WHERE)==-1){
            //没有order by
            if(sql.indexOf(CommonDao.ORDERBY)==-1){
                sql += " WHERE " +whereSql ;
            }
            else{
                String[] arySql= sql.split(CommonDao.ORDERBY);
                sql=arySql[0] +" WHERE " + whereSql + CommonDao.ORDERBY + " " + arySql[1];
            }
        }
        else{
            //判断有没有order by
            if(sql.indexOf(CommonDao.ORDERBY)==-1){
                sql+=" AND (" +whereSql +")";
            }
            else{
                String[] arySql= sql.split(CommonDao.ORDERBY);
                sql=arySql[0] +" AND ("+whereSql +")" + CommonDao.ORDERBY + " " + arySql[1];
            }
        }
        sql=sql.replaceFirst("(?i)WHERE\\s+1=1\\s*AND"," WHERE ");
        return  sql;
    }

}
