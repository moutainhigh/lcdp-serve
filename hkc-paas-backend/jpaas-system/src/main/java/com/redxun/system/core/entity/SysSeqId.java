
/**
 * <pre>
 *
 * 描述：系统流水号实体类定义
 * 表:sys_seq_id
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-08 23:31:48
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_seq_id")
public class SysSeqId  extends BaseExtEntity<String> {

    /**
     * 按天生成=DAY
     */
    public final static String GEN_TYPE_DAY="DAY";
    /**
     * 按周生成=WEEK
     */
    public final static String GEN_TYPE_WEEK="WEEK";
    /**
     * 按月生成=MONTH
     */
    public final static String GEN_TYPE_MONTH="MONTH";
    /**
     * 按年生成=YEAR
     */
    public final static String GEN_TYPE_YEAR="YEAR";
    /**
     * 自动生成=AUTO
     */
    public final static String GEN_TYPE_AUTO="AUTO";

    public final static String SYS_SQL_ID = "SEQ_";

    /**
     * 流程实例的产生的序号
     */
    public final static String ALIAS_BPM_INST_BILL_NO="BPM_INST_BILL_NO";

    @JsonCreator
    public SysSeqId() {
    }

    //流水号ID
    @TableId(value = "SEQ_ID_",type = IdType.INPUT)
	private String seqId;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_")
    private String alias;
    //当前日期
    @TableField(value = "CUR_DATE_")
    private java.util.Date curDate;
    //规则
    @TableField(value = "RULE_")
    private String rule;
    //规则配置
    @FieldDef(comment = "规则配置")
    @TableField(value = "RULE_CONF_")
    private String ruleConf;
    //初始值
    @TableField(value = "INIT_VAL_")
    private Integer initVal;
    //生成方式
    @TableField(value = "GEN_TYPE_")
    private String genType;
    //流水号长度
    @TableField(value = "LEN_")
    private Integer len;
    //当前值
    @TableField(value = "CUR_VAL")
    private Integer curVal;
    //步长
    @TableField(value = "STEP_")
    private Short step;
    //备注
    @TableField(value = "MEMO_")
    private String memo;
    //系统缺省

    @TableField(value = "IS_DEFAULT_")
    private String isDefault;
    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //SYS_ID_
    @TableField(value = "SYS_ID_")
    private String sysId;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @TableField(exist = false)
    private List<Object> confAttr;


    @Override
    public String getPkId() {
        return seqId;
    }

    @Override
    public void setPkId(String pkId) {
        this.seqId=pkId;
    }
}



