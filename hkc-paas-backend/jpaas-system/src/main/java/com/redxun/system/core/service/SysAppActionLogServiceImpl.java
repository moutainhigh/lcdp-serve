
package com.redxun.system.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysAppActionLog;
import com.redxun.system.core.mapper.SysAppActionLogMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [应用操作日志]业务服务类
*/
@Service
public class SysAppActionLogServiceImpl extends SuperServiceImpl<SysAppActionLogMapper, SysAppActionLog> implements BaseService<SysAppActionLog> {

    @Resource
    private SysAppActionLogMapper sysAppActionLogMapper;

    @Override
    public BaseDao<SysAppActionLog> getRepository() {
        return sysAppActionLogMapper;
    }


}
