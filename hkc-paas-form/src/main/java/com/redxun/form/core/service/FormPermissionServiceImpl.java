package com.redxun.form.core.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.form.FormConst;
import com.redxun.dto.form.FormParams;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPermission;
import com.redxun.form.core.mapper.FormPermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * [表单权限配置]业务服务类
 */
@Service
public class FormPermissionServiceImpl extends SuperServiceImpl<FormPermissionMapper, FormPermission> implements BaseService<FormPermission> {


    @Resource
    private FormPermissionMapper formPermissionMapper;


    @Override
    public BaseDao<FormPermission> getRepository() {
        return formPermissionMapper;
    }

    /**
     * 根据表单方案ID获取权限。
     *
     * @param configId
     * @param type
     * @return
     */
    public FormPermission getByConfig(String type, String configId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("TYPE_", type);
        wrapper.eq("CONFIG_ID_", configId);
        FormPermission permission = formPermissionMapper.selectOne(wrapper);
        return permission;
    }

    /**
     * 根据表单查询权限
     *
     * @param formId
     * @return
     */
    public FormPermission getByFormId(String formId) {
        FormPermission formPermission = getByConfig(FormPermission.TYPE_FORM, formId);
        return formPermission;
    }

    /**
     * 根据表单方案ID查询权限
     *
     * @param solId
     * @return
     */
    public FormPermission getByFormSolId(String solId) {
        FormPermission formPermission = getByConfig(FormPermission.TYPE_FORM, solId);
        return formPermission;
    }

    /**
     * 根据表单Id或者表单方案ID查询权限
     *
     * @param solId
     * @return
     */
    public FormPermission getByconfigId(String type, String solId) {
        FormPermission formPermission = getByConfig(type, solId);
        return formPermission;
    }


    /**
     * 根据boDefId 获取初始的权限。
     * {
     * name:"w",
     * address:"w",
     * sub__contract:{name:"w",price:"w",amount:"w",tablerights:{add:true,del:true,delExist:true,up:true,down:true}},
     * sub__linkman:{name:"w",phone:"w",tablerights:{add:true,del:true,delExist:true,up:true,down:true}},
     * buttons:{add:true,del:true,delExist:true,up:true,down:true},
     * }
     *
     * @param boEntity
     * @return
     */
    public JSONObject getInitPermission(FormBoEntity boEntity,FormPc formPc) {
        JSONObject permissionJson = new JSONObject();

        //主表自动
        List<FormBoAttr> mainAttrs = boEntity.getBoAttrList();
        for (FormBoAttr attr : mainAttrs) {
            permissionJson.put(attr.getName(), FormPermission.PERMISSION_WRITE);
        }

        //主表按钮
        mainBtnCalcFields(permissionJson, new JSONObject(),new JSONObject(),new HashMap<>(), formPc, false,null);
        //子表
        List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
        for (FormBoEntity subEnt : boEntityList) {
            String entName = subEnt.getAlias();
            JSONObject subJson = getEntPermission(subEnt,formPc,entName);
            permissionJson.put(FormConst.SUB_PRE + entName, subJson);
        }

        return permissionJson;

    }

    public JSONObject calcRights(FormBoEntity boEntity, JSONObject rightSetting, Map<String, Set<String>> profileMap, FormPc formPc, boolean readonly, FormParams formParams) {

        JSONObject permissionJson = new JSONObject();

        //主表
        JSONObject main = null;
        if (BeanUtil.isEmpty(rightSetting) || !rightSetting.containsKey(FormPermission.PERMISSION_MAIN)) {
            main = new JSONObject();
        } else {
            main = rightSetting.getJSONObject(FormPermission.PERMISSION_MAIN);
        }

        //按钮
        JSONObject tabBtn = null;
        if (!rightSetting.containsKey(FormPermission.PERMISSION_TABBTN)) {
            tabBtn = new JSONObject();
        } else {
            tabBtn = rightSetting.getJSONObject(FormPermission.PERMISSION_TABBTN);
        }

        //TAB
        JSONObject tabList = null;
        if (!rightSetting.containsKey(FormPermission.PERMISSION_TABLIST)) {
            tabList = new JSONObject();
        } else {
            tabList = rightSetting.getJSONObject(FormPermission.PERMISSION_TABLIST);
        }

        //子表
        JSONObject sub = null;
        if (!rightSetting.containsKey(FormPermission.PERMISSION_SUB)) {
            sub = new JSONObject();
        } else {
            sub = rightSetting.getJSONObject(FormPermission.PERMISSION_SUB);
        }


        //计算主表
        List<FormBoAttr> mainAttrs = boEntity.getBoAttrList();
        calcFields(permissionJson, mainAttrs, main, profileMap, readonly,formParams);

        //计算主表按钮
        mainBtnCalcFields(permissionJson, tabBtn,tabList, profileMap, formPc, false,formParams);


        //计算子表
        List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
        subCalcFields(permissionJson, boEntityList, sub, profileMap, formPc, readonly,formParams);
        return permissionJson;
    }


