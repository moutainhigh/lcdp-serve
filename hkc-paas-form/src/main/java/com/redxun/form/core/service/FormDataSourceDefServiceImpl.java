package com.redxun.form.core.service;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.SQLConst;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.common.CustomerClient;
import com.redxun.feign.org.OrgClient;
import com.redxun.form.core.entity.FormDataSourceDef;
import com.redxun.form.core.mapper.FormDataSourceDefMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
* [数据源定义管理]业务服务类
*/
@Service
public class FormDataSourceDefServiceImpl extends SuperServiceImpl<FormDataSourceDefMapper, FormDataSourceDef> implements BaseService<FormDataSourceDef> {

    @Resource
    private FormDataSourceDefMapper formDataSourceDefMapper;
    @Resource
    private CommonDao commonDao;
    @Resource
    private OrgClient orgClient;
    @Resource
    private CustomerClient customerClient;


    @Override
    public BaseDao<FormDataSourceDef> getRepository() {
        return formDataSourceDefMapper;
    }

    /**
     * 测试数据库连接
     * @param formDataSourceDef
     * @return
     */
    public JsonResult testConnect(FormDataSourceDef formDataSourceDef,boolean valid){
        String dbType=formDataSourceDef.getDbType();
        String json=formDataSourceDef.getSetting();
        JSONArray jsonArray=JSONArray.parseArray(json);
        String url =getValByName(jsonArray,"url");
        if(SQLConst.DB_MYSQL.equals(dbType)){
            String pattern="jdbc:mysql://(localhost|\\w+.\\w+.\\w+.\\w+):(\\d{3,5})/?.*\\?.+";
            boolean isMatch= Pattern.matches(pattern,url);
            if(!isMatch){
                return JsonResult.getFailResult("连接字符串不合法!");
            }
        }
        String username=getValByName(jsonArray,"username");
        String password=getValByName(jsonArray,"password");
        boolean encrypt=MBoolean.TRUE_LOWER.val.equals(formDataSourceDef.getEncrypt());
        if(encrypt) {
            try {
                password = ConfigTools.decrypt(password);
            } catch (Exception e) {
            }
        }
        //1.校验数据源配置是否正确
        JsonResult result=DataSourceUtil.validConn(dbType,url,username,password);
        if(!result.isSuccess()){
            return result;
        }
        if(valid) {
            //2.校验数据源是否在微服务启用
            String appName = formDataSourceDef.getAppName();
            if (StringUtils.isNotEmpty(appName)) {
                JSONObject params = new JSONObject();
                String[] nameAry = appName.split(",");
                List<String> errorList=new ArrayList<>();
                for (String name : nameAry) {
                    params.put("alias", formDataSourceDef.getAlias());
                    Object obj = customerClient.executeGetApi(name, "global/getDataSourcesByAlias", params);
                    if (obj == null) {
                        errorList.add(name);
                    }
                }
                if(!errorList.isEmpty()){
                    return JsonResult.Fail("微服务【" + StringUtils.join(errorList,",") + "】没有启用此数据源!");
                }
            }
        }
        return result;
    }

    private String getValByName(JSONArray jsonArray,String inName){
        for(int i=0;i<jsonArray.size();i++){
            JSONObject obj=jsonArray.getJSONObject(i);
            String name=obj.getString("name");
            if(inName.equals(name)){
                return obj.getString("val");
            }
        }
        return "";
    }

    public boolean isExist(FormDataSourceDef sourceDef){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",sourceDef.getAlias());
        if(StringUtils.isNotEmpty(sourceDef.getId())){
            wrapper.ne("ID_",sourceDef.getId());
        }
        int count=formDataSourceDefMapper.selectCount(wrapper);
        return  count>0;
    }

    /*
     * 判断是否存在指定别名的数据源
     * Elwin ZHANG 2021-4-7
     * @param alias 数据源别名
     */
    public boolean isExistByAlias(String alias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",alias);
        int count=formDataSourceDefMapper.selectCount(wrapper);
        return  count>0;
    }

    /**
     * 获取租户使用的数据源
     * @return
     */
    public List<FormDataSourceDef> getInstDatasource() {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ENABLE_","true");
        wrapper.eq("IS_TENANT_","1");
        return formDataSourceDefMapper.selectList(wrapper);
    }
}
