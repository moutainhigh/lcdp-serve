package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.portal.core.entity.InsNews;
import com.redxun.portal.core.mapper.InsNewsMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* [信息公告]业务服务类
*/
@Service
public class InsNewsServiceImpl extends SuperServiceImpl<InsNewsMapper, InsNews> implements BaseService<InsNews> {

    @Resource
    private InsNewsMapper insNewsMapper;

    @Override
    public BaseDao<InsNews> getRepository() {
        return insNewsMapper;
    }

    /**
     * 获得栏目里的新闻列表(文字列表)
     * @param sysDicNew
     * @return
     */
    public List<InsNews> getBySysDicNew(IPage page,String sysDicNew){
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("sysDicNew", sysDicNew);
        return insNewsMapper.getBySysDicNew(page,params);
    }


}
