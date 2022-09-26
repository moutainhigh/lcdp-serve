package com.redxun.system.ext.handler;

/**
 * 接口返回数据处理器
 *  @author hujun
 */
public interface ApiReturnDataHandler {
    String getName();

    /**
     * 解析文本数据,返回json/xml字符串
     * @param data
     * @return
     */
    String analysisText(String data);
}
