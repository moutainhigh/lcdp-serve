
package com.redxun.form.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPcHistory;
import com.redxun.form.core.mapper.FormPcHistoryMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

/**
* [表单设计历史]业务服务类
*/
@Service
public class FormPcHistoryServiceImpl extends SuperServiceImpl<FormPcHistoryMapper, FormPcHistory> implements BaseService<FormPcHistory> {

    @Resource
    private FormPcHistoryMapper formPcHistoryMapper;

    @Override
    public BaseDao<FormPcHistory> getRepository() {
        return formPcHistoryMapper;
    }

    /**
     * 保存表单设计历史
     * @param formPc
     * @param remark
     */
    public void insertFormPcHistory(FormPc formPc,String remark) {
        IUser user = ContextUtil.getCurrentUser();
        FormPcHistory formPcHistory=new FormPcHistory();
        cn.hutool.core.bean.BeanUtil.copyProperties(formPc,formPcHistory);
        formPcHistory.setId(IdGenerator.getIdStr());
        formPcHistory.setFormPcId(formPc.getId());
        formPcHistory.setRemark(remark);
        formPcHistory.setCreateBy(user.getUserId());
        formPcHistory.setCreateTime(new Date());
        formPcHistory.setTenantId(user.getTenantId());
        insert(formPcHistory);
        //删除超出数量的历史记录
        FormPcHistory history=formPcHistoryMapper.selectByNum(formPc.getId(),20);
        if(BeanUtil.isNotEmpty(history)){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.le("CREATE_TIME_",history.getCreateTime());
            wrapper.eq("FORM_PC_ID_",formPc.getId());
            formPcHistoryMapper.delete(wrapper);
        }
    }

}
