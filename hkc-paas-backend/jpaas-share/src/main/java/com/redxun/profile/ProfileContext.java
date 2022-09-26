package com.redxun.profile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 用户与用户组策略上下文
 */
@Service
public class ProfileContext {

    @Autowired
    private  static   final Map<String, IProfileService> PROFILE_MAP = new ConcurrentHashMap<>();

    public ProfileContext(Map<String, IProfileService> map) {
        ProfileContext.PROFILE_MAP.clear();
        map.forEach((k, v)-> ProfileContext.PROFILE_MAP.put(v.getType().getType(), v));
    }

    /**
     * 获取处理器列表。
     * @return
     */
    public  static Collection<IProfileService> getListHandler(){
        return  PROFILE_MAP.values();
    }

    /**
     * 获取处理器列表。
     * @return
     */
    public  static List<ProfileType> getProfileList(){
        List<ProfileType> list=new ArrayList<>();
        for(IProfileService profileService : PROFILE_MAP.values()){
            list.add(profileService.getType());
        }
        return list;
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IProfileService getHandler(String type){
        IProfileService attrHandler= PROFILE_MAP.get(type);
        return  attrHandler;
    }

    /**
     * 获取当前人的身份信息。
     * @return
     */
    public static Map<String, Set<String>> getCurrentProfile(){
        Map<String,Set<String>> map=new HashMap<String, Set<String>>();
        for(IProfileService service: PROFILE_MAP.values()){
            Set<String> set=service.getCurrentProfile();
            if(BeanUtil.isEmpty(set)){
                continue;
            }
            map.put(service.getType().getType(), set);
        }
        return map;
    }

    /**
     * 获取处理器列表。
     * @return
     */
    public  static JSONArray getOsUserGroupHanderList(){
        JSONArray list = new JSONArray();
        for(IProfileService service: PROFILE_MAP.values()){
            JSONObject userOrGroup =new JSONObject();
            userOrGroup.put("type",service.getType().getType());
            userOrGroup.put("name",service.getType().getName());
            userOrGroup.put("values","");
            userOrGroup.put("names","");
            list.add(userOrGroup);
        }
        return list;
    }
}
