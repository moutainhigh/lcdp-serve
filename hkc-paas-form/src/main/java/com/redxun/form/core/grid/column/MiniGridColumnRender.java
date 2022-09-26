package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;

import java.util.Map;

/**
 * MiniUI表列数值渲染
 * @author mansan
 *
 */
public interface MiniGridColumnRender {
	/**
	 * 获得渲染类型
	 * @return
	 */
	String getRenderType();

	/**
	 * 根据传入的值进行返回值的渲染
	 * @param gridHeader
	 * @param rowData
	 * @param val
	 * @param isExport
	 * @return
	 */
	String render(GridHeader gridHeader, Map<String, Object> rowData, Object val, boolean isExport);
}
