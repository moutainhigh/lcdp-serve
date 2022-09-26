package com.redxun.form.core.datahandler.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.base.entity.KeyValEnt;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ProcessHandleHelper;
import com.redxun.common.utils.SysBoEntParam;
import com.redxun.constvar.ConstVarContext;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.datahandler.ISqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultSqlBuilder implements ISqlBuilder {

    @Autowired
    IOrgService orgService;

    @Resource
    ConstVarContext contextHandlerFactory;

    /**
     * 子表权限
     * [
     *  {alias:"sub_01",name:"子表名称01",type:"all",setting:""},
     *  {alias:"sub_02",name:"子表名称02",type:"user",setting:""}
     *  {alias:"sub_03",name:"子表名称03",type:"group",setting:""}
     *   {alias:"sub_04",name:"子表名称04",type:"sql",setting:"{text:"已配置",value:"select * FROM OS_USER"}"}
     * ]
     */

    @Override
    public SqlModel getByFk(FormBoEntity boEnt, String fk) {
        boolean external = !boEnt.external();

        String fkField = external ? FormBoEntity.FIELD_FK : boEnt.getBoRelation().getFkField();
        String sql = "select * from " + boEnt.getTableName() + " where " + fkField + "=#{w.fk}";


        SqlModel sqlModel = new SqlModel(sql);
        sqlModel.addParam("fk", fk);
//		//构建子表SQL。
        bulidCondition(sqlModel, boEnt.getAlias());

        return sqlModel;
    }

    private void bulidCondition(SqlModel sqlModel, String tableName) {
        JSONObject rightJson = getRightJson(tableName);
        if(BeanUtil.isEmpty(rightJson)){
            return;
        }

        String type = rightJson.getString("type");

        if ("user".equals(type)) {
            handUser(sqlModel);
        } else if ("group".equals(type)) {
            handGroup(sqlModel);
        } else if ("sql".equals(type)) {
            String setting = rightJson.getString("setting");
            if(StringUtils.isEmpty(setting)){
                return;
            }
            JSONObject settingJson = JSONObject.parseObject(setting);
            String tmpSql = settingJson.getString("value");
            if(StringUtils.isEmpty(tmpSql)){
                return;
            }
            handSql(sqlModel, tmpSql);
        }
    }

    private JSONObject getRightJson(String tableName){
        JSONObject rightJson = null;
        Object config = ProcessHandleHelper.getObjectLocal();
        if (BeanUtil.isEmpty(config)) {
            return  rightJson;
        }
        JSONArray subtableRights = (JSONArray)config;
        if(BeanUtil.isEmpty(subtableRights)){
            return  rightJson;
        }
        for (Object rigth:subtableRights) {
            rightJson = (JSONObject)rigth;
            if(tableName.equals(rightJson.getString("alias"))){
                break;
            }
        }
        return  rightJson;
    }

    private void handUser(SqlModel sqlModel) {
        String userId = ContextUtil.getCurrentUserId();
        String sql = sqlModel.getSql() + " and " + SysBoEntParam.FIELD_CREATE_BY + "=#{w." + SysBoEntParam.FIELD_CREATE_BY + "}";

        sqlModel.addParam(SysBoEntParam.FIELD_CREATE_BY, userId);
        sql += " order by " + SysBoEntParam.FIELD_CREATE_TIME + " desc ";

        sqlModel.setSql(sql);
    }

    private void handGroup(SqlModel sqlModel) {
        String userId = ContextUtil.getCurrentUserId();
        //主部门
        OsGroupDto group = orgService.getMainDeps(userId, "webApp");
        if (group == null) {
            return;
        }
        String sql = sqlModel.getSql() + " and " + SysBoEntParam.FIELD_GROUP + "=#{" + SysBoEntParam.FIELD_GROUP + "}";
        sqlModel.addParam(SysBoEntParam.FIELD_GROUP, group.getGroupId());
        sql += " order by " + SysBoEntParam.FIELD_CREATE_TIME + " desc ";
        sqlModel.setSql(sql);
    }

    private void handSql(SqlModel sqlModel, String sql) {
        List<KeyValEnt> list = contextHandlerFactory.getHandlers();

        Map<String, Object> vars = new HashMap<String, Object>();

        for (KeyValEnt ent : list) {
            String key = ent.getKey().replace("[", "").replace("]", "");
            if (sql.indexOf(ent.getKey()) == -1){
                continue;
            }
            sql = sql.replace(ent.getKey(), "#{" + key + "}");
            Object val = contextHandlerFactory.getValByKey(ent.getKey(), vars);
            sqlModel.addParam(key, val);
        }
        sqlModel.setSql(sql);
    }
}
