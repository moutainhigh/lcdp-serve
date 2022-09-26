package com.redxun.dto.form;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 单据数据处理返回的数据结果
 */
@Setter
@Getter
public class DataResult implements Serializable {
    /**
     * 添加操作
     */
    public final static String ACTION_ADD="add";
    /**
     * 更新操作
     */
    public final static String ACTION_UPD="upd";
    /**
     * 对数据的动作
     */
    private  String action="";
    /**
     * 主键值
     */
    private String pk="";
    /**
     * 业务对象别名
     */
    private String boAlias="";
    /**
     * 表单方案标识
     */
    private String solutionAlias="";

    /**
     * 流程实例ID
     */
    private String instId="";
    /**
     * 流程状态。
     */
    private String status="";

    public static DataResult getResult(String pk,String action){
        DataResult result=new DataResult();
        result.setPk(pk);
        result.setAction(action);
        return  result;
    }
}
