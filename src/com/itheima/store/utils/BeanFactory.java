package com.itheima.store.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 工厂 + 反射 + 配置文件
 * 创建实体类
 * @author 趴布里克
 *
 */
public class BeanFactory {
	public static Object getBean(String id) {
		//解析配置文件
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml"));
			Element element = (Element) document.selectSingleNode("//bean[@id='"+id+"']");
			String value = element.attributeValue("class");
			//System.out.println(value);
			//反射创建对象
			return Class.forName(value).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		BeanFactory.getBean("userDao");
	}
}
