package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.ValueResult;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Map;


public interface IAttrEasyHandler {

	/**
	 * 字段解析
	 * @param jsonObject
	 * @return
	 */
	FormBoAttr parse(JSONObject jsonObject);

	/**
	 * 返回插件名称。
	 * @return
	 */
	String getPluginName();

	/**
	 * 描述
	 * @return
	 */
	String getDescription();


	/**
	 * 返回数据格式如下{key:"",name:""}
	 * 如果是单字段属性则返回{key:""}
	 * 复合字段则返回{key:"",name:""}
	 * @param attr
	 * @param jsonData
	 * @return
	 */
	void getInitData(FormBoAttr attr, JSONObject jsonData);

	/**
	 * 获取列。
	 * @return
	 */
	List<Column> getColumns(FormBoAttr attr);

	/**
	 * 根据控件和一行数据获取控件的值。
	 * @param attr
	 * @param rowData
	 * @return
	 */
	ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal);

	/**
	 * 0 双值
	 * 1 单值
	 * 2 多值(非双值)
	 * @return
	 */
	default int getType(){
		return 1;
	};

	void handFromByType(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator,boolean isCurrentField);

	/**
	 * 删除行的数据。
	 * @param attr
	 * @param rowData
	 * @param isExternal
	 */
	void removeFields(FormBoAttr attr, Map<String,Object> rowData, boolean isExternal);

	/**
	 * 在保存时，我们会根据控件类型将表单数据分拆成多个表字段进行插入。
	 * <pre>
	 *     {user:{label:"",value:""}}
	 *     这个数据将会分拆后，插入到 F_USER,F_USER_NAME 字段。
	 *     这个我们可以自定义hander 将数据插入到 多个字段中。
	 * </pre>
	 * @param attr
	 * @param jsonRow
	 * @return
	 */
	List<FieldEntity> getFieldEntity(FormBoAttr attr,JSONObject jsonRow);

}
