package com.redxun.system.restApi;

import com.redxun.cache.CacheUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.redis.util.AppTokenUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysAuthManager;
import com.redxun.system.core.service.SysAuthManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * token相关API
 */
@Slf4j
@RestController
@RequestMapping("/system/token")
public class TokenController {
    @Resource
    SysAuthManagerService sysAuthManagerService;
    @Resource
    RedisRepository redisRepository;






    private final static String APP_PRE="app_";



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

    private String getAppKey(String clientId){
        return APP_PRE +clientId;
    }


    @GetMapping("/genToken")
    public JsonResult<TokenModel> genToken(@RequestParam(value="clientId",required = false) String clientId,
                                       @RequestParam(value="clientSecret",required = false) String clientSecret){
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret) ){
            return new JsonResult<>(false,"应用ID或密钥不能为空");
        }


        //获取客户端授权对象。
        SysAuthManager sysAuthManager=getByClientId(clientId);
        //验证授权对象是否合法
        JsonResult result=validClient(sysAuthManager,clientSecret);
        if(!result.isSuccess()){
            return result;
        }

        String appKey=getAppKey(clientId);
        String token= (String) redisRepository.get(appKey);
        //直接从缓存获取。
        if(StringUtils.isNotEmpty(token)){
            TokenModel model=new TokenModel(token,sysAuthManager.getExpire());
            result.setData(model);
            return result;
        }

        //生成token
        token = AppTokenUtil.genSecret();
        TokenModel model=new TokenModel(token,sysAuthManager.getExpire());
        result.setData(model);


        String key= AppTokenUtil.getTokenKey(token);
        //添加缓存 ,token -> clientId
        redisRepository.setExpire(key,sysAuthManager.getId(),sysAuthManager.getExpire());
        //添加缓存 ,clientId -> token
        redisRepository.setExpire(appKey,token,sysAuthManager.getExpire());

        return result;
    }
}
