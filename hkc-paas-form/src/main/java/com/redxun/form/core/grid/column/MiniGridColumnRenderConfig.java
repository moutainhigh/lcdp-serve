package com.redxun.form.core.grid.column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MiniGrid列头渲染的配置
 * @author mansan
 *
 */
@Service
public class MiniGridColumnRenderConfig {
	/**
	 * 列值渲染器映射
	 */
	@Autowired
	private Map<String, MiniGridColumnRender> columnRenderMap = new HashMap<String, MiniGridColumnRender>();
	/**
	 * 默认列值渲染器
	 */
	private MiniGridColumnRender defaulColumnRender = new MiniGridColumnRenderCommon();


	public Map<String, MiniGridColumnRender> getColumnRenderMap() {
		return columnRenderMap;
	}

	public MiniGridColumnRender getColumnRenderMapByType(String type){
		for(Map.Entry<String,MiniGridColumnRender> entry:columnRenderMap.entrySet()){
			MiniGridColumnRender miniGridColumnRender=entry.getValue();
			if(miniGridColumnRender.getRenderType().equals(type)){
				return miniGridColumnRender;
			}
		}
		return null;
	}

	public void setColumnRenderMap(Map<String, MiniGridColumnRender> columnRenderMap) {
		this.columnRenderMap = columnRenderMap;
	}

	public MiniGridColumnRender getDefaulColumnRender() {
		return defaulColumnRender;
	}

	public void setDefaulColumnRender(MiniGridColumnRender defaulColumnRender) {
		this.defaulColumnRender = defaulColumnRender;
	}

}
