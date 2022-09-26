package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysSeqId;
import com.redxun.system.core.mapper.SysSeqIdMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
* [系统流水号]业务服务类
*/
@Service
public class SysSeqIdServiceImpl extends SuperServiceImpl<SysSeqIdMapper, SysSeqId> implements BaseService<SysSeqId> {


    @Resource
    private SysSeqIdMapper sysSeqIdMapper;

    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Resource
    RedisTemplate redisTemplate;


    @Override
    public BaseDao<SysSeqId> getRepository() {
        return sysSeqIdMapper;
    }




    public SysSeqId getByAlias(String alias){
        QueryWrapper wrapper= new QueryWrapper<SysSeqId>();
        wrapper.eq("ALIAS_", alias);
        SysSeqId seqId=sysSeqIdMapper.selectOne(wrapper);
        return seqId;
    }

    public List getByAliasAndName(String alias,String name,String appId){
        QueryWrapper wrapper= new QueryWrapper<SysSeqId>();
        if(!"".equals(alias)){
            wrapper.eq("ALIAS_", alias);
        }
        if (!"".equals(name)){
            wrapper.like("NAME_", name);
        }
        if(StringUtils.isNotEmpty(appId)){
            wrapper.eq("APP_ID_",appId);
        }
        wrapper.eq("TENANT_ID_", ContextUtil.getCurrentTenantId());
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List list = sysSeqIdMapper.selectMaps(wrapper);
        return list;
    }

    @Override
    public List getAll(){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.select(new String[]{"NAME_","ALIAS_"});
        wrapper.orderByDesc("CREATE_TIME_");
        List list = sysSeqIdMapper.selectMaps(wrapper);
        return list;
    }



    /**
     * 根据规则返回需要显示的流水号。
     * @param rule			流水号规则。
     * @param length		流水号的长度。
     * @param curValue		流水号的当前值。
     * @return
     */
    public String getByRule(String rule,int length, Long curValue){
        Calendar now= Calendar.getInstance();
        NumberFormat nf=new DecimalFormat("00");
        int year=now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH)+1;
        int day=now.get(Calendar.DATE);

        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            sb.append("0");
        }
        SimpleDateFormat fullDateFormat=new SimpleDateFormat("yyyMMdd");
        SimpleDateFormat shortDateFormat=new SimpleDateFormat("yyyMM");

        NumberFormat seqFt=new DecimalFormat(sb.toString());

        String seqNo=seqFt.format(curValue);


        String rtn=rule.replace("{yyyy}", year+"")
                .replace("{yy}",String.valueOf(year).substring(2, 4))
                .replace("{MM}", nf.format(month))
                .replace("{mm}",month+"")
                .replace("{DD}", nf.format(day))
                .replace("{dd}", day+"")
                .replace("{NO}", seqNo)
                .replace("{no}", curValue+"")
                .replace("{yyyyMM}", shortDateFormat.format(now.getTime()))
                .replace("{yyyyMMdd}", fullDateFormat.format(now.getTime()));

        return rtn;
    }


    public boolean isExist(SysSeqId sysSeqId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",sysSeqId.getAlias());
        if(StringUtils.isNotEmpty(sysSeqId.getSeqId()) ){
            wrapper.ne("SEQ_ID_",sysSeqId.getSeqId());
        }
        int count=this.sysSeqIdMapper.selectCount(wrapper);
        return  count>0;

    }



    /**
     * 根据流程规则别名获取得下一个流水号。
     * @param alias		流水号规则别名。
     * @return
     */
    public String genSeqNo(String alias)  {
        SysSeqId identity=getByAlias(alias);
        String rule=identity.getRule();
        //生成类型
        String genType =identity.getGenType();
        //初始值
        Integer initVal=identity.getInitVal();
        Long curValue=1L;
        //每天产生
        if(SysSeqId.GEN_TYPE_DAY.equals(genType)){
            //getNo()
            String key=SysSeqId.SYS_SQL_ID +alias + DateUtils.dateTimeNow("YYMMDD");
            curValue= getNo(key,initVal,identity.getStep(),3600*30);
        }
        //每周产生
        else if(SysSeqId.GEN_TYPE_WEEK.equals(genType)){
            String key=SysSeqId.SYS_SQL_ID +alias + DateUtils.getCurWeekOfYear();;
            curValue= getNo(key,initVal,identity.getStep(),3600*24*8);
        }
        //每月产生
        else if(SysSeqId.GEN_TYPE_MONTH.equals(genType)){
            String key=SysSeqId.SYS_SQL_ID +alias + DateUtils.getCurMonth();;
            curValue= getNo(key,initVal,identity.getStep(),3600*24*32);
        }
        //每年产生
        else if(SysSeqId.GEN_TYPE_YEAR.equals(genType)){
            String key=SysSeqId.SYS_SQL_ID +alias + DateUtils.getCurYear();
            curValue= getNo(key,initVal,identity.getStep(),3600*24*366);
        }
        else {
            String key=SysSeqId.SYS_SQL_ID +SysSeqId.GEN_TYPE_AUTO+alias ;
            curValue= getNo(key,initVal,identity.getStep(),3600*24*365*20);
        }
        String no=getByRule(rule,identity.getLen(),curValue);
        return no;
    }

    /**
     * 使用redis lua 脚本产生流水号。
     * @param key           生成的KEY
     * @param initVal       初始值
     * @param step          步长
     * @param timeout       超时事件
     * @return
     */
    private Long getNo(String key,Integer initVal,Short step,Integer timeout){
        DefaultRedisScript<Long> redisScript= new DefaultRedisScript<>();

        //setex (key,timeout,val)
        String script="if(redis.call('exists',KEYS[1])==0) then  redis.call('setex',KEYS[1], tonumber(KEYS[4]), tonumber(KEYS[2]))  " +
                "else redis.call('incrby',KEYS[1],tonumber(KEYS[3])) end  return tonumber(redis.call('get',KEYS[1]))";

        redisScript.setScriptText(script);

        List list=new ArrayList<>();
        //键
        list.add(key);
        //初始值
        list.add(initVal.toString());
        //步长
        list.add(step.toString());
        //超时时间
        list.add(timeout.toString());

        redisScript.setResultType(Long.class);

        Long rtn=(Long) redisTemplate.execute(redisScript,list);

        return rtn;
    }



    public void importSysSeq(MultipartFile file, String treeId,String appId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入流水号:");

        JSONArray sysSeqArray  = sysInvokeScriptService.readZipFile(file);
        for (Object obj:sysSeqArray) {
            JSONObject seqObj = (JSONObject)obj;
            JSONObject sysSeq = seqObj.getJSONObject("sysSeqId");
            if(BeanUtil.isEmpty(sysSeq)){
                continue;
            }
            String sysSeqStr = sysSeq.toJSONString();
            SysSeqId sysSeqId = JSONObject.parseObject(sysSeqStr,SysSeqId.class);

            sb.append(sysSeqId.getName() +"("+sysSeqId.getSeqId() +"),");
            sysSeqId.setAppId(appId);
            String id = sysSeqId.getSeqId();
            SysSeqId oldSeq = get(id);
            if(BeanUtil.isNotEmpty(oldSeq)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldSeq.getAppId())){
                    update(sysSeqId);
                }else{
                    sysSeqId.setSeqId(IdGenerator.getIdStr());
                    insert(sysSeqId);
                }
            }
            else{
                insert(sysSeqId);
            }
        }
        sb.append(",导入到分类:" + treeId);

        LogContext.put(Audit.DETAIL,sb.toString());

    }



}
