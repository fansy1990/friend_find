package com.fz.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * spring bean 获取类
 * @author fansy
 *
 */
public class SpringUtil {
	private static ApplicationContext ac =null;
	private static ApplicationContext getContext(){
		if(ac==null){
			ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		return ac;
	}
	public static Object getBean(String name){
		return getContext().getBean(name);
	}
	
}
