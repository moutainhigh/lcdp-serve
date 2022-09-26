
package com.redxun.common.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;


/**
 * 
 * @author mansan
 * @Email chshxuan@163.com
 * @Copyright (c) 2014-2020 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
public class BeanUtil {

	static Logger logger= LoggerFactory.getLogger(BeanUtil.class);

	private final static String CHILDREN="children";

	/**
	 * BeanUtil类型转换器
	 */
	public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

	private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());

	static {
		convertUtilsBean.register(new BeanDateConverter(), Date.class);
		convertUtilsBean.register(new LongConverter(null), Long.class);
	}


	/**
	 * 获取参数不为空值
	 *
	 * @param value defaultValue 要判断的value
	 * @return value 返回值
	 */
	public static <T> T nvl(T value, T defaultValue)
	{
		return value != null ? value : defaultValue;
	}


	public static <T> List copyList(List<T> list, Class destClass) {
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList();
		}
		return JSON.parseArray(JSON.toJSONString(list), destClass);
	}

	/**
	 * 拷贝一个bean中的非空属性于另一个bean中
	 * 
	 * @param dest
	 *            目标bean
	 * @param orig
	 *            源bean
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	public static void copyNotNullProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
		BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass().getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				// Need to check isReadable() for WrapDynaBean
				// (see Jira issue# BEANUTILS-61)
				if (beanUtils.getPropertyUtils().isReadable(orig, name) && beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					beanUtils.copyProperty(dest, name, value);
				}
			}
		} else if (orig instanceof Map) {
			Iterator entries = ((Map) orig).entrySet().iterator();
			while (entries.hasNext()) {
				Entry entry = (Entry) entries.next();
				String name = (String) entry.getKey();
				if (beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					beanUtils.copyProperty(dest, name, entry.getValue());
				}
			}
		} else /* if (orig is a standard JavaBean) */{
			PropertyDescriptor[] origDescriptors = beanUtils.getPropertyUtils().getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				if (beanUtils.getPropertyUtils().isReadable(orig, name) && beanUtils.getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = beanUtils.getPropertyUtils().getSimpleProperty(orig, name);
						if (value != null) {
							if (value instanceof HashSet) {
								HashSet valMap = (HashSet) value;
								if (valMap.size() > 0) {
									beanUtils.copyProperty(dest, name, value);
								}
							} else {
								beanUtils.copyProperty(dest, name, value);
							}
						}
					} catch (NoSuchMethodException e) {
						// Should not happen
					}
				}
			}
		}

	}

	public static void copyProperties(Object dest, Object orig) {
		try {
			beanUtilsBean.copyProperties(dest, orig);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			logger.error(e.getMessage());
		}
	}

	public static void copyProperty(Object bean, String name, Object value) {
		try {
			beanUtilsBean.copyProperty(bean, name, value);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			logger.error(e.getMessage());
		}
	}

	/**
	 * 取得能转化类型的bean
	 * 
	 * @return
	 */
	public static BeanUtilsBean getBeanUtils() {
		BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
		return beanUtilsBean;
	}

	/**
	 * 通过Map转化为entity
	 * 
	 * @param entity
	 * @param dataMap
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 *             Object
	 * @exception
	 * @since 1.0.0
	 */
	public static Object populateEntity(Object entity, Map<String, Object> dataMap) throws IllegalAccessException, InvocationTargetException {
		getBeanUtils().populate(entity, dataMap);
		return entity;
	}

	/**
	 * 对一个bean进行深度复制，所有的属性节点全部会被复制
	 * 
	 * @param source
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T deepCopyBean(T source) {
		if (source == null) {
			return null;
		}
		try {
			if (source instanceof Collection) {
				return (T) deepCopyCollection((Collection) source);
			}
			if (source.getClass().isArray()) {
				return (T) deepCopyArray(source);
			}
			if (source instanceof Map) {
				return (T) deepCopyMap((Map) source);
			}
			if (source instanceof Date) {
				return (T) new Date(((Date) source).getTime());
			}
			if (source instanceof Timestamp) {
				return (T) new Timestamp(((Timestamp) source).getTime());
			}
			// 基本类型直接返回原值
			if (source.getClass().isPrimitive() || source instanceof String || source instanceof Boolean || Number.class.isAssignableFrom(source.getClass())) {
				return source;
			}
			if (source instanceof StringBuilder) {
				return (T) new StringBuilder(source.toString());
			}
			if (source instanceof StringBuffer) {
				return (T) new StringBuffer(source.toString());
			}
			Object dest = source.getClass().newInstance();
			BeanUtilsBean bean = BeanUtilsBean.getInstance();
			PropertyDescriptor[] origDescriptors = bean.getPropertyUtils().getPropertyDescriptors(source);
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue;
				}

				if (bean.getPropertyUtils().isReadable(source, name) && bean.getPropertyUtils().isWriteable(dest, name)) {
					try {
						Object value = deepCopyBean(bean.getPropertyUtils().getSimpleProperty(source, name));
						bean.getPropertyUtils().setSimpleProperty(dest, name, value);
					} catch (NoSuchMethodException e) {
					}
				}
			}
			return (T) dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Collection deepCopyCollection(Collection source) throws InstantiationException, IllegalAccessException {
		Collection dest = source.getClass().newInstance();
		for (Object o : source) {
			dest.add(deepCopyBean(o));
		}
		return dest;
	}

	private static Object deepCopyArray(Object source) throws InstantiationException, IllegalAccessException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
		int length = Array.getLength(source);
		Object dest = Array.newInstance(source.getClass().getComponentType(), length);
		if (length == 0) {
			return dest;
		}
		for (int i = 0; i < length; i++) {
			Array.set(dest, i, deepCopyBean(Array.get(source, i)));
		}
		return dest;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map deepCopyMap(Map source) throws InstantiationException, IllegalAccessException {
		Map dest = source.getClass().newInstance();
		for (Object o : source.entrySet()) {
			Entry e = (Entry) o;
			dest.put(deepCopyBean(e.getKey()), deepCopyBean(e.getValue()));
		}
		return dest;
	}

	/**
	 * 把实体类中的所有声明的字段及值转为Map
	 * 
	 * @param entity
	 *            实体对象
	 * @return Map<String,Object>
	 * @exception
	 * @since 1.0.0
	 */
	public static Map<String, Object> convertFieldToMap(Object entity) {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		Class<?> cls = entity.getClass();
		for (; cls != Object.class; cls = cls.getSuperclass()) {
			Field[] fs = cls.getDeclaredFields();
			for (Field f : fs) {
				try {
					Method m = cls.getDeclaredMethod("get" + StringUtils.makeFirstLetterUpperCase(f.getName()));
					Object fieldVal = m.invoke(entity);
					fieldMap.put(f.getName(), fieldVal);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fieldMap;
	}

	/**
	 * 从实体对象中获取属性值
	 * 
	 * @param obj
	 * @param attName
	 * @return Object
	 * @exception
	 * @since 1.0.0
	 */
	public static Object getFieldValueFromObject(Object obj, String attName) {
		if (obj == null)
			return null;
		Object val = null;
		try {
			Method getMethod = obj.getClass().getDeclaredMethod("get" + StringUtils.makeFirstLetterUpperCase(attName));
			if(getMethod==null){
				return null;
			}
			val = getMethod.invoke(obj);
		} catch (Exception e) {
			//logger.warn(e.getMessage());
		}
		return val;
	}

	/**
	 * 设置字段值
	 * 
	 * @param instObj
	 * @param attName
	 * @param val
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public static void setFieldValue(Object instObj, String attName, Object val) {
		if (instObj == null) {
			return;
		}

		Class<?> cls = instObj.getClass();
		Field field = null;
		Method setMethod = null;
		for (; cls != Object.class; cls = cls.getSuperclass()) {
			try {
				setMethod= getSetMethod(cls,attName);
				if(setMethod!=null){
					setMethod.invoke(instObj, val);
					break;
				}
				
				field = cls.getDeclaredField(attName);
				String attr=StringUtils.makeFirstLetterUpperCase(attName);
				if (field != null) {
					setMethod = cls.getDeclaredMethod("set" + attr, field.getType());
					setMethod.invoke(instObj, val);
					break;
				}
				
			} catch (Exception e) {
				//logger.error(e.getMessage());
			}
		}
	}
	
	private static Method getSetMethod(Class cls, String attName){
		String attr=StringUtils.makeFirstLetterUpperCase(attName);
		Method[] aryMethod= cls.getDeclaredMethods();
		for(Method m : aryMethod){
			if( m.getName().equals("set" + attr)){
				return m;
			}
		}
		return null;
	}
	
	/**
	 * 可以用于判断 Map,Collection,String,Array,Long是否为空
	 * 
	 * @param o
	 *            java.lang.Object.
	 * @return boolean.
	 */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Object o) {
		if (o == null){
			return true;
		}

		if (o instanceof String) {
			return ((String) o).trim().length() == 0;
		} else if (o instanceof Collection) {
			return ((Collection<?>) o).size() == 0;
		} else if (o.getClass().isArray()) {
			return ((Object[]) o).length == 0;
		} else if (o instanceof Map) {
			return ((Map<?, ?>) o).size() == 0;
		}
		return false;

	}

	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	
	/**
	 * 判断对象是否为数字
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isNumber(Object o) {
		if (o == null){
			return false;
		}

		if (o instanceof Number){
			return true;
		}

		if (o instanceof String) {
			try {
				Double.parseDouble((String) o);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 根据指定的类名判定指定的类是否存在。
	 * 
	 * @param className
	 * @return
	 */
	public static boolean validClass(String className) {
		try {
			Class.forName(className);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * 判定类是否继承自父类
	 * 
	 * @param cls
	 *            子类
	 * @param parentClass
	 *            父类
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean isInherit(Class cls, Class parentClass) {
		return parentClass.isAssignableFrom(cls);
	}
	
	/**
	 * 将数据进行转型。
	 * @param typeName 
	 * 可能的值为：
	 * int,
	 * short,
	 * long,
	 * float,
	 * double,
	 * boolean,
	 * String
	 * @param value
	 * @return
	 */
	public static Object convertByActType(String typeName,String value){
		Object o = null;
		if ("int".equals(typeName)) {
			o = Integer.parseInt(value);
		} else if ("short".equals( typeName)) {
			o = Short.parseShort(value);
		} else if ("long".equals( typeName)) {
			o = Long.parseLong(value);
		} else if ("float".equals( typeName)) {
			o = Float.parseFloat(value);
		} else if ("double".equals( typeName)) {
			o = Double.parseDouble(value);
		} else if ("boolean".equals( typeName)) {
			o = Boolean.parseBoolean(value);
		} else if ("String".equals( typeName)) {
			o = value;
		} else if ("Properites".equals(typeName)){
		    Properties properties=new Properties();
            String[] ary=value.split(";");
            for(String obj:ary){
                String[] config=obj.split("=");
                properties.setProperty(config[0],config[1]);
            }
		    o=properties;
        }
		else{
			o=value;
		}
		return o;
	}
	


	
	/**
	 * 获取类的方法。
	 * @param cls			类
	 * @param methodName	方法名
	 * @param parameters	参数数组
	 * @return
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> cls, String methodName, Object[] parameters) throws NoSuchMethodException {
		Method[] methods =cls.getDeclaredMethods();
		for(Method method:methods){
			Class<?>[] parameterTypes = method.getParameterTypes();
			int len=parameters==null?0:parameters.length;
			if(methodName.equals(method.getName()) && parameterTypes.length==len){
				return method;
			}
		}
		throw new NoSuchMethodException();
	}


	/**
	 * @描述 list数据转Tree，大多使用在前台json中。
	 * @说明 实现接口 Tree即可
	 * @扩展 可通过反射获取id,pid，目前只提供Tree接口排序的实现
	 */
	public static <T  extends Tree> List<T> listToTree(List<T> list){
		Map<String, T> tempMap = new LinkedHashMap<>();
		if(BeanUtil.isEmpty(list) ) {
			return Collections.emptyList();
		}
		if(!(list.get(0) instanceof Tree)) {
			throw new RuntimeException("树形转换出现异常。数据必须实现Tree接口！");
		}

		List<T> returnList = new ArrayList<T>();
		for(T tree : (List<T>)list){
			tempMap.put(tree.getId(),tree);
		}

		for(T obj : (List<T>)list){
			String parentId = obj.getParentId();
			if(tempMap.containsKey(parentId) && !obj.getId().equals(parentId)){
				T parent= tempMap.get(parentId);
				if(parent.getChildren()==null){
					parent.setChildren(new ArrayList());
				}
				parent.getChildren().add(obj);
			}else{
				returnList.add((T) obj);
			}
		}

		return returnList;
	}

	/**
	 * 将list 数据转换成 树形结构的数据。
	 * @param array
	 * @param pkField
	 * @param parenField
	 * @return
	 */
	public static JSONArray arrayToTree(JSONArray array,String pkField,String parenField){
		Map<String, JSONObject> tempMap = new LinkedHashMap<>();
		for(int i=0;i<array.size();i++){
			JSONObject json=array.getJSONObject(i);
			String pk=json.getString(pkField);
			tempMap.put(pk,json);
		}
		JSONArray returnAry = new JSONArray();
		for(int i=0;i<array.size();i++){
			JSONObject json=array.getJSONObject(i);
			String id=json.getString(pkField);
			String parentId = json.getString(parenField);
			if(tempMap.containsKey(parentId) && !id.equals(parentId)){
				JSONObject parent= tempMap.get(parentId);
				if(!parent.containsKey(CHILDREN)){
					parent.put(CHILDREN,new JSONArray());
				}
				JSONArray children=parent.getJSONArray(CHILDREN);
				children.add(json);
			}else{
				returnAry.add(json);
			}
		}
		return returnAry;
	}

	public static void main(String[] args) {

		String str="[{id:\"1\",pid:\"0\",name:\"a\"},{id:\"2\",pid:\"1\",name:\"a\"}," +
				"{id:\"3\",pid:\"0\",name:\"a\"},{id:\"4\",pid:\"3\",name:\"a\"},{id:\"5\",pid:\"3\",name:\"a\"}]";

		JSONArray ary= JSONArray.parseArray(str);
		JSONArray rtn=arrayToTree(ary,"id","pid");
		System.err.println(rtn.size());

	}
}