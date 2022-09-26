package com.redxun.bpm.ext.script;

import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.MethodDefine;
import com.redxun.bpm.script.cls.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * BPM的自定义脚本示例
 */
//@ClassScriptType(type = "DemoScript",description = "自定义脚本示例")
//@Component("DemoScript")
public class DemoScript implements IScript {

    @MethodDefine(title="根据角色KEY返回人员",description = "根据角色KEY返回人员")
    public List<TaskExecutor> getByRole(@ParamDefine(varName = "roleKey",description = "角色KEY")
                                                    String roleKey){
        IUser user= ContextUtil.getCurrentUser();
        String depId=user.getDeptId();

        return new ArrayList<>();
    }
}
