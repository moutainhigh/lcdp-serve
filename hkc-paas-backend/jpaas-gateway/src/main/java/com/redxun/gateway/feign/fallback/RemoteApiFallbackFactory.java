//package com.redxun.gateway.feign.fallback;
//
//import com.redxun.common.dto.AuthDto;
//import com.redxun.gateway.feign.RemoteApiService;
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Slf4j
//@Component
//public class RemoteApiFallbackFactory implements FallbackFactory<RemoteApiService>
//{
//    @Override
//    public RemoteApiService create(Throwable throwable)
//    {
//        log.error(throwable.getMessage());
//        return new RemoteApiService()
//        {
//            @Override
//            public List<AuthDto> selectApisByGroupIds(String groupIds)
//            {
//                log.error("查询系统正常显示菜单(含按钮):{}", throwable);
//                return null;
//            }
//
//            @Override
//            public Map<String, Set<String>> getUrlGroupIdMap() {
//                log.error("查询数据错误", throwable);
//                return null;
//            }
//        };
//    }
//}
