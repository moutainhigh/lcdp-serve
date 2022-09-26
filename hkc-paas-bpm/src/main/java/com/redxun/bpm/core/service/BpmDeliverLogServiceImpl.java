
package com.redxun.bpm.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmDeliverLog;
import com.redxun.bpm.core.mapper.BpmDeliverLogMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [流程待办任务交接日志]业务服务类
*/
@Service
public class BpmDeliverLogServiceImpl extends SuperServiceImpl<BpmDeliverLogMapper, BpmDeliverLog> implements BaseService<BpmDeliverLog> {

    @Resource
    private BpmDeliverLogMapper bpmDeliverLogMapper;

    @Override
    public BaseDao<BpmDeliverLog> getRepository() {
        return bpmDeliverLogMapper;
    }

}
