package com.redxun.user.org.entity;

import com.redxun.common.tool.BeanUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @模块包 (com.redxun.common.model)
 * @创建人 csx
 * @创建时间 2019/12/29
 * @描述 TODO
 **/
@Data
public class OsGroupNode extends OsGroup {
    /**
     * 子记录
     */
    private List<OsGroupNode> children=new ArrayList<>();

    public OsGroupNode(){

    }

    public OsGroupNode(OsGroup osGroup){
        try {
            BeanUtil.copyNotNullProperties(this, osGroup);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
