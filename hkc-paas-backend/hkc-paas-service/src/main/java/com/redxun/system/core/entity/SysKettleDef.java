
/**
 * <pre>
 *
 * 描述：KETTLE定义实体类定义
 * 表:SYS_KETTLE_DEF
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-06-17 22:23:15
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_KETTLE_DEF")
public class SysKettleDef  extends BaseExtEntity<String> {

    @JsonCreator
    public SysKettleDef() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //分类
    @TableField(value = "GATEGORY_")
    private String gategory;
    //名称
    @TableField(value = "NAME_")
    private String name;
    //变量配置
    @TableField(value = "PARAMETERS_")
    private String parameters;
    //存储设定
    @TableField(value = "STORE_SETTING_")
    private String storeSetting;
    //存储类型(文件:file，资源库:resource)
    @TableField(value = "STORE_TYPE_")
    private String storeType;
    //类型(job,trans)
    @TableField(value = "TYPE_")
    private String type;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



