package com.redxun.web.controller;

import com.alibaba.fastjson.JSONObject;

public interface IExport {

    /**
     * 导出接口。
     * @param id
     * @param sb 用于构造日志输出
     * @return
     */
    JSONObject doExportById(String id,StringBuilder sb);
}
