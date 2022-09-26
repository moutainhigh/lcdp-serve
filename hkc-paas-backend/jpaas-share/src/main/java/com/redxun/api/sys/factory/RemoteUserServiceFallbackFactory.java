package com.redxun.api.sys.factory;//package com.redxun.system.feign.factory;
//
//import com.redxun.common.constant.SysConstant;
//import com.redxun.common.model.JPaasUser;
//import com.redxun.system.feign.RemoteUserService;
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
///**
// * userService降级工场
// *
// * @author yjy
// * @date 2019/11/02
// */
//@Slf4j
//@Component
//public class RemoteUserServiceFallbackFactory implements FallbackFactory<RemoteUserService> {
//    @Override
//    public RemoteUserService create(Throwable throwable) {
//        return new RemoteUserService() {
//            @Override
//            public JPaasUser selectByUsername(String username) {
//                log.error("通过用户名查询用户异常:{}", username, throwable);
//                return new JPaasUser();
//            }
//
//            @Override
//            public JPaasUser findByUsername(String username) {
//                log.error("通过用户名查询用户异常:{}", username, throwable);
//                return new JPaasUser();
//            }
//
//            @Override
//            public JPaasUser findByMobile(String mobile) {
//                log.error("通过手机号查询用户异常:{}", mobile, throwable);
//                return new JPaasUser();
//            }
//
//            @Override
//            public JPaasUser findByOpenId(String openId) {
//                log.error("通过openId查询用户异常:{}", openId, throwable);
//                return new JPaasUser();
//            }
//
//            @Override
//            public Map<String, Set<String>> getUrlGroupIdMap() {
//                log.error("查询数据异常", throwable);
//                return new HashMap<>(SysConstant.INIT_CAPACITY_16);
//            }
//        };
//    }
//}
