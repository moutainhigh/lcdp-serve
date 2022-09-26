package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户组Mapper接口
 *
 * @author yjy
 * @date 2019-11-08
 */
@Mapper
public interface OsGroupMapper extends BaseDao<OsGroup> {

    /**
     * 根据组织id列表查询数据
     * 逻辑删除
     * @param groupIds
     * @return
     */
    List<OsGroup> getByGroupIds(@Param("groupIds") List<String> groupIds,@Param("deleted") String deleted);


    /**
     * 根据组织ID查询用户组
     * @param groupId
     * @return
     */
    OsGroup getByGroupId(@Param("groupId") String groupId);

    /**
     * 根据路径查询列表
     * @param path
     * @return
     */
    List<OsGroup> getByLikePath(@Param("path")String path);


    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsGroup> findList(Page<OsGroup> page, @Param("p") Map<String, Object> params);

    /**
     * 通过父Id获取用户组
     * @param parentId
     * @return
     */
    List<OsGroup> getByParentId(@Param("parentId")String parentId,@Param("initRankLevel")Integer initRankLevel);

    /**
     * 通过维度ID,用户ID,关系ID,是否主关系,机构ID来获取用户组
     * @param dimId
     * @param userId
     * @param relTypeId
     * @param isMain
     * @param tenantId
     * @return
     */
    List<OsGroup> getByDimIdUserIdRelTypeIdIsMain(@Param("dimId")String dimId,
                                                  @Param("userId")String userId,
                                                  @Param("relTypeId")String relTypeId,
                                                  @Param("isMain")String isMain,
                                                  @Param("tenantId")String tenantId);

    /**
     * 通过关联方ID,关系ID,机构ID来获取用户组
     * @param party2
     * @param relType
     * @param tenantId
     * @return
     */
    List<OsGroup> getByParty2RelType(@Param("party2")String party2,@Param("relType")String relType,@Param("tenantId")String tenantId);

    /**
     * 通过用户ID,关系ID来获取用户组
     * @param params
     * @return
     */
    List<OsGroup> getByUserIdRelTypeId(@Param("params") Map<String, Object> params);

    /**
     * 通过维度ID，用户ID，关系ID来获取用户组
     * @param params
     * @return
     */
    List<OsGroup> getByDimIdUserIdRelTypeId(@Param("params") Map<String, Object> params);

    List<OsGroup> getByDimIdAdmin(@Param("tenantId")String tenantId, @Param("dimId")String dimId,@Param("userId") String userId);

    /**
     * 根据用户查找授权的角色列表。
     * @param tenantId
     * @param dimId
     * @param userId
     * @param companyId
     * @return
     */
    List<OsGroup> getByDimIdRole(@Param("tenantId")String tenantId,
                                 @Param("dimId")String dimId,
                                 @Param("userId")String userId,
                                 @Param("companyId")String companyId);

    List<OsGroup> getByDimId(@Param("tenantId")String tenantId,@Param("dimId")String dimId);


    /**
     * 根据userId获取主部门
     * @param userId
     * @return
     */
    OsGroup getMainGroup(@Param("userId")String userId,@Param("tenantId")String tenantId);

    /**
     * 查看指定用户组的列表
     * @param path
     * @param initRankLevel
     * @return
     */
    List<OsGroup> getOsGroupByGroupId(@Param("path")String path,@Param("initRankLevel")Integer initRankLevel);

    /**
     * 获取排除指定用户组ID列表
     * @param groupId
     * @return
     */
    List<OsGroup> getOsGroupByExcludeGroupId(@Param("groupId")String groupId,
                                             @Param("dimId")String dimId,
                                             @Param("parentId")String parentId,
                                             @Param("tenantId") String tenantId);

    /**
     * 查询组织列表
     * @param dimId 维度id
     * @param parentId 父节点id
     * @return
     */
    List<OsGroup> queryGroups(
            @Param("tenantId") String tenantId,
            @Param("dimId")String dimId,
            @Param("parentId")String parentId);


    /**
     * 查询行政组织列表
     *
     * @param tenantId   租户id
     * @param dimId      维度id
     * @param orgId      组织id
     * @param parentId   父组织id
     * @return
     */
    List<OsGroup> queryOrgs(
            @Param("tenantId") String tenantId,
            @Param("dimId")String dimId,
            @Param("orgId")String orgId,
            @Param("parentId")String parentId);


    /**
     * 通过父Id获取用户组与用户
     * @param parentId
     * @return
     */
    List<OsGroup> getGroupAndUserByParentId(@Param("parentId")String parentId, @Param("dimId")String dimId,@Param("tenantId")String tenantId);


    /**
     * 获取顶级组织。
     * @param tenantId
     * @param dimId
     * @param parentId
     * @return
     */
    List<OsGroup> getTopGroup(@Param("tenantId")String tenantId,@Param("dimId")String dimId,@Param("parentId")String parentId);

    /**
     * 通过关系定义主键获取关联
     * @param relTypeId
     * @return
     */
    List<OsGroup> getOsGroupsByRelTypeId(@Param("relTypeId")String relTypeId);

    /**
     * 根据维度Id与用户组Key或者用户组名称返回用户组Id
     * @param
     * @return
     */
    List<OsGroup> getByDimIdAndKeyOrName(@Param("u") Map<String, Object> params);



    /**
     * 获取平铺的数据。
     * 逻辑删除
     * @param tenantId      租户ID
     * @param dimId         维度ID
     * @param companyId     公司ID
     * @return
     */
    List<OsGroup>  getByFlatCompany(@Param("tenantId")String tenantId,@Param("dimId")String dimId,@Param("companyId")String companyId,@Param("deleted")String deleted);


    /**
     * 获取树形的公司数据。
     * 逻辑删除
     * @param tenantId
     * @param dimId
     * @param parentId
     * @param companyId
     * @return
     */
    List<OsGroup>  getByTreeCompany(@Param("tenantId")String tenantId,@Param("dimId")String dimId,
                                    @Param("parentId")String parentId,@Param("companyId")String companyId,@Param("deleted")String deleted);



}
