
package com.redxun.system.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysErrorLog;
import com.redxun.system.core.mapper.SysErrorLogMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [错误日志]业务服务类
*/
@Service
public class SysErrorLogServiceImpl extends SuperServiceImpl<SysErrorLogMapper, SysErrorLog> implements BaseService<SysErrorLog> {

    @Resource
    private SysErrorLogMapper sysErrorLogMapper;

    @Override
    public BaseDao<SysErrorLog> getRepository() {
        return sysErrorLogMapper;
    }

}
