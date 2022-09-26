package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.tool.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;

/**
 * EXT_JSON_字段中的扩展属性
 * @param <T>
 */
@Setter
@Getter
public abstract class FormCalendarViewExt<T> extends BaseExtEntity<T> {
    //表单方案
    @TableField(exist = false)
    private String formAlias;
    //编辑表单名称
    @TableField(exist = false)
    private String formName;
    //添加表单方案
    @TableField(exist = false)
    private String formAddAlias;
    //添加表单名称
    @TableField(exist = false)
    private String formAddName;
    //表单方案明细别名
    @TableField(exist = false)
    private String formDetailAlias;
    //明细表单名称
    @TableField(exist = false)
    private String formDetailName;
    //列头配置
    @TableField(exist = false)
    private JSONObject columnConf;
    //按钮配置
    @TableField(exist = false)
    private JSONArray buttonConf;
    //脚本JS
    @TableField(exist = false)
    private String jsScript;
    //是否全屏
    @TableField(exist = false)
    private String isMax;
    //高度
    @TableField(exist = false)
    private Integer height;
    //宽度
    @TableField(exist = false)
    private Integer width;
    //扩展属性
    @TableField(value = "EXT_JSON_",jdbcType=JdbcType.VARCHAR)
    private String extJson;

    public String getExtJson() {
        Class class_ = getClass().getSuperclass();
        Field[] fields = class_.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(TableField.class) &&
                        !field.getAnnotation(TableField.class).exist()) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value == null) {
                        continue;
                    }
                    setExtJsonValueByKey(field.getName(), value);
                }
            } catch (Exception e) {
            }
        }
        return this.extJson;
    }

    public void setExtJson(String extJson){
        this.extJson=extJson;
        if(StringUtils.isNotEmpty(this.extJson)) {
            JSONObject extJsonObj=JSONObject.parseObject(this.extJson);
            Class class_ = getClass().getSuperclass();
            Field[] fields = class_.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.isAnnotationPresent(TableField.class) &&
                            !field.getAnnotation(TableField.class).exist()) {
                        field.setAccessible(true);
                        if(extJsonObj.containsKey(field.getName())){
                            field.set(this, extJsonObj.get(field.getName()));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void setExtJsonValueByKey(String key,Object value) {
        JSONObject extJsonObj=new JSONObject();
        if(StringUtils.isNotEmpty(extJson)){
            extJsonObj=JSONObject.parseObject(extJson);
        }
        extJsonObj.put(key,value);
        this.extJson=extJsonObj.toJSONString();
    }

}
