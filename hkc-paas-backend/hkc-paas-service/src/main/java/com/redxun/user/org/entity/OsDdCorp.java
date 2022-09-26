
/**
 * <pre>
 *
 * 描述：os_dd_corp实体类定义
 * 表:os_dd_corp
 * 作者：gjh
 * 邮箱: zyg@redxun.cn
 * 日期:2021-02-07 11:15:51
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 钉钉企业
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_dd_corp")
public class OsDdCorp  extends BaseExtEntity<String> {

    @JsonCreator
    public OsDdCorp() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //园区企业ID
    @TableField(value = "CORP_ID_")
    private String corpId;



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



