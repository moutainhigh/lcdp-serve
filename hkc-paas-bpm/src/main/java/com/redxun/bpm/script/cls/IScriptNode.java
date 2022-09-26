package com.redxun.bpm.script.cls;

import java.util.List;

/**
 * 脚本或方法树节点
 */
public interface IScriptNode {
    /**
     * 节点Key
     * @return
     */
    String getKey();

    /**
     * 节点Key
     * @return
     */
    String getTitle();

    /**
     * 描述
     * @return
     */
    String getDescription();

    /**
     * 节点类型，不同的节点类型，脚本最终解析时不同
     * @return
     */
    String getNodeType();

    /**
     * 获取前置标识
     * @return
     */
    String getPreFix();

    /**
     * 是否为子节点
     * @return
     */
    boolean getIsLeaf();

    /**
     * 获得子节点
     * @return
     */
    List<IScriptNode> getChildren();

    /**
     * 节点类型=函数，FUN
     */
    public static final String NODE_TYPE_FUN="FUN";
    /**
     * 节点类型=属性，ATTR
     */
    public static final String NODE_TYPE_ATTR="ATTR";
    /**
     * 节点类型=对话框，DIALOG
     */
    public static final String NODE_TYPE_DIALOG="DIALOG";
}
