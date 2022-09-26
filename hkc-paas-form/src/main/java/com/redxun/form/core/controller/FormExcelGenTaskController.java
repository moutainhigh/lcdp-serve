
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.dto.sys.SysFileDto;
import com.redxun.feign.SysFileClient;
import com.redxun.form.core.entity.FormDownloadRecord;
import com.redxun.form.core.entity.FormExcelGenTask;
import com.redxun.form.core.entity.FormExecuteLog;
import com.redxun.form.core.service.FormDownloadRecordServiceImpl;
import com.redxun.form.core.service.FormExcelGenTaskServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.service.FormExecuteLogServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formExcelGenTask")
@Api(tags = "Excel生成任务")
@ClassDefine(title = "Excel生成任务", alias = "FormExcelGenTaskController", path = "/form/core/formExcelGenTask", packages = "core", packageName = "子系统名称")

public class FormExcelGenTaskController extends BaseController<FormExcelGenTask> {

    @Autowired
    FormExcelGenTaskServiceImpl formExcelGenTaskService;
    @Autowired
    SysFileClient sysFileClient;
    @Autowired
    FormDownloadRecordServiceImpl formDownloadRecordService;

    @Override
    public BaseService getBaseService() {
        return formExcelGenTaskService;
    }

    @Override
    public String getComment() {
        return "Excel生成任务";
    }

    @MethodDefine(title = "下载excel记录", path = "/downloadRecord", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "genTaskId")})
    @ApiOperation(value = "下载excel记录")
    @AuditLog(operation = "下载excel记录")
    @PostMapping("/downloadRecord")
    public void downloadRecord(@ApiParam @RequestParam(value = "genTaskId") String genTaskId) throws Exception {
        if(StringUtils.isEmpty(genTaskId)){
            return;
        }
        IUser user = ContextUtil.getCurrentUser();
        FormExcelGenTask formExcelGenTask = formExcelGenTaskService.get(genTaskId);
        FormDownloadRecord formDownloadRecord=new FormDownloadRecord();
        formDownloadRecord.setId(IdGenerator.getIdStr());
        formDownloadRecord.setGenRecord(genTaskId);
        formDownloadRecord.setListId(formExcelGenTask.getListId());
        formDownloadRecord.setListName(formExcelGenTask.getListName());
        formDownloadRecord.setCreateByName(user.getFullName());
        formDownloadRecordService.save(formDownloadRecord);
    }

    @MethodDefine(title = "更新生成任务", path = "/updateExcelGenTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "genTaskId")})
    @ApiOperation(value = "更新生成任务")
    @AuditLog(operation = "更新生成任务")
    @PostMapping("/updateExcelGenTask")
    public void updateExcelGenTask(@RequestBody JSONObject jsonObject)  {
        FormExcelGenTask formExcelGenTask = JSONObject.parseObject(jsonObject.toJSONString(), FormExcelGenTask.class);
        formExcelGenTaskService.update(formExcelGenTask);
    }

    /**
     *
     * @param list
     * @return
     */
    @Override
    protected JsonResult beforeRemove(List<String> list) {
        List<String> fileIds=new ArrayList<>();
        for (String id : list) {
            FormExcelGenTask formExcelGenTask = formExcelGenTaskService.get(id);
            //文件的ID
            String fileId = formExcelGenTask.getFileId();
            if(StringUtils.isEmpty(fileId)){
                continue;
            }
            //删除下载记录
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("GEN_RECORD_", id);
            formDownloadRecordService.remove(queryWrapper);
            fileIds.add(fileId);
        }
        sysFileClient.delByFileId(fileIds);
        return JsonResult.Success();
    }
}

