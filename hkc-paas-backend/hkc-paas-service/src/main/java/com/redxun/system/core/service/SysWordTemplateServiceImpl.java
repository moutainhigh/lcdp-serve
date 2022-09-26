
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.dto.form.BoRelation;
import com.redxun.dto.form.FormBoAttrDto;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.system.core.entity.SysWordTemplate;
import com.redxun.system.core.mapper.SysWordTemplateMapper;
import com.redxun.system.feign.FormBoEntityClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* [文档模板编辑]业务服务类
*/
@Service
public class SysWordTemplateServiceImpl extends SuperServiceImpl<SysWordTemplateMapper, SysWordTemplate> implements BaseService<SysWordTemplate> {

    @Resource
    private SysWordTemplateMapper sysWordTemplateMapper;
    @Resource
    private FormBoEntityClient formBoEntityClient;
    @Resource
    private CommonDao commonDao;

    @Override
    public BaseDao<SysWordTemplate> getRepository() {
        return sysWordTemplateMapper;
    }

    public JSONArray getMetaData(String pkId) throws Exception{
        SysWordTemplate sysWordTemplate=get(pkId);
        String sqlType = sysWordTemplate.getType();
        if (sqlType.equals("sql")) {
            return getBySql( sysWordTemplate);
        }
        else {
            return getBySqlBoDefId(sysWordTemplate.getBoDefId());
        }
    }

    /**
     * {
     * 	main：sql
     *  sub:[{name:"",type:"",sql:""}]
     * }
     * @author mical 2018年5月23日
     * describe：
     * @param template
     * @return
     * @throws Exception
     */
    private JSONArray getBySql(SysWordTemplate template) throws Exception{
        String setting=template.getSetting();
        String dsAlias=template.getDsAlias();
        if(StringUtils.isNotEmpty(dsAlias)){
            DataSourceContextHolder.setDataSource(dsAlias);
        }
        JSONArray jsonAray=new JSONArray();
        JSONObject json=JSONObject.parseObject(setting);
        //main
        String sql=json.getString("main");
        sql = sql.replace("${pk}", "0");
        List<GridHeader> list = DbUtil.getGridHeader(sql);
        JSONArray jsonAry=getJsonAray(list,"","main");
        jsonAray.addAll(jsonAry);

        //sub
        JSONArray subAry=json.getJSONArray("sub");
        if(subAry.size()>0){
            for(int i=0;i<subAry.size();i++){
                JSONObject subTb=subAry.getJSONObject(i);
                String name=subTb.getString("name");
                String type=subTb.getString("type");
                String subSql=subTb.getString("sql");
                subSql = subSql.replace("${pk}", "0");

                JSONObject jsonTb=new JSONObject();
                jsonTb.put("key",name);
                jsonTb.put("type",type);
                String typeName=getRelationType(type);
                jsonTb.put("title", name + typeName);
                jsonTb.put("isField", false);

                List<GridHeader> sublist = DbUtil.getGridHeader(subSql);
                JSONArray subTableAry=getJsonAray(sublist,name,type);
                jsonTb.put("children", subTableAry);
                jsonAray.add(jsonTb);
            }
        }
        return jsonAray;
    }

    private JSONArray getBySqlBoDefId(String boDefId) throws Exception{
        JSONArray jsonAray=new JSONArray();
        FormBoEntityDto sysBoEnt = formBoEntityClient.getBoIdFields(boDefId);

        JSONArray jsonAry = getBoJsonAray(sysBoEnt.getBoAttrs(),"","main");
        jsonAray.addAll(jsonAry);


        List<FormBoEntityDto> boEntList = sysBoEnt.getSubBoList(); //子表集合
        if (boEntList.size() > 0) {
            for (FormBoEntityDto sub : boEntList) {
                String subName = sub.getAlias();
                JSONObject jsonTb=new JSONObject();
                jsonTb.put("key", subName);
                String type=getRelationType(sub.getRelationType());
                jsonTb.put("title", sub.getName()+type);
                jsonTb.put("type", sub.getRelationType());
                jsonTb.put("isField", false);

                JSONArray subAry=getBoJsonAray(sub.getBoAttrs(),subName,sub.getRelationType());
                jsonTb.put("children", subAry);
                jsonAray.add(jsonTb);
            }
        }


        return jsonAray;
    }

    private String getRelationType(String type){
        if(type.equals("onetoone")){
            return "[一对一]";
        }
        if(type.equals("onetomany")){
            return "[一对多]";
        }
        return "";
    }

