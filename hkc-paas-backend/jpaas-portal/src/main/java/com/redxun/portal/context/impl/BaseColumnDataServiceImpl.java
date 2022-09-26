package com.redxun.portal.context.impl;

import com.redxun.portal.context.IColumnDataService;
import lombok.Getter;
import lombok.Setter;

/**
 * 栏目数据服务基础类。
 */
@Getter
@Setter
public abstract class BaseColumnDataServiceImpl implements IColumnDataService {

    private  String setting;

    private  String colId;

    /**
     * 设置查询对象
     * @return
     */
    @Override
    public void setSettingValue(String setting,String colId){
        setSetting(setting);
        setColId(colId);
    };
}
