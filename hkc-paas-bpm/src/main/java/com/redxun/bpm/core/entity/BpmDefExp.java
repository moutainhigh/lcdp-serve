
/**
 * <pre>
 *
 * 描述：流程定义实体类定义
 * 表:bpm_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-02 14:23:15
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.bpm.flow.entity.ActGeBytearray;
import com.redxun.bpm.flow.entity.ActReDeployment;
import com.redxun.bpm.flow.entity.ActReProcdef;
import com.redxun.dto.form.FormMobileDto;
import com.redxun.dto.form.FormPcDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BpmDefExp{
    @JsonCreator
    public BpmDefExp() {
    }

    /**
     * 是否默认发布。
     */
    private boolean deployed=false;
    /**
     * 流程定义Id
     */
    private String bpmDefId;

    /**
     * 流程定义名称
     */
    private String bpmDefName;

    /**
     * 流程定义
     */
    private BpmDef bpmDef;
    private ActGeBytearray byteArrays;
    private ActReDeployment actDeployMent;
    private ActReProcdef actProcDef;

    /**
     * pc表单视图
     */
    private Set<FormPcDto> bpmFormPcViews=new HashSet<FormPcDto>();

    /**
     * moble表单视图
     */
    private Set<FormMobileDto> mobileForms=new HashSet<FormMobileDto>();
    /**
     * bo定义
     */
    private JSONArray sysBoDefs=new JSONArray();
}



