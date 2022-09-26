package com.redxun.user.org.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.user.org.entity.OsPropertiesDef;
import com.redxun.user.org.entity.OsPropertiesGroup;
import com.redxun.user.org.entity.OsPropertiesVal;
import com.redxun.user.org.mapper.OsPropertiesGroupMapper;
import com.redxun.user.org.mapper.OsPropertiesValMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
* [自定义属性分组]业务服务类
*/
@Service
public class OsPropertiesGroupServiceImpl extends SuperServiceImpl<OsPropertiesGroupMapper, OsPropertiesGroup> implements BaseService<OsPropertiesGroup> {

    @Resource
    private OsPropertiesGroupMapper osPropertiesGroupMapper;
    @Resource
    private OsPropertiesValMapper osPropertiesValMapper;

    @Override
    public BaseDao<OsPropertiesGroup> getRepository() {
        return osPropertiesGroupMapper;
    }

    /**
     * 获取扩展属性组
     * @param dimId 维度id（用户为-1）
     * @param ownerId 所属人
     */
    public List<OsPropertiesGroup> getPropertiesGroups(String dimId,String ownerId,String tenantId) {
        if(StringUtils.isEmpty(tenantId)){
            tenantId=ContextUtil.getCurrentTenantId();
        }
        List<OsPropertiesGroup> propertiesGroups= osPropertiesGroupMapper.getPropertiesGroups(dimId,tenantId);
        if(propertiesGroups.size()>0){
            //根据维度id获取扩展属性的值
            if(StringUtils.isNotEmpty(ownerId)){
                List<OsPropertiesVal> osPropertiesVals = osPropertiesValMapper.getByOwnerId(ownerId);
                for (OsPropertiesVal osPropertiesVal : osPropertiesVals) {
                    for (OsPropertiesGroup propertiesGroup : propertiesGroups) {
                        List<OsPropertiesDef> osPropertiesDefList = propertiesGroup.getOsPropertiesDefList();
                        if(osPropertiesVal.getGroupId().equals(propertiesGroup.getId())){
                            for (OsPropertiesDef osPropertiesDef : osPropertiesDefList) {
                                if(osPropertiesVal.getProperyId().equals(osPropertiesDef.getId())){
                                    //属性值id
                                    osPropertiesDef.setPropertyId(osPropertiesVal.getId());
                                    //根据数据类型存值
                                    String dataType=osPropertiesDef.getDataType();
                                    if("varchar".equals(dataType)){
                                        osPropertiesDef.setPropertyValue(osPropertiesVal.getTxtVal());
                                    }else if("number".equals(dataType)){
                                        osPropertiesDef.setPropertyValue(osPropertiesVal.getNumVal());
                                    }else if("date".equals(dataType)){
                                        osPropertiesDef.setPropertyValue(osPropertiesVal.getDateVal());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return propertiesGroups;
    }
}
