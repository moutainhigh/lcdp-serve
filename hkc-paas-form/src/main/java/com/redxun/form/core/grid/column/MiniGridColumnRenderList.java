package com.redxun.form.core.grid.column;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.form.core.grid.enums.MiniGridColumnType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class MiniGridColumnRenderList implements MiniGridColumnRender {
    @Override
    public String getRenderType() {
        return MiniGridColumnType.LIST.name();
    }

    @Override
    public String render(GridHeader gridHeader, Map<String, Object> rowData, Object val, boolean isExport) {
        if(val==null) {
            return "";
        }
        return val.toString();
    }
}
