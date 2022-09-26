//package com.redxun.sentinel.config;
//
//import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
//import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.redxun.common.base.entity.JsonResult;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Sentinel配置类
// *
// * @author yjy
// * @date 2019/1/22
// */
//public class SentinelAutoConfigure {
//    public SentinelAutoConfigure() {
//        WebCallbackManager.setUrlBlockHandler(new CustomUrlBlockHandler());
//    }
//
//    /**
//     * 限流、熔断统一处理类
//     */
//    public class CustomUrlBlockHandler implements UrlBlockHandler {
//        @Override
//        public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
//            JsonResult result = JsonResult.getFailResult("flow-limiting");
//            httpServletResponse.getWriter().print(JSONObject.toJSONString(result));
//        }
//    }
//}
