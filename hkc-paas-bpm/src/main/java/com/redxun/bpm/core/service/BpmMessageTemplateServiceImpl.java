
package com.redxun.bpm.core.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmDefaultTemplate;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmMessageTemplate;
import com.redxun.bpm.core.mapper.BpmMessageTemplateMapper;
import com.redxun.common.tool.BeanUtil;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;

/**
* [bpm_message_template]业务服务类
*/
@Service
public class BpmMessageTemplateServiceImpl extends SuperServiceImpl<BpmMessageTemplateMapper, BpmMessageTemplate> implements BaseService<BpmMessageTemplate> {

    @Resource
    private BpmMessageTemplateMapper bpmMessageTemplateMapper;
    @Resource
    private BpmDefaultTemplateServiceImpl bpmDefaultTemplateService;

    @Override
    public BaseDao<BpmMessageTemplate> getRepository() {
        return bpmMessageTemplateMapper;
    }

    /**
     * 获取所有的模板。
     * @param defId
     * @param nodeId
     * @return
     */
    public List<BpmMessageTemplate> getByDefAndNodeId(String defId,String nodeId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.select("ID_","MSG_TYPE_","TEMPLATE_TYPE_","BO_ALIAS_","BPM_DEF_ID_");
        wrapper.eq("BPM_DEF_ID_",defId);
        wrapper.eq("NODE_ID_",nodeId);
        List<BpmMessageTemplate> templateList = bpmMessageTemplateMapper.selectList(wrapper);

        List< BpmDefaultTemplate> defaultTemplates= bpmDefaultTemplateService.getAll();

        Map<String, BpmMessageTemplate> templateMap = templateList.stream().collect(Collectors.toMap(item -> {
            return item.getTemplateType() + "-" + item.getMsgType();
        }, p -> p));

        for(BpmDefaultTemplate template:defaultTemplates){
            String key=template.getTemplateType() +"-" + template.getMessageType();

            BpmMessageTemplate messageTemplate=templateMap.get(key);
            if(messageTemplate==null){
                messageTemplate=new BpmMessageTemplate();

                messageTemplate.setTemplateType(template.getTemplateType());
                messageTemplate.setMsgType(template.getMessageType());
                messageTemplate.setIsDefault(MBoolean.YES.val);
                messageTemplate.setId(template.getId());
                templateList.add(messageTemplate);
            }
        }
        return templateList;
    }


    /**
     * 保存模板。
     * @param template
     * @return
     */
    public JsonResult saveTemplate(BpmMessageTemplate template){
        boolean isExist=isExists(template);
        if(!isExist){
            this.insert(template);
            return JsonResult.Success("成功新增模板!");
        }
        else{
            BpmMessageTemplate bpmMessageTemplate= getByTemplate(template);
            bpmMessageTemplate.setTemplate(template.getTemplate());
            bpmMessageTemplate.setBoAlias(template.getBoAlias());
            this.update(bpmMessageTemplate);
            return JsonResult.Success("成功更新模板!");
        }

    }

    /**
     * 是否存在。
     * @param template
     * @return
     */
    private boolean isExists(BpmMessageTemplate template){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BPM_DEF_ID_",template.getBpmDefId());
        wrapper.eq("NODE_ID_",template.getNodeId());
        wrapper.eq("TEMPLATE_TYPE_",template.getTemplateType());
        wrapper.eq("MSG_TYPE_",template.getMsgType());
        return bpmMessageTemplateMapper.selectCount(wrapper) >0;
    }


    private BpmMessageTemplate getByTemplate(BpmMessageTemplate template){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BPM_DEF_ID_",template.getBpmDefId());
        wrapper.eq("NODE_ID_",template.getNodeId());
        wrapper.eq("TEMPLATE_TYPE_",template.getTemplateType());
        wrapper.eq("MSG_TYPE_",template.getMsgType());
        return bpmMessageTemplateMapper.selectOne(wrapper) ;
    }


    private List<BpmMessageTemplate> getByNodeId(String defId,String nodeId,String templateType){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BPM_DEF_ID_",defId);
        wrapper.eq("NODE_ID_",nodeId);
        wrapper.eq("TEMPLATE_TYPE_",templateType);
        List<BpmMessageTemplate> list = bpmMessageTemplateMapper.selectList(wrapper);
        return list;
    }

    /**
     * 获取流程消息模板。
     * <pre>
     *     1.获取节点的模板
     *     2.获取全局的模板
     *     3.获取默认的模板
     * </pre>
     * @param defId             流程定义ID
     * @param nodeId            节点ID
     * @param templateType      模板类型
     * @param messageType       消息类型使用逗号分隔
     * @return
     */
    public Map<String,String> getByBpmNode(String defId,String nodeId,String templateType,String messageType){
        //取节点的模板。
        List<BpmMessageTemplate> nodeList=getByNodeId(defId,nodeId,templateType);

        String[] messageTypeAry=messageType.split(",");
        List<String> typeList=new ArrayList<>();
        for(String type: messageTypeAry){
            typeList.add(type);
        }

        Map<String,String> templateMap=new HashMap<>();

        for(BpmMessageTemplate template:nodeList){
            typeList.remove(template.getMsgType());
            templateMap.put(template.getMsgType(),template.getTemplate());
        }
        //如果还有没有找到的模板
        if(BeanUtil.isNotEmpty(typeList)){
            //获取全局的模板
            nodeList=getByNodeId(defId,"-1",templateType);

            for(BpmMessageTemplate template:nodeList){
                typeList.remove(template.getMsgType());
                templateMap.put(template.getMsgType(),template.getTemplate());
            }
        }

        //如果类型为空则返回
        if(BeanUtil.isEmpty(typeList) ){
            return  templateMap;
        }
        //如果获取不到则获取默认的模板。
        List<BpmDefaultTemplate> defaultTemplates= bpmDefaultTemplateService.getByTemplateType(templateType);
        for (BpmDefaultTemplate defaultTemplate:defaultTemplates){
            if(typeList.contains(defaultTemplate.getMessageType())){
                templateMap.put(defaultTemplate.getMessageType(),defaultTemplate.getTemplate());
            }
        }
        return templateMap;

    }


}
