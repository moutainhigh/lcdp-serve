package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程事件的实体基类
 */
@Data
public class Event implements Serializable {
   /**
    * 事件类型
    */
   private  String eventType;


   /**
    * 任务配置。
    */
   private String nodeIds;

   /**
    * 事件配置
    */
   private List<EventConfig> eventConfigs = new ArrayList<>();
}