package com.redxun.gateway.exception;

import javax.naming.AuthenticationException;

public class AuthException extends AuthenticationException {

    public AuthException(String msg){
        super(msg);
    }
}
