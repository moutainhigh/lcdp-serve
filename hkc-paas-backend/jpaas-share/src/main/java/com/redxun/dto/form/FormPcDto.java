package com.redxun.dto.form;

import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class FormPcDto extends BaseDto {
    //主键
    private String id;
    //名称
    private String name;
    //分类ID
    private String categoryId;
    //别名
    private String alias;
    //类型
    private String type="ONLINE-DESIGN";
    //模版
    private String template;
    //表单脚本
    private String javascript;
    //表单脚本
    private String javascriptKey;
    private String metadata;
    //发布状态
    private Integer deployed;
    //业务模型ID
    private String bodefId;
    private String boDefAlias;
    //版本号
    private Integer version;
    //主版本
    private Integer main;
    private String buttonDef="";
    private String tableButtonDef="";
    /**
     * 这个字段主要处理表单的初始数据和时需要处理的数据。
     */
    private String dataSetting="";
    /**
     * 表单设定，这个字段存储表单的一些设定，比如高宽数据源，是否必填等。
     */
    private String formSettings="";


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FormPcDto formPcDto = (FormPcDto) o;
        return Objects.equals(id, formPcDto.id)  ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
