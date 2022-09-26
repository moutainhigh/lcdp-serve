package com.redxun.form.core.importdatahandler;


import com.redxun.common.base.entity.JsonResult;

import java.util.List;
import java.util.Map;

/**
 * 表单数据导入处理器的接口类
 *
 * @author zfh
 */
public interface ImportDataHandler {

    String getName();

    //对每一行进行判断处理
    default JsonResult handRow(Map<String,Object> data){
        return JsonResult.Success();
    }

    //对读取的数据进行处理
    default JsonResult beforeInsert(List<Map<String,Object>> data){
        return JsonResult.Success();
    }

}
