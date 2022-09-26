package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.db.DbUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysTransferLog;
import com.redxun.system.core.entity.SysTransferSetting;
import com.redxun.system.core.mapper.SysTransferSettingMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [权限转移设置表]业务服务类
 */
@Service
public class SysTransferSettingServiceImpl extends SuperServiceImpl<SysTransferSettingMapper, SysTransferSetting> implements BaseService<SysTransferSetting> {

    @Resource
    private SysTransferSettingMapper sysTransferSettingMapper;
    @Resource
    private SysTransferLogServiceImpl sysTransferLogService;
    @Resource
    GroovyEngine groovyEngine;

    public static String IDS = "{ids}";
    public static String NAMES = "{names}";
    public static String TARGET_PERSONID = "{targetPersonId}";
    public static String TARGET_PERSONNAME = "{targetPersonName}";
    public static String TARGET_PERSONGROUPID = "{targetPersonGroupId}";
    public static String AUTHOR_ID = "{authorId}";
    public static String AUTHOR_NAME = "{authorName}";
    public static String AUTHOR_GROUP_ID = "{authorGroupId}";


    @Override
    public BaseDao<SysTransferSetting> getRepository() {
        return sysTransferSettingMapper;
    }

    /**
     * 获取有效的数据
     *
     * @return
     */
    public List<SysTransferSetting> getInvailAll() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("STATUS_", "YES");
        return sysTransferSettingMapper.selectList(queryWrapper);
    }

    public List<GridHeader> onRun(String selectSql) throws Exception {
        Map<String, Object> map = new HashMap<>(SysConstant.INIT_CAPACITY_16);
        //获取SQL
        IUser user=ContextUtil.getCurrentUser();
        String sql = (String) groovyEngine.executeScripts(selectSql
                .replace(AUTHOR_ID, user.getUserId())
                .replace(AUTHOR_NAME,user.getFullName())
                .replace(AUTHOR_GROUP_ID,StringUtils.join(user.getRoles(),",")), map);

        LogContext.put(Audit.DETAIL,sql);
        return DbUtil.getGridHeader(sql);
    }

    public List<Map<String, Object>> excuteSelectSql(SysTransferSetting sysTransDef, OsUserDto author) {
        JdbcTemplate jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
        Map<String, Object> map = new HashMap<>(SysConstant.INIT_CAPACITY_16);
        //获取SQL
        String sql = (String) groovyEngine.executeScripts(sysTransDef.getSelectSql()
                .replace(AUTHOR_ID, author.getUserId())
                .replace(AUTHOR_NAME,author.getFullName())
                .replace(AUTHOR_GROUP_ID,StringUtils.join(author.getRoles(),",")), map);
        if (StringUtils.isEmpty(sql)) {
            return new ArrayList<>();
        }
        String detail="执行:" + sysTransDef.getName() +",源用户:" + author.getFullName()
                +"("+author.getUserId()+"),执行SQL:" + sql;
        LogContext.put(Audit.DETAIL,detail);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }

    public void excuteUpdateSql(SysTransferSetting sysTransDef, OsUserDto author, OsUserDto targetPerson, String selectedItem) {
        String ds = sysTransDef.getDsAlias();
        DataSourceContextHolder.setDataSource(ds);
        JdbcTemplate jdbcTemplate = SpringUtil.getBean(JdbcTemplate.class);
        JSONArray selectedItemJa = JSONArray.parseArray(selectedItem);

        // 替代sql
        String updateSql = parseContent(sysTransDef,selectedItemJa,author,targetPerson,sysTransDef.getUpdateSql());

        String detail="执行转移:" + sysTransDef.getName() +"("+sysTransDef.getId()+"),源用户:" +author.getFullName()
                +"("+author.getUserId()+")"
                +"目标用户:" + targetPerson.getFullName() +"("+targetPerson.getUserId()+"),执行SQL:"+updateSql;
        LogContext.put(Audit.DETAIL,detail);

        //获取SQL
        String sql = (String) groovyEngine.executeScripts(updateSql, new HashMap<>(SysConstant.INIT_CAPACITY_16));
        if (StringUtils.isNotEmpty(sql)) {
            jdbcTemplate.execute(sql);
            //恢复默认数据源
            DataSourceContextHolder.setDefaultDataSource();
            String authorId = author.getUserId();
            String targetPersonId = targetPerson.getUserId();
            String content = getLogContent(sysTransDef, author, targetPerson, selectedItemJa);
            SysTransferLog entity = new SysTransferLog();
            entity.setAuthorPerson(authorId);
            entity.setOpDescp(content);
            entity.setTargetPerson(targetPersonId);
            sysTransferLogService.insert(entity);
        }
    }

    public String getLogContent(SysTransferSetting sysTransDef, OsUserDto author,
                                OsUserDto target, JSONArray selectedItemJa) {
        // 开始写日志
        String content = sysTransDef.getLogTemplet();
        return parseContent(sysTransDef,selectedItemJa,author,target,content);
    }

    private String parseContent(SysTransferSetting sysTransferSetting,JSONArray selectedItemJa,OsUserDto author,
                                OsUserDto target,String content){
        StringBuffer ids = new StringBuffer();
        StringBuffer names = new StringBuffer();
        for (int i = 0; i < selectedItemJa.size(); i++) {
            if (ids.length() != 0) {
                ids.append(",");
                names.append(",");
            }
            JSONObject jo = selectedItemJa.getJSONObject(i);
            ids.append(jo.getString(sysTransferSetting.getIdField()));
            names.append(jo.getString(sysTransferSetting.getNameField()));
        }

        content = content
                .replace(AUTHOR_ID, author.getUserId())
                .replace(AUTHOR_NAME, author.getFullName())
                .replace(AUTHOR_GROUP_ID,StringUtils.join(author.getRoles(),","))
                .replace(TARGET_PERSONID, target.getUserId())
                .replace(TARGET_PERSONNAME, target.getFullName())
                .replace(TARGET_PERSONGROUPID,StringUtils.join(target.getRoles(),","))
                .replace(IDS, ids.toString())
                .replace(NAMES, names.toString());
        return content;
    }
}
