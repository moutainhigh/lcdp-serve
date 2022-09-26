
package com.redxun.form.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.form.DataResult;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.core.entity.FormBusInstData;
import com.redxun.form.core.entity.FormBusinessSolution;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.mapper.FormBusInstDataMapper;
import com.redxun.form.core.mapper.FormBusinessSolutionMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

/**
* [表单业务方案]业务服务类
*/
@Service
public class FormBusinessSolutionServiceImpl extends SuperServiceImpl<FormBusinessSolutionMapper, FormBusinessSolution> implements BaseService<FormBusinessSolution> {

    @Resource
    private FormBusinessSolutionMapper formBusinessSolutionMapper;
    @Resource
    private FormBusInstDataMapper formBusInstDataMapper;
    @Resource
    FormBusInstDataServiceImpl formBusInstDataService;
    @Resource
    FormSolutionServiceImpl formSolutionService;


    @Override
    public BaseDao<FormBusinessSolution> getRepository() {
        return formBusinessSolutionMapper;
    }

    /**
     * @param jsonObject
     * {
     *     "busSolutionId" -> "业务方案id"
     *      "data" -> [{
     *          setting:{alias:"表单方案名称",action:"动作 startFlow:启动流程,save:保存数据"},
     *           //表单数据
     *           data:{}
     *        }]
     *      // save为暂存 submit为提交数据
     *      "action" -> "save"
     *      "mainPk" -> "主表主键值"
     * }
     * @param dataResult
     */
    @GlobalTransactional
    public void saveInitData(JSONObject jsonObject, DataResult dataResult,String pk) {
        String action = jsonObject.getString("action");
        String mainPk = jsonObject.getString("mainPk");
        String busSolutionId = jsonObject.getString("busSolutionId");
        if(StringUtils.isEmpty(pk)){
            pk=dataResult.getPk();
        }
        //数据动作
        String dataAction = dataResult.getAction();
        String solutionAlias = dataResult.getSolutionAlias();
        FormSolution formSolution = formSolutionService.getByAlias(solutionAlias);
        String status="0";
        //submit为提交数据 状态更新为生效
        if("submit".equals(action)){
            status="1";
        }
        //新增
        if("add".equals(dataAction)){
            FormBusInstData formBusInstData=new FormBusInstData();
            formBusInstData.setBusSolId(busSolutionId);
            formBusInstData.setMainPk(mainPk);
            formBusInstData.setRelFormsolId(formSolution.getId());
            formBusInstData.setRelPk(pk);
            formBusInstData.setStatus(status);
            formBusInstDataService.insert(formBusInstData);
        }else {
            //提交则将状态改为1
            if("submit".equals(action)){
                QueryWrapper queryWrapper=new QueryWrapper();
                queryWrapper.eq("BUS_SOL_ID_",busSolutionId);
                queryWrapper.eq("REL_PK_",pk);
                FormBusInstData formBusInstData = formBusInstDataMapper.selectOne(queryWrapper);
                if("0".equals(formBusInstData.getStatus())){
                    formBusInstData.setStatus(status);
                    formBusInstDataMapper.updateById(formBusInstData);
                }
            }
        }
    }

