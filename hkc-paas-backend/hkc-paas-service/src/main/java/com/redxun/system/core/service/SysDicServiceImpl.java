package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.sys.SysDicDto;
import com.redxun.system.core.entity.SysDic;
import com.redxun.system.core.mapper.SysDicMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* 数字字典业务服务类
*/
@Service
@Slf4j
public class SysDicServiceImpl extends SuperServiceImpl<SysDicMapper, SysDic> implements BaseService<SysDic> {

    @Resource
    private SysDicMapper sysDicMapper;

    @Resource
    private SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Override
    public BaseDao<SysDic> getRepository() {
        return sysDicMapper;
    }

    /**
     * 缓存区域
     */
    public static String REGION_SYSDIC="sysDic";

    /**
     * 获取缓存KEY.
     * @param key
     * @return
     */
    public String getDicKey(String key){
        return  REGION_SYSDIC + key;
    }

    public void removeCache(String key){
        //清除缓存
        CacheUtil.remove(REGION_SYSDIC,getDicKey(key));
    }
    /**
     * 获取某一分类下的所有分类
     * @param treeId
     * @return
     */
    public List<SysDic> getByTreeId(String treeId){
        String cacheKey=getDicKey(treeId);
        Object obj= CacheUtil.get(REGION_SYSDIC,cacheKey);
        if(obj!=null){
            return (List<SysDic>) obj;
        }
        List<SysDic> dicList = sysDicMapper.getByTreeId(treeId);
        if(dicList!=null){
            CacheUtil.set(REGION_SYSDIC,cacheKey,dicList);
        }
        return dicList;
    }

    /**
     * 获取某一分类下的所有数字字典
     * @param treeId
     * @return
     */
    public List<SysDicDto> getTopDicByTreeId(String treeId){
        List<SysDic> list= sysDicMapper.getTopDicByTreeId(treeId);
        List<SysDicDto> rtnList=new ArrayList<>();
        list.forEach(p->{
            if(p.getChildAmount()>0){
                p.setChildren(new ArrayList<>());
            }
            SysDicDto dicDto=new SysDicDto();
            BeanUtil.copyProperties(dicDto,p);
            rtnList.add(dicDto);
        });
        return rtnList;
    }

    /**
     * 取得某父类下所有的分类数字项
     * @param parentId
     * @return
     */
    public List<SysDic> getByParentId(String parentId){
        List<SysDic> list= sysDicMapper.getByParentId(parentId);
        list.forEach(p->{
            if(p.getChildAmount()>0){
                p.setChildren(new ArrayList<>());
            }
        });
        return list;
    }

    /**
     * 按路径取得某一项下下级的所有数字项
     * @param path
     * @return
     */
    public List<SysDic> getByLeftLikePath(String path){
        return sysDicMapper.getByLikePath(path+"%");
    }

    /**
     * 按树路径删除所有下下级的所有数字项
     * @param path
     */
    public void delByLeftPath(String path){
        sysDicMapper.delByLeftPath(path + "%");
    }

    /**
     * 级联删除某一个字典及其下下级所有的字典项
     * @param dicId
     */
    public void delCascade(String dicId){
        SysDic sysDic=get(dicId);
        if(StringUtils.isNotEmpty(sysDic.getPath())) {
            delByLeftPath(sysDic.getPath());

        }
        //若对于树节点的路径为空，同时删除本身
        delete(dicId);
    }

    public  SysDic getByDicId(String dicId){
        SysDic dic=sysDicMapper.getById(dicId);
        if(dic.getChildAmount()>0){
            dic.setChildren(new ArrayList<>());
        }
        return  dic;
    }


    /**
     * 名字不重复。
     * @param dic
     * @return
     */
    public boolean isNameExist(SysDic dic){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("VALUE_",dic.getValue());
        wrapper.eq("TREE_ID_",dic.getTreeId());
        if(StringUtils.isNotEmpty(dic.getDicId())){
            wrapper.ne("DIC_ID_",dic.getDicId());
        }
        Integer rtn= sysDicMapper.selectCount(wrapper);
        return  rtn>0;
    }

    /**
     * 值不重复。
     * @param dic
     * @return
     */
    public boolean isValueExist(SysDic dic){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("VALUE_",dic.getValue());
        wrapper.eq("TREE_ID_",dic.getTreeId());
        if(StringUtils.isNotEmpty(dic.getDicId())){
            wrapper.ne("DIC_ID_",dic.getDicId());
        }
        Integer rtn= sysDicMapper.selectCount(wrapper);
        return  rtn>0;
    }

    /**
     * 根据id与父id查询数据字典
     * @param treeId
     * @param parentId
     * @return
     */
    public List<SysDic> getByPidAndDicId(String treeId, String parentId) {
        return sysDicMapper.getByPidAndDicId(treeId,parentId);
    }

    public void importDic(MultipartFile file, String treeId,String appId) {
        StringBuilder sb=new StringBuilder();
        sb.append("导入字典数据:");

        JSONArray sysDicArray  = sysInvokeScriptService.readZipFile(file);

        for (Object obj:sysDicArray) {
            JSONObject DicObj = (JSONObject)obj;
            JSONObject sysDic = DicObj.getJSONObject("sysDic");
            if(BeanUtil.isEmpty(sysDic)){
                continue;
            }
            String sysDicStr = sysDic.toJSONString();
            SysDic sysNewDic = JSONObject.parseObject(sysDicStr,SysDic.class);

            sb.append(sysNewDic.getName() +"("+sysNewDic.getDicId()+"),");
            sysNewDic.setAppId(appId);
            sysNewDic.setTreeId(treeId);
            String id = sysNewDic.getDicId();
            SysDic oldDic = get(id);
            if(BeanUtil.isNotEmpty(oldDic)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldDic.getAppId())){
                    update(sysNewDic);
                }else{
                    String newId=IdGenerator.getIdStr();
                    sysNewDic.setDicId(newId);
                    String newPath=sysNewDic.getPath().replace(id,newId);
                    sysNewDic.setPath(newPath);
                    insert(sysNewDic);
                }
                update(sysNewDic);
            }
            else{
                insert(sysNewDic);
            }

        }

        sb.append(",导入到分类:"+ treeId);

    }
    /**
     * 根据id与父id、项名查询数据字典
     * @param treeId
     * @param parentId
     * @return
     */
    public List<SysDic> getByPidAndName(String treeId, String parentId, String name) {
        return sysDicMapper.getByPidAndName(treeId,parentId,name);
    }

    public List<SysDic> getDicByDicValue(String treeId, String dicValue) {
        List<String> dicValueList=new ArrayList<>();
        if(StringUtils.isNotEmpty(dicValue)){
            dicValueList= Arrays.asList(dicValue.split(","));
        }
        return sysDicMapper.getDicByDicValue(treeId,dicValueList);
    }


    public List<SysDic> getByTreeIdParentId(String treeId, String parentId) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TREE_ID_",treeId);
        if(StringUtils.isNotEmpty(parentId)){
            wrapper.eq("PARENT_ID_",parentId);
        }
        return sysDicMapper.selectList(wrapper);
    }
}


