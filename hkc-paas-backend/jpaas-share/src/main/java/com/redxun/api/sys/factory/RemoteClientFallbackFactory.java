package com.redxun.api.sys.factory;//package com.redxun.system.feign.factory;
//
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class RemoteClientFallbackFactory implements FallbackFactory<RemoteClientService>
//{
//    @Override
//    public RemoteClientService create(Throwable throwable)
//    {
//        log.error(throwable.getMessage());
//        return new RemoteClientService()
//        {
//
//
//
//            @Override
//            public boolean getAllClients()
//            {
//                log.error("通过菜单ID集合与菜单类型查询菜单异常:{}", throwable);
//                return false;
//            }
//
//
//        };
//    }
//}