    /**
     *
     * @param jsonObject
     * {
     *     busSolId:"表单方案Id",
     *     id:"删除的id"
     *     rows:[] 选中的数据 用于获取非ID_值
     * }
     * @return
     */
    public JsonResult delByIds(JSONObject jsonObject) {
        String busSolId = jsonObject.getString("busSolId");
        JSONArray rows = jsonObject.getJSONArray("rows");
        String idStr = jsonObject.getString("id");
        String[] deleteIds = idStr.split(",");
        FormBusinessSolution formBusinessSolution = formBusinessSolutionMapper.selectById(busSolId);
        if(BeanUtil.isEmpty(formBusinessSolution)){
            return new JsonResult().setSuccess(false).setMessage("删除失败,未找到业务方案!");
        }
        String mainFormSolution = formBusinessSolution.getMainFormSolution();
        if(StringUtils.isEmpty(mainFormSolution)){
            return new JsonResult().setSuccess(false).setMessage("删除失败,未配置主表单方案!");
        }
        String formSolutionStr = formBusinessSolution.getFormSolutions();
        JSONArray formSolutions = JSON.parseArray(formSolutionStr);
        String fieldName = formSolutions.getJSONObject(0).getString("fieldName");
        if(StringUtils.isEmpty(fieldName)){
            fieldName="ID_";
        }
        List resultList=new ArrayList();
        JSONArray data=new JSONArray();
        for (int i = 0; i < rows.size(); i++) {
            JSONObject row = rows.getJSONObject(i);
            String pk = row.getString(fieldName);
            String id = deleteIds[i];
            List<FormBusInstData> list = formBusInstDataMapper.getDataByMainPk(busSolId,pk);
            if(list.size()==0) {
                JSONObject mainJson=new JSONObject();
                mainJson.put("pk",pk);
                mainJson.put("id",id);
                mainJson.put("relSolAlias",JSONObject.parseObject(mainFormSolution).getString("value"));
                data.add(mainJson);
                resultList.add(new JsonResult().setSuccess(false).setMessage("无关联的数据!"));
                continue;
            }
            for (FormBusInstData formBusInstData : list) {
                JSONObject jsonObj_=new JSONObject();
                String relSolAlias = formBusInstData.getRelFormsolAlias();
                //主表单关联数据 则直接获取当前行的
                if(formBusInstData.getRelPk().equals(formBusInstData.getMainPk())){
                    jsonObj_.put("pk",pk);
                    jsonObj_.put("id",id);
                    jsonObj_.put("relSolAlias",relSolAlias);
                    jsonObj_.put("initDataId",formBusInstData.getId());
                    data.add(jsonObj_);
                }else {
                    //获取每个关联表单的数据
                    jsonObj_ = getInstData(relSolAlias, formSolutions,formBusInstData.getRelPk());
                    jsonObj_.put("initDataId",formBusInstData.getId());
                    if(BeanUtil.isNotEmpty(jsonObj_)){
                        data.add(jsonObj_);
                    }
                }
            }
        }
        for (int i = 0; i < data.size(); i++) {
            JSONObject dataObj = data.getJSONObject(i);
            JSONObject jsonObj=new JSONObject();
            jsonObj.put("alias",dataObj.getString("relSolAlias"));
            jsonObj.put("id",dataObj.getString("id"));
            String initDataId = dataObj.getString("initDataId");
            JsonResult jsonResult =formSolutionService.removeData(jsonObj);
            if(jsonResult.isSuccess() && StringUtils.isNotEmpty(initDataId)){
                formBusInstDataMapper.deleteById(initDataId);
            }
            resultList.add(jsonResult);
        }
        return new JsonResult().setSuccess(true).setMessage("删除成功!").setData(resultList);
    }

    private JSONObject  getInstData(String relSolAlias,JSONArray formSolutions,String relPk){
        JSONObject data=new JSONObject();
        for (int i = 0; i < formSolutions.size(); i++) {
            JSONObject jsonObject = formSolutions.getJSONObject(i);
            String formSolution = jsonObject.getString("formSolution");
            String relField = jsonObject.getString("relField");
            if(StringUtils.isEmpty(formSolution)){
                continue;
            }
            JSONObject formSolutionObj = JSONObject.parseObject(formSolution);
            String solAlias = formSolutionObj.getString("value");
            if(relSolAlias.equals(solAlias)){
                //无主键方式
                if(!"ID_".equals(relField)){
                    JSONArray jsonArray=new JSONArray();
                    JSONObject jsonObject_=new JSONObject();
                    jsonObject_.put("valueDef",relPk);
                    jsonObject_.put("name",relField);
                    jsonObject_.put("valueSource","fixedVar");
                    jsonArray.add(jsonObject_);
                    JSONObject record=formSolutionService.getData(solAlias,"",jsonArray);
                    data.put("pk",record.getString(relField));
                    data.put("id",record.getString("ID_"));
                    data.put("relSolAlias",solAlias);
                    break;
                }else {
                    data.put("pk",relPk);
                    data.put("id",relPk);
                    data.put("relSolAlias",solAlias);
                    break;
                }
            }
        }
        return data;
    }
}
