
package com.redxun.form.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.form.core.entity.FormDatasourceShare;
import com.redxun.form.core.mapper.FormDatasourceShareMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * [数据源共享]业务服务类
 */
@Service
public class FormDatasourceShareServiceImpl extends SuperServiceImpl<FormDatasourceShareMapper, FormDatasourceShare> implements BaseService<FormDatasourceShare> {

    @Resource
    private FormDatasourceShareMapper formDatasourceShareMapper;

    @Override
    public BaseDao<FormDatasourceShare> getRepository() {
        return formDatasourceShareMapper;
    }

    /**
    * @Description:  批量插入数据源共享记录
    * @param list 数据源共享记录
    * @Author: Elwin ZHANG  @Date: 2021/12/24 15:33
    **/
    public void batchInsert( List<FormDatasourceShare> list){
        if(list==null || list.size()==0){
            return;
        }
        for (FormDatasourceShare share : list ) {
            formDatasourceShareMapper.insert(share);
        }
    }

    /**
    * @Description:  删除某个数据源相关共享
    * @param dsId 数据源ID
    * @Author: Elwin ZHANG  @Date: 2021/12/24 15:27
    **/
    public void deleteByDataSourceId(String dsId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("DS_ID_",dsId);
        formDatasourceShareMapper.delete(wrapper);
    }
    /**
    * @Description:  根据数据源ID查找数据源共享记录
    * @param dsId 数据源ID
    * @return java.util.List<com.redxun.form.core.entity.FormDatasourceShare>
    * @Author: Elwin ZHANG  @Date: 2021/12/24 14:11
    **/
    public List<FormDatasourceShare> getByDataSourceId(String dsId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("DS_ID_",dsId);
        return formDatasourceShareMapper.selectList(wrapper);
    }
    /**
    * @Description:  根据应用ID查找数据源共享记录
    * @param appId 应用ID
    * @return java.util.List<com.redxun.form.core.entity.FormDatasourceShare>
    * @Author: Elwin ZHANG  @Date: 2021/12/24 13:59
    **/
    public List<FormDatasourceShare> getByAppId(String appId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("APP_ID_",appId);
        return formDatasourceShareMapper.selectList(wrapper);
    }

}
