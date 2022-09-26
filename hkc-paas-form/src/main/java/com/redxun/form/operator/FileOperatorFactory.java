package com.redxun.form.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileOperatorFactory {

	@Autowired
	private final Map<String ,IFileOperator> fileOperatorMap= new ConcurrentHashMap<>();
	
	/**
	 * 根据类型获取文件操作对象。
	 * @param type
	 * @return
	 */
	public IFileOperator getByType(String type){
		if(fileOperatorMap.containsKey(type)){
			return fileOperatorMap.get(type);
		}
		return null;
	}
	

}
