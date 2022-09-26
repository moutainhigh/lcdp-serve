package com.redxun.api.sys.factory;

import com.redxun.api.sys.RemoteAppService;
import com.redxun.dto.sys.SysAppDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteAppFallbackFactory implements FallbackFactory<RemoteAppService>
{
    @Override
    public RemoteAppService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteAppService()
        {


            @Override
            public List<SysAppDto> getAllApps()
            {
                log.error("通过菜单ID集合与菜单类型查询菜单异常:{}", throwable);
                return null;
            }

            @Override
            public List<SysAppDto> getAppsByIds(String appIds)
            {
                log.error("通过菜单ID集合与权限查询菜单异常:{}", appIds, throwable);
                return null;
            }


        };
    }
}
