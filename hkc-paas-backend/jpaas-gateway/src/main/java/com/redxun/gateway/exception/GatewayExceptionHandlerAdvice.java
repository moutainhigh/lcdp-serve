package com.redxun.gateway.exception;

import com.redxun.common.base.entity.JsonResult;
import io.netty.channel.ConnectTimeoutException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.SocketException;
import java.security.SignatureException;

/**
 * 网关异常处理器切面
 * @author csx
 */
@Component
@Slf4j
public class GatewayExceptionHandlerAdvice {
    /**
     * 统一 异常
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public JsonResult handle(Throwable throwable) {
        log.error(throwable.getMessage());
        //统一异常的处理入口
        if (throwable instanceof SignatureException) {
            return signHandle((SignatureException) throwable);
        } else if(throwable instanceof InvalidTokenException){//令牌过期了
            return invalidTokenHandle((InvalidTokenException)throwable);
        } else if (throwable instanceof NotFoundException) { // 找不到服务
            return notFoundHandle((NotFoundException) throwable);
        } else if (throwable instanceof ResponseStatusException) { // 响应错误
            return handle((ResponseStatusException) throwable);
        }  else if (throwable instanceof ConnectTimeoutException) {
            return timeoutHandle((ConnectTimeoutException) throwable);
        } else if( throwable instanceof SocketException) { // 服务不可用
            return serviceUnavailableHandle((SocketException) throwable);
        }

        JsonResult result = JsonResult.getFailResult(throwable.getMessage());
        result.setDetailMsg(throwable.getMessage());
        return result;
    }


    /**
     * 503 服务不可用 异常
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {SocketException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public JsonResult serviceUnavailableHandle(SocketException ex) {
        log.error("SERVICE_UNAVAILABLE:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.SERVICE_UNAVAILABLE.value(),"服务不可用503",ex.getMessage());
    }

    /**
     * 401 令牌过期 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {InvalidTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonResult invalidTokenHandle(InvalidTokenException ex) {
        log.error("InvalidTokenException:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.UNAUTHORIZED.value(),"令牌过期！",ex.getMessage());
    }

    /**
     * 401 校验 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public JsonResult signHandle(SignatureException ex) {
        log.error("SignatureException:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.UNAUTHORIZED.value(),"校验失败！",ex.getMessage());
    }

    /**
     * 404 服务未找到 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResult notFoundHandle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.NOT_FOUND.value(),"找不到该服务！",ex.getMessage());
    }

    /**
     * 500   其他服务 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ResponseStatusException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult handle(ResponseStatusException ex) {
        log.error("ResponseStatusException:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.INTERNAL_SERVER_ERROR.value(),"内部错误！",ex.getMessage());
    }

//    /**
//     * 502 错误网关 异常
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {GateWayException.class})
//    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public JsonResult badGatewayHandle(GateWayException ex) {
//        log.error("badGateway exception:{}", ex.getMessage());
//        return JsonResult.Fail(HttpStatus.BAD_GATEWAY.value());
//    }


    /**
     * 504 网关超时 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ConnectTimeoutException.class})
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public JsonResult timeoutHandle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return JsonResult.Fail(HttpStatus.GATEWAY_TIMEOUT.value(),"服务访问超时！",ex.getMessage());
    }
}
