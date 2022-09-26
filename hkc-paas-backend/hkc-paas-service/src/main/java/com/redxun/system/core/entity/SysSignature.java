
/**
 * <pre>
 *
 * 描述：签名实体实体类定义
 * 表:sys_signature
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-06-10 14:58:11
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
@TableName(value = "sys_signature")
public class SysSignature  extends BaseExtEntity<String> {

    @JsonCreator
    public SysSignature() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //签名图片ID
    @TableField(value = "FILE_ID_")
    private String fileId;
    //签名图片名称
    @TableField(value = "FILE_NAME_")
    private String fileName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



