package com.redxun.bpm.core.entity;

import com.redxun.dto.form.BpmView;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 启动流程时对象数据结构。
 */
@Getter
@Setter
public class FormData {



    List<BpmView> formData;
}
