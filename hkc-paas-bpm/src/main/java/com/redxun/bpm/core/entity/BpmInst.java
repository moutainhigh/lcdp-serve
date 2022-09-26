
/**
 * <pre>
 *
 * 描述：流程实例实体类定义
 * 表:BPM_INST
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-17 21:15:25
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "BPM_INST")
public class BpmInst  extends BaseExtEntity<String> {



    @JsonCreator
    public BpmInst() {
    }

    //INST_ID_
    @TableId(value = "INST_ID_",type = IdType.INPUT)
	private String instId;

    //流程定义ID
    @TableField(value = "DEF_ID_")
    private String defId;

    @TableField(value = "TREE_ID_")
    private String treeId;
    //表单方案别名
    @TableField(value = "FORM_SOLUTION_ALIAS")
    private String formSolutionAlias;
    //ACT实例ID
    @TableField(value = "ACT_INST_ID_")
    private String actInstId;
    //ACT定义ID
    @TableField(value = "ACT_DEF_ID_")
    private String actDefId;
    //父流程实例ID
    @TableField(value = "PARENT_ACT_INST_ID_")
    private String parentActInstId;
    //解决方案ID_
    @TableField(value = "DEF_CODE_")
    private String defCode;
    //流程实例工单号
    @TableField(value = "INST_NO_")
    private String instNo;
    //业务单号
    @TableField(value = "BILL_NO_")
    private String billNo;

    //单据类型
    @TableField(value = "BILL_TYPE_")
    private String billType;
    //标题
    @TableField(value = "SUBJECT_")
    private String subject;
    //运行状态
    @TableField(value = "STATUS_")
    private String status;

    //版本
    @TableField(value = "VERSION_")
    private Integer version;
    //业务键ID
    @TableField(value = "BUS_KEY_")
    private String busKey;
    //审批正文依据ID
    @TableField(value = "CHECK_FILE_ID_")
    private String checkFileId;
    //是否为测试
    @TableField(value = "IS_TEST_")
    private String isTest;
    //出错信息
    @TableField(value = "ERRORS_")
    private String errors;
    //结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "END_TIME_")
    private java.util.Date endTime;
    //数据保存模式(all,json,db)
    @TableField(value = "DATA_SAVE_MODE_")
    private String dataSaveMode;
    //支持手机端
    @TableField(value = "SUPPORT_MOBILE_")
    private Integer supportMobile;
    //发起部门ID
    @TableField(value = "START_DEP_ID_")
    private String startDepId;
    //发起部门全名
    @TableField(value = "START_DEP_FULL_")
    private String startDepFull;
    //是否复活
    @TableField(value = "IS_LIVE_")
    private String isLive= MBoolean.NO.name();
    //复活的流程实例
    @TableField(value = "LIVE_INST_ID_")
    private String liveInstId;
    @TableField(value = "LOCKED_BY_")
    private String lockedBy;

    @TableField(value = "FIELD_JSON_")
    private String fieldJson;

    /**
     * 申请人姓名
     */
    @TableField(exist = false)
    private String applicantName;
    /**
     * 申请人账号
     */
    @TableField(exist = false)
    private String applicantNo;


    @Override
    public String getPkId() {
        return instId;
    }

    @Override
    public void setPkId(String pkId) {
        this.instId=pkId;
    }
}



