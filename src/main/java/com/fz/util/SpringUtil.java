package com.fz.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring bean 获取类
 * 
 * @author fansy
 * 
 */
@Component
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext ac = null;
	

	@Override
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		this.ac=arg0;
		
	}
	
	public synchronized static Object getBean(String name){
		return ac.getBean(name);
	}
}
