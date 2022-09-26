package com.redxun.feign.sys;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.dto.sys.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 系统用户的Feign客户端
 *
 */
@FeignClient(name = "jpaas-system")
public interface SystemClient {


    /**
     *获取所有按钮菜单列表
     * @param menuType
     * @return
     */
    @GetMapping("/system/core/sysMenu/getAllButtonsByMenuType")
    JSONObject getAllButtonsByMenuType(@RequestParam("menuType") String menuType);


    /**
     * 获取流水号数据
     * @param alias
     * @return
     */
    @GetMapping("/system/core/sysSeqId/genSeqNo")
    String genSeqNo(@RequestParam(value = "alias") String alias);

    /**
     * 权限查询
     * @param type 权限类型
     * @param readKey 权限字段
     * @param treeId 树ID
     * @return
     */
    @GetMapping("/system/core/sysAuthSetting/findAuthRight")
    boolean findAuthRight(@RequestParam(value = "type")String type,@RequestParam(value="readKey")String readKey,@RequestParam(value="treeId")String treeId);

    /**
     *根据菜单ID集合与菜单类型获取菜单信息
     * @param queryParam
     * @return
     */
    @PostMapping("/system/core/sysMenu/getMenusByIdsAndTypes")
    List<SysMenuDto> getMenusByIdsAndTypes(@RequestBody MultiValueMap<String,String> queryParam);

    /**
     *根据appIds集合获取应用列表
     * @param queryParam
     * @return
     */
    @PostMapping("/system/core/sysApp/getAppsByIds")
    List<SysAppDto> getAppsByIds(@RequestBody MultiValueMap<String,String> queryParam);

    /**
     * 通过appId查询授权接口
     * @param appId
     * @return
     */
    @GetMapping("/system/core/sysAppAuth/findListByAppId")
    List<SysAppAuthDto> findListByAppId(@RequestParam(value="appId") String appId);

    /**
     * 保存授权日志
     * @param sysAppLogDto
     */
    @PostMapping("/system/core/sysAppLog/save")
    void saveLog(SysAppLogDto sysAppLogDto);


    @PostMapping("/system/core/sysMenu/getMenuMenuIds")
    List<SysMenuDto> getMenuMenuIds(@RequestBody MultiValueMap<String,String> queryParam);


    /**
     * 根据当前租户ID获取菜单资源。
     * @return
     */
    @GetMapping("/system/core/sysMenu/getMenusByTenantId")
    List<SysMenuDto> getMenusByTenantId( @RequestParam(name = "tenantId") String tenantId );


    /**
     * 根据当前租户ID获取菜单资源。
     * @return
     */
    @GetMapping("/user/org/osInstTypeMenu/getInstTypeMenusByTenantId")
    List<String> getInstTypeMenusByTenantId(@RequestParam(name = "tenantId") String tenantId);


    /**
     * 获取某分类下的所有树节点，含下下一级的子节点
     * @param catKey
     * @return
     */
    @ApiOperation("获取某分类下的所有树节点，含下下一级的子节点")
    @GetMapping("/system/core/sysTree/getByCatKey")
    List<SysTreeDto> getByCatKey (@ApiParam @RequestParam(value = "catKey") String catKey,
                                  @ApiParam @RequestParam(value = "readKey") String readKey,@ApiParam @RequestParam(value = "isAdmin") Boolean isAdmin,
                                  @ApiParam @RequestParam(value = "isGrant") Boolean isGrant,@ApiParam @RequestParam(value = "appId")String appId);

    /**
     * 根据分类KEY获取数据字典数据
     * @param key
     * @return
     */
    @GetMapping("/system/core/sysDic/treeByKey")
    JSONArray getDicByKey (@ApiParam @RequestParam(value = "key") String key);

    /**
     * 根据分类KEY获取数据字典数据
     * @param key
     * @return
     */
    @GetMapping("/system/core/sysDic/getTopDicByKey")
    List<SysDicDto> getTopDicByKey (@ApiParam @RequestParam(value = "key") String key);
    /**
     * 根据资源ID获取资源对象列表另外同时获取关联的接口数据。
     * @param queryParam
     * @return
     */
    @PostMapping("/system/core/sysMenu/getAllByMenuIds")
    List<SysMenuDto> getAllByMenuIds(@RequestBody MultiValueMap<String,String> queryParam);

    /**
     * 根据appIds集合与应用类型获取应用列表
     * @param appIds
     * @return
     */
    @GetMapping("/system/core/sysApp/getAppsByIdsAndType")
    List<SysAppDto> getAppsByIdsAndType(@RequestParam(name = "appIds")String appIds,@RequestParam(name = "appType") Integer appType);

    @ApiOperation("根据companyId集合获取公司级别应用列表")
    @GetMapping("/system/core/sysApp/getCompanyApps")
    List<SysAppDto> getCompanyApps(@ApiParam @RequestParam(value = "companyId")String companyId);

    @ApiOperation("根据companyId集合获取公司级别菜单列表")
    @GetMapping("/system/core/sysMenu/getCompanyMenus")
    List<SysMenuDto> getCompanyMenus(@ApiParam @RequestParam(value = "companyId")String companyId);

    /**
     * @Description 根据租户ID合作用户ID获取应用安装菜单
     * @Author xtk
     * @Date 2021/12/3 15:45
     * @param tenantId 租户ID
     * @param userId   用户ID
     * @return java.util.List<SysMenuDto>
     */
    @ApiOperation("根据租户ID或用户ID获取应用安装菜单")
    @GetMapping("/system/core/app/auth/menu/getTenantOrUserAppInstallMenu")
    List<SysMenuDto> getTenantOrUserAppInstallMenu(@RequestParam(name = "tenantId") String tenantId, @RequestParam(name = "userId") String userId);

    /**
     * 获取全部菜单
     * @return
     */
    @GetMapping("/system/core/sysMenu/getAllMenus")
    List<SysMenuDto> getAllMenus();

    /**
     * feign公共上传文件接口
     *  file:"文件",
     *  uploadPath:"文件上传路，非必填",
     *  fileId:"文件主键，非必填",
     *  isConvertOffice:"是否转换pdf，非必填",
     * @return
     */
    @PostMapping(value = "/system/core/sysFile/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    JsonResult uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam(required = false) String uploadPath,
                          @RequestParam(required = false) String fileId, @RequestParam(required = false) boolean isConvertOffice);

}
