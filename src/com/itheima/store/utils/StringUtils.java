package com.itheima.store.utils;
/**
 * 判断字符串是否为空的工具类
 * @author 趴布里克
 *
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		/*boolean flag = false;
		if(str==null || "".equals(str)) {
			flag = true;
		}
		return flag;*/
		
		return str==null||"".equals(str);
	}
}
