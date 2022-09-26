package com.redxun.api.sys.factory;

import com.redxun.api.sys.RemoteMenuService;
import com.redxun.common.dto.AuthDto;
import com.redxun.common.dto.SysMenuDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RemoteMenuFallbackFactory implements FallbackFactory<RemoteMenuService>
{
    @Override
    public RemoteMenuService create(Throwable throwable)
    {
        log.error(throwable.getMessage());
        return new RemoteMenuService()
        {

            @Override
            public List<AuthDto> selectMenusByMenuIds(String menuIds)
            {
                log.error("查询系统正常显示菜单(含按钮):{}", throwable);
                return null;
            }


            @Override
            public List<SysMenuDto> getMenusByType(String menuType) {
                return null;
            }
        };
    }
}
