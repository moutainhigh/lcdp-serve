package com.redxun.util.feishu.constants;

/**
 * 飞书token常量
 */
public class Keys {
    private static final String appTicketKeyPrefix = "feishu_app_ticket";
    private static final String appAccessTokenKeyPrefix = "feishu_app_access_token";
    private static final String tenantAccessTokenKeyPrefix = "feishu_tenant_access_token";

    public static String appTicketKey(String appID) {
        return appTicketKeyPrefix + "-" + appID;
    }

    public static String appAccessTokenKey(String appID) {
        return appAccessTokenKeyPrefix + "-" + appID;
    }

    public static String tenantAccessTokenKey(String appID, String tenantKey) {
        return tenantAccessTokenKeyPrefix + "-" + appID + "-" + tenantKey;
    }
}
