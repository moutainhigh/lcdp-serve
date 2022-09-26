
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.MobileTag;
import com.redxun.bpm.core.mapper.MobileTagMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * <pre> 
 * 描述：记录CID和机型 处理接口
 * 作者:mansan
 * 日期:2017-11-29 22:29:36
 * 版权：广州红迅软件
 * </pre>
 */
@Service
public class MobileTagServiceImpl extends SuperServiceImpl<MobileTagMapper, MobileTag> implements BaseService<MobileTag> {

	@Resource
	private MobileTagMapper mobileTagMapper;

	@Override
	public BaseDao<MobileTag> getRepository() {
		return mobileTagMapper;
	}

	public MobileTag getByCid(String cid){
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.eq("CID_",cid);
		return mobileTagMapper.selectOne(wrapper);
	}

	public Integer getCount(String cid,String mobileType){
		QueryWrapper wrapper=new QueryWrapper();
		wrapper.eq("CID_",cid);
		wrapper.eq("MOBILE_TYPE_",mobileType);
		return mobileTagMapper.selectCount(wrapper);
	}
}
