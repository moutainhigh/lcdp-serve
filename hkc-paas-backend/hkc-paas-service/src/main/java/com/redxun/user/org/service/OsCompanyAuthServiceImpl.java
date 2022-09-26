
package com.redxun.user.org.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.user.org.entity.OsCompanyAuth;
import com.redxun.user.org.mapper.OsCompanyAuthMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* [分公司授权表]业务服务类
*/
@Service
public class OsCompanyAuthServiceImpl extends SuperServiceImpl<OsCompanyAuthMapper, OsCompanyAuth> implements BaseService<OsCompanyAuth> {

    @Resource
    private OsCompanyAuthMapper osCompanyAuthMapper;

    @Override
    public BaseDao<OsCompanyAuth> getRepository() {
        return osCompanyAuthMapper;
    }

    /**
     * 根据公司ID获取公司授权角色数据。
     * @param companyId
     * @return
     */
    public List<OsCompanyAuth> getByCompanyId(String companyId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq(CommonConstant.COMPANY_ID,companyId);
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.eq("DELETED_","0");
        }
        List<OsCompanyAuth> list = osCompanyAuthMapper.selectList(wrapper);
        return list;
    }

    /**
     * 保存授权信息
     * @param json {companyId:"",groups:[{id:"",name:""}]}
     * @return
     */
    @Transactional
    public JsonResult saveAuth(JSONObject json){
        String companyId=json.getString("companyId");
        JSONArray array=json.getJSONArray("groups");
        for(int i=0;i<array.size();i++){
            JSONObject obj=array.getJSONObject(i);
            OsCompanyAuth auth=new OsCompanyAuth();
            String groupId=obj.getString("id");
            String name=obj.getString("name");
            auth.setGroupId(groupId).setGroupName(name)
                    .setCompanyId(companyId);

            boolean isExist=isExist(groupId,companyId);
            if(isExist){
                continue;
            }
            this.insert(auth);
        }
        return JsonResult.Success("保存公司授权成功");
    }

    /**
     * 判断是否存在。
     * @param groupId
     * @param companyId
     * @return
     */
    private boolean isExist(String groupId,String companyId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("COMPANY_ID_",companyId);
        wrapper.eq("GROUP_ID_",groupId);
        Integer count= osCompanyAuthMapper.selectCount(wrapper);
        return count>0;
    }

}
