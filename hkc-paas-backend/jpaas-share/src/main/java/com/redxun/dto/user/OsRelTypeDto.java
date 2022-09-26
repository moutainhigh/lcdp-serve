package com.redxun.dto.user;

import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 关系类型DTO
 * 参考 jpaas-user的OsRelType类
 */
@Getter
@Setter
public class OsRelTypeDto extends BaseDto {
  private String id;

  /** 关系名 */
  private String name;

  /** 关系业务主键 */
  private String key;

  /** 关系类型。用户关系=USER-USER；用户组关系=GROUP-GROUP；用户与组关系=USER-GROUP；组与用户关系=GROUP-USER */
  private String relType;

  /** 关系约束类型。1对1=one2one；1对多=one2many；多对1=many2one；多对多=many2many */
  private String constType;

  /** 关系当前方名称 */
  private String party1;

  /** 关系关联方名称 */
  private String party2;

  /** 当前方维度ID（仅对用户组关系） */
  private String dimId1;

  /** 关联方维度ID（用户关系忽略此值） */
  private String dimId2;

  /** 等级 */
  private Long level;

  /** 状态。actived 已激活；locked 锁定；deleted 已删除 */
  private String status;

  /** 是否系统预设 */
  private String isSystem;

  /** 是否默认 */
  private String isDefault;

  /** 是否是双向 */
  private String isTwoWay;

  /** 关系备注 */
  private String memo;
}
