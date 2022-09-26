package com.redxun.bpm.activiti.ext;

import com.redxun.bpm.core.service.ConfigCacheUtil;
import com.redxun.cache.CacheUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;

/**
 * 流程定义缓存。
 *
 * @author ray
 */
@Slf4j
public class FlowDefCache  implements DeploymentCache {


    private static  final String REGION="bpm";

    @SneakyThrows
    @Override
    public Object get(String key) {
        if(!this.contains(key)){
            return  null;
        }
        Object o=CacheUtil.get(REGION,key);

//        ProcessDefinitionCacheEntry origin=(ProcessDefinitionCacheEntry)o;
//
//        BpmnModel orignBpmnModel=origin.getBpmnModel();
//
//        ProcessDefinitionCacheEntry entry=new ProcessDefinitionCacheEntry();
//        entry.setProcess(origin.getProcess().clone());
//        entry.setProcessDefinition(origin.getProcessDefinition());
//        entry.setBpmnModel(orignBpmnModel.clone());

        return  o;
    }

    @Override
    public boolean contains(String key) {
        return CacheUtil.isExist(REGION,key);
    }

    @SneakyThrows
    @Override
    public void add(String key, Object o) {
        CacheUtil.set(REGION,key,o);
    }

    @Override
    public void remove(String key) {
        CacheUtil.get(REGION,key);
    }

    @Override
    public void clear() {
        CacheUtil.clear(REGION);
    }

    /**
     * 将流程定义从缓存删除。
     * @param actDefId
     */
    public static void  removeByDefId(String actDefId){
        CacheUtil.remove(REGION,actDefId);
        ConfigCacheUtil.remove(actDefId);
    }
}
