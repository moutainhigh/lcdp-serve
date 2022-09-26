package com.redxun.system.mq;

import cn.hutool.core.bean.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.system.core.entity.SysErrorLog;
import com.redxun.system.core.mapper.SysErrorLogMapper;
import com.redxun.web.dto.SysErrorLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class ErrLogConsumer {

    @Resource
    SysErrorLogMapper errorLogMapper;

    @StreamListener(ErrLogInput.INPUT_FILED)
    public void handErrLog(SysErrorLogDto dto) {
        SysErrorLog errorLog=new SysErrorLog();
        BeanUtil.copyProperties(dto,errorLog);
        String id= IdGenerator.getIdStr();
        errorLog.setId(id);
        errorLogMapper.insert(errorLog);
    }
}
