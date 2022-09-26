package com.redxun.gateway.filter;

import com.redxun.cache.CacheUtil;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.redis.util.AppTokenUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.gateway.ApiLogOutput;
import com.redxun.gateway.SysAppAuthDto;
import com.redxun.gateway.SysAppLogDto;
import com.redxun.gateway.feign.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 从jpaas-share中迁移过来, 外部API调用的统一拦截,处理/restApi/*路径,
 * 该过滤器的作用是拦截应用端传来的token，或者传递的用户登录token，验证接口的访问有效性。
 * @Author: Elwin ZHANG
 * @Date: 2021/8/23 16:08
 **/
@Slf4j
@Component
public class ApiTokenFilter implements GlobalFilter, Ordered {
    private static final  String URL_PATTERN="/restapi/";
    //缓存外部API接口授权记录的Key前缀
    private static final  String APP_AUTH_KEY_PREFIX="sys_app_auth_";
    private  static final  String APP_AUTH_REGION="api_auth";
    private static final String START_TIME = "url_request_start_time";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String reqUrl = serverHttpRequest.getPath().value().toLowerCase();
        if(reqUrl.indexOf(URL_PATTERN)<0){
            return chain.filter(exchange);
        }
        String method=serverHttpRequest.getMethodValue();
        String token="";
        List<String> lstToken= serverHttpRequest.getHeaders().get("token");
        if(lstToken!=null  && lstToken.size()>0){
            token=lstToken.get(0);
        }
        String appId="local";
        ServerHttpResponse response=exchange.getResponse();
        //token存在的情况
        if(StringUtils.isNotEmpty(token)){
            boolean rtn= AppTokenUtil.validToken(token);
            if(!rtn){
               throw new IllegalStateException("{success:false,message:\"token已过期或非法\",data:-2}");
            }
            else{
                appId= AppTokenUtil.getAppId(token);
                if(!isContainAuthUrl(appId,reqUrl,method)){
                   throw new IllegalStateException("{success:false,message:\"此请求没有被授权\",data:-3}");
                }
            }
            exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
            String applicationName= SpringUtil.getApplicationContext().getEnvironment().getProperty("spring.application.name");
            String[] strArray=reqUrl.split("/");
            if(strArray!=null && strArray.length>2){
                applicationName="jpaas-" + strArray[2];
            }
            SysAppLogDto sysAppLogDto=new SysAppLogDto();
            sysAppLogDto.setAppId(appId);
            sysAppLogDto.setMethod(method);
            sysAppLogDto.setAppName(applicationName);
            sysAppLogDto.setUrl(serverHttpRequest.getURI().toString());
            return chain.filter(exchange).then( Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(START_TIME);
                if (startTime != null) {
                    long executeTime = (System.currentTimeMillis() - startTime);
                    sysAppLogDto.setDuration((int)executeTime);
                    ApiLogOutput apiLogOutput=SpringUtil.getBean(ApiLogOutput.class);
                    if(apiLogOutput!=null){
                        apiLogOutput.logOutput().send(MessageBuilder.withPayload(sysAppLogDto).build());
                    }
                }
            }));
        }
        //用户通过本地调用接口。
        else {
            List<String> auths= serverHttpRequest.getHeaders().get(SecurityConstants.Authorization);
            if(auths==null || auths.size()==0){
                throw new IllegalStateException("{success:false,message:\"没有传入TOKEN\",data:-1}");
            }
            String authorization = auths.get(0);
            String[] tokens =authorization.split("Bearer ");
            token=tokens[1];
            if(StringUtils.isEmpty(token)){
                throw new IllegalStateException("{success:false,message:\"没有传入TOKEN\",data:-1}");
            }else {
                Boolean rtn = SysUserUtil.validToken(token);
                if(!rtn){
                    throw new IllegalStateException("{success:false,message:\"token已过期或非法\",data:-2}");
                }
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 判断当前URL是否在授权的地址中
     * @param appId         应用ID
     * @param requestUrl    请求地址
     * @Param method        方法类型
     */
    private boolean isContainAuthUrl(String appId,String requestUrl,String method){
        List<SysAppAuthDto>  list = getSysAppAuth(appId);
      for(SysAppAuthDto sysAppAuthDto : list){
            Boolean containUrl=sysAppAuthDto.getUrl().toLowerCase().indexOf(requestUrl)!=-1;
            if(containUrl && method.equals(sysAppAuthDto.getMethod())){
                return true;
            }
        }
        return false;
    }

    /**
    * @Description: 获取sys_app_auth 表中的接口授权信息
    * @param appId 客户端ID
    * @Author: Elwin ZHANG  @Date: 2021/8/24 10:57
    **/
    private  List<SysAppAuthDto> getSysAppAuth(String appId){
        //先尝试从缓存中获取
        String key=APP_AUTH_KEY_PREFIX + appId;
        Object object=CacheUtil.get(APP_AUTH_REGION,key);
        if(object!=null){
            try{
                return (List<SysAppAuthDto>) object;
            }catch (Exception e){
                log.warn(e.getMessage(),e);
            }
        }

        SystemClient  systemClient=SpringUtil.getBean(SystemClient.class);

        //缓存中不存在，则调用Feign接口获取
        List<SysAppAuthDto>  list = systemClient.findListByAppId(appId);
        CacheUtil.set(APP_AUTH_REGION,key,list);
        return list;
    }

    @Override
    public int getOrder() {
        return 300;
    }
}
