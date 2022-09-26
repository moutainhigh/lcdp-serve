package com.redxun.system.ext.log.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.log.model.Audit;
import com.redxun.system.ext.log.ILogResume;
import com.redxun.system.feign.FormClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 处理表单数据（新增，编辑，删除）
 */
@Component
public class FormLogResume implements ILogResume {
    @Resource
    FormClient formClient;

    @Override
    public String getType() {
        return Audit.BUS_TYPE_FORM;
    }

    @Override
    public void resume(String action,String detail,String extParams) {
        JSONObject detailJson = JSONObject.parseObject(detail);
        if ("saveForm".equals(action)) {
            //删除保存的数据
            JSONObject json = new JSONObject();
            json.put(Audit.IS_LOG, false);
            json.put("alias", detailJson.getString("alias"));
            json.put("id", detailJson.getString("pk"));
            formClient.removeById(json);
        } else if ("updateForm".equals(action)) {
            JSONObject json = new JSONObject();
            json.put(Audit.IS_LOG, false);
            JSONObject setting = new JSONObject();
            setting.put("action", "save");
            setting.put("alias", detailJson.getString("alias"));
            json.put("setting", setting);
            json.put("data", parseCheckedData(detailJson,detailJson.getJSONObject("oldData"),extParams));
            formClient.saveForm(json);
        } else if ("removeById".equals(action)) {
            //恢复删除的数据
            JSONObject json = new JSONObject();
            json.put(Audit.IS_LOG, false);
            JSONObject setting = new JSONObject();
            setting.put("action", "save");
            setting.put("alias", detailJson.getString("alias"));
            json.put("setting", setting);
            JSONArray data = detailJson.getJSONArray("data");
            for (int i = 0; i < data.size(); i++) {
                json.put("data", parseCheckedData(detailJson,data.getJSONObject(i),extParams));
                formClient.saveForm(json);
            }
        }
    }

    /**
     * 解析被选择的数据返回
     * @param detailJson
     * @param formData
     * @param extParams
     * @return
     */
    private JSONObject parseCheckedData(JSONObject detailJson,JSONObject formData,String extParams) {
        String pk = detailJson.getString("pk");
        String pkField = detailJson.getString("pkField");
        String alias = detailJson.getString("alias");
        String[] checkedAry = extParams.split(",");
        List<String> checkedList = Arrays.asList(checkedAry);
        JSONObject mainData = BeanUtil.deepCopyBean(formData);
        JSONObject subData = new JSONObject();
        for (String key : formData.keySet()) {
            if (key.indexOf("sub__") != -1) {
                subData.put(key.substring(key.indexOf("sub__")+5), mainData.remove(key));
            }
        }
        JSONObject json = new JSONObject();
        if (checkedList.contains(alias)) {
            json.putAll(mainData);
        } else {
            json.put(pkField, pk);
        }
        for (String subKey : subData.keySet()) {
            if (checkedList.contains(subKey)) {
                json.put("sub__"+subKey, subData.get(subKey));
            }
        }
        return json;
    }
}
