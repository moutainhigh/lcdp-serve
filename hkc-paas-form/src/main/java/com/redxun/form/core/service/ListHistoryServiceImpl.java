
package com.redxun.form.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.ListHistory;
import com.redxun.form.core.mapper.ListHistoryMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [form_bo_list_history]业务服务类
*/
@Service
public class ListHistoryServiceImpl extends SuperServiceImpl<ListHistoryMapper, ListHistory> implements BaseService<ListHistory> {

    @Resource
    private ListHistoryMapper listHistoryMapper;

    @Override
    public BaseDao<ListHistory> getRepository() {
        return listHistoryMapper;
    }

    public void addList(FormBoList entity,String remark){
        ListHistory listHistory = new ListHistory();
        listHistory.setId(IdGenerator.getIdStr());
        listHistory.setContent(entity.getListHtml());
        listHistory.setMobileContent(entity.getMobileHtml());
        listHistory.setListId(entity.getId());
        listHistory.setRemark(remark);
        Integer version = listHistoryMapper.getMaxVersion(entity.getId());
        if(version == null){
            listHistory.setVersion(1);
        }
        else {
            listHistory.setVersion(version + 1);
        }
        listHistoryMapper.insert(listHistory);
        //删除超出数量的历史记录
        ListHistory history = listHistoryMapper.selectByNum(entity.getId(), 20);
        if(BeanUtil.isNotEmpty(history)){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.le("CREATE_TIME_",history.getCreateTime());
            wrapper.eq("LIST_ID_",entity.getId());
            listHistoryMapper.delete(wrapper);
        }
    }

    public List<ListHistory> getByFk(String fk){
        QueryWrapper wrapper=new QueryWrapper<ListHistory>();
        wrapper.eq("LIST_ID_",fk);
        return listHistoryMapper.selectList(wrapper);
    }

}
