



package com.redxun.bpm.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <pre>
 *  
 * 描述：记录CID和机型实体类定义
 * 作者：mansan
 * 邮箱: keitch@redxun.com
 * 日期:2017-11-29 22:29:36
 * 版权：广州红迅软件
 * </pre>
 */
@Getter
@Setter
@TableName(value = "BPM_MOBILE_TAG")
public class MobileTag extends BaseExtEntity<String> {

	@TableId(value = "TAGID_",type = IdType.INPUT)
	private String tagid;

	@TableField(value = "CID_")
	private String cid;

	@TableField(value = "MOBILE_TYPE_")
	private String mobileType;

	@TableField(value = "ISBAN_")
	private String isban;

	@TableField(value = "USER_ID_")
	private String userId;

	@TableField(value = "TAG_")
	private String tag;

	@Override
	public String getPkId() {
		return tagid;
	}

	@Override
	public void setPkId(String pkId) {
		this.tagid=pkId;
	}

	@JsonCreator
	public MobileTag() {
	}



}



