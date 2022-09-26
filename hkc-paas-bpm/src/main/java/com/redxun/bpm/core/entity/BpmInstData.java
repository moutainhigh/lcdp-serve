
/**
 * <pre>
 *
 * 描述：BPM_INST_DATA实体类定义
 * 表:BPM_INST_DATA
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-01 16:16:14
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

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
@TableName(value = "BPM_INST_DATA")
public class BpmInstData  extends BaseExtEntity<String> {

    @JsonCreator
    public BpmInstData() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //INST_ID_
    @TableField(value = "INST_ID_")
    private String instId;
    //PK_
    @TableField(value = "PK_")
    private String pk;
    //BODEF_ALIAS_
    @TableField(value = "BODEF_ALIAS_")
    private String bodefAlias;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



