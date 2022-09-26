package com.redxun.system.mq;


import cn.hutool.core.bean.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.dto.sys.SysAppLogDto;
import com.redxun.system.core.entity.SysAppLog;
import com.redxun.system.core.mapper.SysAppLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ApiLogConsumer {

    @Resource
    private SysAppLogMapper appLogMapper;

    @StreamListener(ApiLogInput.INPUT_FILED)
    public void handApiLog(SysAppLogDto appLogDto) {
        String id= IdGenerator.getIdStr();
        SysAppLog appLog=new SysAppLog();
        BeanUtil.copyProperties(appLogDto,appLog);
        appLog.setId(id);
        appLogMapper.insert(appLog);
    }
}
