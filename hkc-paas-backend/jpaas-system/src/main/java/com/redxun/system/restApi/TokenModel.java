package com.redxun.system.restApi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenModel {

    public TokenModel(){}

    public TokenModel(String token, int expire) {
        this.token = token;
        this.expire = expire;
    }

    String token;
    int expire=0;
}
