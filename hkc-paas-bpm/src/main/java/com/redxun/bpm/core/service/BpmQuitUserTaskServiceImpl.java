
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmQuitUserTask;
import com.redxun.bpm.core.mapper.BpmQuitUserTaskMapper;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
* [离职人员流程任务表]业务服务类
*/
@Service
public class BpmQuitUserTaskServiceImpl extends SuperServiceImpl<BpmQuitUserTaskMapper, BpmQuitUserTask> implements BaseService<BpmQuitUserTask> {

    @Resource
    private BpmQuitUserTaskMapper bpmQuitUserTaskMapper;

    @Override
    public BaseDao<BpmQuitUserTask> getRepository() {
        return bpmQuitUserTaskMapper;
    }

    /**
     * 离职人员的处理
     * 判断该用户在当前节点的离职人员流程任务表是否有记录
     * 有记录，不做任何处理
     * 无记录，新增一条记录
     * @param bpmQuitUserTask
     */
    public void dealQuitUser(BpmQuitUserTask bpmQuitUserTask) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("QUIT_USER_ID_", bpmQuitUserTask.getQuitUserId());
        queryWrapper.eq("DEF_ID_", bpmQuitUserTask.getDefId());
        queryWrapper.eq("NODE_ID_", bpmQuitUserTask.getNodeId());
        List<BpmQuitUserTask> list = bpmQuitUserTaskMapper.selectList(queryWrapper);
        //该用户在当前节点的离职人员流程任务表有记录，不做任何处理
        if(BeanUtil.isEmpty(list)){
            bpmQuitUserTask.setId(IdGenerator.getIdStr());
            this.insert(bpmQuitUserTask);
        }
        else{
            this.update(list.get(0));
        }
    }
}
