
package com.redxun.bpm.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmDefaultTemplate;
import com.redxun.bpm.core.mapper.BpmDefaultTemplateMapper;
import com.redxun.common.tool.StringUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [bpm_default_template]业务服务类
*/
@Service
public class BpmDefaultTemplateServiceImpl extends SuperServiceImpl<BpmDefaultTemplateMapper, BpmDefaultTemplate> implements BaseService<BpmDefaultTemplate> {

    @Resource
    private BpmDefaultTemplateMapper bpmDefaultTemplateMapper;

    @Override
    public BaseDao<BpmDefaultTemplate> getRepository() {
        return bpmDefaultTemplateMapper;
    }


    public boolean isExists(BpmDefaultTemplate template){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("MESSAGE_TYPE_",template.getMessageType());
        queryWrapper.eq("TEMPLATE_TYPE_",template.getTemplateType());
        if(StringUtils.isNotEmpty(template.getId()) ){
            queryWrapper.ne("ID_",template.getId());
        }
        Integer count = bpmDefaultTemplateMapper.selectCount(queryWrapper);
        return count>0;
    }

    /**
     * 根据模板类型查询模板。
     * @param templateType
     * @return
     */
    public List<BpmDefaultTemplate> getByTemplateType(String templateType){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TEMPLATE_TYPE_",templateType);

        List list = bpmDefaultTemplateMapper.selectList(queryWrapper);
        return list;
    }



}
