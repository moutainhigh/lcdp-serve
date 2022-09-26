package com.redxun.user.org.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsPropertiesDef;
import com.redxun.user.org.entity.OsPropertiesGroup;
import com.redxun.user.org.entity.OsPropertiesVal;
import com.redxun.user.org.mapper.OsPropertiesValMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * [扩展属性值]业务服务类
 */
@Service
public class OsPropertiesValServiceImpl extends SuperServiceImpl<OsPropertiesValMapper, OsPropertiesVal> implements BaseService<OsPropertiesVal> {

    @Resource
    private OsPropertiesValMapper osPropertiesValMapper;

    @Override
    public BaseDao<OsPropertiesVal> getRepository() {
        return osPropertiesValMapper;
    }

    /**
     * @param propertiesGroups
     * @param ownerId
     */
    public void saveProPerty(List<OsPropertiesGroup> propertiesGroups, String ownerId) {
        for (OsPropertiesGroup propertiesGroup : propertiesGroups) {
            List<OsPropertiesDef> osPropertiesDefList = propertiesGroup.getOsPropertiesDefList();
            for (OsPropertiesDef osPropertiesDef : osPropertiesDefList) {
                OsPropertiesVal osPropertiesVal = new OsPropertiesVal();
                osPropertiesVal.setDimId(propertiesGroup.getDimId());
                osPropertiesVal.setGroupId(propertiesGroup.getId());
                osPropertiesVal.setOwnerId(ownerId);
                osPropertiesVal.setProperyId(osPropertiesDef.getId());

                if ("varchar".equals(osPropertiesDef.getDataType())) {
                    osPropertiesVal.setTxtVal((String) osPropertiesDef.getPropertyValue());
                } else if ("number".equals(osPropertiesDef.getDataType())) {
                    handNumber(osPropertiesDef, osPropertiesVal);

                } else if ("date".equals(osPropertiesDef.getDataType())) {
                    handDate(osPropertiesDef, osPropertiesVal);
                }
                //有值id为更新
                if (StringUtils.isNotEmpty(osPropertiesDef.getPropertyId())) {
                    osPropertiesVal.setId(osPropertiesDef.getPropertyId());
                    osPropertiesValMapper.updateById(osPropertiesVal);
                } else {
                    insert(osPropertiesVal);
                }

            }
        }
    }

    private void handNumber(OsPropertiesDef osPropertiesDef, OsPropertiesVal osPropertiesVal) {
        double num;
        if (BeanUtil.isNotEmpty(osPropertiesDef.getPropertyValue())) {
            if (osPropertiesDef.getPropertyValue() instanceof String) {
                num = (Double.parseDouble((String) osPropertiesDef.getPropertyValue()));
            } else {
                int propertyValue = (Integer) osPropertiesDef.getPropertyValue();
                num = (double) propertyValue;
            }
            osPropertiesVal.setNumVal(num);
        }else {
            osPropertiesVal.setNumVal(null);
        }
    }

    private void handDate(OsPropertiesDef osPropertiesDef, OsPropertiesVal osPropertiesVal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            if (BeanUtil.isNotEmpty(osPropertiesDef.getPropertyValue())) {
                if (osPropertiesDef.getPropertyValue() instanceof String) {
                    date = sdf.parse((String) osPropertiesDef.getPropertyValue());
                    osPropertiesVal.setDateVal(date);
                } else {
                    date = sdf.parse(sdf.format(osPropertiesDef.getPropertyValue()));
                    osPropertiesVal.setDateVal(date);
                }
            }else {
                osPropertiesVal.setDateVal(null);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void delByDefId(String[] defIds) {
        for (String defId : defIds) {
            osPropertiesValMapper.deleteByDefId(defId);
        }
    }

    public List<OsPropertiesVal> getByOwnerId(String ownerId){
        return osPropertiesValMapper.getByOwnerId(ownerId);
    }

    //根据条件获取所属人id
    public List<String> getOwnerIdsByCondition(String dimId, String condition) {
       return osPropertiesValMapper.getOwnerIdsByCondition(dimId,condition);
    }
}
