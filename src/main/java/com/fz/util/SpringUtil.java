package com.fz.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * spring bean 获取类
 * 
 * @author fansy
 * 
 */
public class SpringUtil extends SpringBeanAutowiringSupport {
	private static ApplicationContext ac = null;

	private static ApplicationContext getContext() {
		if (ac == null) {
			ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		}
		return ac;
	}

	public static Object getBean(String name) {
		return getContext().getBean(name);
	}

	@Autowired
	private static BeanFactory beanFactory;

	private static SpringUtil instance;
	static {
		// 静态块，初始化实例
		instance = new SpringUtil();
	}

	private SpringUtil(){}
	public static Object getBeanById(String beanId) {
		return beanFactory.getBean(beanId);
	}

	public static SpringUtil getInstance() {
		return instance;
	}
}
