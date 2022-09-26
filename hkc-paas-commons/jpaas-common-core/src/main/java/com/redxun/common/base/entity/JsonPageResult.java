package com.redxun.common.base.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 模块：jpaas
 * 包名：com.redxun.common.base.entity
 * 功能描述：用于页面分页大小
 *
 * @author：csx
 * @date:2019/8/3
 */
@Data
public class JsonPageResult extends JsonResult{

    JsonPage result;

    public JsonPageResult() {

    }

    public JsonPageResult(IPage iPage){
        this.result=new JsonPage(iPage);
    }

    public void setPageData(IPage ipage){
        result=new JsonPage(ipage);
    }

    /**
     * 生成成功的状态
     * @param msg
     * @return
     */
    public static  JsonPageResult getSuccess(String msg){
        JsonPageResult jsonPageResult=new JsonPageResult();
        jsonPageResult.setSuccess(true);
        jsonPageResult.setCode(SUCESS_CODE);
        jsonPageResult.setMessage(msg);
        jsonPageResult.setShow(false);
        return jsonPageResult;
    }
    /**
     * 生成失败的状态
     * @param msg
     * @return
     */
    public static JsonPageResult getFail(String msg){
        JsonPageResult jsonPageResult=new JsonPageResult();
        jsonPageResult.setSuccess(false);
        jsonPageResult.setMessage(msg);
        jsonPageResult.setCode(FAIL_CODE);
        jsonPageResult.setShow(false);
        return jsonPageResult;
    }
}
