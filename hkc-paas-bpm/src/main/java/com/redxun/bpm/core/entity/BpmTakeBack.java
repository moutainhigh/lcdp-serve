package com.redxun.bpm.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能: 是否可以退回。
 * @author ray
 * @date 2022/5/30 22:46
 */
@Setter
@Getter
public class BpmTakeBack {

    public static String BACK_TYPE_START="mystart";

    public static String BACK_TYPE_DONE="mydone";

    /**
     * 当前人审批的路径。
     */
    private BpmRuPath curPath;

    /**
     * 后续执行的路径。
     */
    private List<BpmRuPath> nextPaths;

    /**
     * 流程实例。
     */
    private BpmInst bpmInst;

}
