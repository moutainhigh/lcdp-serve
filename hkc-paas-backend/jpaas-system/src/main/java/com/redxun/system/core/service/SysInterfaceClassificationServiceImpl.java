
package com.redxun.system.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysInterfaceClassification;
import com.redxun.system.core.mapper.SysInterfaceClassificationMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [接口分类表]业务服务类
*/
@Service
public class SysInterfaceClassificationServiceImpl extends SuperServiceImpl<SysInterfaceClassificationMapper, SysInterfaceClassification> implements BaseService<SysInterfaceClassification> {

    @Resource
    private SysInterfaceClassificationMapper sysInterfaceClassificationMapper;

    @Override
    public BaseDao<SysInterfaceClassification> getRepository() {
        return sysInterfaceClassificationMapper;
    }

    /**
     * 根据项目ID查询接口分类集合
     * @param projectId 项目ID
     * @return
     */
    public List<SysInterfaceClassification> getByProjectId(String projectId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PROJECT_ID_",projectId);
        return sysInterfaceClassificationMapper.selectList(queryWrapper);
    }

    public void deleteByProjectId(String projectId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PROJECT_ID_",projectId);
        sysInterfaceClassificationMapper.delete(queryWrapper);
    }
}
