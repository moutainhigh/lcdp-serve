package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsRelInst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 关系实例Mapper接口
 * 
 * @author yjy
 * @date 2019-11-08
 */
@Mapper
public interface OsRelInstMapper extends BaseDao<OsRelInst> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsRelInst> findList(Page<OsRelInst> page, @Param("p") Map<String, Object> params);

    /**
     * 通过当前方ID，关联方ID，关系ID，获取关系数据
     * @param party1
     * @param party2
     * @param relTypeId
     * @return
     */
    OsRelInst getByParty1Party2RelTypeId(@Param("party1")String party1,@Param("party2")String party2,@Param("relTypeId")String relTypeId);

    /**
     * 通过关系ID，关联方ID，获取关系数据
     * @param relTypeId
     * @param party2
     * @return
     */
    List<OsRelInst> getByRelTypeIdParty2(@Param("relTypeId")String relTypeId,@Param("party2")String party2);

    /**
     * 通过关系ID，当前方ID，获取关系数据
     * @param relTypeId
     * @param party1
     * @return
     */
    List<OsRelInst> getByRelTypeIdParty1(@Param("tenantId") String tenantId,
                                         @Param("relTypeId")String relTypeId,
                                         @Param("party1")String party1);

    /**
     * 通过用户组ID，关系ID，获取用户关系数据
     * @param page
     * @param params
     * @return
     */
    List<OsRelInst> getUserListByGroupIdAndRelTypeId(Page<OsRelInst> page, @Param("u") Map<String, Object> params);

    /**
     * 通过用户组ID，关系ID，获取用户组关系数据
     * @param page
     * @param params
     * @return
     */
    List<OsRelInst> getGroupListByGroupIdAndRelTypeId(Page<OsRelInst> page, @Param("p") Map<String, Object> params);


    /**
     * 通过当前方ID，关联方ID，关系ID，删除关系
     * @param party1
     * @param party2
     * @param relTypeId
     */
    void delByParty1Party2RelTypeId(@Param("party1")String party1,@Param("party2")String party2,@Param("relTypeId")String relTypeId);

    /**
     * 是否存在于关系中
     * @param party1
     * @param party2
     * @param relTypeId
     * @return
     */
    Long isPartyExistInRelation(@Param("party1")String party1,@Param("party2")String party2,@Param("relTypeId")String relTypeId);


    /**
     * 根据路径与关系id查询关系
     * @return
     */
    List<OsRelInst> getByPathRelTypeId(@Param("relTypeId")String relTypeId,@Param("path")String path);

    /**
     *根据用户Ids
     * @param userId
     */
    int deleteByUserId(String userId);

    /**
     * 通过用户组ID，关系ID，获取用户关系数据
     * @param params
     * @return
     */
    List<OsRelInst> getUserListByGroupId(@Param("relTypeId")String relTypeId,@Param("p")Map<String, Object> params);

    /**
     * 查找某些部门或组下的某个角色
     * @param groupIds
     * @param roleIds
     * @return
     */
    public List getUserIdsByGroupIdsRoleIds(@Param("groupIds") List<String> groupIds,@Param("roleIds") List<String> roleIds);

    /**
     * 查找同时从属于多个用户组下的用户
     * @param groupId1
     * @param groupId2
     * @return
     */
    public List<String> getUserIdsByGroupId1GroupId2(@Param("groupId1") String groupId1,@Param("groupId2") String groupId2);

    /**
     * 根据用户组1与关系类型Key查找用户ID
     * @param party1 用户组ID或用户ID
     * @param relTypeKey 关系标识Key
     * @return
     */
    public List<OsRelInst> getByParty1RelTypeKey(@Param("party1") String party1,@Param("relTypeKey") String relTypeKey);

    /**
     * 根据用户组ID及关系标识Key查找用户ID
     * @param party1 用户组ID或用户ID
     * @param relTypeKey 关系标识Key
     * @return
     */
    public List<String> getUserIdsByParty1RelTypeKey(@Param("party1") String party1,@Param("relTypeKey") String relTypeKey);

    /**
     * 通过当前行政组织部门ID，关联维度ID，用户组关系类型ID获取当前用户组关系
     */
    public OsRelInst getGroupListByGroupIdAndRelTypeIdAndParty2(@Param("p")Map<String, Object> params);

    /**
     * 通过用户组ID，关系ID，获取用户关系数据
     * @param params
     * @return
     */
    List<OsRelInst> getUserListByGroupIdAndRelTypeIdAndDimId(@Param("u") Map<String, Object> params);

    /**
     * 通过用户组ID，关系ID，获取用户关系数据
     * @param page
     * @param params
     * @return
     */
    IPage<OsRelInst> getUserListByGroupIdAndRelTypeIdPage(IPage page, @Param("u") Map<String, Object> params);
}