    private void mainBtnCalcFields(JSONObject permissionJson, JSONObject tabBtn,JSONObject tabList,
                                   Map<String, Set<String>> profileMap, FormPc formPc, boolean readonly,FormParams formParams) {
        //主表按钮权限列表
        JSONObject tabBtnJson = new JSONObject();
        String buttons = formPc.getButtonDef();
        if (StringUtils.isNotEmpty(buttons)){
            JSONArray btns = JSONArray.parseArray(buttons);
            for (int i = 0; i < btns.size(); i++) {
                JSONObject btn = btns.getJSONObject(i);
                String alias = btn.getString(FormPermission.PERMISSION_ALIAS);
                if (!tabBtn.containsKey(alias)) {
                    if (readonly) {
                        tabBtnJson.put(alias, false);
                    } else {
                        tabBtnJson.put(alias, true);
                    }

                } else {
                    tabBtnJson.put(alias, btnCalcField(tabBtn.getJSONObject(alias), profileMap, readonly,formParams));
                }
            }
        }
        permissionJson.put("buttons", tabBtnJson);
        //主表TAB权限列表
        JSONObject tabListJson = new JSONObject();
        String tabLists = formPc.getTabDef();
        if (StringUtils.isNotEmpty(tabLists)){
            JSONArray tabListAry = JSONArray.parseArray(tabLists);
            for (int i = 0; i < tabListAry.size(); i++) {
                JSONObject tab = tabListAry.getJSONObject(i);
                String name = tab.getString("name");
                if("表单TAB".equals(name)){
                    continue;
                }
                JSONArray tabChild=tab.getJSONArray("children");

                JSONObject tabJson=new JSONObject();
                if (!tabList.containsKey(name)) {
                    for(Object obj:tabChild) {
                        String subName=((JSONObject)obj).getString("name");
                        if (readonly) {
                            tabJson.put(subName,false);
                        } else {
                            tabJson.put(subName,true);
                        }
                    }
                } else {
                    if(btnCalcTabField(tabList.getJSONObject(name), profileMap,formParams)) {
                        JSONArray tabCalcAry=tabList.getJSONObject(name).getJSONArray("children");
                        for (Object obj : tabChild) {
                            String subName = ((JSONObject) obj).getString("name");
                            for(Object tabCalcObj:tabCalcAry){
                                JSONObject tabCalcJson=((JSONObject)tabCalcObj);
                                if(subName.equals(tabCalcJson.getString("name"))){
                                    tabJson.put(subName, btnCalcTabField(tabCalcJson, profileMap,formParams));
                                }
                            }
                        }
                    }
                }
                tabListJson.put(name,tabJson);
            }
        }
        permissionJson.put("tabs", tabListJson);
    }

    private void subCalcFields(JSONObject permissionJson, List<FormBoEntity> boEntityList, JSONObject sub,
                               Map<String, Set<String>> profileMap, FormPc formPc, boolean readonly,FormParams formParams) {
        for (FormBoEntity subEnt : boEntityList) {
            JSONObject subpermissionJson = new JSONObject();
            String subTabName = subEnt.getAlias();
            //子表按钮权限
            JSONObject tablerightsJson = new JSONObject();
            if (!sub.containsKey(subTabName)) {
                List<FormBoAttr> subAttrs = subEnt.getBoAttrList();
                //子表控件权限
                JSONObject subAttr = new JSONObject();
                calcFields(subpermissionJson, subAttrs, subAttr, profileMap, readonly,formParams);
                //子表不包括，则全部默认可编辑
                subBtnInitPermission(tablerightsJson, formPc, subTabName, readonly);
            } else {
                List<FormBoAttr> subAttrs = subEnt.getBoAttrList();
                //子表控件权限
                JSONObject subJson = sub.getJSONObject(subTabName);
                JSONObject subAttr = subJson.getJSONObject("subAttr");
                calcFields(subpermissionJson, subAttrs, subAttr, profileMap, readonly,formParams);

                JSONObject subTabBtn = subJson.getJSONObject("subTabBtn");
                btnCalcFields(tablerightsJson, subTabBtn, profileMap, formPc, subTabName, readonly,formParams);
            }
            subpermissionJson.put("tablerights", tablerightsJson);
            permissionJson.put(FormConst.SUB_PRE + subTabName, subpermissionJson);
        }
    }

