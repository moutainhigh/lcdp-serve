package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统分类树
 数据库访问层
 */
@Mapper
public interface SysMenuMapper extends BaseDao<SysMenu> {
    /**
     * 查询系统所有菜单（含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuAll();

    /**
     * 通过应用ID获取菜单
     * @param appId
     * @return
     */
    public List<SysMenu> getMenusByAppId(@Param("appId") String appId);

    /**
     * 根据菜单类型获取。
     * @param menuType
     * @return
     */
    List<SysMenu> getMenusByType(@Param("menuType") String menuType);

    /**
     * 获取菜单权限
     * @return
     */
    public List<SysMenu> selectAllMenusAndPerms();

    /**
     * 根据角色ID查询菜单
     * @param groupId
     * @return 权限列表
     */
    public List<SysMenu> selectMenuIdsByGroupId(String groupId);

    /**
     * 查询系统正常显示菜单（不含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuNormalAll();

    /**
     * 查询系统正常显示菜单（含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuAndPermsNormalAll();

    /**
     * 根据菜单ID集合与菜单类型集合获取菜单信息
     * @param menuIds
     * @param menuType
     * @return
     */
    public List<SysMenu> getMenusByIdsAndType(@Param("menuIds") String menuIds, @Param("menuType") String menuType);





    List<SysMenu> getMenusByIds(@Param("menuIds") String menuIds);


    /**
     * 新增菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

    /**
     * 根据父菜单id获取子菜单
     * @param menuId
     * @return
     */
    List<SysMenu> getChildrenList(@Param("menuId") String menuId);

    List<SysMenu> getInterfaceByParent(@Param("ParentId") String ParentId);

    List<SysMenu> getInterface();

    /**
     * 通过应用ID获取菜单
     * @param appId
     * @return
     */
    List<SysMenu> listMenusByAppId(@Param("appId") String appId);



    /**
     * 获取所有授权菜单列表
     *
     * @param tenantId
     * @param companyId
     * @param subCompanyIds
     * @param menuIds
     * @return
     */
    List<SysMenu> getAllGrantMenus(@Param(value = "tenantId") String tenantId,
                                   @Param(value = "companyId") String companyId,
                                   @Param(value = "subCompanyIds") String subCompanyIds,
                                   @Param(value = "menuIds") String menuIds);



}
