
package com.redxun.system.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysJobTask;
import com.redxun.system.core.mapper.SysJobTaskMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [定时任务定义]业务服务类
*/
@Service
public class SysJobTaskServiceImpl extends SuperServiceImpl<SysJobTaskMapper, SysJobTask> implements BaseService<SysJobTask> {

    @Resource
    private SysJobTaskMapper sysJobTaskMapper;

    @Override
    public BaseDao<SysJobTask> getRepository() {
        return sysJobTaskMapper;
    }

}
