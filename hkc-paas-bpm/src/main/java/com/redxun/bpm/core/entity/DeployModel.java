package com.redxun.bpm.core.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 发布模型
 */
@Getter
@Setter
@Accessors(chain = true)
public class DeployModel {

    public static final  String ACTION_SAVE="save";
    public static final  String ACTION_MODIFY="modify";
    public static final  String ACTION_DEPLOY="deploy";
    public static final  String ACTION_CHECK="check";



    /**
     * 发布动作。
     * 1. 保存 save
     * 2. 保存修改 modify
     * 3. 发布流程 deploy
     */
    private String action="";
    /**
     * 发布的模型
     */
    private BpmDef bpmDef;

}
