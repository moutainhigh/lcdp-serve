
package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysInterfaceProject;
import com.redxun.system.core.mapper.SysInterfaceProjectMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [接口项目表]业务服务类
*/
@Service
public class SysInterfaceProjectServiceImpl extends SuperServiceImpl<SysInterfaceProjectMapper, SysInterfaceProject> implements BaseService<SysInterfaceProject> {

    @Resource
    private SysInterfaceProjectMapper sysInterfaceProjectMapper;

    @Override
    public BaseDao<SysInterfaceProject> getRepository() {
        return sysInterfaceProjectMapper;
    }

    public List<SysInterfaceProject> queryProject() {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("STATUS_", MBoolean.YES.name());
        return sysInterfaceProjectMapper.selectList(queryWrapper);
    }

    public boolean isExist(SysInterfaceProject sysInterfaceProject) {
        Map<String,Object> params=new HashMap<>();
        params.put("projectAlias",sysInterfaceProject.getProjectAlias());
        params.put("projectName",sysInterfaceProject.getProjectName());
        if(StringUtils.isNotEmpty(sysInterfaceProject.getProjectId()) ) {
            params.put("projectId", sysInterfaceProject.getProjectId());
        }
        int count=this.sysInterfaceProjectMapper.isExist(params);
        return  count>0;
    }
}
