package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface IParameterHandler {

    String handParameter(JSONObject json, HttpServletRequest request);
}
