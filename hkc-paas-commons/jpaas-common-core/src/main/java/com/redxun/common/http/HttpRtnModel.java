package com.redxun.common.http;

import com.redxun.common.base.entity.CookieModel;
import com.redxun.common.base.entity.KeyValEnt;

import java.util.ArrayList;
import java.util.List;

/**
 * http 返回对象。
 *
 * @author ray
 */
public class HttpRtnModel {

    private List<KeyValEnt> ents = new ArrayList<KeyValEnt>();
    /**
     * 返回的内容
     */
    private String content = "";
    /**
     * 返回的状态编码
     */
    private int statusCode = 200;

    /**
     * 添加头
     *
     * @param key
     * @param val
     */
    public void addHeader(String key, String val) {
        KeyValEnt ent = new KeyValEnt(key, val);
        ents.add(ent);
    }

    public List<KeyValEnt> getHeader(String key) {
        List<KeyValEnt> rtnList = new ArrayList<KeyValEnt>();
        for (KeyValEnt ent : ents) {
            if (ent.getKey().equals(key)) {
                rtnList.add(ent);
            }
        }
        return rtnList;
    }


    public List<KeyValEnt> getEnts() {
        return ents;
    }

    public void setEnts(List<KeyValEnt> ents) {
        this.ents = ents;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<CookieModel> getCookies() {
        List<CookieModel> models = new ArrayList<CookieModel>();
        List<KeyValEnt> ents = getHeader("Set-Cookie");

        for (KeyValEnt ent : ents) {
            String val = ent.getVal().toString();
            CookieModel model = getCookie(val);
            models.add(model);
        }

        return models;
    }

    private CookieModel getCookie(String str) {
        String[] aryTmp = str.split(";");
        String tmp = aryTmp[0];
        String[] aryCookie = tmp.split("=");
        CookieModel model = new CookieModel();
        model.setName(aryCookie[0]);
        if (aryCookie.length == 2) {
            model.setValue(aryCookie[1]);
        }

        return model;
    }

    public String getSessionId() {
        List<CookieModel> list = getCookies();

        for (CookieModel model : list) {
            if ("JSESSIONID".equals(model.getName())) {
                return model.getValue();
            }
        }
        return "";
    }

}