    private void subBtnInitPermission(JSONObject permissionJson, FormPc formPc, String alias, boolean readonly) {
        //子表按钮权限列表
        JSONArray tabBtnList = new JSONArray();
        String buttons = formPc.getTableButtonDef();
        if (StringUtils.isEmpty(buttons)) {
            return;
        } else {
            JSONObject btns = JSONObject.parseObject(buttons);
            if (!btns.containsKey(alias)) {
                return;
            }
            JSONArray btnsJsonArray = btns.getJSONArray(alias);
            for (int i = 0; i < btnsJsonArray.size(); i++) {
                JSONObject btn = btnsJsonArray.getJSONObject(i);
                String btnAlias = btn.getString(FormPermission.PERMISSION_ALIAS);
                boolean isShow = true;
                if (readonly) {
                    isShow = false;
                }
                permissionJson.put(btnAlias, isShow);
                if (FormPermission.PERMISSION_DEFAULT.equals(btn.getString(FormPermission.PERMISSION_TYPE)) &&
                        (FormPermission.PERMISSION_EDIT.equals(btn.getString(FormPermission.PERMISSION_ALIAS)) ||
                                FormPermission.PERMISSION_REMOVE.equals(btn.getString(FormPermission.PERMISSION_ALIAS)))) {
                    permissionJson.put(btnAlias + "Exist", isShow);
                }
            }
        }
    }

    private void btnCalcFields(JSONObject permissionJson, JSONObject fields,
                               Map<String, Set<String>> profileMap, FormPc formPc, String alias, boolean readonly,FormParams formParams) {
        //子表按钮权限列表
        String buttons = formPc.getTableButtonDef();
        if (StringUtils.isEmpty(buttons)) {
            return;
        } else {
            JSONObject btns = JSONObject.parseObject(buttons);
            if (!btns.containsKey(alias)) {
                return;
            }
            JSONArray btnsJsonArray = btns.getJSONArray(alias);
            for (int i = 0; i < btnsJsonArray.size(); i++) {
                JSONObject btn = btnsJsonArray.getJSONObject(i);
                String btnAlias = btn.getString(FormPermission.PERMISSION_ALIAS);
                String btnAliasExist = btnAlias + "Exist";
                if (!fields.containsKey(btnAlias) && !fields.containsKey(btnAliasExist)) {
                    permissionJson.put(btnAlias, true);
                    continue;
                }
                if (fields.containsKey(btnAlias)) {
                    permissionJson.put(btnAlias, btnCalcField(fields.getJSONObject(btnAlias), profileMap, readonly,formParams));
                }
                if (fields.containsKey(btnAliasExist)) {
                    permissionJson.put(btnAliasExist, btnCalcField(fields.getJSONObject(btnAliasExist), profileMap, readonly,formParams));
                }
            }
        }
    }

    private boolean btnCalcField(JSONObject field, Map<String, Set<String>> profileMap, boolean readonly,FormParams formParams) {
        String editAry = field.getString(FormPermission.PERMISSION_EDIT);
        boolean canEdit = hasRights(editAry, profileMap,formParams);
        //有编辑权限
        if (canEdit && !readonly) {
            return true;
        } else {
            return false;
        }
    }

