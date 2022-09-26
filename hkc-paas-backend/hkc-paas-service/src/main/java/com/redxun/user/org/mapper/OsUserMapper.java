package com.redxun.user.org.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户表 Mapper 接口
 *
 * @author yjy
 * @data 2018-10-29
 */
@Mapper
public interface OsUserMapper extends BaseDao<OsUser> {
    /**
     * 分页查询用户列表
     * @param page
     * @param params
     * @return
     */
    List<OsUser> findList(Page<OsUser> page, @Param("u") Map<String, Object> params);

    /**
     * 通过用户组ID，关系ID获取用户数据
     * @param groupId
     * @param relTypeId
     * @return
     */
    List<OsUser> getListByGroupIdAndRelTypeId(@Param("groupId")String groupId,@Param("relTypeId")String relTypeId);

    /**
     * 按用户组Id与其他条件传入查找用户列表，用户组Id若不传入，则按所有的用户组查询
     * @param page
     * @param params
     * @return
     */
    IPage<OsUser> searchByBelongGroupId(IPage page, @Param("w") Map<String, Object> params);

    /**
     *
     * @param userId
     * @param quitTime 离职时间
     */
    void setStatusAndQuitTime(@Param("userId")String userId,@Param("status")String status,@Param("quitTime") Date quitTime);

    /**
     * 根据账号获取用户对象。
     * @param userNo
     * @return
     */
    OsUser getByUsername(@Param("userNo")String userNo);

    /**
     * 根据手机号获取用户对象。
     * @param mobile
     * @return
     */
    OsUser getByMobile(@Param("mobile")String mobile);

    /**
     * 根据openId获取用户对象。
     * @param openId
     * @return
     */
    OsUser getByOpenId(@Param("openId")String openId);

    /**
     * 根据租户获取所有的租户管理员。
     * @param tenantId
     * @return
     */
    List<OsUser> getAdmin(@Param("tenantId")String tenantId);

    /**
     * 根据用户ID 租户ID获取用户数据。
     * @param userId
     * @return
     */
    OsUser getById(@Param("userId")String userId);

    /**
     * 更新默认登陆机构
     * @param params
     */
    void updateTenantIdFromDomain(Map<String, Object> params);

    /**
     * 获取离没有修改状态且并离职的用户
     * @return
     */
    List<OsUser> getQuitTimeUsers(@Param("quitTime") Date quitTime);

    /**
     * 根据账号获取用户对象。
     * @param userNo
     * @return
     */
    OsUser getByUsernameAndTenantId(@Param("userNo")String userNo, @Param("tenantId")String tenantId);


    /**
     * 通过组id获取该组以及子组的所有用户
     * @return
     */
    IPage<OsUser> getAllUserbyGroupId(IPage page,@Param("w") Map<String, String> params);

    /**
     * 通过组id获取该组以及子组的所有用户
     */
    int updatePassword(@Param("pwd")String pwd,@Param("updateTime") Date updateTime,@Param("updateUserId")String updateUserId,@Param("userId")String userId);

    /**
     * 根据openId和平台类型获取用户
     * @param openId
     * @param platformType
     * @param tenantId
     * @return
     */
    OsUser getUserByOpenId(@Param("openId")String openId,@Param("platformType")Integer platformType,@Param("tenantId")String tenantId);

    /**
     * 获取当前最大的序号
     * @return
     */
    OsUser getMaxSn();

}
