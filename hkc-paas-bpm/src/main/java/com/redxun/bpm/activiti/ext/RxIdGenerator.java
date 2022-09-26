package com.redxun.bpm.activiti.ext;

import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * id 产生器。
 */
public class RxIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return com.redxun.common.tool.IdGenerator.getIdStr();
    }
}
