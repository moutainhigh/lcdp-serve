package com.redxun.db;

/**
 * 自定义查询配置Bean
 */
public class CustomQueryConfigBean {
		// in查询
		public static String TypeOperateIn = "IN";
		// BETWEEN查询
		public static String TypeOperateBetween = "BETWEEN";
		// 模糊查询
		public static String TypeOperateLike = "LI";
		// 左模糊查询
		public static String TypeOperateLikeLeft = "LL";
		// 右模糊查询
		public static String TypeOperateLikeRight = "LR";
		// 等于
		public static String TypeOperateEqual = "=";
		
		public static String TypeOperateNotEqual = "!=";
		// 大于
		public static String TypeOperateGreater = ">";
		// 大于或等于
		public static String TypeOperateEqualGreater = ">=";
		// 小于
		public static String TypeOperateLess = "<";
		// 小于或等于
		public static String TypeOperateEqualLess = "<=";
}
