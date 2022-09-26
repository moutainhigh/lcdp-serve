
package com.redxun.form.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormDownloadRecord;
import com.redxun.form.core.mapper.FormDownloadRecordMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [Excel下载记录(异步)]业务服务类
*/
@Service
public class FormDownloadRecordServiceImpl extends SuperServiceImpl<FormDownloadRecordMapper, FormDownloadRecord> implements BaseService<FormDownloadRecord> {

    @Resource
    private FormDownloadRecordMapper formDownloadRecordMapper;

    @Override
    public BaseDao<FormDownloadRecord> getRepository() {
        return formDownloadRecordMapper;
    }

}
