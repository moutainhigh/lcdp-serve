package com.redxun.common.constant;

/**
 * Http请求方法常量
 *
 */
public enum HttpMethodConstants {


    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE")
    ;

    HttpMethodConstants(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private Integer code;

    private String value;

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据code获取去value
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code){
        for(HttpMethodConstants httpMethod:HttpMethodConstants.values()){
            if(code.equals(httpMethod.getCode())){
                return httpMethod.getValue();
            }
        }
        return  null;
    }
}
