package com.redxun.dto.sys;

import com.redxun.common.tool.BeanUtil;
import lombok.Data;

import java.util.List;

/**
 * 系统树节点DTO
 */
@Data
public class SysTreeNodeDto extends SysTreeDto {
    /**
     * 子树
     */
    private List<SysTreeNodeDto> children;

    public SysTreeNodeDto(){

    }

    public SysTreeNodeDto(SysTreeDto tree){
        try {
            BeanUtil.copyNotNullProperties(this, tree);
        }catch (Exception e){e.printStackTrace();}
    }

}