    private boolean btnCalcTabField(JSONObject field, Map<String, Set<String>> profileMap,FormParams formParams) {
        String editAry = field.getString(FormPermission.PERMISSION_EDIT);
        boolean canEdit = hasRights(editAry, profileMap,formParams);
        //有编辑权限
        if (canEdit) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * 计算字段权限。
     *
     * @param permissionJson
     * @param attrs
     * @param fields
     * @param profileMap
     * @param readonly
     */
    private void calcFields(JSONObject permissionJson, List<FormBoAttr> attrs, JSONObject fields,
                            Map<String, Set<String>> profileMap, boolean readonly,FormParams formParams) {
        for (FormBoAttr attr : attrs) {
            String name = attr.getName();
            if (!fields.containsKey(name)) {
                if (readonly) {
                    permissionJson.put(name, FormPermission.PERMISSION_READONLY);
                } else {
                    permissionJson.put(name, FormPermission.PERMISSION_WRITE);
                }
                continue;
            }
            JSONObject field = fields.getJSONObject(name);
            String right = calcField(field, profileMap, readonly,formParams);
            permissionJson.put(name, right);
        }
    }

    private String calcField(JSONObject field, Map<String, Set<String>> profileMap, boolean readonly,FormParams formParams) {
        String editAry = field.getString(FormPermission.PERMISSION_EDIT);
        String readAry = field.getString(FormPermission.PERMISSION_READ);
        String requireAry = field.getString(FormPermission.PERMISSION_REQUIRE);

        //只读
        if (readonly) {
            //可以读取
            boolean canRead = hasRights(readAry, profileMap,formParams);
            if (canRead) {
                //附件的删除、下载权限
                if(readAry.indexOf("deleteAttach") > -1 || readAry.indexOf("downloadAttach") > -1 || readAry.indexOf("noneAttach") > -1){
                    JSONObject jo = new JSONObject();
                    jo.put("default", FormPermission.PERMISSION_READONLY);
                    jo.put("deleteAttach", 0);
                    jo.put("downloadAttach", 0);

                    String[] arr = readAry.split(",");
                    for(String s : arr){
                        if(s.equals("deleteAttach")){
                            jo.put("deleteAttach", 1);
                        }else if(s.equals("downloadAttach")){
                            jo.put("downloadAttach", 1);
                        }
                    }
                    return jo.toJSONString();
                }else{
                    return FormPermission.PERMISSION_READONLY;
                }

            } else {
                return FormPermission.PERMISSION_NONE;
            }
        }
        //编辑
        else {
            boolean canEdit = hasRights(editAry, profileMap,formParams);
            if (canEdit) {
                boolean require = hasRights(requireAry, profileMap,formParams);
                if (require) {
                    return "required";
                } else {
                    //附件的删除、下载权限
                    if(editAry.indexOf("deleteAttach") > -1 || editAry.indexOf("downloadAttach") > -1 || editAry.indexOf("noneAttach") > -1){
                        JSONObject jo = new JSONObject();
                        jo.put("default", FormPermission.PERMISSION_WRITE);
                        jo.put("deleteAttach", 0);
                        jo.put("downloadAttach", 0);

                        String[] arr = editAry.split(",");
                        for(String s : arr){
                            if(s.equals("deleteAttach")){
                                jo.put("deleteAttach", 1);
                            }else if(s.equals("downloadAttach")){
                                jo.put("downloadAttach", 1);
                            }
                        }
                        return jo.toJSONString();
                    }else{
                        return FormPermission.PERMISSION_WRITE;
                    }

                }
            } else {
                boolean canRead = hasRights(readAry, profileMap,formParams);
                if (canRead) {
                    //附件的删除、下载权限
                    if(readAry.indexOf("deleteAttach") > -1 || readAry.indexOf("downloadAttach") > -1 || readAry.indexOf("noneAttach") > -1){
                        JSONObject jo = new JSONObject();
                        jo.put("default", FormPermission.PERMISSION_READONLY);
                        jo.put("deleteAttach", 0);
                        jo.put("downloadAttach", 0);

                        String[] arr = readAry.split(",");
                        for(String s : arr){
                            if(s.equals("deleteAttach")){
                                jo.put("deleteAttach", 1);
                            }else if(s.equals("downloadAttach")){
                                jo.put("downloadAttach", 1);
                            }
                        }
                        return jo.toJSONString();
                    }else{
                        return FormPermission.PERMISSION_READONLY;
                    }


                } else {
                    return FormPermission.PERMISSION_NONE;
                }
            }
        }
    }

    public boolean hasRights(String rightAry, Map<String, Set<String>> profileMap){
        return hasRights(rightAry,profileMap,null);
    }
    /**
     * 计算权限。
     *
     * @param rightAry
     * @param profileMap
     * @return
     */
    public boolean hasRights(String rightAry, Map<String, Set<String>> profileMap,FormParams formParams) {
        if (StringUtils.isEmpty(rightAry) || FormPermission.PERMISSION_CUSTOM.equals(rightAry)) {
            return false;
        }
        //所有人
        if (FormPermission.PERMISSION_EVERYONE.equals(rightAry)) {
            return true;
        }
        if(rightAry.indexOf(FormPermission.PERMISSION_EVERYONE) > -1){
            return true;
        }
        //无权限
        if (FormPermission.PERMISSION_NONE.equals(rightAry)) {
            return false;
        }
        JSONObject json = JSONObject.parseObject(rightAry);
        //计算流程相关权限
        if(hasBpmRights(json,formParams)){
            return true;
        }
        for (String key : profileMap.keySet()) {
            if (!json.containsKey(key)) {
                continue;
            }
            JSONObject setType = json.getJSONObject(key);
            String val = setType.getString("values");
            //配置为空跳过。
            if (StringUtils.isEmpty(val)) {
                continue;
            }
            Boolean include = setType.getBoolean("include");
            String[] aryVal = val.split(",");
            for (int j = 0; j < aryVal.length; j++) {
                String id = aryVal[j];
                Set<String> set = profileMap.get(key);
                if (BeanUtil.isNotEmpty(set)) {
                    if (include) {
                        if (set.contains(id)) {
                            return true;
                        }
                    } else {
                        if (!set.contains(id)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean hasBpmRights(JSONObject json,FormParams formParams){
        if(formParams!=null) {
            if (json.containsKey("selectNodes")) {
                JSONArray selectNodes = json.getJSONArray("selectNodes");
                String approveNodes=formParams.getApproveNodes();
                if(StringUtils.isNotEmpty(approveNodes)) {
                    String[] nodeAry=approveNodes.split(",");
                    for(String nodeId:nodeAry) {
                        if (selectNodes.indexOf(nodeId) != -1) {
                            return true;
                        }
                    }
                }
            }
            if (json.containsKey("startUser")) {
                Boolean startUser = json.getBoolean("startUser");
                if (startUser && ContextUtil.getCurrentUserId().equals(formParams.getStartUserId())) {
                    return true;
                }
            }
            //审批人
            if (json.containsKey("approver")) {
                Boolean approver = json.getBoolean("approver");
                String approveNodes=formParams.getApproveNodes();
                if(approver && StringUtils.isNotEmpty(approveNodes)){
                    return true;
                }
            }
            //抄送人
            if (json.containsKey("ccUser")) {
                Boolean ccUser = json.getBoolean("ccUser");
                String ccUserIds=formParams.getCcUserIds();
                if(StringUtils.isNotEmpty(ccUserIds)) {
                    String[] userIdAry = ccUserIds.split(",");
                    for (String userId : userIdAry) {
                        if (ContextUtil.getCurrentUserId().equals(userId)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    private JSONObject getEntPermission(FormBoEntity boEntity,FormPc formPc,String subAlias) {
        JSONObject json = new JSONObject();
        List<FormBoAttr> attrs = boEntity.getBoAttrList();
        for (FormBoAttr attr : attrs) {
            json.put(attr.getName(), FormPermission.PERMISSION_WRITE);
        }
        //子表按钮
        JSONObject tbRights = new JSONObject();

        String buttons =formPc.getTableButtonDef();
        if(StringUtils.isEmpty(buttons)){
            setDefaultSubButtons(tbRights);
        }else {
            JSONObject btns = JSONObject.parseObject(buttons);
            if (!btns.containsKey(subAlias)) {
                setDefaultSubButtons(tbRights);
            }
            subBtnInitPermission(tbRights, formPc, subAlias, false);
        }
        json.put("tablerights", tbRights);
        return json;
    }

    private void setDefaultSubButtons(JSONObject tbRights){
        tbRights.put("add", true);
        tbRights.put("remove", true);
        tbRights.put("removeExist", true);
        tbRights.put("up", true);
        tbRights.put("down", true);
        tbRights.put(FormPermission.PERMISSION_EDIT, true);
        tbRights.put("editExist", true);
    }

    /**
     * 列表权限转object 权限
     *
     * @param perJson       数据初始权限配置。
     * @return
     */
    public JSONObject getInitPermissionObject(JSONObject perJson) {
        JSONObject saveData = new JSONObject();
        JSONObject main = new JSONObject();
        JSONObject tabBtn = new JSONObject();
        JSONObject tabList = new JSONObject();
        JSONObject sub = new JSONObject();
        JSONArray subtableRights = new JSONArray();

        //主表字段
        JSONArray mainList = perJson.getJSONArray("main");
        if (BeanUtil.isNotEmpty(mainList) && mainList.size() > 0) {
            for (int i = 0; i < mainList.size(); i++) {
                setAliasObj(mainList, i, main);
            }
        }
        saveData.put("main", main);

        //主表按钮
        JSONArray tabBtnList = perJson.getJSONArray("tabBtnList");
        for (int i = 0; BeanUtil.isNotEmpty(tabBtnList) && i < tabBtnList.size(); i++) {
            setAliasObj(tabBtnList, i, tabBtn);
        }
        saveData.put("tabBtn", tabBtn);

        //TAB
        JSONArray tabListAry = perJson.getJSONArray("tabList");
        for (int i = 0; BeanUtil.isNotEmpty(tabListAry) && i < tabListAry.size(); i++) {
            setAliasObj(tabListAry, i, tabList);
        }
        saveData.put("tabList", tabList);

        //子表
        JSONArray subList = perJson.getJSONArray("sub");
        for (int i = 0; BeanUtil.isNotEmpty(subList) && i < subList.size(); i++) {
            JSONObject subDataObj = subList.getJSONObject(i);
            String subAlias = subDataObj.getString("alias");

            JSONObject dataObj = new JSONObject();
            JSONObject subAttr = new JSONObject();
            JSONObject subTabBtn = new JSONObject();

            //子表字段
            JSONArray subMainList = subDataObj.getJSONArray("subAttrs");
            for (int k = 0; BeanUtil.isNotEmpty(subMainList) && k < subMainList.size(); k++) {
                setAliasObj(subMainList, k, subAttr);
            }
            dataObj.put("subAttr", subAttr);

            //子表按钮
            JSONArray subTabBtnList = subDataObj.getJSONArray("tabBtnList");
            for (int j = 0; BeanUtil.isNotEmpty(subTabBtnList) && j < subTabBtnList.size(); j++) {
                setAliasObj(subTabBtnList, j, subTabBtn);
            }
            dataObj.put("subTabBtn", subTabBtn);

            sub.put(subAlias, dataObj);
        }
        saveData.put("sub", sub);

        //子表权限
        subtableRights = perJson.getJSONArray("subtableRights");
        if (BeanUtil.isEmpty(subtableRights)) {
            subtableRights = new JSONArray();
        }
        saveData.put("subtableRights", subtableRights);
        return saveData;
    }

    private void setAliasObj(JSONArray jsonArray, int num, JSONObject returnObj) {
        JSONObject dataJson = jsonArray.getJSONObject(num);
        String alias = dataJson.getString("alias");
        returnObj.put(alias, dataJson);
    }

    public JSONObject getFormInitPermission(FormBoEntity boEntity, FormPc formPc, String permission, boolean readonly) {
        JSONObject permissionJson = new JSONObject();


        JSONObject oldMainJson = null;
        JSONObject oldTabBtnJson = null;
        JSONObject oldTabJson = null;
        JSONObject oldSubJson = null;
        //子表权限设置
        JSONArray subtableRights = null;

        if (StringUtils.isNotEmpty(permission)) {
            JSONObject oldPermissionJson = JSONObject.parseObject(permission);
            oldMainJson = oldPermissionJson.getJSONObject(FormPermission.PERMISSION_MAIN);
            oldTabBtnJson = oldPermissionJson.getJSONObject("tabBtn");
            oldTabJson = oldPermissionJson.getJSONObject("tabList");
            oldSubJson = oldPermissionJson.getJSONObject("sub");
            //子表权限设置
            subtableRights = oldPermissionJson.getJSONArray("subtableRights");
        }

        //主表字段权限列表
        JSONArray mainAttrsList = new JSONArray();
        List<FormBoAttr> mainAttrs = boEntity.getBoAttrList();
        for (FormBoAttr attr : mainAttrs) {
            String alias = attr.getName();
            if (BeanUtil.isEmpty(oldMainJson) || !oldMainJson.containsKey(alias)) {
                JSONObject attrJson = getAttrJson(attr, readonly);
                attrJson.put("dataType", attr.getControl());
                mainAttrsList.add(attrJson);
            } else {
                JSONObject jo = oldMainJson.getJSONObject(alias);
                jo.put("dataType", attr.getControl());
                mainAttrsList.add(jo);
            }
        }
        permissionJson.put(FormPermission.PERMISSION_MAIN, mainAttrsList);

        //主表按钮权限列表
        JSONArray tabBtnList = new JSONArray();
        String buttons = formPc.getButtonDef();
        if (StringUtils.isNotEmpty(buttons)) {
            JSONArray btns = JSONArray.parseArray(buttons);
            for (int i = 0; i < btns.size(); i++) {
                JSONObject btn = btns.getJSONObject(i);
                String alias = btn.getString(FormPermission.PERMISSION_ALIAS);
                if (BeanUtil.isEmpty(oldMainJson) || !oldTabBtnJson.containsKey(alias)) {
                    btn.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
                    btn.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
                    if (readonly) {
                        btn.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
                        btn.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
                    }
                    tabBtnList.add(btn);
                } else {
                    tabBtnList.add(oldTabBtnJson.getJSONObject(alias));
                }
            }
        }
        permissionJson.put("tabBtnList", tabBtnList);

        //主表TAB权限列表
        JSONArray tabList = new JSONArray();
        String tabs = formPc.getTabDef();
        if (StringUtils.isNotEmpty(tabs)) {
            JSONArray tabAry = JSONArray.parseArray(tabs);
            for (int i = 0; i < tabAry.size(); i++) {
                JSONObject tab = tabAry.getJSONObject(i);
                String name = tab.getString("name");
                if (BeanUtil.isEmpty(oldMainJson) || !oldTabJson.containsKey(name)) {
                    JSONArray children=tab.getJSONArray("children");
                    for(Object obj:children){
                        JSONObject subTab=(JSONObject)obj;
                        subTab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
                        subTab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
                        if (readonly) {
                            subTab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
                            subTab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
                        }
                    }
                    tab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
                    tab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
                    if (readonly) {
                        tab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
                        tab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
                    }
                    tabList.add(tab);
                } else {
                    JSONArray tabChild=tab.getJSONArray("children");
                    JSONArray oldTabChild=oldTabJson.getJSONObject(name).getJSONArray("children");
                    if(tabChild.size()!=oldTabChild.size()){
                        for(Object obj:tabChild){
                            JSONObject subTab=(JSONObject)obj;
                            for(Object oldObj:oldTabChild){
                                JSONObject oldSubTab=(JSONObject)oldObj;
                                if(subTab.getString("name").equals(oldSubTab.getString("name"))){
                                    subTab.put(FormPermission.PERMISSION_EDIT, oldSubTab.getString(FormPermission.PERMISSION_EDIT));
                                    subTab.put(FormPermission.PERMISSION_EDIT_NAME, oldSubTab.getString(FormPermission.PERMISSION_EDIT_NAME));
                                    break;
                                }else{
                                    subTab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
                                    subTab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
                                    if (readonly) {
                                        subTab.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
                                        subTab.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
                                    }
                                }
                            }
                        }
                        tabList.add(tab);
                    }else {
                        tabList.add(oldTabJson.getJSONObject(name));
                    }
                }
            }
        }
        permissionJson.put("tabList", tabList);

        //子表字段（按钮）权限列表
        JSONObject oldSubOneJson = null;
        JSONObject oldSubAttrJson = null;
        JSONObject oldSubTabBtnJson = null;
        JSONArray subTabAttrsList = new JSONArray();

        List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
        for (FormBoEntity subEnt : boEntityList) {
            String alias = subEnt.getAlias();
            String name = subEnt.getName();

            if (BeanUtil.isNotEmpty(oldSubJson)) {
                oldSubOneJson = oldSubJson.getJSONObject(alias);
                if (BeanUtil.isNotEmpty(oldSubOneJson)) {
                    oldSubAttrJson = oldSubOneJson.getJSONObject("subAttr");
                    oldSubTabBtnJson = oldSubOneJson.getJSONObject("subTabBtn");
                }
            }
            JSONObject subJson = getSubPermission(subEnt, oldSubAttrJson, readonly);
            subJson.put("name", name);
            subJson.put(FormPermission.PERMISSION_ALIAS, subEnt.getAlias());
            subJson.put("tabBtnList", getSubBtnPermission(subEnt.getAlias(), formPc, oldSubTabBtnJson, readonly));
            subTabAttrsList.add(subJson);
        }
        permissionJson.put("subtableRights", setSubtableRights(subtableRights, boEntityList));

        permissionJson.put("sub", subTabAttrsList);

        return permissionJson;
    }


    private JSONArray setSubtableRights(JSONArray oldSubtableRights, List<FormBoEntity> boEntityList) {
        JSONArray newSubtableRights = new JSONArray();
        for (FormBoEntity subEnt : boEntityList) {
            String alias = subEnt.getAlias();
            String name = subEnt.getName();
            if (BeanUtil.isEmpty(oldSubtableRights)) {
                newSubTableRight(newSubtableRights, alias, name);
                continue;
            }
            boolean isContainAlias = false;
            for (Object rigth : oldSubtableRights) {
                JSONObject rigthJson = (JSONObject) rigth;
                String oldAlias = rigthJson.getString("alias");
                if (alias.equals(oldAlias)) {
                    newSubtableRights.add(rigthJson);
                    isContainAlias = true;
                    break;
                }
            }
            if (!isContainAlias) {
                newSubTableRight(newSubtableRights, alias, name);
            }
        }
        return newSubtableRights;
    }

    private void newSubTableRight(JSONArray newSubtableRights, String alias, String name) {
        JSONObject right = new JSONObject();
        right.put("alias", alias);
        right.put("name", name);
        right.put("type", "all");
        right.put("setting", "");
        newSubtableRights.add(right);
    }

    private JSONObject getSubPermission(FormBoEntity boEntity, JSONObject oldSubAttrJson, boolean readonly) {
        JSONArray subAttrsList = new JSONArray();
        JSONObject json = new JSONObject();
        List<FormBoAttr> attrs = boEntity.getBoAttrList();
        for (FormBoAttr attr : attrs) {
            String alias = attr.getName();
            if (BeanUtil.isEmpty(oldSubAttrJson) || !oldSubAttrJson.containsKey(alias)) {
                JSONObject attrJson = getAttrJson(attr, readonly);
                subAttrsList.add(attrJson);
            } else {
                subAttrsList.add(oldSubAttrJson.getJSONObject(alias));
            }
        }
        json.put("subAttrs", subAttrsList);
        return json;
    }

    private JSONObject getAttrJson(FormBoAttr attr, boolean readonly) {
        JSONObject attrJson = new JSONObject();
        attrJson.put("name", attr.getComment());
        attrJson.put(FormPermission.PERMISSION_ALIAS, attr.getName());
        attrJson.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
        attrJson.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
        if (readonly) {
            attrJson.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
            attrJson.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
        }

        attrJson.put(FormPermission.PERMISSION_READ, FormPermission.PERMISSION_EVERYONE);
        attrJson.put(FormPermission.PERMISSION_READ_NAME, FormPermission.PERMISSION_EVERYONE_NAME);

        attrJson.put(FormPermission.PERMISSION_REQUIRE, FormPermission.PERMISSION_NONE);
        attrJson.put(FormPermission.PERMISSION_REQUIRE_NAME, FormPermission.PERMISSION_NONE_NAME);
        return attrJson;
    }

    private JSONArray getSubBtnPermission(String alias, FormPc formPc, JSONObject oldSubTabBtnJson, boolean readonly) {
        //子表按钮权限列表
        JSONArray tabBtnList = new JSONArray();
        String buttons = formPc.getTableButtonDef();
        if (StringUtils.isEmpty(buttons)) {
            return tabBtnList;
        } else {
            JSONObject btns = JSONObject.parseObject(buttons);
            if (!btns.containsKey(alias)) {
                return tabBtnList;
            }
            JSONArray btnsJsonArray = btns.getJSONArray(alias);
            for (int i = 0; i < btnsJsonArray.size(); i++) {
                JSONObject btn = btnsJsonArray.getJSONObject(i);
                String btnAlias = btn.getString(FormPermission.PERMISSION_ALIAS);
                if (BeanUtil.isEmpty(oldSubTabBtnJson) || !oldSubTabBtnJson.containsKey(btnAlias)) {
                    btn.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
                    btn.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_EVERYONE_NAME);
                    if (readonly) {
                        btn.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_NONE);
                        btn.put(FormPermission.PERMISSION_EDIT_NAME, FormPermission.PERMISSION_NONE_NAME);
                    }
                    tabBtnList.add(btn);
                    getEditOrRemoveBtn(tabBtnList, btn, null);
                } else {
                    tabBtnList.add(oldSubTabBtnJson.getJSONObject(btnAlias));
                    getEditOrRemoveBtn(tabBtnList, btn, oldSubTabBtnJson);
                }
            }
        }
        return tabBtnList;
    }

    private void getEditOrRemoveBtn(JSONArray tabBtnList, JSONObject btn, JSONObject oldSubTabBtnJson) {
        if (!FormPermission.PERMISSION_DEFAULT.equals(btn.getString(FormPermission.PERMISSION_TYPE))) {
            return;
        }
        if (!FormPermission.PERMISSION_EDIT.equals(btn.getString(FormPermission.PERMISSION_ALIAS)) &&
                !FormPermission.PERMISSION_REMOVE.equals(btn.getString(FormPermission.PERMISSION_ALIAS))) {
            return;
        }

        String btnAlias = btn.getString(FormPermission.PERMISSION_ALIAS) + "Exist";

        if (BeanUtil.isEmpty(oldSubTabBtnJson) || !oldSubTabBtnJson.containsKey(btnAlias)) {
            JSONObject btnExist = new JSONObject();
            btnExist.put("name", btn.getString("name") + "已添加");
            btnExist.put(FormPermission.PERMISSION_ALIAS, btnAlias);
            btnExist.put(FormPermission.PERMISSION_TYPE, btn.getString(FormPermission.PERMISSION_TYPE));
            btnExist.put(FormPermission.PERMISSION_EDIT, FormPermission.PERMISSION_EVERYONE);
            btnExist.put(FormPermission.PERMISSION_EDIT_NAME, "所有人");
            tabBtnList.add(btnExist);
        } else {
            tabBtnList.add(oldSubTabBtnJson.getJSONObject(btnAlias));
        }

    }
}
