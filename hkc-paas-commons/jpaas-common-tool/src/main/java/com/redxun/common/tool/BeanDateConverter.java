package com.redxun.common.tool;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 *
 * <pre>
 * 描述：日期格式的转换处理
 * 构建组：ent-base-core
 * 作者：csx
 * 邮箱:chensx@jee-soft.cn
 * 日期:2014年10月20日-上午11:54:55
 * 广州红迅软件有限公司（http://www.redxun.cn）
 * </pre>
 */
public class BeanDateConverter implements Converter {

    public BeanDateConverter() {
        
    }
    /**
     * 值转化
     * @param type
     * @param value
     * @return 
     */
    @Override
    public Object convert(Class type, Object value) {
        
        if(value==null) {
            return null;
        }
        
        if("java.util.Date".equals( type.getName()) && value instanceof Date){
        	return value;
        }
        
        String dateStr = value.toString();
        if (dateStr.length() > 19) {
            dateStr = dateStr.substring(0, 19);
        }
       
        try {
            return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
