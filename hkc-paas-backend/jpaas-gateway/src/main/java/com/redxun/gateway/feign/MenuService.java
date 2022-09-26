package com.redxun.gateway.feign;

import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.common.dto.SysMenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 菜单的Feign调用
 * @author yjy
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE,decode404 = true)
public interface MenuService {
	/**
	 * 角色菜单列表
	 * @param roleCodes
	 */
	@GetMapping(value = "/menus/{roleCodes}")
	List<SysMenuDto> findByRoleCodes(@PathVariable("roleCodes") String roleCodes);

}
