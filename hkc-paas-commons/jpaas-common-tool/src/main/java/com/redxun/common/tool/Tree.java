package com.redxun.common.tool;

import java.beans.Transient;
import java.util.List;

/**
 * 树结构对象，用于将列表数据转换成树结构。
 * @author ray
 *
 */
public interface Tree {
	
	/**
	 * 主键ID
	 * @return
	 */
	String getId();
	
	/**
	 * 父ID
	 * @return
	 */
	String getParentId();

	/**
	 * 显示的值。
	 * @return
	 */
	String getText();
	
	/**
	 * 子对象。
	 * @return
	 */
	List<Tree> getChildren();

	boolean hasChildren();
	
	/**
	 * 设置子对象。
	 * @param list
	 */
	void setChildren(List<Tree> list);
}