package com.redxun.util.feishu.entity;

import lombok.Data;

/**
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuResp<T> {
    /**
     * 返回码，0表示请求成功，其他表示请求失败
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回业务数据
     */
    private T data;
}
