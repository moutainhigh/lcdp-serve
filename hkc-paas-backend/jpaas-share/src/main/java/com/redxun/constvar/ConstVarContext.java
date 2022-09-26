package com.redxun.constvar;

import com.redxun.common.base.entity.KeyValEnt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文常量。
 * @author ray
 *
 */
@Service
public class ConstVarContext {

	@Autowired
	private  static   final Map<String, IConstVarService> HAND_LER_MAP = new ConcurrentHashMap<>();


	public ConstVarContext(Map<String, IConstVarService> map) {
		ConstVarContext.HAND_LER_MAP.clear();
		map.forEach((k, v)-> ConstVarContext.HAND_LER_MAP.put(v.getType().getKey(), v));
	}


	/**
	 * 返回值。
	 * @param key
	 * @return
	 */
	public Object getValByKey(String key,Map<String,Object> vars){
		if(HAND_LER_MAP.containsKey(key)){
			return HAND_LER_MAP.get(key).getValue(vars);
		}
		return null;
	}

	/**
	 * 消息类型。
	 * @return
	 */
	public  static List<ConstVarType> getTypes(){
		List<ConstVarType> list=new ArrayList<>();
		HAND_LER_MAP.values().forEach(p->{
			list.add(p.getType());
		});
		return list;
	}


	/**
	 * 返回处理器。
	 * @return
	 */
	public List<KeyValEnt> getHandlers(){
		List<KeyValEnt> list=new ArrayList<KeyValEnt>();
		HAND_LER_MAP.values().forEach(p->{
			list.add(new KeyValEnt(p.getType().getKey(), p.getType().getName()));
		});
		return list;
	}

}