    /**
     * @author mical 2018年5月18日
     * describe：
     * @return
     */
    private JSONArray getJsonAray(List<GridHeader> list,String tableName,String type){
        JSONArray jsonArray = new JSONArray();
        for (GridHeader header:list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", header.getFieldName());
            jsonObject.put("title", header.getFieldLabel());
            jsonObject.put("dataType", header.getDataType());
            jsonObject.put("tableName", tableName);
            jsonObject.put("isField", true);
            jsonObject.put("type", type);
            jsonObject.put("dataSet","none");
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private JSONArray getBoJsonAray(List<FormBoAttrDto> list, String tableName, String type){
        JSONArray jsonArray = new JSONArray();
        for (FormBoAttrDto attr:list) {
            if(attr.getIsSingle()!=1 && !"rx-address".equals(attr.getControl())){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", attr.getName()+"-value");
                jsonObject.put("title", attr.getComment());
                jsonObject.put("dataType", attr.getDataType());
                jsonObject.put("tableName", tableName);
                jsonObject.put("isField", true);
                jsonObject.put("type", type);
                jsonObject.put("dataSet","value");
                jsonArray.add(jsonObject);
                jsonObject = new JSONObject();
                jsonObject.put("key", attr.getName()+"-label");
                jsonObject.put("title", attr.getComment()+"(名称)");
                jsonObject.put("dataType", attr.getDataType());
                jsonObject.put("tableName", tableName);
                jsonObject.put("isField", true);
                jsonObject.put("type", type);
                jsonObject.put("dataSet","label");
                jsonArray.add(jsonObject);
            }else{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("key", attr.getName());
                jsonObject.put("title", attr.getComment());
                jsonObject.put("dataType", attr.getDataType());
                jsonObject.put("tableName", tableName);
                jsonObject.put("isField", true);
                jsonObject.put("type", type);
                jsonObject.put("dataSet","rx-address".equals(attr.getControl())?"address":"none");
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    public SysWordTemplate getByTemplateId(String templateId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TEMPLATE_ID_",templateId);
        return sysWordTemplateMapper.selectOne(queryWrapper);
    }

    public JSONObject getData(SysWordTemplate sysWordTemplate, String id){
        String type = sysWordTemplate.getType();
        String boDefId = sysWordTemplate.getBoDefId();

        //存在主表、子表的情况下，传入的是主表的pk，查出主表的数据，获取子表的主键，再通过子表的主键获取子表的数据
        if ("sql".equals(type)) {
            return  getDataBySql( sysWordTemplate, id);
        }
        else {
            return formBoEntityClient.getDataByBoDef(boDefId, id);
        }
    }

    private JSONObject getDataBySql(SysWordTemplate template ,String pk){
        String setting=template.getSetting();
        String dsAlias=template.getDsAlias();
        if(StringUtils.isNotEmpty(dsAlias)){
            DataSourceContextHolder.setDataSource(dsAlias);
        }
        JSONObject settingJson=JSONObject.parseObject(setting);
        JSONObject returnJson = new JSONObject();
        JSONObject subTableJson = new JSONObject();
        //main
        String mainSql=settingJson.getString("main");
        mainSql = mainSql.replace("${pk}", pk);
        SqlModel sqlModel=new SqlModel(mainSql);
        Object main = commonDao.queryForMap(sqlModel);
        returnJson.put("main", main);
        //sub
        JSONArray subAry=settingJson.getJSONArray("sub");

        if(BeanUtil.isNotEmpty(subAry)){
            for(int i=0;i<subAry.size();i++){
                JSONObject subJson=subAry.getJSONObject(i);
                String name=subJson.getString("name");
                String type=subJson.getString("type");
                String subSql=subJson.getString("sql");
                subSql = subSql.replace("${pk}", pk);
                if(BoRelation.RELATION_ONETOONE.equals(type)){
                    SqlModel subModel=new SqlModel(subSql);
                    Object subData = commonDao.queryForMap(subModel);
                    subTableJson.put(name, subData);
                }
                else{
                    List subData = commonDao.query(subSql);
                    subTableJson.put(name, subData);
                }
            }
            returnJson.put("sub", subTableJson);
        }
        for(String str:returnJson.keySet()){
            Map<String,Object> map = (Map)returnJson.get(str);
            for (String key : map.keySet()) {
                if(map.get(key) instanceof Timestamp){
                    try {
                        String res;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = ((Timestamp)map.get(key));
                        res = simpleDateFormat.format(date);
                        map.put(key,res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            returnJson.put(str,map);

        }
        return returnJson;
    }
}
