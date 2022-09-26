
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.CodeGenTemplate;
import com.redxun.form.core.mapper.CodeGenTemplateMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [代码生成模板]业务服务类
*/
@Service
public class CodeGenTemplateServiceImpl extends SuperServiceImpl<CodeGenTemplateMapper, CodeGenTemplate> implements BaseService<CodeGenTemplate> {

    @Resource
    private CodeGenTemplateMapper codeGenTemplateMapper;

    @Override
    public BaseDao<CodeGenTemplate> getRepository() {
        return codeGenTemplateMapper;
    }

}
