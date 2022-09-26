package com.redxun.system.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SysOffice工具类。
 *
 */
@Slf4j
public class SysOfficeUtil {
	public static final String OPEN_OFFICE_CONFIG="openOfficeConfig";

	public static String formatDate(Date date, String format){
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
}
