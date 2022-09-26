package com.redxun.bpm.util;

import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;

import java.util.HashMap;
import java.util.Map;

/**
 *@描述 流程处理信息工具
 *@创建人 csx
 *@创建时间 2019/12/19
 **/
public class ProcessHandleUtil {

    private static ThreadLocal<IExecutionCmd> processCmdLocal=new ThreadLocal<IExecutionCmd>();

    public static void setProcessCmd(IExecutionCmd cmd){
        processCmdLocal.set(cmd);
    }

    public static IExecutionCmd getProcessCmd(){
        return processCmdLocal.get();
    }

    public static void clearProcessCmd(){
        processCmdLocal.remove();
    }


    private static ThreadLocal<Map<String,BpmTask>> taskMap=new ThreadLocal<>();






}
