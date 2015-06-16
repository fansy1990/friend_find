/**
 * 
 */
package com.fz.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import com.fz.service.DBService;

/**
 * Spring容器启动完成后，执行数据库表插入逻辑
 * 初始化登录表
 * @author fansy
 * @date 2015-6-15
 */
@Repository
public class ApplicationListenerImpl implements ApplicationListener<ApplicationEvent> {

	@Resource
	private DBService dBService;
	@Override
	public void onApplicationEvent(ApplicationEvent  arg0) {
		if(dBService.getTableData("LoginUser")){
			System.out.println(new java.util.Date()+"：登录表有数据，不需要初始化！");
		}else{
			dBService.insertLoginUser();
			System.out.println(new java.util.Date()+"：初始化登录表完成！");
		}
	}
	/**
	 * @return the dBService
	 */
	public DBService getdBService() {
		return dBService;
	}
	/**
	 * @param dBService the dBService to set
	 */
	public void setdBService(DBService dBService) {
		this.dBService = dBService;
	}

}
