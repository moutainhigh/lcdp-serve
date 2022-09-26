package com.redxun.form.core.datahandler;

import com.redxun.common.base.entity.SqlModel;
import com.redxun.form.bo.entity.FormBoEntity;

/**
 * 此接口用于构建子表查询的sql语句。
 * @author yongguo
 *
 */
public interface ISqlBuilder {
	
	/**
	 * 根据外键查询数据。
	 * @param boEnt
	 * @param fk
	 * @return
	 */
	SqlModel getByFk(FormBoEntity boEnt, String fk);

}
