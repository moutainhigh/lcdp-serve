package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryParam;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GridReportParam {

    private String reportConfigId;

    private QueryData queryData;

    private String incomeParams;
}
