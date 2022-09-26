package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.form.DataResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AfterParams {

    private DataResult dataResult;

    private JSONObject jsonData;


}
