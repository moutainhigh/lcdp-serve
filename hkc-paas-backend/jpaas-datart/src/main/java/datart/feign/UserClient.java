package datart.feign;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.common.model.JPaasUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户的Feign客户端
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE)
public interface UserClient {
    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @GetMapping(value = "user/org/osUser/findByUsername", params = "username")
    JPaasUser findByUsername(@RequestParam("username") String username);


    @GetMapping(value = "user/org/osUser/getLoginUserInfoByToken", params = "token")
    JsonResult<JPaasUser> getLoginUserInfoByToken(@RequestParam("token") String token);

    /**
     * 根据用户名跟租户ID获取用户信息
     * @param username
     * @param tenantId
     * @return
     */
    @GetMapping(value = "user/org/osUser/findByUsernameAndTenantId", params={"username","tenantId"})
    JPaasUser findByUsernameAndTenantId(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    @GetMapping(value = "user/org/osUser/getUserById", params = "userId")
    JPaasUser getUserById(@RequestParam("userId") String userId);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "user/org/osUser/users-anon/mobile", params = "mobile")
    JPaasUser findByMobile(@RequestParam("mobile") String mobile);


    /**
     * 根据OpenID获取用户信息
     * @param tenantId
     * @param openId
     * @return
     */
    @GetMapping(value="wxent/getuser",params={"tenantId","openId"})
    JPaasUser findByOpenId( @RequestParam("tenantId") String tenantId, @RequestParam("openId")String openId);

    /**
     * 根据租户ID与钉钉ID获取用户信息
     * @param tenantId
     * @param ddId
     * @return
     */
    @GetMapping(value="dd/getuser",params={"tenantId","ddId"})
    JPaasUser findByDdId( @RequestParam("tenantId") String tenantId, @RequestParam("ddId")String ddId);

    /**
     * 重置用户密码
     * @param username  用户名
     * @param password  需要重置的密码
     * @return
     */
    @GetMapping(value="/user/org/osUser/resetPassword",params="userId")
    JsonResult resetPassword( @RequestParam("username")String username,  @RequestParam("password")String password);

    /**
     * 按机构域名查找租户Id
     * @param domain
     * @return
     */
    @GetMapping(value = "user/org/osInst/findInstIdByDomain", params = "domain")
    String findTenanIdByDomain(@RequestParam("domain") String domain);

    /**
     * 按机构ID及机构编码查找机构ID
     * @param instNo
     * @return
     */
    @GetMapping(value = "user/org/osInst/findInstIdByInstNo", params = "instNo")
    String findTenanIdByInstNo(@RequestParam("instNo") String instNo);


}
