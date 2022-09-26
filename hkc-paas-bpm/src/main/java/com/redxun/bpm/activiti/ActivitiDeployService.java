package com.redxun.bpm.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * Activiti流程发布服务
 */
@Service
public class ActivitiDeployService {

    @Resource
    private RepositoryService repositoryService;

    /**
     * 发布流程定义至Activiti流程库中
     * @param defKey 流程定义Key
     * @param defName 流程定义名称
     * @param xml 流程XML,必须为xml编码
     * @return 流程定义发布Deployment
     */
    public Deployment deploy(String defKey, String defName, String xml){
        if(StringUtils.isEmpty(defName)){
            defName=defKey;
        }
        Deployment deployment=null;
        try {
             deployment = repositoryService.createDeployment()
                    .addBytes("process/" + defKey + ".bpmn20.xml",xml.getBytes("UTF-8")).name(defName).deploy();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return deployment;
    }

    /**
     * 发布流程定义XML
     * @param defKey
     * @param xml
     * @return
     */
    public Deployment deploy(String defKey,String xml){
        return deploy(defKey,null,xml);
    }
}
