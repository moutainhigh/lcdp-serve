package com.redxun.user.org.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.redxun.common.tool.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户导出实例
 * @author yjy
 */
@Getter
@Setter
@Accessors(chain = true)
public class SysUserExcel implements Serializable {
    private static final long serialVersionUID = -5886012896705137070L;

    @Excel(name = "用户姓名", width = 30, isImportField = "true_st")
    private String username;

    @Excel(name = "用户昵称", width = 30, isImportField = "true_st")
    private String nickname;

    @Excel(name = "手机号码", width = 30, isImportField = "true_st")
    private String mobile;

    @Excel(name = "性别", replace = { "男_0", "女_1" }, isImportField = "true_st")
    private Integer sex;

    @Excel(name = "创建时间", format = CommonConstant.DATETIME_FORMAT, isImportField = "true_st", width = 20)
    private Date createTime;

    @Excel(name = "修改时间", format = CommonConstant.DATETIME_FORMAT, isImportField = "true_st", width = 20)
    private Date updateTime;
}
