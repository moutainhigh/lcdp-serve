package com.redxun.system.script;

import com.redxun.cache.CacheUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.redis.util.AppTokenUtil;
import com.redxun.common.script.GroovyScript;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysAuthManager;
import com.redxun.system.core.service.SysAuthManagerService;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.util.wechat.WxEntUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接口脚本方法定义
 * @author hujun
 */
@Component
public class InterfaceScript implements GroovyScript {
    @Autowired
    WxEntUtil wxEntUtil;
    @Resource
    SysAuthManagerService sysAuthManagerService;
    @Resource
    RedisRepository redisRepository;
    @Resource
    SysInterfaceApiServiceImpl sysInterfaceApiService;

    private final static String APP_PRE="app_";

    /**
     * 调用第三方接口
     * @param apiId
     * @param params
     * @return
     */
    public JsonResult executeApi(String apiId,String params) throws Exception{
        return sysInterfaceApiService.executeApi(apiId,params, IdGenerator.getIdStr());
    }

    /**
     * 获取微信Token
     * @param corpId
     * @param secret
     * @param tenantId
     * @return
     */
    public String getWxToken(String corpId,String secret,String tenantId){
        return wxEntUtil.getToken(corpId,secret,tenantId);
    }

    /**
     * 获取应用token
     * @param clientId
     * @param clientSecret
     * @return
     */
    public String getAppToken(String clientId,String clientSecret){
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret) ){
            return "";
        }
        //获取客户端授权对象。
        SysAuthManager sysAuthManager=getByClientId(clientId);
        //验证授权对象是否合法
        JsonResult result=validClient(sysAuthManager,clientSecret);
        if(!result.isSuccess()){
            return "";
        }
        String appKey=getAppKey(clientId);
        String token= (String) redisRepository.get(appKey);
        //直接从缓存获取。
        if(StringUtils.isNotEmpty(token)){
            return token;
        }
        //生成token
        token = AppTokenUtil.genSecret();
        String key= AppTokenUtil.getTokenKey(token);
        //添加缓存 ,token -> clientId
        redisRepository.setExpire(key,sysAuthManager.getId(),sysAuthManager.getExpire());
        //添加缓存 ,clientId -> token
        redisRepository.setExpire(appKey,token,sysAuthManager.getExpire());
        return token;
    }

    private String getAppKey(String clientId){
        return APP_PRE +clientId;
    }

    private SysAuthManager getByClientId(String clientId){
        String key=sysAuthManagerService.getCacheKey(clientId);

        SysAuthManager sysAuthManager= (SysAuthManager) CacheUtil.get(SysAuthManagerService. REGIN_API,key);
        if(sysAuthManager!=null){
            return sysAuthManager;
        }

        sysAuthManager=sysAuthManagerService.get(clientId);
        if(sysAuthManager!=null){
            CacheUtil.set(SysAuthManagerService.REGIN_API,key,sysAuthManager);
        }
        return sysAuthManager;
    }
    private JsonResult validClient(SysAuthManager sysAuthManager,String secret){
        if(sysAuthManager==null ){
            return  new JsonResult<>(false,"clientId传输错误!");
        }

        String enable= sysAuthManager.getEnable();
        if(MBoolean.FALSE_LOWER.val.equals(enable)){
            return  new JsonResult<>(false,"此应用不参与授权!");
        }

        if(!sysAuthManager.getSecret().equals(secret)){
            return  new JsonResult<>(false,"clientSecret传输错误!");
        }
        return JsonResult.Success();
    }

}
