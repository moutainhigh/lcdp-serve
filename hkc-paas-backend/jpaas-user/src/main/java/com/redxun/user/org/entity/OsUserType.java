package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
/**
 * <pre>
 *
 * 描述：用户类型实体类定义
 * 表:OS_USER_TYPE
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-26 11:34:38
 * 版权：广州红迅软件
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "OS_USER_TYPE")
public class OsUserType  extends BaseExtEntity<String> {

    @JsonCreator
    public OsUserType() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //编码
    @FieldDef(comment = "编码")
    @TableField(value = "CODE_")
    private String code;
    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //描述
    @TableField(value = "DESCP_")
    private String descp;
    //GROUP_ID_
    @TableField(value = "GROUP_ID_")
    private String groupId;

    /**
     * 是否授权
     */
    @TableField(exist = false)
    private boolean grant=false;
    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



