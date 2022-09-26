package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsMsgboxBox;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
 * INS_MSGBOX_BOX_DEF数据库访问层
 */
@Mapper
public interface InsMsgboxBoxMapper extends BaseDao<InsMsgboxBox> {
    List<InsMsgboxBox> queryByBoxId(String boxId);

    void delByBoxId(String boxId);
}
